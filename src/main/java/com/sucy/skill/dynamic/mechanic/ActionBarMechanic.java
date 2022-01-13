package com.sucy.skill.dynamic.mechanic;

import com.rit.sucy.text.TextFormatter;
import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.player.PlayerClass;
import com.sucy.skill.api.player.PlayerData;
import com.sucy.skill.api.player.PlayerSkill;
import com.sucy.skill.api.util.ActionBar;
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
                text = replaceTextWithPlayerSkillStatus(text, player);
                ActionBar.show(player,filter(caster, target, text));
                worked = true;
            }
        }
        return worked;
    }
    private String replaceTextWithPlayerSkillStatus(String text, Player player){
        PlayerData data = SkillAPI.getPlayerData(player);
        String className = data.getClass("legend").getData().getName();
        PlayerSkill playerSkill1 = data.getSkill(className+"_skill_1");
        float skill1Cooldown = playerSkill1.getCooldown();
        double skill1RequiredMana = playerSkill1.getManaCost();
        int skill1CooldownInt = Math.round(skill1Cooldown);
        PlayerSkill playerSkill2 = data.getSkill(className+"_skill_2");
        float skill2Cooldown = playerSkill2.getCooldown();
        double skill2RequiredMana = playerSkill2.getManaCost();
        int skill2CooldownInt = Math.round(skill2Cooldown);
        double playerMana = data.getMana();

        double manaCostReduceStat = data.scaleStat(AttributeManager.MANA_COST, 0);
        double manaCostReducePercent = ((100-manaCostReduceStat)/100);

        String skill1Status, skill2Status;
        String bold = ""+ChatColor.BOLD;
        if(skill1Cooldown > 0){
            skill1Status = ChatColor.DARK_AQUA+bold+skill1CooldownInt+"s";
        }else if(playerMana < skill1RequiredMana*manaCostReducePercent){
            skill1Status = ChatColor.AQUA+bold+"魔力不足";
        }else{
            skill1Status = ChatColor.GREEN+bold+"右鍵使用";
        }
        if(skill2Cooldown > 0){
            skill2Status = ChatColor.AQUA+bold+skill2CooldownInt+"s";
        }else if(playerMana < skill2RequiredMana*manaCostReducePercent){
            skill2Status = ChatColor.AQUA+bold+"魔力不足";
        }else{
            skill2Status = ChatColor.GREEN+bold+"F鍵使用";
        }
        text = text.replace("{skill_1_status}", skill1Status);
        text = text.replace("{skill_2_status}", skill2Status);
        return text;
    }
}
