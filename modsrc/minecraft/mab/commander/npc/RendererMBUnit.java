package mab.commander.npc;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

import mab.commander.EnumTeam;
import mab.commander.MBCommander;
import mab.commander.npc.EntityMBUnit;
import mab.commander.npc.EnumUnitItems;
import mab.commander.utils.MBClientHelper;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemArmor;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModelBiped;
import net.minecraft.src.RenderBiped;
import net.minecraft.src.Tessellator;
import net.minecraftforge.client.ForgeHooksClient;

public class RendererMBUnit extends RenderBiped{
	
    private ModelBiped modelBody;

	public RendererMBUnit(ModelBiped par1ModelBiped, float par2) {
		super(par1ModelBiped, par2);
		
		modelBody = new ModelBiped(0F);
		this.setRenderPassModel(modelBody);
		
	}

	private void renderUnit(EntityMBUnit unit, double par2,
				double par4, double par6, float par8, float par9) {
		 

		if(unit.isWeaponsDrawn() && unit.getWeaponOption()!=null){
			this.modelBipedMain.heldItemRight = 1;
			this.modelBody.heldItemRight = 1;
		}else{
			this.modelBipedMain.heldItemRight = 0;
			this.modelBody.heldItemRight = 0;
		}
		
		if(unit.isWeaponsDrawn() && unit.getWeaponOffHandOption()!=null){
			this.modelBipedMain.heldItemLeft = 1;
			this.modelBody.heldItemLeft = 1;
		}else{
			this.modelBipedMain.heldItemLeft = 0;
			this.modelBody.heldItemLeft = 0;
		}
		this.modelBipedMain.bipedHeadwear.showModel = unit.getHelmNumber() > -1;
			
		
		super.doRenderLiving(unit, par2, par4, par6, par8, par9);
		
		if(unit.renderShadow)
			renderInfo(unit, par2, par4, par6, 10);
		
		if(MBCommander.PROXY.getSelectedUnits().contains(unit))
			renderBox(unit, par2, par4, par6);
			 
	}
	 
	 
	 
