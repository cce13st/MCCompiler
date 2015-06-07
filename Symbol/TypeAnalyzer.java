package Symbol;

import java.util.ArrayList;

import Absyn.ArgList;
import Absyn.ArrayExp;
import Absyn.Assign;
import Absyn.AssignStmt;
import Absyn.BinOpExp;
import Absyn.CallExp;
import Absyn.CallStmt;
import Absyn.Case;
import Absyn.CaseList;
import Absyn.CompoundStmt;
import Absyn.Decl;
import Absyn.DeclList;
import Absyn.Exp;
import Absyn.FloatExp;
import Absyn.ForStmt;
import Absyn.FuncList;
import Absyn.Function;
import Absyn.I2FExp;
import Absyn.IdExp;
import Absyn.IdentList;
import Absyn.Identifier;
import Absyn.IfStmt;
import Absyn.IntExp;
import Absyn.ParamList;
import Absyn.Program;
import Absyn.RetStmt;
import Absyn.Stmt;
import Absyn.StmtList;
import Absyn.SwitchStmt;
import Absyn.Type;
import Absyn.UnOpExp;
import Absyn.WhileStmt;

class TypeAnalyzer {
	public Table table;
	public Scope global;
	public Program root;

	private Scope current;
	private int currentIdx;
	private Stack scopeStack;
	
	private boolean resultValidity = true;

	public TypeAnalyzer(Table t, Program p) {
		this.table = t;
		this.global = t.global;
		root = p;
		scopeStack = new Stack();
	}

	public boolean startAnalysis() {
		current = global;
		currentIdx = 0;

		System.out.println("================ Type analysis start ================");
		visit(root);
		

		System.out.println("================ Type analysis end ================");
		System.out.println("");
		return resultValidity;
	}

	public void visit(Program p) {
		DeclList dl = p.dlist;
		FuncList fl = p.flist;
		if (dl != null)
			visit(dl);

		if (fl != null)
			visit(fl);
	}

	public void visit(DeclList dl) {
		for (int i = 0; i < dl.length; i++)
			visit(dl.get(i));
	}

	public void visit(FuncList fl) {
		currentIdx = 0;
		for (int i = 0; i < fl.length; i++) {
			ScopeStore();
			visit(fl.get(i));
			ScopeRecovery();
		}
	}

	public void visit(Decl d) {
		Type type = d.type;
		IdentList il = d.ilist;

		for (int i = 0; i < il.length; i++) {
			Identifier id = il.get(i);
			Symbol s = id.s;

			if (s.isDuplicated()) {
				StaticError.DuplicatedDeclaration(s, current, s.line, s.pos);
				resultValidity = false;
			}
			if (s.type != type.ty) {
				StaticError.TypeMismatched(s, type.ty, s.line, s.pos);
				resultValidity = false;
			}
		}
	}

	public void visit(Function f) {
		visit(f.compoundStmt, false);
	}

	public void visit(CompoundStmt cs, boolean block) {
		if (block) {
			Scope prev = current;
		}
		else {
			if (cs.dlist != null)
				visit(cs.dlist);
			if (cs.slist != null)
				visit(cs.slist);
		}
	}

	public void visit(StmtList sl) {
		for (int i = 0; i < sl.length; i++) {
			visit(sl.get(i), true);
		}
	}

