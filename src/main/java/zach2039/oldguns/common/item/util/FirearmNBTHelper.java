package zach2039.oldguns.common.item.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import zach2039.oldguns.api.firearm.FirearmType.FirearmCondition;

public class FirearmNBTHelper
{
	/**
	 * Sets the ammo count of the firearm itemstack instance.
	 * @param stackIn
	 * @param ammoCount
	 */
	private static void setNBTTagAmmoCount(ItemStack stackIn, int ammoCount)
	{
		/* Create new NBT tag if none. */
		if (!stackIn.hasTagCompound())
			stackIn.setTagCompound(new NBTTagCompound());
		
		stackIn.getTagCompound().setInteger("ammoCount", ammoCount);
	}
	
	public static void setNBTTagMagazineStack(ItemStack stackIn, List<ItemStack> firearmAmmoList)
	{
		/* Create new NBT tag and set value to 0 if no NBT tag. */
		if (!stackIn.hasTagCompound())
			setNBTTagAmmoCount(stackIn, 0);
		
		/* Accumulate list of firearm ammo objects into NBT form. */
		NBTTagList ammoStackNBTTagList = new NBTTagList();
		for (ItemStack ammoStack : firearmAmmoList)
		{
			/* Serialize ammo itemstack. */
			NBTTagCompound ammoStackNBTForm = ammoStack.serializeNBT();
			
			/* Store in TagList. */
			ammoStackNBTTagList.appendTag(ammoStackNBTForm);
		}
		
		/* Set tag list on firearm stack. */
		stackIn.getTagCompound().setTag("ammoList", ammoStackNBTTagList);
		
		/* Set ammo count. */
		setNBTTagAmmoCount(stackIn, firearmAmmoList.size());
	}
	
	public static List<ItemStack> getNBTTagMagazineStack(ItemStack stackIn)
	{
		/* Create new NBT tag and set value to 0 if no NBT tag. */
		if (!stackIn.hasTagCompound())
			setNBTTagMagazineStack(stackIn, new ArrayList<ItemStack>());
		
		/* Get tag list from firearm item. */
		if (!stackIn.getTagCompound().hasKey("ammoList"))
			stackIn.getTagCompound().setTag("ammoList", new NBTTagList());
			
		NBTTagList ammoStackNBTTagList = (NBTTagList) stackIn.getTagCompound().getTag("ammoList");
		
		/* Populate list from deserialized itemstacks. */
		List<ItemStack> firearmAmmoList = new ArrayList<ItemStack>();
		ammoStackNBTTagList.forEach((t) -> 
				{
					firearmAmmoList.add(new ItemStack((NBTTagCompound) t)); 
				}
			);
		
		return firearmAmmoList;
	}
	
	public static void emptyNBTTagAmmo(ItemStack stackIn)
	{
		/* Set ammo list on itemstack to empty list. */
		setNBTTagMagazineStack(stackIn, new ArrayList<ItemStack>());
	}
	
	public static void pushNBTTagAmmo(ItemStack stackIn, ItemStack ammoStack)
	{
		/* Get ammo list from stack. */
		List<ItemStack> ammoStackList = getNBTTagMagazineStack(stackIn);
		
		/* Add item to list. */
		ammoStackList.add(ammoStack);
		
		/* Set ammo list on itemstack. */
		setNBTTagMagazineStack(stackIn, ammoStackList);
	}
	
	public static ItemStack peekNBTTagAmmo(ItemStack stackIn)
	{
		/* Ammo output itemstack. */
		ItemStack ammoStackOutput = ItemStack.EMPTY;
		
		/* Get ammo list from stack. */
		List<ItemStack> ammoStackList = getNBTTagMagazineStack(stackIn);
		
		/* Get ammo count of firearm stack. */
		int ammoCount = ammoStackList.size();
		
		/* Don't return anything from internal magazine stack if empty. */
		if (ammoCount > 0)
		{
			/* Get copy of topmost item. */
			ammoStackOutput = ammoStackList.get(ammoStackList.size() - 1).copy();
		}
		
		return ammoStackOutput;
	}
	
