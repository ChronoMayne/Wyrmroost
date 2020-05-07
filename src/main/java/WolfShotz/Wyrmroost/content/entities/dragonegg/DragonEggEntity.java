package WolfShotz.Wyrmroost.content.entities.dragonegg;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.client.animation.Animation;
import WolfShotz.Wyrmroost.client.animation.IAnimatedObject;
import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.network.NetworkUtils;
import WolfShotz.Wyrmroost.network.messages.EggHatchMessage;
import WolfShotz.Wyrmroost.registry.WRItems;
import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.entity.*;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.Objects;

public class DragonEggEntity extends Entity implements IAnimatedObject, IEntityAdditionalSpawnData
{
    public EntityType<AbstractDragonEntity> containedDragon;
    public int hatchTime;
    public DragonEggProperties properties;

    public boolean wiggleInvert, wiggleInvert2;
    private int animationTick;
    private Animation animation = NO_ANIMATION;

    public static final Animation WIGGLE_ANIMATION = new Animation(10);

    public DragonEggEntity(EntityType<? extends DragonEggEntity> dragonEgg, World world) { super(dragonEgg, world); }
    
    // ================================
    //           Entity NBT
    // ================================
    @Override
    protected void registerData() {}
    
    @Override
    public void readAdditional(CompoundNBT compound)
    {
        containedDragon = ModUtils.getTypeByString(compound.getString("dragonType"));
        hatchTime = compound.getInt("hatchTime");
    }
    
    @Override
    public void writeAdditional(CompoundNBT compound)
    {
        compound.putString("dragonType", getDragonKey());
        compound.putInt("hatchTime", hatchTime);
    }
    
    public String getDragonKey()
    {
        return EntityType.getKey(containedDragon).toString();
    }
    
    // ================================
    
    @Override
    public void tick()
    {
        if (!world.isRemote && containedDragon == null)
        {
            safeError();
            return;
        }
        
        super.tick();
        
        updateMotion();

        if (getProperties().getConditions().test(this))
        {
            if (world.isRemote)
            {
                if (ticksExisted % 3 == 0)
                {
                    double x = getPosX() + rand.nextGaussian() * 0.2d;
                    double y = getPosY() + rand.nextDouble() + getHeight() / 2;
                    double z = getPosZ() + rand.nextGaussian() * 0.2d;
                    world.addParticle(new RedstoneParticleData(1f, 1f, 0, 0.5f), x, y, z, 0, 0, 0);
                }
            }
            else
            {
                if (--hatchTime <= 0)
                {
                    Wyrmroost.NETWORK.send(PacketDistributor.TRACKING_ENTITY.with(() -> this), new EggHatchMessage(this));
                    hatch();
                    return;
                }
                
                int bounds = Math.max(hatchTime / 2, 3);
                
                if (hatchTime < getProperties().getHatchTime() / 2 && rand.nextInt(bounds) == 0 && getAnimation() != WIGGLE_ANIMATION)
                    crack(true);
            }
        }
        
        EntitySize size = getProperties().getSize();
        if (getWidth() != size.width || getHeight() != size.height) recalculateSize();
        
        if (getAnimation() != NO_ANIMATION)
        {
            ++animationTick;
            if (animationTick >= animation.getDuration()) setAnimation(NO_ANIMATION);
        }
    }

    @Override
    public boolean onLivingFall(float distance, float damageMultiplier)
    {
        if (distance > 3)
        {
            crack(false);
            return true;
        }
        return false;
    }

    private void updateMotion()
    {
        boolean flag = getMotion().y <= 0.0D;
        double d1 = getPosY();
        double d0 = 0.5d;
        
        move(MoverType.SELF, getMotion());
        if (!hasNoGravity() && !isSprinting())
        {
            Vec3d vec3d2 = getMotion();
            double d2;
            if (flag && Math.abs(vec3d2.y - 0.005D) >= 0.003D && Math.abs(vec3d2.y - d0 / 16.0D) < 0.003D) d2 = -0.003D;
            else d2 = vec3d2.y - d0 / 16.0D;
            
            setMotion(vec3d2.x, d2, vec3d2.z);
        }
        
        Vec3d vec3d6 = getMotion();
        if (collidedHorizontally && isOffsetPositionInLiquid(vec3d6.x, vec3d6.y + (double) 0.6F - getPosY() + d1, vec3d6.z))
        {
            setMotion(vec3d6.x, 0.3F, vec3d6.z);
        }
    }
    
