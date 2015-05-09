package Absyn;

public class ForStmt extends Stmt {
	public Exp var;
	public Exp hi;
	public Stmt body;

	public ForStmt(int p, Exp v, Exp h, Stmt b) {
		pos = p;
		var = v;
		hi = h;
		body = b;
	}
}