	public static int peekNBTTagAmmoCount(ItemStack stackIn)
	{
		/* Get ammo list from stack. */
		List<ItemStack> ammoStackList = getNBTTagMagazineStack(stackIn);
		
		/* Get ammo count of firearm stack. */
		int ammoCount = ammoStackList.size();
		
		return ammoCount;
	}
	
	public static ItemStack popNBTTagAmmo(ItemStack stackIn)
	{
		/* Ammo output itemstack. */
		ItemStack ammoStackOutput = ItemStack.EMPTY;
		
		/* Get ammo list from stack. */
		List<ItemStack> ammoStackList = getNBTTagMagazineStack(stackIn);
		
		/* Get ammo count of firearm stack. */
		int ammoCount = ammoStackList.size();
		
		/* Don't pop anything from internal magazine stack if empty. */
		if (ammoCount > 0)
		{
			/* Get copy of topmost item. */
			ammoStackOutput = ammoStackList.get(ammoStackList.size() - 1).copy();
			
			/* Remove topmost item from list. */
			ammoStackList.remove(ammoStackList.size() - 1);
			
			/* Set ammo list on itemstack. */
			setNBTTagMagazineStack(stackIn, ammoStackList);
		}
		
		return ammoStackOutput;
	}
	
	/**
	 * Sets the condition state of the firearm itemstack instance.
	 * @param stackIn
	 * @param ammoCount
	 */
	public static void setNBTTagCondition(ItemStack stackIn, FirearmCondition condition)
	{
		/* Create new NBT tag if none. */
		if (!stackIn.hasTagCompound())
			stackIn.setTagCompound(new NBTTagCompound());
		
		stackIn.getTagCompound().setInteger("condition", condition.ordinal());
	}
	
	/**
	 * Gets the condition state of the firearm itemstack instance.
	 * @param stackIn
	 * @param ammoCount
	 */
	public static FirearmCondition getNBTTagCondition(ItemStack stackIn)
	{
		/* Create new NBT tag and set value if no NBT tag. */
		if (!stackIn.hasTagCompound() || !stackIn.getTagCompound().hasKey("condition"))
			setNBTTagCondition(stackIn, FirearmCondition.VERY_GOOD);

		return FirearmCondition.values()[stackIn.getTagCompound().getInteger("condition")];
	}
	
	/**
	 * Refreshes the condition of a firearm itemstack instance.
	 * @param stackIn
	 */
	public static void refreshFirearmCondition(ItemStack stackIn)
	{
		float damageLevel = (float)((float)stackIn.getItemDamage() / (float)stackIn.getMaxDamage());
		
		/* If firearm is broken, don't bother setting state. */
		if (FirearmNBTHelper.getNBTTagCondition(stackIn) == FirearmCondition.BROKEN)
			return;
		
		/* Set condition of firearm based on damage percentage. */
		if (damageLevel < 0.10f)
		{
			/* Very good condition. */
			FirearmNBTHelper.setNBTTagCondition(stackIn, FirearmCondition.VERY_GOOD);
		}
		else if (damageLevel < 0.25f)
		{
			/* Good condition. */
			FirearmNBTHelper.setNBTTagCondition(stackIn, FirearmCondition.GOOD);
		}
		else if (damageLevel < 0.75f)
		{
			/* Fair condition. */
			FirearmNBTHelper.setNBTTagCondition(stackIn, FirearmCondition.FAIR);
		}
		else if (damageLevel < 0.90f)
		{
			/* Poor condition. */
			FirearmNBTHelper.setNBTTagCondition(stackIn, FirearmCondition.POOR);
		}
		else if (stackIn.getItemDamage() != stackIn.getMaxDamage())
		{
			/* Very poor condition. */
			FirearmNBTHelper.setNBTTagCondition(stackIn, FirearmCondition.VERY_POOR);
		}
		else
		{
			FirearmNBTHelper.setNBTTagCondition(stackIn, FirearmCondition.BROKEN);
		}
	}
}