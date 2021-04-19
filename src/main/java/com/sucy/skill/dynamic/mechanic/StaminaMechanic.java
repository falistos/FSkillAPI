package com.sucy.skill.dynamic.mechanic;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.enums.StaminaCost;
import com.sucy.skill.api.enums.StaminaSource;
import com.sucy.skill.api.player.PlayerData;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.List;

public class StaminaMechanic extends MechanicComponent {
    private static final String TYPE  = "type";
    private static final String VALUE = "value";

    @Override
    public String getKey() {
        return "stamina";
    }

    /**
     * Executes the component
     *
     * @param caster  caster of the skill
     * @param level   level of the skill
     * @param targets targets to apply to
     *
     * @return true if applied to something, false otherwise
     */
    @Override
    public boolean execute(LivingEntity caster, int level, List<LivingEntity> targets) {
        boolean percent = settings.getString(TYPE, "stamina").toLowerCase().equals("percent");
        double value = parseValues(caster, VALUE, level, 1.0);

        boolean worked = false;
        for (LivingEntity target : targets) {
            if (!(target instanceof Player)) {
                continue;
            }

            worked = true;

            PlayerData data = SkillAPI.getPlayerData((Player) target);
            double amount;
            if (percent) {
                amount = data.getMaxStamina() * value / 100;
            } else {
                amount = value;
            }

            if (amount > 0) {
                data.giveStamina(amount, StaminaSource.SKILL);
            } else {
                data.useStamina(-amount, StaminaCost.SKILL_EFFECT);
            }
        }
        return worked;
    }
}