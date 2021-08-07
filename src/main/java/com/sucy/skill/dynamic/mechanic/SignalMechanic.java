package com.sucy.skill.dynamic.mechanic;

import com.sucy.skill.api.event.OnSignalEvent;
import org.bukkit.entity.LivingEntity;

import java.util.List;

public class SignalMechanic extends MechanicComponent {
    private static final String SIGNAL   = "signal";

    @Override
    public String getKey() { return "signal"; }

    @Override
    public boolean execute(LivingEntity caster, int level, List<LivingEntity> targets) {

        String signal = settings.getString(SIGNAL, "null");

        if (targets.size() == 0) {
            return false;
        }
        for (LivingEntity target : targets) {
            new OnSignalEvent(caster, target, signal);
        }
        return true;
    }
}

