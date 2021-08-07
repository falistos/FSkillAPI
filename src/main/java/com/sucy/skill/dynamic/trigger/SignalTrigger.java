package com.sucy.skill.dynamic.trigger;

import com.sucy.skill.api.Settings;
import com.sucy.skill.api.event.OnSignalEvent;
import org.bukkit.entity.LivingEntity;

import java.util.Map;

public class SignalTrigger implements Trigger<OnSignalEvent> {

    /** {@inheritDoc} */
    @Override
    public String getKey() {
        return "Signal";
    }

    /** {@inheritDoc} */
    @Override
    public Class<OnSignalEvent> getEvent() {
        return OnSignalEvent.class;
    }

    /** {@inheritDoc} */
    @Override
    public boolean shouldTrigger(OnSignalEvent event, int level, Settings settings) {

        final String trueSignal = settings.getString("signal");

        return  trueSignal.equals(event.getSignal());
    }

    /** {@inheritDoc} */
    @Override
    public void setValues(OnSignalEvent event, Map<String, Object> data) {
        data.put("data-signal", event.getSignal());
    }

    /** {@inheritDoc} */
    @Override
    public LivingEntity getCaster(final OnSignalEvent event) { return event.getPlayer(); }

    /** {@inheritDoc} */
    @Override
    public LivingEntity getTarget(final OnSignalEvent event, final Settings settings) {
        return isUsingTarget(settings) ? event.getTarget() : event.getPlayer();
    }

    boolean isUsingTarget(final Settings settings) {
        return settings.getString("target", "true").equalsIgnoreCase("false");
    }
}
