package com.zach2039.oldguns.item.part;

import com.zach2039.oldguns.OldGuns;
import com.zach2039.oldguns.api.firearm.FirearmType.FirearmPart;

public class LargeBrassBarrelItem extends FirearmPartItem {

	public LargeBrassBarrelItem() {
		super((FirearmPartProperties) new FirearmPartProperties()				
				.partType(FirearmPart.LARGE_METAL_BARREL)
				.tab(OldGuns.ITEM_GROUP));
	}
}