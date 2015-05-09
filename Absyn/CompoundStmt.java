package Absyn;

public class CompoundStmt extends Stmt {
	public DeclList dl;
	public StmtList sl;

	public CompoundStmt(DeclList dl, StmtList sl) {
		this.dl = dl;
		this.sl = sl;
	}

	public CompoundStmt(StmtList sl) {
		this.sl = sl;
	}
}
