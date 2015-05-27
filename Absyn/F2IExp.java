package Absyn;

public class F2IExp extends Exp {
	public Exp child;
	
	public F2IExp(int l, int p, Exp c) {
		line = l;
		pos = p;
		child = c;
	}
}