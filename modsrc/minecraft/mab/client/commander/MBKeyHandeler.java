package mab.client.commander;

import java.util.EnumSet;
import java.util.List;

import org.lwjgl.input.Keyboard;

import mab.client.commander.gui.order.GUIOrderMenu;
import mab.client.commander.utils.MBClientHelper;
import mab.common.commander.MBCommander;
import mab.common.commander.npc.EntityMBUnit;
import net.minecraft.client.Minecraft;
import net.minecraft.src.KeyBinding;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;

public class MBKeyHandeler extends KeyHandler{
	
	public static final KeyBinding selectUnit = new KeyBinding("Select Unit", Keyboard.KEY_X);
	public static final KeyBinding up = new KeyBinding("Menu Up", Keyboard.KEY_UP);
	public static final KeyBinding altDown = new KeyBinding("Order Menu Navagate", Keyboard.KEY_Z);
	public static final KeyBinding down = new KeyBinding("Menu Down", Keyboard.KEY_DOWN);
	public static final KeyBinding cancel = new KeyBinding("Cancel", Keyboard.KEY_C);
	public static final KeyBinding selectOrder = new KeyBinding("Select Order", Keyboard.KEY_RETURN);
	
	public static Minecraft mc;
	
	public MBKeyHandeler() {
		super(new KeyBinding[]{selectUnit, up, down, altDown, cancel, selectOrder}, new boolean[]{false, false, false, false, false, false});
	}

	@Override
	public String getLabel() {
		return "Mine & Blade: Keybindings";
	}

	@Override
	public void keyDown(EnumSet<TickType> types, KeyBinding kb,
			boolean tickEnd, boolean isRepeat) {

		if(mc == null)
			mc = FMLClientHandler.instance().getClient();
		
		GUIOrderMenu menu = getOrderMenu();
		
		if(tickEnd){
			
			if(kb == selectUnit){
				List<EntityMBUnit> selected = MBCommander.PROXY.getSelectedUnits();
				EntityMBUnit unit = MBClientHelper.getUnitMouseOver(5, 0, mc.thePlayer);
				
				if(unit != null){
					if(!selected.contains(unit)){
						selected.add(unit);
						MBCommander.PROXY.reparseOrderGUIOptions();
						menu.show();
					}else{
						selected.remove(unit);
						MBCommander.PROXY.reparseOrderGUIOptions();
						if(selected.isEmpty())
							menu.hide();
					}
				}else{
					//MBCommander.PROXY.resetSelectedUnits();
					//menu.hide();
				}
			}else if (kb == up){
				if(menu.isDisplayed() || menu.isShowing()){
					menu.moveSelectionUp();
				}
			}else if (kb == down || kb == altDown){
				if(menu.isDisplayed() || menu.isShowing()){
					menu.moveSelectionDown();
				}
			}else if (kb == selectOrder){
				menu.applySelectedOrder(MBCommander.PROXY.getSelectedUnits(), FMLClientHandler.instance().getClient().thePlayer);				
			}else if (kb == cancel){
				MBCommander.PROXY.resetSelectedUnits();
				menu.hide();
			}

		}
		
	}

	private void handelSelect() {
		GUIOrderMenu orderMenu = getOrderMenu();
	}
	
	private static GUIOrderMenu getOrderMenu(){
		if(((ClientProxy)MBCommander.PROXY).orderMenu == null){
			((ClientProxy)MBCommander.PROXY).orderMenu = new GUIOrderMenu(mc);
		}
		return ((ClientProxy)MBCommander.PROXY).orderMenu;
	}

	@Override
	public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd) {
		
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.RENDER);
	}

}
