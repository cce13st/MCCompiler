package Symbol;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Scope {
	public LinkedHashMap<String, Symbol> map;
	public Scope outer;
	public String loc;
	public boolean isFunc;
	public ArrayList<Scope> descend;
	
	public int ifnum = 1;
	public int whilenum = 1;
	public int fornum = 1;
	public int comnum = 1;

	public Scope(Scope out, String l, boolean isF) {
		map = new LinkedHashMap<String, Symbol>();
		descend = new ArrayList<Scope>();
		outer = out;
		if (out != null)
			out.descend.add(this);
		loc = l;
		isFunc = isF;
	}
	
	public Scope newForScope() {
		Scope s = new Scope(this, "for(" + this.fornum + ")", false);
		this.fornum++;
		return s;
	}

	public Scope newIfScope(boolean isIf) {
		String name;
		if (isIf) 
			name = "if(";
		else
			name = "else(";
		
		Scope s = new Scope(this, name + this.ifnum + ")", false);
		this.ifnum++;
		return s;
	}

	public Scope newCompoundScope() {
		Scope s = new Scope(this, "compound(" + this.comnum + ")", false);
		this.comnum++;
		return s;
	}
	
	public Scope newWhileScope() {
		Scope s = new Scope(this, "while(" + this.whilenum + ")", false);
		this.whilenum++;
		return s;
	}

	public void addBind(String key, Symbol s) {
		if (map.get(key) != null) {
			s.setDuplicated(true);
		}
		else
			map.put(key, s);
	}
	
	public Symbol lookup(String key) {
		Scope current = this;
		while (current != null) {
			Symbol s = current.map.get(key);
			if (s != null) {
				return s;
			}
			current = current.outer;
		}
		return null;
	}
	
	public String getLocName() {
		String locName = loc;
		Scope current = this;
		
		while(!current.isFunc) {
			current = current.outer;
			if (current.loc == "GLOBAL")
				break;
			locName = current.loc + "-" + locName;
		}
		
		return locName;
	}
}