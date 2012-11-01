package mab.commander.npc;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

import mab.commander.EnumTeam;
import mab.commander.MBCommander;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;

public abstract class EntityMBUnit extends EntityLiving{
	
	public boolean renderShadow = true;
	
	private int weaponDrawTimer;
	
	
	public EntityMBUnit(World par1World, EnumTeam team, EnumUnits type) {
		super(par1World);
		
		weaponDrawTimer = 20*10; //30 seconds
		dataWatcher.addObject(16, (byte)team.ordinal());
		dataWatcher.addObject(17, (byte)type.ordinal());
		
		dataWatcher.addObject(18, (byte)getMaxHealth());
		dataWatcher.addObject(19, (byte)0);
		
		dataWatcher.addObject(27, (byte)0);
	}
	
	@Override
	protected void entityInit() {
		super.entityInit();
		
		//DataWatcher Positions
		//16 = team
		//17 = subtype
		//18 = health
		//19 = experience
		//20-25 = cutomisation Options
		//26 = owner
		//27 = weaponDraw
		//28 = order
		//29-31 = order data
		
		for(int i = 0; i < 6; i++){
			dataWatcher.addObject(20+i, (byte)0);
		}
	}
	
    
	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
	}
	
	/**
     * Returns true if the newer Entity AI code should be run
     */
    public boolean isAIEnabled()
    {
        return true;
    }
    
    /**
    * main AI tick function, replaces updateEntityActionState
    */
   protected void updateAITick()
   {
	   
	   if(getAITarget() == null && hurtTime == 0){
			if(weaponDrawTimer > 0)
				weaponDrawTimer--;
		}else{
			weaponDrawTimer = 20*10;
		}
	   
	   if(weaponDrawTimer == 0 && isWeaponsDrawn())
		   dataWatcher.updateObject(27, (byte)0);
	   else if (weaponDrawTimer > 0 && !isWeaponsDrawn())
		   dataWatcher.updateObject(27, (byte)1);
	   
       if(weaponDrawTimer == 0 && this.ticksExisted % 60 == 0){
    	   this.heal(1);
       }
       
       this.dataWatcher.updateObject(18, (byte)this.getHealth());
   }
	
	
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
	}

	public boolean isWeaponsDrawn(){
		return dataWatcher.getWatchableObjectByte(27) == 1;
	}
	
	

	@Override
	public int getMaxHealth() {
		// TODO Auto-generated method stub
		return 0;
	}

	/** The amount of damage the unit delivers */
	public abstract int getAttackStrength();
	
	
	public String getUnitName(){
		return EnumUnits.values()[dataWatcher.getWatchableObjectByte(17)].getName();
	}
	

	@Override
	public void writeEntityToNBT(NBTTagCompound par1nbtTagCompound) {
		super.writeEntityToNBT(par1nbtTagCompound);

		par1nbtTagCompound.setByte("team", dataWatcher.getWatchableObjectByte(16));
		par1nbtTagCompound.setByte("unit", dataWatcher.getWatchableObjectByte(17));
		for(int i = 0; i < 6; i++){
			par1nbtTagCompound.setByte("Option"+i, getOption(i));
		}
		
		
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound par1nbtTagCompound) {
		super.readEntityFromNBT(par1nbtTagCompound);
		
		dataWatcher.updateObject(16, par1nbtTagCompound.getByte("team"));
		dataWatcher.updateObject(17, par1nbtTagCompound.getByte("unit"));
		for(int i = 0; i < 6; i++){
			setOption(i, par1nbtTagCompound.getByte("Option"+i));
		}
	}
	
	public final byte getOptionMax(int option){
		return (byte)EnumUnits.values()[dataWatcher.getWatchableObjectByte(17)].getOptionMax()[option];
	}
	
	public final String getOptionLabel(int option){
		return EnumUnits.labels[option];
	}
	
	public final int getOptionIcon(int option){
		return EnumUnits.values()[dataWatcher.getWatchableObjectByte(17)].getOptionIcons()[option];
	}
	
	public byte getOption(int option){
		return dataWatcher.getWatchableObjectByte(20+option);
	}
	
	public byte getCurrentHealth(){
		return dataWatcher.getWatchableObjectByte(18);
	}
	
	public byte getCurrentExperience(){
		return dataWatcher.getWatchableObjectByte(19);
	}
	
	public EnumTeam getTeam(){
		return EnumTeam.values()[dataWatcher.getWatchableObjectByte(16)];
	}
	
	public EnumUnits getUnitType(){
		return EnumUnits.values()[dataWatcher.getWatchableObjectByte(17)];
	}

	public void setOption(int option, byte value){
		dataWatcher.updateObject(option+20, value);
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
	
	public abstract int getHelmNumber();
	
	
	public static EntityMBUnit generateUnit(World world, EnumTeam team, EnumUnits unit){
		try {
			return (EntityMBUnit) unit.getUnitClass().getConstructor(World.class, EnumTeam.class, EnumUnits.class)
				.newInstance(world, team, unit);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String getDisplayOrder(){
		return "Stand Guard";
	}
	
}
