package Parse;

import java.io.BufferedReader;
import java.io.FileReader;

import Absyn.*;

public class Main {

	public static void main(String argv[]) throws Exception {
		String filename = null;

		if (argv.length > 0)
			filename = argv[0];
		else
			filename = "3";

		System.out.println("--- Start MiniC Java Compiler! ---");

		BufferedReader in = new BufferedReader(new FileReader(filename));

		Lexer l = new Lexer(in);
		System.out.println(l.yytext());
		
		Parser p = new Parser(new Lexer(in));
		Program a = (Program) p.parse().value;
		Visitor v = new Visitor(a);
		v.startVisit();
	}
}
