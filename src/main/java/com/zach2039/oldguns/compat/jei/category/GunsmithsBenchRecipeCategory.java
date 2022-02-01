/* Some taken from IE with the following license.
 * 
 * ---
 * 
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */
package com.zach2039.oldguns.compat.jei.category;

import java.util.ArrayList;
import java.util.List;

import com.zach2039.oldguns.OldGuns;
import com.zach2039.oldguns.api.crafting.IDesignNotes;
import com.zach2039.oldguns.compat.jei.OldGunsRecipeCategory;
import com.zach2039.oldguns.compat.jei.util.JEIIngredientStackListBuilder;
import com.zach2039.oldguns.compat.jei.util.OldGunsCraftingGridHelper;
import com.zach2039.oldguns.init.ModBlocks;
import com.zach2039.oldguns.init.ModItems;
import com.zach2039.oldguns.world.inventory.menu.GunsmithsBenchMenu;
import com.zach2039.oldguns.world.item.crafting.GunsmithsBenchRecipe;
import com.zach2039.oldguns.world.item.crafting.recipe.ShapedGunsmithsBenchRecipe;
import com.zach2039.oldguns.world.item.crafting.recipe.ShapelessGunsmithsBenchRecipe;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class GunsmithsBenchRecipeCategory extends OldGunsRecipeCategory<GunsmithsBenchRecipe> {

	public static final ResourceLocation UID = new ResourceLocation(OldGuns.MODID, "gunsmiths_bench");
	
	public GunsmithsBenchRecipeCategory(IGuiHelper guiHelper) {
		super(GunsmithsBenchRecipe.class, guiHelper, UID, "block.oldguns.gunsmiths_bench");
		ResourceLocation background = new ResourceLocation(OldGuns.MODID, "textures/gui/container/gunsmiths_bench.png");
		setBackground(guiHelper.createDrawable(background, 5, 13, 142, 60));
		setIcon(new ItemStack(ModBlocks.GUNSMITHS_BENCH.get()));
	}

	@Override
	public void setIngredients(GunsmithsBenchRecipe recipe, IIngredients ingredients) {
		List<List<ItemStack>> inputLists = JEIIngredientStackListBuilder.make(recipe.getIngredients()).build();
			
		ingredients.setInputLists(VanillaTypes.ITEM, inputLists);
		
		ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, GunsmithsBenchRecipe recipe, IIngredients ingredients) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		OldGunsCraftingGridHelper gridHelper = new OldGunsCraftingGridHelper(1);
		
		/* Initialize output slot. */
		guiItemStacks.init(0, false, 118, 21);

		for (int y = 0; y < 3; ++y) {
			for (int x = 0; x < 3; ++x) {
				int index = 1 + x + (y * 3);
				guiItemStacks.init(index, true, 24 + (x * 18), 3 + (y * 18));
			}
		}
		
		// Init design notes slot
		guiItemStacks.init(GunsmithsBenchMenu.NOTES_SLOT+1, true, 2, 21);
		
		List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
		List<List<ItemStack>> outputs = ingredients.getOutputs(VanillaTypes.ITEM);
		List<ItemStack> designNotes = getNotesForRecipe(outputs.get(0));
		
		if (recipe instanceof ShapedGunsmithsBenchRecipe) {
			int width = ((ShapedGunsmithsBenchRecipe)recipe).getWidth();
			int height = ((ShapedGunsmithsBenchRecipe)recipe).getHeight();
			gridHelper.setInputs(guiItemStacks, inputs, width, height);
			guiItemStacks.set(GunsmithsBenchMenu.NOTES_SLOT+1, designNotes);
		} else if (recipe instanceof ShapelessGunsmithsBenchRecipe) {
			gridHelper.setInputs(guiItemStacks, inputs);
			guiItemStacks.set(GunsmithsBenchMenu.NOTES_SLOT+1, designNotes);
			recipeLayout.setShapeless();
		}
		
		guiItemStacks.set(0, outputs.get(0));
	}
	
	private List<ItemStack> getNotesForRecipe(List<ItemStack> outputs) {
		List<ItemStack> designNotesStacks = new ArrayList<ItemStack>();
		
		outputs.forEach((stack) -> {
			boolean hasDesign = IDesignNotes.hasDesignNotes(stack.getItem());
			if (hasDesign) {
				ItemStack designItem = IDesignNotes.setDesignTagOnItem(new ItemStack(ModItems.DESIGN_NOTES.get()), stack.getItem());
				designNotesStacks.add(designItem);
			}
		});
		
		return designNotesStacks;
	}
}