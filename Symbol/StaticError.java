package Symbol;

import Absyn.Function;
import Absyn.Type;

public class StaticError {

	public static void DuplicatedDeclaration(Symbol x, Scope target, int line, int pos) {
		System.out.println("Duplicated declaration : " + x + " in "
				+ target.loc + LinePos(line, pos));
	}
	
	public static void FuncNotDeclared(String name, Scope target) {
		System.out.println("Cannot find a function " + name);
	}

	public static void VarNotDeclared(Symbol x, Scope target, int line, int pos) {
		System.out.println("variable " + x + " does not declared in "
				+ target.loc + LinePos(line, pos));
	}

	public static void TypeMismatched(Symbol x, Type.type type) {
		System.out.println("Type mismatched " + x + ":" + x.type + " with "
				+ type);
	}
	
	public static void NotInteger(int line, int pos) {
		System.out.println("Not integer index" + LinePos(line, pos));
	}
	
	public static void NotConditionExp(int line, int pos) {
		System.out.println("Cannot convert type to boolean for conditional" + LinePos(line, pos));
	}
	
	private static String LinePos(int line, int pos) {
		 return " at line " + line + ", pos " + pos;
	}
}