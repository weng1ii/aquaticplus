package net.kaupenjoe.tutorialmod.worldgen.carver;

import net.kaupenjoe.tutorialmod.TutorialMod;
import net.minecraft.world.level.levelgen.carver.CarverConfiguration;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModWorldCarvers {
    //Carvers

    public static final RegistryObject<WorldCarver<CaveCarverConfiguration>> ORIGIN_CAVE = register("origin_cave", () -> new OriginCaveWorldCarver(CaveCarverConfiguration.CODEC));

    private static <C extends CarverConfiguration, F extends WorldCarver<C>> RegistryObject<F> register(String key, Supplier<F> carverSupplier)
    {
        return TutorialMod.CARVER_REGISTER.register(key, carverSupplier);
    }

    public static void setup() {}
}
