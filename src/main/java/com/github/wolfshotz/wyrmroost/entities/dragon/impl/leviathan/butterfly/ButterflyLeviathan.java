package com.github.wolfshotz.wyrmroost.entities.dragon.impl.leviathan.butterfly;

import com.github.wolfshotz.wyrmroost.WRConfig;
import com.github.wolfshotz.wyrmroost.client.ClientEvents;
import com.github.wolfshotz.wyrmroost.client.screen.DragonControlScreen;
import com.github.wolfshotz.wyrmroost.containers.BookContainer;
import com.github.wolfshotz.wyrmroost.entities.dragon.TameableDragonEntity;
import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.DragonInventory;
import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.LessShitLookController;
import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.goals.*;
import com.github.wolfshotz.wyrmroost.entities.dragon.impl.leviathan.butterfly.goals.ButterflyLeviathanAttackGoal;
import com.github.wolfshotz.wyrmroost.entities.dragon.impl.leviathan.butterfly.goals.ButterflyLeviathanJumpOutOfWaterGoal;
import com.github.wolfshotz.wyrmroost.entities.util.EntitySerializer;
import com.github.wolfshotz.wyrmroost.entities.util.EntitySerializerBuilder;
import com.github.wolfshotz.wyrmroost.entities.util.EntitySerializerType;
import com.github.wolfshotz.wyrmroost.entities.util.data.DataParameterBuilder;
import com.github.wolfshotz.wyrmroost.items.book.action.BookActions;
import com.github.wolfshotz.wyrmroost.network.packets.AnimationPacket;
import com.github.wolfshotz.wyrmroost.network.packets.KeybindHandler;
import com.github.wolfshotz.wyrmroost.registry.WRSounds;
import com.github.wolfshotz.wyrmroost.util.LerpedFloat;
import com.github.wolfshotz.wyrmroost.util.Mafs;
import com.github.wolfshotz.wyrmroost.util.ModUtils;
import com.github.wolfshotz.wyrmroost.util.animation.Animation;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.ForgeMod;

import javax.annotation.Nullable;
import java.util.Random;

import static com.github.wolfshotz.wyrmroost.entities.util.EntityConstants.*;
import static net.minecraft.entity.ai.attributes.Attributes.*;

public class ButterflyLeviathan extends TameableDragonEntity {

    public final LerpedFloat beachedTimer = LerpedFloat.unit();
    public final LerpedFloat swimTimer = LerpedFloat.unit();
    public final LerpedFloat sitTimer = LerpedFloat.unit();
    public int lightningCooldown = 0;
    public boolean beached = true;
    private final DataParameter<Boolean> hasConduitData;

    public ButterflyLeviathan(EntityType<? extends TameableDragonEntity> dragon, World level) {
        super(dragon, level);
        noCulling = WRConfig.NO_CULLING.get();
        moveControl = new ButterflyLeviathanMovementController(this);
        maxUpStep = 2;
        this.hasConduitData = DataParameterBuilder.getDataParameter(this.getClass(), DataSerializers.BOOLEAN);
        setPathfindingMalus(PathNodeType.WATER, 0);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new WRSitGoal(this));
        goalSelector.addGoal(1, new MoveToHomeGoal(this));
        goalSelector.addGoal(2, new ButterflyLeviathanAttackGoal(this));
        goalSelector.addGoal(3, new WRFollowOwnerGoal(this));

        goalSelector.addGoal(4, new DragonBreedGoal(this));
        goalSelector.addGoal(5, new ButterflyLeviathanJumpOutOfWaterGoal(this));
        goalSelector.addGoal(6, new RandomSwimmingGoal(this, 1, 40));
        goalSelector.addGoal(7, new LookAtGoal(this, LivingEntity.class, 14f));
        goalSelector.addGoal(8, new LookRandomlyGoal(this));

