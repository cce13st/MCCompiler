package Absyn;

public class Type {
	public enum type {
		INT, FLOAT
	};
	
	public type ty;
	
	public Type (type ty) {
		this.ty = ty;
	}
}