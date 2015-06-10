package Absyn;

abstract public class Exp extends Absyn {
	public boolean paren = false;
    public int reg;
    
    public Type.type type;
}
