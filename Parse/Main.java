package Parse;

import java.io.BufferedReader;
import java.io.FileReader;

import Absyn.Function;
import Absyn.Program;

public class Main {

	public static void main(String argv[]) throws Exception {
		String filename = null;

		if (argv.length > 0)
			filename = argv[0];
		else
			filename = "input.txt";

		System.out.println("--- Start MiniC Java Compiler! ---");

		BufferedReader in = new BufferedReader(new FileReader(filename));

		Lexer l = new Lexer(in);
		System.out.println(l.yytext());
		
		Parser p = new Parser(new Lexer(in));
		/* open input files, etc. here */
		Program a = (Program) p.parse().value;
		Function f = a.flist.list.get(0);
		System.out.println(f.id);
	}
}
