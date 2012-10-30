package mab.commander.npc;

public enum EnumUnits {

	Militia("unit.melee1", new int[]{4,5,5,6,-1,-1}, new int[]{2,0,1,3,-1,-1}, EntityMBMilitia.class),
	
	KnightShield("unit.melee3A", new int[]{4,5,5,2,2,2}, new int[]{2,0,1,3,4,5}, EntityMBKnight.class),
	KnightDuel("unit.melee3B", new int[]{4,5,5,2,2,2}, new int[]{2,0,1,3,3,5}, EntityMBKnight.class),
	KnightSpear("unit.melee3C", new int[]{4,5,5,1,2,2}, new int[]{2,0,1,3,4,5}, EntityMBKnight.class);
	
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
		"gui.options.hair",
		"gui.options.eyes",
		"gui.options.skin",
		"gui.options.weapons",
		"gui.options.shields",
		"gui.options.helmet"
	};
	
	
}
