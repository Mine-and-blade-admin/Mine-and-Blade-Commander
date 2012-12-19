package mab.client.commander.utils;

import net.minecraft.src.Entity;
import net.minecraft.src.ModelBase;
import net.minecraft.src.ModelRenderer;


	public class ModelWaypoint extends ModelBase
	{
	  public ModelRenderer arrow;
	  public ModelRenderer target;

	  public ModelWaypoint()
	  {
	    this.arrow = new ModelRenderer(this, 0, 0);
	    this.arrow.addBox(-8.0F, 0.0F, 0.0F, 16, 24, 0);
	    this.arrow.setRotationPoint(0.0F, 0.0F, 0.0F);

	    this.target = new ModelRenderer(this, 16, 0);
	    this.target.addBox(-8.0F, 0.0F, -8.0F, 16, 0, 16);
	    this.target.setRotationPoint(0.0F, 24.0F, 0.0F);
	    
	  }
	  
	  

	  @Override
	public void render(Entity par1Entity, float par2, float par3, float par4,
			float par5, float par6, float par7) {
		// TODO Auto-generated method stub
		super.render(par1Entity, par2, par3, par4, par5, par6, par7);
		
		 moveArrow(par1Entity.ticksExisted);

		    this.arrow.render(par7 * 1.5F);
		    this.target.render(par7 * 1.5F);
	}



	

	  private void moveArrow(int ticksExisted)
	  {
	    this.arrow.setRotationPoint(0.0F, (float)Math.sin(3.141592653589793D * ticksExisted / 25.0D) * -4.0F - 4.0F, 0.0F);
	    this.arrow.rotateAngleY = (ticksExisted % 100 / 100.0F * 3.141593F * 2.0F);
	  }



	public void renderArrow(Entity var1, float f, float g, float h, float i,
			float j, float k) {
		 this.arrow.render(k * 1.5F);
	}



	public void renderBase(Entity var1, float f, float g, float h, float i,
			float j, float k) {
		super.render(var1, f,g,h,i,j,k);
		 moveArrow(var1.ticksExisted);

		 this.target.render(k * 1.5F);
		
	}

}
	

