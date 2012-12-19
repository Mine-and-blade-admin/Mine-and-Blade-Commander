package mab.common.commander.npc;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.text.PlainDocument;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import cpw.mods.fml.relauncher.ReflectionHelper;

import mab.common.commander.CommanderPacketHandeler;
import mab.common.commander.ConfigHelper;
import mab.common.commander.DamageSourceArmourPenatrate;
import mab.common.commander.EnumTeam;
import mab.common.commander.IUpgradeable;
import mab.common.commander.MBCommander;
import mab.common.commander.npc.ai.EntityMBNearestAttackableTarget;
import mab.common.commander.npc.ai.EnumOrder;
import mab.common.commander.npc.ai.MBEntityAIAttackMelee;
import mab.common.commander.npc.ai.MBEntityAIFollow;
import mab.common.commander.npc.ai.MBEntityAIStandGuard;
import mab.common.commander.utils.CommonHelper;
import mab.common.commander.utils.TeamMap;
import net.minecraft.src.Block;
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
import net.minecraft.src.EntityAITasks;
import net.minecraft.src.EntityAIWander;
import net.minecraft.src.EntityAIWatchClosest;
import net.minecraft.src.EntityCreature;
import net.minecraft.src.EntityCreeper;
import net.minecraft.src.EntityDamageSource;
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
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.PathNavigate;
import net.minecraft.src.Potion;
import net.minecraft.src.PotionEffect;
import net.minecraft.src.PotionHelper;
import net.minecraft.src.StringTranslate;
import net.minecraft.src.World;

public abstract class EntityMBUnit extends EntityCreature implements IUpgradeable, IEntityAdditionalSpawnData{
	
	public boolean renderShadow = true;
	
	private int weaponDrawTimer;
	
	private int healBonusTimer = 0;
	
	public static final int MAX_WEAPON_DRAW = 20*20; //20 seconds
	public static final int NORMAL_HEALRATE = 20*3; // every 3 seconds
	public static final int ADVANCED_HEAL_RATE = NORMAL_HEALRATE; // every 1.5 seconds
	public static final int MORALE_DEGRADE_RATE = 20*15; // 15 seconds
	
	private int playerDamage = 0;

	private boolean addedTasks = false;
	
	protected EnumTeam team;
	protected EnumUnits unit;
	protected byte[] options;
	private String owner = "";
	
	private int moraleCounter = MORALE_DEGRADE_RATE;
	
	private byte current_morale;

	protected double targetDistance = 16;
	
	
	public EntityMBUnit(World par1World, EnumTeam team, EnumUnits type) {
		super(par1World);
		
		this.moveSpeed = .3F;
		this.setSize(0.6F, 1.8F);
		
		this.unit = type;
		this.team = team;
		
		options  = new byte[6];
		Arrays.fill(options, (byte)-1);
		
		weaponDrawTimer =MAX_WEAPON_DRAW;
		
		//18: Health
		dataWatcher.addObject(18, (byte)getMaxHealth());
		//19: Experience
		dataWatcher.addObject(19, (byte)0);
		//20: Morale
		dataWatcher.addObject(20, (byte)0);
		//21: Misc
		dataWatcher.addObject(21, (byte)0);
		
		//26: Weapon Draw Timer
		dataWatcher.addObject(26, (byte)1);
		//27: Order
		dataWatcher.addObject(27, (byte)0);
		//28: Order Data String
		dataWatcher.addObject(28, "");
		//29, 30, 31: Order Data
		dataWatcher.addObject(29, -1);
		dataWatcher.addObject(30, -1);
		dataWatcher.addObject(31, -1);

		ReflectionHelper.setPrivateValue(EntityLiving.class, this, new PathNavigate(this, par1World, ConfigHelper.getPathFindSearch()), 58);
		
		this.health = getMaxHealth();
	}
	
	

	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
		
