package mab.common.commander.npc;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.PlainDocument;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

import mab.common.commander.EnumTeam;
import mab.common.commander.MBCommander;
import mab.common.commander.npc.ai.EnumOrder;
import mab.common.commander.npc.ai.MBEntityAIAttackMelee;
import mab.common.commander.npc.ai.MBEntityAIFollow;
import mab.common.commander.npc.ai.MBEntityAIStandGuard;
import net.minecraft.src.DamageSource;
import net.minecraft.src.EnchantmentHelper;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityAIAttackOnCollide;
import net.minecraft.src.EntityAIFollowOwner;
import net.minecraft.src.EntityAIHurtByTarget;
import net.minecraft.src.EntityAILookIdle;
import net.minecraft.src.EntityAIMoveTwardsRestriction;
import net.minecraft.src.EntityAINearestAttackableTarget;
import net.minecraft.src.EntityAIOpenDoor;
import net.minecraft.src.EntityAIRestrictOpenDoor;
import net.minecraft.src.EntityAISwimming;
import net.minecraft.src.EntityAIWander;
import net.minecraft.src.EntityAIWatchClosest;
import net.minecraft.src.EntityCreature;
import net.minecraft.src.EntityDamageSourceIndirect;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityMob;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IMob;
import net.minecraft.src.Item;
import net.minecraft.src.ItemFood;
import net.minecraft.src.ItemPotion;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.PathNavigate;
import net.minecraft.src.Potion;
import net.minecraft.src.PotionEffect;
import net.minecraft.src.PotionHelper;
import net.minecraft.src.StringTranslate;
import net.minecraft.src.World;

public abstract class EntityMBUnit extends EntityCreature{
	
	public boolean renderShadow = true;
	
	private int weaponDrawTimer;
	
	private int healBonusTimer = 0;
	
	public static final int MAX_WEAPON_DRAW = 15*20; //20 seconds
	public static final int NORMAL_HEALRATE = 20*3; // every 3 seconds
	public static final int ADVANCED_HEAL_RATE = NORMAL_HEALRATE/2; // every 1.5 seconds
	
	
	private int playerDamage = 0;
	
	public EntityMBUnit(World par1World, EnumTeam team, EnumUnits type) {
		super(par1World);
		
		this.moveSpeed = .3F;
		this.setSize(0.6F, 1.8F);
		 
		weaponDrawTimer =MAX_WEAPON_DRAW;
		dataWatcher.addObject(16, (byte)team.ordinal());
		dataWatcher.addObject(17, (byte)type.ordinal());
		
		dataWatcher.addObject(18, (byte)getMaxHealth());
		dataWatcher.addObject(19, (byte)0);
		
		dataWatcher.addObject(27, (byte)1);
		dataWatcher.addObject(28, (byte)0);
		dataWatcher.addObject(29, -1);
		dataWatcher.addObject(30, -1);
		dataWatcher.addObject(31, -1);
		dataWatcher.addObject(15, "");
		
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new MBEntityAIAttackMelee(this, this.moveSpeed, true));
		
		
		
		this.tasks.addTask(4, new EntityAIRestrictOpenDoor(this));
        this.tasks.addTask(5, new EntityAIOpenDoor(this, true));
        this.tasks.addTask(6, new EntityAIMoveTwardsRestriction(this, .3F));
        
		
        this.tasks.addTask(7, new MBEntityAIFollow(this, this.moveSpeed, 6.0F, 3.5F));
        this.tasks.addTask(8, new MBEntityAIStandGuard(this, .0F));
		this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(9, new EntityAILookIdle(this));
        
        //this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, IMob.class, 16.0F, 0, true));
        //this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityMBUnit.class, 16.0F, 0, true));
       // detachHome();
       
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
	
	
	/**
     * Returns true if the newer Entity AI code should be run
     */
    public boolean isAIEnabled(){
        return true;
    }
    
