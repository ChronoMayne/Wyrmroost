package WolfShotz.Wyrmroost.content.entities.dragon.owdrake;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonRenderer;
import WolfShotz.Wyrmroost.content.items.DragonArmorItem;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class OWDrakeRenderer extends AbstractDragonRenderer<OWDrakeEntity>
{
    public final ResourceLocation MALE_COM           = location("male_com.png");
    public final ResourceLocation FEMALE_COM         = location("female_com.png");
    public final ResourceLocation MALE_SAV           = location("male_sav.png");
    public final ResourceLocation FEMALE_SAV         = location("female_sav.png");
    public final ResourceLocation MALE_SPE           = location("male_spe.png");
    public final ResourceLocation FEMALE_SPE         = location("female_spe.png");
    public final ResourceLocation CHILD_COM          = location("child_com.png");
    public final ResourceLocation CHILD_SAV          = location("child_sav.png");
    public final ResourceLocation CHILD_SPE          = location("child_spe.png");
    // Easter Egg
    public final ResourceLocation DAISY              = location("dasy.png");
    // Saddle
    public final ResourceLocation SADDLE_LAYER       = location("accessories/saddle.png");
    // Armor
    public final ResourceLocation ARMOR_IRON         = location("accessories/armor_iron.png");
    public final ResourceLocation ARMOR_GOLD         = location("accessories/armor_gold.png");
    public final ResourceLocation ARMOR_DIAMOND      = location("accessories/armor_diamond.png");
    public final ResourceLocation ARMOR_PLATINUM     = location("accessories/armor_platinum.png");
    public final ResourceLocation ARMOR_GEODE_BLUE   = location("accessories/armor_geode_blue.png");
    public final ResourceLocation ARMOR_GEODE_RED    = location("accessories/armor_geode_red.png");
    public final ResourceLocation ARMOR_GEODE_PURPLE = location("accessories/armor_geode_purple.png");

    public OWDrakeRenderer(EntityRendererManager manager) {
        super(manager, new OWDrakeModel(), 1.6f);
        addLayer(new ConditionalLayer(d -> getArmorTexture(d.getArmor().getType()), AbstractDragonEntity::hasArmor));
        addLayer(new ConditionalLayer(SADDLE_LAYER, OWDrakeEntity::isSaddled));
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(OWDrakeEntity drake) {
        if (drake.hasCustomName() && drake.getCustomName().getUnformattedComponentText().equals("Daisy")) return DAISY;
        boolean isSavannah = drake.getDrakeVariant();
        boolean isSpecial = drake.isSpecial();
        boolean gender = drake.getGender();
    
        if (drake.isChild()) {
            if (isSpecial)  return CHILD_SPE;
            if (isSavannah) return CHILD_SAV;
            return CHILD_COM;
        }
        if (isSpecial)  return gender? MALE_SPE : FEMALE_SPE;
        if (isSavannah) return gender? MALE_SAV : FEMALE_SAV;
        return gender? MALE_COM : FEMALE_COM;
    }
    
    private ResourceLocation getArmorTexture(DragonArmorItem.DragonArmorType type) {
        switch (type) {
            default:
            case IRON:          return ARMOR_IRON;
            case GOLD:          return ARMOR_GOLD;
            case DIAMOND:       return ARMOR_DIAMOND;
            case PLATINUM:      return ARMOR_PLATINUM;
            case BLUE_GEODE:    return ARMOR_GEODE_BLUE;
            case RED_GEODE:     return ARMOR_GEODE_RED;
            case PURPLE_GEODE:  return ARMOR_GEODE_PURPLE;
        }
    }
    
    @Override
    public String getResourceDirectory() { return DEF_LOC + "owdrake/"; }
}
