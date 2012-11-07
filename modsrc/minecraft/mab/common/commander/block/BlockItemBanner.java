package mab.common.commander.block;

import mab.common.commander.EnumTeam;
import mab.common.commander.MBCommander;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class BlockItemBanner extends ItemBlock{
	
	 public BlockItemBanner(int meta)
	 {
		 super(meta);
		 this.setCreativeTab(CreativeTabs.tabDecorations);
		 this.setHasSubtypes(true);
	 }
	 
	 public int getIconFromDamage(int par1)
	 {
		 return MBCommander.INSTANCE.banner.getBlockTextureFromSideAndMetadata(2, par1);
	 }

	public int getPlacedBlockMetadata(int i){
		return i;
	}
	    
	@Override
	public String getItemNameIS(ItemStack itemstack){
		return (new StringBuilder()).append(super.getItemName())
				.append(".").
				append(EnumTeam.values()[itemstack.getItemDamage()].name()).toString();
	}
	
	

	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player,
			World world, int x, int y, int z, 
			int side, float hitX, float hitY, float hitZ, int i) {
		
		if(side == 1){ // on top of a block
			if(world.getBlockId(x, y+1, z) == 0)
			{
				
				boolean placed = super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, i);
				
				world.setBlockAndMetadata(x, y+1, z, MBCommander.INSTANCE.banner.blockID, stack.getItemDamage());
				
				float angle = player.rotationYaw + 45+180;
				while(angle < 0)
					angle =angle+360;
				while (angle>360)
					angle = angle-360;
				
				byte state = (byte) (angle / 45);
				world.setBlockTileEntity(x, y, z, new TileEntityBanner(state, EnumTeam.values()[stack.getItemDamage()]));
				world.setBlockTileEntity(x, y+1, z, new TileEntityBanner((byte) (state+8), EnumTeam.values()[stack.getItemDamage()]));

				return placed;
			}else
				return false;
			
			
		}else if (side != 0){
			
		}
		return false;
		
	}

	@Override
	public int getMetadata(int par1) {
		return par1;
	}
}
