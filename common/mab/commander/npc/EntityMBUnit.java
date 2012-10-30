package mab.commander.npc;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

import mab.commander.EnumTeam;
import mab.commander.MBCommander;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;

public abstract class EntityMBUnit extends EntityLiving{
	
	public EnumUnits unitType;
	
	public boolean renderShadow = true;
	
	private int weaponDrawTimer;
	
	
	public EntityMBUnit(World par1World, EnumTeam team) {
		super(par1World);
		
		weaponDrawTimer = 20*30; //30 seconds
		dataWatcher.addObject(16, (byte)team.ordinal());
		dataWatcher.addObject(17, 0);
	}
	
	@Override
	protected void entityInit() {
		super.entityInit();
		
		//DataWatcher Positions
		//16 = team
		//17 = subtype
		//18-23 = cutomisation Options
		//24 = owner
		//25 = order
		//26-28 = order data
		
		for(int i = 0; i < 6; i++){
			dataWatcher.addObject(18+i, (byte)0);
		}
	}

	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
		
		if(getAITarget() == null){
			if(weaponDrawTimer > 0)
				weaponDrawTimer--;
		}else{
			weaponDrawTimer = 20*30;
		}
	}
	
	public boolean isWeaponsDrawn(){
		return weaponDrawTimer > 0;
	}
	
	

	@Override
	public int getMaxHealth() {
		// TODO Auto-generated method stub
		return 0;
	}

	/** The amount of damage the unit delivers */
	public abstract int getAttackStrength();
	
	
	public String getUnitName(){
		return unitType.getName();
	}
	

	@Override
	public void writeEntityToNBT(NBTTagCompound par1nbtTagCompound) {
		super.writeEntityToNBT(par1nbtTagCompound);

		par1nbtTagCompound.setByte("team", dataWatcher.getWatchableObjectByte(16));
		par1nbtTagCompound.setByte("unit", (byte)unitType.ordinal());
		for(int i = 0; i < 6; i++){
			par1nbtTagCompound.setByte("Option"+i, getOption(i));
		}
		
		
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound par1nbtTagCompound) {
		super.readEntityFromNBT(par1nbtTagCompound);
		
		dataWatcher.updateObject(16, par1nbtTagCompound.getByte("team"));
		unitType = EnumUnits.values()[par1nbtTagCompound.getByte("unit")];
		for(int i = 0; i < 6; i++){
			setOption(i, par1nbtTagCompound.getByte("Option"+i));
		}
	}
	
	public byte getOptionMax(int option){
		switch(option){
		case 0:
			return 4;
		case 1:
			return 5;
		case 2:
			return 5;
		default:
			return -1;
		}
	}
	
	public String getOptionLabel(int option){
		switch(option){
		case 0:
			return "gui.options.skin";
		case 1:
			return "gui.options.hair";
		case 2:
			return "gui.options.eyes";
		default:
			return "";
		}
	}
	
	public int getOptionIcon(int option){
		switch(option){
		case 0:
			return 2;
		case 1:
			return 0;
		case 2:
			return 1;
		default:
			return -1;
		}
	}
	
	public byte getOption(int option){
		return dataWatcher.getWatchableObjectByte(18+option);
	}
	
	public EnumTeam getTeam(){
		return EnumTeam.values()[dataWatcher.getWatchableObjectByte(16)];
	}
	
	public byte getSubType(){
		return dataWatcher.getWatchableObjectByte(17);
	}

	public void setOption(int option, byte value){
		dataWatcher.updateObject(option+18, value);
	}

	public String getSkinFile(){
		return MBCommander.IMAGE_FOLDER+"skins/Body-"+getOption(0)+".png";
	}
	
	public String getHairFile(){
		return MBCommander.IMAGE_FOLDER+"skins/Hair-"+getOption(1)+".png";
	}
	
	public String getEyesFile(){
		return MBCommander.IMAGE_FOLDER+"skins/Eyes-"+getOption(2)+".png";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getShadowSize() {
		if(renderShadow)
			return super.getShadowSize();
		else
			return 0F;
	}

	public abstract EnumUnitItems getWeaponOption();
	
	public abstract EnumUnitItems getWeaponOffHandOption();
	
	
}
