package com.sucy.skill.api.event;

import com.sucy.skill.api.player.PlayerSkill;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.List;

public class SkillTargetEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final LivingEntity caster;
    private final PlayerSkill skill;
    private final List<LivingEntity> targets;

    private SkillTargetEvent(PlayerSkill skill, LivingEntity caster, List<LivingEntity> targets) {
        this.skill = skill;
        this.caster = caster;
        this.targets = targets;
    }

    public static SkillTargetEvent invoke(PlayerSkill skill, LivingEntity caster, List<LivingEntity> targets) {
        SkillTargetEvent event = new SkillTargetEvent(skill, caster, targets);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    /**
     * @return entity casting the parent skill
     */
    public LivingEntity getCaster()
    {
        return caster;
    }

    /**
     * @return parent skill
     */
    public PlayerSkill getParentSkill()
    {
        return skill;
    }

    /**
     * @return list of targets
     */
    public List<LivingEntity> getTargets() {
        return targets;
    }

    /**
     * Retrieves the handlers for the event
     *
     * @return list of event handlers
     */
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * Retrieves the handlers for the event
     *
     * @return list of event handlers
     */
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
