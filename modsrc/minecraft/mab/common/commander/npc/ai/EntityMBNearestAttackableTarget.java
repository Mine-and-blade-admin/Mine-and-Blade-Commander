package mab.common.commander.npc.ai;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import mab.common.commander.ConfigHelper;
import mab.common.commander.npc.EntityMBUnit;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityAIBase;
import net.minecraft.src.EntityAINearestAttackableTargetSorter;
import net.minecraft.src.EntityAITarget;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IEntitySelector;

public class EntityMBNearestAttackableTarget extends EntityAITarget
{
    EntityLiving targetEntity;
    int targetChance;
    private final IEntitySelector field_82643_g;

    /** Instance of EntityAINearestAttackableTargetSorter. */
    private MBEntityAINearestAttackableTargetSorter theNearestAttackableTargetSorter;

    public EntityMBNearestAttackableTarget(EntityLiving par1EntityLiving, float par3, int par4, boolean par5)
    {
        this(par1EntityLiving, par3, par4, par5, false);
    }

    public EntityMBNearestAttackableTarget(EntityLiving par1EntityLiving, float par3, int par4, boolean par5, boolean par6)
    {
        this(par1EntityLiving, par3, par4, par5, par6, (IEntitySelector)null);
    }

    public EntityMBNearestAttackableTarget(EntityLiving par1, float par3, int par4, boolean par5, boolean par6, IEntitySelector par7IEntitySelector)
    {
        super(par1, par3, par5, par6);
        this.targetDistance = par3;
        this.targetChance = par4;
        this.theNearestAttackableTargetSorter = new MBEntityAINearestAttackableTargetSorter(this, par1);
        this.field_82643_g = par7IEntitySelector;
        this.setMutexBits(1);
    }
    
    

	@Override
	public boolean continueExecuting() {
		return super.continueExecuting() && targetEntity == taskOwner.getAITarget();
	}

	@Override
	public void resetTask() {
		if(taskOwner.getAITarget() == targetEntity)
			super.resetTask();
	}

	/**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
    	
        if(this.taskOwner.getAttackTarget()==null)
        {

            
                List var5 = this.taskOwner.worldObj.getEntitiesWithinAABBExcludingEntity(this.taskOwner, this.taskOwner.boundingBox.expand(((EntityMBUnit)taskOwner).getTargetDist(), 4.0D,((EntityMBUnit)taskOwner).getTargetDist()));
                
                Collections.sort(var5, this.theNearestAttackableTargetSorter);
                Iterator var2 = var5.iterator();

                while (var2.hasNext())
                {
                    Entity var3 = (Entity)var2.next();
                    if(var3 instanceof EntityLiving){
	                    EntityLiving var4 = (EntityLiving)var3;
	
	                    if (((EntityMBUnit)taskOwner).isEnemy(var4) && taskOwner.canEntityBeSeen(var4))
	                    {
	                        this.targetEntity = var4;
	                        return true;
	                    }
                    }
                }
            

            return false;
        }else
        	return false;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.taskOwner.setAttackTarget(this.targetEntity);
        super.startExecuting();
    }
}
