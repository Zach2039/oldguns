package com.zach2039.oldguns.data.crafting.recipe;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.zach2039.oldguns.OldGuns;
import com.zach2039.oldguns.util.ModRegistryUtil;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

/**
 * An extension of {@link ShapedRecipeBuilder} that allows the recipe result to have NBT and a custom group name for
 * the recipe advancement.
 *
 * @author Choonster
 */
public class EnhancedShapedRecipeBuilder<
		RECIPE extends IRecipe<?>,
		BUILDER extends EnhancedShapedRecipeBuilder<RECIPE, BUILDER>
		> extends ShapedRecipeBuilder {
	private static final Method ENSURE_VALID = ObfuscationReflectionHelper.findMethod(ShapedRecipeBuilder.class, /* ensureValid */ "func_200463_a", ResourceLocation.class);
	private static final Field ADVANCEMENT = ObfuscationReflectionHelper.findField(ShapedRecipeBuilder.class, /* advancement */ "field_200479_f");
	private static final Field GROUP = ObfuscationReflectionHelper.findField(ShapedRecipeBuilder.class, /* group */ "field_200480_g");
	private static final Field ROWS = ObfuscationReflectionHelper.findField(ShapedRecipeBuilder.class, /* rows */ "field_200477_d");
	private static final Field KEY = ObfuscationReflectionHelper.findField(ShapedRecipeBuilder.class, /* key */ "field_200478_e");

	protected final ItemStack result;
	protected final IRecipeSerializer<? extends RECIPE> serializer;
	protected String itemGroup;
	protected final List<ResourceLocation> conditions;

	protected EnhancedShapedRecipeBuilder(final ItemStack result, final IRecipeSerializer<? extends RECIPE> serializer) {
		super(result.getItem(), result.getCount());
		this.result = result;
		this.serializer = serializer;
		this.conditions = new ArrayList<ResourceLocation>();
	}

	/**
	 * Sets the item group name to use for the recipe advancement. This allows the result to be an item without an
	 * item group, e.g. minecraft:spawner.
	 *
	 * @param group The group name
	 * @return This builder
	 */
	@SuppressWarnings("unchecked")
	public BUILDER itemGroup(final String group) {
		itemGroup = group;
		return (BUILDER) this;
	}

	/**
	 * Adds a key to the recipe pattern.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public BUILDER define(final Character symbol, final ITag<Item> tagIn) {
		return (BUILDER) super.define(symbol, tagIn);
	}

	/**
	 * Adds a key to the recipe pattern.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public BUILDER define(final Character symbol, final IItemProvider itemIn) {
		return (BUILDER) super.define(symbol, itemIn);
	}

	/**
	 * Adds a key to the recipe pattern.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public BUILDER define(final Character symbol, final Ingredient ingredientIn) {
		return (BUILDER) super.define(symbol, ingredientIn);
	}

	/**
	 * Adds a new entry to the patterns for this recipe.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public BUILDER pattern(final String pattern) {
		return (BUILDER) super.pattern(pattern);
	}

	/**
	 * Adds a criterion needed to unlock the recipe.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public BUILDER unlockedBy(final String name, final ICriterionInstance criterion) {
		return (BUILDER) super.unlockedBy(name, criterion);
	}

	@SuppressWarnings("unchecked")
	@Override
	public BUILDER group(final String group) {
		return (BUILDER) super.group(group);
	}
	
	@SuppressWarnings("unchecked")
	public BUILDER condition(final ResourceLocation condition) {
		this.conditions.add(condition);
		return (BUILDER) this;
	}

	/**
	 * Builds this recipe into a {@link FinishedRecipe}.
	 */
	@Override
	public void save(final Consumer<IFinishedRecipe> consumer) {
		save(consumer, ModRegistryUtil.getRequiredRegistryName(result.getItem()));
	}

	/**
	 * Builds this recipe into a {@link FinishedRecipe}. Use {@link #save(Consumer)} if save is the same as the ID for
	 * the result.
	 */
	@Override
	public void save(final Consumer<IFinishedRecipe> consumer, final String save) {
		final ResourceLocation registryName = result.getItem().getRegistryName();
		if (new ResourceLocation(save).equals(registryName)) {
			throw new IllegalStateException("Shaped Recipe " + save + " should remove its 'save' argument");
		} else {
			save(consumer, new ResourceLocation(save));
		}
	}

	/**
	 * Validates that the recipe result has NBT or a custom group has been specified.
	 *
	 * @param id The recipe ID
	 */
	protected void ensureValid(final ResourceLocation id) {
		if (itemGroup == null && result.getItem().getItemCategory() == null) {
			throw new IllegalStateException("Enhanced Shaped Recipe " + id + " has result " + result + " with no item group - use EnhancedShapedRecipeBuilder.itemGroup to specify one");
		}
	}

	/**
	 * Builds this recipe into a {@link FinishedRecipe}.
	 */
	@Override
	public void save(final Consumer<IFinishedRecipe> consumer, final ResourceLocation id) {
		try {
			// Perform the super class's validation
			ENSURE_VALID.invoke(this, id);

			// Perform our validation
			ensureValid(id);

			// We can't call the super method directly because it throws an exception when the result is an item that
			// doesn't belong to an item group (e.g. Mob Spawners).

			final Advancement.Builder advancementBuilder = ((Advancement.Builder) ADVANCEMENT.get(this))
					.parent(new ResourceLocation("minecraft", "recipes/root"))
					.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
					.rewards(AdvancementRewards.Builder.recipe(id))
					.requirements(IRequirementsStrategy.OR);

			String group = (String) GROUP.get(this);
			if (group == null) {
				group = "";
			}

			@SuppressWarnings("unchecked")
			final List<String> rows = (List<String>) ROWS.get(this);

			@SuppressWarnings("unchecked")
			final Map<Character, Ingredient> key = (Map<Character, Ingredient>) KEY.get(this);

			String itemGroupName = itemGroup;
			if (itemGroupName == null) {
				ItemGroup itemGroup = Preconditions.checkNotNull(result.getItem().getItemCategory());
				if (itemGroup == null) 
					itemGroup = OldGuns.ITEM_GROUP;
				itemGroupName = itemGroup.getRecipeFolderName();
			}

			final ResourceLocation advancementID = new ResourceLocation(id.getNamespace(), "recipes/" + itemGroupName + "/" + id.getPath());

			final ConditionedResult baseRecipe = new ConditionedResult(id, result.getItem(), result.getCount(), group, rows, key, advancementBuilder, advancementID, conditions);

			consumer.accept(new SimpleFinishedRecipe(baseRecipe, result, serializer));
		} catch (final IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException("Failed to build Enhanced Shaped Recipe " + id, e);
		}
	}

	public static class Vanilla extends EnhancedShapedRecipeBuilder<ShapedRecipe, Vanilla> {
		private Vanilla(final ItemStack result) {
			super(result, IRecipeSerializer.SHAPED_RECIPE);
		}

		/**
		 * Creates a new builder for a Vanilla shaped recipe with NBT and/or a custom item group.
		 *
		 * @param result The recipe result
		 * @return The builder
		 */
		public static Vanilla shapedRecipe(final ItemStack result) {
			return new Vanilla(result);
		}

		@Override
		protected void ensureValid(final ResourceLocation id) {
			super.ensureValid(id);

			if (!result.hasTag() && itemGroup == null) {
				throw new IllegalStateException("Vanilla Shaped Recipe " + id + " has no NBT and no custom item group - use ShapedRecipeBuilder instead");
			}
		}
	}
	
	 public static class ConditionedResult implements IFinishedRecipe {
	      private final ResourceLocation id;
	      private final Item result;
	      private final int count;
	      private final String group;
	      private final List<String> pattern;
	      private final Map<Character, Ingredient> key;
	      private final Advancement.Builder advancement;
	      private final ResourceLocation advancementId;
	      private final List<ResourceLocation> conditions;

	      public ConditionedResult(ResourceLocation p_176754_, Item p_176755_, int p_176756_, String p_176757_, List<String> p_176758_, Map<Character, Ingredient> p_176759_, Advancement.Builder p_176760_, ResourceLocation p_176761_, List<ResourceLocation> conditions) {
	         this.id = p_176754_;
	         this.result = p_176755_;
	         this.count = p_176756_;
	         this.group = p_176757_;
	         this.pattern = p_176758_;
	         this.key = p_176759_;
	         this.advancement = p_176760_;
	         this.advancementId = p_176761_;
	         this.conditions = conditions;
	      }

	    @SuppressWarnings("deprecation")
		public void serializeRecipeData(JsonObject p_126167_) {
	         if (!this.group.isEmpty()) {
	            p_126167_.addProperty("group", this.group);
	         }
	         
	         JsonArray condArray = new JsonArray();
	         
	         for(ResourceLocation l : this.conditions) {
	        	 JsonObject typeObj = new JsonObject();
	        	 typeObj.addProperty("type", l.toString());
	        	 condArray.add(typeObj); 
	         }
	         
	         p_126167_.add("conditions", condArray);
	         JsonArray jsonarray = new JsonArray();

	         for(String s : this.pattern) {
	            jsonarray.add(s);
	         }

	         p_126167_.add("pattern", jsonarray);
	         JsonObject jsonobject = new JsonObject();

	         for(Entry<Character, Ingredient> entry : this.key.entrySet()) {
	            jsonobject.add(String.valueOf(entry.getKey()), entry.getValue().toJson());
	         }

	         p_126167_.add("key", jsonobject);
	         JsonObject jsonobject1 = new JsonObject();
	         jsonobject1.addProperty("item", Registry.ITEM.getKey(this.result).toString());
	         if (this.count > 1) {
	            jsonobject1.addProperty("count", this.count);
	         }

	         p_126167_.add("result", jsonobject1);
	      }

	      public IRecipeSerializer<?> getType() {
	         return IRecipeSerializer.SHAPED_RECIPE;
	      }

	      public ResourceLocation getId() {
	         return this.id;
	      }

	      @Nullable
	      public JsonObject serializeAdvancement() {
	         return this.advancement.serializeToJson();
	      }

	      @Nullable
	      public ResourceLocation getAdvancementId() {
	         return this.advancementId;
	      }
	   }
}