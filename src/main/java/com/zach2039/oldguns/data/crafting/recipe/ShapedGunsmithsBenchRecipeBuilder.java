package com.zach2039.oldguns.data.crafting.recipe;

import com.zach2039.oldguns.init.ModCrafting;
import com.zach2039.oldguns.world.item.crafting.recipe.ShapedGunsmithsBenchRecipe;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class ShapedGunsmithsBenchRecipeBuilder extends EnhancedShapedRecipeBuilder<ShapedGunsmithsBenchRecipe, ShapedGunsmithsBenchRecipeBuilder> {
	protected ShapedGunsmithsBenchRecipeBuilder(final ItemStack result) {
		super(result, ModCrafting.Recipes.GUNSMITHS_BENCH_SHAPED.get());
	}

	public static ShapedGunsmithsBenchRecipeBuilder shaped(final IItemProvider result) {
		return shaped(new ItemStack(result));
	}

	public static ShapedGunsmithsBenchRecipeBuilder shaped(final ItemStack result) {
		return new ShapedGunsmithsBenchRecipeBuilder(result);
	}
	
	public static ShapedGunsmithsBenchRecipeBuilder shaped(final IItemProvider result, final int count) {
		return shaped(new ItemStack(result, count));
	}
}
