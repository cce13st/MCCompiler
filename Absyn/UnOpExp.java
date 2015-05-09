package Absyn;

public class UnOpExp extends Exp {
	public Exp exp;
	public Op oper;

	public UnOpExp(int p, Op o, Exp e) {
		pos = p;
		oper = o;
		exp = e;
		System.out.println("P : " + p + ", OP : " + o);
	}

	public enum Op {
		MINUS
	};
}
