package net.weng1i.aquaticplus;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import net.weng1i.aquaticplus.block.ModBlocks;
import net.weng1i.aquaticplus.block.entity.ModBlockEntities;
import net.weng1i.aquaticplus.config.BiomeConfig;
import net.weng1i.aquaticplus.config.ConfigHolder;
import net.weng1i.aquaticplus.config.SpawnConfig;
import net.weng1i.aquaticplus.entity.ModEntities;
import net.weng1i.aquaticplus.entity.client.AnglerfishRenderer;
import net.weng1i.aquaticplus.entity.client.BluefinfishRenderer;
import net.weng1i.aquaticplus.entity.client.ModBoatRenderer;
import net.weng1i.aquaticplus.entity.client.RhinoRenderer;
import net.weng1i.aquaticplus.item.ModCreativeModTabs;
import net.weng1i.aquaticplus.item.ModItems;
import net.weng1i.aquaticplus.loot.ModLootModifiers;
import net.weng1i.aquaticplus.recipe.ModRecipes;
import net.weng1i.aquaticplus.screen.GemPolishingStationScreen;
import net.weng1i.aquaticplus.screen.ModMenuTypes;
import net.weng1i.aquaticplus.sound.ModSounds;
import net.weng1i.aquaticplus.util.ModWoodTypes;
import net.weng1i.aquaticplus.villager.ModVillagers;
import net.weng1i.aquaticplus.world.MobSpawnBiomeModifier;
import net.weng1i.aquaticplus.worldgen.biome.ModTerrablender;
import net.weng1i.aquaticplus.worldgen.biome.surface.ModSurfaceRules;
import net.weng1i.aquaticplus.worldgen.tree.ModFoliagePlacers;
import net.weng1i.aquaticplus.worldgen.tree.ModTrunkPlacerTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;
import terrablender.api.SurfaceRuleManager;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Aquaticplus.MOD_ID)
public class Aquaticplus {
    public static final String MOD_ID = "aquaticplus";
    public static final DeferredRegister<WorldCarver<?>> CARVER_REGISTER = DeferredRegister.create(Registries.CARVER, MOD_ID);
    public static final CommonProxy PROXY = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);
    public static final Logger LOGGER = LogUtils.getLogger();

    public Aquaticplus() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::setupClient);
        modEventBus.addListener(this::onModConfigEvent);
        final ModLoadingContext modLoadingContext = ModLoadingContext.get();
        ModCreativeModTabs.register(modEventBus);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);

        ModLootModifiers.register(modEventBus);
        ModVillagers.register(modEventBus);

        ModSounds.register(modEventBus);
        ModEntities.register(modEventBus);

        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);

        ModRecipes.register(modEventBus);
        ModTrunkPlacerTypes.register(modEventBus);

        ModFoliagePlacers.register(modEventBus);
        ModTerrablender.registerBiomes();


        final DeferredRegister<Codec<? extends BiomeModifier>> biomeModifiers = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, Aquaticplus.MOD_ID);
        biomeModifiers.register(modEventBus);
        biomeModifiers.register("am_mob_spawns", MobSpawnBiomeModifier::makeCodec);
        modLoadingContext.registerConfig(ModConfig.Type.COMMON, ConfigHolder.COMMON_SPEC, "aquaticplus.toml");
        PROXY.init();

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.CATMINT.getId(), ModBlocks.POTTED_CATMINT);

            SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, MOD_ID, ModSurfaceRules.makeRules());
        });
    }


    @SubscribeEvent
    public void onModConfigEvent(final ModConfigEvent event) {
        final ModConfig config = event.getConfig();
        // Rebake the configs when they change
        if (config.getSpec() == ConfigHolder.COMMON_SPEC) {
            SpawnConfig.bake(config);
        }
        BiomeConfig.init();
    }

    private void setup(final FMLCommonSetupEvent event) {
        PROXY.initPathfinding();
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.SAPPHIRE);
            event.accept(ModItems.RAW_SAPPHIRE);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            Sheets.addWoodType(ModWoodTypes.PINE);

            EntityRenderers.register(ModEntities.RHINO.get(), RhinoRenderer::new);
            EntityRenderers.register(ModEntities.MOD_BOAT.get(), pContext -> new ModBoatRenderer(pContext, false));
            EntityRenderers.register(ModEntities.MOD_CHEST_BOAT.get(), pContext -> new ModBoatRenderer(pContext, true));

            EntityRenderers.register(ModEntities.MOD_CHEST_BOAT.get(), pContext -> new ModBoatRenderer(pContext, true));

            EntityRenderers.register(ModEntities.BLUEFINFISH.get(), BluefinfishRenderer::new);
            EntityRenderers.register(ModEntities.ANGLERFISH.get(), AnglerfishRenderer::new);

            MenuScreens.register(ModMenuTypes.GEM_POLISHING_MENU.get(), GemPolishingStationScreen::new);
        }
    }

    private void setupClient(FMLClientSetupEvent event) {
        event.enqueueWork(PROXY::clientInit);
    }
}
