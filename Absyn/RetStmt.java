package Absyn;

public class RetStmt extends Stmt {
	public Exp exp;
	
	public RetStmt(int p) {
		pos = p;
		exp = null;
	}

	public RetStmt(int p, Exp e) {
		pos = p;
		exp = e;
	}
}
