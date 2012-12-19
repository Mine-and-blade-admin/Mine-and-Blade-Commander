package mab.common.commander.npc.ai;

import java.util.Iterator;
import java.util.List;

import com.google.common.util.concurrent.MoreExecutors;

import mab.common.commander.npc.EntityMBUnit;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityAIBase;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.RandomPositionGenerator;
import net.minecraft.src.Vec3;

public class MBEntityFlee extends EntityAIBase{

	private EntityMBUnit unit;
	private float speed;
	
	private EntityLiving closestEnemy;
    
    private int fleeDist;
    
    
	public MBEntityFlee(EntityMBUnit unit, float speed, int fleeDist) {
		super();
		this.unit = unit;
		this.speed = speed;
		this.fleeDist = fleeDist;
	}

	
	@Override
	public boolean shouldExecute() {
		
		double distSq = 100*100;
		
		//List var1 = this.unit.worldObj.getEntitiesWithinAABB(this.targetEntityClass, this.theEntity.boundingBox.expand((double)this.distanceFromEntity, 3.0D, (double)this.distanceFromEntity));
		List<Entity> entities = unit.worldObj.getEntitiesWithinAABBExcludingEntity(unit, unit.boundingBox.expand(25.0, 5.0D, 25.0));
		if(entities.isEmpty())
			return false;
		
		for (Entity entity : entities) {
			double dist = this.unit.getDistanceSqToEntity(entity);
			if(dist < distSq && unit.isEnemy(entity) && unit.canEntityBeSeen(entity)){
				distSq = dist;
				closestEnemy = (EntityLiving)entity;
			}
		}
		
		
		
		if (closestEnemy == null && (unit.getCurrentMorale() > -50 && unit.getCurrentHealth() < 0.15F * unit.getMaxHealth()) 
				|| (unit.getCurrentMorale() < -49 && unit.getCurrentHealth() > (((float)(50-unit.getCurrentMorale()-75))/100F) * unit.getMaxHealth())){
			return false;
		}else{		
			Vec3 var2 = RandomPositionGenerator.findRandomTargetBlockAwayFrom(unit, 5, 5, 
					unit.worldObj.getWorldVec3Pool().getVecFromPool(this.closestEnemy.posX, this.closestEnemy.posY, this.closestEnemy.posZ));
			
			
			
		}
		
		return true;
	}

	@Override
	public void startExecuting() {
		// TODO Auto-generated method stub
		super.startExecuting();
	}

	@Override
	public void resetTask() {
		// TODO Auto-generated method stub
		super.resetTask();
	}

	@Override
	public void updateTask() {
		// TODO Auto-generated method stub
		super.updateTask();
	}

	
}
