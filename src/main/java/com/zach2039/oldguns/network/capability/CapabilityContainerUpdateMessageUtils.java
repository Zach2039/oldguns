package com.zach2039.oldguns.network.capability;

import javax.annotation.Nullable;

import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

/**
 * Utility methods and common interfaces for {@link UpdateContainerCapabilityMessage} .
 *
 * @author Choonster
 */
public class CapabilityContainerUpdateMessageUtils {
	/**
	 * Applies the data instance to the capability handler instance in the specified {@link AbstractContainerMenu} slot.
	 *
	 * @param menu                  The menu
	 * @param stateID               The state ID from the menu
	 * @param slotNumber            The slot number to apply the data to
	 * @param capability            The capability to apply the data for
	 * @param facing                The facing to apply the data for
	 * @param data                  The data instance to apply
	 * @param capabilityDataApplier A function that applies the capability data from a data instance to a capability handler instance
	 * @param <HANDLER>             The capability handler type
	 * @param <DATA>                The data type written to and read from the buffer
	 */
	static <HANDLER, DATA> void applyCapabilityDataToContainerSlot(
			final Container menu,
			final int slotNumber,
			final Capability<HANDLER> capability,
			@Nullable final Direction facing,
			final DATA data,
			final CapabilityDataApplier<HANDLER, DATA> capabilityDataApplier
			) {
		final ItemStack originalStack = menu.getSlot(slotNumber).getItem();

		originalStack.getCapability(capability, facing).ifPresent(originalHandler -> {
			final ItemStack newStack = originalStack.copy();

			newStack.getCapability(capability, facing).ifPresent(newHandler -> {
				capabilityDataApplier.apply(newHandler, data);

				if (!originalHandler.equals(newHandler)) {
					menu.setItem(slotNumber,  newStack);
				}
			});
		});
	}

	/**
	 * Converts a capability handler instance to a data instance.
	 *
	 * @param <HANDLER> The capability handler type
	 * @param <DATA>    The data type written to and read from the buffer
	 */
	@FunctionalInterface
	public interface CapabilityDataConverter<HANDLER, DATA> {
		@Nullable
		DATA convert(HANDLER handler);
	}

	/**
	 * A function that decodes a data instance from the buffer
	 *
	 * @param <DATA> The data type written to and read from the buffer
	 */
	@FunctionalInterface
	public interface CapabilityDataDecoder<DATA> {
		DATA decode(PacketBuffer buffer);
	}

	/**
	 * A function that encodes a data instance to the buffer
	 *
	 * @param <DATA> The data type written to and read from the buffer
	 */
	@FunctionalInterface
	public interface CapabilityDataEncoder<DATA> {
		void encode(DATA data, PacketBuffer buffer);
	}

	/**
	 * A function that applies the capability data from a data instance to a capability handler instance.
	 *
	 * @param <HANDLER> The capability handler type
	 * @param <DATA>    The data type written to and read from the buffer
	 */
	@FunctionalInterface
	public interface CapabilityDataApplier<HANDLER, DATA> {
		void apply(HANDLER handler, DATA data);
	}
}

