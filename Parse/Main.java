package Parse;

import java.io.BufferedReader;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;

import Absyn.Program;
import Absyn.Visitor;
import Symbol.Table;

public class Main {

	static private final String treeOut = "tree.txt";
	static private final String tableOut = "table.txt";

	public static void main(String argv[]) throws Exception {
		String filename = null;
		PrintStream outstream = null;
        outstream = new PrintStream(new FileOutputStream(treeOut));
        System.setOut(outstream);  

		if (argv.length > 0)
			filename = argv[0];
		else
			filename = "Test5";

		BufferedReader in = new BufferedReader(new FileReader(filename));
		Lexer l = new Lexer(in);
		Parser p = new Parser(l);

		Program a = (Program) p.parse().value;
		Visitor v = new Visitor(a);
//		v.printAST();
		
//        outstream = new PrintStream(new FileOutputStream(tableOut));
//        System.setOut(outstream);  
		
		Table t = new Table();
		t.fillTable(a);
//		t.printTable();
		
		outstream = new PrintStream(new FileOutputStream(FileDescriptor.out));
        System.setOut(outstream);  
		boolean result = t.typeAnalysis(a);
		
		if (result) {
			v.printAST();
		}
	}
}
