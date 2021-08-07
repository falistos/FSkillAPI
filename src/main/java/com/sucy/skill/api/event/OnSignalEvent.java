package com.sucy.skill.api.event;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class OnSignalEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final LivingEntity player;
    private final LivingEntity target;
    private final String signal;

    public OnSignalEvent(final LivingEntity player, final LivingEntity target, final String signal) {
        this.player = player;
        this.target = target;
        this.signal = signal;
    }

    public LivingEntity getPlayer() {
        return player;
    }

    public LivingEntity getTarget() { return target; }

    public String getSignal() {
        return signal;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
