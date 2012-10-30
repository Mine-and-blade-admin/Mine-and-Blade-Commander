package mab.commander.block;

import mab.commander.MBCommander;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

import org.lwjgl.opengl.GL11;

public class TileEntityBannerRenderer extends TileEntitySpecialRenderer{

	ModelBanner bannerModel;
	
	
	public TileEntityBannerRenderer() {
		super();
		this.bannerModel = new ModelBanner();
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d, double d1,
			double d2, float f) {

		TileEntityBanner banner = (TileEntityBanner)tileentity;
		
		if(banner.isBase()){
			if(banner.isOnGround()){
				 GL11.glPushMatrix();
			     float scale = 0.6666667F;
			     
			     GL11.glTranslatef((float)d + 0.5F, (float)d1 + 1.05F, (float)d2 + 0.5F);
			     float angle = 45 * banner.getState();

		         GL11.glRotatef(-angle, 0.0F, 1.0F, 0.0F);
		         
		         bindTextureByName(MBCommander.IMAGE_FOLDER+"/banners/Banner-"+banner.getTeam().ordinal()+".png");
		         
		         if (! banner.isOnGround()){
		        	 GL11.glTranslatef(0, 0, -.45F);
		         }
		         
		         GL11.glPushMatrix();
		         GL11.glScalef(scale, -scale, -scale);
		         bannerModel.render();
		         GL11.glPopMatrix();
		        
		         
		         GL11.glPopMatrix();
			}
		}

	}
}
