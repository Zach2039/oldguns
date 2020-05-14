package zach2039.oldguns.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import zach2039.oldguns.common.init.ModBlocks;

public class ContainerGunsmithsBench extends Container
{
	/** Crafting matrix inventory of 3x3. */
	public InventoryCraftingGunsmithsBench craftMatrix = new InventoryCraftingGunsmithsBench(this, 3, 3);
	
	/** Crafting result inventory. */
	public InventoryCraftResult craftResult = new InventoryCraftResult();
	
	/** World of gunsmith's bench. */
	private final World world;
	
	/** Position of gunsmith's bench. */
	private final BlockPos pos;
	
	/** EntityPlayer using the gunsmith's bench. */
	private final EntityPlayer player;
	
	public ContainerGunsmithsBench(InventoryPlayer playerInventory, World worldIn, BlockPos posIn)
	{
		this.world = worldIn;
		this.pos = posIn;
		this.player = playerInventory.player;
		
		// Add slots to this container gui for result.
		this.addSlotToContainer(new SlotCrafting(playerInventory.player, this.craftMatrix, this.craftResult, 0, 124, 35));
		
		// Add input slots to this container.
		for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 3; ++j)
            {
                this.addSlotToContainer(new Slot(this.craftMatrix, j + i * 3, 30 + j * 18, 17 + i * 18));
            }
        }

		// Add player inventory slots to this container.
        for (int k = 0; k < 3; ++k)
        {
            for (int i1 = 0; i1 < 9; ++i1)
            {
                this.addSlotToContainer(new Slot(playerInventory, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
            }
        }

        // Add hotbar slots to this container.
        for (int l = 0; l < 9; ++l)
        {
            this.addSlotToContainer(new Slot(playerInventory, l, 8 + l * 18, 142));
        }
	}

	@Override
	public void onCraftMatrixChanged(IInventory inventoryIn)
    {
		// Send packet using mincraft built-in methods.
        this.slotChangedCraftingGrid(this.world, this.player, this.craftMatrix, this.craftResult);
    }
	
	@Override
	public void onContainerClosed(EntityPlayer playerIn)
    {
        super.onContainerClosed(playerIn);

        if (!this.world.isRemote)
        {
            this.clearContainer(playerIn, this.world, this.craftMatrix);
        }
    }
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		if (this.world.getBlockState(this.pos).getBlock() != ModBlocks.GUNSMITHS_BENCH)
        {
            return false;
        }
        else
        {
            return playerIn.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index == 0)
            {
                itemstack1.getItem().onCreated(itemstack1, this.world, playerIn);

                if (!this.mergeItemStack(itemstack1, 10, 46, true))
                {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (index >= 10 && index < 37)
            {
                if (!this.mergeItemStack(itemstack1, 37, 46, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (index >= 37 && index < 46)
            {
                if (!this.mergeItemStack(itemstack1, 10, 37, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 10, 46, false))
            {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount())
            {
                return ItemStack.EMPTY;
            }

            ItemStack itemstack2 = slot.onTake(playerIn, itemstack1);

            if (index == 0)
            {
                playerIn.dropItem(itemstack2, false);
            }
        }

        return itemstack;
    }
	
	@Override
	public boolean canMergeSlot(ItemStack stack, Slot slotIn)
    {
        return slotIn.inventory != this.craftResult && super.canMergeSlot(stack, slotIn);
    }
}
