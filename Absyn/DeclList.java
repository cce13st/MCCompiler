package Absyn;

import java.util.ArrayList;

public class DeclList{
	public int length;
	public ArrayList<Stmt> list = new ArrayList<Stmt>();

	public DeclList() {
		length = 0;
	}
	
	public void add(Stmt s) {
		length++;
		list.add(s);
	}
}
