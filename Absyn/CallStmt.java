package Absyn;

public class CallStmt extends Stmt {
	public Exp exp;
	
	public CallStmt(int p, Exp e) {
		pos = p;
		exp = e;
	}
}
