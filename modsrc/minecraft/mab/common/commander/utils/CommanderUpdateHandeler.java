package mab.common.commander.utils;

import java.awt.Dimension;

import mab.common.commander.MBCommander;
import net.minecraft.src.Tessellator;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import updatemanager.client.GuiModList;
import updatemanager.client.ModReleaseType;
import updatemanager.client.ModType;
import updatemanager.common.UpdateManager;
import updatemanager.common.UpdateManagerMod;
import updatemanager.common.checking.CheckingMethod;

public class CommanderUpdateHandeler  extends UpdateManagerMod{

	public CommanderUpdateHandeler(Mod m) {
		super(m);
	}

	@Override
	public String getModURL() {
		return "http://www.minecraftforum.net/topic/1542377-mine-blade-commander";
	}

	@Override
	public String getUpdateURL() {
		return "https://dl.dropbox.com/u/5780473/Mine%20%26%20Blade%20Commander/MB-C%20Version.txt";
	}

	@Override
	public ModType getModType() {
		return ModType.CONTENT;
	}

	@Override
	public String getModName() {
		return "Mine & Blade - Commander";
	}

	@Override
	public String getChangelogURL() {
		return "https://dl.dropbox.com/u/5780473/Mine%20%26%20Blade%20Commander/MB-Changelog.txt";
	}

	@Override
	public String getDirectDownloadURL() {
		return "https://dl.dropbox.com/u/5780473/Mine%20%26%20Blade%20Commander/MB-Update.txt";
	}

	@Override
	public String getDisclaimerURL() {
		return null;
	}

	@Override
	public ModReleaseType getReleaseType() {
		return ModReleaseType.ALPHA;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Dimension renderIcon(int x, int y, GuiModList modList) {
		GL11.glPushMatrix();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 
				FMLClientHandler.instance().getClient().renderEngine.getTexture(MBCommander.ImageSheet));
		this.drawTexturedModalRect(x, y, 192, 160, 32, 32);
		GL11.glPopMatrix();
		return new Dimension(34, 34);
	}
	
	@SideOnly(Side.CLIENT)
	public void drawTexturedModalRect(int x, int y, int u, int v, int width, int height)
    {
        float var7 = 0.00390625F;
        float var8 = 0.00390625F;
        float zLevel = 100;
        Tessellator var9 = Tessellator.instance;
        var9.startDrawingQuads();
        var9.addVertexWithUV((double)(x + 0), (double)(y + height), (double)zLevel, (double)((float)(u + 0) * var7), (double)((float)(v + height) * var8));
        var9.addVertexWithUV((double)(x + width), (double)(y + height), (double)zLevel, (double)((float)(u + width) * var7), (double)((float)(v + height) * var8));
        var9.addVertexWithUV((double)(x + width), (double)(y + 0), (double)zLevel, (double)((float)(u + width) * var7), (double)((float)(v + 0) * var8));
        var9.addVertexWithUV((double)(x + 0), (double)(y + 0), (double)zLevel, (double)((float)(u + 0) * var7), (double)((float)(v + 0) * var8));
        var9.draw();
    }

	@Override
	public CheckingMethod getCheckingMethod() {
		return CheckingMethod.EQUALS;
	}

	@Override
	public String getBitlyLink() {
		return "http://bit.ly/Tf0cbI";
	}
	
}
