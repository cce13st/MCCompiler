package Absyn;

public class UnOpExp extends Exp {
	public Exp exp;
	public Op oper;

	public UnOpExp(int l, int p, Op o, Exp e) {
		line = l;
		pos = p;
		oper = o;
		exp = e;
	}

	public enum Op {
		MINUS
	};
}
