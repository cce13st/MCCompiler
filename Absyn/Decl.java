package Absyn;

import java.util.ArrayList;

import Symbol.Symboll;

public class Decl extends Absyn {
	public Type type;
    public ArrayList<Symboll> slist;
	
	public Decl(int p, Type t, ArrayList<Symboll> sl) {
		pos = p;
		type = t;
        slist = sl;
	}
}
