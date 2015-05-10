package Absyn;

public class Function extends Absyn {
	public Type type;
	public String id;
	public ParamList paramList;
	public CompoundStmt compoundStmt;
	
	public Function(int p, Type t, String i, ParamList pl, CompoundStmt cs) {
		pos = p;
		type = t;
		id = i;
		paramList = pl;
		compoundStmt = cs;
	}

	public Function(int p, Type t, String i, CompoundStmt cs) {
		pos = p;
		type = t;
		id = i;
		compoundStmt = cs;
	}
}