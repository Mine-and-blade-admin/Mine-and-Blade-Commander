package mab.client.commander;

import java.util.EnumSet;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import mab.client.commander.gui.order.GUIOrderMenu;
import mab.client.commander.utils.EntityGoToWaypoint;
import mab.client.commander.utils.MBClientHelper;
import mab.common.commander.EnumTeam;
import mab.common.commander.MBCommander;
import mab.common.commander.npc.EntityMBUnit;
import mab.common.commander.npc.ai.EnumOrder;
import mab.common.commander.utils.TeamMap;
import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.ScaledResolution;
import net.minecraft.src.StringTranslate;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class MBRnderHandeler implements ITickHandler{
	
	public static StringTranslate st = StringTranslate.getInstance();

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		Minecraft mc = FMLClientHandler.instance().getClient();
		
		if(type.contains(TickType.CLIENT)){
			if(mc.currentScreen != null)
				MBCommander.PROXY.resetSelectedUnits();
		}
		
		if(type.contains(TickType.RENDER)){
			GUIOrderMenu orderMenu = ((ClientProxy)MBCommander.PROXY).orderMenu;
			if(orderMenu != null && mc.currentScreen == null){
				orderMenu.renderOverlayRightBox();
				if(! orderMenu.isMainMenu() && orderMenu.getCurrentMenu()[0].equals(EnumOrder.GoToSelect)){
					((ClientProxy)MBCommander.PROXY).spawnAndMoveWaypoint();
				}else{
					((ClientProxy)MBCommander.PROXY).removeWaypoint();
					orderMenu.setSubMenu(null);
				}
			}else{
				((ClientProxy)MBCommander.PROXY).removeWaypoint();
				if(orderMenu!=null)
					orderMenu.setSubMenu(null);
			}
			
			MovingObjectPosition mouseOver = MBClientHelper.getMouseOver(5, 0);
			if(mouseOver != null && mouseOver.entityHit instanceof EntityMBUnit &&
					 FMLClientHandler.instance().getClient().currentScreen == null &&
						!((EntityMBUnit)mouseOver.entityHit).isEnemy(mc.thePlayer, false)){
				String message = st.translateKey("gui.screen.select");
				message = message.replaceAll("@key@", Keyboard.getKeyName(MBKeyHandeler.selectUnit.keyCode));
				message = message.replaceAll("@unit@", StringTranslate.getInstance().translateKey(((EntityMBUnit)mouseOver.entityHit).getUnitName()));

				ScaledResolution sr = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
	            int width = sr.getScaledWidth();
	            int height = sr.getScaledHeight();
				mc.fontRenderer.drawStringWithShadow(message, width/2 - mc.fontRenderer.getStringWidth(message) / 2, height/2 + 30, 0xFFFFFF);
			}
			
			if(FMLClientHandler.instance().getClient().currentScreen == null){
				ScaledResolution sr = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
	            int width = sr.getScaledWidth();
	            int height = sr.getScaledHeight();
	            
	            EnumTeam team = TeamMap.getInstance().getTeamForPlayer(mc.thePlayer);
	            if(team != null){
		            int index = TeamMap.getInstance().getTeamForPlayer(mc.thePlayer).ordinal();
		            
		            int x = index%8*32;
		            int y = index/8*32 + 32*6;
		            
		            GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture(MBCommander.IMAGE_FOLDER+"BigItemSheet.png"));
		            FMLClientHandler.instance().getClient().ingameGUI.drawTexturedModalRect(width - 5- 32, height-5-32, x, y, 32, 32);
	            }
			}
			
		}
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.RENDER, TickType.CLIENT);
	}

	@Override
	public String getLabel() {
		return "Mine & Blade: Render Tick Handeler";
	}

}
