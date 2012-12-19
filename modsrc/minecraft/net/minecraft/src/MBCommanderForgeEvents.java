package net.minecraft.src;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import mab.common.commander.CommanderPacketHandeler;
import mab.common.commander.EnumTeam;
import mab.common.commander.npc.EntityMBUnit;
import mab.common.commander.npc.ai.MBEntityAIAttackMelee;
import mab.common.commander.utils.CommonHelper;
import mab.common.commander.utils.TeamMap;
import mab.common.commander.utils.TeamPacketHandeler;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.living.LivingSpecialSpawnEvent;
import net.minecraftforge.event.world.WorldEvent;

public class MBCommanderForgeEvents {
	
	
	//private static MBNearestAttackableSorter a;
	
	@ForgeSubscribe
	public void livingSpawnEvent(EntityJoinWorldEvent event){
		
		if(event.entity instanceof IMob){
			EntityLiving living = (EntityLiving)event.entity;
			
			if(living != null){
				
				if(living.isAIEnabled()){
					if(living instanceof IRangedAttackMob){
						living.targetTasks.addTask(2, new EntityAINearestAttackableTarget(living, EntityMBUnit.class, 16.0F, 0, true));
					}else if(living instanceof EntityCreeper){
						
					}else{
						living.tasks.addTask(2, new MBEntityAIAttackMelee(living, .3F, true));
						living.targetTasks.addTask(2, new EntityAINearestAttackableTarget(living, EntityMBUnit.class, 16.0F, 0, true));
					}
				}
			}
		}
	}

	@ForgeSubscribe
	public void livingUpdateEvent(LivingUpdateEvent event){
		
		if(event.entity != null && event.entity instanceof IMob && event.entity instanceof EntityCreature){
			EntityCreature living = (EntityCreature)event.entity;
			if(! living.isAIEnabled() && living.getEntityToAttack() == null){
				Entity attackTarget = findUnitToAttack(living, 16);
				living.setTarget(attackTarget);
		        if (living.getEntityToAttack() != null)
		        {
		        	living.setPathToEntity(living.worldObj.getPathEntityToEntity(
		          			living, living.getEntityToAttack(), 16.0F, true, false, false, true));
		        }
			}
		}else if(event.entity != null && event.entity instanceof EntityPlayer && !event.entity.worldObj.isRemote){
			if(event.entity.ticksExisted % 20*2 == 0){ //only every 2 seconds
				
				if(TeamMap.getInstance().isGameActive()){
					EnumTeam team = TeamMap.getInstance().getTeamForPlayer((EntityPlayer) event.entity);
					if(team != null){
						ByteBuffer buffer = ByteBuffer.allocate(6);
						buffer.put((byte)TeamPacketHandeler.TeamPacketType.SendTeam.ordinal());
						buffer.putInt(event.entity.entityId);
						
						buffer.put((byte)team.ordinal());
						
						Packet250CustomPayload packet = new Packet250CustomPayload(CommanderPacketHandeler.teamPacket, buffer.array());
						
						//Send to eveyone tracking player & to player
						((WorldServer)event.entity.worldObj).getEntityTracker().sendPacketToAllPlayersTrackingEntity(event.entity, packet);
						PacketDispatcher.sendPacketToPlayer(packet, (Player)event.entity);
	
					}
				}
			}
		}
	}
	

	private EntityLiving findUnitToAttack(EntityLiving e, float dist) {
		
		//From EntitAINearestAttackableTarget
		List var5 = e.worldObj.selectEntitiesWithinAABB(EntityMBUnit.class, e.boundingBox.expand((double)dist, 4.0D, (double)dist), null);
		Collections.sort(var5, new MBNearestAttackableSorter(e));
        Iterator var2 = var5.iterator();
        while (var2.hasNext())
        {
            Entity var3 = (Entity)var2.next();
            EntityLiving var4 = (EntityLiving)var3;

            if (var4 instanceof EntityMBUnit && e.getEntitySenses().canSee(var4)){
            	return var4;
            }
        }
		
		return null;
	}
	
	private class MBNearestAttackableSorter implements Comparator{
		
		private Entity theEntity;
		
		public MBNearestAttackableSorter(Entity theEntity){
			this.theEntity=theEntity;
		}
	
		public int compareDistanceSq(Entity par1Entity, Entity par2Entity)
	    {
	        double var3 = this.theEntity.getDistanceSqToEntity(par1Entity);
	        double var5 = this.theEntity.getDistanceSqToEntity(par2Entity);
	        return var3 < var5 ? -1 : (var3 > var5 ? 1 : 0);
	    }
	
	    public int compare(Object par1Obj, Object par2Obj)
	    {
	        return this.compareDistanceSq((Entity)par1Obj, (Entity)par2Obj);
	    }
	}
	
	@ForgeSubscribe
	public void ServerLoad(WorldEvent.Load event){
		if(!event.world.isRemote && 
				(event.world.worldInfo.getDimension() == 0 )){//|| TeamMap.isNotLoaded()){
			TeamMap.load(((SaveHandler)event.world.getSaveHandler()).getSaveDirectory());
		}
	}
	
	@ForgeSubscribe
	public void ServerSave(WorldEvent.Save event){
		if(!event.world.isRemote){
			TeamMap.save(((SaveHandler)event.world.getSaveHandler()).getSaveDirectory());
		}
	}
	
}
