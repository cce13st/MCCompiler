package Absyn;

public class Visitor {
	
	public Program root;
	
	public Visitor(Program p) {
		root = p;
	}
	
	public void printAST() {
		visit(root);
	}
	
	public void visit(Program p) {
		DeclList dl = p.dlist;
		FuncList fl = p.flist;
		if (dl != null) {
			visit(dl);
			System.out.println("");
		}
		
		if (fl != null) visit(fl);
	}
	
	public void visit(ArgList al) {
		for (int i=0; i<al.length; i++) {
			if (i > 0)
				System.out.print(", ");
			visit((Exp) al.get(i));
		}
	}
	
	public void visit(Assign a) {
		System.out.print(a.s + "<" + a.s.getHidden() + ">" + "{" + a.s.location + "}");
		if (a.index != null) {
			System.out.print("[");
			visit(a.index);
			System.out.print("]");
		}
		System.out.print(" = ");
		visit(a.rhs);
	}
	
	public void visit(Exp e) {
		if (e.paren)
			System.out.print("(");
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
			visit(b.left);
			System.out.print(" " + b.getOp() + " ");
			visit(b.right);
		}
		else if (e instanceof CallExp) {
			CallExp c = (CallExp) e;
			System.out.print(c.funcName+ "(");
			if(c.args != null) visit(c.args);
			System.out.print(")");
		}
		else if (e instanceof FloatExp) {
			FloatExp f = (FloatExp) e;
			System.out.print(f.value);
		}
		else if (e instanceof IdExp) {
			IdExp i = (IdExp) e;
			System.out.print(i.s + "<" + i.s.getHidden() + ">{" + i.s.location + "}");
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
		if (e.paren)
			System.out.print(")");
	}
	
	public void visit(Stmt s, boolean block) {
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
			if (block)
				System.out.println("{");
			if (c.dlist != null) visit(c.dlist);
			if (c.slist != null) visit(c.slist);
			if (block)
				System.out.println("}");
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
			visit(f.body, false);
			System.out.println("}");
		}
		else if (s instanceof IfStmt) {
			IfStmt i = (IfStmt) s;
			System.out.print("if (");
			visit(i.cond);
			System.out.println(") {");
			visit(i.thenClause, false);
			System.out.println("}");
			if (i.elseClause != null) {
				System.out.println("else {");
				visit (i.elseClause, false);
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
				visit(w.body, false);
				System.out.print("} while (");
				visit(w.cond);
				System.out.println(");");
			}
			else {
				System.out.print("while (");
				visit(w.cond);
				System.out.println(") {");
				visit(w.body, false);
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
			Case c = cl.get(i);
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
			visit((Stmt) sl.get(i), true);
		}
	}
	
	/* Declaration */
	public void visit(Decl d) {
		visit(d.type);
		visit(d.ilist);
	}
	
	public void visit(DeclList dl) {
		for(int i=0; i<dl.length; i++) {
			visit((Decl) dl.get(i));
			System.out.println(";");
		}
	}
	
	public void visit(Type t) {
		System.out.print(t.ty.toString().toLowerCase() + " ");
	}
	
	public void visit(IdentList il) {
		for(int i=0; i<il.length; i++) {
			if (i > 0)
				System.out.print(", ");
			visit(il.get(i));
		}
	}
	
	public void visit(Identifier i) {
        System.out.print(i.s + "<" + i.s.getHidden() + ">" + "{" + i.s.location + "}");
		if (i.index != null)
			System.out.print("[" + i.index + "]");
		else
			System.out.print("");
	}
	
	public void visit(Function f) {
		visit(f.type);
		System.out.print(f.id + "(");
		if (f.paramList != null) visit(f.paramList);
		System.out.println(")");
		visit(f.compoundStmt, true);
		System.out.println("\n");
	}
	
	public void visit(ParamList pl) {
		for(int i=0; i<pl.length; i++) {
			if (i > 0)
				System.out.print(", ");
			visit(pl.getType(i));
			visit(pl.getIdentifier(i));
		}
	}
	
	public void visit(FuncList fl) {
		for(int i=0; i<fl.length; i++) {
			visit(fl.get(i));
		}
	}
}