        targetSelector.addGoal(0, new OwnerHurtByTargetGoal(this));
        targetSelector.addGoal(1, new OwnerHurtTargetGoal(this));
        targetSelector.addGoal(3, new HurtByTargetGoal(this));
        targetSelector.addGoal(4, new DefendHomeGoal(this));
        targetSelector.addGoal(5, new NonTamedTargetGoal<>(this, LivingEntity.class, false, e ->
        {
            EntityType<?> type = e.getType();
            return e.isInWater() == isInWater() && (type == EntityType.PLAYER || type == EntityType.GUARDIAN || type == EntityType.SQUID);
        }));
    }

    @Override
    public EntitySerializer<ButterflyLeviathan> getSerializer() {
        return EntitySerializerBuilder.getEntitySerializer(this.getClass(), EntitySerializerType.VARIANT);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(hasConduitData, false);
        entityData.define(variantData, 0);
    }

    @Override
    public void aiStep() {
        super.aiStep();

        Vector3d conduitPos = getConduitPos();

        // cooldown for lightning attack
        if (lightningCooldown > 0) --lightningCooldown;

        // handle "beached" logic (if this fat bastard is on land)
        boolean prevBeached = beached;
        if (!beached && onGround && !wasTouchingWater) beached = true;
        else if (beached && wasTouchingWater) beached = false;
        if (prevBeached != beached) refreshDimensions();
        beachedTimer.add((beached) ? 0.1f : -0.05f);
        swimTimer.add(isUnderWater() ? -0.1f : 0.1f);
        sitTimer.add(isInSittingPose() ? 0.1f : -0.1f);

        if (isJumpingOutOfWater()) {
            Vector3d motion = getDeltaMovement();
            xRot = (float) (Math.signum(-motion.y) * Math.acos(Math.sqrt(Entity.getHorizontalDistanceSqr(motion)) / motion.length()) * (double) (180f / Mafs.PI)) * 0.725f;
        }

        // conduit effects
        if (hasConduit()) {
            if (level.isClientSide && isInWaterRainOrBubble() && getRandom().nextDouble() <= 0.1) {
                for (int i = 0; i < 16; ++i)
                    level.addParticle(ParticleTypes.NAUTILUS,
                            conduitPos.x,
                            conduitPos.y + 2.25,
                            conduitPos.z,
                            Mafs.nextDouble(getRandom()) * 1.5f,
                            Mafs.nextDouble(getRandom()),
                            Mafs.nextDouble(getRandom()) * 1.5f);
            }

            // nearby entities: if evil, kill, if not, give reallly cool potion effect
            if (tickCount % 80 == 0) {
                boolean attacked = false;
                for (LivingEntity entity : getEntitiesNearby(25, Entity::isInWaterRainOrBubble)) {
                    if (entity != getTarget() && (entity instanceof PlayerEntity || isAlliedTo(entity)))
                        entity.addEffect(new EffectInstance(Effects.CONDUIT_POWER, 220, 0, true, true));

                    if (!attacked && entity instanceof IMob) {
                        attacked = true;
                        entity.hurt(DamageSource.MAGIC, 4);
                        playSound(SoundEvents.CONDUIT_ATTACK_TARGET, 1, 1);
                    }
                }
            }

            // play some sounds because immersion is important for some reason
            if (level.isClientSide && tickCount % 100 == 0)
                if (getRandom().nextBoolean()) playSound(SoundEvents.CONDUIT_AMBIENT, 1f, 1f, true);
                else playSound(SoundEvents.CONDUIT_AMBIENT_SHORT, 1f, 1f, true);
        }
    }

    public void lightningAnimation(int time) {
        lightningCooldown += 6;
        if (time == 10) playSound(WRSounds.ENTITY_BFLY_ROAR.get(), 3f, 1f, true);
        if (!level.isClientSide && isInWaterRainOrBubble() && time >= 10) {
            LivingEntity target = getTarget();
            if (target != null) {
                if (hasConduit()) {
                    if (time % 10 == 0) {
                        Vector3d vec3d = target.position().add(Mafs.nextDouble(getRandom()) * 2.333, 0, Mafs.nextDouble(getRandom()) * 2.333);
                        createLightning(level, vec3d, false);
                    }
                } else if (time == 10) createLightning(level, target.position(), false);
            }
        }
    }

    public void conduitAnimation(int time) {
        ((LessShitLookController) getLookControl()).stopLooking();
        if (time == 0) playSound(WRSounds.ENTITY_BFLY_ROAR.get(), 5f, 1, true);
        else if (time == 15) {
            playSound(SoundEvents.BEACON_ACTIVATE, 1, 1);
            if (!level.isClientSide) createLightning(level, getConduitPos().add(0, 1, 0), true);
            else {
                Vector3d conduitPos = getConduitPos();
                for (int i = 0; i < 26; ++i) {
                    double velX = Math.cos(i);
                    double velZ = Math.sin(i);
                    level.addParticle(ParticleTypes.CLOUD, conduitPos.x, conduitPos.y + 0.8, conduitPos.z, velX, 0, velZ);
                }
            }
        }
    }

    public void biteAnimation(int time) {
        if (time == 0) playSound(WRSounds.ENTITY_BFLY_HURT.get(), 1, 1, true);
        else if (time == 6)
            attackInBox(getBoundingBox().move(Vector3d.directionFromRotation(isUnderWater() ? xRot : 0, yHeadRot).scale(5.5f)).inflate(0.85), 40);
    }

    @Override
    public ActionResultType playerInteraction(PlayerEntity player, Hand hand, ItemStack stack) {
        if (((beached && lightningCooldown > 60 && level.isRainingAt(blockPosition())) || player.isCreative() || isHatchling()) && isFood(stack)) {
            eat(stack);
            if (!level.isClientSide) tame(getRandom().nextDouble() < 0.2, player);
            return ActionResultType.sidedSuccess(level.isClientSide);
        }

        return super.playerInteraction(player, hand, stack);
    }

    @Override
    public void travel(Vector3d vec3d) {
        if (isInWater()) {
            if (canBeControlledByRider()) {
                float speed = getTravelSpeed() * 0.225f;
                LivingEntity entity = (LivingEntity) getControllingPassenger();
                double moveY = vec3d.y;
                double moveX = vec3d.x;
                double moveZ = entity.zza;

                yHeadRot = entity.yHeadRot;
                if (!isJumpingOutOfWater()) xRot = entity.xRot * 0.5f;
                double lookY = entity.getLookAngle().y;
                if (entity.zza != 0 && (isUnderWater() || lookY < 0)) moveY = lookY;

                setSpeed(speed);
                vec3d = new Vector3d(moveX, moveY, moveZ);
            }

            // add motion if were coming out of water fast; jump out of water like a dolphin
            if (getDeltaMovement().y > 0.25 && level.getBlockState(new BlockPos(getEyePosition(1)).above()).getFluidState().isEmpty())
                setDeltaMovement(getDeltaMovement().multiply(1.2, 1.5f, 1.2d));

            moveRelative(getSpeed(), vec3d);
            move(MoverType.SELF, getDeltaMovement());
            setDeltaMovement(getDeltaMovement().scale(0.9d));

            animationSpeedOld = animationSpeed;
            double xDiff = getX() - xo;
            double yDiff = getY() - yo;
            double zDiff = getZ() - zo;
            if (yDiff < 0.2) yDiff = 0;
            float amount = MathHelper.sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff) * 4f;
            if (amount > 1f) amount = 1f;

            animationSpeed += (amount - animationSpeed) * 0.4f;
            animationPosition += animationSpeed;

            if (vec3d.z == 0 && getTarget() == null && !isInSittingPose())
                setDeltaMovement(getDeltaMovement().add(0, -0.003d, 0));
        } else super.travel(vec3d);
    }

    @Override
    public float getTravelSpeed() {
        //@formatter:off
        return isInWater() ? (float) getAttributeValue(ForgeMod.SWIM_SPEED.get())
                : (float) getAttributeValue(MOVEMENT_SPEED);
        //@formatter:on
    }

    @Override
    public ItemStack eat(World level, ItemStack stack) {
        lightningCooldown = 0;
        return super.eat(level, stack);
    }

    @Override
    public void doSpecialEffects() {
        if (getVariant() == -1 && tickCount % 25 == 0) {
            double x = getX() + (Mafs.nextDouble(getRandom()) * getBbWidth() + 1);
            double y = getY() + (getRandom().nextDouble() * getBbHeight() + 1);
            double z = getZ() + (Mafs.nextDouble(getRandom()) * getBbWidth() + 1);
            level.addParticle(ParticleTypes.END_ROD, x, y, z, 0, 0.05f, 0);
        }
    }

    @Override
    public void onInvContentsChanged(int slot, ItemStack stack, boolean onLoad) {
        if (slot == BUTTERFLY_LEVIATHAN_CONDUIT_SLOT) {
            boolean flag = stack.getItem() == Items.CONDUIT;
            boolean hadConduit = hasConduit();
            entityData.set(hasConduitData, flag);
            if (!onLoad && flag && !hadConduit) setAnimation(BUTTERFLY_LEVIATHAN_CONDUIT_ANIMATION);
        }
    }

    @Override
    public void recievePassengerKeybind(int key, int mods, boolean pressed) {
        if (pressed && noAnimations()) {
            if (key == KeybindHandler.MOUNT_KEY) setAnimation(BUTTERFLY_LEVIATHAN_BITE_ANIMATION);
            else if (key == KeybindHandler.ALT_MOUNT_KEY && !level.isClientSide && canZap()) {
                EntityRayTraceResult ertr = Mafs.clipEntities(getControllingPlayer(), 40, e -> e instanceof LivingEntity && e != this);
                if (ertr != null && wantsToAttack((LivingEntity) ertr.getEntity(), getOwner())) {
                    setTarget((LivingEntity) ertr.getEntity());
                    AnimationPacket.send(this, BUTTERFLY_LEVIATHAN_LIGHTNING_ANIMATION);
                }
            }
        }
    }

    @Override
    public boolean shouldSleep() {
        return false;
    }

    public Vector3d getConduitPos() {
        return getEyePosition(1)
                .add(0, 0.4, 0.35)
                .add(calculateViewVector(xRot, yHeadRot).scale(4.15));
    }

    @Override
    public void applyStaffInfo(BookContainer container) {
        super.applyStaffInfo(container);

        container.slot(BookContainer.accessorySlot(getInventory(), BUTTERFLY_LEVIATHAN_CONDUIT_SLOT, 0, -65, -75, DragonControlScreen.CONDUIT_UV).only(Items.CONDUIT).limit(1))
                .addAction(BookActions.TARGET);
    }

    @Override
    public void setMountCameraAngles(boolean backView, EntityViewRenderEvent.CameraSetup event) {
        if (backView)
            event.getInfo().move(ClientEvents.getViewCollision(-10, this), 1, 0);
        else
            event.getInfo().move(ClientEvents.getViewCollision(-5, this), -0.75, 0);
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 1;
    }

    @Override
    public boolean checkSpawnObstruction(IWorldReader level) {
        return level.noCollision(this);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.getItem().isEdible() && stack.getItem().getFoodProperties().isMeat();
    }

    @Override
    public boolean defendsHome() {
        return true;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return WRSounds.ENTITY_BFLY_IDLE.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return WRSounds.ENTITY_BFLY_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return WRSounds.ENTITY_BFLY_DEATH.get();
    }

    @Override
    protected PathNavigator createNavigation(World level) {
        return new ButterflyLeviathanSwimmerPathNavigator(this);
    }

    public boolean hasConduit() {
        return entityData.get(hasConduitData);
    }

    @Override
    public DragonInventory createInv() {
        return new DragonInventory(this, 1);
    }

    public boolean isJumpingOutOfWater() {
        return !isInWater() && !beached;
    }

    public boolean canZap() {
        return isInWaterRainOrBubble() && lightningCooldown <= 0;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean isImmuneToArrows() {
        return true;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return ModUtils.contains(source, DamageSource.LIGHTNING_BOLT, DamageSource.IN_FIRE, DamageSource.IN_WALL) || super.isInvulnerableTo(source);
    }

    @Override
    public float getScale() {
        return getAgeScale(0.225f);
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntitySize size) {
        return size.height * (beached ? 1f : 0.6f);
    }

    @Override
    public EntitySize getDimensions(Pose pose) {
        return getType().getDimensions().scale(getScale());
    }

    @Override // 2 passengers
    protected boolean canAddPassenger(Entity passenger) {
        return isTame() && isJuvenile() && getPassengers().size() < 2;
    }

    @Override
    public Vector3d getPassengerPosOffset(Entity entity, int index) {
        return new Vector3d(0, getPassengersRidingOffset(), index == 1 ? -2 : 0);
    }

    @Override
    public boolean canBeRiddenInWater(Entity rider) {
        return true;
    }

    @Override
    public int getYawRotationSpeed() {
        return 6;
    }

    @Override
    public int determineVariant() {
        return getRandom().nextDouble() < 0.02 ? -1 : getRandom().nextInt(2);
    }

    @Override
    public boolean canFly() {
        return false;
    }

    @Override
    public Animation[] getAnimations() {
        return BUTTERFLY_LEVIATHAN_ANIMATIONS;
    }

    @Override
    public CreatureAttribute getMobType() {
        return CreatureAttribute.WATER;
    }

    @Override
    public boolean checkSpawnRules(IWorld levelIn, SpawnReason spawnReasonIn) {
        return true;
    }

    private static void createLightning(World level, Vector3d position, boolean effectOnly) {
        if (level.isClientSide) return;
        LightningBoltEntity entity = EntityType.LIGHTNING_BOLT.create(level);
        entity.moveTo(position);
        entity.setVisualOnly(effectOnly);
        level.addFreshEntity(entity);
    }

    public static <F extends MobEntity> boolean getSpawnPlacement(EntityType<F> fEntityType, IServerWorld level, SpawnReason reason, BlockPos pos, Random random) {
        if (reason == SpawnReason.SPAWNER) return true;
        if (level.getFluidState(pos).is(FluidTags.WATER)) {
            final double chance = random.nextDouble();
            if (reason == SpawnReason.CHUNK_GENERATION) return chance < 0.325;
            else if (reason == SpawnReason.NATURAL) return chance < 0.001;
        }
        return false;
    }

    public static AttributeModifierMap.MutableAttribute getAttributeMap() {
        return MobEntity.createMobAttributes()
                .add(MAX_HEALTH, 180)
                .add(MOVEMENT_SPEED, 0.08)
                .add(ForgeMod.SWIM_SPEED.get(), 0.3)
                .add(KNOCKBACK_RESISTANCE, 1)
                .add(ATTACK_DAMAGE, 14)
                .add(FOLLOW_RANGE, 50);
    }
}
