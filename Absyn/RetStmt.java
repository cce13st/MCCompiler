package Absyn;

public class RetStmt extends Stmt {
	public Exp exp;
	public String func_id;
	
	public RetStmt(int l, int p) {
		line = l;
		pos = p;
		exp = null;
	}

	public RetStmt(int l, int p, Exp e) {
		line = l;
		pos = p;
		exp = e;
	}
}
