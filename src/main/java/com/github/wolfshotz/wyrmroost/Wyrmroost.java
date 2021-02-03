package com.github.wolfshotz.wyrmroost;

import com.github.wolfshotz.wyrmroost.client.ClientEvents;
import com.github.wolfshotz.wyrmroost.network.packets.*;
import com.github.wolfshotz.wyrmroost.registry.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Wyrmroost.MOD_ID)
public class Wyrmroost
{
    public static final String MOD_ID = "wyrmroost";
    public static final Logger LOG = LogManager.getLogger(MOD_ID);
    public static final SimpleChannel NETWORK = buildChannel();

    public Wyrmroost()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        CommonEvents.load();
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientEvents::load);

        WREntities.REGISTRY.register(bus);
        WREntities.Attributes.REGISTRY.register(bus);
        WRBlocks.REGISTRY.register(bus);
        WRItems.REGISTRY.register(bus);
        WRIO.REGISTRY.register(bus);
        WRSounds.REGISTRY.register(bus);
        WRWorld.Features.REGISTRY.register(bus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, WRConfig.Common.SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, WRConfig.Client.SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, WRConfig.Server.SPEC);
    }

    private static SimpleChannel buildChannel()
    {
        final String PROTOCOL_VERSION = "1.0";
        final SimpleChannel network = NetworkRegistry.ChannelBuilder
                .named(rl("network")).clientAcceptedVersions(PROTOCOL_VERSION::equals)
                .serverAcceptedVersions(PROTOCOL_VERSION::equals)
                .networkProtocolVersion(() -> PROTOCOL_VERSION)
                .simpleChannel();

        int index = 1;
        network.messageBuilder(AnimationPacket.class, index, NetworkDirection.PLAY_TO_CLIENT).encoder(AnimationPacket::encode).decoder(AnimationPacket::new).consumer(AnimationPacket::handle).add();
        network.messageBuilder(KeybindPacket.class, ++index, NetworkDirection.PLAY_TO_SERVER).encoder(KeybindPacket::encode).decoder(KeybindPacket::new).consumer(KeybindPacket::handle).add();
        network.messageBuilder(RenameEntityPacket.class, ++index, NetworkDirection.PLAY_TO_SERVER).encoder(RenameEntityPacket::encode).decoder(RenameEntityPacket::new).consumer(RenameEntityPacket::handle).add();
        network.messageBuilder(StaffActionPacket.class, ++index, NetworkDirection.PLAY_TO_SERVER).encoder(StaffActionPacket::encode).decoder(StaffActionPacket::new).consumer(StaffActionPacket::handle).add();
        network.messageBuilder(SGGlidePacket.class, ++index, NetworkDirection.PLAY_TO_SERVER).encoder(SGGlidePacket::encode).decoder(SGGlidePacket::new).consumer(SGGlidePacket::handle).add();
        network.messageBuilder(AddPassengerPacket.class, ++index, NetworkDirection.PLAY_TO_CLIENT).encoder(AddPassengerPacket::encode).decoder(AddPassengerPacket::new).consumer(AddPassengerPacket::handle).add();

        return network;
    }

    /**
     * Register a new Wyrmroost Specific Resource Location. <P>
     * Don't bash me for the method name it makes total sense ffs: <P>
     * <b><i>r</i></b>esource <P>
     * <b><i>l</i></b>ocation <P>
     *
     * @return somethin related to a resource idk
     */
    public static ResourceLocation rl(String path)
    {
        return new ResourceLocation(MOD_ID, path);
    }
}
