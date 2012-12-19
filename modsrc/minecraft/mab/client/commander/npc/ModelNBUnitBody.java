package mab.client.commander.npc;

import net.minecraft.src.Entity;
import net.minecraft.src.MathHelper;
import net.minecraft.src.ModelBiped;

public class ModelNBUnitBody extends ModelBiped{
	
	public int weaponType = ONE_HAND;
	
	public static final int ONE_HAND = 0;
	public static final int DUEL = 1;
	public static final int TWO_HAND = 2;
	public static final int THRUST = 3;

	public ModelNBUnitBody() {
		super();
	}

	public ModelNBUnitBody(float par1, float par2, int par3, int par4) {
		super(par1, par2, par3, par4);
	}

	public ModelNBUnitBody(float par1) {
		super(par1);
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
        this.bipedHeadwear.rotateAngleY = this.bipedHead.rotateAngleY;
        this.bipedHeadwear.rotateAngleX = this.bipedHead.rotateAngleX;
        this.bipedRightArm.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float)Math.PI) * 2.0F * par2 * 0.5F;
        this.bipedLeftArm.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 2.0F * par2 * 0.5F;
        this.bipedRightArm.rotateAngleZ = 0.0F;
        this.bipedLeftArm.rotateAngleZ = 0.0F;
        this.bipedRightLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 1.4F * par2;
        this.bipedLeftLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float)Math.PI) * 1.4F * par2;
        this.bipedRightLeg.rotateAngleY = 0.0F;
        this.bipedLeftLeg.rotateAngleY = 0.0F;

        if (this.isRiding)
        {
            this.bipedRightArm.rotateAngleX += -((float)Math.PI / 5F);
            this.bipedLeftArm.rotateAngleX += -((float)Math.PI / 5F);
            this.bipedRightLeg.rotateAngleX = -((float)Math.PI * 2F / 5F);
            this.bipedLeftLeg.rotateAngleX = -((float)Math.PI * 2F / 5F);
            this.bipedRightLeg.rotateAngleY = ((float)Math.PI / 10F);
            this.bipedLeftLeg.rotateAngleY = -((float)Math.PI / 10F);
        }

        if (this.heldItemLeft != 0)
        {
            this.bipedLeftArm.rotateAngleX = this.bipedLeftArm.rotateAngleX * 0.5F - ((float)Math.PI / 10F) * (float)this.heldItemLeft;
        }

        if (this.heldItemRight != 0)
        {
            this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.5F - ((float)Math.PI / 10F) * (float)this.heldItemRight;
        }

        this.bipedRightArm.rotateAngleY = 0.0F;
        this.bipedLeftArm.rotateAngleY = 0.0F;
        float var8;
        float var9;

        if (this.onGround > -9990.0F)
        {
        	switch(weaponType){
        	case ONE_HAND:
        		var8 = this.onGround;
        		 this.bipedBody.rotateAngleY = MathHelper.sin(MathHelper.sqrt_float(var8) * (float)Math.PI * 2.0F) * 0.2F;
                 this.bipedRightArm.rotationPointZ = MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
                 this.bipedRightArm.rotationPointX = -MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
                 this.bipedLeftArm.rotationPointZ = -MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
                 this.bipedLeftArm.rotationPointX = MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
                 this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY;
                 this.bipedLeftArm.rotateAngleY += this.bipedBody.rotateAngleY;
                 this.bipedLeftArm.rotateAngleX += this.bipedBody.rotateAngleY;
                 var8 = 1.0F - this.onGround;
                 var8 *= var8;
                 var8 *= var8;
                 var8 = 1.0F - var8;
                 var9 = MathHelper.sin(var8 * (float)Math.PI);
                 float var10 = MathHelper.sin(this.onGround * (float)Math.PI) * -(this.bipedHead.rotateAngleX - 0.7F) * 0.75F;
                 this.bipedRightArm.rotateAngleX = (float)((double)this.bipedRightArm.rotateAngleX - ((double)var9 * 1.2D + (double)var10));
                 this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY * 2.0F;
                 this.bipedRightArm.rotateAngleZ = MathHelper.sin(this.onGround * (float)Math.PI) * -0.4F;
                 break;
        	case THRUST:
        		var8 = this.onGround;
       		 	this.bipedBody.rotateAngleY = MathHelper.sin(MathHelper.sqrt_float(var8) * (float)Math.PI * 2.0F) * 0.2F;
                this.bipedRightArm.rotationPointZ = MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
                this.bipedRightArm.rotationPointX = -MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
                this.bipedLeftArm.rotationPointZ = -MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
                this.bipedLeftArm.rotationPointX = MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;

                this.bipedRightArm.rotateAngleX = -1F * MathHelper.sin(((onGround))* (float)Math.PI);
                this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY * 2.0F;
                this.bipedRightArm.rotateAngleZ = MathHelper.sin(this.onGround * (float)Math.PI) * -0.4F;
                
        		break;
        	case DUEL:
        		// Speed Up 1st Arm
        		var8 = this.onGround;
        		var8= (float) Math.min(var8/.75F, 1);
       		 	this.bipedBody.rotateAngleY = MathHelper.sin(MathHelper.sqrt_float(var8) * (float)Math.PI * 2.0F) * 0.2F;
                this.bipedRightArm.rotationPointZ = MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
                this.bipedRightArm.rotationPointX = -MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
                this.bipedLeftArm.rotationPointZ = -MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
                this.bipedLeftArm.rotationPointX = MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
                this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY;
                this.bipedLeftArm.rotateAngleY += this.bipedBody.rotateAngleY;
                this.bipedLeftArm.rotateAngleX += this.bipedBody.rotateAngleY;
                var8 = 1.0F - (float) Math.min(this.onGround/.75F, 1);
                var8 *= var8;
                var8 *= var8;
                var8 = 1.0F - var8;
                var9 = MathHelper.sin(var8 * (float)Math.PI);
                var10 = MathHelper.sin(Math.min(var8/.75F, 1) * (float)Math.PI) * -(this.bipedHead.rotateAngleX - 0.7F) * 0.75F;
                this.bipedRightArm.rotateAngleX = (float)((double)this.bipedRightArm.rotateAngleX - ((double)var9 * 1.2D + (double)var10));
                this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY * 2.0F;
                this.bipedRightArm.rotateAngleZ = MathHelper.sin(this.onGround * (float)Math.PI) * -0.4F;
                
                float var82 = 1 - (float)Math.max((this.onGround-.25)/.75F, 0);
                var82 *= var82;
                var82 *= var82;
                var82= 1.0F - var82;
                var9 = MathHelper.sin(var82 * (float)Math.PI);
                var10 = MathHelper.sin((float)Math.max((this.onGround-.25)/.75F, 0) * (float)Math.PI) * -(this.bipedHead.rotateAngleX - 0.7F) * 0.75F;
                this.bipedLeftArm.rotateAngleX = (float)((double)this.bipedLeftArm.rotateAngleX - ((double)var9 * 1.2D + (double)var10));
                this.bipedLeftArm.rotateAngleY += -this.bipedBody.rotateAngleY * 2.0F;
                this.bipedLeftArm.rotateAngleZ = -MathHelper.sin(this.onGround * (float)Math.PI) * -0.4F;

        		break;
        	}
           
           
        }

        if (this.isSneak)
        {
            this.bipedBody.rotateAngleX = 0.5F;
            this.bipedRightArm.rotateAngleX += 0.4F;
            this.bipedLeftArm.rotateAngleX += 0.4F;
            this.bipedRightLeg.rotationPointZ = 4.0F;
            this.bipedLeftLeg.rotationPointZ = 4.0F;
            this.bipedRightLeg.rotationPointY = 9.0F;
            this.bipedLeftLeg.rotationPointY = 9.0F;
            this.bipedHead.rotationPointY = 1.0F;
            this.bipedHeadwear.rotationPointY = 1.0F;
        }
        else
        {
            this.bipedBody.rotateAngleX = 0.0F;
            this.bipedRightLeg.rotationPointZ = 0.1F;
            this.bipedLeftLeg.rotationPointZ = 0.1F;
            this.bipedRightLeg.rotationPointY = 12.0F;
            this.bipedLeftLeg.rotationPointY = 12.0F;
            this.bipedHead.rotationPointY = 0.0F;
            this.bipedHeadwear.rotationPointY = 0.0F;
        }

        this.bipedRightArm.rotateAngleZ += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
        this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
        this.bipedRightArm.rotateAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
        this.bipedLeftArm.rotateAngleX -= MathHelper.sin(par3 * 0.067F) * 0.05F;

        if (this.aimedBow)
        {
            var8 = 0.0F;
            var9 = 0.0F;
            this.bipedRightArm.rotateAngleZ = 0.0F;
            this.bipedLeftArm.rotateAngleZ = 0.0F;
            this.bipedRightArm.rotateAngleY = -(0.1F - var8 * 0.6F) + this.bipedHead.rotateAngleY;
            this.bipedLeftArm.rotateAngleY = 0.1F - var8 * 0.6F + this.bipedHead.rotateAngleY + 0.4F;
            this.bipedRightArm.rotateAngleX = -((float)Math.PI / 2F) + this.bipedHead.rotateAngleX;
            this.bipedLeftArm.rotateAngleX = -((float)Math.PI / 2F) + this.bipedHead.rotateAngleX;
            this.bipedRightArm.rotateAngleX -= var8 * 1.2F - var9 * 0.4F;
            this.bipedLeftArm.rotateAngleX -= var8 * 1.2F - var9 * 0.4F;
            this.bipedRightArm.rotateAngleZ += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
            this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
            this.bipedRightArm.rotateAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
            this.bipedLeftArm.rotateAngleX -= MathHelper.sin(par3 * 0.067F) * 0.05F;
        }
    }
	
	
}
