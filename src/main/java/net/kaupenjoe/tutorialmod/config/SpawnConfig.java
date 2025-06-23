package net.kaupenjoe.tutorialmod.config;

import net.kaupenjoe.tutorialmod.TutorialMod;
import net.minecraftforge.fml.config.ModConfig;

public class SpawnConfig {
    public static int anglerfishSpawnWeight = 2;
    public static int anglerfishSpawnRolls = 1;
    public static int bluefinfishSpawnWeight = 4;
    public static int bluefinfishSpawnRolls = 2;
    public static int pathfindingThreads = 5;

    public static void bake(ModConfig config) {
        try {
            anglerfishSpawnWeight = ConfigHolder.COMMON.anglerfishSpawnWeight.get();
            anglerfishSpawnRolls = ConfigHolder.COMMON.anglerfishSpawnRolls.get();
            bluefinfishSpawnWeight = ConfigHolder.COMMON.bluefinfishSpawnWeight.get();
            bluefinfishSpawnRolls = ConfigHolder.COMMON.bluefinfishSpawnRolls.get();
            pathfindingThreads = ConfigHolder.COMMON.pathfindingThreads.get();
        } catch (Exception e) {
            TutorialMod.LOGGER.warn("An exception was caused trying to load the config for Tutorial Mod");
            e.printStackTrace();
        }
    }
}
