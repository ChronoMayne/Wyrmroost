package WolfShotz.Wyrmroost.registry;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.content.items.*;
import WolfShotz.Wyrmroost.content.items.base.ArmorMaterialList;
import WolfShotz.Wyrmroost.content.items.base.ItemArmorBase;
import WolfShotz.Wyrmroost.content.items.base.ToolMaterialList;
import WolfShotz.Wyrmroost.content.items.dragonegg.DragonEggItem;
import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.block.Block;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class WRItems
{
    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, Wyrmroost.MOD_ID);
    
    public static final RegistryObject<Item> TARRAGON_TOME = register("tarragon_tome", new TarragonTomeItem());
    public static final RegistryObject<Item> MINUTUS = register("minutus", new MinutusItem());
    public static final RegistryObject<Item> DRAGON_EGG = register("dragon_egg", new DragonEggItem());
    public static final RegistryObject<Item> SOUL_CRYSTAL = register("soul_crystal", new SoulCrystalItem());
    public static final RegistryObject<Item> DRAGON_STAFF = register("dragon_staff", new DragonStaffItem());
    
    public static final RegistryObject<Item> BLUE_GEODE = register("geode");
    public static final RegistryObject<Item> RED_GEODE = register("geode_red");
    public static final RegistryObject<Item> PURPLE_GEODE = register("geode_purple");
    public static final RegistryObject<Item> PLATINUM_INGOT = register("platinum_ingot");
    public static final RegistryObject<Item> BLUE_SHARD = register("blue_shard");
    public static final RegistryObject<Item> GREEN_SHARD = register("green_shard");
    public static final RegistryObject<Item> ORANGE_SHARD = register("orange_shard");
    public static final RegistryObject<Item> YELLOW_SHARD = register("yellow_shard");
    public static final RegistryObject<Item> ASH_PILE = register("ash_pile");
    
    public static final RegistryObject<Item> BLUE_GEODE_SWORD = register("geode_sword", new SwordItem(ToolMaterialList.GEODE, 4, -2.4f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> BLUE_GEODE_PICKAXE = register("geode_pick", new PickaxeItem(ToolMaterialList.GEODE, 2, -2.8f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> BLUE_GEODE_AXE = register("geode_axe", new AxeItem(ToolMaterialList.GEODE, 2, -2.8f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> BLUE_GEODE_SHOVEL = register("geode_shovel", new ShovelItem(ToolMaterialList.GEODE, 1.5f, -3.0f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> BLUE_GEODE_HOE = register("geode_hoe", new HoeItem(ToolMaterialList.GEODE, 1.5f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> BLUE_GEODE_HELMET = register("geode_helmet", new ItemArmorBase(ArmorMaterialList.GEODE, EquipmentSlotType.HEAD));
    public static final RegistryObject<Item> BLUE_GEODE_CHESTPLATE = register("geode_chestplate", new ItemArmorBase(ArmorMaterialList.GEODE, EquipmentSlotType.CHEST));
    public static final RegistryObject<Item> BLUE_GEODE_LEGGINGS = register("geode_leggings", new ItemArmorBase(ArmorMaterialList.GEODE, EquipmentSlotType.LEGS));
    public static final RegistryObject<Item> BLUE_GEODE_BOOTS = register("geode_boots", new ItemArmorBase(ArmorMaterialList.GEODE, EquipmentSlotType.FEET));
    
    public static final RegistryObject<Item> RED_GEODE_SWORD = register("geode_red_sword", new SwordItem(ToolMaterialList.GEODERED, 4, -2.4f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> RED_GEODE_PICKAXE = register("geode_red_pickaxe", new PickaxeItem(ToolMaterialList.GEODERED, 2, -2.8f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> RED_GEODE_AXE = register("geode_red_axe", new AxeItem(ToolMaterialList.GEODERED, 2, -2.8f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> RED_GEODE_SHOVEL = register("geode_red_shovel", new ShovelItem(ToolMaterialList.GEODERED, 1.5f, -3.0f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> RED_GEODE_HOE = register("geode_red_hoe", new HoeItem(ToolMaterialList.GEODERED, 1.5f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> RED_GEODE_HELMET = register("geode_red_helmet", new ItemArmorBase(ArmorMaterialList.GEODERED, EquipmentSlotType.HEAD));
    public static final RegistryObject<Item> RED_GEODE_CHESTPLATE = register("geode_red_chestplate", new ItemArmorBase(ArmorMaterialList.GEODERED, EquipmentSlotType.CHEST));
    public static final RegistryObject<Item> RED_GEODE_LEGGINGS = register("geode_red_leggings", new ItemArmorBase(ArmorMaterialList.GEODERED, EquipmentSlotType.LEGS));
    public static final RegistryObject<Item> RED_GEODE_BOOTS = register("geode_red_boots", new ItemArmorBase(ArmorMaterialList.GEODERED, EquipmentSlotType.FEET));
    
    public static final RegistryObject<Item> PURPLE_GEODE_SWORD = register("geode_purple_sword", new SwordItem(ToolMaterialList.GEODEPURPLE, 4, -2.4f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> PURPLE_GEODE_PICKAXE = register("geode_purple_pickaxe", new PickaxeItem(ToolMaterialList.GEODEPURPLE, 2, -2.8f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> PURPLE_GEODE_AXE = register("geode_purple_axe", new AxeItem(ToolMaterialList.GEODEPURPLE, 2, -2.8f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> PURPLE_GEODE_SHOVEL = register("geode_purple_shovel", new ShovelItem(ToolMaterialList.GEODEPURPLE, 1.5f, -3.0f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> PURPLE_GEODE_HOE = register("geode_purple_hoe", new HoeItem(ToolMaterialList.GEODEPURPLE, 1.5f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> PURPLE_GEODE_HELMET = register("geode_purple_helmet", new ItemArmorBase(ArmorMaterialList.GEODEPURPLE, EquipmentSlotType.HEAD));
    public static final RegistryObject<Item> PURPLE_GEODE_CHESTPLATE = register("geode_purple_chestplate", new ItemArmorBase(ArmorMaterialList.GEODEPURPLE, EquipmentSlotType.CHEST));
    public static final RegistryObject<Item> PURPLE_GEODE_LEGGINGS = register("geode_purple_leggings", new ItemArmorBase(ArmorMaterialList.GEODEPURPLE, EquipmentSlotType.LEGS));
    public static final RegistryObject<Item> PURPLE_GEODE_BOOTS = register("geode_purple_boots", new ItemArmorBase(ArmorMaterialList.GEODEPURPLE, EquipmentSlotType.FEET));
    
    public static final RegistryObject<Item> PLATINUM_SWORD = register("platinum_sword", new SwordItem(ToolMaterialList.PLATINUM, 4, -2.4f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> PLATINUM_PICKAXE = register("platinum_pickaxe", new PickaxeItem(ToolMaterialList.PLATINUM, 2, -2.8f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> PLATINUM_AXE = register("platinum_axe", new AxeItem(ToolMaterialList.PLATINUM, 2, -2.8f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> PLATINUM_SHOVEL = register("platinum_shovel", new ShovelItem(ToolMaterialList.PLATINUM, 1.5f, -3f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> PLATINUM_HOE = register("platinum_hoe", new HoeItem(ToolMaterialList.PLATINUM, 1.5f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> PLATINUM_HELMET = register("platinum_helmet", new ItemArmorBase(ArmorMaterialList.PLATINUM, EquipmentSlotType.HEAD));
    public static final RegistryObject<Item> PLATINUM_CHESTPLATE = register("platinum_chestplate", new ItemArmorBase(ArmorMaterialList.PLATINUM, EquipmentSlotType.CHEST));
    public static final RegistryObject<Item> PLATINUM_LEGGINGS = register("platinum_leggings", new ItemArmorBase(ArmorMaterialList.PLATINUM, EquipmentSlotType.LEGS));
    public static final RegistryObject<Item> PLATINUM_BOOTS = register("platinum_boots", new ItemArmorBase(ArmorMaterialList.PLATINUM, EquipmentSlotType.FEET));

    public static final RegistryObject<Item> FOOD_LOWTIER_MEAT_RAW = register("lowtier_meat_raw", new Item(ModUtils.itemBuilder().food(WRItems.RAW_LOWTIER_MEAT)));
    public static final RegistryObject<Item> FOOD_COMMON_MEAT_RAW = register("common_meat_raw", new Item(ModUtils.itemBuilder().food(WRItems.RAW_COMMON_MEAT)));
    public static final RegistryObject<Item> FOOD_APEX_MEAT_RAW = register("apex_meat_raw", new Item(ModUtils.itemBuilder().food(WRItems.RAW_APEX_MEAT)));
    public static final RegistryObject<Item> FOOD_BEHEMOTH_MEAT_RAW = register("behemoth_meat_raw", new Item(ModUtils.itemBuilder().food(WRItems.RAW_BEHEMOTH_MEAT)));
    public static final RegistryObject<Item> FOOD_LOWTIER_MEAT_COOKED = register("lowtier_meat_cooked", new Item(ModUtils.itemBuilder().food(WRItems.COOKED_LOWTIER_MEAT)));
    public static final RegistryObject<Item> FOOD_COMMON_MEAT_COOKED = register("common_meat_cooked", new Item(ModUtils.itemBuilder().food(WRItems.COOKED_COMMON_MEAT)));
    public static final RegistryObject<Item> FOOD_APEX_MEAT_COOKED = register("apex_meat_cooked", new Item(ModUtils.itemBuilder().food(WRItems.COOKED_APEX_MEAT)));
    public static final RegistryObject<Item> FOOD_BEHEMOTH_MEAT_COOKED = register("behemoth_meat_cooked", new Item(ModUtils.itemBuilder().food(WRItems.COOKED_BEHEMOTH_MEAT)));
    public static final RegistryObject<Item> FOOD_COOKED_MINUTUS = register("cooked_minutus", new Item(ModUtils.itemBuilder().food(WRItems.COOKED_MINUTUS)));
    public static final RegistryObject<Item> FOOD_JEWELLED_APPLE = register("jewelled_apple", new Item(ModUtils.itemBuilder().food(WRItems.JEWELLED_APPLE)));
    public static final RegistryObject<Item> FOOD_DRAGON_FRUIT = register("dragon_fruit", new Item(ModUtils.itemBuilder().food(WRItems.DRAGON_FRUIT)));
    public static final RegistryObject<Item> FOOD_BLUEBERRIES = register("blueberries", new Item(ModUtils.itemBuilder().food(WRItems.BLUEBERRIES)));
    
    public static final RegistryObject<Item> DRAGON_ARMOR_IRON = register("iron_dragonarmor", new DragonArmorItem(DragonArmorItem.DragonArmorType.IRON));
    public static final RegistryObject<Item> DRAGON_ARMOR_GOLD = register("gold_dragonarmor", new DragonArmorItem(DragonArmorItem.DragonArmorType.GOLD));
    public static final RegistryObject<Item> DRAGON_ARMOR_DIAMOND = register("diamond_dragonarmor", new DragonArmorItem(DragonArmorItem.DragonArmorType.DIAMOND));
    public static final RegistryObject<Item> DRAGON_ARMOR_PLATINUM = register("platinum_dragonarmor", new DragonArmorItem(DragonArmorItem.DragonArmorType.PLATINUM));
    public static final RegistryObject<Item> DRAGON_ARMOR_BLUE_GEODE = register("geode_blue_dragonarmor", new DragonArmorItem(DragonArmorItem.DragonArmorType.BLUE_GEODE));
    public static final RegistryObject<Item> DRAGON_ARMOR_RED_GEODE = register("geode_red_dragonarmor", new DragonArmorItem(DragonArmorItem.DragonArmorType.RED_GEODE));
    public static final RegistryObject<Item> DRAGON_ARMOR_PURPLE_GEODE = register("geode_purple_dragonarmor", new DragonArmorItem(DragonArmorItem.DragonArmorType.PURPLE_GEODE));
    
    public static final RegistryObject<Item> EGG_DRAKE = register("drake_egg", new CustomSpawnEggItem(WREntities.OVERWORLD_DRAKE::get, 0x788716, 0x3E623E));
    public static final RegistryObject<Item> EGG_MINUTUS = register("minutus_egg", new CustomSpawnEggItem(WREntities.MINUTUS::get, 0xD6BCBC, 0xDEB6C7));
    public static final RegistryObject<Item> EGG_SILVER_GLIDER = register("silverglider_egg", new CustomSpawnEggItem(WREntities.SILVER_GLIDER::get, 0xC8C8C8, 0xC4C4C4));
    public static final RegistryObject<Item> EGG_ROOSTSTALKER = register("rooststalker_egg", new CustomSpawnEggItem(WREntities.ROOSTSTALKER::get, 0x52100D, 0x959595));
    public static final RegistryObject<Item> EGG_BUTTERFLYLEVIATHAN = register("butterflyleviathan_egg", new CustomSpawnEggItem(WREntities.BUTTERFLY_LEVIATHAN::get, 0x17283C, 0x7A6F5A));
    public static final RegistryObject<Item> EGG_DRAGONFRUITDRAKE = register("fruitdrake_egg", new CustomSpawnEggItem(WREntities.DRAGON_FRUIT_DRAKE::get, 0xe05c9a, 0x788716));
    public static final RegistryObject<Item> EGG_CANARIWYVERN = register("canariwyvern_egg", new CustomSpawnEggItem(WREntities.CANARI_WYVERN::get, 0xffffff, 0xffffff));

    static RegistryObject<Item> register(String name, Item item) { return ITEMS.register(name, () -> item); }
    static RegistryObject<Item> register(String name) { return ITEMS.register(name, () -> new Item(ModUtils.itemBuilder())); }

    //  ===========================
    //          Food List
    //  ===========================

    private static final Food RAW_LOWTIER_MEAT = new Food.Builder().hunger(2).saturation(0.25f).meat().build();
    private static final Food RAW_COMMON_MEAT = new Food.Builder().hunger(3).saturation(0.35f).meat().build();
    private static final Food RAW_APEX_MEAT = new Food.Builder().hunger(5).saturation(0.45f).meat().build();
    private static final Food RAW_BEHEMOTH_MEAT = new Food.Builder().hunger(7).saturation(0.7f).meat().build();
    private static final Food COOKED_LOWTIER_MEAT = new Food.Builder().hunger(5).saturation(0.7f).meat().build();
    private static final Food COOKED_COMMON_MEAT = new Food.Builder().hunger(8).saturation(0.8f).meat().build();
    private static final Food COOKED_APEX_MEAT = new Food.Builder().hunger(12).saturation(1f).meat().build();
    private static final Food COOKED_BEHEMOTH_MEAT = new Food.Builder().hunger(16).saturation(1.4f).meat().build();
    private static final Food DRAGON_FRUIT = new Food.Builder().hunger(6).saturation(0.55f).build();
    private static final Food BLUEBERRIES = new Food.Builder().hunger(2).saturation(0.1f).build();
    private static final Food COOKED_MINUTUS = new Food.Builder().hunger(6).saturation(0.7f).meat().build();
    private static final Food JEWELLED_APPLE = new Food.Builder().hunger(8).saturation(0.9f).setAlwaysEdible()
                                                       .effect(new EffectInstance(Effects.GLOWING, 800), 1.0f)
                                                       .effect(new EffectInstance(Effects.REGENERATION, 100, 2), 1.0f)
                                                       .effect(new EffectInstance(Effects.RESISTANCE, 800), 1.0f)
                                                       .effect(new EffectInstance(Effects.ABSORPTION, 6000, 2), 1.0f)
                                                       .effect(new EffectInstance(Effects.NIGHT_VISION, 800), 1.0f)
                                                       .build();
    
    public static class Tags
    {
        public static final Tag<Item> GEODES = tag("geodes");

        public static final Map<Tag<Block>, Tag<Item>> ITEM_BLOCKS = new HashMap<>();

        public static final Tag<Item> CANARI_LOGS = tag(new ResourceLocation("logs/canari_logs"));
        public static final Tag<Item> BLUE_CORIN_LOGS = itemBlockTag(new ResourceLocation("logs/blue_corin_logs"), WRBlocks.Tags.BLUE_CORIN_LOGS);
        public static final Tag<Item> TEAL_CORIN_LOGS = itemBlockTag(new ResourceLocation("logs/teal_corin_logs"), WRBlocks.Tags.TEAL_CORIN_LOGS);
        public static final Tag<Item> RED_CORIN_LOGS = itemBlockTag(new ResourceLocation("logs/red_corin_logs"), WRBlocks.Tags.RED_CORIN_LOGS);
    
        private static Tag<Item> tag(String name) { return new ItemTags.Wrapper(ModUtils.resource(name)); }
        private static Tag<Item> tag(ResourceLocation name) { return new ItemTags.Wrapper(name); }

        private static Tag<Item> itemBlockTag(ResourceLocation name, Tag<Block> copy)
        {
            ItemTags.Wrapper tag = new ItemTags.Wrapper(name);
            ITEM_BLOCKS.put(copy, tag);
            return tag;
        }
    }
}