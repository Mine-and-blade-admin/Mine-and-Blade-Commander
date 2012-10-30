package mab.commander.npc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

import mab.commander.EnumTeam;
import mab.commander.MBCommander;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class EntityMBMilitia extends EntityMBUnit{
	
	private int weapon;
	
	private ItemStack mainWeapon;

	
	public EntityMBMilitia(World par1World) {
		this(par1World, EnumTeam.values()[par1World.rand.nextInt(16)], EnumUnits.Militia);
	}
	
	public EntityMBMilitia(World par1World, EnumTeam colour, EnumUnits unit) {
		super(par1World, colour, unit);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getTexture() {
		return MBCommander.IMAGE_FOLDER+"skins/units/milita/Militia-"+getTeam().ordinal()+".png";
	}
	

	@Override
	public int getAttackStrength() {
		return 5;
	}

	@Override
	public int getMaxHealth() {
		return 20;
	}

	@Override
	public EnumUnitItems getWeaponOption(){
		switch(dataWatcher.getWatchableObjectByte(21)){
		case 0:
			return EnumUnitItems.StoneSword;
		case 1:
			return EnumUnitItems.StoneAxe;
		case 2:
			return EnumUnitItems.WoodMace;
		case 3:
			return EnumUnitItems.StoneMace;
		case 4:
			return EnumUnitItems.WoodSpear;
		case 5:
			return EnumUnitItems.StoneSpear;
		case 6:
			return EnumUnitItems.PitchFork;
		}
		
		return null;
	}

	@Override
	public EnumUnitItems getWeaponOffHandOption() {
		return null;
	}

	@Override
	public int getHelmNumber() {
		return -1;
	}
	
	
}
