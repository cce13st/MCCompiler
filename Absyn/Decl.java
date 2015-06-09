package Absyn;

public class Decl extends Absyn {
	public Type type;
    public SymbolList slist;
	
	public Decl(int p, Type t, SymbolList sl) {
		pos = p;
		type = t;
        slist = sl;
	}
}
