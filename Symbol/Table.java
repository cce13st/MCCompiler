package Symbol;

import java.util.HashMap;

import Absyn.Function;
import Absyn.Program;

public class Table {
	public Scope global;
	public HashMap<String, Function> funcMap;
	
	public Table() {
		global = new Scope(null, "GLOBAL", true);
		funcMap = new HashMap<String, Function>();
	}
	
	public void addFunction(Function f) {
		funcMap.put(f.id, f);
	}
	
	public void fillTable(Program p) {
		TableFiller filler = new TableFiller(this);
		filler.fillTable(p);
	}
	
	public void printTable() {
		printScope(global);
	}
	
	public void printScope(Scope s) {
//		if (s.map.keySet().size() == 0) {
//			for (int i=0; i<s.descend.size(); i++) {
//				printScope(s.descend.get(i));
//			}
//			return;
//		}
		
		System.out.println("Function name : " + s.getLocName());
		System.out.println("-------------------------------------------------");
		
		int cnt = 1;
		
		for (String key : s.map.keySet()) {
			Symbol sym = s.map.get(key);
			String arrayStr;
			
			if (sym.array == 0)
				arrayStr = "   ";
			else
				arrayStr = sym.array + "";
				
			System.out.print(cnt + "\t\t" + key + "\t\t" + sym.type.toString() + "\t\t" + arrayStr + "\t\t");
			if(sym.var)
				System.out.println("variable");
			else
				System.out.println("parameter");
			cnt++;
		}
		System.out.println("");
		
		for (int i=0; i<s.descend.size(); i++) {
			printScope(s.descend.get(i));
		}
	}
	
	public void typeAnalysis(Program p) {
		TypeAnalyzer ta = new TypeAnalyzer(global, p);
		ta.startAnalysis();
	}
}