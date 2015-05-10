package Absyn;

public class Case {
	public int casenum;
	public StmtList slist;
	public boolean br;
	
	public Case(int n, StmtList sl, boolean b) {
		casenum = n;
		slist = sl;
		br = b;
	}
}