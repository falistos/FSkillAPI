package com.sucy.skill.dynamic.condition;

import org.bukkit.entity.LivingEntity;

import java.util.List;

public class DistanceCondition extends ConditionComponent {
    private static final String MIN = "min-value";
    private static final String MAX = "max-value";

    @Override
    public String getKey() {
        return "distance";
    }

    @Override
    public boolean execute(LivingEntity caster, int level, List<LivingEntity> targets) {
        return test(caster, level, null) && executeChildren(caster, level, targets);
    }

    @Override
    boolean test(final LivingEntity caster, final int level, final LivingEntity target) {
        final double min = parseValues(caster, MIN, level, 1);
        final double max = parseValues(caster, MAX, level, 999);
        final double distance = target.getLocation().distance(caster.getLocation());
        if(target != null) {
            return distance >= min && distance <= max;
        }
        return false;
    }
}
