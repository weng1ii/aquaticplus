package net.weng1i.aquaticplus.item;

import net.weng1i.aquaticplus.Aquaticplus;
import net.weng1i.aquaticplus.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Aquaticplus.MOD_ID);

    public static final RegistryObject<CreativeModeTab> AQUATICPLUS_TAB = CREATIVE_MODE_TABS.register("aquaticplus_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.RAW_BLUEFINFISH.get()))
                    .title(Component.translatable("creativetab.tutorial_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModItems.BLUEFINFISH_SPAWN_EGG.get());
                        pOutput.accept(ModItems.ANGLERFISH_SPAWN_EGG.get());
                        pOutput.accept(ModItems.RAW_BLUEFINFISH.get());
                        pOutput.accept(ModItems.COOKED_BLUEFINFISH.get());
                    })
                    .build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
