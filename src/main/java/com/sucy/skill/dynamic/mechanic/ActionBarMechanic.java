package com.sucy.skill.dynamic.mechanic;

import at.actionbar.main.ActionbarAPI;
import com.rit.sucy.text.TextFormatter;
import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.player.PlayerClass;
import com.sucy.skill.api.player.PlayerData;
import com.sucy.skill.api.player.PlayerSkill;
import com.sucy.skill.api.util.ActionBar;
import com.sucy.skill.api.util.FlagManager;
import com.sucy.skill.api.util.StatusFlag;
import com.sucy.skill.manager.AttributeManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.List;


public class ActionBarMechanic extends MechanicComponent
{
    private static final String TEXT = "text";

    @Override
    public String getKey() {
        return "action bar";
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
                ActionbarAPI.sendActionbar(player,filter(caster, target, text));
                worked = true;
            }
        }
        return worked;
    }
}
