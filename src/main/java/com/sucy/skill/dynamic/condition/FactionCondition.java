package com.sucy.skill.dynamic.condition;

import com.sucy.skill.hook.MythicMobsHook;
import com.sucy.skill.hook.PluginChecker;
import org.bukkit.entity.LivingEntity;

public class FactionCondition extends ConditionComponent{
    private static final String FACTION = "faction";

    @Override
    public String getKey() { return "faction"; }

    @Override
    boolean test (final LivingEntity caster, final int level, final  LivingEntity target) {
        String faction = settings.getString(FACTION);

        if (PluginChecker.isMythicMobsActive() && MythicMobsHook.isMonster(target)){
            return MythicMobsHook.isRightFaction(target, faction);
        } else return false;
    }
}
