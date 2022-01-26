package com.zach2039.oldguns.item.part;

import com.zach2039.oldguns.OldGuns;
import com.zach2039.oldguns.api.firearm.FirearmType.FirearmPart;

public class TinyIronBarrelItem extends FirearmPartItem {

	public TinyIronBarrelItem() {
		super((FirearmPartProperties) new FirearmPartProperties()				
				.partType(FirearmPart.TINY_METAL_BARREL)
				.tab(OldGuns.ITEM_GROUP));
	}
}