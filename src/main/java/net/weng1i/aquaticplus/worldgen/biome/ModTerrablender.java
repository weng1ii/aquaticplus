package net.weng1i.aquaticplus.worldgen.biome;

import net.weng1i.aquaticplus.Aquaticplus;
import net.minecraft.resources.ResourceLocation;
import terrablender.api.Regions;

public class ModTerrablender {
    public static void registerBiomes() {
        Regions.register(new ModOverworldRegion(new ResourceLocation(Aquaticplus.MOD_ID, "overworld"), 5));
    }
}
