package mab.common.commander;

import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityDamageSource;

public class DamageSourceArmourPenatrate extends EntityDamageSource{

	public DamageSourceArmourPenatrate(Entity par2Entity) {
		super("mob", par2Entity);
		this.setDamageBypassesArmor();
	}


}
