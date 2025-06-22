package net.kaupenjoe.tutorialmod.config;

import com.github.alexthe666.citadel.config.biome.BiomeEntryType;
import com.github.alexthe666.citadel.config.biome.SpawnBiomeData;

public class BiomesGroup {
    public static final SpawnBiomeData EMPTY = new SpawnBiomeData();

    public static final SpawnBiomeData ANGLERFISH = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, "minecraft:is_overworld", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, "minecraft:is_ocean", 0);




    public static final SpawnBiomeData BLUEFINFISH = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, "minecraft:is_overworld", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, "minecraft:is_ocean", 0);


}
