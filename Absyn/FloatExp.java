package Absyn;

public class FloatExp extends Exp {
	public float value;

	public FloatExp(int l, int p, float v) {
		line = l;
		pos = p;
		value = v;
	}
}
