package mab.common.commander.asm;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

public class CommanderCoreMod implements IFMLLoadingPlugin {
	
	public static final String RenderPlayerClass = "mab.common.commander.asm.RenderPlayerTransformer";
	public static final String[] ASM = new String[]{RenderPlayerClass};
	
	static{
		System.out.println("Loading Coremod");
	}

	@Override
	public String[] getLibraryRequestClass() {
		return null;
	}

	@Override
	public String[] getASMTransformerClass() {
		System.out.println("get Transformer Class");
		return ASM;
	}

	@Override
	public String getModContainerClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSetupClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
		
	}

}
