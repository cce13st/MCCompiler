package Compile;

import java.util.ArrayList;
import java.util.Iterator;

import Absyn.*;
import Symbol.Symboll;
import Symbol.Table;

/**
 * Created by yeonni on 15. 6. 7..
 */
public class CodeGenerator {
	public Program root;
	public Table table;
	private int regNum = 1;
	private int branchCount = 1;
	private int expCount = 1;
	private int callCount = 1;

	private final String SP = "SP";
	private final String FP = "FP";

	private final String MOVE = "MOVE";
	private final String ADD = "ADD";
	private final String SUB = "SUB";
	private final String MUL = "MUL";
	private final String DIV = "DIV";
	private final String I2F = "I2F";
	private final String FADD = "FADD";
	private final String FSUB = "FSUB";
	private final String FMUL = "FMUL";
	private final String FDIV = "FDIV";
	private final String F2I = "F2I";
	private final String TOZ = "TOZ";
	private final String JMP = "JMP";
	private final String JMPZ = "JMPZ";
	private final String JMPN = "JMPN";
	private final String LAB = "LAB";
	private final String READI = "READI";
	private final String READF = "READF";
	private final String WRITE = "WRITE";

	public CodeGenerator(Program p, Table t) {
		this.root = p;
		this.table = t;
	}

	public String emit() {
		String decl = "AREA\t\tSP\n";
		decl += "AREA\t\tFP\n";
		decl += "AREA\t\tVR\n";
		decl += "AREA\t\tMEM\n\n";

		String instr = "LAB\t\tSTART\n";
		//TODO initialize SP, FP
		
		instr += makeCode(MOVE, Value(0), FP);
		instr += makeCode(MOVE, Value(0), SP);

		instr += emit(this.root);
		instr += "LAB\t\tEND\n";
		return decl + instr;
	}


	/* Helper Code for printing T-machine code */
	private int newRegister() {
		return this.regNum++;
	}

	private String Value(int num) {
		return Integer.toString(num);
	}

	private String Value(float num) {
		return Float.toString(num);
	}

	private String Reg(int num) {
		return "VR(" + num + ")";
	}

	private String RegRef(int num) {
		return "VR(" + num + ")@";
	}

	private String Mem(int num) {
		return "MEM(" + num + ")";
	}

	private String Mem(String composit) {
		return "MEM(" + composit + ")";
	}

	private String MemRef(int num) {
		return "MEM(" + num + ")@";
	}

	private String MemIndirectRef(int num) {
		return "MEM(VR(" + num + ")@)@";
	}

	private String MemRef(String composit) {
		return "MEM(" + composit + ")@";
	}

	/* make formatted T code */
	private String makeCode(String instr, String m1, String m2, String m3) {
		return instr + "\t\t" + m1 + "\t" + m2 + "\t" + m3 + "\n";
	}

	private String makeCode(String instr, String m1, String m2) {
		return instr + "\t\t" + m1 + "\t" + m2 + "\n";
	}

	private String makeCode(String instr, String m1) {
		return instr + "\t\t" + m1 + "\n";
	}

	private String pushStack(int n) {
		String instr = "";

		int tmp = newRegister();
		instr += makeCode(MOVE, Value(n), Reg(tmp));
		instr += makeCode(ADD, SP + "@", RegRef(tmp), SP);
		return instr;
	}

	private String popStack(int n) {
		String instr = "";

		int tmp = newRegister();
		instr += makeCode(MOVE, Value(n), Reg(tmp));
		instr += makeCode(SUB, SP + "@", RegRef(tmp), SP);
		return instr;
	}


	/* emit functions */

	private String emit(Program p) {

		DeclList dl = p.dlist;
		FuncList fl = p.flist;
		String instr = "";

		if (dl != null) {
			instr += emit(dl);
		}
		System.out.println(instr);

		/* Find main function and jump to it */
		Function main = table.funcMap.get("main");
		if (main != null) {
			CallExp e = new CallExp(0, 0, main.id);
			e.func = main;
			instr += emit(e);
		}
		instr += makeCode(JMP, "END");
		
		instr += "\n";
		if (fl != null) {
			instr += emit(fl);
		}
		
		return instr;
	}

	private String emit(DeclList ast) {
		String instr = "";

		Iterator<Decl> iter = ast.list.iterator();
		Decl item;
		while (iter.hasNext()) {
			item = iter.next();
			instr += emit(item);
		}

		return instr;
	}

	private String emit(FuncList ast) {
		String instr = "";

		Iterator<Function> iter = ast.list.iterator();
		Function item;
		while (iter.hasNext()) {
			item = iter.next();
			instr += emit(item);
		}

		return instr;
	}

