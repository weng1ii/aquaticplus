package net.weng1i.aquaticplus.config;

import com.github.alexthe666.citadel.config.biome.BiomeEntryType;
import com.github.alexthe666.citadel.config.biome.SpawnBiomeData;

public class BiomesGroup {
    public static final SpawnBiomeData EMPTY = new SpawnBiomeData();

    public static final SpawnBiomeData ANGLERFISH = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_ocean", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "forge:is_cold/overworld", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "forge:is_hot/overworld", 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "forge:is_/overworld", 0)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "minecraft:lukewarm_ocean", 1)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "minecraft:deep_ocean", 2)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "minecraft:deep_lukewarm_ocean", 3);

    public static final SpawnBiomeData BLUEFINFISH = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_overworld", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_ocean", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "forge:is_hot/overworld", 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "minecraft:is_river", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, "forge:is_/overworld", 0)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "minecraft:lukewarm_ocean", 1)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "minecraft:deep_ocean", 2)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "minecraft:deep_lukewarm_ocean", 3);

}
