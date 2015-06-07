JFLAGS=-g

Parse/Main.class: Absyn/*.java Parse/*.java Parse/Parser.java Parse/sym.java Parse/ Parse/Lexer.java Parse/LocValue.java
		javac -cp .:libs/java-cup-11a.jar ${JFLAGS} Parse/*.java

Absyn/Absyn.class : Absyn/*.java
		javac ${JFLAGS} Absyn/*.java


Parse/Parser.java: Parse/rules.cup
		java -jar libs/java-cup-11a.jar -parser Parser Parse/rules.cup;
		mv Parser.java Parse/Parser.java;
		mv sym.java Parse/sym.java;

Parse/Lexer.java: Parse/lex.flex
		java -jar libs/jflex-1.6.1.jar Parse/lex.flex;

ErrorMsg/ErrorMsg.class:  ErrorMsg/*.java
		javac ${JFLAGS} ErrorMsg/*.java

Symbol/Symbol.class : Symbol/*.java
		javac ${JFLAGS} Symbol/*.java

clean :
		rm -rf Absyn/*.class Symbol/*.class ErrorMsg/*.class Parse/*.class; rm Parse/parser.java; rm Parse/sym.java; rm Parse/Lexer.java
