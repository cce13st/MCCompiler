package Absyn;

import Symbol.Symbol;

public class Function extends Absyn {
	public Type type;
	public Symbol id;
	public ParamList paramList;
	public CompoundStmt compoundStmt;
	
	public Function(int p, Type t, String i, ParamList pl, CompoundStmt cs) {
		pos = p;
		type = t;
		id = Symbol.newSymbol(i);
		paramList = pl;
		compoundStmt = cs;
	}

	public Function(int p, Type t, String i, CompoundStmt cs) {
		pos = p;
		type = t;
		id = Symbol.newSymbol(i);
		compoundStmt = cs;
	}
}