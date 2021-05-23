package com.sucy.skill.hook;

import ac.github.originskill.api.OriginSkillAPI;
import ac.github.originskill.internal.ItemSkill;
import ac.github.originskill.internal.holder.EventHolder;
import org.bukkit.entity.LivingEntity;

public class OriginSkillHook {
    public static void castskill(final LivingEntity target, final EventHolder Command, final ItemSkill skills) {
        OriginSkillAPI.cast(target, skills, Command);
    }
}
