package mab.commander.npc.ai;

import mab.commander.npc.EntityMBUnit;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.StringTranslate;

public enum EnumOrder {
	
	StandGuard("order.stand_guard"),
	Follow("order.follow");
	
	private String label;
	private static StringTranslate st = StringTranslate.getInstance();
	
	private EnumOrder(String label){
		this.label = label;
	}
	
	public String getLabel(){
		return label;
	}
	
	public String getTranslatedLabel(String postfix){
		if(postfix == null)
			return st.translateKey(label);
		else
			return st.translateKey(label)+" ("+postfix+")";
	}
	
	
	public void setOrderFromPacketData(EntityMBUnit unit, EntityPlayer player){
		switch (this) {
		case Follow:
			unit.setOrder(this, new int[]{-1, -1, -1}, player.username);
			break;
		case StandGuard:
			unit.setOrder(this, new int[]{
					(int)Math.floor(unit.posX),
					(int)Math.floor(unit.posY),
					(int)Math.floor(unit.posZ)
				}, "");
			break;
		default:
			break;
		}
	}

}
