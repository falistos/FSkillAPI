package com.sucy.skill.dynamic.condition;

import com.sucy.skill.api.enums.LunarType;
import com.sucy.skill.api.util.LunarAPI;
import org.bukkit.entity.LivingEntity;

public class LunarCondition extends ConditionComponent {
    private static final String TYPE = "type";

    @Override
    boolean test(final LivingEntity caster, final int level, final LivingEntity target) {
        LunarType lunarType = LunarType.valueOf(TYPE);
        LunarType targetLunarType = LunarAPI.Lunar(target.getWorld());
        return lunarType == targetLunarType;
    }

    @Override
    public String getKey() {
        return "lunar";
    }
}
