package Absyn;

import java.util.ArrayList;

import Symbol.Symboll;

public class ParamList{
	public int length;
	public ArrayList<Type> tlist = new ArrayList<Type>();
	public ArrayList<Symboll> slist = new ArrayList<Symboll>();

	public ParamList() {
		length = 0;
	}
	
	public void add(Type t, Symboll s) {
		length++;
		tlist.add(t);
		slist.add(s);
	}
	
	public Type getType(int i) {
		return tlist.get(i);
	}
	
	public Symboll getSymbol(int i) {
		return slist.get(i);
	}
	
	public void replaceSymbol(int i, Symboll s) {
		slist.set(i, s);
	}
}
