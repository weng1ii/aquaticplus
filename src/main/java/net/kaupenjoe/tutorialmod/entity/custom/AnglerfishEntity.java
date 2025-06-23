package net.kaupenjoe.tutorialmod.entity.custom;

import net.kaupenjoe.tutorialmod.config.SpawnConfig;
import net.kaupenjoe.tutorialmod.entity.ModEntities;
import net.kaupenjoe.tutorialmod.entity.ai.SwimGoalAngler;
import net.kaupenjoe.tutorialmod.entity.ai.WaterEntityController;
import net.kaupenjoe.tutorialmod.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class AnglerfishEntity extends WaterAnimal  implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private int chaseTime = 0;
    private boolean isAttacking = false;

    public AnglerfishEntity(EntityType<? extends WaterAnimal> type, Level level) {
        super(type, level);
        this.moveControl = new WaterEntityController(this, 1.0F, 15F);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER_BORDER, 0.0F);
        this.xpReward = 10;
    }

    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return ModEntities.rollSpawn(SpawnConfig.anglerfishSpawnRolls, this.getRandom(), spawnReasonIn);
    }

    public static <T extends Mob> boolean canAnglefishSpawn(EntityType<AnglerfishEntity> entityType, ServerLevelAccessor iServerWorld, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return reason == MobSpawnType.SPAWNER || iServerWorld.isWaterAt(pos) && iServerWorld.isWaterAt(pos.above());
    }

    @javax.annotation.Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @javax.annotation.Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        if (reason == MobSpawnType.NATURAL) {
            doInitialPosing(worldIn);
        }
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    private void doInitialPosing(LevelAccessor world) {
        BlockPos down = this.blockPosition();
        while(!world.getFluidState(down).isEmpty() && down.getY() > 1){
            down = down.below();
        }
        this.setPos(down.getX() + 0.5F, down.getY() + 3 + random.nextInt(3), down.getZ() + 0.5F);
    }

    protected PathNavigation createNavigation(Level worldIn) {
        return new WaterBoundPathNavigation(this, worldIn);
    }

    public void aiStep() {
        this.updateSwingTime();
        this.updateNoActionTime();
        super.aiStep();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return WaterAnimal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 100.0D)
                .add(Attributes.FOLLOW_RANGE, 18D)
                .add(Attributes.ATTACK_DAMAGE, 16.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.34F)
                .add(Attributes.ATTACK_KNOCKBACK, 1D)
                .add(Attributes.ATTACK_SPEED, 1.6D);
    }

    protected void updateNoActionTime() {
        float f = this.getLightLevelDependentMagicValue();
        if (f > 0.5F) {
            this.noActionTime += 2;
        }
    }

    @Override
    protected void updateSwingTime() {
        int i = this.getCurrentSwingDuration();
        if (this.swinging) {
            ++this.swingTime;
            if (this.swingTime >= i) {
                this.swingTime = 0;
                this.swinging = false;
            }
        } else {
            this.swingTime = 0;
        }

        this.attackAnim = (float)this.swingTime / (float)i;
    }

    @Override
    public void swing(InteractionHand pHand) {
        this.swing(pHand, false);
    }

    @Override
    public void swing(InteractionHand pHand, boolean pUpdateSelf) {
        ItemStack stack = this.getItemInHand(pHand);
        if (!stack.isEmpty() && stack.onEntitySwing(this)) return;
        if (!this.swinging || this.swingTime >= this.getCurrentSwingDuration() / 2 || this.swingTime < 0) {
            this.swingTime = -1;
            this.swinging = true;
            this.swingingArm = pHand;
            if (this.level() instanceof ServerLevel) {
                ClientboundAnimatePacket clientboundanimatepacket = new ClientboundAnimatePacket(this, pHand == InteractionHand.MAIN_HAND ? 0 : 3);
                ServerChunkCache serverchunkcache = ((ServerLevel)this.level()).getChunkSource();
                if (pUpdateSelf) {
                    serverchunkcache.broadcastAndSend(this, clientboundanimatepacket);
                } else {
                    serverchunkcache.broadcast(this, clientboundanimatepacket);
                }
            }
        }

    }

    private int getCurrentSwingDuration() {
        return 22;
    }


    public int getMaxSpawnClusterSize() {
        return 1;
    }

    public boolean isMaxGroupSizeReached(int sizeIn) {
        return false;
    }



    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2D, true));
        this.goalSelector.addGoal(3, new TryFindWaterGoal(this));
        this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1.0D, 10));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(8, new FollowBoatGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, (new NearestAttackableTargetGoal(this, Player.class, true){
            public boolean canContinueToUse() {
                return chaseTime >= 0 && super.canContinueToUse();}
        }));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Squid.class, false));
    }

    public void travel(Vec3 travelVector) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(this.getSpeed(), travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            float f = 0.6F;
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.9D, f, 0.9D));
            if (this.getTarget() == null) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.005D, 0.0D));
            }
        } else {
            super.travel(travelVector);
        }
    }

    public static void applyDarknessAround(ServerLevel pLevel, Vec3 pPos, @javax.annotation.Nullable Entity pSource, int pRadius) {
        MobEffectInstance mobeffectinstance = new MobEffectInstance(MobEffects.DARKNESS, 260, 0, false, false);
        MobEffectUtil.addEffectToPlayersAround(pLevel, pSource, pPos, (double)pRadius, mobeffectinstance, 200);
    }

    @Override
    public void tick() {
        super.tick();
        if (chaseTime < 0)
            chaseTime++;
        if (!this.level().isClientSide) {
            ServerLevel serverlevel = (ServerLevel)this.level();
            LivingEntity target = this.getTarget();
            if (target != null) {
                chaseTime++;
                double distance = this.distanceTo(target);
                if (chaseTime > 400 && distance > (target instanceof Player ? 5 : 10)) {
                    chaseTime = -40;
                    this.setTarget(null);
                    this.setLastHurtByMob(null);
                    this.setLastHurtMob(null);
                    this.lastHurtByPlayer = null;
                }
                isAttacking = target instanceof Player && this.distanceTo(target) <= 3;
                if(isAttacking){
                    applyDarknessAround(serverlevel, this.position(), this, 14);
                }
            }
            else {
                stopTriggeredAnimation("chasing_controller", "chasing");
            }
        }
    }

    public MobType getMobType() {return MobType.WATER;}



    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.ANGLER_AMBIENT.get();
    }


    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.ANGLER_DEAD.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return ModSounds.ANGLER_BITE.get();
    }

    @Nullable
    public LivingEntity getTarget() {
        return this.chaseTime < 0 ? null : super.getTarget();
    }

    public void setTarget(@Nullable LivingEntity entitylivingbaseIn) {
        if (this.chaseTime >= 0) {
            if (!isAttacking)
                triggerAnim("chasing_controller", "chasing");
            else {stopTriggeredAnimation("chasing_controller", "chasing");}
            super.setTarget(entitylivingbaseIn);

        } else {
            stopTriggeredAnimation("chasing_controller", "chasing");
            super.setTarget(null);
        }
    }

    @Nullable
    public LivingEntity getLastHurtByMob() {
        return this.chaseTime < 0 ? null : super.getLastHurtByMob();
    }

    public void setLastHurtByMob(@Nullable LivingEntity entitylivingbaseIn) {
        if (this.chaseTime >= 0) {
            if (!isAttacking)
                triggerAnim("chasing_controller", "chasing");
            else {stopTriggeredAnimation("chasing_controller", "chasing");}
            super.setLastHurtByMob(entitylivingbaseIn);
        } else {
            stopTriggeredAnimation("chasing_controller", "chasing");
            super.setLastHurtByMob(null);
        }
    }

    //Geckolib Animations

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(DefaultAnimations.genericLivingController(this));
        controllers.add(DefaultAnimations.genericAttackAnimation(this, DefaultAnimations.ATTACK_BITE));
        controllers.add(new AnimationController<>(this, "chasing_controller", state -> PlayState.STOP)
                .triggerableAnim("chasing", RawAnimation.begin().then("chasing", Animation.LoopType.LOOP)));
    }

    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }


    public static class FishLikeMoveControl extends MoveControl {
        private final WaterAnimal fish;

        public FishLikeMoveControl(WaterAnimal fish) {
            super(fish);
            this.fish = fish;
        }

        @Override
        public void tick() {
            if (this.operation == Operation.MOVE_TO && !this.fish.getNavigation().isDone()) {
                double dx = this.wantedX - this.fish.getX();
                double dy = this.wantedY - this.fish.getY();
                double dz = this.wantedZ - this.fish.getZ();
                double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
                dy /= dist;
                this.fish.setDeltaMovement(this.fish.getDeltaMovement().add(0.0D, dy * 0.1D, 0.0D));
                float targetYaw = (float) (Mth.atan2(dz, dx) * (180F / Math.PI)) - 90.0F;
                this.fish.setYRot(this.rotlerp(this.fish.getYRot(), targetYaw, 10.0F));
                this.fish.yBodyRot = this.fish.getYRot();
                this.fish.yHeadRot = this.fish.getYRot();

                double baseSpeed = this.fish.getAttributeValue(Attributes.MOVEMENT_SPEED);
                float speedMultiplier = (float) this.speedModifier;
                float maxWaterSpeedFactor = 2f;
                float finalSpeed = speedMultiplier * (float) baseSpeed * maxWaterSpeedFactor;

                this.fish.setSpeed(finalSpeed);
            } else {
                this.fish.setSpeed(0.0F);
            }
        }
    }
}
