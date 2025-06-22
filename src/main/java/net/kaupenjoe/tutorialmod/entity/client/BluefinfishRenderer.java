package net.kaupenjoe.tutorialmod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.kaupenjoe.tutorialmod.TutorialMod;
import net.kaupenjoe.tutorialmod.entity.custom.AnglerfishEntity;
import net.kaupenjoe.tutorialmod.entity.custom.BluefinfishEntity;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BluefinfishRenderer extends GeoEntityRenderer<BluefinfishEntity> {

    public BluefinfishRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BluefinfishModel());
    }

    @Override
    public ResourceLocation getTextureLocation(BluefinfishEntity animatable) {
        return new ResourceLocation(TutorialMod.MOD_ID, "textures/entity/bluefinfish.png");
    }

    @Override
    public void render(BluefinfishEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        if (pEntity.isBaby()) {
            pMatrixStack.scale(0.5f,0.5f,0.5f);
        }
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }
}
