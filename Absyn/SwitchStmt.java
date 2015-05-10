package Absyn;

public class SwitchStmt extends Stmt {
	public Identifier id;
	public CaseList clist;
	
	public SwitchStmt(Identifier i, CaseList cl) {
		id = i;
		clist = cl;
	}
}