	 private void renderBox(EntityMBUnit unit, double d, double d1, double d2) {


	      AxisAlignedBB box = AxisAlignedBB.getBoundingBox(
	    		  d - unit.width / 2.0F - 0.2000000029802322D,
	    		  d1, 
	    		  d2 - unit.width / 2.0F - 0.2000000029802322D,
	    		  d + unit.width / 2.0F + 0.2000000029802322D,
	    		  d1 + unit.height + 0.2000000029802322D,
	    		  d2 + unit.width / 2.0F + 0.2000000029802322D);
	      
	      drawBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, unit.getTeam().getRGBA(100), 1.0F);
		
	}

	private void drawBox(double x1, double y1, double z1, double x2,
			double y2, double z2, int colour, float width) {
		System.out.println("drawing box");
		float red = (float)(colour >> 16 & 255) / 255.0F;
        float blue = (float)(colour >> 8 & 255) / 255.0F;
        float green = (float)(colour & 255) / 255.0F;

	    GL11.glEnable(3042);
	    GL11.glBlendFunc(770, 771);
	    GL11.glColor3f(red, green, blue);
	    GL11.glLineWidth(width);
	    GL11.glDisable(3553);
	    GL11.glDepthMask(false);

	    Tessellator o = Tessellator.instance;

	    o.startDrawing(2);
	    o.addVertex(x1, y1, z1);
	    o.addVertex(x2, y1, z1);
	    o.addVertex(x2, y1, z2);
	    o.addVertex(x1, y1, z2);
	    o.draw();

	    o.startDrawing(2);
	    o.addVertex(x1, y2, z1);
	    o.addVertex(x2, y2, z1);
	    o.addVertex(x2, y2, z2);
	    o.addVertex(x1, y2, z2);
	    o.draw();

	    o.startDrawing(1);

	    o.addVertex(x1, y1, z1);
	    o.addVertex(x1, y2, z1);

	    o.addVertex(x2, y1, z1);
	    o.addVertex(x2, y2, z1);

	    o.addVertex(x2, y1, z2);
	    o.addVertex(x2, y2, z2);

	    o.addVertex(x1, y1, z2);
	    o.addVertex(x1, y2, z2);

	    o.draw();

	    GL11.glDepthMask(true);
	    GL11.glEnable(3553);
	    GL11.glDisable(3042);
		
	}

	@Override
	protected void func_82420_a(EntityLiving par1EntityLiving,
			ItemStack par2ItemStack) {
		
	}


	protected int shouldRenderPass(EntityLiving par1EntityLiving, int par2, float par3)
	    {
	        return this.setArmorModel((EntityMBUnit)par1EntityLiving, par2, par3);
	    }
	 
	 /**
	     * Set the specified armor model as the player model. Args: player, armorSlot, partialTick
	     */
	    protected int setArmorModel(EntityMBUnit unit, int par2, float par3)
	    {
	        
	        switch(par2){
	        case 0://body
	        	loadTexture(unit.getSkinFile());
	        	this.setRenderPassModel(modelBody);
	        	return 1;
	        case 1://eyes
	        	loadTexture(unit.getEyesFile());
	        	this.setRenderPassModel(modelBody);
	        	return 1;
	        case 2: //hair
		        loadTexture(unit.getHairFile());
	        	this.setRenderPassModel(modelBody);
	        	return 1;
	        }
	        
	        return -1;
	    }

	
	
	
	@Override
	protected void renderEquippedItems(EntityLiving living, float f) {
        float var3 = 1.0F;
        GL11.glColor3f(var3, var3, var3);
        
		EntityMBUnit unit = (EntityMBUnit)living;
		EnumUnitItems weapon = unit.getWeaponOption();
		EnumTeam team = unit.getTeam();
		int teamIndex = team.ordinal();
		if(weapon != null){
			if(unit.isWeaponsDrawn()){
				GL11.glPushMatrix();
                
				this.modelBody.bipedRightArm.postRender(0.0625F);
	            GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);
	            
	            float var6 = 0.625F;

                GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
                
                if(weapon.isBigSheet()){
                	GL11.glScalef(1F, 1.25F, 1.25F);
                }
                
                GL11.glScalef(var6, -var6, var6);
                GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
	            
                
				MBClientHelper.renderUnitItems(weapon, FMLClientHandler.instance().getClient(), false, teamIndex);
				
				GL11.glPopMatrix();
			}else{
				GL11.glPushMatrix();
				
				if(weapon.isOnBack()){
					this.modelBody.bipedBody.postRender(0.0625F);
					
					float var6 = 0.625F;
					
					GL11.glTranslatef(0F, .5F, .15F);
					GL11.glScalef(var6, -var6, var6);
					
					GL11.glRotatef(90F, 0.0F, 0.0F, 1.0F);
					
					GL11.glRotatef(45.0F+90, 0.0F, 1.0F, 0.0F);
					
					GL11.glRotatef(35F, 1.0F, 0.0F, 1.0F);
					
					MBClientHelper.renderUnitItems(weapon, FMLClientHandler.instance().getClient(), false, teamIndex);
					
				}else{
					
					this.modelBody.bipedBody.postRender(0.0625F);
					GL11.glTranslatef(.26F, .5F, -0.1F);
					float var6 = 0.625F*.85F;
					GL11.glScalef(var6, -var6, var6);
					GL11.glRotatef(-255F, 1.0F, 0.0F, 0.0F);
	                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
					  
					MBClientHelper.renderUnitItems(weapon, FMLClientHandler.instance().getClient(), false, teamIndex);
				}
				
				GL11.glPopMatrix();
			}
		}
		EnumUnitItems offWeapon = unit.getWeaponOffHandOption();
		if(offWeapon != null){
			
			if(unit.isWeaponsDrawn()){
				
				if(offWeapon.isShield()){
					GL11.glPushMatrix();
	                
					this.modelBody.bipedLeftArm.postRender(0.0625F);
					 float var6 = 0.5F;
					 GL11.glTranslatef(-.25F, .65F, -0.12F);
					 GL11.glScalef(-var6, -var6, var6);
					 
					 GL11.glRotatef(-20, 1, 0, 0);
					 
					MBClientHelper.renderUnitItems(offWeapon, FMLClientHandler.instance().getClient(), false, teamIndex);
					
					GL11.glPopMatrix();
				}else{
					GL11.glPushMatrix();
	                
					this.modelBody.bipedLeftArm.postRender(0.0625F);
		            GL11.glTranslatef(0.0625F, 0.4375F, 0.0625F);
		            
		            float var6 = 0.625F;

	                GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
	                
	                GL11.glScalef(var6, -var6, var6);
	                GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
	                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
		            
	                
					MBClientHelper.renderUnitItems(offWeapon, FMLClientHandler.instance().getClient(), false, teamIndex);
					
					GL11.glPopMatrix();
				}
				
			}else{
				
				GL11.glPushMatrix();
				
				if(offWeapon.isOnBack()){
					this.modelBody.bipedBody.postRender(0.0625F);
					
					float var6 = 0.625F;
					
					GL11.glTranslatef(0F, .5F, .15F);
					if(weapon.isOnBack()){
						GL11.glTranslatef(0F, .0F, .05F);
						GL11.glScalef(-1, 1, 1);
					}
					
					if(offWeapon.isShield()){
						var6 = 0.5F;
						GL11.glTranslatef(.3F, 0, .05F);
						GL11.glScalef(var6, -var6, var6);
						MBClientHelper.renderUnitItems(offWeapon, FMLClientHandler.instance().getClient(), false, teamIndex);
					}
					else{
						GL11.glScalef(var6, -var6, var6);
						
						GL11.glRotatef(90F, 0.0F, 0.0F, 1.0F);
						
						GL11.glRotatef(45.0F+90, 0.0F, 1.0F, 0.0F);
						
						GL11.glRotatef(35F, 1.0F, 0.0F, 1.0F);
						
						MBClientHelper.renderUnitItems(offWeapon, FMLClientHandler.instance().getClient(), false, teamIndex);
					}
					
				}else{
					
					this.modelBody.bipedBody.postRender(0.0625F);
					GL11.glTranslatef(-.26F, .5F, -0.1F);
					float var6 = 0.625F*.85F;
					GL11.glScalef(var6, -var6, var6);
					GL11.glRotatef(-255F, 1.0F, 0.0F, 0.0F);
	                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
					  
					MBClientHelper.renderUnitItems(offWeapon, FMLClientHandler.instance().getClient(), false, teamIndex);
				}
				
				GL11.glPopMatrix();
				
			}
			
		}
	}


	public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9)
    {
        this.renderUnit((EntityMBUnit)par1EntityLiving, par2, par4, par6, par8, par9);
    }
	/**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        this.renderUnit((EntityMBUnit)par1Entity, par2, par4, par6, par8, par9);
        
    }

    
    protected void renderInfo(EntityMBUnit unit, double x, double y, double z, int renderDistance){
    	double var10 = unit.getDistanceSqToEntity(this.renderManager.livingPlayer);
    	
    	if(var10 <= renderDistance * renderDistance){
    		FontRenderer var12 = this.getFontRendererFromRenderManager();
            float var13 = 1.6F;
            float var14 = 0.016666668F * var13;
            GL11.glPushMatrix();
            GL11.glTranslatef((float)x + 0.0F, (float)y + 2.3F+ 0.25F, (float)z);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
            GL11.glScalef(-var14, -var14, var14);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDepthMask(false);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            Tessellator var15 = Tessellator.instance;
            byte var16 = 0;
            String order = unit.getDisplayOrder();
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            var15.startDrawingQuads();
            int var17 = var12.getStringWidth(order) / 2;
            var15.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
            var15.addVertex((double)(-var17 - 1), (double)(-1 + var16), 0.0D);
            var15.addVertex((double)(-var17 - 1), (double)(8 + var16), 0.0D);
            var15.addVertex((double)(var17 + 1), (double)(8 + var16), 0.0D);
            var15.addVertex((double)(var17 + 1), (double)(-1 + var16), 0.0D);
            var15.draw();
            
            var15.startDrawingQuads();
            var15.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
            var15.addVertex((double)(-20), (double)(9 + var16), 0.0D);
            var15.addVertex((double)(-20), (double)(13.5 + var16), 0.0D);
            var15.addVertex((double)(20), (double)(13.5 + var16), 0.0D);
            var15.addVertex((double)(20), (double)(9 + var16), 0.0D);
            var15.draw();
            
            var15.startDrawingQuads();
            var15.setColorRGBA_F(0.0F, 0.0F, 0.0F, .75F);
            var15.addVertex((double)(20), (double)(9 + var16), 0.0D);
            var15.addVertex((double)(20), (double)(13.5 + var16), 0.0D);
            var15.addVertex((double)(20.5), (double)(13.5 + var16), 0.0D);
            var15.addVertex((double)(20.5), (double)(9 + var16), 0.0D);
            var15.draw();
            
            var15.startDrawingQuads();
            var15.setColorRGBA_F(0.0F, 0.0F, 0.0F, .75F);
            var15.addVertex((double)(-20.5), (double)(9 + var16), 0.0D);
            var15.addVertex((double)(-20.5), (double)(13.5 + var16), 0.0D);
            var15.addVertex((double)(-20), (double)(13.5 + var16), 0.0D);
            var15.addVertex((double)(-20), (double)(9 + var16), 0.0D);
            var15.draw();
            
            var15.startDrawingQuads();
            var15.setColorRGBA_F(0.0F, 0.0F, 0.0F, .75F);
            var15.addVertex((double)(-20.5), (double)(8.5 + var16), 0.0D);
            var15.addVertex((double)(-20.5), (double)(9 + var16), 0.0D);
            var15.addVertex((double)(20.5), (double)(9 + var16), 0.0D);
            var15.addVertex((double)(20.5), (double)(8.5 + var16), 0.0D);
            var15.draw();
            
            var15.startDrawingQuads();
            var15.setColorRGBA_F(0.0F, 0.0F, 0.0F, .75F);
            var15.addVertex((double)(-20.5), (double)(13.5 + var16), 0.0D);
            var15.addVertex((double)(-20.5), (double)(14 + var16), 0.0D);
            var15.addVertex((double)(20.5), (double)(14 + var16), 0.0D);
            var15.addVertex((double)(20.5), (double)(13.5 + var16), 0.0D);
            var15.draw();
            
            var15.startDrawingQuads();
            var15.setColorRGBA_F(0.0F, 0.0F, 0.0F, .75F);
            var15.addVertex((double)(-20.5), (double)(11 + var16), 0.0D);
            var15.addVertex((double)(-20.5), (double)(11.5 + var16), 0.0D);
            var15.addVertex((double)(20.5), (double)(11.5 + var16), 0.0D);
            var15.addVertex((double)(20.5), (double)(11 + var16), 0.0D);
            var15.draw();
            
            var15.startDrawingQuads();
            var15.setColorRGBA_F(0.0F, 0.0F, 0.0F, .75F);
            var15.addVertex((double)(-20.5), (double)(11 + var16), 0.0D);
            var15.addVertex((double)(-20.5), (double)(11.5 + var16), 0.0D);
            var15.addVertex((double)(20.5), (double)(11.5 + var16), 0.0D);
            var15.addVertex((double)(20.5), (double)(11 + var16), 0.0D);
            var15.draw();
            
            
            float health = (float)unit.getCurrentHealth() / (float)unit.getMaxHealth();
            
            var15.startDrawingQuads();
            var15.setColorRGBA_I(calculateHealthColour(health), 127);
            var15.addVertex((double)(-20), (double)(9 + var16), 0.0D);
            var15.addVertex((double)(-20), (double)(11 + var16), 0.0D);
            var15.addVertex((double)((40*health)-20), (double)(11 + var16), 0.0D);
            var15.addVertex((double)((40*health)-20), (double)(9 + var16), 0.0D);
            var15.draw();
            
            var15.startDrawingQuads();
            float exp = (float)unit.getCurrentExperience() / (float)100;
            var15.setColorRGBA_I(calculateExpColour(exp), 127);
            var15.addVertex((double)(-20), (double)(11.5 + var16), 0.0D);
            var15.addVertex((double)(-20), (double)(13.5 + var16), 0.0D);
            var15.addVertex((double)((40*exp)-20), (double)(13.5 + var16), 0.0D);
            var15.addVertex((double)((40*exp)-20), (double)(11.5 + var16), 0.0D);
            var15.draw();
            
            
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            var12.drawString(order, -var12.getStringWidth(order) / 2, var16, 553648127);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(true);
            var12.drawString(order, -var12.getStringWidth(order) / 2, var16, -1);

            
            GL11.glPopMatrix();
            
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_BLEND);
            
            
    	}
    }
    
    
    /**
     * Draws lines for the edges of the bounding box.
     */
    private void drawOutlinedBoundingBox(Tessellator t, AxisAlignedBB par1AxisAlignedBB)
    {
        Tessellator var2 = Tessellator.instance;
        var2.startDrawing(3);
        var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
        var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
        var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
        var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
        var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
        var2.draw();
        var2.startDrawing(3);
        var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
        var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
        var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
        var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
        var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
        var2.draw();
        var2.startDrawing(1);
        var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
        var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
        var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
        var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
        var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
        var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
        var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
        var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
        var2.draw();
    }

    
    private int calculateHealthColour(float healthPercent){
    	int health =  (int)Math.round((healthPercent)* 255);
    	return 255 - health << 16 | health << 8;
    }
    
    private int calculateExpColour(float expPercentage){
    	int exp =  (int)Math.round((1-expPercentage)* 255);
    	
    	
    	if(expPercentage == 1 && System.currentTimeMillis() / 500 % 2 == 0){
    		return 255 << 8 | 255 << 16;
    	}
    	else if(expPercentage == 0.5)
    		return 255;
    	else if(expPercentage < 0.5)
    		return 255 | (exp - 128) << 16;
    	else
    		return 255 | 2* ((127 - exp) << 8);
    }
	
    
    

}
