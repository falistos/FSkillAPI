package com.sucy.skill.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.util.Vector;

public class ArmorStandUtils {
    public static void setRotationAccordingToVector(ArmorStand armorStand, Vector vector){
        Vector normalized = vector.clone().normalize();
        double deltaX = normalized.getX();
        double deltaY = normalized.getY();
        double deltaZ = normalized.getZ();
        double distance = Math.sqrt(deltaZ * deltaZ + deltaX * deltaX);
        double pitch = Math.asin(deltaY/distance);
        double yaw = Math.atan2(deltaX, deltaZ);
        Bukkit.broadcastMessage("deltaX: "+deltaX);
        Bukkit.broadcastMessage("deltaY: "+deltaY);
        Bukkit.broadcastMessage("deltaZ: "+deltaZ);
        Bukkit.broadcastMessage("pitch: "+pitch);
        Bukkit.broadcastMessage("yaw: "+yaw);
        armorStand.setRotation((float)pitch, (float)yaw);
    }
}
