package zach2039.oldguns.common.item.ammo;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import zach2039.oldguns.common.entity.EntityProjectile;

public class ItemLargeStoneBirdshot extends ItemFirearmAmmo implements IFirearmAmmo
{
	public ItemLargeStoneBirdshot()
	{
		super("large_stone_birdshot", 4);
		setAmmoDamage(2.0f);
		setProjectileSize(0.2f);
		setProjectileCount(10);
	}
}
