/**
 * SkillAPI
 * com.sucy.skill.api.projectile.ParticleProjectile
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Steven Sucy
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software") to deal
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
package com.sucy.skill.api.projectile;

import com.sk89q.worldedit.util.Direction;
import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.Settings;
import com.sucy.skill.api.event.ParticleProjectileExpireEvent;
import com.sucy.skill.api.event.ParticleProjectileHitEvent;
import com.sucy.skill.api.event.ParticleProjectileLandEvent;
import com.sucy.skill.api.event.ParticleProjectileLaunchEvent;
import com.sucy.skill.api.util.ParticleHelper;
import com.sucy.skill.dynamic.target.RememberTarget;
import com.sucy.skill.util.ArmorStandUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.atan2;
import static java.lang.Math.hypot;

/**
 * A fake projectile that plays particles along its path
 */
public class ParticleProjectile extends CustomProjectile
{
    /**
     * Settings key for the projectile speed
     */
    public static final String SPEED = "velocity";

    /**
     * Settings key for the projectile lifespan
     */
    private static final String LIFESPAN = "lifespan";

    /**
     * Settings key for the projectile's frequency of playing particles
     */
    private static final String FREQUENCY = "frequency";

    /**
     * Settings key for the projectile's effective gravity
     */
    private static final String GRAVITY = "gravity";

    private static final String PIERCE = "pierce";
    private static final String MAX_PIERCE_AMOUNT = "max_pierce_amount";

    private Location loc;
    private Settings settings;
    private Vector   vel;
    private int      steps;
    private int      count;
    private int      freq;
    private int      life;
    private Vector   gravity;
    private boolean  pierce;
    private int maxPierceAmount;
    private boolean   missileTargetKeepUpdating;
    private String   missileTargetID;
    private double   missileThreshold;
    private double   missileAngle;
    private double   missileDelay;
    private Entity   currentMissileTarget;
    private boolean  missileStarted = false;
    private double collisionRadius = 1.5;
    private ArmorStand hiddenArmorStand = null;
    private String speedFormula = "none";
    private int tick;

    /**
     * Constructor
     *
     * @param shooter  entity that shot the projectile
     * @param level    level to use for scaling the speed
     * @param loc      initial location of the projectile
     * @param settings settings for the projectile
     */
    public ParticleProjectile(LivingEntity shooter,
                              int level,
                              Location loc,
                              Settings settings,
                              double collisionRadius,
                              boolean missileTargetKeepUpdating,
                              String missileTargetID,
                              double missileThreshold,
                              double missileAngle,
                              double missileDelay,
                              ItemStack customModelItemStack,
                              String speedFormula)
    {
        super(shooter);

        //#region Input data
        this.loc = loc;
        this.settings = settings;
        this.vel = loc.getDirection().multiply(settings.getAttr(SPEED, level, 1.0));
        this.freq = (int) (20 * settings.getDouble(FREQUENCY, 0.5));
        this.life = (int) (settings.getDouble(LIFESPAN, 2) * 20);
        this.tick = 1;
        this.gravity = new Vector(0, settings.getDouble(GRAVITY, 0), 0);
        this.pierce = settings.getBool(PIERCE, false);
        this.maxPierceAmount = settings.getInt(MAX_PIERCE_AMOUNT, 1);
        this.collisionRadius = collisionRadius;
        this.missileTargetKeepUpdating = missileTargetKeepUpdating;
        this.missileTargetID = missileTargetID;
        this.missileThreshold = missileThreshold;
        this.missileAngle = missileAngle;
        this.missileDelay = missileDelay;
        this.speedFormula = speedFormula;
        steps = (int) Math.ceil(vel.length() * 2);
        vel.multiply(1.0 / steps);
        gravity.multiply(1.0 / steps);

        Bukkit.getPluginManager().callEvent(new ParticleProjectileLaunchEvent(this));
        //#endregion

        //Initializing systems
        initMissileSystem();
        initCustomModelSystem(customModelItemStack);
    }
    //#region Missile system
    /**
     * To initialize the missile system
    */
    private void initMissileSystem(){
        //Update the missile target from remember target ID first.
        updateMissileTarget();
        //Missile delay
        if(missileDelay == 0){
            missileStarted = true;
        }else {
            //start the missile after a delay
            startMissileAfterSeconds(this.missileDelay);
        }
    }
    /**
     * Start the missile after a given seconds. Before that the projectile will go straight.
     * @param missileDelay the delay in seconds
     * */
    private void startMissileAfterSeconds(double missileDelay){
        new BukkitRunnable() {

            @Override
            public void run() {
                missileStarted = true;
            }
        }.runTaskLater(SkillAPI.singleton, Math.round(missileDelay * 20));
    }
    /**
     * To update the missile target from the remember target ID
     */
    private void updateMissileTarget(){
        //Get the missile targets from remember target list.
        final List<LivingEntity> missileTargets = RememberTarget.remember(this.getShooter(), missileTargetID);
        if(missileTargets.size() > 0) {
            currentMissileTarget = missileTargets.get(0);
        }
    }
    /**
     * To update missile system including physics and updating missile target
     */
    private void updateMissileSystem(){
        if(missileTargetKeepUpdating){
            updateMissileTarget();//update the missile target from the remember ID given in the beginning
        }
        if (currentMissileTarget != null && missileStarted) {
            final Vector vel = getVelocity();
            final double speed = vel.length();
            final Vector dir = vel.multiply(1 / speed);
            final Vector towards = currentMissileTarget.getLocation().toVector().subtract(getLocation().toVector());
            final Vector targetDir = towards.normalize();
            final double dot = dir.dot(targetDir);//degree of inaccurate
            if (dot >= missileThreshold) {
                setVelocity(targetDir.multiply(speed));
            } else {
                Vector currentDir = vel.normalize();
                Vector sum = currentDir.add(targetDir);
                Vector newDir = sum.divide(new Vector(2, 2, 2));
                Vector normalizedNewDir = newDir.normalize();
                setVelocity(normalizedNewDir.multiply(speed));
            }
        }
    }
    //#endregion

