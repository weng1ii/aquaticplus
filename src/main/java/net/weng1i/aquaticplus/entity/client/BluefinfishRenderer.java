package net.weng1i.aquaticplus.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.weng1i.aquaticplus.Aquaticplus;
import net.weng1i.aquaticplus.entity.custom.BluefinfishEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BluefinfishRenderer extends GeoEntityRenderer<BluefinfishEntity> {

    public BluefinfishRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BluefinfishModel());
    }

    @Override
    public ResourceLocation getTextureLocation(BluefinfishEntity animatable) {
        return new ResourceLocation(Aquaticplus.MOD_ID, "textures/entity/bluefinfish.png");
    }

    @Override
    public void render(BluefinfishEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        if (pEntity.isBaby()) {
            pMatrixStack.scale(0.5f,0.5f,0.5f);
        }
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }
}
