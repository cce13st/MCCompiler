package Absyn;

public class WhileStmt extends Stmt {
	public Exp cond;
	public Stmt body;

	public WhileStmt(int p, Exp t, Stmt b) {
		pos = p;
		cond = t;
		body = b;
	}
}
