package Symbol;

import java.util.Set;

import Absyn.Program;

public class Table {
	public Scope global;
	
	public Table() {
		global = new Scope(null, "GLOBAL", true);
	}
	
	public void fillTable(Program p) {
		TableFiller filler = new TableFiller(this);
		filler.fillTable(p);
	}
	
	public void printTable() {
		printScope(global);
	}
	
	public void printScope(Scope s) {
		if (s.map.keySet().size() == 0) {
			for (int i=0; i<s.descend.size(); i++) {
				printScope(s.descend.get(i));
			}
			return;
		}
		
		System.out.println("Function name : " + s.getLocName());
		System.out.println("-------------------------------------------------");
		
		int cnt = 1;
		
		for (String key : s.map.keySet()) {
			Symbol sym = s.map.get(key);
			System.out.print(cnt + "\t" + key + "\t" + sym.type.toString() + "\t\t" + sym.array + "\t");
			if(sym.var)
				System.out.println("variable");
			else
				System.out.println("parameter");
			cnt ++;
		}
		System.out.println("");
		
		for (int i=0; i<s.descend.size(); i++) {
			printScope(s.descend.get(i));
		}
	}
}