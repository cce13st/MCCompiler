package Absyn;

import java.util.ArrayList;

public class ArgList{
	public int length;
	public ArrayList<Exp> list = new ArrayList<Exp>();

	public ArgList() {
		length = 0;
	}
	
	public void add(Exp e) {
		length++;
		list.add(e);
	}
}
