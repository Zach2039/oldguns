package com.zach2039.oldguns.world.item.crafting.cauldron;

import java.util.List;
import java.util.Map;

import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface ICauldronRecipe {

    boolean isInput(ItemStack input);

    ItemStack getOutput(ItemStack input);

	ItemStack getOutput();
}
