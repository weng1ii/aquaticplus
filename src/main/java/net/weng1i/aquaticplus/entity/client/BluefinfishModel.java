package net.weng1i.aquaticplus.entity.client;


import java.lang.Math;
import net.weng1i.aquaticplus.Aquaticplus;
import net.weng1i.aquaticplus.entity.custom.BluefinfishEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class BluefinfishModel extends GeoModel<BluefinfishEntity> {


	@Override
	public ResourceLocation getModelResource(BluefinfishEntity animatable) {
		return new ResourceLocation(Aquaticplus.MOD_ID, "geo/bluefinfish.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(BluefinfishEntity animatable) {
		return new ResourceLocation(Aquaticplus.MOD_ID, "textures/entity/bluefinfish.png");
	}

	@Override
	public ResourceLocation getAnimationResource(BluefinfishEntity animatable) {
		return new ResourceLocation(Aquaticplus.MOD_ID, "animations/bluefinfish.animation.json");
	}

	@Override
	public void setCustomAnimations(BluefinfishEntity animatable, long instanceId, AnimationState<BluefinfishEntity> animationState) {
		CoreGeoBone head = getAnimationProcessor().getBone("head");

		if(head != null) {
			EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
			head.setRotX(entityData.headPitch() * ((float) Math.PI / 180F));
			head.setRotY(entityData.netHeadYaw() * ((float) Math.PI / 180F));
		}
	}
}