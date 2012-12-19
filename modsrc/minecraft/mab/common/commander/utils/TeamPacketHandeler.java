package mab.common.commander.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mab.common.commander.CommanderPacketHandeler;
import mab.common.commander.EnumTeam;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.World;

import com.jcraft.jogg.Packet;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class TeamPacketHandeler {
	
	public enum TeamPacketType{
		SendTeam,
		SelectTeamReply,
		UpdateListRequest,
		UpdateListReply
	}
	
	public static class PlayerIDTeam{
		int id;
		byte teamId;
		public PlayerIDTeam(EntityPlayer player, EnumTeam team){
			id = player.entityId;
			if(team == null)
				teamId = -1;
			else
				teamId = (byte)team.ordinal();
		}
		public Entity getPlayer(World world){
			return world.getEntityByID(id);
		}
		public EnumTeam getTeam(){
			if(teamId == -1)
				return null;
			else
				return EnumTeam.values()[teamId];
		}
	}
	
	public static void readAndProcessTeamPacket(Packet250CustomPayload packet, EntityPlayer entityPlayer){
		
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		try {
			
			TeamPacketType type = TeamPacketType.values()[inputStream.readByte()];
			
			switch(type){
			case SendTeam:
				if(entityPlayer.worldObj.isRemote){
					Entity e = entityPlayer.worldObj.getEntityByID(inputStream.readInt());
					if(e instanceof EntityPlayer){
						TeamMap.getInstance().setTeamForPlayer(((EntityPlayer)e), EnumTeam.values()[inputStream.readByte()]);
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	public static Packet250CustomPayload generateListPacket(World world) {
		
		Packet250CustomPayload packet = null;
		
		PlayerIDTeam[] list = TeamMap.getInstance().generateList(world);
		if(list.length > 0){
			
			ByteArrayOutputStream bos = new ByteArrayOutputStream(list.length * 5 + 1);
			DataOutputStream outputStream = new DataOutputStream(bos);
			
			try {
				outputStream.writeByte((byte)TeamPacketType.UpdateListReply.ordinal());
				for (PlayerIDTeam playerIDTeam : list) {
					outputStream.writeInt(playerIDTeam.id);
					outputStream.writeByte(playerIDTeam.teamId);
				}
				
				
				packet = new Packet250CustomPayload(CommanderPacketHandeler.teamPacket, bos.toByteArray());
				
				outputStream.close();
				bos.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		return packet;
	}

	
}
