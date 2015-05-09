Project root : MCJavaCompiler/
Project have 5 directories
 - Absyn : Unused now
 - ErrorMsg : Unused now
 - Parse : This directory has 'Main' class for execution and lexer/parser classes.
 - Symbol : Unused now
 - libs : It has library files, JFlex and CUP.

1. Building the project
'Makefile' contains build sequence.
You can build the whole project by 'make' command.

2. Executing the MiniC parser
Execute the main java class with CUP library on root directory of the project. 'libs' directory already contains CUP library, so you just need this command.
> java -cp libs/java-cup-11a.jar: Parse/Main
This command execute parser with '/input.txt' as input.

You can pass another file as an argument of the parser.
> java -cp libs/java-cup-11a.jar: Parse/Main your_test_file
Replace 'your_test_file' to anything you want.