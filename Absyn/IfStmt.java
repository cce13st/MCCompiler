package Absyn;

public class IfStmt extends Stmt {
	public Exp cond;
	public Stmt thenClause;
	public Stmt elseClause; /* optional */

	public IfStmt(int p, Exp x, Stmt y) {
		this.pos = p;
		cond = x;
		thenClause = y;
		elseClause = null;
	}

	public IfStmt(int p, Exp x, Stmt y, Stmt z) {
		this.pos = p;
		cond = x;
		thenClause = y;
		elseClause = z;
	}
}
