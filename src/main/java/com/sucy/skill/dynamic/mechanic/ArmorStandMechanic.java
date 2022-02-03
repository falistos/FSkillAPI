package com.sucy.skill.dynamic.mechanic;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.armorstand.ArmorStandInstance;
import com.sucy.skill.api.armorstand.ArmorStandManager;
import com.sucy.skill.listener.MechanicListener;
import com.sucy.skill.task.RemoveTask;
import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Summons an armor stand that can be used as a marker or for item display. Applies child components on the armor stand
 */
public class ArmorStandMechanic extends MechanicComponent {
    private static final Vector UP = new Vector(0, 1, 0);

    private static final String KEY = "key";
    private static final String DURATION = "duration";
    private static final String NAME = "name";
    private static final String NAME_VISIBLE = "name-visible";
    private static final String FOLLOW = "follow";
    private static final String GRAVITY = "gravity";
    private static final String SMALL = "tiny";
    private static final String ARMS = "arms";
    private static final String BASE = "base";
    private static final String VISIBLE = "visible";
    private static final String MARKER = "marker";
    private static final String FORWARD = "forward";
    private static final String UPWARD = "upward";
    private static final String RIGHT = "right";
    private static final String AUTO_ROTATION_X = "auto-rotation-x";
    private static final String AUTO_ROTATION_Y = "auto-rotation-y";
    private static final String AUTO_ROTATION_Z = "auto-rotation-z";
    private static final String MODEL_ENGINE_ID = "model-engine-id";

    @Override
    public String getKey() { return "armor stand"; }

    @Override
    public boolean execute(LivingEntity caster, int level, List<LivingEntity> targets) {
        String key = filter(caster, null, settings.getString(KEY, skill.getName()));
        int duration = (int) (20 * parseValues(caster, DURATION, level, 5));
        String name = settings.getString(NAME, "Armor Stand");
        boolean nameVisible = settings.getBool(NAME_VISIBLE, false);
        boolean follow = settings.getBool(FOLLOW, false);
        boolean gravity = settings.getBool(GRAVITY, false);
        boolean small = settings.getBool(SMALL, false);
        boolean arms = settings.getBool(ARMS, false);
        boolean base = settings.getBool(BASE, false);
        boolean visible = settings.getBool(VISIBLE, true);
        boolean marker = settings.getBool(MARKER, false);
        double forward = parseValues(caster, FORWARD, level, 0);
        double upward = parseValues(caster, UPWARD, level, 0);
        double right = parseValues(caster, RIGHT, level, 0);
        double autoRotationX = parseValues(caster, AUTO_ROTATION_X, level,0);
        double autoRotationY = parseValues(caster, AUTO_ROTATION_Y, level,0);
        double autoRotationZ = parseValues(caster, AUTO_ROTATION_Z, level,0);
        String modelEngineID = settings.getString(MODEL_ENGINE_ID, "none");

        List<LivingEntity> armorStands = new ArrayList<>();
        for (LivingEntity target : targets) {
            Location loc = target.getLocation().clone();
            Vector dir = loc.getDirection().setY(0).normalize();
            Vector side = dir.clone().crossProduct(UP);
            loc.add(dir.multiply(forward)).add(0, upward, 0).add(side.multiply(right));

            ArmorStand armorStand = target.getWorld().spawn(loc, ArmorStand.class, as -> {
                try {
                    as.setMarker(marker);
                } catch (NoSuchMethodError ignored) {}
                try {
                    as.setSilent(marker);
                } catch (NoSuchMethodError ignored) {}
                as.setCollidable(!marker);
                as.setInvulnerable(marker);
                as.setGravity(gravity);
                as.setCustomName(name);
                as.setCustomNameVisible(nameVisible);
                as.setSmall(small);
                as.setArms(arms);
                as.setBasePlate(base);
                as.setVisible(visible);
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        if(as==null) {
                            cancel();
                            return;
                        }
                        EulerAngle headPose = as.getHeadPose();
                        headPose = headPose.add(autoRotationX, autoRotationY, autoRotationZ);
                        as.setHeadPose(headPose);
                    }
                }.runTaskTimer(SkillAPI.singleton, 0, 1);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        try {
                            if (modelEngineID != null && !modelEngineID.equals("none")) {
                                ActiveModel model = ModelEngineAPI.api.getModelManager().createActiveModel(modelEngineID);

                                if (model == null) {
                                    caster.sendMessage("SkillAPI Armor stand Failed to load model engine with ID: " + ChatColor.WHITE + modelEngineID);
                                } else {
                                    ModeledEntity modeledEntity = ModelEngineAPI.api.getModelManager().createModeledEntity(as);

                                    if (modeledEntity == null) {
                                        caster.sendMessage("Failed to create modelled entity");
                                    } else {
                                        modeledEntity.addActiveModel(model);
                                        modeledEntity.detectPlayers();
                                    }
                                }

                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }.runTaskLater(SkillAPI.singleton, 1);
            });
            /*
            SkillAPI.singleton.protocolManager.addPacketListener(new PacketAdapter(SkillAPI.singleton, PacketType.Play.Server.SPAWN_ENTITY_LIVING)
            {
                //for out packets
                @Override
                public void onPacketSending(PacketEvent event)
                {
                    Bukkit.broadcastMessage("entity spawn living packet created");
                    int entityID = event.getPacket().getIntegers().read(0);
                    if(entityID == armorStand.getEntityId()){
                        event.setCancelled(true);
                        Bukkit.broadcastMessage("cancelled sending spawn armorstand packet to user");
                    }
                }
            });
            */
            //SkillAPI.setMeta(armorStand, MechanicListener.ARMOR_STAND, true);
            armorStands.add(armorStand);

            ArmorStandInstance instance;
            if (follow) {
                instance = new ArmorStandInstance(armorStand, target, forward, upward, right);
            } else {
                instance = new ArmorStandInstance(armorStand, target);
            }
            ArmorStandManager.register(instance, target, key);
        }
        executeChildren(caster, level, armorStands);
        new RemoveTask(armorStands, duration);
        return targets.size() > 0;
    }
}
