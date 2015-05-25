package Absyn;

public class IntExp extends Exp {
	public int value;

	public IntExp(int l, int p, int v) {
		line = l;
		pos = p;
		value = v;
	}
}
