package Absyn;

public class FloatExp extends Exp {
	public float value;

	public FloatExp(int p, float v) {
		pos = p;
		value = v;
		System.out.println("INTINTEXP");
	}
}
