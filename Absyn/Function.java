package Absyn;

public class Function extends Absyn {
	public Type type;
	public String id;
	public ParamList paramList;
	public CompoundStmt compoundStmt;
	
	public boolean duplicate = false;
	
	public Function(int l, int p, Type t, String i, ParamList pl, CompoundStmt cs) {
		line = l;
		pos = p;
		type = t;
		id = i;
		paramList = pl;
		compoundStmt = cs;
	}

	public Function(int l, int p, Type t, String i, CompoundStmt cs) {
		line = l;
		pos = p;
		type = t;
		id = i;
		compoundStmt = cs;
	}
}