package mab.commander.npc.ai;

import mab.commander.npc.EntityMBUnit;
import net.minecraft.src.EntityAIBase;
import net.minecraft.src.MathHelper;
import net.minecraft.src.PathNavigate;
import net.minecraft.src.World;

public class MBEntityAIStandGuard extends EntityAIBase{

	
	 private EntityMBUnit unit;
	private World theWorld;
	private float warpDist;
	private PathNavigate petPathfinder;
	private int timer;
	private boolean avoidWater;

	public MBEntityAIStandGuard(EntityMBUnit par1EntityTameable, float par2)
	    {
	        this.unit = par1EntityTameable;
	        this.theWorld = par1EntityTameable.worldObj;
	        this.warpDist = 1;
	        this.petPathfinder = par1EntityTameable.getNavigator();
	    }
	 
	@Override
	public boolean shouldExecute() {
		return unit.getOrder() == EnumOrder.StandGuard 
				&& distSqToTarget() > warpDist*warpDist
				&& unit.getAttackTarget() == null;
				
	}
	
	/**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return !this.petPathfinder.noPath() &&
        		distSqToTarget() > (double)(this.warpDist * this.warpDist) 
        		&& unit.getOrder() == EnumOrder.StandGuard && unit.getAttackTarget() == null && unit.hurtTime == 0;
    }
	
    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.timer = 0;
        int[] data = unit.getOrderData();
        System.out.println((data[0]+.5F)+", "+data[1]+", "+ (data[2]+.5F));
        System.out.println(this.petPathfinder.tryMoveToXYZ(data[0]+.5F, data[1], data[2]+.5F,.3F));
        this.avoidWater = unit.getNavigator().getAvoidsWater();
        unit.getNavigator().setAvoidsWater(false);
    }
    
    /**
     * Resets the task
     */
    public void resetTask()
    {
        //this.petPathfinder.clearPathEntity();
        unit.getNavigator().setAvoidsWater(avoidWater);
        if(unit.getAttackTarget() == null && unit.hurtTime == 0 && distSqToTarget() > (double)(this.warpDist * this.warpDist) ){
	        int[] data = unit.getOrderData();
	        //unit.setPosition(data[0]+.5F, data[1], data[2]+.5F);
        }
    }
	
	
	private double distSqToTarget(){
		int[] data = unit.getOrderData();
		return unit.getDistanceSq(data[0]+.5F, data[1], data[2]+.5F);
	}
	
	
	/**
     * Updates the task
     */
    public void updateTask()
    {
        //unit.getLookHelper().setLookPositionWithEntity(owner, 10.0F, (float)unit.getVerticalFaceSpeed());

        if (unit.getOrder() == EnumOrder.StandGuard)
        {
            if (--this.timer <= 0)
            {
                this.timer = 10;

                int[] data = unit.getOrderData();
               
                this.petPathfinder.tryMoveToXYZ(data[0]+.5F, data[1], data[2]+.5F,.3F);
            }
        }
    }

}
