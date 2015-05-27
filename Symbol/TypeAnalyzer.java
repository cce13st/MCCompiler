package Symbol;

import Absyn.*;

class TypeAnalyzer {
	public Table table;
	public Scope global;
	public Program root;

	private Scope current;
	private int currentIdx;

	private Scope prev;
	private int prevIdx;

	public TypeAnalyzer(Table t, Program p) {
		this.table = t;
		this.global = t.global;
		root = p;
	}

	public void startAnalysis() {
		current = global;
		currentIdx = 0;

		System.out.println("================ Type analysis start ================");
		System.out.println("");
		visit(root);
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
		Scope prev = current;
		for (int i = 0; i < fl.length; i++) {
			current = prev.descend.get(i);
			visit(fl.get(i));
		}
		current = prev;
	}

	public void visit(Decl d) {
		Type type = d.type;
		IdentList il = d.ilist;

		for (int i = 0; i < il.length; i++) {
			Identifier id = il.get(i);
			Symbol s = id.s;

			if (s.isDuplicated()) {
				StaticError.DuplicatedDeclaration(s, current, s.line, s.pos);
			}
			if (s.type != type.ty) {
				StaticError.TypeMismatched(s, type.ty, s.line, s.pos);
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
				StaticError.VarNotDeclared(sw.id.s, current, sw.id.s.line, sw.id.s.pos);
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
			a.s = checkDeclared(current, a.s);
			if (!a.s.isDeclared()) {
				StaticError.VarNotDeclared(a.s, current, a.s.line, a.s.pos);
			}

			// The type of array symbol
			if(current.lookup(a.s.name).array == 0) {
				StaticError.VarNotArray(a.s, a.line, a.pos);
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
				else
					conv = Type.type.FLOAT;
				
				ExpInfo result = new ExpInfo(conv);
				result.complete = true;
				return result;
			}
			else if (b.op == BinOpExp.Op.EQEQ || b.op == BinOpExp.Op.NOTEQ) {
				if (li.type != ri.type) {
					System.err.println("Two side not compatible");
					return null;
				}
				ExpInfo result = new ExpInfo(null);
				result.complete = true;
				result.isBool = true;
				return result;
			}
			else {
				ExpInfo result = new ExpInfo(null);
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
				return null;
			}
			else
				c.func = f;

			if (c.args != null) {
				ArgList al = c.args;
				ParamList pl = f.paramList;
				
				for (int i=0; i<al.length; i++) {
					ExpInfo argInfo = visit(al.get(i));
					Type paramType = pl.tlist.get(i);
					
					if (argInfo == null) {
						System.err.println("argument expression does not exist");
					}
					
					// Type checking with parameter type
					if (argInfo.type != paramType.ty) {
						System.err.println("Type mismatched parameter and argument");
					}
				}
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
			i.s = checkDeclared(current, i.s);
			if (!i.s.isDeclared()) {
				StaticError.VarNotDeclared(i.s, current, i.line, i.pos);
				return null;
			}

			if(current.lookup(i.s.name).array != 0) {
				StaticError.ArrayWithoutIndex(i.s, i.line, i.pos);
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
				System.err.println("Operation type mismatched - UnOp");
			}

			return descend;
		}
		return null;
	}

	public void visit(Assign a) {
		a.s = checkDeclared(current, a.s);
		if (!a.s.isDeclared()) {
			StaticError.VarNotDeclared(a.s, current, a.line, a.pos);
			return;
		}

		if (a.index != null) {
			ExpInfo ei = visit(a.index);
			if (ei == null || !ei.isInteger()) {
				StaticError.NotInteger(a.line, a.pos);
				return;
			}
		}
		
		if(current.lookup(a.s.name).array == 0 && a.index != null) {
			StaticError.VarNotArray(a.s, a.line, a.pos);
			return;
		}
		if(current.lookup(a.s.name).array != 0 && a.index == null) {
			StaticError.ArrayWithoutIndex(a.s, a.line, a.pos);
			return;
		}

		ExpInfo ri = visit(a.rhs);
		
		if (ri == null) {
			return;
		}
		
		if (ri.type != a.s.type) {
			if (ri.type == Type.type.INT)
				return;
			StaticError.TypeMismatched(a.s, ri.type, a.line, a.pos);
		}
	}

	private void ScopeStore() {
		prev = current;
		prevIdx = currentIdx;
		current = prev.descend.get(prevIdx);
		currentIdx = 0;
	}

	private void ScopeRecovery() {
		current = prev;
		currentIdx = prevIdx++;
	}
	
	private Symbol checkDeclared(Scope current, Symbol target) {
		Symbol s = current.lookup(target.name);
		if (s == null) {
			target.setDeclared(false);
			s = target;
		}
		else {
			target = s;
		}
		
		return s;
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