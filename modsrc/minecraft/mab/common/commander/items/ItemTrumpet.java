package mab.common.commander.items;

import java.util.List;

import mab.common.commander.MBCommander;
import mab.common.commander.npc.EntityMBUnit;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class ItemTrumpet extends Item{

	public ItemTrumpet(int id) {
		super(id);
		this.setCreativeTab(CreativeTabs.tabMisc);
		this.setIconIndex(112);
		this.setItemName("trumpet");
		this.setTextureFile(MBCommander.ImageSheet);
		this.setFull3D();
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World,
			EntityPlayer par3EntityPlayer) {

		List<EntityMBUnit> surrounded = par2World.getEntitiesWithinAABB(EntityMBUnit.class, 
				AxisAlignedBB.getBoundingBox(par3EntityPlayer.posX-15, par3EntityPlayer.posY-15, par3EntityPlayer.posZ-15, 
						par3EntityPlayer.posX+15, par3EntityPlayer.posY+15, par3EntityPlayer.posZ+15));
		
		MBCommander.PROXY.selectAllUnitsAround(surrounded, par3EntityPlayer);
		MBCommander.PROXY.reparseOrderGUIOptions();
		
		return par1ItemStack;
	}
	
	

}
