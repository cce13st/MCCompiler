package Absyn;

import Symbol.Symbol;

public class ArrayExp extends Exp {
	public Symbol s;
	public Exp index;
	
	public ArrayExp(int l, int p, String id, Exp e) {
		line = l;
		pos = p;
		s = Symbol.newSymbol(id);
		index = e;
	}
}