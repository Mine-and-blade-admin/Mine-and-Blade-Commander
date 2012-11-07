package mab.client.commander.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.TextureFXManager;

import mab.common.commander.npc.EntityMBUnit;
import mab.common.commander.npc.EnumUnitItems;
import net.minecraft.client.Minecraft;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Entity;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.RenderManager;
import net.minecraft.src.Tessellator;
import net.minecraft.src.Vec3;
import net.minecraftforge.client.IItemRenderer;

public class MBClientHelper {
	
	public static Random random = new Random();
	
	public static void renderUnitItems(EnumUnitItems item, Minecraft mc, boolean enchanted, int team){
		
		if(item.isBigSheet()){
			int xCoord = item.getIndex() / 8;
			int yCoord = item.getIndex() % 8;
			
			renderBigImageAsItem(item.getImageSheet(), item.getIndex(), 1, mc, enchanted, true);
			
		}else if (item.isShield()){
			
			renderImageAsItem(item.getImageSheet(), item.getShieldForTeam(team), 1, mc, enchanted, false);
			
		}else{
			renderImageAsItem(item.getImageSheet(), item.getIndex(), 1, mc, enchanted, true);
		}
		
		
	}

	public static void renderImageAsItem(String path, int index, float thickness, Minecraft mc, boolean enchanted, boolean rotate){

		GL11.glPushMatrix();
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture(path));
		int res = (int) (Math.sqrt(mc.renderEngine.getTextureContents(path).length)) / 16;
		
		Tessellator tes = Tessellator.instance;
		
		float yStart = ((float)(index % 16 * 16) + 0.0F) / 256.0F;
        float yEnd = ((float)(index % 16 * 16) + 15.99F) / 256.0F;
        float xStart = ((float)(index / 16 * 16) + 0.0F) / 256.0F;
        float xEnd = ((float)(index / 16 * 16) + 15.99F) / 256.0F;
        
        float var11 = 0.0F;
        float var12 = 0.3F;
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        
        GL11.glTranslatef(-var11, -var12, 0.0F);
        float var13 = 1.5F;
        GL11.glScalef(var13, var13, var13);
        
        if(rotate){
	        GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
	        GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
        }
        GL11.glTranslatef(-0.9375F, -0.0625F, 0.0F);
        
        renderItemIn2D(tes, yEnd, xStart, yStart, xEnd, thickness, false);
        
