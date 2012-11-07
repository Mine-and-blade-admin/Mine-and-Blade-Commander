package mab.commander.npc;

import mab.commander.npc.melee.EntityMBKnight;
import mab.commander.npc.melee.EntityMBMilitia;
import mab.commander.npc.melee.EntityMBFootman;

public enum EnumUnits {

	Militia("entity.MaB-Commander.Militia.name", new int[]{4,5,5,6,-1,-1}, new int[]{0,1,2,3,-1,-1}, EntityMBMilitia.class),
	ManAtArms("entity.MaB-Commander.ManAtArms.name", new int[]{4,5,2,6,2,3}, new int[]{0,1,2,3,4,5}, EntityMBFootman.class),
	Spearman("entity.MaB-Commander.Spearman.name", new int[]{4,5,5,1,2,3}, new int[]{0,1,2,3,4,5}, EntityMBFootman.class),
	
	
	KnightShield("unit.melee3A", new int[]{4,5,5,2,2,4}, new int[]{0,1,2,3,4,5}, EntityMBKnight.class),
	KnightDuel("unit.melee3B", new int[]{4,5,5,2,2,4}, new int[]{0,1,2,3,3,5}, EntityMBKnight.class),
	KnightSpear("unit.melee3C", new int[]{4,5,5,1,2,4}, new int[]{0,1,2,3,4,5}, EntityMBKnight.class);
	
	private EnumUnits(String name, int[] optionMax, int[] optionLabels, Class unitClass){
		this.name = name;
		this.optionMax = optionMax;
		this.optionIcons = optionLabels;
		this.unitClass = unitClass;
	}
	
	private String name;
	private int[] optionMax;
	private int[] optionIcons;
	private Class unitClass;
	
	public String getName(){
		return name;
	}
	
	public int[] getOptionMax() {
		return optionMax;
	}

	public int[] getOptionIcons() {
		return optionIcons;
	}
	
	public Class getUnitClass(){
		return unitClass;
	}
	
	public static final int OPTION_HAIR = 0;
	public static final int OPTION_EYES = 1;
	public static final int OPTION_SKIN = 2;
	public static final int OPTION_WEAPON = 3;
	public static final int OPTION_SHIELD = 4;
	public static final int OPTION_HELM = 5;
	
	public static String[] labels = new String []{
		"gui.options.skin",
		"gui.options.hair",
		"gui.options.eyes",
		"gui.options.weapons",
		"gui.options.shields",
		"gui.options.helmet"
	};
	
	
}
