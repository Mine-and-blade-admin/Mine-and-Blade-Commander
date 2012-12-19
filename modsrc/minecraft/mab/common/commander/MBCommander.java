package mab.common.commander;


import updatemanager.common.ModConverter;
import updatemanager.common.UpdateManager;
import updatemanager.common.mod_UpdateManager;
import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MBCommanderForgeEvents;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import mab.common.commander.block.BlockBanner;
import mab.common.commander.block.BlockItemBanner;
import mab.common.commander.block.TileEntityBanner;
import mab.common.commander.commands.MBRestartGame;
import mab.common.commander.commands.MBStartGame;
import mab.common.commander.commands.MBStopGame;
import mab.common.commander.items.ItemTrumpet;
import mab.common.commander.npc.EntityMBMeleeUnit;
import mab.common.commander.npc.EnumUnits;
import mab.common.commander.utils.CommanderUpdateHandeler;
import mab.common.commander.utils.MBChatListener;
import mab.common.commander.utils.MBCraftingHandeler;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.ServerStarted;
import cpw.mods.fml.common.Mod.ServerStarting;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.EntityRegistry.EntityRegistration;


@Mod(name="Mine & Blade: Commander", modid="MaB-Commander", version="0.0.6", acceptedMinecraftVersions="1.4.4")
@NetworkMod(clientSideRequired=true, serverSideRequired=false, channels={"MBc|Banner", "MBc|Spawn", "MBc|Upgrade", "MBc|Order", "MBc|Team", "MBc|Particle"}, packetHandler = CommanderPacketHandeler.class)
public class MBCommander {
	
	@Instance("MaB-Commander")
	public static MBCommander INSTANCE;
	
	@SidedProxy(clientSide="mab.client.commander.ClientProxy", serverSide="mab.common.commander.CommonProxy")
	public static CommonProxy PROXY;
	
	public static String IMAGE_FOLDER = "/mab/images/";
	public static String ImageSheet = IMAGE_FOLDER+"MBSheet.png";
	
	public static Configuration config;

	public BlockBanner banner;
	public ItemTrumpet trumpet;
	
	@PreInit
	public void PreInitialization(FMLPreInitializationEvent event){
		config = new Configuration(event.getSuggestedConfigurationFile());
		config.addCustomCategoryComment(ConfigHelper.CAT_GAMEPLAY, "Global Gameplay Options");
		config.addCustomCategoryComment(ConfigHelper.CAT_UNITS, "Any configuration options to do with units or unit AI");
		config.addCustomCategoryComment(ConfigHelper.CAT_UNITS_IDS, "The Entity IDs for the units");
		
		//ConfigHelper.teamGame = config.get(ConfigHelper.CAT_GAMEPLAY, ConfigHelper.TEAM_GAME, FMLCommonHandler.instance().getSide() != Side.CLIENT).getBoolean(FMLCommonHandler.instance().getSide() != Side.CLIENT);
		//config.get(ConfigHelper.CAT_UNITS, ConfigHelper.UNIT_RANDOM, true);
		//config.get(ConfigHelper.CAT_UNITS, ConfigHelper.UNIT_MELEE_ATTACK_EXP, false);
		//config.get(ConfigHelper.CAT_UNITS, ConfigHelper.UNIT_RANGE_ATTACK_EXP, true);
		
		ConfigHelper.getPathFindSearch();
	}

	@Init
	public void Initialization(FMLInitializationEvent event){
		banner = new BlockBanner(config.getBlock(ConfigHelper.BLOCK_BANNER, 2650).getInt());
		trumpet = new ItemTrumpet(config.getItem(ConfigHelper.ITEM_TRUMPET, 5000).getInt());
		
		GameRegistry.registerBlock(banner);
		GameRegistry.registerTileEntity(TileEntityBanner.class, "MBBanner");
		
		Item.itemsList[banner.blockID] = null;
		Item.itemsList[banner.blockID] = (new BlockItemBanner(banner.blockID-256)).setItemName("banner");
		
		NetworkRegistry.instance().registerGuiHandler(this, PROXY);
		MinecraftForge.EVENT_BUS.register(new MBCommanderForgeEvents());

		EntityRegistry.registerModEntity(EntityMBMeleeUnit.class, "M&B_Unit", 
				config.get(ConfigHelper.CAT_UNITS_IDS, "MBMeleeUnit", 212).getInt(),
				this, 50, 3, true);
		EntityRegistration er = EntityRegistry.instance().lookupModSpawn(EntityMBMeleeUnit.class, false);
		
		//Trumpet Recipies
		GameRegistry.addRecipe(new ItemStack(trumpet), 
				new Object[]{
			"# ", " #", Character.valueOf('#'), Item.ingotIron
		});

		for(int i = 0; i < 16; i++){
			GameRegistry.addRecipe(new ItemStack(banner, 1, i), 
					new Object[]{
				"#", "#", "S", 
				Character.valueOf('#'), new ItemStack(Block.cloth, 1, 15-i),
				Character.valueOf('S'), Item.stick
			});
			
			for(int j = 0; j < 16; j++){
				GameRegistry.addShapelessRecipe(new ItemStack(banner, 1, i), 
						new Object[]{
					new ItemStack(Item.dyePowder, 1, i),
					new ItemStack(banner, 1, j)
				});
			}
			
		}
	}
	
	@PostInit
	public void PostInitialization(FMLPostInitializationEvent event){
		PROXY.registerRenderInformation();
		PROXY.loadLanguages();
		PROXY.registerPlayerTracker();
		NetworkRegistry.instance().registerChatListener(new MBChatListener());
		config.save();
		GameRegistry.registerCraftingHandler(new MBCraftingHandeler());
		UpdateManager.registerMod(new CommanderUpdateHandeler(ModConverter.getMod(getClass())));
	}
	
	@ServerStarting
	public void ServerStarting(FMLServerStartingEvent event){
		event.registerServerCommand(new MBStartGame());
		event.registerServerCommand(new MBRestartGame());
		event.registerServerCommand(new MBStopGame());
	}
}
