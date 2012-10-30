package mab.commander;

import java.nio.ByteBuffer;
import java.util.Arrays;

import mab.commander.block.TileEntityBanner;
import mab.commander.npc.EntityMBMilitia;
import mab.commander.npc.EntityMBUnit;
import mab.commander.npc.EnumUnits;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.INetworkManager;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class CommanderPacketHandeler implements IPacketHandler {
	
	public static String BannerPacket = "MBc|Banner";
	public static String BannerPacketDespawn = "MBc|BannerD";
	public static String spawnPacket = "MBc|Spawn";


	@Override
	public void onPacketData(INetworkManager manager,
			Packet250CustomPayload packet, Player player) {
		
		EntityPlayer entityPlayer = ((EntityPlayer)player);
	
		if(packet.channel.equals(BannerPacket)){
			int x = readIntFromByteArray(packet.data, 0);
			int y = readIntFromByteArray(packet.data, 4);
			int z = readIntFromByteArray(packet.data, 8);
			
			entityPlayer.worldObj.setBlockTileEntity(x, y, z, 
					new TileEntityBanner(packet.data[12], EnumTeam.values()[packet.data[13]]));
			
		}else if (packet.channel.equals(spawnPacket)){
			
			System.out.println("spawning soldier");
			entityPlayer.worldObj.spawnEntityInWorld(readUnitFromPacket(packet, entityPlayer.worldObj));
			
			Packet250CustomPayload packet2 = new Packet250CustomPayload(BannerPacketDespawn, packet.data);
			
			PacketDispatcher.sendPacketToAllPlayers(packet2);
			
		}else if (packet.channel.equals(BannerPacketDespawn)){
			int x = readIntFromByteArray(packet.data, 4);
			int y = readIntFromByteArray(packet.data, 8);
			int z = readIntFromByteArray(packet.data, 12);
			
			entityPlayer.worldObj.setBlockTileEntity(x, y, z, null);
			entityPlayer.worldObj.setBlockTileEntity(x, y+1, z, null);
			
			entityPlayer.worldObj.setBlockWithNotify(x, y, z, 0);
			entityPlayer.worldObj.setBlockWithNotify(x, y+1, z, 0);
		}
		
	}
	
	
	
public static Packet250CustomPayload generateSpawnPacket(EntityMBUnit unit, int x, int y, int z){
		
		byte[]  data = new byte[26];
		
		//0-3 entityID
		writeIntToByteArray(data, unit.entityId, 0);
		//4-7 posx
		writeIntToByteArray(data, x, 4);
		//8-11 posy
		writeIntToByteArray(data, y, 8);
		//12-15 posz
		writeIntToByteArray(data, z, 12);
		//16 - yaw
		data[16] = (byte)((int)(unit.rotationYaw * 256.0F / 360.0F));
		//17 - pitch
		data[17] = (byte)((int)(unit.rotationPitch * 256.0F / 360.0F));
		//18 - type
		data[18] = (byte)unit.getUnitType().ordinal();
		//19 - colour
		data[19] = (byte)unit.getTeam().ordinal();
		//20 - 26 options
		for(int i = 0; i < 6; i++){
			data[20+i] = unit.getOption(i);
		}		
		Packet250CustomPayload packet = new Packet250CustomPayload(spawnPacket, data);
		return packet;
	}
	
	public static EntityMBUnit readUnitFromPacket(Packet250CustomPayload p, World world){
		if(p.channel.equals(spawnPacket)){
			
			EntityMBUnit unit = EntityMBUnit.generateUnit(world, EnumTeam.values()[p.data[19]], EnumUnits.values()[p.data[18]]);
			
			
			int x = readIntFromByteArray(p.data, 4);
			int y = readIntFromByteArray(p.data, 8);
			int z = readIntFromByteArray(p.data, 12);
			unit.setLocationAndAngles(
					(double)x+.5F,
					(double)y, 
					(double)z+.5F, 
					p.data[16], p.data[17]);
			
			world.setBlockTileEntity(x, y, z, null);
			world.setBlockTileEntity(x, y+1, z, null);
			
			world.setBlockWithNotify(x, y, z, 0);
			world.setBlockWithNotify(x, y+1, z, 0);
			
			for(int i = 0; i < 6; i++){
				unit.setOption(i, p.data[20+i]);
			}
			
			return unit;
		}else
			return null;
		
	}
	
	private static int readIntFromByteArray(byte[] bytes, int index){
		return ByteBuffer.allocate(4).put(bytes, index, 4).getInt(0);
	}
	
	private static void writeIntToByteArray(byte[] data, int value, int position){
		ByteBuffer b = ByteBuffer.allocate(4);
		System.arraycopy(b.putInt(value).array(), 0, data, position, 4);
	}
}
