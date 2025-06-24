package net.weng1i.aquaticplus.event;

import net.weng1i.aquaticplus.Aquaticplus;
import net.weng1i.aquaticplus.entity.ModEntities;
import net.weng1i.aquaticplus.entity.custom.AnglerfishEntity;
import net.weng1i.aquaticplus.entity.custom.BluefinfishEntity;
import net.weng1i.aquaticplus.entity.custom.RhinoEntity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Aquaticplus.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.RHINO.get(), RhinoEntity.createAttributes().build());
        event.put(ModEntities.BLUEFINFISH.get(), BluefinfishEntity.createAttributes().build());
        event.put(ModEntities.ANGLERFISH.get(), AnglerfishEntity.createAttributes().build());
    }

}
