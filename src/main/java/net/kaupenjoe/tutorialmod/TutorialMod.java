package net.kaupenjoe.tutorialmod;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import net.kaupenjoe.tutorialmod.block.ModBlocks;
import net.kaupenjoe.tutorialmod.block.entity.ModBlockEntities;
import net.kaupenjoe.tutorialmod.config.BiomeConfig;
import net.kaupenjoe.tutorialmod.config.ConfigHolder;
import net.kaupenjoe.tutorialmod.config.SpawnConfig;
import net.kaupenjoe.tutorialmod.entity.ModEntities;
import net.kaupenjoe.tutorialmod.entity.client.AnglerfishRenderer;
import net.kaupenjoe.tutorialmod.entity.client.BluefinfishRenderer;
import net.kaupenjoe.tutorialmod.entity.client.ModBoatRenderer;
import net.kaupenjoe.tutorialmod.entity.client.RhinoRenderer;
import net.kaupenjoe.tutorialmod.item.ModCreativeModTabs;
import net.kaupenjoe.tutorialmod.item.ModItems;
import net.kaupenjoe.tutorialmod.loot.ModLootModifiers;
import net.kaupenjoe.tutorialmod.recipe.ModRecipes;
import net.kaupenjoe.tutorialmod.screen.GemPolishingStationScreen;
import net.kaupenjoe.tutorialmod.screen.ModMenuTypes;
import net.kaupenjoe.tutorialmod.sound.ModSounds;
import net.kaupenjoe.tutorialmod.util.ModWoodTypes;
import net.kaupenjoe.tutorialmod.villager.ModVillagers;
import net.kaupenjoe.tutorialmod.world.MobSpawnBiomeModifier;
import net.kaupenjoe.tutorialmod.worldgen.biome.ModTerrablender;
import net.kaupenjoe.tutorialmod.worldgen.biome.surface.ModSurfaceRules;
import net.kaupenjoe.tutorialmod.worldgen.carver.ModWorldCarvers;
import net.kaupenjoe.tutorialmod.worldgen.tree.ModFoliagePlacers;
import net.kaupenjoe.tutorialmod.worldgen.tree.ModTrunkPlacerTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
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
@Mod(TutorialMod.MOD_ID)
public class TutorialMod {
    public static final String MOD_ID = "tutorialmod";
    public static final DeferredRegister<WorldCarver<?>> CARVER_REGISTER = DeferredRegister.create(Registries.CARVER, MOD_ID);
    public static final CommonProxy PROXY = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);
    public static final Logger LOGGER = LogUtils.getLogger();

    public TutorialMod() {
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


        final DeferredRegister<Codec<? extends BiomeModifier>> biomeModifiers = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, TutorialMod.MOD_ID);
        biomeModifiers.register(modEventBus);
        biomeModifiers.register("am_mob_spawns", MobSpawnBiomeModifier::makeCodec);
        modLoadingContext.registerConfig(ModConfig.Type.COMMON, ConfigHolder.COMMON_SPEC, "tutorialmod.toml");
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