	private String emit(Decl ast) {
		String instr = "";
		int size = 0;

		Iterator<Symboll> iter = ast.slist.iterator();
		Symboll item;
		while (iter.hasNext()) {
			item = iter.next();
			if (item.array > 0)
				size += item.array;
			else
				size += 1;
		}

		instr += pushStack(size);

		return instr;
	}

	private String emit(Function ast) {
		String fname = "FUNCTION" + ast.id;
		String instr = makeCode(LAB, fname);

		/* store current FP */
		instr += pushStack(1);
		instr += makeCode(MOVE, FP + "@", Mem(SP + "@"));

		/* change FP to current SP */
		instr += makeCode(MOVE, SP + "@", FP);

		if (ast.paramList != null) {
			instr += emit(ast.paramList);
		}
		instr += emit(ast.compoundStmt);

		instr += makeCode(LAB, fname + "EXIT");

		/* change current SP to FP */
		instr += makeCode(MOVE, FP + "@", SP);
		/* restore old FP from stack */
		instr += makeCode(MOVE, SP + "@", FP);
		instr += popStack(1);

		/* get return address */
		instr += makeCode(JMP, MemRef(SP + "@"));
		
		return instr;
	}

	private String emit(ParamList ast) {
		/* This is a leaf node! */
		String instr = "";

		/*
        Iterator<Symboll> iter = ast.slist.iterator();

        Symboll item;
        while (iter.hasNext()) {
            item = iter.next();
        }
		 */

		return instr;
	}

	private String emit(StmtList ast) {
		String instr = "";
		Iterator<Stmt> iter = ast.list.iterator();
		Stmt item;
		while(iter.hasNext()) {
			item = iter.next();
			instr += emit(item);
		}
		return instr;
	}

	private String emit(Stmt ast) {
		if (ast instanceof AssignStmt) {
			return emit((AssignStmt) ast);
		} else if (ast instanceof CallStmt) {
			return emit((CallStmt) ast);
		} else if (ast instanceof CompoundStmt) {
			return emit((CompoundStmt) ast);
		} else if (ast instanceof EmptyStmt) {
			return emit((EmptyStmt) ast);
		} else if (ast instanceof ForStmt) {
			return emit((ForStmt) ast);
		} else if (ast instanceof IfStmt) {
			return emit((IfStmt) ast);
		} else if (ast instanceof RetStmt) {
			return emit((RetStmt) ast);
		} else if (ast instanceof SwitchStmt) {
			return emit((SwitchStmt) ast);
		} else if (ast instanceof WhileStmt) {
			return emit((WhileStmt) ast);
		}
		return "";
	}

	private String emit(AssignStmt ast) {
		return emit(ast.assign);
	}

	private String emit(Assign ast) {
		String instr = "";
		Symboll s = ast.s;
		int offset = s.getOffset();

		instr += emit(ast.rhs);

		int offsetReg = newRegister();
		int rhsReg = ast.rhs.reg;

		if (ast.index != null) {
			instr += emit(ast.index);
			instr += makeCode(MOVE, Value(offset), Reg(offsetReg));
			instr += makeCode(ADD, RegRef(ast.index.reg), RegRef(offsetReg), Reg(offsetReg));
			if (!s.isGlobal())
				instr += makeCode(ADD, FP+"@", RegRef(offsetReg), Reg(offsetReg));

			instr += makeCode(MOVE, RegRef(rhsReg), Mem(RegRef(offsetReg)));
		}
		else {
			instr += makeCode(MOVE, Value(offset), Reg(offsetReg));
			if (!s.isGlobal())
				instr += makeCode(ADD, FP+"@", RegRef(offsetReg), Reg(offsetReg));

			instr += makeCode(MOVE, RegRef(rhsReg), Mem(RegRef(offsetReg)));
		}

		return instr;
	}

	private String emit(CallStmt ast) {
		return emit((CallExp) ast.exp);
	}

	private String emit(CompoundStmt ast) {
		String instr = "";
		if (ast.dlist != null) {
			instr += emit(ast.dlist);
		}
		instr += emit(ast.slist);
		return instr;
	}

	private String emit(EmptyStmt ast) {
		return "";
	}

	private String emit(ForStmt ast) {
		String loopInit = "FOR" + Value(this.branchCount++);
		String loopExit = loopInit + "EXIT";
		String instr = emit(ast.init);
		instr += makeCode(LAB, loopInit);
		instr += emit(ast.cond);
		instr += makeCode(JMPZ, Reg(ast.cond.reg), loopExit);
		instr += emit(ast.body);
		instr += emit(ast.post);
		instr += makeCode(JMP, loopInit);

		instr += makeCode(LAB, loopExit);
		return instr;
	}

