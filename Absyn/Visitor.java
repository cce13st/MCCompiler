package Absyn;

public class Visitor {
	
	public Program root;
	
	public Visitor(Program p) {
		root = p;
	}
	
	public void startVisit() {
		visit(root);
	}
	
	public void visit(Program p) {
		DeclList dl = p.dlist;
		FuncList fl = p.flist;
		visit(dl);
		visit(fl);
	}
	
	public void visit(ArgList al) {
		for (int i=0; i<al.length; i++) {
			if (i > 0)
				System.out.print(", ");
			visit((Exp) al.list.get(i));
		}
	}
	
	public void visit(Assign a) {
		System.out.print(a.s);
		if (a.index != null) {
			System.out.print("[");
			visit(a.index);
			System.out.print("]");
		}
		System.out.print(" = ");
		visit(a.rhs);
	}
	
	public void visit(Exp e) {
		if (e instanceof ArrayExp) {
			ArrayExp a = (ArrayExp) e;
			System.out.print(a.s);
			Exp index = ((ArrayExp) e).index;
			visit(index);
		}
		else if (e instanceof BinOpExp) {
			BinOpExp b = (BinOpExp) e;
			System.out.print("(");
			visit(b.left);
			System.out.print(" " + b.getOp() + " ");
			visit(b.right);
			System.out.print(")");
		}
		else if (e instanceof CallExp) {
			CallExp c = (CallExp) e;
			System.out.print(c.func.toString() + "(");
			if(c.args != null) visit(c.args);
			System.out.print(")");
		}
		else if (e instanceof FloatExp) {
			FloatExp f = (FloatExp) e;
			System.out.print(f.value);
		}
		else if (e instanceof IdExp) {
			IdExp i = (IdExp) e;
			System.out.print(i.s);
		}
		else if (e instanceof IntExp) {
			IntExp i = (IntExp) e;
			System.out.print(i.value);
		}
		else if (e instanceof UnOpExp) {
			UnOpExp u = (UnOpExp) e;
			System.out.print("-");
			visit(u.exp);
		}
	}
	
	public void visit(Stmt s) {
		if (s instanceof AssignStmt) {
			AssignStmt a = (AssignStmt) s;
			visit(a.assign);
			System.out.println(";");
		}
		else if (s instanceof CallStmt) {
			CallStmt c = (CallStmt) s;
			visit(c.exp);
			System.out.println(";");
		}
		else if (s instanceof CompoundStmt) {
			CompoundStmt c = (CompoundStmt) s;
			visit(c.slist);
		}
		else if (s instanceof EmptyStmt) {
			System.out.println(";");
		}
		else if (s instanceof ForStmt) {
			ForStmt f = (ForStmt) s;
			System.out.print("for (");
			visit(f.init);
			System.out.print("; ");
			visit(f.cond);
			System.out.print("; ");
			visit(f.post);
			System.out.println(") {");
			visit(f.body);
			System.out.println("}");
		}
		else if (s instanceof IfStmt) {
			IfStmt i = (IfStmt) s;
			System.out.print("if (");
			visit(i.cond);
			System.out.println(") {");
			visit(i.thenClause);
			System.out.println("}");
			if (i.elseClause != null) {
				System.out.println("else {");
				visit (i.elseClause);
				System.out.println("}");
			}
		}
		else if (s instanceof RetStmt) {
			RetStmt r = (RetStmt) s;
			System.out.print("return");
			if (r.exp != null) {
				System.out.print(" "); visit(r.exp);
			}
			System.out.println(";");
		}
		else if (s instanceof WhileStmt) {
			WhileStmt w = (WhileStmt) s;
			if (w.doWhile) {
				System.out.println("do {");
				visit(w.body);
				System.out.print("} while (");
				visit(w.cond);
				System.out.println(")");
			}
			else {
				System.out.print("while (");
				visit(w.cond);
				System.out.println(") {");
				visit(w.body);
				System.out.println("}");
			}
		}
		else if (s instanceof SwitchStmt) {
			SwitchStmt sw = (SwitchStmt) s;
			System.out.print("switch (");
			visit(sw.id);
			System.out.println(") {");
			visit(sw.clist);
			System.out.println("}");
		}
	}
	
	public void visit(CaseList cl) {
		for (int i=0; i<cl.length; i++) {
			Case c = cl.clist.get(i);
			if (c instanceof DefaultCase) {
				System.out.println("default:");
				visit(c.slist);
			}
			else {
				System.out.println("case " + c.casenum + ":");
				visit(c.slist);
			}
			if (c.br)
				System.out.println("break;");
		}
	}
	
	/* StmtList */
	public void visit(StmtList sl) {
		for(int i=0; i<sl.length; i++) {
			visit((Stmt) sl.list.get(i));
		}
	}
	
	/* Declaration */
	public void visit(Decl d) {
		visit(d.type);
		visit(d.ilist);
	}
	
	public void visit(DeclList dl) {
		for(int i=0; i<dl.length; i++) {
			visit((Decl) dl.list.get(i));
			System.out.println(";");
		}
	}
	
	public void visit(Type t) {
		System.out.print(t.ty.toString() + " ");
	}
	
	public void visit(IdentList il) {
		for(int i=0; i<il.length; i++) {
			if (i > 0)
				System.out.print(", ");
			visit(il.list.get(i));
		}
	}
	
	public void visit(Identifier i) {
		System.out.print(i.s);
		if (i.index != null)
			System.out.print("[" + i.index + "]");
		else
			System.out.print("");
	}
	
	public void visit(Function f) {
		visit(f.type);
		System.out.print(f.id + "(");
		if (f.paramList != null) visit(f.paramList);
		System.out.println(")\n{");
		visit(f.compoundStmt);
		System.out.println("}\n");
	}
	
	public void visit(ParamList pl) {
		for(int i=0; i<pl.length; i++) {
			if (i > 0)
				System.out.print(", ");
			visit(pl.tlist.get(i));
			visit(pl.ilist.get(i));
		}
	}
	
	public void visit(FuncList fl) {
		for(int i=0; i<fl.length; i++) {
			visit(fl.list.get(i));
		}
	}
}