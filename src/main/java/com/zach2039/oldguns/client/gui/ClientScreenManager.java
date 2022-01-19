package com.zach2039.oldguns.client.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zach2039.oldguns.OldGuns;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

/**
 * Handles this mod's client-side GUI factories
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber(modid = OldGuns.MODID, value = Dist.CLIENT, bus = Bus.MOD)
public class ClientScreenManager {
	private static final Logger LOGGER = LogManager.getLogger();

	private static final Map<ResourceLocation, IScreenConstructor<?>> CONSTRUCTORS = new HashMap<>();

	public static void openScreen(final ResourceLocation id, final PacketBuffer additionalData) {
		getScreenConstructor(id).ifPresent(f -> f.createAndOpenScreen(id, additionalData, Minecraft.getInstance()));
	}

	public static <T extends Container> Optional<IScreenConstructor<?>> getScreenConstructor(final ResourceLocation id) {
		final IScreenConstructor<?> constructor = CONSTRUCTORS.get(id);

		if (constructor == null) {
			LOGGER.warn("Failed to create screen for id: {}", id);
			return Optional.empty();
		}

		return Optional.of(constructor);
	}

	public static <S extends Screen> void registerScreenConstructor(final ResourceLocation id, final IScreenConstructor<S> constructor) {
		final IScreenConstructor<?> oldConstructor = CONSTRUCTORS.put(id, constructor);

		if (oldConstructor != null) {
			throw new IllegalStateException("Duplicate registration for " + id);
		}
	}

	@FunctionalInterface
	public interface IScreenConstructor<S extends Screen> {
		default void createAndOpenScreen(final ResourceLocation id, final PacketBuffer additionalData, final Minecraft mc) {
			final S screen = create(id, additionalData);
			mc.setScreen(screen);
		}

		S create(ResourceLocation id, PacketBuffer additionalData);
	}
}
