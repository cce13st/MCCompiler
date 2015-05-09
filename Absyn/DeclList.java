package Absyn;

import java.util.ArrayList;

public class DeclList{
	public int length;
	public ArrayList<Decl> list = new ArrayList<Decl>();

	public DeclList() {
		length = 0;
	}
	
	public void add(Decl s) {
		length++;
		list.add(s);
	}
}
