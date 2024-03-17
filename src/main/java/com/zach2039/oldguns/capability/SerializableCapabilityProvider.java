package com.zach2039.oldguns.capability;

import com.google.common.base.Preconditions;
import net.minecraft.core.Direction;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.common.util.INBTSerializable;

import javax.annotation.Nullable;

/**
 * A simple implementation of {@link ICapabilityProvider} and {@link INBTSerializable} that supports a single {@link Capability} handler instance.
 * <p>
 * The handler instance must implement {@link INBTSerializable}.
 *
 * @author Choonster
 */
public class SerializableCapabilityProvider<HANDLER> extends SimpleCapabilityProvider<HANDLER> implements INBTSerializable<Tag> {
	private final INBTSerializable<Tag> serializableInstance;

	/**
	 * Create a provider for the specified handler instance.
	 *
	 * @param capability The Capability instance to provide the handler for
	 * @param facing     The Direction to provide the handler for
	 * @param instance   The handler instance to provide
	 */
	@SuppressWarnings("unchecked")
	public SerializableCapabilityProvider(final Capability<HANDLER> capability, @Nullable final Direction facing, final HANDLER instance) {
		super(capability, facing, instance);

		Preconditions.checkArgument(instance instanceof INBTSerializable, "instance must implement INBTSerializable");

		serializableInstance = (INBTSerializable<Tag>) instance;
	}

	@Override
	public Tag serializeNBT() {
		return serializableInstance.serializeNBT();
	}

	@Override
	public void deserializeNBT(final Tag tag) {
		serializableInstance.deserializeNBT(tag);
	}

}