		if (this.worldObj.isRemote)
        {
            if (isJumping())
            {
                this.isJumping = true;
            }
            else
            {
                this.isJumping = false;
            }
        }
        else
        {
            this.setAdjacentClimbBlock(this.checkForAdjacentClimbBlock());
        }
		
	}
	
	protected void addAITasks(EnumUnits unit){
		
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new MBEntityAIAttackMelee(this, moveSpeed, true));
		
		//this.tasks.addTask(4, new EntityAIRestrictOpenDoor(this));
        this.tasks.addTask(5, new EntityAIOpenDoor(this, true));
        //this.tasks.addTask(6, new EntityAIMoveTwardsRestriction(this, .3F));
        
		
        this.tasks.addTask(7, new MBEntityAIFollow(this, moveSpeed, 6.0F, 3.5F));
        this.tasks.addTask(8, new MBEntityAIStandGuard(this, .25F));
		this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(9, new EntityAILookIdle(this));

        this.targetTasks.addTask(2, new EntityMBNearestAttackableTarget(this, 16.0F, 0, false));
        
        this.getNavigator().setBreakDoors(true);
	}
	
	@Override
	protected void entityInit() {
		super.entityInit();
		moraleCounter = MORALE_DEGRADE_RATE;
	}
	
	
	@Override
	public float getSpeedModifier() {
		float morale = (float)current_morale / 100F;

		return super.getSpeedModifier() + .1F*morale;
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
	   if(!addedTasks){
		   addAITasks(getUnitType());
		   addedTasks  = true;
		   getNavigator().setEnterDoors(true);
	   }
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
	   
	   if(moraleCounter > 0)
		   moraleCounter --;
	   else{
		   byte morale = getCurrentMorale();
		   if(morale > -100)
			   dataWatcher.updateObject(20, (byte)(morale-1));
		   moraleCounter = MORALE_DEGRADE_RATE;
	   }
	   
	   
	   
	   if(weaponDrawTimer == 0 && isWeaponsDrawn())
		   dataWatcher.updateObject(26, (byte)0);
	   else if (weaponDrawTimer > 0 && !isWeaponsDrawn())
		   dataWatcher.updateObject(26, (byte)1);
	   
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
	    	   if (worldObj.getPlayerEntityByName(dataWatcher.getWatchableObjectString(28)) == null){
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
		
		this.updateArmSwingProgress();
		current_morale = calculateCurrentMorale();
		super.onLivingUpdate();
	}
	
	public abstract EnumUnits[] getUpgrades();


	public boolean isWeaponsDrawn(){
		return dataWatcher.getWatchableObjectByte(26) == 1;
	}

	/** The amount of damage the unit delivers */
	public abstract int getAttackStrength();
	
	/** The amount of damage the unit delivers */
	public int getAttackStrengthBonus() {
		byte morale = current_morale;
		if(morale > 50)
			return 1;
		else if(morale >= -50)
			return 0;
		else
			return -1;
		
	}
	
	public abstract int getBaseArmour();
	
	public int getArmourBonus() {
		byte morale = current_morale;
		if(morale > 75)
			return 2;
		else if (morale > 25)
			return 1;
		else if (morale >= -25)
			return 0;
		else if(morale >= -75)
			return -1;
		else
			return -2;
		
	}
	
	@Override
	public int getTotalArmorValue() {
		return getBaseArmour() + getArmourBonus();
	}

	public String getUnitName(){
		return unit.getFullName();
	}
	

	@Override
	public void writeEntityToNBT(NBTTagCompound par1nbtTagCompound) {
		super.writeEntityToNBT(par1nbtTagCompound);

		par1nbtTagCompound.setByte("team", (byte)team.ordinal());
		par1nbtTagCompound.setByte("unit", (byte)unit.ordinal());
		par1nbtTagCompound.setByte("exp", dataWatcher.getWatchableObjectByte(19));
		par1nbtTagCompound.setByte("morale", dataWatcher.getWatchableObjectByte(20));
		for(int i = 0; i < 6; i++){
			par1nbtTagCompound.setByte("Option"+i, getOption(i));
		}
		par1nbtTagCompound.setByte("order", dataWatcher.getWatchableObjectByte(27));
		for(int i = 0; i < 3; i++){
			par1nbtTagCompound.setInteger("order data"+i, dataWatcher.getWatchableObjectInt(29+i));
		}
		
		par1nbtTagCompound.setString("order data string", dataWatcher.getWatchableObjectString(28));
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound par1nbtTagCompound) {
		super.readEntityFromNBT(par1nbtTagCompound);
		
		team = EnumTeam.values()[par1nbtTagCompound.getByte("team")];
		unit = EnumUnits.values()[par1nbtTagCompound.getByte("unit")];
		dataWatcher.updateObject(19, par1nbtTagCompound.getByte("exp"));
		dataWatcher.updateObject(20, par1nbtTagCompound.getByte("morale"));
		for(int i = 0; i < 6; i++){
			setOption(i, par1nbtTagCompound.getByte("Option"+i));
		}
		dataWatcher.updateObject(27, par1nbtTagCompound.getByte("order"));
		for(int i = 0; i < 3; i++){
			dataWatcher.updateObject(29+i, par1nbtTagCompound.getInteger("order data"+i));
		}
		
		dataWatcher.updateObject(28, par1nbtTagCompound.getString("order data string"));
		
		
		addAITasks(getUnitType());
	}
	
	public final byte getOptionMax(int option){
		return (byte)unit.getOptionMax()[option];
	}
	
	public final String getOptionLabel(int option){
		return EnumUnits.labels[unit.getOptionIcons()[option]];
	}
	
	public final int getOptionIcon(int option){
		return unit.getOptionIcons()[option];
	}
	
	public byte getOption(int option){
		return this.options[option];
	}
	
	public byte getCurrentHealth(){
		return dataWatcher.getWatchableObjectByte(18);
	}
	
	public byte getCurrentExperience(){
		return dataWatcher.getWatchableObjectByte(19);
	}
	
	public byte getCurrentMorale(){
		return dataWatcher.getWatchableObjectByte(20);
	}
	
	public byte calculateCurrentMorale(){
		byte morale = getCurrentMorale();
		
		List<Entity> entities = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(16, 16, 16));
		
		int bonus = 0;
		
		boolean allowDifferetTeam = ! MBCommander.config.get(ConfigHelper.CAT_UNITS, ConfigHelper.UNIT_ATTACK_TEAM, false).getBoolean(false);
		for (Entity entity : entities) {
			if(entity instanceof EntityMBUnit){
				if(allowDifferetTeam || ((EntityMBUnit)entity).getTeam() == team){
					bonus += ((EntityMBUnit)entity).getUnitType().getTier() * 3;
				}else{
					bonus -= ((EntityMBUnit)entity).getUnitType().getTier() * 3;
				}
			}else if (entity instanceof EntityPlayer){
				if(CommonHelper.isTeamGame()){
					if(worldObj.isRemote){
						if(TeamMap.getInstance().isOnSameTeam((EntityPlayer)entity, getTeam()))
							bonus+=10;
						else
							bonus-=10;
					}else{
						if(entity.getEntityData().hasKey("MB-Team") && entity.getEntityData().getByte("MB-Team") == this.getTeam().ordinal()){
							bonus+=10;
						}else
							bonus-=10;
					}
				}else
					bonus += 10;
			}else if(entity instanceof IMob){
				bonus = bonus - 5;
			}
				
		}

		
		return (byte)Math.max(Math.min(morale+bonus, 100), -100);
	}
	

	public EnumTeam getTeam(){
		return team;
	}
	
	public EnumUnits getUnitType(){
		return unit;
	}
	
	public EnumOrder getOrder(){
		return EnumOrder.values()[dataWatcher.getWatchableObjectByte(27)];
	}
	
	public int[] getOrderData(){
		return new int[]{dataWatcher.getWatchableObjectInt(29), dataWatcher.getWatchableObjectInt(30), dataWatcher.getWatchableObjectInt(31)};
	}

	public void setOption(int option, byte value){
		options[option] = value;
	}

	public String getSkinFile(){
		return MBCommander.IMAGE_FOLDER+"skins/Body ("+getOption(0)+").png";
	}
	
	public String getHairFile(){
		return MBCommander.IMAGE_FOLDER+"skins/Hair ("+getOption(1)+").png";
	}
	
	public String getEyesFile(){
		return MBCommander.IMAGE_FOLDER+"skins/Eyes ("+getOption(2)+").png";
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
		//if(getAttackTarget() == null){
			EnumOrder order = getOrder();
			switch(order){
			case Follow:
				Entity e = worldObj.getEntityByID(dataWatcher.getWatchableObjectInt(29));
				if( e instanceof EntityPlayer || e == null)
					postfix = dataWatcher.getWatchableObjectString(28);
			default:
				postfix = null;
			
			}
			return this.getOrder().getTranslatedLabel(postfix);
		//}else{
		//	StringTranslate st = StringTranslate.getInstance();
		//	return st.translateKey("order.attackMelee")+" "+getAttackTarget().getEntityName();
		//}
	}
	
	public String getOrderStringData(){
		return dataWatcher.getWatchableObjectString(28);
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
							this.addPotionEffect(new PotionEffect(potionEffect.getPotionID(), potionEffect.getDuration(), potionEffect.getAmplifier()));
						}
					}
					
					if(!player.capabilities.isCreativeMode){
						player.inventory.decrStackSize(player.inventory.currentItem, 1);
						player.inventory.addItemStackToInventory(new ItemStack(Item.glassBottle));
					}
					
					this.worldObj.playSoundAtEntity(this, "random.drink", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
					
					return true;
				}
				else
					return false;
			}else if(stack.getItem() instanceof ItemFood){
				if(!worldObj.isRemote){
					ItemFood food = (ItemFood)(stack.getItem());
					
					if(food.shiftedIndex == Item.rottenFlesh .shiftedIndex){
						dataWatcher.updateObject(20, (byte)Math.max(getCurrentMorale() - (byte)(food.getHealAmount()*30), -100));	
					}else
						dataWatcher.updateObject(20, (byte)Math.min(getCurrentMorale() + (byte)(food.getHealAmount()*10), 100));		
					
				}
				this.worldObj.playSoundAtEntity(this, "random.eat", 0.5F + 0.5F * (float)this.rand.nextInt(2), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
				
			}
			
		}
		
			return false;
		
	}
	
	public void setOrder(EnumOrder order, int[] data, String stringData){
		this.dataWatcher.updateObject(27, (byte)order.ordinal());
		for(int i = 0; i < 3 && i <  data.length; i++){
			this.dataWatcher.updateObject(29+i, data[i]);
		}
		dataWatcher.updateObject(28, stringData);
	}
	
	
	public boolean attackEntityAsMob(Entity par1Entity)
    {
		int var6 = getArmourPenatratingDamage();
        int var2 = getAttackStrength() - var6 + getAttackStrengthBonus();

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

            int var5 = EnchantmentHelper.func_90036_a((EntityLiving)par1Entity);

            if (var5 > 0)
            {
                par1Entity.setFire(var5 * 4);
            }
            
            
            if(var6 > 0){
            	par1Entity.hurtResistantTime = 0;
            	par1Entity.attackEntityFrom(new DamageSourceArmourPenatrate(this), var6);
            }
            
            if(!worldObj.isRemote && ! par1Entity.isEntityAlive()){
            	addToMorale(10);
            	List<EntityMBUnit> units = getSurroundingTeamUnits();
            	for (EntityMBUnit entityMBUnit : units) {
					entityMBUnit.addToMorale(3);
				}
            }
            
        }

        return var4;
    }

	public void addToMorale(int i) {
		dataWatcher.updateObject(20, (byte)Math.min(i + getCurrentMorale(), 100));
		
	}

	protected abstract byte getExperiencePerHit();

	public abstract byte getCost();
	


	@Override
	public int getMaxHealth() {
		return 0;
	}


	@Override
	protected boolean canDespawn() {
		return false;
	}
	
	
	@Override
	public int getFirstEditableOptionIndex() {
		return 3;
	}

	@Override
	public Packet250CustomPayload generatePacket(EntityMBUnit unit,
			EntityPlayer player) {
		return CommanderPacketHandeler.generateUpgradePacket(this, unit, player);
	}

	@Override
	public EntityMBUnit getDefaltUnit() {
		return this;
	}
	
	public EnumAbilities[] getAbilities(){
		return new EnumAbilities[0];
	}

	@Override
	public byte getDefaultOption(int i) {
		if(i > 2)
			return 0;
		else 
			return getOption(i);
	}

	public static EntityMBUnit generateUnit(World world,
			EnumTeam team, EnumUnits enumUnits, IUpgradeable upgradeable) {

		EntityMBUnit unit = generateUnit(world, team, enumUnits);
		
		if(upgradeable instanceof EntityMBUnit){
			EntityMBUnit prev = (EntityMBUnit)upgradeable;
			for(int i = 0; i < 3; i++)
				unit.setOption(i, prev.getOption(i));
		}
		
		return unit;
	}
	
	public int getArmourPenatratingDamage(){
		return 0;
	}

	@Override
	public void writeSpawnData(ByteArrayDataOutput data) {
		
		data.writeByte((byte)team.ordinal());
		data.writeByte((byte)unit.ordinal());
		for(int i = 0; i < options.length; i++){
			data.writeByte(options[i]);
		}
		data.writeUTF(owner);
		
	}

	@Override
	public void readSpawnData(ByteArrayDataInput data) {
		
		team = EnumTeam.values()[data.readByte()];
		unit = EnumUnits.values()[data.readByte()];
		for(int i = 0; i < 6; i++){
			options[i] = data.readByte();
		}
		owner = data.readUTF();
		
	}

	public void setOwner(String username) {
		this.owner = username;
	}
	
	@Override
	public String getEntityName() {
		System.out.println(super.getEntityName());
		return StringTranslate.getInstance().translateKey(getUnitName());
	}



	public String getOwner() {
		return owner;
	}
	
	protected int getBonusExperience(){
		byte morale = current_morale;
		if(morale >=50)
			return 1;
		else if (morale < -50)
			return -1;
		else
			return 0;
	}
	
	public List<EntityMBUnit> getSurroundingTeamUnits(){
		boolean teams = MBCommander.config.get(ConfigHelper.CAT_UNITS, ConfigHelper.UNIT_ATTACK_TEAM, false).getBoolean(false);
		List<EntityMBUnit> units = new ArrayList<EntityMBUnit>();
		List<Entity> entities = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(16, 16, 16));
		for (Entity entity : entities) {
			if(entity instanceof EntityMBUnit){
				if(!teams || getTeam().equals(((EntityMBUnit)entity).getTeam())){
					units.add((EntityMBUnit) entity);
				}
			}
		}
		return units;
	}

	@Override
	public void onDeath(DamageSource par1DamageSource) {
		if(!worldObj.isRemote){
        	List<EntityMBUnit> units = getSurroundingTeamUnits();
        	for (EntityMBUnit entityMBUnit : units) {
				entityMBUnit.addToMorale(-2 * entityMBUnit.getUnitType().getTier());
			}
        }
		super.onDeath(par1DamageSource);
	}

	public boolean isEnemy(Entity entity, boolean filterCreative) {
		if(CommonHelper.isTeamGame()){
			if(entity instanceof EntityPlayer && (!((EntityPlayer)entity).capabilities.isCreativeMode || !filterCreative)){
				return (! TeamMap.getInstance().isOnSameTeam((EntityPlayer)entity, getTeam()));
			}else if (entity instanceof EntityMBUnit)
				return (! ((EntityMBUnit)entity).getTeam().equals(getTeam()));
		}

		return entity instanceof EntityLiving && entity instanceof IMob && !(entity instanceof EntityCreeper);
	}
	
	public boolean isEnemy(Entity entity){
		return isEnemy(entity, true);
	}
	
	public byte getCurrentMoraleWithBonus(){
		return current_morale;
	}

	public byte getBaseMorale() {
		return dataWatcher.getWatchableObjectByte(20);
	}
	
	public void setBaseMorale(byte morale) {
		dataWatcher.updateObject(20, morale);
	}

	@Override
	public boolean attackEntityFrom(DamageSource s, int par2) {
		
		
			if (s.getSourceOfDamage() instanceof EntityLiving
					&& s.getSourceOfDamage().entityId != this.entityId){
				
						if (s.getSourceOfDamage() instanceof EntityPlayer){
					playerDamage += par2;
					
					if(playerDamage > 10 && 
						! ((EntityPlayer)s.getSourceOfDamage()).capabilities.isCreativeMode){
						setAttackTarget((EntityLiving)s.getSourceOfDamage());
					}
				}else{
					setAttackTarget((EntityLiving)s.getEntity());
				}
			}
			
		
		
		return super.attackEntityFrom(s, par2);
	}

	public float getTurnRate() {
		return 30F;
	}

    public boolean isOnLadder()
    {
        return this.isAdjacentClimbBlock();
    }

	private boolean isJumping() {
		return this.dataWatcher.getWatchableObjectByte(21) %2 == 1;
	}
	
	public boolean isAdjacentClimbBlock() {
		return (this.dataWatcher.getWatchableObjectByte(21) / 2) %2 == 1;
    }
	
	public boolean isHoldingOntoLadder() {
		return dataWatcher.getWatchableObjectByte(21) > 3;
	}
	
	
	private void updateMiscDataWatcher(boolean jump, boolean nextTo, boolean isHolding){
		if (!this.worldObj.isRemote){
			byte value = 0;
			if(jump)
				value=1;
			if(nextTo)
				value = (byte) (value | 2);
			if(isHolding)
				value = (byte) (value | 4);
			
			this.dataWatcher.updateObject(21, value);
		}
	}
	
	public void setJumping(boolean var1)
    {
        super.setJumping(var1);
        updateMiscDataWatcher(var1, isAdjacentClimbBlock(), isHoldingOntoLadder());
    }

	public void setAdjacentClimbBlock(boolean nextTo) {
		updateMiscDataWatcher(isJumping(), nextTo, isHoldingOntoLadder());
    }
	
	public void setHoldingOntoLadder(boolean isHolding) {
		 updateMiscDataWatcher(isJumping(), isAdjacentClimbBlock(), isHolding);
	}

    public boolean checkForAdjacentClimbBlock()
    {
        int var1 = MathHelper.floor_double(this.posX);
        int var2 = MathHelper.floor_double(this.boundingBox.minY);
        int var3 = MathHelper.floor_double(this.posZ);
        int var4 = this.worldObj.getBlockId(var1, var2, var3);
        return Block.blocksList[var4] != null && Block.blocksList[var4].isLadder(this.worldObj, var1, var2, var3);
    }

	public double getTargetDist() {
		return targetDistance ;
	}
	
	public double getNormalTargetDistance(){
		return 16D;
	}
	
	public double getNearTargetDistance(){
		return 8D;
	}
	
	public void setTargetDistance(double target){
		targetDistance = target;
	}
}