        //enchantment effects
        if (enchanted)
        {
            GL11.glDepthFunc(GL11.GL_EQUAL);
            GL11.glDisable(GL11.GL_LIGHTING);
            mc.renderEngine.bindTexture(mc.renderEngine.getTexture("%blur%/misc/glint.png"));
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
            float var14 = 0.76F;
            GL11.glColor4f(0.5F * var14, 0.25F * var14, 0.8F * var14, 1.0F);
            GL11.glMatrixMode(GL11.GL_TEXTURE);
            GL11.glPushMatrix();
            float var15 = 0.125F;
            GL11.glScalef(var15, var15, var15);
            float var16 = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F * 8.0F;
            GL11.glTranslatef(var16, 0.0F, 0.0F);
            GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
            renderItemIn2D(tes, yEnd, xStart, yStart, xEnd, thickness, false);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glScalef(var15, var15, var15);
            var16 = (float)(Minecraft.getSystemTime() % 4873L) / 4873.0F * 8.0F;
            GL11.glTranslatef(-var16, 0.0F, 0.0F);
            GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
            renderItemIn2D(tes, yEnd, xStart, yStart, xEnd, thickness, false);
            GL11.glPopMatrix();
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDepthFunc(GL11.GL_LEQUAL);
        }

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        
        
        
        GL11.glPopMatrix();
		
	}
	
	private static void renderBigImageAsItem(String path, int index, float thickness, Minecraft mc, boolean enchanted, boolean middle){
		GL11.glPushMatrix();
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture(path));
		int res = (int) (Math.sqrt(mc.renderEngine.getTextureContents(path).length)) / 8;
		
		if(middle){
			GL11.glTranslatef(0, 0, 0);
		}
		
		Tessellator tes = Tessellator.instance;	
		
		float yStart = ((float)(index % 8 * 32) + 0.0F) / 256.0F;
        float yEnd = ((float)(index % 8 * 32) + 31.999999F) / 256.0F;
        float xStart = ((float)(index / 8 * 32) + 0.0F) / 256.0F;
        float xEnd = ((float)(index / 8 * 32) + 31.999999F) / 256.0F;
        
        
        float var11 = -0.2F;
        float var12 = 1.2F;
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        
        GL11.glTranslatef(-var11, -var12, var11);
        float var13 = 2.5F;
        GL11.glScalef(var13, var13, var13);
        GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
        GL11.glTranslatef(-0.9375F, -0.0625F, 0.0F);
       
        
        renderItemIn2D(tes, yEnd, xStart, yStart, xEnd, thickness, true);
        
        //enchantment effects
        if (enchanted)
        {
            GL11.glDepthFunc(GL11.GL_EQUAL);
            GL11.glDisable(GL11.GL_LIGHTING);
            mc.renderEngine.bindTexture(mc.renderEngine.getTexture("%blur%/misc/glint.png"));
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
            float var14 = 0.76F;
            GL11.glColor4f(0.5F * var14, 0.25F * var14, 0.8F * var14, 1.0F);
            GL11.glMatrixMode(GL11.GL_TEXTURE);
            GL11.glPushMatrix();
            float var15 = 0.125F;
            GL11.glScalef(var15, var15, var15);
            float var16 = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F * 8.0F;
            GL11.glTranslatef(var16, 0.0F, 0.0F);
            GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
            renderItemIn2D(tes, yEnd, xStart, yStart, xEnd, thickness, true);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glScalef(var15, var15, var15);
            var16 = (float)(Minecraft.getSystemTime() % 4873L) / 4873.0F * 8.0F;
            GL11.glTranslatef(-var16, 0.0F, 0.0F);
            GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
            renderItemIn2D(tes, yEnd, xStart, yStart, xEnd, thickness, true);
            GL11.glPopMatrix();
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDepthFunc(GL11.GL_LEQUAL);
        }

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        
        
        
        GL11.glPopMatrix();
		
	}
	
	
	private static void renderItemIn2D(Tessellator par1Tessellator, float par2, float par3, float par4, float par5,
			float thickness, boolean bigImage)
    {
        float var6 = 1.0F;
        float var7 = 0.0625F * thickness;
        par1Tessellator.startDrawingQuads();
        par1Tessellator.setNormal(0.0F, 0.0F, 1.0F);
        par1Tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, (double)par2, (double)par5);
        par1Tessellator.addVertexWithUV((double)var6, 0.0D, 0.0D, (double)par4, (double)par5);
        par1Tessellator.addVertexWithUV((double)var6, 1.0D, 0.0D, (double)par4, (double)par3);
        par1Tessellator.addVertexWithUV(0.0D, 1.0D, 0.0D, (double)par2, (double)par3);
        par1Tessellator.draw();
        par1Tessellator.startDrawingQuads();
        par1Tessellator.setNormal(0.0F, 0.0F, -1.0F);
        par1Tessellator.addVertexWithUV(0.0D, 1.0D, (double)(0.0F - var7), (double)par2, (double)par3);
        par1Tessellator.addVertexWithUV((double)var6, 1.0D, (double)(0.0F - var7), (double)par4, (double)par3);
        par1Tessellator.addVertexWithUV((double)var6, 0.0D, (double)(0.0F - var7), (double)par4, (double)par5);
        par1Tessellator.addVertexWithUV(0.0D, 0.0D, (double)(0.0F - var7), (double)par2, (double)par5);
        par1Tessellator.draw();
        par1Tessellator.startDrawingQuads();
        par1Tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        int var8;
        float var9;
        float var10;
        float var11;

        /* Gets the width/16 of the currently bound texture, used 
         * to fix the side rendering issues on textures != 16 */
        int tileSize = TextureFXManager.instance().getTextureDimensions(GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D)).width / 16;
        if(bigImage)
        	tileSize = tileSize*2;
        
        float tx = 1.0f / (32 * tileSize);
        float tz = 1.0f /  tileSize;

        for (var8 = 0; var8 < tileSize; ++var8)
        {
            var9 = (float)var8 / tileSize;
            var10 = par2 + (par4 - par2) * var9 - tx;
            var11 = var6 * var9;
            par1Tessellator.addVertexWithUV((double)var11, 0.0D, (double)(0.0F - var7), (double)var10, (double)par5);
            par1Tessellator.addVertexWithUV((double)var11, 0.0D, 0.0D, (double)var10, (double)par5);
            par1Tessellator.addVertexWithUV((double)var11, 1.0D, 0.0D, (double)var10, (double)par3);
            par1Tessellator.addVertexWithUV((double)var11, 1.0D, (double)(0.0F - var7), (double)var10, (double)par3);
        }

        par1Tessellator.draw();
        par1Tessellator.startDrawingQuads();
        par1Tessellator.setNormal(1.0F, 0.0F, 0.0F);

        for (var8 = 0; var8 < tileSize; ++var8)
        {
            var9 = (float)var8 / tileSize;
            var10 = par2 + (par4 - par2) * var9 - tx;
            var11 = var6 * var9 + tz;
            par1Tessellator.addVertexWithUV((double)var11, 1.0D, (double)(0.0F - var7), (double)var10, (double)par3);
            par1Tessellator.addVertexWithUV((double)var11, 1.0D, 0.0D, (double)var10, (double)par3);
            par1Tessellator.addVertexWithUV((double)var11, 0.0D, 0.0D, (double)var10, (double)par5);
            par1Tessellator.addVertexWithUV((double)var11, 0.0D, (double)(0.0F - var7), (double)var10, (double)par5);
        }

        par1Tessellator.draw();
        par1Tessellator.startDrawingQuads();
        par1Tessellator.setNormal(0.0F, 1.0F, 0.0F);

        for (var8 = 0; var8 < tileSize; ++var8)
        {
            var9 = (float)var8 / tileSize;
            var10 = par5 + (par3 - par5) * var9 - tx;
            var11 = var6 * var9 + tz;
            par1Tessellator.addVertexWithUV(0.0D, (double)var11, 0.0D, (double)par2, (double)var10);
            par1Tessellator.addVertexWithUV((double)var6, (double)var11, 0.0D, (double)par4, (double)var10);
            par1Tessellator.addVertexWithUV((double)var6, (double)var11, (double)(0.0F - var7), (double)par4, (double)var10);
            par1Tessellator.addVertexWithUV(0.0D, (double)var11, (double)(0.0F - var7), (double)par2, (double)var10);
        }

        par1Tessellator.draw();
        par1Tessellator.startDrawingQuads();
        par1Tessellator.setNormal(0.0F, -1.0F, 0.0F);

        for (var8 = 0; var8 < tileSize; ++var8)
        {
            var9 = (float)var8 / tileSize;
            var10 = par5 + (par3 - par5) * var9 - tx;
            var11 = var6 * var9;
            par1Tessellator.addVertexWithUV((double)var6, (double)var11, 0.0D, (double)par4, (double)var10);
            par1Tessellator.addVertexWithUV(0.0D, (double)var11, 0.0D, (double)par2, (double)var10);
            par1Tessellator.addVertexWithUV(0.0D, (double)var11, (double)(0.0F - var7), (double)par2, (double)var10);
            par1Tessellator.addVertexWithUV((double)var6, (double)var11, (double)(0.0F - var7), (double)par4, (double)var10);
        }

        par1Tessellator.draw();
    }
	
	
	
	
	

	public static void renderItemAsEntity(int par1, int par2, RenderManager renderManager) {
		Tessellator var3 = Tessellator.instance;
        float var4 = (float)(par1 % 16 * 16 + 0) / 256.0F;
        float var5 = (float)(par1 % 16 * 16 + 16) / 256.0F;
        float var6 = (float)(par1 / 16 * 16 + 0) / 256.0F;
        float var7 = (float)(par1 / 16 * 16 + 16) / 256.0F;
        float var8 = 1.0F;
        float var9 = 0.5F;
        float var10 = 0.25F;

        for (int var11 = 0; var11 < par2; ++var11)
        {
            GL11.glPushMatrix();

            if (var11 > 0)
            {
                float var12 = (random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                float var13 = (random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                float var14 = (random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                GL11.glTranslatef(var12, var13, var14);
            }

            GL11.glRotatef(180.0F - renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            var3.startDrawingQuads();
            var3.setNormal(0.0F, 1.0F, 0.0F);
            var3.addVertexWithUV((double)(0.0F - var9), (double)(0.0F - var10), 0.0D, (double)var4, (double)var7);
            var3.addVertexWithUV((double)(var8 - var9), (double)(0.0F - var10), 0.0D, (double)var5, (double)var7);
            var3.addVertexWithUV((double)(var8 - var9), (double)(1.0F - var10), 0.0D, (double)var5, (double)var6);
            var3.addVertexWithUV((double)(0.0F - var9), (double)(1.0F - var10), 0.0D, (double)var4, (double)var6);
            var3.draw();
            GL11.glPopMatrix();
        }
	}
	
	
	
	/**
     * Finds what block or object the mouse is over at the specified partial tick time. Args: partialTickTime
     */
    public static MovingObjectPosition getMouseOver(double distance, float f)
    {
    	Minecraft mc = FMLClientHandler.instance().getClient();
    	
        if (mc.renderViewEntity != null)
        {
            if (mc.theWorld != null)
            {
                double var2 = distance;
                MovingObjectPosition mouseOver = mc.renderViewEntity.rayTrace(var2, f);
                Vec3 var6 = mc.renderViewEntity.getPosition(f);

                double var4 = var2;
                if (mouseOver != null)
                {
                    var4 = mouseOver.hitVec.distanceTo(var6);
                }

                Vec3 var7 = mc.renderViewEntity.getLook(f);
                Vec3 var8 = var6.addVector(var7.xCoord * var2, var7.yCoord * var2, var7.zCoord * var2);
                Entity pointedEntity = null;
                float var9 = 1.0F;
                List var10 = mc.theWorld.getEntitiesWithinAABBExcludingEntity(mc.renderViewEntity, mc.renderViewEntity.boundingBox.addCoord(var7.xCoord * var2, var7.yCoord * var2, var7.zCoord * var2).expand((double)var9, (double)var9, (double)var9));
                double var11 = var4;
                Iterator var13 = var10.iterator();

                while (var13.hasNext())
                {
                    Entity var14 = (Entity)var13.next();

                    if (var14.canBeCollidedWith())
                    {
                        float var15 = var14.getCollisionBorderSize();
                        AxisAlignedBB var16 = var14.boundingBox.expand((double)var15, (double)var15, (double)var15);
                        MovingObjectPosition var17 = var16.calculateIntercept(var6, var8);

                        if (var16.isVecInside(var6))
                        {
                            if (0.0D < var11 || var11 == 0.0D)
                            {
                                pointedEntity = var14;
                                var11 = 0.0D;
                            }
                        }
                        else if (var17 != null)
                        {
                            double var18 = var6.distanceTo(var17.hitVec);

                            if (var18 < var11 || var11 == 0.0D)
                            {
                                pointedEntity = var14;
                                var11 = var18;
                            }
                        }
                    }
                }

                if (pointedEntity != null && (var11 < var4 || mouseOver == null))
                {
                   mouseOver = new MovingObjectPosition(pointedEntity);
                }
                
                return mouseOver;
            }
        }
        return null;
    }
    
    public static EntityMBUnit getUnitMouseOver(double distance, float f){
    	MovingObjectPosition mouseOver = getMouseOver(distance, f);
    	if(mouseOver != null && mouseOver.entityHit != null && mouseOver.entityHit instanceof EntityMBUnit){
    		return (EntityMBUnit) mouseOver.entityHit;
    	}else
    		return null;
    }

}
