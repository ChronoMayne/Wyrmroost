package WolfShotz.Wyrmroost;

import WolfShotz.Wyrmroost.content.tileentities.teegg.EggRenderer;
import WolfShotz.Wyrmroost.content.tileentities.teegg.EggTileEntity;
import WolfShotz.Wyrmroost.event.ForgeEvents;
import WolfShotz.Wyrmroost.event.SetupEntity;
import WolfShotz.Wyrmroost.event.SetupItem;
import WolfShotz.Wyrmroost.event.SetupOreGen;
import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Wyrmroost.modID)
public class Wyrmroost
{
    public static final String modID = "wyrmroost";
    public static final ItemGroup creativeTab = new CreativeTab();

    public Wyrmroost() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.addListener(ForgeEvents::cancelFall);
        
        SetupOreGen.setupOreGen();

        ModUtils.L.debug("commonSetup complete");
    }

    private void clientSetup(final FMLClientSetupEvent event) {
//        MinecraftForge.EVENT_BUS.addListener(ForgeEvents::ridingPerspective);
        
        SetupEntity.registerEntityRenders();
        ClientRegistry.bindTileEntitySpecialRenderer(EggTileEntity.class, new EggRenderer());

        ModUtils.L.info("clientSetup complete");
    }

    private static class CreativeTab extends ItemGroup
    {
        private CreativeTab() { super("wyrmroost"); }

        @Override
        public ItemStack createIcon() { return new ItemStack(SetupItem.itemgeode); }
    }

}
