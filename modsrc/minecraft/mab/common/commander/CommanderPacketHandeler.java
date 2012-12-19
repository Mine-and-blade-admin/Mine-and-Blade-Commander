package mab.common.commander;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import javax.jws.Oneway;
import javax.swing.JTable.PrintMode;

import mab.client.commander.utils.MBClientHelper;
import mab.common.commander.block.TileEntityBanner;
import mab.common.commander.npc.EntityMBUnit;
import mab.common.commander.npc.EnumUnits;
import mab.common.commander.npc.ai.EnumOrder;
import mab.common.commander.utils.CommonHelper;
import mab.common.commander.utils.TeamPacketHandeler;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.INetworkManager;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class CommanderPacketHandeler implements IPacketHandler {
	
	public static String BannerPacket = "MBc|Banner";
	public static String spawnPacket = "MBc|Spawn";
	public static String upgradePacket = "MBc|Upgrade";
	public static String orderPacket = "MBc|Order";
	public static String particlePacket = "MBc|Particle";
	public static String teamPacket = "MBc|Team";

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

			
		}else if (packet.channel.equals(upgradePacket)){
			readAndProcessUpgradePacket(packet, entityPlayer.worldObj, entityPlayer);
		}else if(packet.channel.equals(orderPacket)){
			
			DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
			try {
				
				int playerID = inputStream.readInt();
				EnumOrder order = EnumOrder.values()[inputStream.readByte()];
				byte size = inputStream.readByte();
				
				
				int[] data = new int[3];
				String stringData = "";
				
				EntityMBUnit[] units = new EntityMBUnit[size];
				for(int i = 0; i < size; i++){
					
					Entity e = entityPlayer.worldObj.getEntityByID(inputStream.readInt());
					
					if(e != null && e instanceof EntityMBUnit){
						units[i] = (EntityMBUnit)e;
					}else{
						System.out.println("Mine & Blade: Unexpected Entity ID in Order Packet");
					}
					
				}
				
				if(order == EnumOrder.GoTo){
					
					for(int i = 0; i < 3; i++)
						data[i] = inputStream.readInt();
					CommonHelper.findAndSetGotoPos(units, data);

					units[0].setOrder(EnumOrder.StandGuard, data, "");
					
				}else if(order == EnumOrder.TargetDist){
					for(int i = 0; i < size; i++){
						units[i].setTargetDistance(data[0]);
						units[i].setAttackTarget(null);
					}
				}else{
					for(int i = 0; i < size; i++){
						
						order.setOrderFromPacketData(units[i], entityPlayer);
					}
				}
				
				
			}catch(Exception e){
				e.printStackTrace();
			}			
		}else if (packet.channel.equals(particlePacket)){
			readAndProcessParticlerPacket(packet, entityPlayer, entityPlayer.worldObj);
		}else if(packet.channel.equals(teamPacket)){
			TeamPacketHandeler.readAndProcessTeamPacket(packet, entityPlayer);
		}
		
	}
	
	private void readAndProcessParticlerPacket(Packet250CustomPayload p,
			EntityPlayer entityPlayer, World world) {
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(p.data));
		try{
			
			byte type = inputStream.readByte();
			
			switch (type) {
			case 0: //spawn effect
				Entity e = world.getEntityByID(inputStream.readInt());
				if(e != null){
					 for (int var1 = 0; var1 < 20; ++var1)
			            {
			                double var8 = world.rand.nextGaussian() * 0.02D;
			                double var4 = world.rand.nextGaussian() * 0.02D;
			                double var6 = world.rand.nextGaussian() * 0.02D;
			                world.spawnParticle("explode", 
			                		e.posX + (double)(world.rand.nextFloat() * e.width * 2.0F) - 
			                		(double)e.width, e.posY + (double)(world.rand.nextFloat() * e.height),
			                		e.posZ + (double)(world.rand.nextFloat() * e.width * 2.0F) - (double)e.width, var8, var4, var6);
			            }
				}
				 
				break;
			default:
				break;
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	public static Packet250CustomPayload generateUpgradePacket(EntityMBUnit previousUnit, EntityMBUnit unit, EntityPlayer player){
		ByteArrayOutputStream bos = new ByteArrayOutputStream(15);
		DataOutputStream outputStream = new DataOutputStream(bos);
		try {
			
			outputStream.writeInt(previousUnit.entityId);
			outputStream.writeByte((byte)unit.getUnitType().ordinal());
			
			for(int i = 0; i < 6; i++){
				outputStream.writeByte(unit.getOption(i));
			}
			
			outputStream.writeInt(player.entityId);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	
		return new Packet250CustomPayload(upgradePacket, bos.toByteArray());
	}
	
	public static void readAndProcessUpgradePacket(Packet250CustomPayload p, World world, EntityPlayer player){
		if(p.channel.equals(upgradePacket)){

			DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(p.data));
			try{
				
				Entity e = world.getEntityByID(inputStream.readInt());
				if(e != null && e instanceof EntityMBUnit){
					EntityMBUnit previous = (EntityMBUnit)e;
					
					EntityMBUnit unit = EntityMBUnit.generateUnit(world,previous.getTeam(), EnumUnits.values()[inputStream.readByte()]);
					
					unit.setLocationAndAngles(
							previous.posX,
							previous.posY, 
							previous.posZ, 
							previous.rotationYaw,
							previous.rotationPitch);
					
					unit.setOrder(previous.getOrder(), previous.getOrderData(), previous.getOrderStringData());
					
					unit.setBaseMorale(previous.getBaseMorale());
					
					previous.setDead();
					
					PacketDispatcher.sendPacketToAllAround(unit.posX, unit.posY, unit.posZ, 30, unit.dimension, generateParticleEffectPacket((byte)0, previous));

					for(int i = 0; i < 6; i++){
						unit.setOption(i, inputStream.readByte());
					}
					
					
					e = world.getEntityByID(inputStream.readInt());
					if(e instanceof EntityPlayer && !((EntityPlayer)e).capabilities.isCreativeMode){
						InventoryPlayer inv = ((EntityPlayer)e).inventory;
						int required = unit.getCost();
						MBCommander.PROXY.scanForGold(inv,required);
					}
					
					unit.setOwner(previous.getOwner());
					
					world.spawnEntityInWorld(unit);
					
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	
	private static Packet250CustomPayload generateParticleEffectPacket(byte i, Entity centerEntity) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(5);
		DataOutputStream outputStream = new DataOutputStream(bos);
		try {
			outputStream.writeByte(i);
			outputStream.writeInt(centerEntity.entityId);
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return new Packet250CustomPayload(particlePacket, bos.toByteArray());
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
				unit.setOwner(player.username);
				
				return unit;
			}catch(Exception e){
				e.printStackTrace();
				return null;
			}
		}else
			return null;
		
	}
	
}
