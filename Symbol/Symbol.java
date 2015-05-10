package Symbol;

import Absyn.Type;

public class Symbol {
	public boolean init;
	public Type.type type;
	public String name;
	public int array;
	public boolean var;
	
	public static Symbol newSymbol(String n) {
		return new Symbol(n);
	}
	
	public Symbol(String n) {
		name = n;
		init = false;
	}

	public Symbol(Type.type t, String n, int size, boolean v) {
		type = t;
		name = n;
		array = size;
		var = v;
		
		init = true;
	}
	
	public String toString() {
		return name;
	}
}
