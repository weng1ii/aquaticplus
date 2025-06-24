package net.weng1i.aquaticplus.world;

import com.mojang.serialization.Codec;
import net.weng1i.aquaticplus.Aquaticplus;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MobSpawnBiomeModifier implements BiomeModifier {
    private static final RegistryObject<Codec<? extends BiomeModifier>> SERIALIZER = RegistryObject.create(new ResourceLocation(Aquaticplus.MOD_ID, "am_mob_spawns"), ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, Aquaticplus.MOD_ID);
    private static final Logger LOGGER = LoggerFactory.getLogger(MobSpawnBiomeModifier.class);

    public MobSpawnBiomeModifier() {

    }

    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == Phase.ADD) {
            ResourceLocation biomeId = biome.unwrapKey().map(k -> k.location()).orElse(ResourceLocation.tryParse("unknown"));
            LOGGER.info("[MobSpawnBiomeModifier] Modifying biome: {}", biomeId);

            ModWorldRegistry.addBiomeSpawns(biome, builder);
        }
    }

    public Codec<? extends BiomeModifier> codec() {
        return (Codec)SERIALIZER.get();
    }

    public static Codec<MobSpawnBiomeModifier> makeCodec() {
        return Codec.unit(MobSpawnBiomeModifier::new);
    }
}

