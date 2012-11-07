package mab.client.commander;

import java.util.EnumSet;

import mab.client.commander.utils.MBClientHelper;
import mab.common.commander.MBCommander;
import mab.common.commander.npc.EntityMBUnit;
import net.minecraft.client.Minecraft;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.ScaledResolution;
import net.minecraft.src.StringTranslate;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class MBRnderHandeler implements ITickHandler{

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
			if(((ClientProxy)MBCommander.PROXY).orderMenu != null && 
					mc.currentScreen == null)
				
				((ClientProxy)MBCommander.PROXY).orderMenu.renderOverlayRightBox();
			
			MovingObjectPosition mouseOver = MBClientHelper.getMouseOver(10, 0);
			if(mouseOver != null && mouseOver.entityHit instanceof EntityMBUnit && FMLClientHandler.instance().getClient().currentScreen == null){
				String message = 
						"Press X to Select "+						
						StringTranslate.getInstance().translateKey(((EntityMBUnit)mouseOver.entityHit).getUnitName());
				
				ScaledResolution sr = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
	            int width = sr.getScaledWidth();
	            int height = sr.getScaledHeight();
				mc.fontRenderer.drawStringWithShadow(message, width/2 - mc.fontRenderer.getStringWidth(message) / 2, height/2 + 30, 0xFFFFFF);
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
