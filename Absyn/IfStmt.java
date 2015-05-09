package Absyn;

public class IfStmt extends Stmt {
	public Exp cond;
	public Stmt thenClause;
	public Stmt elseClause; /* optional */

	public IfStmt(int p, Exp x, Stmt y) {
		pos = p;
		cond = x;
		thenClause = y;
		elseClause = null;

		System.err.println("IfStmt no else");
	}

	public IfStmt(int p, Exp x, Stmt y, Stmt z) {
		pos = p;
		cond = x;
		thenClause = y;
		elseClause = z;

		System.err.println("IfStmt with else");
	}
}
