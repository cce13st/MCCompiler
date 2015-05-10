package Absyn;

import java.util.ArrayList;

public class ParamList{
	public int length;
	public ArrayList<Type> tlist = new ArrayList<Type>();
	public ArrayList<Identifier> ilist = new ArrayList<Identifier>();

	public ParamList() {
		length = 0;
	}
	
	public void add(Type t, Identifier i) {
		length++;
		tlist.add(t);
		ilist.add(i);
	}
}