package Absyn;

public class CaseList extends NodeList<Case>{
	public DefaultCase defaultCase;
	
	public CaseList() {
		super();
	}
	
	public void setDefault(DefaultCase d) {
		super.add(d);
		defaultCase = d;
	}
}