	public void visit(Stmt s, boolean block) {
		if (s instanceof AssignStmt) {
			AssignStmt a = (AssignStmt) s;
			visit(a.assign);
		}
		else if (s instanceof CompoundStmt) {
			CompoundStmt c = (CompoundStmt) s;
			visit(c, block);
		}
		else if (s instanceof ForStmt) {
			ScopeStore();

			ForStmt f = (ForStmt) s;

			visit(f.init);
			ExpInfo condInfo = visit(f.cond);
			if (condInfo == null || !condInfo.isBoolean()) {
				StaticError.NotConditionExp(f.cond.line, f.cond.pos);
			}
			
			visit(f.post);
			visit(f.body, false);

			ScopeRecovery();
		}
		else if (s instanceof IfStmt) {
			IfStmt i = (IfStmt) s;
			ExpInfo condInfo = visit(i.cond);
			
			if (condInfo == null || !condInfo.isBoolean()) {
				StaticError.NotConditionExp(i.cond.line, i.cond.pos);
			}

			ScopeStore();

			visit(i.thenClause, false);

			ScopeRecovery();

			if (i.elseClause != null) {
				ScopeStore();

				visit(i.elseClause, false);

				ScopeRecovery();
			}
		}
		else if (s instanceof WhileStmt) {
			WhileStmt w = (WhileStmt) s;
			ExpInfo condInfo = visit(w.cond);
			if (condInfo == null || !condInfo.isBoolean()) {
				StaticError.NotConditionExp(w.cond.line, w.cond.pos);
			}

			ScopeStore();

			visit(w.body, false);

			ScopeRecovery();
		}
		else if (s instanceof SwitchStmt) {
			SwitchStmt sw = (SwitchStmt) s;
			if (!sw.id.s.isDeclared()) {
				StaticError.VarNotDeclared(sw.id.s, current, sw.id.line, sw.id.pos);
				resultValidity = false;
			}
			visit(sw.clist);
		}
		else if (s instanceof CallStmt) {
			CallStmt c = (CallStmt) s;
			visit(c.exp);
		}
		else if (s instanceof RetStmt) {
			RetStmt r = (RetStmt) s;
			Scope funcScope = current.getFuncScope();
			Function f = table.funcMap.get(funcScope.loc);
			if (r.exp != null) {
				ExpInfo retInfo = visit(r.exp);
				
				if(retInfo == null)
					return;
				
				if (retInfo.type != f.type.ty) {
					if (retInfo.type == Type.type.INT)
						return;
					
					StaticError.WarnRetType(f.id, r.line, r.pos);
				}
			}
			else {
				StaticError.WarnNoRet(f.id, r.line, r.pos);
			}
		}
	}

	public void visit(CaseList cl) {
		for (int i = 0; i < cl.length; i++) {
			Case c = cl.get(i);
			visit(c.slist);
		}
	}