    /**
    * main AI tick function, replaces updateEntityActionState
    */
   protected void updateAITick()
   {
	   super.updateAITick();
	   
	   if(getAttackTarget() == null && hurtTime == 0){
			if(weaponDrawTimer > 0)
				weaponDrawTimer--;
			else{
				if(playerDamage > 0)
					playerDamage --;
			}
		}else{
			weaponDrawTimer = MAX_WEAPON_DRAW;
		}
	   
	   
	   
	   if(weaponDrawTimer == 0 && isWeaponsDrawn())
		   dataWatcher.updateObject(27, (byte)0);
	   else if (weaponDrawTimer > 0 && !isWeaponsDrawn())
		   dataWatcher.updateObject(27, (byte)1);
	   
       if(weaponDrawTimer == 0)
    	   if(healBonusTimer > 0){
    		   if(this.ticksExisted % ADVANCED_HEAL_RATE == 0)
    			   this.heal(1);
    	   }else
    		   if(this.ticksExisted % NORMAL_HEALRATE == 0){
    			   this.heal(1);
       }
       
       if(healBonusTimer > 0)
    	   healBonusTimer--;
       
       
       this.dataWatcher.updateObject(18, (byte)this.getHealth());
       
       //If following, check if followee still exists every 5 seconds
       if(ticksExisted % (20*5) == 0){
	       if(getOrder() == EnumOrder.Follow){
	    	   if (worldObj.getPlayerEntityByName(dataWatcher.getWatchableObjectString(15)) == null){
	    		   setOrder(EnumOrder.StandGuard, new int[]{
	    				   (int)Math.floor(posX),
							(int)Math.floor(posY),
							(int)Math.floor(posZ)}, "");
	    	   }
	       }
       }
       
       if(getAttackTarget() != null && getAttackTarget().isDead)
    	   setAttackTarget(null);

   }
   
	@Override
	public void onLivingUpdate() {
		//this is to update the swing progress
		this.updateArmSwingProgress();
		super.onLivingUpdate();
	}


