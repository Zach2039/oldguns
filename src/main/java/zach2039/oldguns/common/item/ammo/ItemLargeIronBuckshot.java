package zach2039.oldguns.common.item.ammo;

import zach2039.oldguns.api.ammo.IFirearmAmmo;

public class ItemLargeIronBuckshot extends ItemFirearmAmmo implements IFirearmAmmo
{
	public ItemLargeIronBuckshot()
	{
		super("large_iron_buckshot", 4);
		setAmmoDamage(15.0f);
		setProjectileSize(0.3f);
		setProjectileCount(7);
		setProjectileEffectiveRange(30.0f);
		setProjectileDeviationModifier(1.5f);
	}
}
