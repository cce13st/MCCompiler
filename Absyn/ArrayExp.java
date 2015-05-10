package Absyn;

import Symbol.Symbol;

public class ArrayExp extends Exp {
	public Symbol s;
	public Exp index;
	
	public ArrayExp(int p, String id, Exp e) {
		s = Symbol.newSymbol(id);
		index = e;
	}
}