package mab.common.commander.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.InstructionAdapter;

public class RenderPlayerVisiter extends ClassVisitor{
	
	public static final String methodName = "a";
	public static final String descript = "(Lmd;Ljava/lang/String;DDDI)V";

	public RenderPlayerVisiter(int api) {
		super(api);
	}

	@Override
	public void visitEnd() {
		MethodVisitor mv = this.cv.visitMethod(Opcodes.ACC_PROTECTED, "a", "(Lmd;Ljava/lang/String;DDDI)V", null, null);
		
		//mv = cv.visitMethod(Opcodes.ACC_PROTECTED, "a", "(Lmd;Ljava/lang/String;DDDI)V", null, null);
		mv.visitCode();
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.ALOAD, 2);
		mv.visitVarInsn(Opcodes.DLOAD, 3);
		mv.visitVarInsn(Opcodes.DLOAD, 5);
		mv.visitVarInsn(Opcodes.DLOAD, 7);
		mv.visitVarInsn(Opcodes.ILOAD, 9);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "bby", "a", "(Lmd;Ljava/lang/String;DDDI)V");
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitTypeInsn(Opcodes.CHECKCAST, "qx");
		mv.visitVarInsn(Opcodes.DLOAD, 3);
		mv.visitVarInsn(Opcodes.DLOAD, 5);
		mv.visitVarInsn(Opcodes.DLOAD, 7);
		mv.visitVarInsn(Opcodes.ILOAD, 9);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, "bcd", "b", "Lbbj;");
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "mab/common/commander/asm/RenderPlayerSymbol", "doRenderPlayerSymbol", "(Lqx;DDDILbbj;)V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(10, 10);
		mv.visitEnd();
		
		super.visitEnd();
	}

	

}
