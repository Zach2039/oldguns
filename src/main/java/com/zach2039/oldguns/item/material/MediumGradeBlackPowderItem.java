package com.zach2039.oldguns.item.material;

import com.zach2039.oldguns.OldGuns;

public class MediumGradeBlackPowderItem extends MaterialItem {
	
	public MediumGradeBlackPowderItem() {
		super(new Properties()
				.stacksTo(64)
				.tab(OldGuns.ITEM_GROUP));
	}

}