package mab.common.commander.asm;

import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.ASMTransformer;
import cpw.mods.fml.relauncher.IClassTransformer;

public class RenderPlayerTransformer implements IClassTransformer {
	@Override
	public byte[] transform(String name, byte[] bytes) {
		if (!name.equals(this.getObfName()) || FMLCommonHandler.instance().getSide() != Side.CLIENT) {
	            return bytes;
		}
		
		ClassReader cr = new ClassReader(bytes);
		System.out.println(cr.getClassName());

		ClassNode cn = new ClassNode(Opcodes.ASM4);
		
		

		return bytes;
	}

	private String getObfName() {
		return "bcd";
	}

}
