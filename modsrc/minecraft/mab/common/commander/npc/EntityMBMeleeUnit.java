package mab.common.commander.npc;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import mab.common.commander.EnumTeam;
import mab.common.commander.MBCommander;
import mab.common.commander.npc.ai.EntityMBNearestAttackableTarget;
import mab.common.commander.npc.ai.MBEntityAIAttackMelee;
import mab.common.commander.npc.ai.MBEntityAIFollow;
import mab.common.commander.npc.ai.MBEntityAIStandGuard;
import net.minecraft.src.DamageSource;
import net.minecraft.src.EntityAIHurtByTarget;
import net.minecraft.src.EntityAILeapAtTarget;
import net.minecraft.src.EntityAILookIdle;
import net.minecraft.src.EntityAINearestAttackableTarget;
import net.minecraft.src.EntityAIOpenDoor;
import net.minecraft.src.EntityAISwimming;
import net.minecraft.src.EntityAIWatchClosest;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IMob;
import net.minecraft.src.World;

public class EntityMBMeleeUnit extends EntityMBUnit{
	
	private static EnumUnitItems[] militaWeapons = new EnumUnitItems[]{EnumUnitItems.StoneSword, EnumUnitItems.StoneSword, EnumUnitItems.StoneAxe, EnumUnitItems.WoodMace, EnumUnitItems.StoneMace, EnumUnitItems.WoodSpear,  EnumUnitItems.StoneSpear, EnumUnitItems.PitchFork};
	private static EnumUnitItems[] oneHandWeapons = new EnumUnitItems[]{EnumUnitItems.IronSword, EnumUnitItems.IronBattleaxe, EnumUnitItems.IronMace};
	private static EnumUnitItems[] spearWeapons= new EnumUnitItems[]{EnumUnitItems.IronSpear, EnumUnitItems.IronHalberard, EnumUnitItems.IronGlaive};
	private static EnumUnitItems[] shield1 = new EnumUnitItems[]{EnumUnitItems.WoodShield, EnumUnitItems.HideShield, EnumUnitItems.HideShiedPaint};
	private static EnumUnitItems[] shield2 = new EnumUnitItems[]{EnumUnitItems.IronShieldPaint, EnumUnitItems.DiamondShieldPaint, EnumUnitItems.GoldShieldPaint};
	
	private static EnumUnits[] militiaUpgrades = new EnumUnits[]{EnumUnits.ManAtArms, EnumUnits.Spearman};
	private static EnumUnits[] manatarmsUpgrades = new EnumUnits[]{EnumUnits.KnightShield, EnumUnits.KnightDuel};
	private static EnumUnits[] spearmanUpgrades = new EnumUnits[]{EnumUnits.KnightSpear};
	
	public EntityMBMeleeUnit(World par1World) {
		this(par1World, EnumTeam.values()[par1World.rand.nextInt(16)], EnumUnits.Militia);
	}
	
