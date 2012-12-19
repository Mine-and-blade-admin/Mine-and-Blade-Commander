package mab.common.commander.utils;

import java.awt.Point;
import java.util.Iterator;
import java.util.List;

import mab.common.commander.EnumTeam;
import mab.common.commander.npc.EntityMBUnit;
import mab.common.commander.npc.ai.EnumOrder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.World;

import com.google.common.base.Function;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.EntitySpawnPacket;

public class CommonHelper {
	
	public class MBSpawnFunction implements Function<EntitySpawnPacket, Entity>{

		@Override
		public Entity apply(EntitySpawnPacket input) {
			return null;
		}
		
	}
	
	public static void findAndSetGotoPos(EntityMBUnit[] units, int[] data) {
		int size = (int)Math.ceil(Math.sqrt(units.length));
		if(size % 2 == 0)
			size++;
		else
			size += 2;
		
		Spiral s = new Spiral(size, size);
		List<Point> points  = s.spiral();
		
		int max = data[1]+1;
		int min = data[1]-1;
		int unitIndex = 0;
		for (Point point : points) {
			int y = getWalkableBlockIndex(units[0].worldObj, data[0]+point.x, data[1], data[2]+point.y, max, min);
			if(y > -1){
				units[unitIndex].setOrder(EnumOrder.StandGuard, new int[]{data[0]+point.x, y, data[2]+point.y}, "");
				unitIndex++;
				if(unitIndex >= units.length)
					return;
			}
			
		}
		System.out.println("Mine & Blade: Could not find valid points for all units");
	}

	private static int getWalkableBlockIndex(World world, 
			int x, int y, int z, int max, int min) {
		
		if(isSolidBlock(world, x, y, z) && !isSolidBlock(world, x, y+1, z) && !isSolidBlock(world, x, y+2, z)){
			return y;
		}
		
		for(int i = 0; i < 2; i++){
			if(isSolidBlock(world, x, y+i, z) && !isSolidBlock(world, x, y+i+1, z) && !isSolidBlock(world, x, y+i+2, z)){
				return y+1;
			}else if(isSolidBlock(world, x, y-i, z) && !isSolidBlock(world, x, y-i+1, z) && !isSolidBlock(world, x, y-i+2, z)){
				return y-1;
			}
		}
		
		return -1;
		
	}
	
	public static boolean isSolidBlock(World world, int x, int y, int z){
		return world.blockExists(x, y, z) && 
				world.getBlockId(x, y, z)!=0 && 
				Block.blocksList[world.getBlockId(x, y, z)].getCollisionBoundingBoxFromPool(world, x, y, z) != null;
	}
	
	
	
	
	
	public static String codeTeams(EnumTeam[] teams){
		StringBuffer buffer = new StringBuffer();
		for(int i = 0; i < teams.length; i++){
			buffer.append(teams[i].ordinal());
			buffer.append(',');
		}
		
		buffer.deleteCharAt(buffer.lastIndexOf(","));
		return buffer.toString();
	}
	
	public static void sendMessageToAll(String string) {
		Iterator<EntityPlayer> it = MinecraftServer.getServer().getConfigurationManager().playerEntityList.iterator();
		while(it.hasNext()){
			it.next().sendChatToPlayer(string);
		}
	}

	public static boolean isTeamGame() {
		return TeamMap.getInstance().isGameActive();
	}

}
