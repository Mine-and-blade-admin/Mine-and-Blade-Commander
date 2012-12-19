package mab.common.commander.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.CompressedStreamTools;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.NBTBase;
import net.minecraft.src.NBTTagByte;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.SaveHandler;
import net.minecraft.src.World;

import mab.common.commander.EnumTeam;
import mab.common.commander.utils.TeamPacketHandeler.PlayerIDTeam;
import mab.common.commander.utils.TeamPacketHandeler.TeamPacketType;

public class TeamMap {
	
	private HashMap<String, EnumTeam> teamMap = new HashMap<String, EnumTeam>();
	private EnumTeam[] teamList = new EnumTeam[0];
	private long id = -1;
	
	private static TeamMap INSTANCE;
	
	private static String fileName = "MB-Teams.dat";
	
	public static TeamMap getInstance(){
		if(INSTANCE==null)
			INSTANCE = new TeamMap();
		
		return INSTANCE;
	}
	
	public static void resetInstance(long id2, EnumTeam[] teams) {
		INSTANCE = new TeamMap();
		INSTANCE.id = id2;
		INSTANCE.teamList = teams;
		INSTANCE.teamMap = new HashMap<String, EnumTeam>();
	}
	
	public static void resetInstance() {
		resetInstance(-1, new EnumTeam[0]);		
	}
	
	public boolean isGameActive(){
		return id !=-1;
	}
	
	public long getID(){
		return id;
	}
	
	public EnumTeam getTeamForPlayer(EntityPlayer player){
		return teamMap.get(player.username);
	}
	
	public void setTeamForPlayer(EntityPlayer player, EnumTeam enumTeam) {
		teamMap.put(player.username, enumTeam);
	}

	public boolean isOnSameTeam(EntityPlayer entity, EnumTeam team) {
		
		EnumTeam playerTeam = teamMap.get(entity.username);
		if(playerTeam == null)
			return true;
		else
			return team.equals(playerTeam);
	}

	public PlayerIDTeam[] generateList(World world){
		ArrayList<PlayerIDTeam> playerTeamArray = new ArrayList<TeamPacketHandeler.PlayerIDTeam>();
		for (Map.Entry<String, EnumTeam> pairs: teamMap.entrySet()) {
			EntityPlayer player = world.getPlayerEntityByName(pairs.getKey());
			if(player!=null){
				playerTeamArray.add(new PlayerIDTeam(player, pairs.getValue()));
			}
		}
		return playerTeamArray.toArray(new PlayerIDTeam[0]);
	}

	public static void load(File worldSave) {
		INSTANCE = new TeamMap();
		File dataPath = new File(worldSave, fileName);
		if(dataPath.exists()){
			try{
				NBTTagCompound tags = CompressedStreamTools.readCompressed(new FileInputStream(dataPath));
				
				//Game ID
				INSTANCE.id = tags.getLong("GameID");
				
				//Team List
				NBTTagCompound teamTag = tags.getCompoundTag("Teams");
				ArrayList<EnumTeam> teamListTemp = new ArrayList<EnumTeam>();
				
				for(int i = 0; i < 16; i++){
					if(teamTag.hasKey(String.valueOf(i)) && teamTag.getBoolean(String.valueOf(i))){
						teamListTemp.add(EnumTeam.values()[i]);
					}
				}				
				INSTANCE.teamList = teamListTemp.toArray(new EnumTeam[0]);
				
				//TeamMap
				INSTANCE.teamMap = new HashMap<String, EnumTeam>();
				NBTTagCompound teamMapTag = tags.getCompoundTag("TeamMap");
				Iterator<NBTBase> it = teamMapTag.getTags().iterator();
				while(it.hasNext()){
					NBTBase nextTag = it.next();
					if(nextTag instanceof NBTTagByte){
						INSTANCE.teamMap.put(nextTag.getName(), EnumTeam.values()[(((NBTTagByte) nextTag).data)]);
					}
				}
				
				
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void save(File worldSave){
		File dataPath = new File(worldSave, fileName);
		if(dataPath.exists()){
			dataPath.delete();
		}
		try{
			CompressedStreamTools.writeCompressed(getInstance().createCompound(), new FileOutputStream(dataPath));			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	private NBTTagCompound createCompound() {
		NBTTagCompound compound = new NBTTagCompound();
		
		compound.setLong("GameID", id);
		NBTTagCompound teams = new NBTTagCompound();
		for(int i = 0; i < teamList.length; i++){
			teams.setBoolean(String.valueOf(teamList[i].ordinal()), true);
		}
		compound.setTag("Teams", teams);
		
		NBTTagCompound teamMapCompound = new NBTTagCompound();
		Iterator<Entry<String, EnumTeam>> it = teamMap.entrySet().iterator();
		while(it.hasNext()){
			Entry<String, EnumTeam> next = it.next();
			teamMapCompound.setByte(next.getKey(), (byte)next.getValue().ordinal());
		}
		compound.setTag("TeamMap", teamMapCompound);
		
		return compound;
	}

	public EnumTeam[] getTeams() {
		return teamList;
	}

	public HashMap<EnumTeam, Integer> countTreams() {
		HashMap<EnumTeam, Integer> counts = new HashMap<EnumTeam, Integer>();
		
		for (EnumTeam team : teamList) {
			counts.put(team, 0);
		}
		
		String[] usernames = MinecraftServer.getServer().getAllUsernames();
		for (String user : usernames) {
			EnumTeam team = teamMap.get(user);
			if(team!=null){
				counts.put(team, counts.get(team)+1);
			}
		}
		return counts;
	}
	
	public EnumTeam getTeamWithMinPlayers(){
		HashMap<EnumTeam, Integer> counts = countTreams();
		EnumTeam minTeam = null;
		int minCount = Integer.MAX_VALUE;
		
		Iterator<Entry<EnumTeam, Integer>> it = counts.entrySet().iterator();
		while(it.hasNext()){
			Entry<EnumTeam, Integer> next = it.next();
			if(next.getValue() < minCount){
				minCount = next.getValue();
				minTeam = next.getKey();
			}
		}
		return minTeam;
	}

	public void addPlayerToTeam(EntityPlayer player, EnumTeam playerTeam) {
		teamMap.put(player.username, playerTeam);
	}

}
