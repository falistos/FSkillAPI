package com.sucy.skill.dynamic.mechanic;

import com.sucy.skill.dynamic.DynamicSkill;
import com.sucy.skill.dynamic.EffectComponent;
import org.bukkit.entity.LivingEntity;

import java.util.List;

/**
 * SkillAPI © 2018
 * com.sucy.skill.dynamic.mechanic.ValueCopyMechanic
 */
public class ValueCopyMechanic extends EffectComponent {
    private static final String KEY       = "key";
    private static final String TO_TARGET = "to-target";

    @Override
    public boolean execute(
            final LivingEntity caster, final int level, final List<LivingEntity> targets) {

        if (targets.size() == 0 || !settings.has(KEY)) {
            return false;
        }

        final String key = settings.getString(KEY);
        final boolean toTarget = settings.getString(TO_TARGET, "true").equalsIgnoreCase("true");

        if (toTarget) {
            targets.forEach(target -> apply(caster, target, key));
        } else {
            apply(targets.get(0), caster, key);
        }

        return true;
    }

    private boolean apply(final LivingEntity from, final LivingEntity to, final String key) {
        final Object value = DynamicSkill.getCastData(from).get(key);
        System.out.println(from.getType() + " -> " + to.getType() + " with " + value);
        if (value == null) return false;
        DynamicSkill.getCastData(to).put(key, value);
        return true;
    }
}
