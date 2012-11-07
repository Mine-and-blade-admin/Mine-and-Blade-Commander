package mab.commander.npc.ai;

import mab.commander.npc.EntityMBUnit;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityAIBase;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityTameable;
import net.minecraft.src.MathHelper;
import net.minecraft.src.PathNavigate;
import net.minecraft.src.World;

public class MBEntityAIFollow extends EntityAIBase{
	
	private EntityMBUnit unit;
	private EntityLiving owner;
	private World theWorld;
	private float warpDist;
    private PathNavigate petPathfinder;
    private int timer;
    float maxDist;
    float minDist;
    private boolean avoidWater;
    
    public MBEntityAIFollow(EntityMBUnit par1EntityTameable, float par2, float par3, float par4)
    {
        this.unit = par1EntityTameable;
        this.theWorld = par1EntityTameable.worldObj;
        this.warpDist = par2;
        this.petPathfinder = par1EntityTameable.getNavigator();
        this.minDist = par3;
        this.maxDist = par4;
        this.setMutexBits(3);
    }

	

	@Override
	public boolean shouldExecute() {
		if(unit.getOrder() == EnumOrder.Follow){
			
			if(owner == null){
				owner = theWorld.getPlayerEntityByName(unit.getOrderStringData());
			}
			
			
			if(owner != null){
				return ! (unit.getDistanceSqToEntity(owner) < (double)(this.minDist * this.minDist));
			}else
				return false;
			
		}else
			return false;
	}
	
	 /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return !this.petPathfinder.noPath() &&
        		unit.getDistanceSqToEntity(owner) > (double)(this.maxDist * this.maxDist) 
        		&& unit.getOrder() == EnumOrder.Follow;
    }
	
	/**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.timer = 0;
        this.avoidWater = unit.getNavigator().getAvoidsWater();
        unit.getNavigator().setAvoidsWater(false);
    }
    
    /**
     * Resets the task
     */
    public void resetTask()
    {
        owner = null;
        this.petPathfinder.clearPathEntity();
        unit.getNavigator().setAvoidsWater(avoidWater);
    }
    
    /**
     * Updates the task
     */
    public void updateTask()
    {
        unit.getLookHelper().setLookPositionWithEntity(owner, 10.0F, (float)unit.getVerticalFaceSpeed());

        if (unit.getOrder() == EnumOrder.Follow)
        {
            if (--this.timer <= 0)
            {
                this.timer = 10;

                if (!this.petPathfinder.tryMoveToEntityLiving(owner, warpDist))
                {
                    if (this.unit.getDistanceSqToEntity(owner) >= 50*50)
                    {
                        int var1 = MathHelper.floor_double(this.owner.posX) - 2;
                        int var2 = MathHelper.floor_double(this.owner.posZ) - 2;
                        int var3 = MathHelper.floor_double(this.owner.boundingBox.minY);

                        for (int var4 = 0; var4 <= 4; ++var4)
                        {
                            for (int var5 = 0; var5 <= 4; ++var5)
                            {
                                if ((var4 < 1 || var5 < 1 || var4 > 3 || var5 > 3) && this.theWorld.doesBlockHaveSolidTopSurface(var1 + var4, var3 - 1, var2 + var5) && !this.theWorld.isBlockNormalCube(var1 + var4, var3, var2 + var5) && !this.theWorld.isBlockNormalCube(var1 + var4, var3 + 1, var2 + var5))
                                {
                                    this.unit.setLocationAndAngles((double)((float)(var1 + var4) + 0.5F), (double)var3, (double)((float)(var2 + var5) + 0.5F), this.unit.rotationYaw, this.unit.rotationPitch);
                                    this.petPathfinder.clearPathEntity();
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    

}
