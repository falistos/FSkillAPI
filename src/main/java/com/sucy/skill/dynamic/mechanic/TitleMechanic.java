package com.sucy.skill.dynamic.mechanic;

import com.rit.sucy.text.TextFormatter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.List;


public class TitleMechanic extends MechanicComponent
{
    private static final String TITLE = "title";
    private static final String SUBTITLE = "subtitle";
    private static final String FADEIN = "fadein";
    private static final String STAY = "stay";
    private static final String FADEOUT = "fadeout";

    @Override
    public String getKey() {
        return "title";
    }


    @Override
    public boolean execute(LivingEntity caster, int level, List<LivingEntity> targets)
    {
        if (targets.size() == 0)
            return false;

        String title = filter(caster, null, TextFormatter.colorString(settings.getString(TITLE)));
        String subtitle = filter(caster, null, TextFormatter.colorString(settings.getString(SUBTITLE)));
        int fadein = (int)parseValues(caster, FADEIN, level, 1);
        int stay = (int)parseValues(caster, STAY, level, 1);
        int fadeout = (int)parseValues(caster, FADEOUT, level, 1);

        boolean worked = false;
        for (LivingEntity target : targets)
        {
            if (target instanceof Player)
            {
                Player player = (Player) target;
                player.sendTitle(title,subtitle,fadein,stay,fadeout);
                worked = true;
            }
        }
        return worked;
    }
}
