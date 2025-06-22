package net.kaupenjoe.tutorialmod.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfig {
    public final ForgeConfigSpec.IntValue anglerfishSpawnWeight;
    public final ForgeConfigSpec.IntValue anglerfishSpawnRolls;
    public final ForgeConfigSpec.IntValue bluefinfishSpawnWeight;
    public final ForgeConfigSpec.IntValue bluefinfishSpawnRolls;
    public ForgeConfigSpec.IntValue pathfindingThreads;

    public CommonConfig(final ForgeConfigSpec.Builder builder) {
        builder.push("general");
        anglerfishSpawnWeight = buildInt(builder, "anglerfishSpawnWeight", "spawns", SpawnConfig.anglerfishSpawnWeight, 0, 1000, "Spawn Weight, added to a pool of other mobs for each biome. Higher number = higher chance of spawning. 0 = disable spawn");
        anglerfishSpawnRolls = buildInt(builder, "anglerfishSpawnRolls", "spawns", SpawnConfig.anglerfishSpawnRolls, 0, Integer.MAX_VALUE, "Random roll chance to enable mob spawning. Higher number = lower chance of spawning");
        bluefinfishSpawnWeight = buildInt(builder, "bluefinfishSpawnWeight", "spawns", SpawnConfig.bluefinfishSpawnWeight, 0, 1000, "Spawn Weight, added to a pool of other mobs for each biome. Higher number = higher chance of spawning. 0 = disable spawn");
        bluefinfishSpawnRolls = buildInt(builder, "bluefinfishSpawnRolls", "spawns", SpawnConfig.bluefinfishSpawnRolls, 0, Integer.MAX_VALUE, "Random roll chance to enable mob spawning. Higher number = lower chance of spawning");
        builder.pop();
        builder.push("dangerZone");
        pathfindingThreads = buildInt(builder, "pathfindingThreads", "dangerZone", SpawnConfig.pathfindingThreads, 1, 100,"How many cpu cores some mobs(elephants, leafcutter ants, bison etc) should utilize when pathing. Bigger number = less impact on TPS");
        builder.pop();
    }

    private static ForgeConfigSpec.IntValue buildInt(ForgeConfigSpec.Builder builder, String name, String catagory, int defaultValue, int min, int max, String comment) {
        return builder.comment(comment).translation(name).defineInRange(name, defaultValue, min, max);
    }



}