	public EntityMBMeleeUnit(World par1World, EnumTeam team, EnumUnits type) {
		super(par1World, team, type);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public String getTexture() {
		
		switch(getUnitType()){
		case Militia:
			return MBCommander.IMAGE_FOLDER+"skins/units/milita/Militia ("+getTeam().ordinal()+").png";
		case ManAtArms:
		case Spearman:
			return MBCommander.IMAGE_FOLDER+"skins/units/footman/Footman ("+getTeam().ordinal()+").png";
		case KnightShield:
		case KnightDuel:
		case KnightSpear:
			return MBCommander.IMAGE_FOLDER+"skins/units/knight/Knight ("+getTeam().ordinal()+").png";
		default:
			return "";
		}
	}

	@Override
	public int getAttackStrength() {
		switch(getUnitType()){
		case Militia:
			return 4;
		case ManAtArms:
		case Spearman:
			return 6;
		case KnightShield:
		case KnightSpear:
			return 8;
		case KnightDuel:
			return 9;
		default:
			return 0;
		}
	}

	@Override
	public EnumUnitItems getWeaponOption() {
		switch(getUnitType()){
		case Militia:
			return militaWeapons[getOption(3)];
		case ManAtArms:
		case KnightShield:
		case KnightDuel:
			return oneHandWeapons[getOption(3)];
		case Spearman:
		case KnightSpear:
			return spearWeapons[getOption(3)];
		default:
			return null;
		}
	}

	@Override
	public EnumUnitItems getWeaponOffHandOption() {
		switch(getUnitType()){
		case ManAtArms:
		case Spearman:
			return shield1[getOption(4)];
		case KnightShield:
		case KnightSpear:
			return shield2[getOption(4)];
		case KnightDuel:
			return oneHandWeapons[getOption(4)];
		default:
			return null;
		}
	}

	@Override
	public int getHelmNumber() {
		if(unit == EnumUnits.Militia)
			return getOption(4)-1;
		else
			return getOption(5)-1;
	}

	@Override
	protected byte getExperiencePerHit() {
		switch(getUnitType()){
		case Militia:
			return (byte) (5 + getBonusExperience());
		case ManAtArms:
		case Spearman:
			return (byte) (3 + getBonusExperience());
		default:
			return 0;
		}
	}

	@Override
	public byte getCost() {
		switch(getUnitType()){
		case Militia:
			return 2;
		case ManAtArms:
		case Spearman:
			return 3;
		case KnightShield:
		case KnightDuel:
		case KnightSpear:
			return 4;
		default:
			return 0;
		}
	}
	
	@Override
	public int getMaxHealth() {
		try{
		switch(getUnitType()){
		case Militia:
			return 15;
		case ManAtArms:
		case Spearman:
			return 20;
		case KnightDuel:
		case KnightShield:
		case KnightSpear:
			return 25;
		default:
			return 0;
		}
		}catch(NullPointerException e){
			return 15;
		}
	}
	
	@Override
	public int getBaseArmour() {
		switch(getUnitType()){
		case Militia:
			return 2;
		case ManAtArms:
		case Spearman:
			return 6;
		case KnightShield:
			return 15;
		case KnightSpear:
			return 13;
		case KnightDuel:
			return 12;
		default:
			return 0;
		}
	}

	@Override
	public int getArmourPenatratingDamage() {
		if(getUnitType() == EnumUnits.KnightDuel)
			return 2;
		else
			return 0;
	}

	@Override
	public EnumAbilities[] getAbilities() {
		
		switch(getUnitType()){
		case Spearman:
			return new EnumAbilities[]{EnumAbilities.ExtendedReach, EnumAbilities.SlowAttack};
		case KnightShield:
			return new EnumAbilities[]{EnumAbilities.ArrowBlock};
		case KnightDuel:
			return new EnumAbilities[]{EnumAbilities.ArmourPenatrate, EnumAbilities.Leap};
		case KnightSpear:
			return new EnumAbilities[]{EnumAbilities.ExtendedReach, EnumAbilities.SlowAttack, EnumAbilities.ArrowBlock};
		default:
			return super.getAbilities();
		}
	}

	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, int par2) {
		EnumUnits unit = getUnitType();
		if((unit == EnumUnits.KnightShield || unit == EnumUnits.KnightSpear) && par1DamageSource.isProjectile()){
			return super.attackEntityFrom(par1DamageSource, (int)(par2*.75F));
		}else
			return super.attackEntityFrom(par1DamageSource, par2);
	}
	
	
	@Override
	protected void addAITasks(EnumUnits type){
			this.tasks.addTask(1, new EntityAISwimming(this));
			
			if(type == EnumUnits.Spearman){
				this.tasks.addTask(2, new MBEntityAIAttackMelee(this, this.moveSpeed, true, 45, 4F));
				this.tasks.addTask(7, new MBEntityAIFollow(this, this.moveSpeed, 6.0F, 4.5F));
			}
			else if(type == EnumUnits.KnightSpear){
				this.tasks.addTask(2, new MBEntityAIAttackMelee(this, this.moveSpeed, true, 40, 4F));
				this.tasks.addTask(7, new MBEntityAIFollow(this, this.moveSpeed, 6.0F, 4.5F));
			}
			else if(type == EnumUnits.KnightDuel){
				this.tasks.addTask(2, new EntityAILeapAtTarget(this, 0.4F));
				this.tasks.addTask(3, new MBEntityAIAttackMelee(this, this.moveSpeed, true));
				
				this.tasks.addTask(7, new MBEntityAIFollow(this, this.moveSpeed, 5.0F, 3.5F));
			}else{
				this.tasks.addTask(2, new MBEntityAIAttackMelee(this, this.moveSpeed, true));
				this.tasks.addTask(7, new MBEntityAIFollow(this, this.moveSpeed, 5.0F, 3.5F));
			}
	        this.tasks.addTask(5, new EntityAIOpenDoor(this, true));	        
			
	        
	        this.tasks.addTask(8, new MBEntityAIStandGuard(this, .0F));
			this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
	        this.tasks.addTask(9, new EntityAILookIdle(this));

	        //this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
	        this.targetTasks.addTask(2, new EntityMBNearestAttackableTarget(this, 16.0F, 0, true));
	}

	@Override
	public EnumUnits[] getUpgrades() {
		switch(getUnitType()){
		case Militia:
			return militiaUpgrades;
		case ManAtArms:
			return manatarmsUpgrades;
		case Spearman:
			return spearmanUpgrades;
		default :
			return new EnumUnits[0];
					
		}
	}
	
	
	

}
