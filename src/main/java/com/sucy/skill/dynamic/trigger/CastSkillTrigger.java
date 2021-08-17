package com.sucy.skill.dynamic.trigger;

import com.sucy.skill.api.Settings;
import com.sucy.skill.api.event.PlayerCastSkillEvent;
import org.bukkit.entity.LivingEntity;

import java.util.Map;

public class CastSkillTrigger implements Trigger<PlayerCastSkillEvent> {

    /** {@inheritDoc} */
    @Override
    public String getKey() {
        return "CAST_SKILL";
    }

    /** {@inheritDoc} */
    @Override
    public Class<PlayerCastSkillEvent> getEvent() {
        return PlayerCastSkillEvent.class;
    }

    /** {@inheritDoc} */
    @Override
    public boolean shouldTrigger(PlayerCastSkillEvent event, int level, Settings settings) {
        final double manaCost = settings.getDouble("manaCost");
        final double staminaCost = settings.getDouble("staminaCost");
        final String skill = settings.getString("skill");
        if (skill == "any") {
            if (manaCost == -1) {
                if (staminaCost == -1) return true;
                else if (staminaCost == event.getStaminaCost()) return true;
                else return false;
            }
            else if (manaCost == event.getManaCost()) {
                if (staminaCost == -1) return true;
                else if (staminaCost == event.getStaminaCost()) return true;
                else return false;
            }
            else return false;
        }
        else if (event.getSkill().equals(skill)) {
            if (manaCost == -1) {
                if (staminaCost == -1) return true;
                else if (staminaCost == event.getStaminaCost()) return true;
                else return false;
            }
            else if (manaCost == event.getManaCost()) {
                if (staminaCost == -1) return true;
                else if (staminaCost == event.getStaminaCost()) return true;
                else return false;
            }
            else return false;
        }
        else return false;
    }

    /** {@inheritDoc} */
    @Override
    public void setValues(final PlayerCastSkillEvent event, final  Map<String, Object> data) {
        data.put("api-skill-name", event.getSkill());
        data.put("api-skill-mana", event.getManaCost());
        data.put("api-skill-stamina", event.getStaminaCost());
    }

    /** {@inheritDoc} */
    @Override
    public LivingEntity getCaster(final PlayerCastSkillEvent event) {
        return event.getPlayer();
    }

    /** {@inheritDoc} */
    @Override
    public LivingEntity getTarget(final PlayerCastSkillEvent event ,final Settings settings) {
        return event.getPlayer();
    }
}
