package mab.common.commander;

import java.util.List;

import mab.common.commander.npc.EntityMBUnit;
import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler{

	public void loadLanguages(){}
	
	public void registerRenderInformation(){}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		return null;
	}
	
	
	public void scanForGold(InventoryPlayer inv, int required){
		
		//loop 1 - gold nuggets
		required = scanForNuggets(inv, required);
		
		//loop2 - goldIngots
		if(required > 0){
			expandIngots(inv, required / 9 + 1);
			required = scanForNuggets(inv, required);
			
			if(required > 0){
				expandBlocks(inv, required / (9*9) + 1);
				expandIngots(inv, required / 9 + 1);
				required = scanForNuggets(inv, required);
			}
		}
		
	}
	
	private void expandBlocks(InventoryPlayer inv, int expands) {
		int expandsNeeded = expands;
		for(int i = 0; i < inv.mainInventory.length && expandsNeeded > 0; i++){
			ItemStack stack = inv.mainInventory[i];
			if(stack != null && stack.itemID == Block.blockGold.blockID){
				if(stack.stackSize > expandsNeeded){
					stack.stackSize = stack.stackSize - expandsNeeded;
					inv.addItemStackToInventory(new ItemStack(Item.ingotGold, expandsNeeded*9));
					expandsNeeded = 0;
				}else if(stack.stackSize == expandsNeeded){
					inv.addItemStackToInventory(new ItemStack(Item.ingotGold, expandsNeeded*9));
					expandsNeeded = 0;
					inv.mainInventory[i] = null;
				}else{
					inv.addItemStackToInventory(new ItemStack(Item.ingotGold, stack.stackSize*9));
					expandsNeeded = expandsNeeded - stack.stackSize;
					inv.mainInventory[i] = null;
				}
			}
		}
	}

	private void expandIngots(InventoryPlayer inv, int expands) {
		int expandsNeeded = expands;
		for(int i = 0; i < inv.mainInventory.length && expandsNeeded > 0; i++){
			ItemStack stack = inv.mainInventory[i];
			if(stack != null && stack.itemID == Item.ingotGold.shiftedIndex){
				if(stack.stackSize > expandsNeeded){
					stack.stackSize = stack.stackSize - expandsNeeded;
					inv.addItemStackToInventory(new ItemStack(Item.goldNugget, expandsNeeded*9));
					expandsNeeded = 0;
				}else if(stack.stackSize == expandsNeeded){
					inv.addItemStackToInventory(new ItemStack(Item.goldNugget, expandsNeeded*9));
					expandsNeeded = 0;
					inv.mainInventory[i] = null;
				}else{
					inv.addItemStackToInventory(new ItemStack(Item.goldNugget, stack.stackSize*9));
					expandsNeeded = expandsNeeded - stack.stackSize;
					inv.mainInventory[i] = null;
				}
			}
		}
		
	}

	private int scanForNuggets(InventoryPlayer inv, int required){
		for(int i = 0; i < inv.mainInventory.length && required > 0; i++){
			ItemStack stack = inv.mainInventory[i];
			if(stack != null && stack.itemID == Item.goldNugget.shiftedIndex){
				if(stack.stackSize > required){
					stack.stackSize = stack.stackSize - required;
					required = 0;
				}else if (stack.stackSize == required){
					required = 0;
					inv.mainInventory[i] = null;
				}else{
					required = required - stack.stackSize;
					inv.mainInventory[i] = null;
				}
			}
		}
		return required;
	}

	public void resetSelectedUnits() {
		// TODO Auto-generated method stub
		
	}

	public List<EntityMBUnit> getSelectedUnits() {
		return null;
	}

}

