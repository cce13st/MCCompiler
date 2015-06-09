package Compile;

import Parse.Frontend;

/**
 * Created by yeonni on 15. 6. 9..
 */

public class Main {


    public static void main(String argv[]) throws Exception {
        String filename = null;


        if (argv.length > 0)
            filename = argv[0];
        else
            filename = "tests/input.txt";

        Frontend parser = new Frontend();
        try {
            parser.build(filename);
        } catch (Exception e) {
            e.printStackTrace();
        }

        CodeGenerator gen = new CodeGenerator(parser.program, parser.table);
        gen.printInstr();
    }
}