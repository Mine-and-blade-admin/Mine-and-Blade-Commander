package mab.commander.npc;

public enum EnumUnits {

	Militia("unit.melee1");
	
	private EnumUnits(String name){
		this.name = name;
	}
	
	private String name;
	
	public String getName(){
		return name;
	}
}
