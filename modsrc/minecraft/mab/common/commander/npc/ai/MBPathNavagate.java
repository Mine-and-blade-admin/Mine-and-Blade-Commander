package mab.common.commander.npc.ai;

import net.minecraft.src.EntityLiving;
import net.minecraft.src.PathNavigate;
import net.minecraft.src.World;

public class MBPathNavagate extends PathNavigate{

	public MBPathNavagate(EntityLiving par1EntityLiving, World par2World,
			float par3) {
		super(par1EntityLiving, par2World, par3);
	}

}
