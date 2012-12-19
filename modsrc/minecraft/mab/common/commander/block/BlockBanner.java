package mab.common.commander.block;

import java.util.List;

import mab.common.commander.EnumTeam;
import mab.common.commander.MBCommander;
import mab.common.commander.utils.CommonHelper;
import mab.common.commander.utils.TeamMap;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumCreatureType;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class BlockBanner extends BlockContainer{

	public BlockBanner(int id) {
		super(id, Material.cloth);
		this.setBlockName("banner");
	    this.setTextureFile(MBCommander.ImageSheet);
	    this.setCreativeTab(CreativeTabs.tabDecorations);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityBanner();
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityBanner((byte)0, EnumTeam.values()[meta]);
	}
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	 
	 public int getRenderType()
	 {
	        return -1;
	 }

	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
	 */
	public void getSubBlocks(int itemID, CreativeTabs creativeTabs, List list)
	{
		for (int i = 0; i < 16; ++i)
		{
			list.add(new ItemStack(itemID, 1, i));
		}
	}
	
	/**
	  * Returns the image associated with the block, taking into considerating its metadata and side </br>
	  * </br>
	  * For the banner only the metadata is used. Furthermore the image is only used for
	  * the image that appears in the inventory as the banners have a custom renderer & model
	  * 
	  * @param side the side of the block (not used)
	  * @param meta The metadata associated with the block.
	  * @return The integer associated with the image used in minecraft
	  */
	 @Override
	 public int getBlockTextureFromSideAndMetadata(int side, int meta) {
		 return meta%4+12 + 16*(meta/4);
	 }
	 
	 /**
	  * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
	  */
	 public boolean canPlaceBlockAt(World world, int x, int y, int z)
	 {
		 if (y >= 255)
		 {
			 return false;
		 }
		    
		 return (world.getBlockId(x, y-1, z) != this.blockID && world.getBlockId(x, y+1, z) != this.blockID) 
				 && (world.getBlockId(x, y+1, z) == 0 || world.getBlockId(x, y-1, z) == 0);
	 }
	 
	 /**
	  * Prevents creatures from spawning on top of banners
	  */
	 @Override
	 public boolean canCreatureSpawn(EnumCreatureType type, World world, int x, int y, int z) {
			return false;
	 }
	 

	@Override
	public int damageDropped(int i) {
		return i;
	}

	/**
	  * Will remove the other section of the banner when it is destroyed
	  */
	 @Override
	 public void breakBlock(World world, int x, int y, int z, int p, int a){
		 
		 TileEntity tile = world.getBlockTileEntity(x, y, z);

		 if(tile != null && tile instanceof TileEntityBanner){
			TileEntityBanner banner = (TileEntityBanner)tile;
			
			//if it is a base, remove the top
			if(banner.isBase()){
				world.setBlockWithNotify(x, y + 1, z, 0);
				world.setBlockTileEntity(x, y + 1, z, null);
			}else{ // if not remove the base
				world.setBlockWithNotify(x, y - 1, z, 0);
				world.setBlockTileEntity(x, y - 1, z, null);
			}
			
		 }
		 
		//remove self
		world.setBlockTileEntity(x, y, z, null);
		
		super.breakBlock(world, x, y, z, p, a);
	 }

	@Override
	public boolean onBlockActivated(World par1World, int x, int y,
			int z, EntityPlayer par5EntityPlayer, int par6, float par7,
			float par8, float par9) {
		
		if(par1World.isRemote){
			if(CommonHelper.isTeamGame())
				if(TeamMap.getInstance().isOnSameTeam(par5EntityPlayer, EnumTeam.values()[par1World.getBlockMetadata(x, y, z)]))
					par5EntityPlayer.openGui(MBCommander.INSTANCE, 0, par1World, x, y, z);
				else
					return false;
			else
				par5EntityPlayer.openGui(MBCommander.INSTANCE, 0, par1World, x, y, z);
		}
		
		return true;
	}
	 
	 

	
	 
	 

}
