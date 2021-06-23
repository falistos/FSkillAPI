package com.sucy.skill.api.util;

import com.sucy.skill.api.enums.LunarType;
import org.bukkit.World;

public class LunarAPI {

    public static LunarType Lunar(World world) {
        long fullTime= world.getFullTime();
        int day = Math.toIntExact(fullTime / 24000);
        long nowTime = world.getTime();
        int weekday = day % 8;
        if (weekday == 0) {
            return LunarType.FULL_MOON;
        }
        else if (weekday == 1) {
            return LunarType.KUI_GIBBOUS_MOON;
        }
        else if (weekday == 2) {
            return LunarType.LAST_QUARTER;
        }
        else if (weekday == 3) {
            return LunarType.WANING_MOON;
        }
        else if (weekday == 4) {
            return LunarType.NEW_MOON;
        }
        else if (weekday == 5) {
            return LunarType.CRESCENT_MOON;
        }
        else if (weekday == 6) {
            return LunarType.FIRST_QUARTER;
        }
        else if (weekday == 7) {
            return LunarType.WAXING_GIBBOUS_MOON;
        } else return null;
    }
}

