package mab.client.commander.gui.order;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.List;

import javax.print.attribute.standard.Media;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.network.PacketDispatcher;

import mab.client.commander.ClientProxy;
import mab.client.commander.utils.EntityGoToWaypoint;
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
	private EnumOrder[] orders = new EnumOrder[]{};
	private EnumOrder[] subMenu = null;
	
	private static EnumOrder[] gotoSubMenu = new EnumOrder[]{EnumOrder.GoToSelect, EnumOrder.GoToCancel};

	private long lastRender = 0;
	
	public GUIOrderMenu(Minecraft par1Minecraft)
    {
        this.mc = par1Minecraft;
        fontRenderer = mc.fontRenderer;
    }
	
    public void renderOverlayRightBox()
    {
    	
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
    		EnumOrder[] menu = getCurrentMenu();
    		
    		ScaledResolution sr = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
            int width = sr.getScaledWidth();
            int height = sr.getScaledHeight();
            
            drawRect(width - (int)(120*out), 75, width, menu.length*17 + 20 + 75, 127<<24);
            
            String message = StringTranslate.getInstance().translateKey("gui.order.label")+" ("+MBCommander.PROXY.getSelectedUnits().size()+")";
    		drawString(fontRenderer, message, (int)(width - 120*out) + 60 - fontRenderer.getStringWidth(message)/2, 80, 0xFFFFFF);
    		for(int i = 0; i < menu.length; i++){
    			int colour = 0xAAAAAA;
    			if(selected == i)
    				colour = 0xFFFF00;
    			drawString(fontRenderer, menu[i].getTranslatedLabel(null), (int)(width - 120*out)+10, 97+17*i, colour);    			
    		}
    	}
    	
    	
    	lastRender = System.currentTimeMillis();
    	
    	if(MBCommander.PROXY.getSelectedUnits().size() < 1)
    		hide();
    	
    	GL11.glColor3f(1F, 1F, 1F);
    }
    
    
    public void renderOverlayLeftBox()
    {
    	
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
    		
    		EnumOrder[] menu = getCurrentMenu();
    		
    		ScaledResolution sr = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
            int width = sr.getScaledWidth();
            int height = sr.getScaledHeight();
            
            drawRect(0, 75, (int)(125*out), orders.length*17 + 20 + 75, 127<<24);
            
            String message = StringTranslate.getInstance().translateKey("gui.order.label")+" ("+MBCommander.PROXY.getSelectedUnits().size()+")";
    		drawString(fontRenderer, message, (int)(120*out) + 60 - fontRenderer.getStringWidth(message)/2-125, 80, 0xFFFFFF);
    		for(int i = 0; i < orders.length; i++){
    			int colour = 0xAAAAAA;
    			if(selected == i)
    				colour = 0xFFFF00;
    			drawString(fontRenderer, orders[i].getTranslatedLabel(null), (int)(120*out)+10-125, 97+17*i, colour);    			
    		}
    	}
    	
    	
    	lastRender = System.currentTimeMillis();
    	
    	if(MBCommander.PROXY.getSelectedUnits().size() < 1)
    		hide();
    	
    	GL11.glColor3f(1F, 1F, 1F);
    }
    
    public void setOrders(EnumOrder[] orders){
    	this.orders = orders;
    	this.subMenu = null;
    }
    
    public void setSubMenu(EnumOrder current){
    	if(current==null)
    		subMenu = null;
    	else{
	    	switch (current) {
			case GoTo:
				selected = 0;
				subMenu = gotoSubMenu;
				break;
			default:
				subMenu = null;
			}
    	}
    }
    
    public void applySelectedOrder(List<EntityMBUnit> units, EntityPlayer player){
    	
    	EnumOrder[] menu = getCurrentMenu();
		
    	if(selected >= 0 && selected < menu.length){
    		Packet packet = null;
    		try{
    			// orderID
    			// array size
    			// 
		    	switch (menu[selected]) {
				case Follow:
				case StandGuard:
					ByteArrayOutputStream bos = new ByteArrayOutputStream(4 + 1 + 1 + units.size() * 4);
					DataOutputStream outputStream = new DataOutputStream(bos);
					outputStream.writeInt(player.entityId);
					
					outputStream.writeByte((byte)orders[selected].ordinal());
					outputStream.writeByte((byte)units.size());
					for (EntityMBUnit entityMBUnit : units) {
						outputStream.writeInt(entityMBUnit.entityId);
					}
					
					packet = new Packet250CustomPayload(CommanderPacketHandeler.orderPacket, bos.toByteArray());
					MBCommander.PROXY.resetSelectedUnits();
					break;
				case Upgrade:
					player.openGui(MBCommander.INSTANCE, 1, player.worldObj, units.get(0).entityId, -1, -1);
					MBCommander.PROXY.resetSelectedUnits();
					break;
				case GoTo:
					setSubMenu(EnumOrder.GoTo);
					break;
				case GoToSelect:
					EntityGoToWaypoint waypoint = ((ClientProxy)MBCommander.PROXY).waypoint;
					if( waypoint != null){
						packet = waypoint.setGoToOrder(units, player);
						((ClientProxy)MBCommander.PROXY).removeWaypoint();
						MBCommander.PROXY.resetSelectedUnits();
						this.setSubMenu(null);
					}
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
			selected = getCurrentMenu().length-1;
	}
	
	public void moveSelectionDown(){
		selected = selected + 1;
		if(selected >= getCurrentMenu().length)
			selected = 0;
	}

	public EnumOrder[] getCurrentMenu(){
		if(subMenu == null)
			return orders;
		else
			return subMenu;
	}
	
	public boolean isMainMenu(){
		return subMenu == null;
	}
	
}
