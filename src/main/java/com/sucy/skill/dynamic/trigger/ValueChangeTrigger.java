package com.sucy.skill.dynamic.trigger;

import com.sucy.skill.api.Settings;
import com.sucy.skill.api.event.ValueChangeEvent;
import org.bukkit.entity.LivingEntity;

import java.util.Map;

public class ValueChangeTrigger implements Trigger<ValueChangeEvent> {

    /** {@inheritDoc} */
    @Override
    public String getKey() {
        return "ValueChange";
    }

    /** {@inheritDoc} */
    @Override
    public Class<ValueChangeEvent> getEvent() {
        return ValueChangeEvent.class;
    }

    /** {@inheritDoc} */
    @Override
    public boolean shouldTrigger(ValueChangeEvent event, int level, Settings settings) {
        final String key = settings.getString("key");
        final Double data = settings.getDouble("data");

        if(key.equalsIgnoreCase("any")){
            if(data.equals("any")){
                return true;
            }
            else if(event.getData().equals(data)){
                return true;
            }
            else return false;
        }
        else if (event.getKey().equals(key)){
            if(data.equals(0)){
                return true;
            }
            else if(event.getData().equals(data)){
                return true;
            }
            else return false;
        } else return false;
    }

    /** {@inheritDoc} */
    @Override
    public void setValues(ValueChangeEvent event, Map<String, Object> data) {
        data.put("data-change-value", event.getData());
    }

    /** {@inheritDoc} */
    @Override
    public LivingEntity getCaster(final ValueChangeEvent event) {
        return event.getPlayer();
    }

    /** {@inheritDoc} */
    @Override
    public LivingEntity getTarget(ValueChangeEvent event, Settings settings) {
        return event.getPlayer();
    }

}
