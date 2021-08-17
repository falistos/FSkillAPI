package com.sucy.skill.api.event;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class ValueChangeEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final LivingEntity player;
    private final String key;
    private final Object data;

    public ValueChangeEvent(final LivingEntity player, final String key, final Object data) {
        this.player = player;
        this.key = key;
        this.data = data;
    }

    public LivingEntity getPlayer() { return player; }

    public String getKey() {
        return key;
    }

    public Object getData() {
        return data;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
