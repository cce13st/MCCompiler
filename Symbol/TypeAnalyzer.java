package Symbol;

import Absyn.*;

class TypeAnalyzer {
	public Scope global;
	public Program root;

	private Scope current;
	private int currentIdx;

	private Scope prev;
	private int prevIdx;

	public TypeAnalyzer(Scope global, Program p) {
		this.global = global;
		root = p;
	}

	public void startAnalysis() {
		current = global;
		currentIdx = 0;

		System.out.println("Type analysis start");
		System.out.println("================================");
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
				StaticError.DuplicatedDeclaration(s, current);
			}
			if (s.type != type.ty) {
				StaticError.TypeMismatched(s, type.ty);
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
				System.err.println("Cannot check condition type");
			}
			
			visit(f.post);
			visit(f.body, false);

			ScopeRecovery();
		}
		else if (s instanceof IfStmt) {
			IfStmt i = (IfStmt) s;
			ExpInfo condInfo = visit(i.cond);
			if (condInfo == null || !condInfo.isBoolean()) {
				System.err.println("Cannot check condition type");
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
				System.err.println("Cannot check condition type");
			}

			ScopeStore();

			visit(w.body, false);

			ScopeRecovery();
		}
		else if (s instanceof SwitchStmt) {
			SwitchStmt sw = (SwitchStmt) s;
			if (!sw.id.s.isDeclared()) {
				System.err
						.println("variable " + sw.id.s + " does not declared");
			}
			visit(sw.clist);
		}
		else if (s instanceof CallStmt) {
			CallStmt c = (CallStmt) s;
			visit(c.exp);
		}
		else if (s instanceof RetStmt) {
			RetStmt r = (RetStmt) s;
			if (r.exp != null)
				visit(r.exp);
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
				StaticError.VarNotDeclared(a.s, current);
			}

			// The type of array symbol

			Exp index = ((ArrayExp) e).index;
			ExpInfo ei = visit(index);
			
			if (ei == null || !ei.isInteger()) {
				System.err.println("this is not integer index");
				return null;
			}

			return new ExpInfo(a.s.type);
		} 
		else if (e instanceof BinOpExp) {
			// 양쪽 모두 number인지 확인

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
			else {
				ExpInfo result = new ExpInfo(null);
				result.complete = true;
				result.isBool = true;
				return result;
			}
		} 
		else if (e instanceof CallExp) {
			// Call하는 함수가 정의되었는지
			// Call하는 함수의 type 확인
			// parameter-argument type compatibility

			CallExp c = (CallExp) e;

			if (!c.func.isDeclared()) {
				StaticError.VarNotDeclared(c.func, current);
				return null;
			}

			if (c.args != null) {
				ArgList al = c.args;
				
				for (int i=0; i<al.length; i++) {
					ExpInfo ei = visit(al.get(i));
					
					if (ei == null) {
						return null;
					}
					
					// Type checking with parameter type
					if (ei.type != null) {
						
					}
				}
			}
			return null;
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
				StaticError.VarNotDeclared(i.s, current);
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
		if (!a.s.isDeclared()) {
			StaticError.VarNotDeclared(a.s, current);
		}

		if (a.index != null) {
			ExpInfo ei = visit(a.index);
			if (ei == null || !ei.isInteger()) {
				System.err.println("this is not integer index");
			}
		}

		ExpInfo ri = visit(a.rhs);
		
		if (ri == null) {
			return;
		}
		
		if (ri.type != a.s.type) {
			
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