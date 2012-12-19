package mab.common.commander.utils;

import mab.common.commander.EnumTeam;
import net.minecraft.src.NetHandler;
import net.minecraft.src.Packet3Chat;
import net.minecraft.src.StringTranslate;
import cpw.mods.fml.common.network.IChatListener;

public class MBChatListener implements IChatListener{

	@Override
	public Packet3Chat serverChat(NetHandler handler, Packet3Chat message) {
		return message;
	}

	@Override
	public Packet3Chat clientChat(NetHandler handler, Packet3Chat message) {
		if(message.message.startsWith("mb.")){
			
			if(message.message.equals("mb.gameEnd") || message.message.startsWith("mb.gameRestart"))
				TeamMap.resetInstance(-1, new EnumTeam[0]);
			
			if(message.message.contains("-")){
				String[] split = message.message.split("-");
				message.message = StringTranslate.getInstance().translateKey(split[0]);
				message.message = message.message.replace("@team@", StringTranslate.getInstance().translateKey(split[1]));
			}else{
				message.message = StringTranslate.getInstance().translateKey(message.message);
			}
		}
		return message;
	}

}
