package com.sucy.skill.task;

import com.rit.sucy.version.VersionManager;
import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.player.PlayerData;
import com.sucy.skill.log.LogType;
import com.sucy.skill.log.Logger;
import com.sucy.skill.thread.RepeatThreadTask;
import org.bukkit.entity.Player;

public class StaminaTask extends RepeatThreadTask
{
    /**
     * Starts a new task for regenerating mana over time. The task is
     * started automatically so don't initialize this class unless wanting to
     * start a new task.
     */
    public StaminaTask()
    {
        super(
                SkillAPI.getSettings().getGainFreq(),
                SkillAPI.getSettings().getGainFreq()
        );
    }

    /**
     * <p>Checks all players for mana regeneration each interval</p>
     */
    public void run()
    {
        Player[] players = VersionManager.getOnlinePlayers();
        Logger.log(LogType.STAMINA, 1, "Applying stamina regen for " + players.length + " players");
        for (Player player : players)
        {
            PlayerData data = SkillAPI.getPlayerData(player);
            data.regenStamina();
        }
    }
}
