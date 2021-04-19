package com.sucy.skill.dynamic.condition;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.player.PlayerData;
import com.sucy.skill.api.player.PlayerSkill;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class StaminaCondition extends ConditionComponent {
    private static final String TYPE = "type";
    private static final String MIN  = "min-value";
    private static final String MAX  = "max-value";

    @Override
    boolean test(final LivingEntity caster, final int level, final LivingEntity target) {
        if (!(target instanceof Player)) {
            return false;
        }

        final String type = settings.getString(TYPE).toLowerCase();
        final double min = parseValues(caster, MIN, level, 0);
        final double max = parseValues(caster, MAX, level, 99);
        final PlayerData data = SkillAPI.getPlayerData((Player) target);
        final PlayerSkill skill = getSkillData(caster);
        final double stamina = data.getStamina();

        double value;
        switch (type) {
            case "difference percent":
                value = (stamina - skill.getPlayerData().getStamina()) * 100 / skill.getPlayerData().getStamina();
                break;
            case "difference":
                value = stamina - skill.getPlayerData().getStamina();
                break;
            case "percent":
                value = stamina * 100 / data.getMaxStamina();
                break;
            default:
                value = stamina;
                break;
        }
        return value >= min && value <= max;
    }

    @Override
    public String getKey() {
        return "stamina";
    }
}