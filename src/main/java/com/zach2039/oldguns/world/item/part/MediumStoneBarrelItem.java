package com.zach2039.oldguns.world.item.part;

import com.zach2039.oldguns.OldGuns;
import com.zach2039.oldguns.api.firearm.FirearmPart;

public class MediumStoneBarrelItem extends FirearmPartItem {

	public MediumStoneBarrelItem() {
		super((FirearmPartProperties) new FirearmPartProperties()				
				.partType(FirearmPart.MEDIUM_ROCK_BARREL)
				.tab(OldGuns.CREATIVE_MODE_TAB));
	}
}