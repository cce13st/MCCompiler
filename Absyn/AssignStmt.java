package Absyn;

public class AssignStmt extends Stmt {
	public Assign assign;
	
	public AssignStmt(int p, Assign a) {
		assign = a;
	}
}