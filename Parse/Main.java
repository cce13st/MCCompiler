package Parse;

import java.io.BufferedReader;
import java.io.FileReader;

import Absyn.Program;
import Absyn.Visitor;
import Symbol.Table;

public class Main {
	
	public static Symbol.Table table;

	public static void main(String argv[]) throws Exception {
		String filename = null;

		if (argv.length > 0)
			filename = argv[0];
		else
			filename = "input.txt";

		System.out.println("--- Start MiniC Java Compiler! ---");

		BufferedReader in = new BufferedReader(new FileReader(filename));
		Lexer l = new Lexer(in);
		Parser p = new Parser(l);
		
		Program a = (Program) p.parse().value;
		Visitor v = new Visitor(a);
		v.printAST();
		
		Table t = new Table();
		t.fillTable(a);
		t.printTable();
	}
}
