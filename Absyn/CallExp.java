package Absyn;

import Symbol.Symbol;

public class CallExp extends Exp {
	public String funcName;
	public Function func;
	public ArgList args;

	public CallExp(int p, String i) {
		pos = p;
		funcName = i;
		args = null;
	}

	public CallExp(int p, String i, ArgList a) {
		pos = p;
		funcName = i;
		args = a;
	}
}
