package net.weng1i.aquaticplus.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFoods {
    public static final FoodProperties RAW_BLUEFINFISH = new FoodProperties.Builder().nutrition(1)
            .saturationMod(0.2f).build();
    public static final FoodProperties COOKED_BLUEFINFISH = new FoodProperties.Builder().nutrition(6)
            .saturationMod(0.2f).build();

}
