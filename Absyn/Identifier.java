package Absyn;

import Symbol.Symbol;

public class Identifier extends Absyn {
	public Symbol s;
	public Integer index = null;
	
	public Identifier(int l, int p, String i) {
		line = l;
		pos = p;
		s = Symbol.newSymbol(i);
	}

	public Identifier(int l, int p, String i, Integer intnum) {
		line = l;
		pos = p;
		s = Symbol.newSymbol(i);
		index = intnum;
	}
}