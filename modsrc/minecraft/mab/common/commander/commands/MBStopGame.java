package mab.common.commander.commands;

import java.util.Iterator;

import mab.common.commander.npc.EntityMBUnit;
import mab.common.commander.utils.CommonHelper;
import mab.common.commander.utils.TeamMap;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.CommandBase;
import net.minecraft.src.Entity;
import net.minecraft.src.ICommandSender;

public class MBStopGame extends CommandBase{

	@Override
	public String getCommandName() {
		return "mb-end";
	}

	@Override
	public void processCommand(ICommandSender var1, String[] var2) {
		TeamMap.resetInstance();
		
		for(int i = 0; i < MinecraftServer.getServer().worldServers.length; i++){
			Iterator<Entity> it = MinecraftServer.getServer().worldServers[i].loadedEntityList.iterator();
			while(it.hasNext()){
				Entity next = it.next();
				if(next instanceof EntityMBUnit){
					((EntityMBUnit)next).setAttackTarget(null);
				}
			}
		}
		
		CommonHelper.sendMessageToAll("mb.gameEnd");
	}
	
    public int getRequiredPermissionLevel(){
    	//only allow OPs to use command on servers
        return 3;
    }

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender par1iCommandSender) {
		if(MinecraftServer.getServer().isDedicatedServer())
			return super.canCommandSenderUseCommand(par1iCommandSender);
		else
			return true;
	}

}
