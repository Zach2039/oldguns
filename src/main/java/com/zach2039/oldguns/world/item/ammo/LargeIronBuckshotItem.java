package com.zach2039.oldguns.world.item.ammo;

import com.zach2039.oldguns.OldGuns;
import com.zach2039.oldguns.api.ammo.IFirearmAmmo;
import com.zach2039.oldguns.config.OldGunsConfig;
import com.zach2039.oldguns.config.OldGunsConfig.FirearmAmmoAttributes;

public class LargeIronBuckshotItem extends FirearmAmmoItem implements IFirearmAmmo {

	private static final FirearmAmmoAttributes ammoAttributes = OldGunsConfig.COMMON.firearmSettings.large_iron_buckshot;
	
	public LargeIronBuckshotItem() {
		super((FirearmAmmoProperties) new FirearmAmmoProperties()
				.projectileCount(ammoAttributes.projectileCount.get())
				.projectileDamage(ammoAttributes.projectileDamage.get().floatValue())
				.projectileSize(ammoAttributes.projectileSize.get().floatValue())
				.projectileEffectiveRange(ammoAttributes.projectileEffectiveRange.get().floatValue())
				.projectileDeviationModifier(ammoAttributes.projectileDeviationModifier.get().floatValue())
				.stacksTo(ammoAttributes.maxStackSize.get())				
				.tab(OldGuns.CREATIVE_MODE_TAB)
				);
	}
}