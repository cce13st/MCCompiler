package Absyn;

public class BinOpExp extends Exp {
	public Exp left, right;
	public Op op;

	public enum Op {
		PLUS, MINUS, MULT, DIV, LT, GT, NOTEQ, LTEQ, GTEQ, EQEQ
	};

	public BinOpExp(int line, int p, Exp l, Op o, Exp r) {
		this.line = line;
		pos = p;
		left = l;
		op = o;
		right = r;
	}
	
	public String getOp() {
		if (op == Op.PLUS)
			return "+";
		else if (op == Op.MINUS)
			return "-";
		else if (op == Op.MULT)
			return "*";
		else if (op == Op.DIV)
			return "/";
		else if (op == Op.LT)
			return "<";
		else if (op == Op.GT)
			return ">";
		else if (op == Op.NOTEQ)
			return "!=";
		else if (op == Op.LTEQ)
			return "<=";
		else if (op == Op.GTEQ)
			return ">=";
		else if (op == Op.EQEQ)
			return "==";
		return null;
	}
}
