package mab.common.commander;

import net.minecraftforge.common.Property;

public class ConfigHelper {
	
	//Categories
	public static final String CAT_UNITS = "Unit_Options";
	public static final String CAT_UNITS_IDS = "Unit_IDs";
	public static final String CAT_GAMEPLAY = "GAMEPLAY";
	
	//Blocks
	public static final String BLOCK_BANNER = "Banner";
	
	//Items
	public static final String ITEM_TRUMPET = "Trumpet";
	
	//Unit Options
	public static final String UNIT_ATTACK_TEAM = "Attack_Other_Teams";
	public static final String UNIT_RANDOM = "Random_Units";
	//public static final String UNIT_MELEE_ATTACK_EXP = "Meele_Units_Attack_Exploding";
	//public static final String UNIT_RANGE_ATTACK_EXP = "Ranged_Units_Attack_Exploding";
	public static final String PATHFIND_SEARCH = "Pathfind_Search";
	public static final String TEAM_GAME = "Team_Game";
	
	
	
	private static int pathfindSearch = -1;
	//public static boolean teamGame;
	
	public static int getPathFindSearch(){
		if(pathfindSearch < 0)
			pathfindSearch = MBCommander.config.get(CAT_UNITS, PATHFIND_SEARCH, 35).getInt(35);
		
		return pathfindSearch;
	}

	
}
