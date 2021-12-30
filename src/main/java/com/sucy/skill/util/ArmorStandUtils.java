package com.sucy.skill.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

public class ArmorStandUtils {
    public static Location faceLocation(Entity entity, Location to) {
        if (entity.getWorld() != to.getWorld()) {
            return null;
        }
        Location fromLocation = entity.getLocation();

        double xDiff = to.getX() - fromLocation.getX();
        double yDiff = to.getY() - fromLocation.getY();
        double zDiff = to.getZ() - fromLocation.getZ();

        double distanceXZ = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
        double distanceY = Math.sqrt(distanceXZ * distanceXZ + yDiff * yDiff);

        double yaw = Math.toDegrees(Math.acos(xDiff / distanceXZ));
        double pitch = Math.toDegrees(Math.acos(yDiff / distanceY)) - 90.0D;
        if (zDiff < 0.0D) {
            yaw += Math.abs(180.0D - yaw) * 2.0D;
        }
        Location loc = entity.getLocation();
        loc.setYaw((float) (yaw - 90.0F));
        loc.setPitch((float) (pitch - 90.0F));
        return loc;
    }
    public static EulerAngle convertVectorToEulerAngle(Vector vec) {

        double x = vec.getX();
        double y = vec.getY();
        double z = vec.getZ();

        double xz = Math.sqrt(x*x + z*z);

        double eulX;
        if(x < 0) {
            if(y == 0) {
                eulX = Math.PI*0.5;
            } else {
                eulX = Math.atan(xz/y)+Math.PI;
            }
        } else {
            eulX = Math.atan(y/xz)+Math.PI*0.5;
        }

        double eulY;
        if(x == 0) {
            if(z > 0) {
                eulY = Math.PI;
            } else {
                eulY = 0;
            }
        } else {
            eulY = Math.atan(z/x)+Math.PI*0.5;
        }

        return new EulerAngle(eulX, eulY, 0);

    }
    public static void setRotationAccordingToVector(ArmorStand armorStand, Vector vector){
        EulerAngle eulerAngle = convertVectorToEulerAngle(vector);
        armorStand.setHeadPose(eulerAngle);
    }
    public static void setRotationFacingTarget(ArmorStand armorStand, Entity target){
        Location newLocation = faceLocation(armorStand, target.getLocation());
        //armorStand.setHeadPose(newLocation.getDirection());
    }
}
