package net.weng1i.aquaticplus.item;

import net.weng1i.aquaticplus.Aquaticplus;
import net.weng1i.aquaticplus.block.ModBlocks;
import net.weng1i.aquaticplus.entity.ModEntities;
import net.weng1i.aquaticplus.item.custom.*;
import net.weng1i.aquaticplus.sound.ModSounds;
import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Aquaticplus.MOD_ID);

    public static final RegistryObject<Item> BLUEFINFISH_SPAWN_EGG = ITEMS.register("bluefinfish_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.BLUEFINFISH, 0x7e9680, 0xa8f0a8, new Item.Properties()));
    public static final RegistryObject<Item> ANGLERFISH_SPAWN_EGG = ITEMS.register("anglerfish_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.ANGLERFISH, 0x003300, 0x003399, new Item.Properties()));

    public static final RegistryObject<Item> COOKED_BLUEFINFISH = ITEMS.register("cooked_bluefinfish",
            () -> new Item(new Item.Properties().food(ModFoods.COOKED_BLUEFINFISH)));
    public static final RegistryObject<Item> RAW_BLUEFINFISH = ITEMS.register("raw_bluefinfish",
            () -> new Item(new Item.Properties().food(ModFoods.RAW_BLUEFINFISH)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
