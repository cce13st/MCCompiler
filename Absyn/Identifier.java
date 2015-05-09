package Absyn;

import Symbol.Symbol;

public class Identifier extends Absyn {
	public Symbol s;
	public Integer index = null;
	
	public Identifier(int p, String i) {
		pos = p;
		s = Symbol.newSymbol(i);
	}

	public Identifier(int p, String i, Integer intnum) {
		pos = p;
		s = Symbol.newSymbol(i);
		index = intnum;
	}
}