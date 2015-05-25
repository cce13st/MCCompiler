package Absyn;

import Symbol.Symbol;

public class IdExp extends Exp {
	public Symbol s;

	public IdExp(int l, int p, String symbol) {
		line = l;
		pos = p;
		this.s = Symbol.newSymbol(symbol);
	}
}
