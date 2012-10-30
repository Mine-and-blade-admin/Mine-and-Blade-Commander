package mab.commander.npc;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

import mab.commander.EnumTeam;
import mab.commander.utils.MBClientHelper;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemArmor;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModelBiped;
import net.minecraft.src.RenderBiped;
import net.minecraftforge.client.ForgeHooksClient;

public class RendererMBUnit extends RenderBiped{
	
    private ModelBiped modelBody;

	public RendererMBUnit(ModelBiped par1ModelBiped, float par2) {
		super(par1ModelBiped, par2);
		
		modelBody = new ModelBiped(0F);
		
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

}
