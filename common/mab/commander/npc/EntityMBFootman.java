package mab.commander.npc;

import mab.commander.EnumTeam;
import net.minecraft.src.World;

public class EntityMBFootman extends EntityMBUnit{
	
	public static final int FOOTMAN = 0;
	public static final int SPEARMAN = 1;
	public static final int MERC = 2;
	
	
	public EntityMBFootman(World par1World) {
		this(par1World, EnumTeam.values()[par1World.rand.nextInt(16)], FOOTMAN);
	}
	
	public EntityMBFootman(World par1World, EnumTeam team, int type) {
		super(par1World, team);
		dataWatcher.updateObject(17, (byte)type);
	}	

	@Override
	public int getAttackStrength() {
		return 10;
	}
	
	@Override
	public byte getOptionMax(int option) {
		if(option == 3)
			switch(getSubType()){
			case FOOTMAN:
			case MERC:
				return 3;
			case SPEARMAN:
				return 2;
			default:
				return 1;
			}
		else
			return super.getOptionMax(option);
	}

	@Override
	public EnumUnitItems getWeaponOption() {
		switch(getSubType()){
		case FOOTMAN:
		case MERC:
			switch(dataWatcher.getWatchableObjectByte(21)){
			case 0:
				return EnumUnitItems.IronSword;
			case 1:
				return EnumUnitItems.IronBattleaxe;
			case 2:
				return EnumUnitItems.IronMace;
			default:
					return null;
			}
		case SPEARMAN:
			switch(dataWatcher.getWatchableObjectByte(21)){
			case 0:
				return EnumUnitItems.IronSpear;
			case 1:
				return EnumUnitItems.IronHalberard;
			}
		}
		
		return null;
	}

	@Override
	public EnumUnitItems getWeaponOffHandOption() {
		switch(getSubType()){
		case FOOTMAN:
		case SPEARMAN:
			switch(dataWatcher.getWatchableObjectByte(22)){
				default:
					return null;
			}
		case MERC:
			switch(dataWatcher.getWatchableObjectByte(22)){
			case 0:
				return EnumUnitItems.IronSword;
			case 1:
				return EnumUnitItems.IronBattleaxe;
			case 2:
				return EnumUnitItems.IronMace;
			default:
					return null;
			}
			
		default:
			return null;
		}

	}

}
