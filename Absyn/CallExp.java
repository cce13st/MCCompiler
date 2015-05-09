package Absyn;

import Symbol.Symbol;

public class CallExp extends Exp {
	public Symbol func;
	public ArgList args;

	public CallExp(int p, String i) {
		pos = p;
		func = Symbol.newSymbol(i);
		args = null;
	}

	public CallExp(int p, String i, ArgList a) {
		pos = p;
		func = Symbol.newSymbol(i);
		args = a;
	}
}
