package net.kaupenjoe.tutorialmod.config;

import com.github.alexthe666.citadel.config.biome.BiomeEntryType;
import com.github.alexthe666.citadel.config.biome.SpawnBiomeData;

public class BiomesGroup {
    public static final SpawnBiomeData EMPTY = new SpawnBiomeData();

    public static final SpawnBiomeData ANGLERFISH = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_overworld", 0)  // Must be overworld
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_ocean", 0)       // Ocean biomes
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "minecraft:deep_lukewarm_ocean", 0) // Include lukewarm deep ocean
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "minecraft:deep_cold_ocean", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_deep_ocean", 0);

    public static final SpawnBiomeData BLUEFINFISH = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_overworld", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_river", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, "forge:is_cold/overworld", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_deep_ocean", 0)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "minecraft:deep_lukewarm_ocean", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, "forge:is_hot/overworld", 0);



}
