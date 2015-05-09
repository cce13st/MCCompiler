package Absyn;

import Symbol.Symbol;

public class Assign extends Absyn {
	public Symbol s;
	public Exp index;
	public Exp rhs;
	
	public Assign(int p, String i, Exp e) {
		s = Symbol.newSymbol(i);
		index = null;
		rhs = e;
	}
	
	public Assign(int p, String i, Exp e1, Exp e2) {
		s = Symbol.newSymbol(i);
		index = e1;
		rhs = e2;
		
		System.out.println("AssignStmt with index : " + s);
	}
}