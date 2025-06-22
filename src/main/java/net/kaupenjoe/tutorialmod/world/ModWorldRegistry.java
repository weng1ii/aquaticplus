package net.kaupenjoe.tutorialmod.world;

import com.github.alexthe666.citadel.config.biome.SpawnBiomeData;
import net.kaupenjoe.tutorialmod.TutorialMod;
import net.kaupenjoe.tutorialmod.config.BiomeConfig;
import net.kaupenjoe.tutorialmod.config.SpawnConfig;
import net.kaupenjoe.tutorialmod.entity.ModEntities;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.mojang.text2speech.Narrator.LOGGER;


@Mod.EventBusSubscriber(modid = TutorialMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModWorldRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger("MobSpawnBiomeModifier");

    private static ResourceLocation getBiomeName(Holder<Biome> biome) {
        return biome.unwrap().map((resourceKey) -> resourceKey.location(), (noKey) -> null);
    }

    public static boolean testBiome(Pair<String, SpawnBiomeData> entry, Holder<Biome> biome) {
        try {
            boolean result = BiomeConfig.test(entry, biome, getBiomeName(biome));
            LOGGER.debug("Biome test for {}: {} -> {}", entry.getLeft(), getBiomeName(biome), result);
            return result;
        } catch (Exception e) {
            LOGGER.warn("Could not test biome config for {}, defaulting to no spawns.", entry.getLeft(), e);
            return false;
        }
    }

    public static void addBiomeSpawns(Holder<Biome> biome, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        ResourceLocation biomeId = getBiomeName(biome);
        if (biomeId == null) return;

        // Debug: Log weights at runtime
        LOGGER.debug("Biome {}: Anglerfish weight = {}, Bluefinfish weight = {}",
                biomeId, SpawnConfig.anglerfishSpawnWeight, SpawnConfig.bluefinfishSpawnWeight);

        // Anglerfish spawn
        if (testBiome(BiomeConfig.anglerfish, biome) && SpawnConfig.anglerfishSpawnWeight > 0) {
            LOGGER.info("Adding Anglerfish to biome: {}", biomeId);
            builder.getMobSpawnSettings().getSpawner(MobCategory.MONSTER)
                    .add(new MobSpawnSettings.SpawnerData(ModEntities.ANGLERFISH.get(), SpawnConfig.anglerfishSpawnWeight, 0, 1));
        } else {
            LOGGER.info("Biome {} is not valid for Anglerfish or spawn weight is 0", biomeId);
        }

        // Bluefinfish spawn
        if (testBiome(BiomeConfig.bluefinfish, biome) && SpawnConfig.bluefinfishSpawnWeight > 0) {
            LOGGER.info("Adding Bluefinfish to biome: {}", biomeId);
            builder.getMobSpawnSettings().getSpawner(MobCategory.WATER_CREATURE)
                    .add(new MobSpawnSettings.SpawnerData(ModEntities.BLUEFINFISH.get(), SpawnConfig.bluefinfishSpawnWeight, 3, 4));
        } else {
            LOGGER.info("Biome {} is not valid for Bluefinfish or spawn weight is 0", biomeId);
        }
    }
}
