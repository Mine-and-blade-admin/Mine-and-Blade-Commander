package mab.client.commander.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import mab.client.commander.utils.MBClientHelper;
import mab.common.commander.CommanderPacketHandeler;
import mab.common.commander.ConfigHelper;
import mab.common.commander.IUpgradeable;
import mab.common.commander.MBCommander;
import mab.common.commander.block.TileEntityBanner;
import mab.common.commander.npc.EntityMBUnit;
import mab.common.commander.npc.EnumAbilities;
import mab.common.commander.npc.EnumUnits;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.IRangedAttackMob;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.OpenGlHelper;
import net.minecraft.src.RenderHelper;
import net.minecraft.src.RenderManager;
import net.minecraft.src.ScaledResolution;
import net.minecraft.src.StringTranslate;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import extendedGUI.GUIAltButton;
import extendedGUI.GUIAltScroll;
import extendedGUI.RadioButtonControl;

public class GuiSpawnUpgrade extends GuiScreen{
	protected Minecraft mc = FMLClientHandler.instance().getClient();
	
	/** The X size of the window in pixels. */
	protected int xSize = 202;

	/** The Y size of the window in pixels. */
	protected int ySize = 182;
	
	/**
     * Starting X position for the Gui. Inconsistent use for Gui backgrounds.
     */
    protected int guiLeft;

    /**
     * Starting Y position for the Gui. Inconsistent use for Gui backgrounds.
     */
    protected int guiTop;
    
    protected StringTranslate st = StringTranslate.getInstance();
    
    
    protected GuiButton spawnButton;
    protected GuiButton detailsButton;
    protected RadioButtonControl radioButtons;
    
    protected GUIAltScroll[] scrolls;
    
    protected EntityMBUnit[] units;
    
    protected IUpgradeable upgradeable;
    
    protected Random rand = new Random();
    protected EntityPlayer player;
    protected int coins;
    
    float percentOut = 1;
    
    public GuiSpawnUpgrade(IUpgradeable upgradeable, EntityPlayer player){
    	super();
    	this.player = player;
    	this.upgradeable = upgradeable;
    }
    
    
    public void initGui()
    {
        super.initGui();
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
        
        spawnButton = new GUIAltButton(0, 115+guiLeft, 142+guiTop+16, 80, 18, st.translateKey("gui.spawn") );
    	detailsButton = new GUIAltButton(1, 115+guiLeft, 82+guiTop, 80, 18,  st.translateKey("gui.details"));
        	
    	EnumUnits[] unitsTemp = upgradeable.getUpgrades();
    	units = new EntityMBUnit[unitsTemp.length];
    	for(int i = 0; i < unitsTemp.length; i++){
    		units[i] = EntityMBUnit.generateUnit(mc.theWorld, upgradeable.getTeam(), unitsTemp[i], upgradeable);
    	}
    	
    	String[] unitLabels = new String[units.length];
    	for(int i = 0; i < units.length; i++){
    		unitLabels[i] =  st.translateKey(units[i].getUnitName());
    		
    		units[i].posX = mc.thePlayer.posX+2;
    		units[i].posY = mc.thePlayer.posY;
    		units[i].posZ = mc.thePlayer.posZ+2;
    		
    		units[i].renderShadow = false;
    	}
    	
    	radioButtons = new RadioButtonControl(2, 67+guiLeft, 10+guiTop, 126, 14*2, unitLabels);
    	
        reAddControls();
        
        coins = countGold();
    }
    
    private int countGold() {
		InventoryPlayer inv = player.inventory;
		int gold = 0;
		for(int i = 0; i < inv.getSizeInventory(); i++){
			ItemStack stack = inv.getStackInSlot(i);
			if(stack != null){
				if(stack.itemID == Item.goldNugget.shiftedIndex){
					gold += stack.stackSize;
				}else if (stack.itemID == Item.ingotGold.shiftedIndex){
					gold+= stack.stackSize*9;
				}else if (stack.itemID == Block.blockGold.blockID){
					gold+= stack.stackSize*9*9;
				}
			}
		}
		return gold;
	}
    
