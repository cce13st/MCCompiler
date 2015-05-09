package Absyn;

import Symbol.Symbol;

public class AssignStmt extends Stmt {
	public Symbol s;
	public Exp index;
	public Exp rhs;
	
	public AssignStmt(int p, String i, Exp e) {
		s = Symbol.newSymbol(i);
		index = null;
		rhs = e;
	}
	
	public AssignStmt(int p, String i, Exp e1, Exp e2) {
		s = Symbol.newSymbol(i);
		index = e1;
		rhs = e2;
		
		System.out.println("AssignStmt with index : " + s);

	}
}