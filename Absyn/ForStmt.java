package Absyn;

public class ForStmt extends Stmt {
	public Assign init;
	public Exp cond;
	public Assign post;
	public Stmt body;

	public ForStmt(int p, Assign i, Exp c, Assign post, Stmt b) {
		pos = p;
		init = i;
		cond = c;
		this.post = post;
		body = b;
	}
}
