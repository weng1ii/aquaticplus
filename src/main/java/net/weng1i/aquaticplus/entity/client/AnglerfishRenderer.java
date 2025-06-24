package net.weng1i.aquaticplus.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.weng1i.aquaticplus.Aquaticplus;
import net.weng1i.aquaticplus.entity.custom.AnglerfishEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class AnglerfishRenderer extends GeoEntityRenderer<AnglerfishEntity> {

    public AnglerfishRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AnglerfishModel());
    }

    @Override
    public ResourceLocation getTextureLocation(AnglerfishEntity animatable) {
        return new ResourceLocation(Aquaticplus.MOD_ID, "textures/entity/anglerfish.png");
    }

    @Override
    public void render(AnglerfishEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        if (pEntity.isBaby()) {
            pMatrixStack.scale(0.4f,0.4f,0.4f);
        }
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }
}