    //#region Custom model (hidden armorstand)
    /**
     * To initialize the custom model system
     *
     * @param customModelItemStack the itemstack with custom 3D model
     */
    private void initCustomModelSystem(ItemStack customModelItemStack){
        if(customModelItemStack!=null){
            //create custom model
            createHiddenArmorStand(customModelItemStack);

            //update the physic of the custom model
            updateCustomModelSystem();
        }
    }

    /**
     * To create a hidden armorstand which contain the itemstack on its head as custom model.
     * @param customModelItemStack the itemstack with custom 3D model
     */
    private void createHiddenArmorStand(ItemStack customModelItemStack){
        Location armorStandLocation = loc.clone();
        World world = loc.getWorld();
        armorStandLocation.setDirection(new Vector(0,0,1));
        armorStandLocation.add(0, -1, 0);
        Entity hiddenArmorStandEntity = world.spawnEntity(armorStandLocation, EntityType.ARMOR_STAND);
        hiddenArmorStand = ((ArmorStand) hiddenArmorStandEntity);
        //Put the item on its head as a helmet
        hiddenArmorStand.getEquipment().setHelmet(customModelItemStack);
        hiddenArmorStand.setInvulnerable(true);
        hiddenArmorStand.setVisible(false);
        hiddenArmorStand.setCollidable(false);
        hiddenArmorStand.setSmall(true);
    }

    /**
     * Destroy the hidden armorstand.
     */
    public void destroyHiddenArmorStand(){
        if(hiddenArmorStand==null) return;
        //teleport it to void to prevent death animation
        hiddenArmorStand.teleport(hiddenArmorStand.getLocation().add(0, -1000, 0));
        //kill it
        hiddenArmorStand.setHealth(0.0);
    }

    /**
     * To update the custom model (hidden armorstand) physic. Including velocity and looking direction
     */
    private void updateCustomModelSystem(){
        if(hiddenArmorStand!=null){
            final Vector vel = getVelocity().clone();
            final double speed = vel.length();
            final Vector dir = vel.multiply(1 / speed);

            Location armorStandLocation = loc.clone();
            armorStandLocation.setDirection(new Vector(0,0,1));
            armorStandLocation.add(0, -1, 0);
            hiddenArmorStand.teleport(armorStandLocation);
            double yaw = -atan2(dir.getX(), dir.getZ());
            double pitch = -atan2(dir.getY(), hypot(dir.getX(), dir.getZ()));
            EulerAngle eulerAngle = new EulerAngle(pitch, yaw, 0);
            hiddenArmorStand.setHeadPose(eulerAngle);
            hiddenArmorStand.setVelocity(getVelocity());
        }
    }
    //#endregion

    //#region Speed Formula system
    private void updateSpeedWithFormula(){
        if(speedFormula!="none" && speedFormula!=null){
            ScriptEngineManager mgr = new ScriptEngineManager();
            ScriptEngine engine = mgr.getEngineByName("JavaScript");
            String mathLine = speedFormula.replace("t", tick+"");
            try {
                float result = Float.parseFloat(engine.eval(mathLine).toString())   ;
                Vector vel = this.vel;
                Vector dir = vel.normalize();
                Vector newVel = dir.multiply(result);
                setVelocity(newVel);
            } catch (ScriptException e) {
                e.printStackTrace();
            }
        }
    }
    //#endregion
    /**
     * Retrieves the location of the projectile
     *
     * @return location of the projectile
     */
    @Override
    public Location getLocation()
    {
        return loc;
    }

    /**
     * Handles expiring due to range or leaving loaded chunks
     */
    @Override
    protected Event expire()
    {
        destroyHiddenArmorStand();
        return new ParticleProjectileExpireEvent(this);
    }

    /**
     * Handles landing on terrain
     */
    @Override
    protected Event land()
    {
        destroyHiddenArmorStand();
        return new ParticleProjectileLandEvent(this);
    }

    /**
     * Handles hitting an entity
     *
     * @param entity entity the projectile hit
     */
    @Override
    protected Event hit(LivingEntity entity)
    {
        destroyHiddenArmorStand();
        return new ParticleProjectileHitEvent(this, entity);
    }

