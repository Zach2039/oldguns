package com.zach2039.oldguns.world.level.storage.loot.predicates;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.zach2039.oldguns.config.OldGunsConfig;
import com.zach2039.oldguns.init.ModLootConditionTypes;

import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class LootSpawnAmmoLootCondition implements LootItemCondition {
	private static final LootSpawnAmmoLootCondition INSTANCE = new LootSpawnAmmoLootCondition();
	
	@Override
	public LootItemConditionType getType() {
		return ModLootConditionTypes.SPAWN_AMMO_LOOT;
	}
	
	@Override
	public boolean test(LootContext p_81930_) {
		return OldGunsConfig.COMMON.lootSettings.allowAmmoInLoot.get();
	}

	public static LootItemCondition.Builder builder() {
		return () -> INSTANCE;
	}

	public static class ConditionSerializer implements Serializer<LootSpawnAmmoLootCondition> {
		@Override
		public void serialize(final JsonObject object, final LootSpawnAmmoLootCondition instance, final JsonSerializationContext context) {}

		@Override	
		public LootSpawnAmmoLootCondition deserialize(final JsonObject object, final JsonDeserializationContext context) {
			return LootSpawnAmmoLootCondition.INSTANCE;
		}
	}
}