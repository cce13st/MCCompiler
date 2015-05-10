package Absyn;

public class CompoundStmt extends Stmt {
	public DeclList dlist;
	public StmtList slist;

	public CompoundStmt(DeclList dl, StmtList sl) {
		dlist = dl;
		slist = sl;
	}

	public CompoundStmt(StmtList sl) {
		slist = sl;
	}
}
