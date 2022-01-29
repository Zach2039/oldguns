package com.zach2039.oldguns.world.item.part;

import com.zach2039.oldguns.OldGuns;
import com.zach2039.oldguns.api.firearm.FirearmPart;

public class GoldGearSetItem extends FirearmPartItem {

	public GoldGearSetItem() {
		super((FirearmPartProperties) new FirearmPartProperties()				
				.partType(FirearmPart.GOLD_GEAR_SET)
				.tab(OldGuns.CREATIVE_MODE_TAB));
	}
}