    private void reAddControls() {
		
		controlList = new ArrayList();
		
		controlList.add(spawnButton);
		//controlList.add(detailsButton);
		controlList.add(radioButtons);
		detailsButton.enabled = false;
		spawnButton.enabled = false;
		
		
		if(radioButtons.selected != -1){
			
			int count = 0;
			
			
			for(int i = upgradeable.getFirstEditableOptionIndex(); i < 6; i++){
				if(units[radioButtons.selected].getOptionMax(i) > 0)
					count++;
			}
			scrolls = new GUIAltScroll[count];
			
			
			for(int i = 0; i < scrolls.length; i++){
				scrolls[i]=new GUIAltScroll(10+i, 29+guiLeft, 85+guiTop+15*i, 77, true, 0, 
						units[radioButtons.selected].getOptionMax(i + upgradeable.getFirstEditableOptionIndex()));
				
				if(MBCommander.INSTANCE.config.get(ConfigHelper.CAT_UNITS, ConfigHelper.UNIT_RANDOM, true).getBoolean(true)){
					scrolls[i].current = rand.nextInt(units[radioButtons.selected].
							getOptionMax(i + upgradeable.getFirstEditableOptionIndex()));
				}
				else
					scrolls[i].current = 0;
				
				scrolls[i].sliderValue = (float)scrolls[i].current / (float)units[radioButtons.selected].
						getOptionMax(i + upgradeable.getFirstEditableOptionIndex());
				
				scrolls[i].displayString = units[radioButtons.selected].getOptionLabel(i + upgradeable.getFirstEditableOptionIndex());
				controlList.add(scrolls[i]);
			}
		}
	}
    
    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
    	if(par1GuiButton.id == this.radioButtons.id){
    		reAddControls();
    		
    		spawnButton.enabled = radioButtons.selected >= 0 && (player.capabilities.isCreativeMode || coins >= units[radioButtons.selected].getCost());
    	}else if(par1GuiButton.id >= 10 && scrolls != null){
    		units[radioButtons.selected].setOption(par1GuiButton.id-10 + upgradeable.getFirstEditableOptionIndex(), (byte)((GUIAltScroll)par1GuiButton).current);
    	}else if (par1GuiButton.id == this.spawnButton.id && radioButtons.selected > -1){
    		
    		if(!player.capabilities.isCreativeMode){
	    		MBCommander.PROXY.scanForGold(player.inventory,units[radioButtons.selected].getCost());
    		}
			
    		PacketDispatcher.sendPacketToServer(upgradeable.generatePacket(units[radioButtons.selected], player));
    		
    		//close the GUI
    		this.mc.displayGuiScreen((GuiScreen)null);
            this.mc.setIngameFocus();
            this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
    		
    	}
    }
    
    @Override
    public void drawScreen(int mouseX, int mouseY, float par3) 
    {
    	drawGuiContainerBackgroundLayer(par3, mouseX, mouseY);
    	
    	super.drawScreen(mouseX, mouseY, par3);
    	
    	
    	this.drawCenteredString(fontRenderer, st.translateKey("gui.spawn.currency"), 116+guiLeft+39, 107+guiTop, Color.WHITE.getRGB());
    	if(radioButtons.selected > -1 && !player.capabilities.isCreativeMode)
    		this.drawString(fontRenderer, st.translateKey("gui.spawn.required")+": "+units[radioButtons.selected].getCost(), 118+guiLeft ,  120+guiTop, Color.WHITE.getRGB());
    	else
    		this.drawString(fontRenderer, st.translateKey("gui.spawn.required")+": -", 118+guiLeft ,  120+guiTop, Color.WHITE.getRGB());
    	
    	this.drawString(fontRenderer, st.translateKey("gui.spawn.have")+": "+coins, 118+guiLeft ,  133+guiTop, Color.WHITE.getRGB());
    	
    	if(scrolls != null)
    	for(int i = 0; i < scrolls.length; i++){
    		int var4 = this.mc.renderEngine.getTexture(MBCommander.IMAGE_FOLDER+"gui/Spawn.png");
    	    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    	    this.mc.renderEngine.bindTexture(var4);
    	    if(radioButtons.selected >= 0){
	    	    int icon = units[radioButtons.selected].getOptionIcon(i + upgradeable.getFirstEditableOptionIndex());
	    	    if(icon >= 0)
	    	    	this.drawTexturedModalRect(guiLeft+7, guiTop+85+15*i, 0+14*icon, 242, 14, 14);
    	    }
    	}
    	
    	
    	
    	
    	if(scrolls != null){
    		for(int i = 0; i < scrolls.length; i++){
    			 if(radioButtons.selected >= 0){
    				 units[radioButtons.selected].setOption(i + upgradeable.getFirstEditableOptionIndex(), (byte)scrolls[i].current);
    			 }
    		}
    	}
    	
    	drawDetailsPanel();
    	
    	
    	drawTooltips(mouseX, mouseY, fontRenderer);
    	
    	if(getSelectedUnit() != null)
         	func_74223_a(this.mc, guiLeft + 35, guiTop + 75, 30, (float)(width + 51) - this.width, (float)(guiTop + 75 - 50) - this.height);
    	
    	
    	
    	
    }
    
    private void drawDetailsPanel() {

        if(width > 205 + 2*(110) && getSelectedUnit() != null){
	    	int var4 = mc.renderEngine.getTexture("/extendedGUI/image/GUI Controls.png");
	    	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	    	this.mc.renderEngine.bindTexture(var4);
	    	
	    	//Draw Left Panel
	    	drawTexturedModalRect(0, guiTop+1, 125 - 110, 213, 110, 20);
	    	for(int i = 0; i < 11; i++){
	    		drawTexturedModalRect(0, guiTop+21 + 14*i, 125 - 110, 233, 110, 14);
	    	}
	    	drawTexturedModalRect(0, guiTop+ySize - 7, 125 - 110, 250, 110, 6);
	    	
	    	
	    	//Draw Right Panel
	    	
	    	drawTexturedModalRect(width - 110, guiTop+1, 131, 213, 110, 20);
	    	for(int i = 0; i < 11; i++){
	    		drawTexturedModalRect(width - 110, guiTop+21 + 14*i, 131, 233, 110, 14);
	    	}
	    	drawTexturedModalRect(width - 110, guiTop+ySize - 7, 131, 250, 110, 6);
	    	
	    	
	    	//Left Panel Text
	    	String title = st.translateKey(getSelectedUnit().getUnitName()) + " "+ st.translateKey("gui.details.stats");
	    	
	    	fontRenderer.drawString(title, (int)(110*percentOut) - (125 + fontRenderer.getStringWidth(title))/2, guiTop + 8, 0x404040);
	    	
	    	drawString(fontRenderer, st.translateKey("gui.details.attack")+": "+getSelectedUnit().getAttackStrength(),
	    			3, guiTop+26, 0xFFFFFF);
	    	drawString(fontRenderer, st.translateKey("gui.details.health")+": "+getSelectedUnit().getMaxHealth(),
	    			3, guiTop+26+12, 0xFFFFFF);
	    	drawString(fontRenderer, st.translateKey("gui.details.armour")+": "+getSelectedUnit().getBaseArmour(),
	    			3, guiTop+26+24, 0xFFFFFF);
	    	
	    	
	    	if(units[radioButtons.selected] instanceof IRangedAttackMob){
		    	drawString(fontRenderer, st.translateKey("gui.details.range")+": "+getSelectedUnit().getTotalArmorValue(),
		    			3, guiTop+26+36, 0xFFFFFF);
		    	drawString(fontRenderer, st.translateKey("gui.details.rate")+": "+getSelectedUnit().getTotalArmorValue(),
		    			3, guiTop+26+48, 0xFFFFFF);
		    	
	    	}
	    	
	    	EnumAbilities[] abilities = getSelectedUnit().getAbilities();
	    	if(abilities.length > 0){
		    	drawCenteredString(fontRenderer, st.translateKey("gui.details.special"), 110/2, guiTop+26+65, 0xFFFF00);

		    	for(int i = 0; i < abilities.length; i++){
		    		if(abilities[i].isPositive())
		    			drawString(fontRenderer, "+", 3, guiTop+26+65+14+i*12, 0x00FF00);
			    	else
			    		drawString(fontRenderer, "-", 3, guiTop+26+65+14+i*12, 0xFF0000);	
		    		
		    		drawString(fontRenderer, st.translateKey(abilities[i].getTranslatedName(-1)),
		    				10, guiTop+26+65+14+i*12, 0xFFFFFF);
		    	}
	    	}
	    	
	    	
	    	//Right Panel Text
	    	title = st.translateKey(getSelectedUnit().getUnitName());

	    	fontRenderer.drawString(title, (int)(width - 110*percentOut) + 55 - fontRenderer.getStringWidth(title)/2 , guiTop + 8, 0x404040);
	    	
	    	
	    	String desc = MBClientHelper.parseUnitDescription(getSelectedUnit());
	    	fontRenderer.drawSplitString(desc, width - (int)(110*percentOut) + 11, guiTop+26, 90, (0xFFFFFF& 16579836) >> 2 | 0xFFFFFF & -16777216);
	    	fontRenderer.drawSplitString(desc, width - (int)(110*percentOut) + 10, guiTop+25, 90, 0xFFFFFF);

	    	
        }
		
	}
    
    
    protected EntityMBUnit getSelectedUnit(){
    	if(radioButtons.selected >= 0)
    		return units[radioButtons.selected];
    	else
    		return null;
    }


	private void drawTooltips(int mouseX, int mouseY, FontRenderer fontRenderer) {
		short var6 = 240;
        short var7 = 240;
		
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var6 / 1.0F, (float)var7 / 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		if(scrolls != null)
		if(mouseX >= guiLeft+8 &&
				mouseY >= guiTop + 85 &&
				mouseX <= guiLeft + 8+98 &&
				mouseY < guiTop+85 + scrolls.length * 15){
			this.zLevel=500;
			
			int index = ((mouseY-84-guiTop)) / (15);
			
			if(index < scrolls.length){			
				String text =  st.translateKey(scrolls[index].displayString);
				
				int x = mouseX+12;
				int y = mouseY+12;
				int width = fontRenderer.getStringWidth(text);
				int height = 8;
				
				int colour = -267386864;
		        this.drawRect(x - 3, y - 4, x + width + 3, y - 3, colour);
		        this.drawRect(x - 3, y + height + 3, x + width + 3, y + height + 4, colour);
		        this.drawRect(x - 3, y - 3, x + width + 3, y + height + 3, colour);
		        this.drawRect(x - 4, y - 3, x - 3, y + height + 3, colour);
		        this.drawRect(x + width + 3, y - 3, x + width + 4, y + height + 3, colour);
		         
		        colour = 1347420415;
		        int colour2 = (colour & 16711422) >> 1 | colour & -16777216;
		        this.drawGradientRect(x - 3, y - 3 + 1, x - 3 + 1, y + height + 3 - 1, colour, colour2);
		        this.drawGradientRect(x + width + 2, y - 3 + 1, x + width + 3, y + height + 3 - 1, colour, colour2);
		        this.drawGradientRect(x - 3, y - 3, x + width + 3, y - 3 + 1, colour, colour);
		        this.drawGradientRect(x - 3, y + height + 2, x + width + 3, y + height + 3, colour2, colour2);
		         
		        this.fontRenderer.drawStringWithShadow(text, x, y, -1); 
			}
			this.zLevel = 0;
		}else if (mouseX < 100 && mouseY >= guiTop + 26){
			
			this.zLevel=5000;
			String text = null;
			EnumAbilities[] abilities = getSelectedUnit().getAbilities();
			if(mouseY > guiTop+26+65+14 && mouseY < guiTop+103+12*abilities.length){
				text = abilities[(mouseY - (guiTop+103)) / 12].getTranslatedDecription(-1, getSelectedUnit());
			}
			
			if(text != null){
				
				int linecount = MBClientHelper.calcNoLinesForSplitString(fontRenderer, text, 90);
				int x = mouseX+12;
				int y = mouseY+12;
				int width = 90;
				int height = 9*(linecount+1);
				
				int colour = -267386864;
		        this.drawRect(x - 3, y - 4, x + width + 3, y - 3, colour);
		        this.drawRect(x - 3, y + height + 3, x + width + 3, y + height + 4, colour);
		        this.drawRect(x - 3, y - 3, x + width + 3, y + height + 3, colour);
		        this.drawRect(x - 4, y - 3, x - 3, y + height + 3, colour);
		        this.drawRect(x + width + 3, y - 3, x + width + 4, y + height + 3, colour);
		         
		        colour = 1347420415;
		        int colour2 = (colour & 16711422) >> 1 | colour & -16777216;
		        this.drawGradientRect(x - 3, y - 3 + 1, x - 3 + 1, y + height + 3 - 1, colour, colour2);
		        this.drawGradientRect(x + width + 2, y - 3 + 1, x + width + 3, y + height + 3 - 1, colour, colour2);
		        this.drawGradientRect(x - 3, y - 3, x + width + 3, y - 3 + 1, colour, colour);
		        this.drawGradientRect(x - 3, y + height + 2, x + width + 3, y + height + 3, colour2, colour2);
				
				fontRenderer.drawSplitString(text, x, y, 90, (0xFFFFFF& 16579836) >> 2 | 0xFFFFFF & -16777216);
		    	fontRenderer.drawSplitString(text, x, y, 90, 0xFFFFFF);
		    	
			}
			this.zLevel=0;
		}
		
	}
    
    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected void drawGuiContainerBackgroundLayer(float par1, int mouseX, int mouseY)
    {
    	int var4 = this.mc.renderEngine.getTexture(MBCommander.IMAGE_FOLDER+"gui/Spawn.png");
    	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    	this.mc.renderEngine.bindTexture(var4);
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, this.xSize, this.ySize);
    }
    
    public void func_74223_a(Minecraft par0Minecraft, int par1, int par2, int par3, float par4, float par5)
    {
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)par1, (float)par2, 50.0F);
        GL11.glScalef((float)(-par3), (float)par3, (float)par3);
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);

        GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
        
        GL11.glRotatef(10, 1.0F, 0.0F, 0.0F);
        

        float percent = (((float)(System.currentTimeMillis()%5000)) / 5000);
        if(percent < 0.75){
        	GL11.glRotatef(percent*180F/.75F-90F, 0, 1, 0);
        }else{
        	GL11.glRotatef(percent*180F/.25F-90F, 0, 1, 0);
        }
        
        GL11.glRotatef(0,0,1,0);
        RenderManager.instance.playerViewY = 180.0F;
        getSelectedUnit().renderShadow = false;
        RenderManager.instance.renderEntityWithPosYaw(getSelectedUnit(), 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
      
        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }
}
