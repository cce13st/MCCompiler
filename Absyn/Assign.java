package Absyn;

import Symbol.Symbol;

public class Assign extends Absyn {
	public Symbol s;
	public Exp index;
	public Exp rhs;
	
	public boolean i2f = false;
	public boolean f2i = false;
	
	public Assign(int l, int p, String i, Exp e) {
		line = l;
		pos = p;
		s = Symbol.newSymbol(i);
		index = null;
		rhs = e;
	}
	
	public Assign(int l, int p, String i, Exp e1, Exp e2) {
		line = l;
		pos = p;
		s = Symbol.newSymbol(i);
		index = e1;
		rhs = e2;
	}
}