	private String emit(IfStmt ast) {
		String branchName = "IF" + Value(this.branchCount++);
		String instr = emit(ast.cond);
		instr += makeCode(JMPZ, Reg(ast.cond.reg), "F" + branchName);
		instr += emit(ast.thenClause);
		instr += makeCode(JMP, branchName + "EXIT");
		instr += makeCode(LAB, "F" + branchName);
		if (ast.elseClause != null) {
			instr += emit(ast.elseClause);
		}
		instr += makeCode(LAB, branchName + "EXIT");
		return instr;
	}

	private String emit(RetStmt ast) {

		String instr = "";

		if (ast.exp != null) {
			/* VR(0) is reserved for return value */
			instr += emit(ast.exp);
			instr += makeCode(MOVE, MemRef(RegRef(ast.exp.reg)), Reg(0));
		}
		
		instr += makeCode(JMP, "FUNCTION" + ast.func_id + "EXIT");

		return instr;
	}

	private String emit(SwitchStmt ast) {
		String instr = "";
		//VR(newReg) = SP + Value(ast.id.offset)
		int offsetReg = newRegister();
		instr += makeCode(MOVE, Value(ast.id.offset), Reg(offsetReg));

		int idReg = newRegister();
		if (ast.id.isGlobal()) {
			int globalBaseOffsetReg = newRegister();
			//TODO : check: global base = 1 as 0 is reserved for return value
			instr += makeCode(MOVE, Value(1), Reg(globalBaseOffsetReg));
			instr += makeCode(ADD, RegRef(globalBaseOffsetReg), RegRef(offsetReg), Reg(idReg));
		} else {
			instr += makeCode(ADD, SP + "@", RegRef(offsetReg), Reg(idReg));
		}

		String branchName = "SWITCH" + Value(this.branchCount++);
		String branchExit = branchName + "EXIT";

		/*
        TODO change it to call emit(CaseList ast)
        prob: how to handle branch name T_T
		 */
		Iterator<Case> iter = ast.clist.list.iterator();
		Case item;
		String jmpTable = "";
		String body = "";
		while (iter.hasNext()) {
			item = iter.next();
			String caseName;

			if (item == ast.clist.defaultCase) {
				caseName = branchName + "DEFAULTCASE";
				jmpTable += makeCode(JMP, caseName);
			} else {
				int caseReg = newRegister();
				int tmpReg = newRegister();
				caseName = branchName + "CASE" + Value(item.casenum);
				jmpTable += makeCode(MOVE, Value(item.casenum), Reg(caseReg));
				jmpTable += makeCode(SUB, RegRef(idReg), RegRef(caseReg), Reg(tmpReg));
				jmpTable += makeCode(JMPZ, caseName);
			}

			body += makeCode(LAB, caseName);
			body += emit(item.slist);
			if (item.br) {
				body += makeCode(JMP, branchExit);
			}
		}
		instr += jmpTable + body;

		//lab case exit
		instr += makeCode(LAB, branchExit);
		return instr;
	}

	private String emit(WhileStmt ast) {
		String loopInit = "WHILE" + Value(this.branchCount++);
		String loopExit = loopInit + "EXIT";
		String instr = makeCode(LAB, loopInit);
		instr += emit(ast.cond);
		instr += makeCode(JMPZ, Reg(ast.cond.reg), loopExit);
		instr += emit(ast.body);
		instr += makeCode(JMP, loopInit);
		instr += makeCode(LAB, loopExit);
		return instr;
	}

	private String emit(Exp ast) {
		if (ast instanceof ArrayExp) {
			return emit((ArrayExp) ast);
		}
		else if (ast instanceof BinOpExp) {
			return emit((BinOpExp) ast);
		}
		else if (ast instanceof CallExp) {
			return emit((CallExp) ast);
		}
		else if (ast instanceof FloatExp) {
			return emit((FloatExp) ast);
		}
		else if (ast instanceof IdExp) {
			return emit((IdExp) ast);
		}
		else if (ast instanceof IntExp) {
			return emit((IntExp) ast);
		}
		else if (ast instanceof UnOpExp) {
			return emit((UnOpExp) ast);
		}
		else if (ast instanceof F2IExp) {
			return emit((F2IExp) ast);
		}
		else if (ast instanceof I2FExp) {
			return emit((I2FExp) ast);
		}
		return "";
	}

