package mab.common.commander.npc.melee;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.World;
import mab.common.commander.EnumTeam;
import mab.common.commander.MBCommander;
import mab.common.commander.npc.EntityMBUnit;
import mab.common.commander.npc.EnumUnitItems;
import mab.common.commander.npc.EnumUnits;

public class EntityMBFootman extends EntityMBUnit{
	
	
	public EntityMBFootman(World par1World) {
		this(par1World, EnumTeam.black, EnumUnits.ManAtArms);
	}
	
	public EntityMBFootman(World par1World, EnumTeam team) {
		this(par1World, team, EnumUnits.ManAtArms);
	}

	public EntityMBFootman(World par1World, EnumTeam team, EnumUnits type) {
		super(par1World, team, type);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public String getTexture() {
		return MBCommander.IMAGE_FOLDER+"skins/units/footman/Footman-"+getTeam().ordinal()+".png";
	}

	@Override
	public int getAttackStrength() {
		return 5;
	}
	
	@Override
	public int getTotalArmorValue() {
		return 7;
	}

	@Override
	public EnumUnitItems getWeaponOption() {
		switch(getUnitType()){
		case ManAtArms:
			switch(getOption(3)){
			case 0:
				return EnumUnitItems.IronSword;
			case 1:
				return EnumUnitItems.IronBattleaxe;
			case 2:
				return EnumUnitItems.IronMace;
			default:
					return null;
			}
		case Spearman:
			switch(getOption(3)){
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
		switch(getOption(4)){
		case 0:
			return EnumUnitItems.IronShield;
		case 1:
			return EnumUnitItems.DiamondShield;
		case 2:
			return EnumUnitItems.GoldShield;
			default:
				return null;
		}
	}

	@Override
	public int getHelmNumber() {
		return getOption(5)-1;
	}

	@Override
	protected byte getExperiencePerHit() {
		return 3;
	}

	@Override
	public byte getCost() {
		return 3;
	}

}
