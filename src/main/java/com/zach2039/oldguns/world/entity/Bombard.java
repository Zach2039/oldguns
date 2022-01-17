package com.zach2039.oldguns.world.entity;

import com.zach2039.oldguns.OldGuns;
import com.zach2039.oldguns.api.artillery.ArtilleryEffect;
import com.zach2039.oldguns.api.artillery.ArtilleryType;
import com.zach2039.oldguns.config.OldGunsConfig;
import com.zach2039.oldguns.config.OldGunsConfig.ArtilleryAttributes;
import com.zach2039.oldguns.init.ModEntities;
import com.zach2039.oldguns.network.ArtilleryEffectMessage;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.PacketDistributor.TargetPoint;

public class Bombard extends MoveableArtillery {
	private static final ArtilleryAttributes artilleryAttributes = OldGunsConfig.SERVER.artillerySettings.bombard;
	
	public Bombard(EntityType<? extends MoveableArtillery> entity, World level) {
		super(ModEntities.BOMBARD.get(), level);
	}

	public Bombard(World level, double x, double y, double z) {
		super(ModEntities.BOMBARD.get(), level, x, y, z);
	}

	@Override
	public void initArtilleryConfiguration() {
		setArtilleryType(ArtilleryType.BOMBARD);
		setBaseProjectileSpeed(artilleryAttributes.projectileSpeed.get());
		setEffectiveRangeModifier(artilleryAttributes.effectiveRangeModifier.get());
		setBaseDeviation(artilleryAttributes.deviationModifier.get());
		setDamageModifier(artilleryAttributes.damageModifier.get());
	}
	
	@Override
	public void doFiringEffect(World level, Player player, double posX, double posY, double posZ)
	{
		TargetPoint point = new PacketDistributor.TargetPoint(
				posX, posY, posZ, 1600d, level.dimension());
		
		
		OldGuns.network.send(PacketDistributor.NEAR.with(() -> point), 
				new ArtilleryEffectMessage((LivingEntity)player, ArtilleryEffect.CANNON_SHOT, posX, posY + getBarrelHeight(), posZ,
						getBarrelPitch(), getBarrelYaw(), 0)
				);
	}
}
