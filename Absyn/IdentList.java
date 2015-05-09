package Absyn;

import java.util.ArrayList;

public class IdentList{
	public int length;
	public ArrayList<Identifier> list = new ArrayList<Identifier>();

	public IdentList() {
		length = 0;
	}
	
	public void add(Identifier i) {
		length++;
		list.add(i);
	}
}
