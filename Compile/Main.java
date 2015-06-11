package Compile;

import Parse.Frontend;

import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * Created by yeonni on 15. 6. 9..
 */

public class Main {


    public static void main(String argv[]) throws Exception {
        String filename = null;

        if (argv.length > 0)
            filename = argv[0];
        else
            filename = "tests/PA4_test_inputs/3_printf2.c";

        Frontend parser = new Frontend();
        try {
            parser.build(filename);
        } catch (Exception e) {
            e.printStackTrace();
        }

        CodeGenerator gen = new CodeGenerator(parser.program, parser.table);
        int extension = filename.indexOf('.');
        System.out.println(extension);
        
        String Tcodename;
        
        if (extension == -1)
        	Tcodename = filename + ".T";
        else
        	Tcodename = filename.substring(0, extension) + ".T";
        
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(Tcodename));
            out.write(gen.emit());
            out.newLine();
            out.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        System.out.println("Compile DONE: " + Tcodename);
    }
}