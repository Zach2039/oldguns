package zach2039.oldguns.common.item.tools;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import zach2039.oldguns.common.OldGuns;

public class ItemRepairKit extends Item
{
	public ItemRepairKit()
	{
		setRegistryName(OldGuns.MODID, "repair_kit");
		setUnlocalizedName("repair_kit");
		setMaxStackSize(1);
		setMaxDamage(4);
		setCreativeTab(CreativeTabs.COMBAT);
	}
	
	@Override
	public Item getContainerItem()
	{
		return this;
	}
}
