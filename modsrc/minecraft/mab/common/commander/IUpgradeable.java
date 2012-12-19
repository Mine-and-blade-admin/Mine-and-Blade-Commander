package mab.common.commander;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Packet250CustomPayload;
import mab.common.commander.npc.EntityMBUnit;
import mab.common.commander.npc.EnumUnits;

public interface IUpgradeable {
	public EnumUnits[] getUpgrades();
	
	public EnumTeam getTeam();
	
	public int getFirstEditableOptionIndex();
	
	public Packet250CustomPayload generatePacket(EntityMBUnit unit, EntityPlayer player);
	
	public EntityMBUnit getDefaltUnit();
	
	public byte getDefaultOption(int i);
}