	public boolean isWeaponsDrawn(){
		return dataWatcher.getWatchableObjectByte(27) == 1;
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
		par1nbtTagCompound.setByte("exp", dataWatcher.getWatchableObjectByte(19));
		for(int i = 0; i < 6; i++){
			par1nbtTagCompound.setByte("Option"+i, getOption(i));
		}
		par1nbtTagCompound.setByte("order", dataWatcher.getWatchableObjectByte(28));
		for(int i = 0; i < 3; i++){
			par1nbtTagCompound.setInteger("order data"+i, dataWatcher.getWatchableObjectInt(29+i));
		}
		
		par1nbtTagCompound.setString("order data string", dataWatcher.getWatchableObjectString(15));
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound par1nbtTagCompound) {
		super.readEntityFromNBT(par1nbtTagCompound);
		
		dataWatcher.updateObject(16, par1nbtTagCompound.getByte("team"));
		dataWatcher.updateObject(17, par1nbtTagCompound.getByte("unit"));
		dataWatcher.updateObject(19, par1nbtTagCompound.getByte("exp"));
		for(int i = 0; i < 6; i++){
			setOption(i, par1nbtTagCompound.getByte("Option"+i));
		}
		dataWatcher.updateObject(28, par1nbtTagCompound.getByte("order"));
		for(int i = 0; i < 3; i++){
			dataWatcher.updateObject(29+i, par1nbtTagCompound.getInteger("order data"+i));
		}
		
		dataWatcher.updateObject(15, par1nbtTagCompound.getString("order data string"));
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
	
	public EnumOrder getOrder(){
		return EnumOrder.values()[dataWatcher.getWatchableObjectByte(28)];
	}
	
	public int[] getOrderData(){
		return new int[]{dataWatcher.getWatchableObjectInt(29), dataWatcher.getWatchableObjectInt(30), dataWatcher.getWatchableObjectInt(31)};
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
		String postfix = null;
		if(getAttackTarget() == null){
			EnumOrder order = getOrder();
			switch(order){
			case Follow:
				Entity e = worldObj.getEntityByID(dataWatcher.getWatchableObjectInt(29));
				if( e instanceof EntityPlayer || e == null)
					postfix = dataWatcher.getWatchableObjectString(15);
			
			}
			return this.getOrder().getTranslatedLabel(postfix);
		}else{
			StringTranslate st = StringTranslate.getInstance();
			return st.translateKey("order.attackMelee")+" "+getAttackTarget().getEntityName();
		}
	}
	
	public String getOrderStringData(){
		return dataWatcher.getWatchableObjectString(15);
	}

	@Override
	public boolean interact(EntityPlayer player) {
		
		ItemStack stack = player.getHeldItem();
		
		if(stack != null){
			
			if(stack.getItem() instanceof ItemPotion){
				if(!ItemPotion.isSplash(stack.getItemDamage())){
					if(!worldObj.isRemote){
						List<PotionEffect> effects = ((ItemPotion)stack.getItem()).getEffects(stack);
						for (PotionEffect potionEffect : effects) {
							System.out.println(this.activePotionsMap.containsKey(Integer.valueOf(potionEffect.getPotionID())));
							this.addPotionEffect(new PotionEffect(potionEffect.getPotionID(), potionEffect.getDuration(), potionEffect.getAmplifier()));
						}
					}
					
					if(!player.capabilities.isCreativeMode){
						player.inventory.decrStackSize(player.inventory.currentItem, 1);
						player.inventory.addItemStackToInventory(new ItemStack(Item.glassBottle));
					}
					
					this.func_85030_a("random.drink", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
					
					return true;
				}
				else
					return false;
			}else if(stack.getItem() instanceof ItemFood){
				if(!worldObj.isRemote){
					int healAmount = ((ItemFood)stack.getItem()).getHealAmount();
					healBonusTimer = Math.max(healAmount * ADVANCED_HEAL_RATE, healBonusTimer);
				}
				this.func_85030_a("random.eat", 0.5F + 0.5F * (float)this.rand.nextInt(2), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
				
			}
			
		}
		
		if(!worldObj.isRemote){
			if(this.getOrder() != EnumOrder.Follow){
				setOrder(EnumOrder.Follow, new int[]{player.entityId}, player.username);
				return true;
			}else{
				System.out.println(this.posX+", "+this.posY+", "+this.posZ+"    -     "+
						((int)Math.floor(posX)+.5F)+", "+((int)Math.floor(posY))+", "+((int)Math.floor(posZ)+.5F)
						);
				setOrder(EnumOrder.StandGuard, new int[]{
								(int)Math.floor(posX),
								(int)Math.floor(posY),
								(int)Math.floor(posZ)
						}, "");
				return true;
			}
		}else
			return false;
		
	}
	
	public void setOrder(EnumOrder order, int[] data, String stringData){
		this.dataWatcher.updateObject(28, (byte)order.ordinal());
		for(int i = 0; i < 3 && i <  data.length; i++){
			this.dataWatcher.updateObject(29+i, data[i]);
		}
		dataWatcher.updateObject(15, stringData);
	}
	
	
	public boolean attackEntityAsMob(Entity par1Entity)
    {
        int var2 = getAttackStrength();

        if (this.isPotionActive(Potion.damageBoost))
        {
            var2 += 3 << this.getActivePotionEffect(Potion.damageBoost).getAmplifier();
        }

        if (this.isPotionActive(Potion.weakness))
        {
            var2 -= 2 << this.getActivePotionEffect(Potion.weakness).getAmplifier();
        }

        int var3 = 0;

        if (par1Entity instanceof EntityLiving)
        {
            var2 += EnchantmentHelper.getEnchantmentModifierLiving(this, (EntityLiving)par1Entity);
            var3 += EnchantmentHelper.getKnockbackModifier(this, (EntityLiving)par1Entity);
        }

        boolean var4 = par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), var2);

        if (var4)
        {
        	swingItem();
        	dataWatcher.updateObject(19, (byte)Math.min((getCurrentExperience() + getExperiencePerHit()), 100));
            if (var3 > 0)
            {
                par1Entity.addVelocity((double)(-MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F) * (float)var3 * 0.5F), 0.1D, (double)(MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F) * (float)var3 * 0.5F));
                this.motionX *= 0.6D;
                this.motionZ *= 0.6D;
            }

            int var5 = EnchantmentHelper.getFireAspectModifier(this, (EntityLiving)par1Entity);

            if (var5 > 0)
            {
                par1Entity.setFire(var5 * 4);
            }
        }

        return var4;
    }

	protected abstract byte getExperiencePerHit();

	public abstract byte getCost();
	
	


	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, int par2) {
		
		if(this.getAttackTarget() == null){
			if (par1DamageSource.getSourceOfDamage() instanceof EntityLiving){
				
				if (par1DamageSource.getSourceOfDamage() instanceof EntityPlayer){
					playerDamage += par2;
					
					if(playerDamage > 10 && 
							! ((EntityPlayer)par1DamageSource.getSourceOfDamage()).capabilities.isCreativeMode){
						setAttackTarget((EntityLiving)par1DamageSource.getSourceOfDamage());
					}
				}else
					setAttackTarget((EntityLiving)par1DamageSource.getSourceOfDamage());
			}
		}
		
		return super.attackEntityFrom(par1DamageSource, par2);
	}


	@Override
	public int getMaxHealth() {
		return 0;
	}


	@Override
	protected boolean canDespawn() {
		return false;
	}
}
