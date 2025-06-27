package net.weng1i.aquaticplus.sound;

import net.weng1i.aquaticplus.Aquaticplus;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.util.ForgeSoundType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Aquaticplus.MOD_ID);

    public static final RegistryObject<SoundEvent> ANGLER_BITE = registerSoundEvents("angler_bite");
    public static final RegistryObject<SoundEvent> ANGLER_AMBIENT = registerSoundEvents("angler_ambient");
    public static final RegistryObject<SoundEvent> ANGLER_DEAD = registerSoundEvents("angler_dead");



    private static RegistryObject<SoundEvent> registerSoundEvents(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Aquaticplus.MOD_ID, name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
