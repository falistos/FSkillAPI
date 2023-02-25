package com.sucy.skill.hook;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.perms.Relation;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class FactionsHook {

    public static boolean canAttack(LivingEntity attacker, LivingEntity defender) {
        if (!(attacker instanceof Player) || (!(defender instanceof Player)))
            return true;

        FPlayer fAttacker = FPlayers.getInstance().getByPlayer((Player) attacker);
        FPlayer fDefender = FPlayers.getInstance().getByPlayer((Player) defender);

        Relation relation = fAttacker.getRelationTo(fDefender);

        return relation.isNeutral() || relation.isEnemy();
    }
}
