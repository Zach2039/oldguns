package zach2039.oldguns.common.item.part;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import zach2039.oldguns.common.OldGuns;
import zach2039.oldguns.common.item.util.FirearmType.FirearmPart;

public abstract class ItemFirearmPart extends Item
{
	/**
	 * Part type of this firearm ammo item instance.
	 */
	protected FirearmPart PartType = FirearmPart.SMALL_HANDLE;
	
	public ItemFirearmPart(String name, FirearmPart partType)
	{
		setRegistryName(OldGuns.MODID, name);
		setUnlocalizedName(name);
		setMaxStackSize(1);
		setCreativeTab(OldGuns.OLDGUNS_CREATIVE_TAB);
		setPartType(partType);
	}
	
	public FirearmPart getPartType()
	{
		return PartType;
	}

	public void setPartType(FirearmPart partType)
	{
		PartType = partType;
	}
}
