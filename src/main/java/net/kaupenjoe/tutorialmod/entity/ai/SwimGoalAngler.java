package net.kaupenjoe.tutorialmod.entity.ai;

import net.kaupenjoe.tutorialmod.entity.custom.AnglerfishEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class SwimGoalAngler extends Goal {
    private final AnglerfishEntity entity;
    private final double baseSpeed;
    private final double burstSpeed;
    private double x, y, z;
    private int cooldown;
    private static final int BURST_DURATION = 20;

    public SwimGoalAngler(AnglerfishEntity entity, double baseSpeed) {
        this.entity = entity;
        this.baseSpeed = baseSpeed;
        this.burstSpeed = baseSpeed * 2.0;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return entity.isInWater() && entity.getRandom().nextInt(10) == 0;
    }

    @Override
    public boolean canContinueToUse() {
        return cooldown > 0;
    }

    @Override
    public void start() {
        double angle = entity.getRandom().nextDouble() * 2 * Math.PI;
        double radius = 5.0;
        x = entity.getX() + Mth.cos((float) angle) * radius;
        y = entity.getY() + (entity.getRandom().nextDouble() * 2.0 - 1.0);
        z = entity.getZ() + Mth.sin((float) angle) * radius;
        cooldown = 60 + entity.getRandom().nextInt(40); // total duration
        entity.getNavigation().moveTo(x, y, z, burstSpeed);
    }

    @Override
    public void tick() {
        cooldown--;
        double currentSpeed = cooldown >= (cooldown - BURST_DURATION) ? burstSpeed : baseSpeed;

        if (entity.getNavigation().isDone()) {
            entity.getNavigation().moveTo(x, y, z, currentSpeed);
        } else {
            entity.getNavigation().setSpeedModifier(currentSpeed);
        }
    }
}
