package Symbol;

import Absyn.Exp;
import Absyn.Function;
import Absyn.Type;

public class StaticError {

	public static void DuplicatedDeclaration(Symbol x, Scope target, int line, int pos) {
		System.out.println(Error() + LinePos(line, pos) + "Duplicated declaration :" + br(x) + "in function ["
				+ target.loc + "]");
	}
	
	public static void FuncNotDeclared(String name, Scope target, int line, int pos) {
		System.out.println(Error() + LinePos(line, pos) + "Cannot find a function [" + name + "]");
	}

	public static void VarNotDeclared(Symbol x, Scope target, int line, int pos) {
		System.out.println(Error() + LinePos(line, pos) + "variable" + br(x) + "does not declared in this scope");
	}

	public static void VarNotArray(Symbol x, int line, int pos) {
		System.out.println(Error() + LinePos(line, pos) + "This variable" + br(x) + "is not an array type");
	}

	public static void ArrayWithoutIndex(Symbol x, int line, int pos) {
		System.out.println(Error() + LinePos(line, pos) + "This variable" + br(x) + "need an array index");
	}

	public static void TypeMismatched(Symbol x, Type.type type, int line, int pos) {
		System.out.println(Error() + LinePos(line, pos) + "Type mismatched" + br(x) + ":" + x.type + " with type "
				+ type);
	}
	
	public static void TypeMismatched(Type.type type, int line, int pos) {
		System.out.println(Error() + LinePos(line, pos) + "Type mismatched in expression with type "
				+ type);
	}
	
	public static void NotArrayArg(int line, int pos) {
		System.out.println(Error() + LinePos(line, pos) + "Argument type mismatched between array and value :");
	}

	public static void NotArrayParam(int line, int pos) {
		System.out.println(Error() + LinePos(line, pos) + "Argument type mismatched between array and value");
	}
	
	public static void ArgsNumber(String funcName, int line, int pos) {
		System.out.println(Error() + LinePos(line, pos) + "Number of argument does not match to function declaration " + funcName);
	}
	
	public static void NotInteger(int line, int pos) {
		System.out.println(Error() + LinePos(line, pos) + "Not integer index");
	}
	
	public static void NotConditionExp(int line, int pos) {
		System.out.println(Warning() + LinePos(line, pos) + "Cannot convert conditional expression to boolean");
	}
	
	public static void WarnRetType(String funcName, int line, int pos) {
		System.out.println(Warning() + LinePos(line, pos) + "Function return type not matched with return at function " + funcName);
	}

	public static void WarnNoRet(String funcName, int line, int pos) {
		System.out.println(Warning() + LinePos(line, pos) + "Function has no return value at function " + funcName);
	}
	
	public static void WarnConversion(Symbol x, int line, int pos) {
		System.out.println(Warning() + LinePos(line, pos) + "Float value is assigned into integer variable" + br(x));
	}
	
	private static String br(Symbol x) {
		return " [" + x + "] ";
	}
	
	private static String Error() {
		return "[Error]\t\t";
	}
	
	private static String Warning() {
		return "[Warning]\t";
	}
	
	private static String LinePos(int line, int pos) {
		 return "Line " + line + ", " + pos + " : ";
	}
}