	private String emit(ArrayExp ast) {
		String instr = "";
		Symboll s = ast.s;
		int offset = s.getOffset();

		/* Calculate array index expression */
		instr += emit(ast.index);

		int newReg = newRegister();
		int locReg = newRegister();
		int indexReg = ast.index.reg;

		if (s.isGlobal()) {
			/* If this variable is in global scope, set base address as 0 */
			instr += makeCode(ADD, Mem(offset), RegRef(indexReg), Reg(locReg));
			// *loc = offset + index
			instr += makeCode(MOVE, MemIndirectRef(locReg), Reg(newReg));
			// *Mem[*loc]
		}
		else {
			/* base address = Frame Pointer */
			instr += makeCode(ADD, Mem(offset), FP+"@", Reg(locReg));
			// *offset = offset + FP
			instr += makeCode(ADD, RegRef(indexReg), RegRef(locReg), Reg(locReg));
			// *offset = *offset + index

			instr += makeCode(MOVE, MemIndirectRef(locReg), Reg(newReg));
		}

		ast.reg = newReg;
		return instr;
	}

	private String emit(BinOpExp ast) {
		String ExpLabel = "EXP" + Value(this.expCount++);
		String instr = "";
		int LReg = ast.left.reg;
		int RReg = ast.right.reg;
		int newReg = newRegister();
		int comp = 0;

		String add_t, sub_t, mul_t, div_t;
		if (ast.left.type == null) {
			System.out.println("ERROR: cannot compile BinOpExp: type is null");
			return "";
		}

		switch(ast.left.type) {
		case INT:
			add_t = ADD;
			sub_t = SUB;
			mul_t = MUL;
			div_t = DIV;
			break;
		case FLOAT:
			add_t = FADD;
			sub_t = FSUB;
			mul_t = FMUL;
			div_t = FDIV;
			break;
		default:
			add_t = ADD;
			sub_t = SUB;
			mul_t = MUL;
			div_t = DIV;
		}

		switch(ast.op) {
		case PLUS:
			instr += makeCode(add_t, RegRef(LReg), RegRef(RReg), Reg(newReg));
			break;
		case MINUS:
			instr += makeCode(sub_t, RegRef(LReg), RegRef(RReg), Reg(newReg));
			break;
		case MULT:
			instr += makeCode(mul_t, RegRef(LReg), RegRef(RReg), Reg(newReg));
			break;
		case DIV:
			instr += makeCode(div_t, RegRef(LReg), RegRef(RReg), Reg(newReg));
			break;
		case LT:
			comp = newRegister();
			instr += makeCode(sub_t, RegRef(LReg), RegRef(RReg), Reg(comp));
			instr += makeCode(JMPN, RegRef(comp), ExpLabel);
			instr += makeCode(MOVE, Value(0), Reg(newReg)); 
			instr += makeCode(JMP, ExpLabel+"EXIT");
			instr += makeCode(LAB, ExpLabel);
			instr += makeCode(MOVE, Value(1), Reg(newReg));
			instr += makeCode(LAB, ExpLabel+"EXIT");
			break;
		case GT:
			comp = newRegister();
			instr += makeCode(sub_t, RegRef(RReg), RegRef(LReg), Reg(comp));
			instr += makeCode(JMPN, RegRef(comp), ExpLabel);
			instr += makeCode(MOVE, Value(0), Reg(newReg)); 
			instr += makeCode(JMP, ExpLabel+"EXIT");
			instr += makeCode(LAB, ExpLabel);
			instr += makeCode(MOVE, Value(1), Reg(newReg));
			instr += makeCode(LAB, ExpLabel+"EXIT");
			break;
		case LTEQ:
			comp = newRegister();
			instr += makeCode(sub_t, RegRef(RReg), RegRef(LReg), Reg(comp));
			instr += makeCode(JMPN, RegRef(comp), ExpLabel);
			instr += makeCode(MOVE, Value(1), Reg(newReg)); 
			instr += makeCode(JMP, ExpLabel+"EXIT");
			instr += makeCode(LAB, ExpLabel);
			instr += makeCode(MOVE, Value(0), Reg(newReg));
			instr += makeCode(LAB, ExpLabel+"EXIT");
			break;
		case NOTEQ:
			comp = newRegister();
			instr += makeCode(sub_t, RegRef(LReg), RegRef(RReg), Reg(comp));
			instr += makeCode(JMPZ, RegRef(comp), ExpLabel);
			instr += makeCode(MOVE, Value(1), Reg(newReg));
			instr += makeCode(JMP, ExpLabel+"EXIT");
			instr += makeCode(LAB, ExpLabel);
			instr += makeCode(MOVE, Value(0), Reg(newReg));
			instr += makeCode(LAB, ExpLabel+"EXIT");
			break;
		case EQEQ:
			comp = newRegister();
			instr += makeCode(sub_t, RegRef(LReg), RegRef(RReg), Reg(comp));
			instr += makeCode(JMPZ, RegRef(comp), ExpLabel);
			instr += makeCode(MOVE, Value(0), Reg(newReg));
			instr += makeCode(JMP, ExpLabel+"EXIT");
			instr += makeCode(LAB, ExpLabel);
			instr += makeCode(MOVE, Value(1), Reg(newReg));
			instr += makeCode(LAB, ExpLabel+"EXIT");
			break;
		}

		ast.reg = newReg;
		return instr;
	}

