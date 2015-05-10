package Symbol;

import Absyn.*;

public class TableFiller {
	public Table table;

	public Scope current;

	public TableFiller(Table table) {
		this.table = table;
		current = table.global;
	}

	public void fillTable(Program p) {
		visit(p);
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
		for (int i = 0; i < dl.length; i++) {
			visit(dl.get(i));
		}
	}

	public void visit(Decl d) {
		Type type = d.type;
		IdentList il = d.ilist;

		for (int i = 0; i < il.length; i++) {
			Identifier id = il.get(i);
			Symbol s;
			if (id.index == null)
				s = new Symbol(type.ty, id.s.name, 0, true);
			else
				s = new Symbol(type.ty, id.s.name, id.index, true);
			current.addBind(id.s.name, s);
		}
	}

	public void visit(FuncList fl) {
		for (int i = 0; i < fl.length; i++) {
			visit(fl.get(i));
		}
	}

	public void visit(Function f) {
		Scope prev = current;
		current = new Scope(prev, f.id, true);
		if (f.paramList != null) visit(f.paramList);
		visit(f.compoundStmt, false);
		current = prev;
	}

	public void visit(ParamList pl) {
		for (int i = 0; i < pl.length; i++) {
			Identifier id = pl.getIdentifier(i);
			Type type = pl.getType(i);

			Symbol s;
			if (id.index == null)
				s = new Symbol(type.ty, id.s.name, 0, false);
			else
				s = new Symbol(type.ty, id.s.name, id.index, false);
			current.addBind(id.s.name, s);
		}
	}

	public void visit(CompoundStmt cs, boolean block) {
		Scope prev = current;
		if (block)
			current = prev.newCompoundScope();

		if (cs.dlist != null) visit(cs.dlist);
		if (cs.slist != null) visit(cs.slist);
		
		if (block)
			current = prev;
	}
	
	public void visit(StmtList sl) {
		for (int i=0; i<sl.length; i++) {
			visit(sl.get(i), true);
		}
	}
	
	public void visit(Stmt s, boolean block) {

		if (s instanceof AssignStmt) {
			AssignStmt a = (AssignStmt) s;
			visit(a.assign);
		}
		else if (s instanceof CallStmt) {
			CallStmt c = (CallStmt) s;
			visit(c.exp);
		}
		else if (s instanceof CompoundStmt) {
			CompoundStmt c = (CompoundStmt) s;
			visit(c, block);
		}
		else if (s instanceof ForStmt) {
			Scope prev = current;
			current = prev.newForScope();
			ForStmt f = (ForStmt) s;
			
			visit(f.init);
			visit(f.cond);
			visit(f.post);
			visit(f.body, false);
			
			current = prev;
		}
		else if (s instanceof IfStmt) {
			IfStmt i = (IfStmt) s;
			visit(i.cond);
			Scope prev = current;
			current = prev.newIfScope(true);
			visit(i.thenClause, false);
			current = prev;
			if (i.elseClause != null) {
				prev = current;
				current = prev.newIfScope(false);
				visit (i.elseClause, false);
				current = prev;
			}
		}
		else if (s instanceof RetStmt) {
			RetStmt r = (RetStmt) s;
			if (r.exp != null)
				visit(r.exp);
		}
		else if (s instanceof WhileStmt) {
			WhileStmt w = (WhileStmt) s;
			visit(w.cond);
			Scope prev = current;
			current = prev.newWhileScope();
			visit(w.body, false);
			current = prev;
		}
		else if (s instanceof SwitchStmt) {
			SwitchStmt sw = (SwitchStmt) s;
			
			visit(sw.clist);
		}
	}
	
	public void visit(CaseList cl) {
		for (int i=0; i<cl.length; i++) {
			Case c = cl.get(i);
			visit(c.slist);
		}
	}
	
	public void visit(Exp e) {
	}
	
	public void visit(Assign a) {
		if (a.index != null) {
			visit(a.index);
		}
		visit(a.rhs);
	}
}