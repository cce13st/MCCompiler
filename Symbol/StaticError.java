package Symbol;

import Absyn.Type;

public class StaticError {

	public static void DuplicatedDeclaration(Symbol x, Scope target) {
		System.out.println("Duplicated declaration : " + x + " in "
				+ target.loc);
	}

	public static void VarNotDeclared(Symbol x, Scope target) {
		System.out.println("variable " + x + " does not declared in "
				+ target.loc);
	}

	public static void TypeMismatched(Symbol x, Type.type type) {
		System.out.println("Type mismatched " + x + ":" + x.type + " with "
				+ type);
	}
}