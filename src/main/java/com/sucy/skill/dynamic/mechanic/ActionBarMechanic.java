package com.sucy.skill.dynamic.mechanic;

import com.rit.sucy.text.TextFormatter;
import com.sucy.skill.api.util.ActionBar;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.List;


public class ActionBarMechanic extends MechanicComponent
{
    private static final String TEXT = "text";

    @Override
    public String getKey() {
        return "actionbar";
    }


    @Override
    public boolean execute(LivingEntity caster, int level, List<LivingEntity> targets)
    {
        if (targets.size() == 0)
            return false;

        String text = TextFormatter.colorString(settings.getString(TEXT));

        boolean worked = false;
        for (LivingEntity target : targets)
        {
            if (target instanceof Player)
            {
                Player player = (Player) target;
                ActionBar.show(player, text);
                worked = true;
            }
        }
        return worked;
    }
}
