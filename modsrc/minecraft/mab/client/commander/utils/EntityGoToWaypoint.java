package mab.client.commander.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.List;

import mab.common.commander.CommanderPacketHandeler;
import mab.common.commander.npc.EntityMBUnit;
import mab.common.commander.npc.ai.EnumOrder;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.World;

public class EntityGoToWaypoint extends Entity{

	public EntityGoToWaypoint(World par1World) {
		super(par1World);
	}

	@Override
	protected void entityInit() {
		
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound var1) {
		
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound var1) {
		
	}
	
	public Packet setGoToOrder(List<EntityMBUnit> units, EntityPlayer player){
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream(4 + 1 + 1 + units.size() * 4 + 4*3);
		DataOutputStream outputStream = new DataOutputStream(bos);
		try{
			
			outputStream.writeInt(player.entityId);
			
			outputStream.writeByte((byte)EnumOrder.GoTo.ordinal());
			outputStream.writeByte((byte)units.size());
			
			for (EntityMBUnit entityMBUnit : units) {
				outputStream.writeInt(entityMBUnit.entityId);
			}
			
			outputStream.writeInt(MathHelper.floor_double(posX));
			outputStream.writeInt(MathHelper.floor_double(posY));
			outputStream.writeInt(MathHelper.floor_double(posZ));
			
			return new Packet250CustomPayload(CommanderPacketHandeler.orderPacket, bos.toByteArray());
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
