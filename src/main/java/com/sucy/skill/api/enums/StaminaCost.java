package com.sucy.skill.api.enums;

public enum StaminaCost
{
    /**
     * The player cast a skill that used some stamina
     */
    SKILL_CAST,

    /**
     * The player was affected by a skill effect that reduced their stamina
     */
    SKILL_EFFECT,

    /**
     * The player lost stamina for some unspecified reason
     */
    SPECIAL,
}
