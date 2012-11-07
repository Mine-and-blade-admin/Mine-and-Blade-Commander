package mab.client.commander.gui.order;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.List;

import javax.print.attribute.standard.Media;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.network.PacketDispatcher;

import mab.common.commander.CommanderPacketHandeler;
import mab.common.commander.MBCommander;
import mab.common.commander.npc.EntityMBUnit;
import mab.common.commander.npc.ai.EnumOrder;
import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.Gui;
import net.minecraft.src.GuiNewChat;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.ScaledResolution;
import net.minecraft.src.StringTranslate;

public class GUIOrderMenu extends Gui{
	
	private static int HIDEN = 0;
	private static int DISPLAYED = 1;
	private static int SHOWING = 2;
	private static int HIDING = 3;
	
	private Minecraft mc;
	private FontRenderer fontRenderer;
	
	private float out = 0;
	private int orderMenuState = HIDEN;
	
	private int selected = 0;
	private EnumOrder[] orders = new EnumOrder[]{EnumOrder.StandGuard, EnumOrder.Follow};
	
	private long lastRender = 0;
	
	public GUIOrderMenu(Minecraft par1Minecraft)
    {
        this.mc = par1Minecraft;
        fontRenderer = mc.fontRenderer;
    }
	
	
    public void renderOverlayLeftTexturedBox()
    {
    	
    	System.out.println(orderMenuState);
    	if(isShowing()){
    		out = out + ((float)(System.currentTimeMillis() - lastRender)) / 500f;
    	}else if(isHiding()){
    		out = Math.max(0, out - ((float)(System.currentTimeMillis() - lastRender)) / 500f);
    	}
    	
    	if(out >= 1){
    		orderMenuState = DISPLAYED;
    		out = 1;
    	}
    	else if (out <= 0){
    		orderMenuState = HIDEN;
    		out = 0;
    	}
    		
    	if(!isHidden()){
    		
    		ScaledResolution sr = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
            int width = sr.getScaledWidth();
            int height = sr.getScaledHeight();
            
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/extendedGUI/image/GUI Controls.png"));
            drawTexturedModalRect(0, 75, 125 - (int)(125*out), 213, (int)(125*out), 20);
            for(int i = 0; i < orders.length; i++){
            	 drawTexturedModalRect(0, 95+17*i, 125 - (int)(125*out), 233, (int)(125*out), 17);
            }
            drawTexturedModalRect(0, 95+17*orders.length, 125 - (int)(125*out), 250, (int)(125*out), 6);
            
            String message = StringTranslate.getInstance().translateKey("gui.order.label")+" ("+MBCommander.PROXY.getSelectedUnits().size()+")";
            
            fontRenderer.drawString(message, (int)(125*out) - (125 + fontRenderer.getStringWidth(message))/2 , 81, 0x404040);
    		
    		for(int i = 0; i < orders.length; i++){
    			int colour = 0xFFFFFF;
    			if(selected == i)
    				colour = 0xFFFF00;
    			drawString(fontRenderer, orders[i].getTranslatedLabel(null), (int)(125*out)+15 - 125, 99+17*i, colour);    			
    		}
    		
    		
            
    		
    		
    		
    		
            drawRect(width - (int)(120*out), 75, width, orders.length*17 + 20 + 75, 127<<24);
            
    		drawString(fontRenderer, message, (int)(width - 120*out) + 60 - fontRenderer.getStringWidth(message)/2, 77, 0xFFFFFF);
    		for(int i = 0; i < orders.length; i++){
    			int colour = 0xAAAAAA;
    			if(selected == i)
    				colour = 0xFFFF00;
    			drawString(fontRenderer, orders[i].getTranslatedLabel(null), (int)(width - 120*out)+10, 97+17*i, colour);    			
    		}
    		
    	}
    	
    	if(MBCommander.PROXY.getSelectedUnits().size() < 1)
    		hide();
    	
    	
    	lastRender = System.currentTimeMillis();
    }
    
    public void renderOverlayRightBox()
    {
    	
    	System.out.println(orderMenuState);
    	if(isShowing()){
    		out = out + ((float)(System.currentTimeMillis() - lastRender)) / 300f;
    	}else if(isHiding()){
    		out = Math.max(0, out - ((float)(System.currentTimeMillis() - lastRender)) / 300f);
    	}
    	
    	if(out >= 1){
    		orderMenuState = DISPLAYED;
    		out = 1;
    	}
    	else if (out <= 0){
    		orderMenuState = HIDEN;
    		out = 0;
    	}
    		
    	if(!isHidden()){
    		
    		ScaledResolution sr = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
            int width = sr.getScaledWidth();
            int height = sr.getScaledHeight();
            
            drawRect(width - (int)(120*out), 75, width, orders.length*17 + 20 + 75, 127<<24);
            
            String message = StringTranslate.getInstance().translateKey("gui.order.label")+" ("+MBCommander.PROXY.getSelectedUnits().size()+")";
    		drawString(fontRenderer, message, (int)(width - 120*out) + 60 - fontRenderer.getStringWidth(message)/2, 80, 0xFFFFFF);
    		for(int i = 0; i < orders.length; i++){
    			int colour = 0xAAAAAA;
    			if(selected == i)
    				colour = 0xFFFF00;
    			drawString(fontRenderer, orders[i].getTranslatedLabel(null), (int)(width - 120*out)+10, 97+17*i, colour);    			
    		}
    	}
    	
    	
    	lastRender = System.currentTimeMillis();
    }
    
    public void setOrders(EnumOrder[] orders){
    	this.orders = orders;
    }
    
    public void applySelectedOrder(List<EntityMBUnit> units, EntityPlayer player){
    	
    	if(selected >= 0 && selected < orders.length){
    		Packet packet = null;
    		try{
    			// orderID
    			// array size
    			// 
    			ByteArrayOutputStream bos = new ByteArrayOutputStream(4 + 1 + 1 + units.size() * 4);
				DataOutputStream outputStream = new DataOutputStream(bos);
				
		    	switch (orders[selected]) {
				case Follow:
				case StandGuard:
					
					outputStream.writeInt(player.entityId);
					
					outputStream.writeByte((byte)orders[selected].ordinal());
					outputStream.writeByte((byte)units.size());
					
					for (EntityMBUnit entityMBUnit : units) {
						outputStream.writeInt(entityMBUnit.entityId);
					}
					
					packet = new Packet250CustomPayload(CommanderPacketHandeler.orderPacket, bos.toByteArray());
					
					break;
				default:
					break;
				}
		    	
		    	if(packet != null){
		    		PacketDispatcher.sendPacketToServer(packet);
		    	}
		    	
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    	}
    	
    }


	public boolean isDisplayed() {
		return orderMenuState == DISPLAYED;
	}
	
	public boolean isHidden(){
		return orderMenuState == HIDEN;
	}
	
	public boolean isHiding(){
		return orderMenuState == HIDING;
	}
	
	public boolean isShowing(){
		return orderMenuState == SHOWING;
	}

	public void show(){
		if(isHidden() || isHiding()){
			lastRender = System.currentTimeMillis() - 1;
			selected = 0;
			orderMenuState = SHOWING;
		}
	}
	
	public void hide(){
		if(isDisplayed() || isShowing()){
			lastRender = System.currentTimeMillis() - 1;
			orderMenuState = HIDING;
		}
	}

	public void moveSelectionUp(){
		selected = selected -1;
		if(selected < 0)
			selected = orders.length-1;
	}
	
	public void moveSelectionDown(){
		selected = selected + 1;
		if(selected >= orders.length)
			selected = 0;
	}

	
	
}
