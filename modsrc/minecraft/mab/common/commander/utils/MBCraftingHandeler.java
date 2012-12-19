package mab.common.commander.utils;

import java.security.cert.CRLSelector;
import java.util.List;

import cpw.mods.fml.common.ICraftingHandler;

import mab.common.commander.block.BlockItemBanner;
import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ICrafting;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;

public class MBCraftingHandeler implements ICraftingHandler{

	@Override
	public void onCrafting(EntityPlayer player, ItemStack item,
			IInventory craftMatrix) {
		
		if(item.getItem() instanceof BlockItemBanner && CommonHelper.isTeamGame()){
			item.setItemDamage(TeamMap.getInstance().getTeamForPlayer(player).ordinal());
		}
		
	}

	@Override
	public void onSmelting(EntityPlayer player, ItemStack item) {
		// TODO Auto-generated method stub
		
	}

	

}
