package mab.commander;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ModelBiped;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraftforge.client.MinecraftForgeClient;
import mab.commander.block.TileEntityBanner;
import mab.commander.block.TileEntityBannerRenderer;
import mab.commander.gui.GUISpawn;
import mab.commander.lang.LanguageHelper;
import mab.commander.npc.EntityMBKnight;
import mab.commander.npc.EntityMBMilitia;
import mab.commander.npc.RendererMBKnight;
import mab.commander.npc.RendererMBUnit;

public class ClientProxy extends CommonProxy{

	@Override
	public void loadLanguages(){
		LanguageHelper.loadAllLanguages();
	}
	
	@Override
	public void registerRenderInformation(){
		MinecraftForgeClient.preloadTexture(MBCommander.ImageSheet);
		RenderingRegistry.instance().registerEntityRenderingHandler(EntityMBMilitia.class, new RendererMBUnit(new ModelBiped(.1F), 0));   
		RenderingRegistry.instance().registerEntityRenderingHandler(EntityMBKnight.class, new RendererMBKnight(new ModelBiped(.1F), 0));   
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBanner.class, new TileEntityBannerRenderer());
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		switch(ID){
			case 0:
				TileEntity tile = world.getBlockTileEntity(x, y, z);
				if(tile != null && tile instanceof TileEntityBanner)
					return new GUISpawn((TileEntityBanner) tile);
				else
					return null;
			default:
				return null;
		}
	}
	
	
	
}
