package net.kaupenjoe.tutorialmod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.kaupenjoe.tutorialmod.TutorialMod;
import net.kaupenjoe.tutorialmod.entity.custom.AnglerfishEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class AnglerfishRenderer extends GeoEntityRenderer<AnglerfishEntity> {

    public AnglerfishRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AnglerfishModel());
    }

    @Override
    public ResourceLocation getTextureLocation(AnglerfishEntity animatable) {
        return new ResourceLocation(TutorialMod.MOD_ID, "textures/entity/anglerfish.png");
    }

    @Override
    public void render(AnglerfishEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        if (pEntity.isBaby()) {
            pMatrixStack.scale(0.4f,0.4f,0.4f);
        }
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }
}