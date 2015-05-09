package Absyn;

public class OpExp extends Exp {
	public Exp left, right;
	public Op oper;

	public OpExp(int p, Exp l, Op o, Exp r) {
		pos = p;
		left = l;
		oper = o;
		right = r;
		System.out.println("P : " + p + ", OP : " + o);
	}

	public enum Op {
		PLUS, MINUS, MULT, DIV, NE, LT, LE, GT, GE, NOTEQ, LTEQ, GTEQ, EQEQ
	};
}
