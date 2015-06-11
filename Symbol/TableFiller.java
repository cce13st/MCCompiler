package Symbol;

import Absyn.*;

import java.util.ArrayList;

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
		ArrayList<Symboll> sl = d.slist;

		for (int i = 0; i < sl.size(); i++) {
			Symboll id = sl.get(i);
			boolean isglobal = false;
			if (current.loc == "GLOBAL")
				isglobal = true;
			
			id = new Symboll(type.ty, id.name, id.array, true, i, id.line, id.pos, isglobal);
			sl.set(i, id);
			
			current.addBind(id.name, id);
		}
	}

	public void visit(FuncList fl) {
		for (int i = 0; i < fl.length; i++) {
			visit(fl.get(i));
		}
		
		Function printf = new Function(0, 0, null, "printf", null, null);
		Function scanf = new Function(0, 0, null, "scanf", null, null);
		
		table.addFunction(printf);
		table.addFunction(scanf);
	}

	public void visit(Function f) {
        Symboll.clearLoc();
		if (table.funcMap.get(f.id) == null)
			table.addFunction(f);
		else {
			f.duplicate = true;
		}
		
		Scope prev = current;
		current = new Scope(prev, f.id, true);
		
		if (f.paramList != null)
			visit(f.paramList);
		visit(f.compoundStmt, false);
		current = prev;
	}

	public void visit(ParamList pl) {
		for (int i = 0; i < pl.length; i++) {
			Symboll id = pl.getSymbol(i);
			Type type = pl.getType(i);
			
			id = new Symboll(type.ty, id.name, id.array, false, i, id.line, id.pos, false);
			pl.replaceSymbol(i, id);

			current.addBind(id.name, id);
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
	
	public void visit(ArgList al) {
		for (int i=0; i<al.length; i++) {
			visit(al.get(i));
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
			sw.id = checkDeclared(current, sw.id);
			visit(sw.clist);
		}
		else if (s instanceof CallStmt) {
			CallStmt c = (CallStmt) s;
			visit(c.exp);
		}
		else if (s instanceof RetStmt) {
			RetStmt r = (RetStmt) s;
			r.func_id = current.getFuncScope().loc;
			if (r.exp != null)
				visit(r.exp);
		}
	}
	
	public void visit(CaseList cl) {
		for (int i=0; i<cl.length; i++) {
			Case c = cl.get(i);
			visit(c.slist);
		}
	}
	
	public void visit(Exp e) {
		if (e instanceof ArrayExp) {
			ArrayExp a = (ArrayExp) e;
            a.s = checkDeclared(current, a.s);
			visit(a.index);
		}
		else if (e instanceof BinOpExp) {
			BinOpExp b = (BinOpExp) e;
			visit(b.left);
			visit(b.right);
		}
		else if (e instanceof CallExp) {
			CallExp c = (CallExp) e;
			
			if(c.args != null)
				visit(c.args);
		}
		else if (e instanceof UnOpExp) {
			UnOpExp u = (UnOpExp) e;
			visit(u.exp);
		}
        else if (e instanceof IdExp) {
            IdExp i = (IdExp) e;
            i.s = checkDeclared(current, i.s);
        }
	}
	
	public void visit(Assign a) {
        a.s = checkDeclared(current, a.s);

		if (a.index != null)
			visit(a.index);
		
		visit(a.rhs);
	}
	
	private Symboll checkDeclared(Scope current, Symboll target) {
		Symboll s = current.lookup(target.name);
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