    /**
     * Called to hatch the dragon egg
     * Usage: <P>
     * - Get the dragon EntityType (If its something it shouldnt, safely fail) <P>
     * - Set the dragons growing age to a baby <P>
     * - Set the position of that dragon to the position of this egg <P>
     * - Remove this entity (the egg) and play any effects
     */
    public void hatch()
    {
        if (!world.isRemote)
        {
            AbstractDragonEntity newDragon = containedDragon.create(world);
            if (newDragon == null)
            {
                safeError();
                return;
            }
            newDragon.setPosition(getPosX(), getPosY(), getPosZ());
            newDragon.setGrowingAge(newDragon.getEggProperties().getGrowthTime());
            newDragon.onInitialSpawn(world, world.getDifficultyForLocation(getPosition()), SpawnReason.BREEDING, null, null);
            world.addEntity(newDragon);
        }
        else
        {
            for (int i = 0; i < getWidth() * 25; ++i)
            {
                double x = rand.nextGaussian() * 0.2f;
                double y = rand.nextDouble() * 0.45f;
                double z = rand.nextGaussian() * 0.2f;
                world.addParticle(new ItemParticleData(ParticleTypes.ITEM, new ItemStack(WRItems.DRAGON_EGG.get())), getPosX(), getPosY(), getPosZ(), x, y, z);
            }
        }
        world.playSound(getPosX(), getPosY(), getPosZ(), SoundEvents.ENTITY_TURTLE_EGG_HATCH, SoundCategory.BLOCKS, 1, 1, false);
        remove();
    }

    public void crack(boolean sendPacket)
    {
        playSound(SoundEvents.ENTITY_TURTLE_EGG_CRACK, 1f, 1f);
        if (sendPacket) NetworkUtils.sendAnimationPacket(this, WIGGLE_ANIMATION);
        else setAnimation(WIGGLE_ANIMATION);
    }
    
    /**
     * Called When the dragon type of the egg is not what it should be.
     */
    private void safeError()
    {
        ModUtils.L.error("THIS ISNT A DRAGON WTF KIND OF ABOMINATION IS THIS HATCHING?!?! Unknown Entity Type for Dragon Egg @ {}", getPosition());
        remove();
    }
    
    @Override
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        CompoundNBT tag = new CompoundNBT();
        
        tag.putInt("hatchTime", hatchTime);
        tag.putString("dragonType", getDragonKey());
        ItemStack itemStack = new ItemStack(WRItems.DRAGON_EGG.get());
        itemStack.setTag(tag);
        InventoryHelper.spawnItemStack(world, getPosX(), getPosY(), getPosZ(), itemStack);
        remove();
        
        return true;
    }
    
    public DragonEggProperties getProperties()
    {
        if (properties == null) // get properties lazily
        {
            try
            {
                return properties = Objects.requireNonNull(containedDragon.create(world)).getEggProperties();

            }
            catch (NullPointerException e)
            {
                ModUtils.L.warn("Unknown Dragon Type!!!");
                return properties = new DragonEggProperties(0.65f, 1f, 12000);
            }
        }

        return properties;
    }
    
    @Override
    public ItemStack getPickedResult(RayTraceResult target)
    {
        ItemStack stack = new ItemStack(WRItems.DRAGON_EGG.get());
        CompoundNBT tag = new CompoundNBT();
        tag.putString("dragonType", getDragonKey());
        tag.putInt("hatchTime", getProperties().getHatchTime());
        stack.setTag(tag);
        return stack;
    }

    @Override
    public EntitySize getSize(Pose poseIn) { return getProperties().getSize(); }
    
    @Override
    public boolean canBePushed()
    {
        return false;
    }

    @Override
    public boolean canBeCollidedWith()
    {
        return true;
    }

    // This is needed because it seems to be ignored on server world...
    @Override
    public void onKillCommand() { remove(); }

    @Override
    public IPacket<?> createSpawnPacket() { return NetworkHooks.getEntitySpawningPacket(this); }

    @Override
    public void writeSpawnData(PacketBuffer buffer) { buffer.writeString(getDragonKey()); }

    @Override
    public void readSpawnData(PacketBuffer buffer)
    {
        this.containedDragon = ModUtils.getTypeByString(buffer.readString());
    }

    // === Animation ===
    @Override
    public int getAnimationTick()
    {
        return animationTick;
    }
    
    @Override
    public void setAnimationTick(int i)
    {
        animationTick = i;
    }
    
    @Override
    public Animation getAnimation()
    {
        return animation;
    }
    
    @Override
    public void setAnimation(Animation animation)
    {
        this.animation = animation;
        setAnimationTick(0);
        
        
        if (world.isRemote && animation == WIGGLE_ANIMATION)
        {
            wiggleInvert = rand.nextBoolean();
            wiggleInvert2 = rand.nextBoolean();
        }
    }
    
    @Override
    public Animation[] getAnimations()
    {
        return new Animation[]{NO_ANIMATION, WIGGLE_ANIMATION};
    }
    
    // ================
}
