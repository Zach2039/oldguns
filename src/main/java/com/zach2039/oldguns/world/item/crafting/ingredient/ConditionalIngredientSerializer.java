package com.zach2039.oldguns.world.item.crafting.ingredient;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.common.crafting.conditions.ICondition;

/**
 * An ingredient serializer that produces another {@link Ingredient} type, but only if the
 * specified conditions are met. If they aren't, it produces {@link IngredientNever#INSTANCE} instead.
 * <p>
 * JSON Properties:
 * <ul>
 * <li><code>conditions</code> - An array of condition objects, using the same format and condition types as the
 * <code>conditions</code> property of the top-level recipe object</li>
 * <li><code>ingredient</code> - An ingredient object or an array of ingredient objects, using the same format and
 * ingredient types as recipes</li>
 * </ul>
 * <p>
 * Test for this thread:
 * https://www.minecraftforge.net/forum/topic/59744-112-how-to-disable-some-mod-recipe-files-via-config-file/
 *
 * @author Choonster
 */
public class ConditionalIngredientSerializer implements IIngredientSerializer<Ingredient> {
	
	private static ICondition.IContext context = ICondition.IContext.EMPTY;
	
	@Override
	public Ingredient parse(final JsonObject json) {
		if (CraftingHelper.processConditions(json, "conditions", context)) {
			return CraftingHelper.getIngredient(json.get("ingredient"), false);
		}

		return IngredientNever.INSTANCE;
	}

	@Override
	public Ingredient parse(final FriendlyByteBuf buffer) {
		throw new UnsupportedOperationException("Can't parse from PacketBuffer, use the Ingredient's own IIngredientSerializer instead");
	}

	@Override
	public void write(final FriendlyByteBuf buffer, final Ingredient ingredient) {
		throw new UnsupportedOperationException("Can't write to PacketBuffer, use the Ingredient's own IIngredientSerializer instead");
	}
}