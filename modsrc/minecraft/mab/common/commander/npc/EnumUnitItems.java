package mab.common.commander.npc;

import mab.common.commander.MBCommander;

public enum EnumUnitItems {
	WoodSword(0, 64),
	StoneSword(0, 65),
	IronSword(0, 66),
	GoldSword(0, 68),
	DiamondSword(0, 67),
	
	Bow0(0, 21),
	Bow1(0, 64),
	Bow2(0, 101),
	Bow3(0, 117),
	Arrow(0, 133),
	
	WoodAxe(0, 112),
	StoneAxe(0, 113),
	IronAxe(0, 114),
	GoldAxe(0, 116),
	DiamondAxe(0, 115),
	
	WoodBattleaxe(1, 0),
	StoneBattleaxe(1, 1),
	IronBattleaxe(1, 2),
	GoldBattleaxe(1, 4),
	DiamondBattleaxe(1, 3),
	
	WoodMace(1, 16),
	StoneMace(1, 17),
	IronMace(1, 18),
	GoldMace(1, 20),
	DiamonMace(1, 19),
	
	WoodSpear(2, 0, true),
	StoneSpear(2, 1, true),
	IronSpear(2, 2, true),
	GoldSpear(2, 4, true),
	DiamondSpear(2, 3, true),
	
	PitchFork(2, 5, true),
	
	IronHalberard(2, 6, true),
	
	IronGlaive(2, 7, true),
	
	WoodShield(1, 161, 165, 160, false),
	HideShield(1, 208-16-16, 179-3+2, 180-3, false),
	HideShiedPaint(1, 208-16, 179-3+2, 180-3),
	IronShieldPaint(1, 208, 179, 180),
	GoldShieldPaint(1, 208+32, 179+6, 180+6),
	DiamondShieldPaint(1, 208+16, 179+3, 180+3);
	
	private int image;
	private int index;
	private boolean bigSheet;
	
	private int back = -1;
	private int trim = -1;
	private boolean colour = false;
	
	EnumUnitItems(int image, int index){
		this(image, index, false);
	}
	
	EnumUnitItems(int image, int index, boolean bigSheet){
		this.image = image;
		this.index = index;
		this.bigSheet = bigSheet;
	}
	
	EnumUnitItems(int image, int front, int back, int trim){
		this(image, front, back, trim, true);
		this.back = back;
		this.trim = trim;
	}
	
	EnumUnitItems(int image, int front, int back, int trim, boolean colour){
		this(image, front, false);
		this.back = back;
		this.trim = trim;
		this.colour = colour;
	}
	
	public String getImageSheet(){
		switch(image){
		case 0:
			return "/gui/items.png";
		case 1:
			return MBCommander.ImageSheet;
		case 2:
			return MBCommander.IMAGE_FOLDER+"BigItemSheet.png";
			default:
				return null;
		}
	}

	public int getImage() {
		return image;
	}

	public int getIndex() {
		return index;
	}

	public boolean isBigSheet() {
		return bigSheet;
	}

	public boolean isOnBack() {
		return bigSheet ||
				this == Bow0 ||
				this == Bow1 ||
				this == Bow2 ||
				this == Bow3 ||
				isShield();
	}
	
	public boolean isShield(){
		return this == IronShieldPaint || this == GoldShieldPaint || this == DiamondShieldPaint 
				||this == HideShield || this == HideShiedPaint || this == WoodShield;
	}
	
	public boolean isJabAttack(){
		return this == WoodSpear || this == StoneSpear || this == IronSpear || this == DiamondSpear || 
				this == GoldSpear || this == IronHalberard || this == IronGlaive  || this == PitchFork;
	}
	
	public int getShieldForTeam(int team){
		if(this.isShield() && colour)
			return this.index+team;
		else
			return index;
	}

	public int getTrim() {
		return trim;
	}

	public int getBack() {
		return back;
	}	
}
