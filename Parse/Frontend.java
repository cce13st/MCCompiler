package Parse;

import java.io.BufferedReader;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;

import Absyn.Program;
import Absyn.Visitor;
import Compile.CodeGenerator;
import Symbol.Table;

public class Frontend {

	static private final String treeOut = "tree.txt";
	static private final String tableOut = "table.txt";
	public Program program;
	public Table table;

	public void build(String filename) throws Exception {
		PrintStream outstream = null;
        outstream = new PrintStream(new FileOutputStream(treeOut));
        System.setOut(outstream);

		BufferedReader in = new BufferedReader(new FileReader(filename));
		Lexer l = new Lexer(in);
		Parser p = new Parser(l);

		Program a = (Program) p.parse().value;
		Visitor v = new Visitor(a);
		v.printAST();
		
        outstream = new PrintStream(new FileOutputStream(tableOut));
        System.setOut(outstream);  
		
		Table t = new Table();
		t.fillTable(a);
		t.printTable();
		v.printAST();
		t.typeAnalysis(a);
		
		System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
		this.program = a;
		this.table = t;
	}
}
