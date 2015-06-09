package Absyn;

import Symbol.Symboll;

public class Assign extends Absyn {
	public Symboll s;
	public Exp index;
	public Exp rhs;
	
	public Assign(int l, int p, String i, Exp e) {
		line = l;
		pos = p;
		s = Symboll.newSymbol(i);
		index = null;
		rhs = e;
	}
	
	public Assign(int l, int p, String i, Exp e1, Exp e2) {
		line = l;
		pos = p;
		s = Symboll.newSymbol(i);
		index = e1;
		rhs = e2;
	}
	
	public void addF2I() {
		F2IExp f2i = new F2IExp(line, pos, rhs);
		rhs = f2i;
	}

	public void addI2F() {
		I2FExp i2f = new I2FExp(line, pos, rhs);
		rhs = i2f;
	}
}