	public ExpInfo visit(Exp e) {
		if (e instanceof ArrayExp) {
			// Array symbol이 정의되었는지

			ArrayExp a = (ArrayExp) e;

			// Array symbol declared?
			if (!a.s.isDeclared()) {
				StaticError.VarNotDeclared(a.s, current, a.s.line, a.s.pos);
				resultValidity = false;
			}

			// The type of array symbol
			if(current.lookup(a.s.name).array == 0) {
				StaticError.VarNotArray(a.s, a.line, a.pos);
				resultValidity = false;
				return null;
			}

			Exp index = ((ArrayExp) e).index;
			ExpInfo ei = visit(index);
			
			if (ei == null || !ei.isInteger()) {
				System.err.println("this is not integer index");
				return null;
			}

			return new ExpInfo(a.s.type);
		} 
		else if (e instanceof BinOpExp) {

			BinOpExp b = (BinOpExp) e;
			ExpInfo li = visit(b.left);
			ExpInfo ri = visit(b.right);
			
			if(li == null || ri == null)
				return null;

			if (!li.isNumber() || !ri.isNumber()) {
				System.err.println("Operation type mismatched - BinOp : " + b.op);
				return null;
			}

			if (b.op == BinOpExp.Op.DIV || b.op == BinOpExp.Op.MINUS
					|| b.op == BinOpExp.Op.MULT || b.op == BinOpExp.Op.PLUS) {
				Type.type conv;
				
				if (li.isInteger() && ri.isInteger())
					conv = Type.type.INT;
				else {
					if (li.isInteger()) {
						I2FExp i2f = new I2FExp(b.left.line, b.left.pos, b.left);
						i2f.child = b.left;
						b.left = i2f;
					}
					if (ri.isInteger()) {
						I2FExp i2f = new I2FExp(b.right.line, b.right.pos, b.right);
						i2f.child = b.right;
						b.right = i2f;
					}
					conv = Type.type.FLOAT;
				}
				
				ExpInfo result = new ExpInfo(conv);
				result.complete = true;
				return result;
			}
			else {
				if (li.isInteger() && !ri.isInteger()) {
					I2FExp i2f = new I2FExp(b.left.line, b.left.pos, b.left);
					i2f.child = b.left;
					b.left = i2f;
				}
				if (!li.isInteger() && ri.isInteger()) {
					I2FExp i2f = new I2FExp(b.right.line, b.right.pos, b.right);
					i2f.child = b.right;
					b.right = i2f;
				}
				
				ExpInfo result = new ExpInfo(Type.type.INT);
				result.complete = true;
				result.isBool = true;
				return result;
			}
		} 
		else if (e instanceof CallExp) {
			CallExp c = (CallExp) e;

			// Check if function declared
			Function f = table.funcMap.get(c.funcName);
			if (f == null) {
				StaticError.FuncNotDeclared(c.funcName, current, c.line, c.pos);
				resultValidity = false;
				return null;
			}
			else
				c.func = f;

			if (c.args != null) {
				ArgList al = c.args;
				ParamList pl = f.paramList;
				
				if (pl.length != al.length) {
					StaticError.ArgsNumber(c.funcName, c.line, c.pos);
					resultValidity = false;
					return null;
				}
				
				boolean valid = true;
				
				for (int i=0; i<al.length; i++) {
					Exp arg = al.get(i);

					// Check Array Pointer passing
					if (arg instanceof IdExp) {
						Symbol s = ((IdExp) arg).s;
						
						if (!s.isDeclared()) {
							StaticError.VarNotDeclared(s, current, s.line, s.pos);
							resultValidity = false;
							valid = false;
							continue;
						}
						
						boolean paramArray;
						if (pl.ilist.get(i).index == null)
							paramArray = false;
						else
							paramArray = true;

						if (paramArray != (s.array > 0)) {
							StaticError.NotArrayArg(s.line, s.pos);
							resultValidity = false;
							valid = false;
							continue;
						}

						Type paramType = pl.tlist.get(i);

						// Type checking with parameter type
						if (s.type != paramType.ty) {
							StaticError.TypeMismatched(paramType.ty, arg.line, arg.pos);
							resultValidity = false;
							valid = false;
							continue;
						}
					}
					else {
						ExpInfo argInfo = visit(arg);
						Type paramType = pl.tlist.get(i);
						
						if (argInfo == null) {
							StaticError.InvalidArg(arg.line, arg.pos);
							valid = false;
							resultValidity = false;
							continue;
						}

						boolean paramArray;
						if (pl.ilist.get(i).index == null)
							paramArray = false;
						else
							paramArray = true;
						
						if (paramArray) {
							StaticError.NotArrayParam(arg.line, arg.pos);
							resultValidity = false;
							valid = false;
							continue;
						}
						
						// Type checking with parameter type
						if (argInfo.type != paramType.ty) {
							StaticError.TypeMismatched(paramType.ty, arg.line, arg.pos);
							resultValidity = false;
							valid = false;
							continue;
						}
					}
				}
				
				if (!valid)
					return null;
			}
			
			// Return the function's return type
			return new ExpInfo(f.type.ty);
		} 
		else if (e instanceof IntExp) {
			ExpInfo ei = new ExpInfo(Type.type.INT);
			return ei;
		} 
		else if (e instanceof FloatExp) {
			ExpInfo ei = new ExpInfo(Type.type.FLOAT);
			return ei;
		} 
		else if (e instanceof IdExp) {
			IdExp i = (IdExp) e;
			if (!i.s.isDeclared()) {
				StaticError.VarNotDeclared(i.s, current, i.line, i.pos);
				resultValidity = false;
				return null;
			}

			if(current.lookup(i.s.name).array != 0) {
				StaticError.ArrayWithoutIndex(i.s, i.line, i.pos);
				resultValidity = false;
				return null;
			}

			ExpInfo ei = new ExpInfo(i.s.type);
			return ei;
		} 
		else if (e instanceof UnOpExp) {
			// Number인지 검사
			UnOpExp u = (UnOpExp) e;
			ExpInfo descend = visit(u.exp);
			
			if (descend == null)
				return null;

			if (!descend.isNumber()) {
				StaticError.TypeMismatched(Type.type.INT, u.line, u.pos);
				resultValidity = false;
			}

			return descend;
		}
		return null;
	}

