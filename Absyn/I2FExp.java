package Absyn;

public class I2FExp extends Exp {
	public Exp child;
	
	public I2FExp(int l, int p, Exp c) {
		line = l;
		pos = p;
		child = c;
	}
}