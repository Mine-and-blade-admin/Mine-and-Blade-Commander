package mab.commander;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

import javax.jws.Oneway;

import mab.commander.block.TileEntityBanner;
import mab.commander.npc.EntityMBUnit;
import mab.commander.npc.EnumUnits;
import mab.commander.npc.ai.EnumOrder;
import mab.commander.npc.melee.EntityMBMilitia;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.INetworkManager;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
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
	public static String orderPacket = "MBc|Order";

	@Override
	public void onPacketData(INetworkManager manager,
			Packet250CustomPayload packet, Player player) {
		
		EntityPlayer entityPlayer = ((EntityPlayer)player);
	
		if(packet.channel.equals(BannerPacket)){
			
			DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
			try {
				entityPlayer.worldObj.setBlockTileEntity(
						inputStream.readInt(), 
						inputStream.readInt(), 
						inputStream.readInt(), 
							new TileEntityBanner(inputStream.readByte(),
									EnumTeam.values()[inputStream.readByte()]));
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
			
		}else if (packet.channel.equals(spawnPacket)){
			
			EntityMBUnit unit = readUnitFromPacket(packet, entityPlayer.worldObj, entityPlayer);
			entityPlayer.worldObj.spawnEntityInWorld(unit);

			//Packet250CustomPayload packet2 = new Packet250CustomPayload(BannerPacketDespawn, packet.data);
			
			//PacketDispatcher.sendPacketToAllPlayers(packet2);
			
		}else if (packet.channel.equals(BannerPacketDespawn)){
//			int x = readIntFromByteArray(packet.data, 4);
//			int y = readIntFromByteArray(packet.data, 8);
//			int z = readIntFromByteArray(packet.data, 12);
//			
//			entityPlayer.worldObj.setBlockTileEntity(x, y, z, null);
//			entityPlayer.worldObj.setBlockTileEntity(x, y+1, z, null);
//			
//			entityPlayer.worldObj.setBlockWithNotify(x, y, z, 0);
//			entityPlayer.worldObj.setBlockWithNotify(x, y+1, z, 0);
		}else if(packet.channel.equals(orderPacket)){
			
			
			
			DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
			try {
				
				int playerID = inputStream.readInt();
				EnumOrder order = EnumOrder.values()[inputStream.readByte()];
				byte size = inputStream.readByte();
				
				int[] data = new int[3];
				String stringData = "";
				
				for(int i = 0; i < size; i++){
					
					Entity e = entityPlayer.worldObj.getEntityByID(inputStream.readInt());
					
					if(e != null && e instanceof EntityMBUnit){
						order.setOrderFromPacketData((EntityMBUnit)e, entityPlayer);
					}else{
						System.out.println("Mine & Blade: Unexpected Entity ID in Order Packet");
					}
					
				}
				
				
			}catch(Exception e){
				e.printStackTrace();
			}			
		}
		
	}
	
	
	
	public static Packet250CustomPayload generateSpawnPacket(EntityMBUnit unit, int x, int y, int z, EntityPlayer player){
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream(31);
		DataOutputStream outputStream = new DataOutputStream(bos);
		try {
			outputStream.writeInt(unit.entityId);
			outputStream.writeInt(x);
			outputStream.writeInt(y);
			outputStream.writeInt(z);
			
			outputStream.writeByte((byte)((int)((player.rotationYaw * 256.0F) / 360.0F)));
			outputStream.writeByte((byte)((int)(unit.rotationPitch * 256.0F / 360.0F)));
			
			outputStream.writeByte((byte)unit.getTeam().ordinal());
			outputStream.writeByte((byte)unit.getUnitType().ordinal());
			
			for(int i = 0; i < 6; i++){
				outputStream.writeByte(unit.getOption(i));
			}
			
			outputStream.writeInt(player.entityId);
		
		}catch (Exception e) {
			e.printStackTrace();
		}
	
		return new Packet250CustomPayload(spawnPacket, bos.toByteArray());
	}
	
	public static EntityMBUnit readUnitFromPacket(Packet250CustomPayload p, World world, EntityPlayer player){
		if(p.channel.equals(spawnPacket)){

			DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(p.data));
			
			try{
				int id = inputStream.readInt();
				int x = inputStream.readInt();
				int y = inputStream.readInt();
				int z = inputStream.readInt();
				byte pitch = inputStream.readByte();
				byte yaw = inputStream.readByte();
				
				
				
				
				world.setBlockTileEntity(x, y, z, null);
				world.setBlockTileEntity(x, y+1, z, null);
				
				world.setBlockWithNotify(x, y, z, 0);
				world.setBlockWithNotify(x, y+1, z, 0);
				
				EntityMBUnit unit = EntityMBUnit.generateUnit(world, EnumTeam.values()[inputStream.readByte()], EnumUnits.values()[inputStream.readByte()]);
				
				unit.setLocationAndAngles(
						(double)x+.5F,
						(double)y, 
						(double)z+.5F, 
						player.rotationYaw,
						player.rotationPitch);
				
				unit.setOrder(EnumOrder.StandGuard, new int[]{x,y,z}, "");
				
				for(int i = 0; i < 6; i++){
					unit.setOption(i, inputStream.readByte());
				}
				
				Entity e = world.getEntityByID(inputStream.readInt());
				if(e instanceof EntityPlayer && !((EntityPlayer)e).capabilities.isCreativeMode){
					InventoryPlayer inv = ((EntityPlayer)e).inventory;
					int required = unit.getCost();
					MBCommander.PROXY.scanForGold(inv,required);
				}
				
				
				return unit;
			}catch(Exception e){
				e.printStackTrace();
				return null;
			}
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
