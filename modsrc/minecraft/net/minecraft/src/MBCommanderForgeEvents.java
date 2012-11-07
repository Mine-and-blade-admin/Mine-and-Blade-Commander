package net.minecraft.src;

import java.util.List;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import mab.common.commander.npc.EntityMBUnit;
import mab.common.commander.npc.melee.EntityMBMilitia;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.living.LivingSpecialSpawnEvent;

public class MBCommanderForgeEvents {

	@ForgeSubscribe
	public void livingSpawnEvent(EntityJoinWorldEvent event){
		
		if(event.entity instanceof IMob){
			EntityLiving living = (EntityLiving)event.entity;
			if(! (living instanceof EntityCreeper) && living != null){
					living.tasks.addTask(2, new EntityAIAttackOnCollide(living, EntityMBUnit.class, .3F, true));
					living.targetTasks.addTask(2, new EntityAINearestAttackableTarget(living, EntityMBUnit.class, 16.0F, 0, true));
			}
			
		}
	}
	
}
