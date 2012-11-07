package mab.commander.npc;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import mab.commander.MBCommander;
import mab.commander.npc.EntityMBUnit;
import mab.commander.utils.MBClientHelper;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.ModelBiped;

public class RendererMBKnight extends RendererMBUnit{

	public RendererMBKnight(ModelBiped par1ModelBiped, float par2) {
		super(par1ModelBiped, par2);
	}

	@Override
	protected void renderEquippedItems(EntityLiving living, float f) {
		super.renderEquippedItems(living, f);
		
		EntityMBUnit unit = (EntityMBUnit)living;
		int helm = unit.getHelmNumber();
		GL11.glPushMatrix();
		
		this.modelBipedMain.bipedHead.postRender(0.0625F);
		float var6 = 0.625F;
		GL11.glScaled(var6, -var6, -var6);
		
		switch(helm){
		case 2: //plume
			GL11.glTranslated(-0.05, var6*18/10, -var6/10*8);
			GL11.glRotatef(-90, 0, 1, 0);
			MBClientHelper.renderImageAsItem(MBCommander.ImageSheet, 144+unit.getTeam().ordinal(), 1, 
					FMLClientHandler.instance().getClient(), false, false);
			break;
		case 3: //crest
			GL11.glTranslated(-.05, var6*18/10-.7, -var6/10*8+.75);
			GL11.glRotatef(-90, 0, 1, 0);
			MBClientHelper.renderImageAsItem(MBCommander.ImageSheet, 174, 1, 
					FMLClientHandler.instance().getClient(), false, false);
			GL11.glTranslated(0, 0, .1);
			MBClientHelper.renderImageAsItem(MBCommander.ImageSheet, 128, 3, 
					FMLClientHandler.instance().getClient(), false, false);
			break;
		case 4: //Horns, not in use (looks dumb)
			GL11.glTranslated(var6, var6*16/10, 0);
			MBClientHelper.renderImageAsItem(MBCommander.ImageSheet, 175+unit.getTeam().ordinal(), 1, 
					FMLClientHandler.instance().getClient(), false, false);
		}
		
		GL11.glPopMatrix();
	}
	
	

}
