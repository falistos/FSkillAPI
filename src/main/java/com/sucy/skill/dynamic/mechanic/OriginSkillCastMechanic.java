package com.sucy.skill.dynamic.mechanic;

import ac.github.originskill.internal.ItemSkill;
import ac.github.originskill.internal.holder.impl.CommandHolder;
import com.sucy.skill.hook.OriginSkillHook;
import com.sucy.skill.hook.PluginChecker;
import com.sucy.skill.log.Logger;
import org.bukkit.entity.LivingEntity;

import java.util.List;
import java.util.Optional;

public class OriginSkillCastMechanic extends MechanicComponent {

    private static final String OriginSkill = "skills";

    @Override
    public String getKey() {
        return "originSkill";
    }

    @Override
    public boolean execute(LivingEntity caster, int level, List<LivingEntity> targets) {
        String skills = settings.getString(OriginSkill, "skills");
        Optional<ItemSkill> optional = ItemSkill.find(skills);
        ItemSkill skill = optional.get();

        boolean worked = false;

        if (skills == null) {
            Logger.invalid("Skills is not present");
            return false;
        }

        for (LivingEntity target : targets) {
            if (PluginChecker.isOriginskillActive()) {
                OriginSkillHook.castskill(caster, new CommandHolder(true), skill);
                worked = true;
            }
        }
        return worked;
    }
}
