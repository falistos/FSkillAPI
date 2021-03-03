package com.sucy.skill.dynamic.mechanic;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.skills.PassiveSkill;
import com.sucy.skill.api.skills.Skill;
import com.sucy.skill.dynamic.DynamicSkill;
import com.sucy.skill.hook.MythicMobsHook;
import com.sucy.skill.hook.PluginChecker;
import com.sucy.skill.log.Logger;
import com.sucy.skill.task.RemoveTask;
import org.bukkit.entity.LivingEntity;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class SummonMythicMobsMechanic extends MechanicComponent {

    private static final String MYTHICMOBS = "mythicmobs";
    private static final String LEVEL = "mobLevel";
    private static final String AMOUNT = "amount";
    private static final String SKILLS = "skills";
    public static final  String SKILL_META = "sapi_mob_skills";
    private static final String SECONDS    = "seconds";

    private final Map<Integer, RemoveTask> tasks = new HashMap<>();

    @Override
    public String getKey() {
        return "summonmythicmobs";
    }

    @Override
    public boolean execute(LivingEntity caster, int level, List<LivingEntity> targets) {

        if (!(caster instanceof Player)) {
            return false;
        }

        cleanUp(caster);

        String mobs = settings.getString(MYTHICMOBS,"mob");
        int mobLevel = settings.getInt(LEVEL,1);
        final Player player = (Player) caster;
        int amount = settings.getInt(AMOUNT,1);
        double seconds = parseValues(player, SECONDS, level, 10.0);
        int ticks = (int) (seconds * 20);

        List<String> skills = settings.getStringList(SKILLS);

        boolean worked = false;

        List<LivingEntity> mythicMobs = new ArrayList<>();
        for (LivingEntity target : targets) {
            if (PluginChecker.isMythicMobsActive()) {
                Location location = target.getLocation();
                if (mobs != null) {
                    for (int i = 1; i < amount; i++) {
                        MythicMobsHook.summonMobs(mobs, location, mobLevel);
                        LivingEntity mob = target.getWorld().spawn(target.getLocation(), LivingEntity.class);
                        List<LivingEntity> owner = new ArrayList<>(1);
                        owner.add(player);
                        DynamicSkill.getCastData(mob).put("api-owner", owner);
                        for (String skillName : skills) {
                            Skill skill = SkillAPI.getSkill(skillName);
                            if (skill instanceof PassiveSkill) {
                                ((PassiveSkill) skill).initialize(mob, level);
                            }
                        }
                        SkillAPI.setMeta(mob, SKILL_META, skills);
                        SkillAPI.setMeta(mob, LEVEL, level);

                        mythicMobs.add(mob);
                    }
                    worked = true;
                }else {
                    worked = false;
                    Logger.invalid("Mob is not present");
                }
            } else {
                worked = false;
                Logger.invalid("MythicMobs is not install");
            }
        }

        final RemoveTask task = new RemoveTask(mythicMobs, ticks);
        tasks.put(caster.getEntityId(), task);

        if (mythicMobs.size() > 0) {
            executeChildren(player, level, mythicMobs);
            return true;
        }
        return  worked;
    }
    @Override
    public void cleanUp(final LivingEntity caster) {
        final RemoveTask task = tasks.remove(caster.getEntityId());
        if (task != null) {
            task.cancel();
            task.run();
        }
    }
}
