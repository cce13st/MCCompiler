package Absyn;

import java.util.ArrayList;

public class StmtList{
	public int length;
	public ArrayList<Stmt> list = new ArrayList<Stmt>();

	public StmtList() {
		length = 0;
	}
	
	public void add(Stmt s) {
		length++;
		list.add(s);
	}
}
