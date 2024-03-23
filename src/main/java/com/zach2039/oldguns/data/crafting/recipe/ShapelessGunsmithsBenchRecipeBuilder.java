package com.zach2039.oldguns.data.crafting.recipe;

import com.zach2039.oldguns.world.item.crafting.recipe.ShapelessGunsmithsBenchRecipe;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.fml.util.ObfuscationReflectionHelper;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * An extension of {@link ShapelessRecipeBuilder} that allows the recipe result to have NBT and a custom group name for
 * the recipe advancement.
 *
 * @author Choonster
 */
public class ShapelessGunsmithsBenchRecipeBuilder<
RECIPE extends ShapelessGunsmithsBenchRecipe,
BUILDER extends ShapelessGunsmithsBenchRecipeBuilder<RECIPE, BUILDER>
> extends ShapelessRecipeBuilder {
	private static final Method ENSURE_VALID = ObfuscationReflectionHelper.findMethod(ShapelessRecipeBuilder.class, /* ensureValid */ "m_126207_", ResourceLocation.class);
	private static final Field CATEGORY = ObfuscationReflectionHelper.findField(ShapelessRecipeBuilder.class, /* category */ "f_244182_");
	private static final Field GROUP = ObfuscationReflectionHelper.findField(ShapelessRecipeBuilder.class, /* group */ "f_126177_");
	private static final Field INGREDIENTS = ObfuscationReflectionHelper.findField(ShapelessRecipeBuilder.class, /* ingredients */ "f_126175_");
	private static final Field CRITERIA = ObfuscationReflectionHelper.findField(ShapelessRecipeBuilder.class, /* criteria */ "f_291209_");

	protected final ItemStack result;
	protected final ShapelessGunsmithsBenchRecipeFactory<? extends RECIPE> factory;
	protected final List<ResourceLocation> conditions;

	protected ShapelessGunsmithsBenchRecipeBuilder(final RecipeCategory recipeCategory, final ItemStack result, final ShapelessGunsmithsBenchRecipeFactory<? extends RECIPE> factory) {
		super(recipeCategory, result.getItem(), result.getCount());
		this.result = result;
		this.factory = factory;
		this.conditions = new ArrayList<ResourceLocation>();
	}

	protected ShapelessGunsmithsBenchRecipeBuilder(
			final RecipeCategory category,
			final ItemStack result,
			final ShapelessGunsmithsBenchRecipeSerializer<? extends RECIPE> serializer
	) {
		this(category, result, serializer.factory());
	}

	@SuppressWarnings("unchecked")
	@Override
	public BUILDER requires(final TagKey<Item> tagIn) {
		return (BUILDER) super.requires(tagIn);
	}

	@SuppressWarnings("unchecked")
	@Override
	public BUILDER requires(final ItemLike itemIn) {
		return (BUILDER) super.requires(itemIn);
	}

	@SuppressWarnings("unchecked")
	@Override
	public BUILDER requires(final ItemLike itemIn, final int quantity) {
		return (BUILDER) super.requires(itemIn, quantity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public BUILDER requires(final Ingredient ingredientIn) {
		return (BUILDER) super.requires(ingredientIn);
	}

	@SuppressWarnings("unchecked")
	@Override
	public BUILDER requires(final Ingredient ingredientIn, final int quantity) {
		return (BUILDER) super.requires(ingredientIn, quantity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public BUILDER unlockedBy(final String name, final Criterion<?> criterionIn) {
		return (BUILDER) super.unlockedBy(name, criterionIn);
	}

	@SuppressWarnings("unchecked")
	@Override
	public BUILDER group(@Nullable final String group) {
		return (BUILDER) super.group(group);
	}

	@SuppressWarnings("unchecked")
	public BUILDER condition(final ResourceLocation condition) {
		this.conditions.add(condition);
		return (BUILDER) this;
	}

	/**
	 * Saves this recipe to the {@link RecipeOutput}.
	 *
	 * @param output The recipe output
	 * @param id     The ID to use for the recipe
	 */
	@Override
	public void save(final RecipeOutput output, final ResourceLocation id) {
		try {
			// Perform the super class's validation
			ENSURE_VALID.invoke(this, id);

			// Perform our validation
			validate(id);

			final var advancement = output
					.advancement()
					.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
					.rewards(AdvancementRewards.Builder.recipe(id))
					.requirements(AdvancementRequirements.Strategy.OR);

			@SuppressWarnings("unchecked") final var criteria = (Map<String, Criterion<?>>) CRITERIA.get(this);
			criteria.forEach(advancement::addCriterion);

			var group = (String) GROUP.get(this);
			if (group == null) {
				group = "";
			}

			final var category = (RecipeCategory) CATEGORY.get(this);

			final var ingredients = getIngredients();

			final var recipe = factory.createRecipe(
					group,
					RecipeBuilder.determineBookCategory(category),
					result,
					ingredients
			);

			output.accept(id, recipe, advancement.build(id.withPrefix("recipes/" + category.getFolderName() + "/")));
		} catch (final IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException("Failed to save shapeless recipe " + id, e);
		}
	}

	@SuppressWarnings("unchecked")
	protected NonNullList<Ingredient> getIngredients() {
		try {
			return (NonNullList<Ingredient>) INGREDIENTS.get(this);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException("Failed to get shapeless recipe ingredients", e);
		}
	}

	protected void validate(final ResourceLocation id) {
	}
}