	public void visit(Assign a) {
		ExpInfo ri = visit(a.rhs);

		// Check ID is declared
		if (!a.s.isDeclared()) {
			StaticError.VarNotDeclared(a.s, current, a.line, a.pos);
			resultValidity = false;
			return;
		}
		
		// Result of visiting RHS
		if (ri == null) {
			return;
		}
		
		// Type of RHS and ID does not match
		if (ri.type != a.s.type) {
			if (ri.type == Type.type.INT) {
				a.addI2F();
				return;
			}
			if (ri.type == Type.type.FLOAT && a.s.type == Type.type.INT) {
				a.addF2I();
				StaticError.WarnConversion(a.s, a.line, a.pos);
				return;
			}
			StaticError.TypeMismatched(a.s, ri.type, a.line, a.pos);
			resultValidity = false;
		}

		// If array index exists
		if (a.index != null) {
			ExpInfo ei = visit(a.index);
			if (ei == null || !ei.isInteger()) {
				StaticError.NotInteger(a.line, a.pos);
				resultValidity = false;
				return;
			}
		}
		
		// Check ID is an array type
		if(current.lookup(a.s.name).array == 0 && a.index != null) {
			StaticError.VarNotArray(a.s, a.line, a.pos);
			resultValidity = false;
			return;
		}
		if(current.lookup(a.s.name).array != 0 && a.index == null) {
			StaticError.ArrayWithoutIndex(a.s, a.line, a.pos);
			resultValidity = false;
			return;
		}
	}

	private void ScopeStore() {
		/* Store current scope information into stack */
//		System.err.println("Enter in ScopeStore - " + current.getLocName() + " : " + currentIdx);
		scopeStack.putScope(current);
		scopeStack.putIdx(currentIdx);
		current = current.descend.get(currentIdx);
		currentIdx = 0;
//		System.err.println("Now in Scope - " + current.getLocName() + " : " + currentIdx);
	}

	private void ScopeRecovery() {
		current = scopeStack.popScope();
		currentIdx = scopeStack.popIdx()+1;
//		System.err.println("Enter in ScopeRecovery - " + current.getLocName() + " : " + currentIdx);
	}
}

class ExpInfo {
	public boolean complete = true;
	public Type.type type;
	public boolean isBool;

	public ExpInfo(Type.type t) {
		type = t;
		if (type != null)
			isBool = true;
	}

	public boolean isComplete() {
		return complete;
	}

	public boolean isInteger() {
		return type == Type.type.INT;
	}

	public boolean isNumber() {
		return (!(type == Type.type.FLOAT) || !(type == Type.type.INT));
	}

	public boolean isBoolean() {
		return isBool;
	}
}

class Stack {
	private ArrayList<Scope> scopeList;
	private ArrayList<Integer> idxList;
	private int length;
	
	public Stack() {
		scopeList = new ArrayList<Scope>();
		idxList = new ArrayList<Integer>();
		length = 0;
	}
	
	public Scope popScope() {
		Scope s = scopeList.remove(length-1);
		return s;
	}
	public int popIdx() {
		Integer i = idxList.remove(length-1);
		length--;
		return i.intValue();
	}
	
	public void putScope(Scope s) {
		scopeList.add(s);
	}
	public void putIdx(int i) {
		idxList.add(i);
		length++;
	}
}
