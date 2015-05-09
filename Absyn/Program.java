package Absyn;

public class Program extends Absyn {
	public DeclList dlist;
	public FuncList flist;
	
	public Program(DeclList d, FuncList fl) {
		dlist = d;
		flist = fl;
	}
}