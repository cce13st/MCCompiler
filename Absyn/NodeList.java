package Absyn;

import java.util.ArrayList;

public class NodeList<T>{
	public int length;
	public ArrayList<T> list = new ArrayList<T>();

	public NodeList() {
		length = 0;
	}
	
	public void add(T e) {
		length++;
		list.add(e);
	}

	public T get(int i) {
		return list.get(i);
	}
}
