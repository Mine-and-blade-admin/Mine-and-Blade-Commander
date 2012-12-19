package mab.client.commander.npc;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

import mab.client.commander.utils.MBClientHelper;
import mab.common.commander.EnumTeam;
import mab.common.commander.MBCommander;
import mab.common.commander.npc.EntityMBUnit;
import mab.common.commander.npc.EnumUnitItems;
import mab.common.commander.npc.EnumUnits;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemArmor;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModelBase;
import net.minecraft.src.ModelBiped;
import net.minecraft.src.RenderBiped;
import net.minecraft.src.Tessellator;
import net.minecraftforge.client.ForgeHooksClient;

public class RendererMBUnit extends RenderBiped{
	
    private ModelNBUnitBody modelBody;
    private ModelMBUnitHead modelBodyDetails;

	public RendererMBUnit(ModelNBUnitBody par1ModelBiped, float par2) {
		super(par1ModelBiped, par2);
		
		modelBody = new ModelNBUnitBody(0F);
		modelBodyDetails = new ModelMBUnitHead(.05F);
		this.setRenderPassModel(modelBody);
		
	}

	private void renderUnit(EntityMBUnit unit, double par2,
				double par4, double par6, float par8, float par9) {
		 

		EnumUnitItems main = unit.getWeaponOption();
		EnumUnitItems off = unit.getWeaponOffHandOption();
		if(main.isJabAttack()){
			modelBody.weaponType = ((ModelNBUnitBody)modelBipedMain).weaponType = ModelNBUnitBody.THRUST;
		}else{
			if(off != null && !off.isShield())
				modelBody.weaponType = ((ModelNBUnitBody)modelBipedMain).weaponType = ModelNBUnitBody.DUEL;
			else
				modelBody.weaponType = ((ModelNBUnitBody)modelBipedMain).weaponType = ModelNBUnitBody.ONE_HAND;
		}
		
		if(unit.isWeaponsDrawn() && main!=null){
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
		
		//this.modelBody.aimedBow = this.modelBipedMain.aimedBow;
		this.modelBody.onGround = this.modelBodyDetails.onGround = this.modelBipedMain.onGround;
		this.modelBipedMain.isChild = false;
		this.modelBody.isChild = false;
		this.modelBodyDetails.isChild = false;
		
		super.doRenderLiving(unit, par2, par4, par6, par8, par9);
		
		if(unit.renderShadow & !unit.getHasActivePotion()) //isInvisible
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
	      
	      drawBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, unit.getTeam().getRGB(), 1.0F);
		
	}

	private void drawBox(double x1, double y1, double z1, double x2,
			double y2, double z2, int colour, float width) {
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
	    	//isInvisible
	        if(unit.getHasActivePotion())
	        	return -1;
	        
	        switch(par2){
	        case 2://body
	        	loadTexture(unit.getSkinFile());
	        	this.setRenderPassModel(modelBody);
	        	return 1;
	        case 0://eyes
	        	loadTexture(unit.getEyesFile());
	        	this.setRenderPassModel(modelBodyDetails);
	        	return 1;
	        case 1: //hair
		        loadTexture(unit.getHairFile());
	        	this.setRenderPassModel(modelBodyDetails);
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
                
				this.modelBipedMain.bipedRightArm.postRender(0.0625F);
	            GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);
	            
	            float var6 = 0.625F;

                GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
                
                if(weapon.isBigSheet()){
                	GL11.glScalef(1F, 1.25F, 1.25F);
                }
                
                if(unit.getWeaponOption().isJabAttack()){
                	GL11.glRotatef(1-(2*Math.abs(.5F-unit.getSwingProgress(f)))*45F+45, 1, 0, 0);
                }
                
                GL11.glScalef(var6, -var6, var6);
                GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
                
				MBClientHelper.renderUnitItems(weapon, FMLClientHandler.instance().getClient(), false, teamIndex);
				
				GL11.glPopMatrix();
			}else{
				GL11.glPushMatrix();
				
				if(weapon.isOnBack()){
					this.modelBipedMain.bipedBody.postRender(0.0625F);
					
					float var6 = 0.625F;
					
					GL11.glTranslatef(0F, .5F, .15F);
					GL11.glScalef(var6, -var6, var6);
					
					GL11.glRotatef(90F, 0.0F, 0.0F, 1.0F);
					
					GL11.glRotatef(45.0F+90, 0.0F, 1.0F, 0.0F);
					
					GL11.glRotatef(35F, 1.0F, 0.0F, 1.0F);
					
					MBClientHelper.renderUnitItems(weapon, FMLClientHandler.instance().getClient(), false, teamIndex);
					
				}else{
					
					this.modelBipedMain.bipedBody.postRender(0.0625F);
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
	                
					this.modelBipedMain.bipedLeftArm.postRender(0.0625F);
					 float var6 = 0.5F;
					 GL11.glTranslatef(-.25F, .65F, -0.12F);
					 GL11.glScalef(-var6, -var6, var6);
					 
					 GL11.glRotatef(-20, 1, 0, 0);
					 
					MBClientHelper.renderUnitItems(offWeapon, FMLClientHandler.instance().getClient(), false, teamIndex);
					
					GL11.glPopMatrix();
				}else{
					GL11.glPushMatrix();
	                
					this.modelBipedMain.bipedLeftArm.postRender(0.0625F);
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
					this.modelBipedMain.bipedBody.postRender(0.0625F);
					
					float var6 = 0.625F;
					
					GL11.glTranslatef(0F, .5F, .15F);
					if(weapon.isOnBack()){
						GL11.glTranslatef(0F, .0F, .03F);
						GL11.glScalef(-1, 1, 1);
					}
					
					if(offWeapon.isShield()){
						var6 = 0.5F;
						GL11.glTranslatef(.3F, 0, .05F);
						GL11.glScalef(var6, -var6, -var6);
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
					
					this.modelBipedMain.bipedBody.postRender(0.0625F);
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
		
		EnumUnits type = unit.getUnitType();
		if(type == EnumUnits.KnightShield || type == EnumUnits.KnightDuel || type == EnumUnits.KnightSpear){
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
				MBClientHelper.renderImageAsItem(MBCommander.ImageSheet, 128+unit.getTeam().ordinal(), 3, 
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
            String order = unit.getDisplayOrder() + " ("+unit.getCurrentMoraleWithBonus()+")";
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
            var12.drawString(order, -var12.getStringWidth(order) / 2, var16, calculateHealthColour((float)((unit.calculateCurrentMorale() +100) / 200F)));
            //var12.drawString(order, -var12.getStringWidth(order) / 2, var16, 0xFFFFFF);
            
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
    	
    	if(healthPercent == 0.5)
    		return 255 << 8 | 255 << 16;
    	else if(healthPercent > .5)
    		return 255 << 8 | (255 - health*2) << 16;
    	else
    		return 255 << 16 | 2*(health-127) << 8;
    	
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
