package mab.client.commander.npc;

import org.lwjgl.opengl.GL11;

import net.minecraft.src.Entity;
import net.minecraft.src.ModelBase;
import net.minecraft.src.ModelBiped;
import net.minecraft.src.ModelRenderer;

public class ModelMBUnitHead extends ModelBase{

	public ModelRenderer bipedHead;
	private boolean isSneak;
	
	
	public ModelMBUnitHead(float par1) {
		super();

		this.bipedHead = new ModelRenderer(this, 0, 0);
        this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, par1);
        this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
	}
	
	
	 /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7)
    {
        this.setRotationAngles(par2, par3, par4, par5, par6, par7, par1Entity);

        if (this.isChild)
        {
        	float var8 = 2.0F;
            GL11.glPushMatrix();
            GL11.glScalef(1.5F / var8, 1.5F / var8, 1.5F / var8);
            GL11.glTranslatef(0.0F, 16.0F * par7, 0.0F);
            this.bipedHead.render(par7);
            GL11.glPopMatrix();
        }else{
        	this.bipedHead.render(par7);
        }        
    }
    
    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity)
    {
        this.bipedHead.rotateAngleY = par4 / (180F / (float)Math.PI);
        this.bipedHead.rotateAngleX = par5 / (180F / (float)Math.PI);
        
        if (this.isSneak)
        	this.bipedHead.rotationPointY = 1.0F;
        else
        	this.bipedHead.rotationPointY = 0.0F;
    }
}
