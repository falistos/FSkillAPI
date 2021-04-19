package com.sucy.skill.cmd;

import com.rit.sucy.commands.CommandManager;
import com.rit.sucy.commands.ConfigurableCommand;
import com.rit.sucy.commands.IFunction;
import com.rit.sucy.config.Filter;
import com.rit.sucy.config.parse.NumberParser;
import com.rit.sucy.version.VersionManager;
import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.enums.StaminaSource;
import com.sucy.skill.api.player.PlayerData;
import com.sucy.skill.language.RPGFilter;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class CmdStamina implements IFunction
{
    private static final String NOT_PLAYER    = "not-player";
    private static final String NOT_NUMBER    = "not-number";
    private static final String NOT_POSITIVE  = "not-positive";
    private static final String GAVE_STAMINA     = "gave-stamina";
    private static final String RECEIVED_STAMINA = "received-stamina";
    private static final String DISABLED      = "world-disabled";

    /**
     * Runs the command
     *
     * @param cmd    command that was executed
     * @param plugin plugin reference
     * @param sender sender of the command
     * @param args   argument list
     */
    @Override
    public void execute(ConfigurableCommand cmd, Plugin plugin, CommandSender sender, String[] args)
    {
        // Disabled world
        if (sender instanceof Player && !SkillAPI.getSettings().isWorldEnabled(((Player) sender).getWorld()) && args.length == 1)
        {
            cmd.sendMessage(sender, DISABLED, "&4You cannot use this command in this world");
        }

        // Only can show info of a player so console needs to provide a name
        else if (args.length >= 1 && (args.length >= 2 || sender instanceof Player))
        {
            // Get the player stamina
            OfflinePlayer target = args.length == 1 ? (OfflinePlayer) sender : VersionManager.getOfflinePlayer(args[0], false);
            if (target == null)
            {
                cmd.sendMessage(sender, NOT_PLAYER, ChatColor.RED + "That is not a valid player name");
                return;
            }

            // Parse the stamina
            double amount;
            try
            {
                amount = NumberParser.parseDouble(args[args.length == 1 ? 0 : 1]);
            }
            catch (Exception ex)
            {
                cmd.sendMessage(sender, NOT_NUMBER, ChatColor.RED + "That is not a valid mana amount");
                return;
            }

            // Invalid amount of stamina
            if (amount <= 0)
            {
                cmd.sendMessage(sender, NOT_POSITIVE, ChatColor.RED + "You must give a positive amount of mana");
                return;
            }

            // Give mana
            PlayerData data = SkillAPI.getPlayerData(target);
            data.giveStamina(amount, StaminaSource.COMMAND);

            // Messages
            if (target != sender)
            {
                cmd.sendMessage(sender, GAVE_STAMINA, ChatColor.DARK_GREEN + "You have given " + ChatColor.GOLD + "{player} {mana} stamina", Filter.PLAYER.setReplacement(target.getName()), RPGFilter.STAMINA.setReplacement("" + amount));
            }
            if (target.isOnline())
            {
                cmd.sendMessage(target.getPlayer(), RECEIVED_STAMINA, ChatColor.DARK_GREEN + "You have received " + ChatColor.GOLD + "{mana} stamina " + ChatColor.DARK_GREEN + "from " + ChatColor.GOLD + "{player}", Filter.PLAYER.setReplacement(sender.getName()), RPGFilter.STAMINA.setReplacement("" + amount));
            }
        }

        // Not enough arguments
        else
        {
            CommandManager.displayUsage(cmd, sender);
        }
    }
}