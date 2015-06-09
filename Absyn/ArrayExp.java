package Absyn;

import Symbol.Symboll;

public class ArrayExp extends Exp {
	public Symboll s;
	public Exp index;
	
	public ArrayExp(int l, int p, String id, Exp e) {
		line = l;
		pos = p;
		s = Symboll.newSymbol(id);
		index = e;
	}
}
