package Absyn;

import Symbol.Symboll;

public class SwitchStmt extends Stmt {
	public Symboll id;
	public CaseList clist;
	
	public SwitchStmt(Symboll i, CaseList cl) {
		id = i;
		clist = cl;
	}
}