package Compile;

import Absyn.*;

public class CodeGenerator {
	public Program root;
	private int regNum = 1;
	
	public CodeGenerator(Program p) {
		root = p;
	}
			
	
	public void start() {
		visit(root);
	}
	
	private int newRegister() {
		return regNum++;
	}
	
	private String Reg(int num) {
		return "VR(" + num + ")";
	}
	
	private String Mem(int num) {
		return "MEM(" + num + ")";
	}
	
	private String MemRef(int num) {
		return "MEM(" + num + ")@";
	}
	
	public void visit(Program p) {
		System.out.println("AREA\t\t");
		System.out.println("AREA\t\t");
		System.out.println("AREA\t\tVR");
		System.out.println("AREA\t\tMEM");
		
		DeclList dl = p.dlist;
		FuncList fl = p.flist;
		
	}

	public int visit(Exp e) {
		if (e instanceof ArrayExp) {
			ArrayExp a = (ArrayExp) e;
			System.out.print(a.s);
			System.out.print("[");
			Exp index = ((ArrayExp) e).index;
			visit(index);
			System.out.print("]");
		}
		else if (e instanceof BinOpExp) {
			BinOpExp b = (BinOpExp) e;
			
			int LReg = visit(b.left);
			int RReg = visit(b.right);
			int newReg = newRegister();
			
			emit(b.getOp(), Reg(LReg), Reg(RReg), Reg(newReg));
			
			return newReg;
		}
		else if (e instanceof CallExp) {
			
		}
		else if (e instanceof FloatExp) {
			FloatExp f = (FloatExp) e;
			System.out.print(f.value);
		}
		else if (e instanceof IdExp) {
			int newReg = newRegister();
			
			System.out.println("LOAD\t\t" + );
			
			return newReg;
		}
		else if (e instanceof IntExp) {
			IntExp i = (IntExp) e;
			
			return 0;
		}
		else if (e instanceof UnOpExp) {
			UnOpExp u = (UnOpExp) e;
			System.out.print("-");
			visit(u.exp);
		}
		else if (e instanceof F2IExp) {
			System.out.print("((int) ");
			visit(((F2IExp) e).child);
			System.out.print(")");
		}
		else if (e instanceof I2FExp) {
			System.out.print("((float) ");
			visit(((I2FExp) e).child);
			System.out.print(")");
		}
	}
}