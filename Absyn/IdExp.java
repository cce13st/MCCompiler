package Absyn;

import Symbol.Symbol;

public class IdExp extends Exp {
	public Symbol s;

	public IdExp(int p, String symbol) {
		pos = p;
		this.s = Symbol.newSymbol(symbol);
		System.out.println("Id expr : " + this.s);
	}
}
