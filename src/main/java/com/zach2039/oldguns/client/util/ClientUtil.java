package com.zach2039.oldguns.client.util;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.neoforged.fml.DistExecutor;

import javax.annotation.Nullable;

/**
 * Client-specific utility methods designed to be called from common code.
 *
 * @author Choonster
 */
public class ClientUtil {
	/**
	 * Gets the client player.
	 * <p>
	 * NOTE: Although this method can be safely referenced in common code, it will throw a classloading exception
	 * if called on the dedicated server. Due to this, callers must check the logical side/dist before calling this
	 * method.
	 *
	 * @return The client player
	 */
	@Nullable
	public static Player getClientPlayer() {
		return DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> ClientOnlyMethods::getClientPlayer);
	}
}