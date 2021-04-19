package com.sucy.skill.api.enums;

public enum StaminaSource
{
    /**
     * The player regenerated some stamina back passively
     */
    REGEN,

    /**
     * A skill effect replenished some of their stamina
     */
    SKILL,

    /**
     * A command restored some of their stamina
     */
    COMMAND,

    /**
     * The player gained stamina for an unspecified reason
     */
    SPECIAL,
}

