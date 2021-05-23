package com.sucy.skill.dynamic.mechanic;

import com.sucy.skill.hook.MythicMobsHook;
import com.sucy.skill.hook.PluginChecker;
import com.sucy.skill.log.Logger;
import org.bukkit.entity.LivingEntity;

import java.util.List;

public class MythicSkillsMechanic extends MechanicComponent {

    private static final String MYTHICSKILLS = "skills";

    @Override
    public String getKey() {
        return "mythicskills";
    }

    @Override
    public boolean execute(LivingEntity caster, int level, List<LivingEntity> targets) {
        String skills = settings.getString(MYTHICSKILLS,"skills");

        boolean worked = false;

        if(skills == null) {
            Logger.invalid("Skill is not present");
            return false;
        }

        for (LivingEntity target : targets){
            if(PluginChecker.isMythicMobsActive()) {
                MythicMobsHook.skills(caster, skills);
                worked = true;
            }
            else worked = false;
            Logger.invalid("MythicMobs is not install");
        }
        return  worked;
    }
}
