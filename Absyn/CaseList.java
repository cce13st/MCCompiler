package Absyn;

import java.util.ArrayList;

public class CaseList{
	public int length;
	public ArrayList<Case> clist = new ArrayList<Case>();
	public DefaultCase defaultCase;

	public CaseList() {
		length = 0;
	}
	
	public void add(Case c) {
		length++;
		clist.add(c);
	}
	
	public void setDefault(DefaultCase d) {
		length++;
		defaultCase = d;
		clist.add(d);
	}
}
