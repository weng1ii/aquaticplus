package net.kaupenjoe.tutorialmod.entity.client;


import java.lang.Math;
import net.kaupenjoe.tutorialmod.TutorialMod;
import net.kaupenjoe.tutorialmod.entity.custom.AnglerfishEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class AnglerfishModel extends GeoModel<AnglerfishEntity> {


    @Override
    public ResourceLocation getModelResource(AnglerfishEntity animatable) {
        return new ResourceLocation(TutorialMod.MOD_ID, "geo/anglerfish.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(AnglerfishEntity animatable) {
        return new ResourceLocation(TutorialMod.MOD_ID, "textures/entity/anglerfish.png");
    }

    @Override
    public ResourceLocation getAnimationResource(AnglerfishEntity animatable) {
        return new ResourceLocation(TutorialMod.MOD_ID, "animations/anglerfish.animation.json");
    }

    @Override
    public void setCustomAnimations(AnglerfishEntity animatable, long instanceId, AnimationState<AnglerfishEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head");

        if(head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            head.setRotX(entityData.headPitch() * ((float) Math.PI / 180F));
            head.setRotY(entityData.netHeadYaw() * ((float) Math.PI / 180F));
        }
    }
}