    /**
     * @return true if passing through a solid block, false otherwise
     */
    @Override
    protected boolean landed()
    {
        return getLocation().getBlock().getType().isSolid();
    }

    /**
     * @return squared radius for colliding
     */
    @Override
    protected double getCollisionRadius()
    {
        return collisionRadius;
    }

    /**
     * @return velocity of the projectile
     */
    @Override
    public Vector getVelocity()
    {
        return vel;
    }

    /**
     * Teleports the projectile to a location
     *
     * @param loc location to teleport to
     */
    public void teleport(Location loc)
    {
        this.loc = loc;
    }

    /**
     * Sets the velocity of the projectile
     *
     * @param vel new velocity
     */
    @Override
    public void setVelocity(Vector vel)
    {
        this.vel = vel;
    }

    /**
     * Updates the projectiles position and checks for collisions
     */
    @Override
    public void run()
    {
        //update the speed formula
        updateSpeedWithFormula();
        // Go through multiple steps to avoid tunneling
        for (int i = 0; i < steps; i++)
        {
            loc.add(vel);
            vel.add(gravity);

            if (!isTraveling())
                return;

            if (!checkCollision(pierce, maxPierceAmount)) break;
        }

        // Particle along path
        count++;
        if (count >= freq)
        {
            count = 0;
            ParticleHelper.play(loc, settings);
        }

        // Lifespan
        life--;
        if (life <= 0)
        {
            cancel();
            Bukkit.getPluginManager().callEvent(new ParticleProjectileExpireEvent(this));
        }

        tick++;

        //Missile
        updateMissileSystem();

        //Custom model
        updateCustomModelSystem();

    }
    /**
     * Fires a spread of projectiles from the location.
     *
     * @param shooter  entity shooting the projectiles
     * @param level    level to use for scaling the speed
     * @param center   the center direction of the spread
     * @param loc      location to shoot from
     * @param settings settings to use when firing
     * @param angle    angle of the spread
     * @param amount   number of projectiles to fire
     * @param callback optional callback for when projectiles hit
     *
     * @return list of fired projectiles
     */
    public static ArrayList<ParticleProjectile> spread(LivingEntity shooter,
                                                       int level,
                                                       Vector center,
                                                       Location loc,
                                                       Settings settings,
                                                       double angle,
                                                       int amount,
                                                       ProjectileCallback callback,
                                                       double collisionRadius,
                                                       boolean missileTargetKeepUpdating,
                                                       String missileTargetID,
                                                       double missileThreshold,
                                                       double missileAngle,
                                                       double missileDelay,
                                                       ItemStack customModelItemStack,
                                                       String speedFormula)
    {
        ArrayList<Vector> dirs = calcSpread(center, angle, amount);
        ArrayList<ParticleProjectile> list = new ArrayList<ParticleProjectile>();
        for (Vector dir : dirs)
        {
            Location l = loc.clone();
            l.setDirection(dir);
            ParticleProjectile p = new ParticleProjectile(shooter,
                    level,
                    l,
                    settings,
                    collisionRadius,
                    missileTargetKeepUpdating,
                    missileTargetID,
                    missileThreshold,
                    missileAngle,
                    missileDelay,
                    customModelItemStack,
                    speedFormula);
            p.setCallback(callback);
            list.add(p);
        }
        return list;
    }

    /**
     * Fires a spread of projectiles from the location.
     *
     * @param shooter  entity shooting the projectiles
     * @param level    level to use for scaling the speed
     * @param center   the center location to rain on
     * @param settings settings to use when firing
     * @param radius   radius of the circle
     * @param height   height above the center location
     * @param amount   number of projectiles to fire
     * @param callback optional callback for when projectiles hit
     *
     * @return list of fired projectiles
     */
    public static ArrayList<ParticleProjectile> rain(LivingEntity shooter,
                                                     int level,
                                                     Location center,
                                                     Settings settings,
                                                     double radius,
                                                     double height,
                                                     int amount,
                                                     ProjectileCallback callback,
                                                     double collisionRadius,
                                                     boolean missileTargetKeepUpdating,
                                                     String missileTargetID,
                                                     double missileThreshold,
                                                     double missileAngle,
                                                     double missileDelay,
                                                     ItemStack customModelItemStack,
                                                     String speedFormula)
    {
        Vector vel = new Vector(0, 1, 0);
        ArrayList<Location> locs = calcRain(center, radius, height, amount);
        ArrayList<ParticleProjectile> list = new ArrayList<ParticleProjectile>();
        for (Location l : locs)
        {
            l.setDirection(vel);
            ParticleProjectile p = new ParticleProjectile(shooter,
                    level,
                    l,
                    settings,
                    collisionRadius,
                    missileTargetKeepUpdating,
                    missileTargetID,
                    missileThreshold,
                    missileAngle,
                    missileDelay,
                    customModelItemStack,
                    speedFormula);
            p.setCallback(callback);
            list.add(p);
        }
        return list;
    }
}
