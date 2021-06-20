package com.sucy.skill.dynamic.condition;

import chsteam.lunarapi.api.LunarType;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;

public class LunarCondition extends ConditionComponent {
    private static final String TYPE = "type";

    @Override
    boolean test(final LivingEntity caster, final int level, final LivingEntity target) {
        final LunarType lunar = LunarType.valueOf(settings.getString(TYPE));
        final World world = target.getWorld();
        final LunarType targetLunar = LunarType.class.cast(world);
        return lunar == targetLunar;
    }

    @Override
    public String getKey() {
        return "lunar";
    }
}
