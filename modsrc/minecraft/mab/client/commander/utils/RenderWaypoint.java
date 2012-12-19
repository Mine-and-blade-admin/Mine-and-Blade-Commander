package mab.client.commander.utils;

import mab.common.commander.MBCommander;
import net.minecraft.src.Entity;
import net.minecraft.src.Render;

import org.lwjgl.opengl.GL11;

public class RenderWaypoint extends Render
{
  ModelWaypoint waypointModel;

  public RenderWaypoint(ModelWaypoint dummy)
  {
    this.waypointModel = dummy;
  }

  public void doRender(Entity var1, double d, double d1, double d2, float f, float f1)
  {
    GL11.glPushMatrix();
    f1 = 0.6666667F;

    GL11.glTranslatef((float)d, (float)d1, (float)d2);

    GL11.glTranslatef(0.0F, 1.51F, 0.0F);

    GL11.glRotatef(0.0F, 0.0F, 1.0F, 0.0F);

    GL11.glPushMatrix();
    GL11.glScalef(f1, -f1, -f1);   
 
    loadTexture(MBCommander.IMAGE_FOLDER+"Waypoint.png");
    this.waypointModel.renderBase(var1, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
    GL11.glColor4f(1F, 1F, 1F, .75F);
    GL11.glEnable(GL11.GL_BLEND);
    this.waypointModel.renderArrow(var1, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
    GL11.glDisable(GL11.GL_BLEND);
    
    GL11.glPopMatrix();

    GL11.glPopMatrix();
  }

}