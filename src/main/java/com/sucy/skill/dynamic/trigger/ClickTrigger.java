package com.sucy.skill.dynamic.trigger;

import com.sucy.skill.api.Settings;
import com.sucy.skill.api.event.KeyPressEvent;
import org.bukkit.entity.LivingEntity;

import java.util.Map;

public class ClickTrigger implements Trigger<KeyPressEvent> {

    /** {@inheritDoc} */
    @Override
    public String getKey() {
        return "CLICK";
    }

    /** {@inheritDoc} */
    @Override
    public Class<KeyPressEvent> getEvent() {
        return KeyPressEvent.class;
    }

    /** {@inheritDoc} */
    @Override
    public boolean shouldTrigger(KeyPressEvent event, int level, Settings settings) {
        final String clickType = settings.getString("clickType");

        return event.getKey().equals(clickType);
    }

    /** {@inheritDoc} */
    @Override
    public LivingEntity getCaster(final KeyPressEvent event) {
        return event.getPlayer();
    }

    /** {@inheritDoc} */
    @Override
    public LivingEntity getTarget(final KeyPressEvent event, final Settings settings) {
        return event.getPlayer();
    }

    /** {@inheritDoc} */
    @Override
    public void setValues(KeyPressEvent event, Map<String, Object> data) {
        data.put("clickType", event.getKey());
    }
}
