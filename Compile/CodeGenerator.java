package Compile;

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
        decl += "AREA\t\tMEM\n";

        String instr = "LAB\tSTART\n";
        //TODO initialize SP, FP
        //TODO call main function
        instr += emit(this.root);
        instr += "LAB\tEND\n";
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
	
	private String MemRef(int num) {
		return "MEM(" + num + ")@";
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
        instr += makeCode(ADD, SP, Reg(tmp), SP);
        return instr;
    }

    private String popStack(int n) {
        String instr = "";

        int tmp = newRegister();
        instr += makeCode(MOVE, Value(n), Reg(tmp));
        instr += makeCode(SUB, SP, Reg(tmp), SP);
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

        if (ast.paramList != null) {
            instr += emit(ast.paramList);
        }
        instr += emit(ast.compoundStmt);

        instr += makeCode(LAB, fname + "EXIT");
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
        //TODO
        return "";
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
        String loopInit = "LOOP" + Value(this.branchCount++);
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
        String branchName = "BRANCH" + Value(this.branchCount++);
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
        //TODO
        return "";
    }

    private String emit(SwitchStmt ast) {
        //TODO
        return "";
    }

    private String emit(WhileStmt ast) {
        String loopInit = "LOOP" + Value(this.branchCount++);
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
        int baseAddr = 0;
        
        instr += emit(ast.index);

        int newReg = newRegister();
        int offsetReg = newRegister();
        int indexResult = ast.index.reg;

        if (!s.isGlobal(table)) {
            baseAddr = 0; //TODO : Set current ebp 
        }
        
        instr += makeCode(MOVE, Value(baseAddr), Reg(offsetReg));
        instr += makeCode(ADD, RegRef(offsetReg), RegRef(indexResult), Reg(offsetReg));
        instr += makeCode(MOVE, "M(" + "VR(" + offsetReg + ")@" + ")@", Reg(newReg));
//TODO: No Helper function
//        instr += makeCode(MOVE, MemRef(RegRef(offsetReg)), Reg(newReg));

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
       
        switch(ast.op) {
            case PLUS:
                instr += makeCode(ADD, RegRef(LReg), RegRef(RReg), Reg(newReg));
                break;
            case MINUS:
                instr += makeCode(SUB, RegRef(LReg), RegRef(RReg), Reg(newReg));
                break;
            case MULT:
                instr += makeCode(MUL, RegRef(LReg), RegRef(RReg), Reg(newReg));
                break;
            case DIV:
                instr += makeCode(DIV, RegRef(LReg), RegRef(RReg), Reg(newReg));
                break;
            case LT:
                comp = newRegister();
                instr += makeCode(SUB, RegRef(LReg), RegRef(RReg), Reg(comp));
                instr += makeCode(JMPN, RegRef(comp), ExpLabel);
                instr += makeCode(MOVE, Value(0), Reg(newReg)); 
                instr += makeCode(JMP, ExpLabel+"EXIT");
                instr += makeCode(LAB, ExpLabel);
                instr += makeCode(MOVE, Value(1), Reg(newReg));
                instr += makeCode(LAB, ExpLabel+"EXIT");
                break;
            case GT:
                comp = newRegister();
                instr += makeCode(SUB, RegRef(RReg), RegRef(LReg), Reg(comp));
                instr += makeCode(JMPN, RegRef(comp), ExpLabel);
                instr += makeCode(MOVE, Value(0), Reg(newReg)); 
                instr += makeCode(JMP, ExpLabel+"EXIT");
                instr += makeCode(LAB, ExpLabel);
                instr += makeCode(MOVE, Value(1), Reg(newReg));
                instr += makeCode(LAB, ExpLabel+"EXIT");
                break;
            case LTEQ:
                comp = newRegister();
                instr += makeCode(SUB, RegRef(RReg), RegRef(LReg), Reg(comp));
                instr += makeCode(JMPN, RegRef(comp), ExpLabel);
                instr += makeCode(MOVE, Value(1), Reg(newReg)); 
                instr += makeCode(JMP, ExpLabel+"EXIT");
                instr += makeCode(LAB, ExpLabel);
                instr += makeCode(MOVE, Value(0), Reg(newReg));
                instr += makeCode(LAB, ExpLabel+"EXIT");
                break;
            case NOTEQ:
                comp = newRegister();
                instr += makeCode(SUB, RegRef(LReg), RegRef(RReg), Reg(comp));
                instr += makeCode(JMPZ, RegRef(comp), ExpLabel);
                instr += makeCode(MOVE, Value(1), Reg(newReg));
                instr += makeCode(JMP, ExpLabel+"EXIT");
                instr += makeCode(LAB, ExpLabel);
                instr += makeCode(MOVE, Value(0), Reg(newReg));
                instr += makeCode(LAB, ExpLabel+"EXIT");
                break;
            case EQEQ:
                comp = newRegister();
                instr += makeCode(SUB, RegRef(LReg), RegRef(RReg), Reg(comp));
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
    	return "";
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
        int baseAddr = 0;

        int newReg = newRegister();

        if (!s.isGlobal(table)) {
            baseAddr = 0; //TODO : Set current ebp 
        }
        instr += makeCode(MOVE, MemRef(baseAddr + offset), Reg(newReg));

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
    	String instr;
        int reg = ast.exp.reg;
        int newReg = newRegister();

        instr = makeCode(MUL, Value(-1), RegRef(reg), Reg(newReg));

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
    
    private String emit(Symboll s) {
    	String instr = "";
    	
    	return instr;
    }
}
