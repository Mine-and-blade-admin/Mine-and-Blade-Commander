package mab.common.commander.npc;


public enum EnumUnits {

	Militia("militia", new int[]{4,5,5,6,1,-1}, new int[]{0,1,2,3,5,-1}, EntityMBMeleeUnit.class, 1),
	
	ManAtArms("manatarms", new int[]{4,5,5,2,2,1}, new int[]{0,1,2,3,4,5}, EntityMBMeleeUnit.class, 2),
	Spearman("spearman", new int[]{4,5,5,2,2,1}, new int[]{0,1,2,3,4,5}, EntityMBMeleeUnit.class, 2),
	
	KnightShield("knight", new int[]{4,5,5,2,2,4}, new int[]{0,1,2,3,4,5}, EntityMBMeleeUnit.class, 3),
	KnightDuel("sellsword", new int[]{4,5,5,2,2,4}, new int[]{0,1,2,3,3,5}, EntityMBMeleeUnit.class, 3),
	KnightSpear("pikeman", new int[]{4,5,5,1,2,4}, new int[]{0,1,2,3,4,5}, EntityMBMeleeUnit.class, 3),
	
	LevyHunter("hunter", new int[]{4,5,5,2,2,2}, new int[]{0,1,2,3,6,5}, null, 1);
	
	private EnumUnits(String name, int[] optionMax, int[] optionLabels, Class unitClass, int tier){
		this.name = name;
		this.optionMax = optionMax;
		this.optionIcons = optionLabels;
		this.unitClass = unitClass;
		this.tier = tier;
	}
	
	private String name;
	private int[] optionMax;
	private int[] optionIcons;
	private Class unitClass;
	private int tier;
	
	public String getFullName(){
		return "entity.MaB-Commander."+name+".name";
	}
	
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
	
	public int getTier(){
		return tier;
	}
	
	public static final int OPTION_HAIR = 0;
	public static final int OPTION_EYES = 1;
	public static final int OPTION_SKIN = 2;
	public static final int OPTION_WEAPON = 3;
	public static final int OPTION_SHIELD = 4;
	public static final int OPTION_HELM = 5;
	public static final int OPTION_QUIVER = 6;
	
	public static String[] labels = new String []{
		"gui.options.skin",
		"gui.options.hair",
		"gui.options.eyes",
		"gui.options.weapons",
		"gui.options.shields",
		"gui.options.helmet",
		"gui.options.quiver"
	};

	
	
	
}
