package com.sucy.skill.manager;

import com.sucy.skill.SkillAPI;
import it.unimi.dsi.fastutil.Hash;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.inventivetalent.glow.GlowAPI;

import java.util.*;

public class GlowManager {
    public class PlayerVisionData{
        public Player player;
        public HashMap<UUID, Long> entityGlowTimeRemainMap = new HashMap<>();
        public PlayerVisionData(Player player){
            this.player = player;
        }
        public void setEntityGlowTimeRemain(Entity entity, long ticks){
            setEntityGlowTimeRemain(entity.getUniqueId(), ticks);
        }
        public void setEntityGlowTimeRemain(UUID entityUUID, long ticks){
            entityGlowTimeRemainMap.put(entityUUID, ticks);
        }
        public long getEntityGlowTimeRemain(Entity entity){
            return getEntityGlowTimeRemain(entity.getUniqueId());
        }
        public long getEntityGlowTimeRemain(UUID entityUUID){
            return entityGlowTimeRemainMap.get(entityUUID);
        }
        //call this function every ticks
        public void updateTick(long loopPeriod){
            List<UUID> keySet = new ArrayList<UUID>(entityGlowTimeRemainMap.keySet());
            for(UUID uuid : keySet){
                long original = getEntityGlowTimeRemain(uuid);
                long after = original-loopPeriod;
                if(after <= 0){
                    entityGlowTimeRemainMap.remove(uuid);
                    Entity entity = Bukkit.getEntity(uuid);
                    if(GlowAPI.isGlowing(entity, player))
                        GlowAPI.setGlowing(entity, null, player);
                    continue;
                }
                setEntityGlowTimeRemain(uuid, after);
            }
        }
    }
    public static GlowManager instance;
    public HashMap<UUID, PlayerVisionData> playerVisionDataMap = new HashMap<>();
    public GlowManager(long loopPeriod){
        instance = this;
        initLoop(loopPeriod);
    }
    public PlayerVisionData getPlayerVisionData(Player player){
        UUID uuid = player.getUniqueId();
        if(!playerVisionDataMap.containsKey(uuid)){
            //Setup player vision data
            playerVisionDataMap.put(uuid, new PlayerVisionData(player));
        }
        return playerVisionDataMap.get(uuid);
    }
    public void initLoop(long loopPeriod){
        new BukkitRunnable() {
            @Override
            public void run() {
                for(UUID uuid : playerVisionDataMap.keySet()){
                    PlayerVisionData playerVisionData = playerVisionDataMap.get(uuid);
                    playerVisionData.updateTick(loopPeriod);
                }
            }
        }.runTaskTimer(SkillAPI.singleton,0, loopPeriod);
    }
}
