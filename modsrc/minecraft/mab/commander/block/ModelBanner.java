package mab.commander.block;

import net.minecraft.src.*;

/**
 * The Banner Model
 * 
 * @author nerd-boy
 * @since 26/11/2012
 */
public class ModelBanner extends ModelBase
{
	public ModelRenderer Banner;
	public ModelRenderer Base1;
	public ModelRenderer Base2;
    public ModelRenderer BaseStick1;
    public ModelRenderer BaseStick2;
    public ModelRenderer Top1;
    public ModelRenderer Top2;
    
    
      public ModelBanner()
      {
        Base2 = new ModelRenderer(this, 0, 0);
        Base2.addBox(-12F, 0F, -1F, 24, 2, 2);
        Base2.setRotationPoint(0F, 22F, 0F);
        Base2.rotateAngleX = 0F;
        Base2.rotateAngleY = -0.7854F;
        Base2.rotateAngleZ = 0F;
        Base2.mirror = false;
        BaseStick1 = new ModelRenderer(this, 0, 4);
        BaseStick1.addBox(0F, 0F, 0F, 2, 18, 2);
        BaseStick1.setRotationPoint(-1F, 4F, -1F);
        BaseStick1.rotateAngleX = 0F;
        BaseStick1.rotateAngleY = 0F;
        BaseStick1.rotateAngleZ = 0F;
        BaseStick1.mirror = false;
        Banner = new ModelRenderer(this, 8, 4);
        Banner.addBox(-8F, 0F, -1.1F, 16, 28, 0);
        Banner.setRotationPoint(0F, -16F, 0F);
        Banner.rotateAngleX = 0F;
        Banner.rotateAngleY = 0F;
        Banner.rotateAngleZ = 0F;
        Banner.mirror = false;
        BaseStick2 = new ModelRenderer(this, 0, 4);
        BaseStick2.addBox(0F, 0F, 0F, 2, 18, 2);
        BaseStick2.setRotationPoint(-1F, -14F, -1F);
        BaseStick2.rotateAngleX = 0F;
        BaseStick2.rotateAngleY = 0F;
        BaseStick2.rotateAngleZ = 0F;
        BaseStick2.mirror = false;
        Base1 = new ModelRenderer(this, 0, 0);
        Base1.addBox(-12F, 0F, -1F, 24, 2, 2);
        Base1.setRotationPoint(0F, 22F, 0F);
        Base1.rotateAngleX = 0F;
        Base1.rotateAngleY = 0.7854F;
        Base1.rotateAngleZ = 0F;
        Base1.mirror = false;
        Top1 = new ModelRenderer(this, 40, 4);
        Top1.addBox(0F, -16F, -1F, 9, 2, 2);
        Top1.setRotationPoint(0F, 0F, 0F);
        Top1.rotateAngleX = 0F;
        Top1.rotateAngleY = 0F;
        Top1.rotateAngleZ = 0F;
        Top1.mirror = false;
        Top2 = new ModelRenderer(this, 40, 4);
        Top2.addBox(-9F, -16F, -1F, 9, 2, 2);
        Top2.setRotationPoint(0F, 0F, 0F);
        Top2.rotateAngleX = 0F;
        Top2.rotateAngleY = 0F;
        Top2.rotateAngleZ = 0F;
        Top2.mirror = false;
      }

      public void render()
      {
    	  float f5 = 0.066666666F;
        Base2.render(f5);
        BaseStick1.render(f5);
        Banner.render(f5);
        BaseStick2.render(f5);
        Base1.render(f5);
        Top1.render(f5);
        Top2.render(f5);
      }

}
