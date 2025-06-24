package net.weng1i.aquaticplus.config;

import com.github.alexthe666.citadel.config.biome.SpawnBiomeConfig;
import com.github.alexthe666.citadel.config.biome.SpawnBiomeData;
import net.weng1i.aquaticplus.Aquaticplus;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class BiomeConfig {
    public static final Pair<String, SpawnBiomeData> anglerfish = Pair.of("aquaticplus:anglerfish_spawns", BiomesGroup.ANGLERFISH);
    public static final Pair<String, SpawnBiomeData> bluefinfish = Pair.of("aquaticplus:bluefinfish_spawns", BiomesGroup.BLUEFINFISH);

    private static boolean init = false;
    private static final Map<String, SpawnBiomeData> biomeConfigValues = new HashMap<>();

    public static void init() {
        try {
            for (Field f : BiomeConfig.class.getDeclaredFields()) {
                Object obj = f.get(null);
                if(obj instanceof Pair){
                    String id = (String)((Pair) obj).getLeft();
                    SpawnBiomeData data = (SpawnBiomeData)((Pair) obj).getRight();
                    biomeConfigValues.put(id, SpawnBiomeConfig.create(new ResourceLocation(id), data));
                }
            }
        }catch (Exception e){
            Aquaticplus.LOGGER.warn("Encountered error building aquaticplus biome config .json files");
            e.printStackTrace();
        }
        init = true;
    }

    public static boolean test(Pair<String, SpawnBiomeData> entry, Holder<Biome> biome, ResourceLocation name){
        if(!init){
            return false;
        }
        return biomeConfigValues.get(entry.getKey()).matches(biome, name);
    }

    public static boolean test(Pair<String, SpawnBiomeData> spawns, Holder<Biome> biome) {
        return test(spawns, biome, ForgeRegistries.BIOMES.getKey(biome.value()));
    }
}