	private String emit(CallExp ast) {
		String instr = "";
		int callId = this.callCount++;
		int argSize = 0;

		if (ast.args != null) {
			/* push arguments to stack in reverse order */
			ArrayList<Exp> argList = ast.args.list;
			ArrayList<Exp> reverseArgList = new ArrayList<Exp>();

			Iterator<Exp> iterArg = argList.iterator();
			Exp item;
			while (iterArg.hasNext()) {
				item = iterArg.next();
				reverseArgList.add(0, item);
			}

			Iterator<Exp> iterR = argList.iterator();
			while (iterR.hasNext()) {
				item = iterR.next();
				instr += pushStack(1);
				argSize += 1;
				instr += makeCode(MOVE, RegRef(item.reg), SP);
			}
		}

		/* push return address */
		String afterCall = "AFTERCALL" + Value(callId);
		instr += pushStack(1);
		instr += makeCode(MOVE, afterCall, Mem(SP + "@"));

		/* jmp to call */
		instr += makeCode(JMP, "FUNCTION" + ast.funcName);
		instr += makeCode(LAB, afterCall);

		/* VR(0) is reserved for return value of call */
		ast.reg = 0;

		/* restore SP */
		instr += popStack(argSize + 1);

		return instr;
	}

	private String emit(FloatExp ast) {
		String instr; 
		int newReg = newRegister();

		instr = makeCode(MOVE, Value(ast.value), Reg(newReg));

		ast.reg = newReg;
		return instr;
	}

	private String emit(IdExp ast) {
		String instr = "";
		Symboll s = ast.s;
		int offset = s.getOffset();

		int newReg = newRegister();

		if (s.isGlobal()) {
			/* If this variable is in global scope, set base address as 0 */
			instr += makeCode(MOVE, MemRef(offset), Reg(newReg));
		}
		else {
			/* base address = Frame Pointer */
			int locReg = newRegister();
			instr += makeCode(ADD, Mem(offset), FP+"@", Reg(locReg));
			instr += makeCode(MOVE, MemIndirectRef(locReg), Reg(newReg));
		}

		ast.reg = newReg;
		return instr;
	}

	private String emit(IntExp ast) {
		String instr;
		int newReg = newRegister();

		instr = makeCode(MOVE, Value(ast.value), Reg(newReg));

		ast.reg = newReg;
		return instr;
	}

	private String emit(UnOpExp ast) {
		String instr = "";
		int reg = ast.exp.reg;
		int newReg = newRegister();

		switch(ast.type) {
		case INT:
			instr += makeCode(MUL, Value(-1), RegRef(reg), Reg(newReg));
			break;
		case FLOAT:
			instr += makeCode(FMUL, Value(-1), RegRef(reg), Reg(newReg));
		}

		ast.reg = newReg;
		return instr;
	}

	private String emit(F2IExp ast) {
		String instr;
		int reg = ast.child.reg;
		int newReg = newRegister();

		instr = makeCode(F2I, Reg(reg), Reg(newReg));

		ast.reg = newReg;
		return instr;
	}

	private String emit(I2FExp ast) {
		String instr;
		int reg = ast.child.reg;
		int newReg = newRegister();

		instr = makeCode(I2F, Reg(reg), Reg(newReg));

		ast.reg = newReg;
		return instr;
	}
//
//	private String make_scanf() {
//		String instr = "";
//
//		/* argument should be variables! */
//		Exp v = ast.args.get(0);
//		instr += emit(v);
//
//		switch(v.type) {
//		case INT:
//			instr += makeCode(READI, Reg(v.reg));
//		case FLOAT:
//			instr += makeCode(READF, Reg(v.reg));
//		}
//
//		return instr;
//	}
//
//	private String make_printf() {
//		String instr = "";
//
//		/* argument should be variables! */
//		Exp v = ast.args.get(0);
//		instr += emit(v);
//		instr += makeCode(WRITE, RegRef(v.reg));
//
//		return instr;
//	}
}
