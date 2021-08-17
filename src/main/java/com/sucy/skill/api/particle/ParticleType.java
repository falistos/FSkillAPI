/**
 * SkillAPI
 * com.sucy.skill.api.particle.ParticleType
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Steven Sucy
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.sucy.skill.api.particle;

public enum ParticleType
{
    ASH ("ash"),
    BARRIER("barrier"),
    BLOCK_CRACK("block crack", "blackcrack_", true),
    BLOCK_DUST("block dust","block_dust",true),
    BUBBLE_COLUMN_UP("bubble column up"),
    BUBBLE_POP("bubble pop"),
    CAMPFIRE_COSY_SMOKE("campfire cosy smoke"),
    CAMPFIRE_SIGNAL_SMOKE("campfire signal smoke"),
    CLOUD("cloud", "cloud"),
    COMPOSTER("composter"),
    CRIMSON_SPORE("Crimson spore"),
    CRIT("crit", "crit"),
    CRIT_MAGIC("magic crit", "magicCrit"),
    DAMAGE_INDICATOR("damage indicator"),
    DOLPHIN("dolphin"),
    DRAGON_BREATH("dragon breath"),
    DRIP_LAVA("drip lava", "dripLava"),
    DRIP_WATER("drip water", "dripWater"),
    DRIPPING_DRIPSTONE_LAVA("dripping dripstone lava"),
    DRIPPING_DRIPSTONE_WATER("dripping dripstone water"),
    DRIPPING_HONEY("dripping honey"),
    DRIPPING_OBSIDIAN_TEAR("dripping obsidian tear"),
    DUST_COLOR_TRANSITION("dust color transition"),
    ELECTRIC_SPARK("electric spark"),
    ENCHANTMENT_TABLE("enchantment table", "enchantmenttable"),
    END_ROD("end rod"),
    EXPLOSION_HUGE("huge explosion", "hugeexplosion"),
    EXPLOSION_LARGE("large explode", "largeexplode"),
    EXPLOSION_NORMAL("explode", "explode"),
    FALLING_DRIPSTONE_LAVA("falling dripstone lava"),
    FALLING_DRIPSTONE_WATER("dripping dripstone water"),
    FALLING_DUST("falling dust", "falling dust", true),
    FALLING_HONEY("falling honey"),
    FALLING_LAVA("falling lava"),
    FALLING_NECATR("falling nectar"),
    FALLING_OBSIDIAN_TEAR("falling obsidian tear"),
    FALLING_SPORE_BLOOSOM("falling spore blossom"),
    FALLING_WATER("falling water"),
    FIREWORKS_SPARK("firework spark", "fireworksSpark"),
    FLAME("flame", "flame"),
    FLASH("flash"),
    FOOTSTEP("footstep", "footstep"),
    GLOW("glow"),
    GLOW_SQUID_INK("glow squid ink"),
    HEART("heart", "heart"),
    ITEM_CRACK("item crack", "item crack", true),
    LANDING_HONEY("landing honey"),
    LANDING_LAVA("landing lava"),
    LANDING_OBSIDIAN_TEAR("landing obsidian tear"),
    LAVA("lava", "lava"),
    LIGHT("light"),
    MOB_APPEARANCE("mob appearance"),
    NAUTILUS("nautilus"),
    NOTE("note", "note"),
    PORTAL("portal", "portal"),
    REDSTONE("redstone", "reddust"),
    REVERSE_PORTAL("reverse portal"),
    SCRAPE("scrape"),
    SLIME("slime", "slime"),
    SMALL_FLAME("small flame"),
    SMOKE_NORMAL("smoke", "smoke"),
    SMOKE_LARGE("large smoke", "largesmoke"),
    SNEEZE("sneeze"),
    SNOWBALL("snowball poof", "snowballpoof"),
    SNOW_SHOVEL("snow shovel", "snowshovel"),
    SNOWFLAKE("snowflake"),
    SOUL("soul"),
    SOUL_FIRE_FLAME("soul fire flame"),
    SPELL("spell", "spell"),
    SPELL_INSTANT("instant spell", "instantSpell"),
    SPELL_MOB("mob spell", "mobSpell"),
    SPELL_MOB_AMBIENT("mob spell ambient", "mobSpellAmbient"),
    SPELL_WITCH("witch magic", "witchMagic"),
    SPIT("spit"),
    SQUID_INK("squid ink"),
    SUSPEND_DEPTH("depth suspend", "depthSuspend"),
    SUSPENDED("suspend", "suspend"),
    SWEEP_ATTACK("sweep attack"),
    TOTEM("totem"),
    TOWN_AURA("town aura", "townaura"),
    VIBRATION("vibration"),
    VILLAGER_ANGRY("angry villager", "angryVillager"),
    VILLAGER_HAPPY("happy villager", "happyVillager"),
    WARPED_SPORE("warped spore"),
    WATER_BUBBLE("bubble", "bubble"),
    WATER_SPLASH("splash", "splash"),
    WATER_WAKE("water wake"),
    WAX_OFF("wax off"),
    WAX_ON("wax on"),
    WHITE_ASH("white ash");


    private String  editor;
    private String  old;
    private boolean mat;

    ParticleType(String editorKey)
    {
        editor = editorKey;
        ParticleLookup.register(this);
    }

    ParticleType(String editorKey, String oldName)
    {
        editor = editorKey;
        old = oldName;
        ParticleLookup.register(this);
    }

    ParticleType(String editorKey, String oldName, boolean usesMat)
    {
        this(editorKey, oldName);
        mat = usesMat;
    }

    public String editorKey()
    {
        return editor;
    }

    public String oldName()
    {
        return old;
    }

    public boolean usesMat()
    {
        return mat;
    }
}
