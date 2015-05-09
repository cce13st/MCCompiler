package Absyn;

public class Decl extends Absyn {
	public Type type;
	public IdentList ilist;
	
	public Decl(int p, Type t, IdentList il) {
		pos = p;
		type = t;
		ilist = il;
	}
}