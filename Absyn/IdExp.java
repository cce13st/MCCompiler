package Absyn;

import Symbol.Symboll;

public class IdExp extends Exp {
	public Symboll s;

	public IdExp(int l, int p, String symbol) {
		line = l;
		pos = p;
		this.s = Symboll.newSymbol(symbol);
	}
}
