package Absyn;

import Symbol.Symboll;

public class CallExp extends Exp {
	public String funcName;
	public Function func;
	public ArgList args;

	public CallExp(int l, int p, String i) {
		line = l;
		pos = p;
		funcName = i;
		args = null;
	}

	public CallExp(int l, int p, String i, ArgList a) {
		line = l;
		pos = p;
		funcName = i;
		args = a;
	}
}
