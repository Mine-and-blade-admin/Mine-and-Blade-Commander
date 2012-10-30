package mab.commander.npc;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import mab.commander.EnumTeam;
import mab.commander.MBCommander;
import net.minecraft.src.World;

public class EntityMBKnight extends EntityMBUnit{
	
	
	public EntityMBKnight(World par1World) {
		this(par1World, EnumTeam.values()[par1World.rand.nextInt(16)], EnumUnits.KnightShield);
	}
	
	public EntityMBKnight(World par1World, EnumTeam team, EnumUnits type) {
		super(par1World, team, type);		
	}	

	@Override
	public int getAttackStrength() {
		return 10;
	}
	
	@Override
	public int getMaxHealth() {
		return 10;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public String getTexture() {
		return MBCommander.IMAGE_FOLDER+"skins/units/knight/Knight-"+getTeam().ordinal()+".png";
	}

	@Override
	public EnumUnitItems getWeaponOption() {
		switch(getUnitType()){
		case KnightShield:
		case KnightDuel:
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
		case KnightSpear:
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
		switch(getUnitType()){
		case KnightShield:
		case KnightSpear:
			switch(dataWatcher.getWatchableObjectByte(22)){
			case 0:
				return EnumUnitItems.IronShield;
			case 1:
				return EnumUnitItems.DiamondShield;
			case 2:
				return EnumUnitItems.GoldShield;
				default:
					return null;
			}
		case KnightDuel:
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

	@Override
	public int getHelmNumber() {
		return getOption(5)-1;
	}
	
	

}
