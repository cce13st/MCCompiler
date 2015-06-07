package Symbol;

import Absyn.Type;

public class Symbol {
	private static int hiddenCnt = 0;
	
	public boolean init;
	public Type.type type;
	public String name;
	public int array;
	public boolean var;
	
	public int line;
	public int pos;
	
	private boolean declared; /* Indicates whether this symbol does not exists (no declaration) */
	private boolean duplicated = false;
	
	
	public String hiddenId = "#err";

	public static Symbol newSymbol(String n) {
		return new Symbol(n);
	}
	
	public Symbol(String n) {
		name = n;
		init = false;
	}

	public Symbol(Type.type t, String n, int size, boolean v, int l, int p) {
		line = l;
		pos = p;
		
		type = t;
		name = n;
		array = size;
		var = v;
		
		init = true;
		
		declared = true;
		
		hiddenId = "@" + hiddenCnt++;
	}
	
	public String toString() {
		return name;
	}
	
	public boolean isDeclared() {
		return declared;
	}
	
	public void setDeclared(boolean d) {
		this.declared = d;
	}
	
	public boolean isNumber() {
		return (type == Type.type.FLOAT || type == Type.type.INT);
	}
	
	public boolean isDuplicated() {
		return duplicated;
	}
	
	public void setDuplicated(boolean d) {
		this.duplicated = d;
	}
}
