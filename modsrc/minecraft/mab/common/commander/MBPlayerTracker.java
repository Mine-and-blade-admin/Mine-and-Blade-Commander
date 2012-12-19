package mab.common.commander;

import java.util.Arrays;

import mab.common.commander.utils.CommonHelper;
import mab.common.commander.utils.TeamMap;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.StringTranslate;
import cpw.mods.fml.common.IPlayerTracker;

public class MBPlayerTracker implements IPlayerTracker {

	@Override
	public void onPlayerLogin(EntityPlayer player) {
		if(!player.worldObj.isRemote){
			
			TeamMap teamMap = TeamMap.getInstance();
			
			if(teamMap.isGameActive() && teamMap.getTeams().length > 0){
				System.out.println("Game Active");
				EnumTeam playerTeam = teamMap.getTeamForPlayer(player);
				if(playerTeam == null){
					
					playerTeam = teamMap.getTeamWithMinPlayers();
					
					teamMap.addPlayerToTeam(player, playerTeam);
					
					System.out.println(player.username+" entered world, assigning "+playerTeam.name());
					player.sendChatToPlayer("mb.gameJoin-"+playerTeam.name());
					
					
				}
				
			}
		}
	}

	@Override
	public void onPlayerLogout(EntityPlayer player) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPlayerChangedDimension(EntityPlayer player) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPlayerRespawn(EntityPlayer player) {
		// TODO Auto-generated method stub

	}

}
