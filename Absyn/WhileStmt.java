package Absyn;

public class WhileStmt extends Stmt {
	public Exp cond;
	public Stmt body;
	public boolean doWhile;

	public WhileStmt(int p, Exp t, Stmt b, boolean doWhile) {
		pos = p;
		cond = t;
		body = b;
		this.doWhile = doWhile;
	}
}
