package Absyn;

import java.util.ArrayList;

public class FuncList{
	public int length;
	public ArrayList<Function> list = new ArrayList<Function>();

	public FuncList() {
		length = 0;
	}
	
	public void add(Function f) {
		length++;
		list.add(f);
	}
}
