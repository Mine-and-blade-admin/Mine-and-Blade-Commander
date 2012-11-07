package mab.commander.block;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.nio.ByteBuffer;

import mab.commander.CommanderPacketHandeler;
import mab.commander.EnumTeam;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.TileEntity;

public class TileEntityBanner extends TileEntity{
	
	//0-7 (on ground top)
	//8-15 (on ground base)
	//16-19 (on wall top)
	//20-24 (on wall base)
	private byte state;
	private EnumTeam team;
	
	public TileEntityBanner(){
		state = 0;
		team = EnumTeam.black;
	}

	public TileEntityBanner(byte state, EnumTeam team) {
		super();
		this.state = state;
		this.team = team;
	}

	public boolean isBase() {
		return state < 8 || (state>15 && state< 20);
	}
	
	public boolean isTop(){
		return !(isBase());
	}
	
	public boolean isOnGround(){
		return state < 16;
	}
	public boolean isOnWall(){
		return state > 15;
	}

	public int getState() {
		return state;
	}

	public void setState(byte state) {
		this.state = state;
	}

	public EnumTeam getTeam() {
		return team;
	}

	public void setTeam(EnumTeam team) {
		this.team = team;
	}
	
	public void setTeam(byte teamIndex) {
		this.team = EnumTeam.values()[teamIndex];
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound) {
		super.readFromNBT(nbtTagCompound);
		state = nbtTagCompound.getByte("state");
		setTeam(nbtTagCompound.getByte("team"));
	}

	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound) {
		super.writeToNBT(nbtTagCompound);
		nbtTagCompound.setByte("state", state);
		nbtTagCompound.setByte("team", (byte)team.ordinal());
		
		
	}
	
	@Override
	public Packet getDescriptionPacket() {
		
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream(14);
		DataOutputStream outputStream = new DataOutputStream(bos);
		try {
			
			outputStream.writeInt(xCoord);
			outputStream.writeInt(yCoord);
			outputStream.writeInt(zCoord);
			
			outputStream.writeByte(state);
			outputStream.writeByte((byte) team.ordinal());
			
		
			return new Packet250CustomPayload(CommanderPacketHandeler.BannerPacket, bos.toByteArray());
			
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	

	
	
	
	
	

}
