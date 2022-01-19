package com.zach2039.oldguns.inventory;

import com.zach2039.oldguns.init.ModRecipeTypes;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class OldGunsResultSlot extends Slot {
	private final GunsmithsBenchCraftingContainer craftSlots;
	private final PlayerEntity player;
	private int removeCount;

	public OldGunsResultSlot(PlayerEntity player, GunsmithsBenchCraftingContainer craftinv, IInventory inv, int slot, int x, int y) {
		super(inv, slot, x, y);
		this.player = player;
		this.craftSlots = craftinv;
	}

	public boolean mayPlace(ItemStack p_40178_) {
		return false;
	}

	public ItemStack remove(int p_40173_) {
		if (this.hasItem()) {
			this.removeCount += Math.min(p_40173_, this.getItem().getCount());
		}

		return super.remove(p_40173_);
	}

	protected void onQuickCraft(ItemStack p_40180_, int p_40181_) {
		this.removeCount += p_40181_;
		this.checkTakeAchievements(p_40180_);
	}

	protected void onSwapCraft(int p_40183_) {
		this.removeCount += p_40183_;
	}

	protected void checkTakeAchievements(ItemStack p_40185_) {
		if (this.removeCount > 0) {
			p_40185_.onCraftedBy(this.player.level, this.player, this.removeCount);
		}

		if (this.container instanceof IRecipeHolder) {
			((IRecipeHolder)this.container).awardUsedRecipes(this.player);
		}

		this.removeCount = 0;
	}

	public ItemStack onTake(PlayerEntity player, ItemStack stack) {
		this.checkTakeAchievements(stack);
		net.minecraftforge.common.ForgeHooks.setCraftingPlayer(player);
		NonNullList<ItemStack> nonnulllist = player.level.getRecipeManager().getRemainingItemsFor(ModRecipeTypes.GUNSMITHS_BENCH, this.craftSlots, player.level);
		net.minecraftforge.common.ForgeHooks.setCraftingPlayer(null);
		for(int i = 0; i < nonnulllist.size(); ++i) {
			ItemStack itemstack = this.craftSlots.getItem(i);
			ItemStack itemstack1 = nonnulllist.get(i);
			if (!itemstack.isEmpty()) {
				this.craftSlots.removeItem(i, 1);
				itemstack = this.craftSlots.getItem(i);
			}

			if (!itemstack1.isEmpty()) {
				if (itemstack.isEmpty()) {
					this.craftSlots.setItem(i, itemstack1);
				} else if (ItemStack.isSame(itemstack, itemstack1) && ItemStack.tagMatches(itemstack, itemstack1)) {
					itemstack1.grow(itemstack.getCount());
					this.craftSlots.setItem(i, itemstack1);
				} else if (!this.player.inventory.add(itemstack1)) {
					this.player.drop(itemstack1, false);
				}
			}
		}
		return stack;
	}
}
