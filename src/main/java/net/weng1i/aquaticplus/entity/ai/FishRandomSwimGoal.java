package net.weng1i.aquaticplus.entity.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class FishRandomSwimGoal extends RandomStrollGoal {
    private final int xzSpread;
    private final boolean submerged;
    private int ySpread = 3;

    public FishRandomSwimGoal(PathfinderMob creature, double speed, int chance, int xzSpread) {
        super(creature, speed, chance, false);
        this.xzSpread = xzSpread;
        this.submerged = false;
    }

    public FishRandomSwimGoal(PathfinderMob creature, double speed, int chance, int xzSpread, boolean submerged) {
        super(creature, speed, chance, false);
        this.xzSpread = xzSpread;
        this.submerged = submerged;
    }

    public FishRandomSwimGoal(PathfinderMob creature, double speed, int chance, int xzSpread, int ySpread, boolean submerged) {
        super(creature, speed, chance, false);
        this.xzSpread = xzSpread;
        this.ySpread = ySpread;
        this.submerged = submerged;
    }

    public boolean canUse() {
        if (this.mob.isVehicle()|| mob.getTarget() != null || !this.mob.isInWater() && !this.mob.isInLava()) {
            return false;
        } else {
            if (!this.forceTrigger) {
                if (this.mob.getRandom().nextInt(this.interval) != 0) {
                    return false;
                }
            }
            Vec3 vector3d = this.getPosition();
            if (vector3d == null) {
                return false;
            } else {
                this.wantedX = vector3d.x;
                this.wantedY = vector3d.y;
                this.wantedZ = vector3d.z;
                this.forceTrigger = false;
                return true;
            }
        }
    }

    @Nullable
    protected Vec3 getPosition() {
        if(this.mob.hasRestriction() && this.mob.distanceToSqr(Vec3.atCenterOf(this.mob.getRestrictCenter())) > this.mob.getRestrictRadius() * this.mob.getRestrictRadius()){
            return DefaultRandomPos.getPosTowards(this.mob, xzSpread, 3, Vec3.atBottomCenterOf(this.mob.getRestrictCenter()), 3);
        }
        if(this.mob.getRandom().nextFloat() < 0.3F){
            Vec3 vector3d = findSurfaceTarget(this.mob, xzSpread, ySpread * 2);
            if(vector3d != null){
                return vector3d;
            }
        }
        Vec3 vector3d = DefaultRandomPos.getPos(this.mob, xzSpread, ySpread);

        for(int i = 0; vector3d != null && !this.mob.level().getBlockState(fromVec3(vector3d)).isPathfindable(this.mob.level(), fromVec3(vector3d), PathComputationType.WATER) && i++ < 15; vector3d = DefaultRandomPos.getPos(this.mob, 10, ySpread)) {
        }
        if(submerged && vector3d != null){
            if(!this.mob.level().getFluidState(fromVec3(vector3d).above()).is(FluidTags.WATER)){
                vector3d = vector3d.add(0, -2, 0);
            }else if(!this.mob.level().getFluidState(fromVec3(vector3d).above(2)).is(FluidTags.WATER)){
                vector3d = vector3d.add(0, -3, 0);
            }
        }
        return vector3d;
    }

    private boolean canJumpTo(BlockPos pos, int dx, int dz, int scale) {
        BlockPos blockpos = pos.offset(dx * scale, 0, dz * scale);
        return this.mob.level().getFluidState(blockpos).is(FluidTags.LAVA) || this.mob.level().getFluidState(blockpos).is(FluidTags.WATER) && !this.mob.level().getBlockState(blockpos).blocksMotion();
    }

    private boolean isAirAbove(BlockPos pos, int dx, int dz, int scale) {
        return this.mob.level().getBlockState(pos.offset(dx * scale, 1, dz * scale)).isAir() && this.mob.level().getBlockState(pos.offset(dx * scale, 2, dz * scale)).isAir();
    }

    private Vec3 findSurfaceTarget(PathfinderMob creature, int i, int i1) {
        BlockPos upPos = creature.blockPosition();
        while(creature.level().getFluidState(upPos).is(FluidTags.WATER) || creature.level().getFluidState(upPos).is(FluidTags.LAVA)){
            upPos = upPos.above();
        }
        if(isAirAbove(upPos.below(), 0, 0, 0) && canJumpTo(upPos.below(), 0, 0, 0)){
            return new Vec3(upPos.getX() + 0.5F, upPos.getY() - 1F, upPos.getZ() + 0.5F);
        }
        return null;
    }

    public static final BlockPos fromCoords(double x, double y, double z) {
        return new BlockPos((int)x, (int)y, (int)z);
    }

    public static final BlockPos fromVec3(Vec3 vec3){
        return fromCoords(vec3.x, vec3.y, vec3.z);
    }
}