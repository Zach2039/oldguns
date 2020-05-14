package zach2039.oldguns.common.init;

import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import zach2039.oldguns.common.network.MessageFirearmEffect;

public class ModMessages
{
	/* Starting message index. */
	private static int messageID = 1;
	
	public static void registerMessages(SimpleNetworkWrapper network)
	{
		network.registerMessage(MessageFirearmEffect.HandlerClient.class, MessageFirearmEffect.class, messageID++, Side.CLIENT);
	}
}
