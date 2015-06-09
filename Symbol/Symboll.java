package Symbol;

import Absyn.Type;

public class Symboll {
    /* Unique id generator */
	private static int hiddenCnt = 1;

    /* Current Location pointer(= Stack pointer) for each procedure) */
    private static int locpoint = 0; 
	
	public boolean init;        // initialized
	public Type.type type;      // INT or FLOAT
	public String name;         // name
	public int array;           // size of array, (if array=1, it is a simple variable)
	public boolean var;         // if false, it is a parameter
	
	public int line;
	public int pos;
	
	private boolean declared;               /* Indicates whether this symbol does not exists (no declaration) */
	private boolean duplicated = false;     /* Indicates whether this symbol has duplicated declaration */
	
	public int hiddenId = -1;               /* Unique id for each variable */
    public int offset = -1;               /* Relative address from SP */

	public static Symboll newSymbol(String n) {
		return new Symboll(n);
	}
	
	public Symboll(String n) {
		name = n;
		init = false;
	}

    public Symboll(int l, int p, String n, int index) {
        this.line = l;
        this.pos = p;
        name = n;
        init = false;
        array = index;
    }

	public Symboll(Type.type t, String n, int size, boolean v, int paramIdx, int l, int p) {
		line = l;
		pos = p;
		
		type = t;
		name = n;
		array = size;
		var = v;
		
		init = true;
		declared = true;
		
		hiddenId = hiddenCnt++;             // give a unique id
        
        if (var) {
            /* If variable, just give a new location */
            offset = locpoint;                    
            if (array == 0)
                locpoint++;
            else
                locpoint += array;        
        }
        else {
            offset = -paramIdx-1;
        }
	}
	
	public String toString() {
		return name;
	}
	
	public boolean isDeclared() {
		return declared;
	}
	
	public void setDeclared(boolean d) {
		this.declared = d;
	}
	
	public boolean isNumber() {
		return (type == Type.type.FLOAT || type == Type.type.INT);
	}
	
	public boolean isDuplicated() {
		return duplicated;
	}
	
	public void setDuplicated(boolean d) {
		this.duplicated = d;
	}

    public boolean isGlobal(Table table) {
        Symboll s = table.global.lookup(this.name);

        if (s == null)
            return false;
        
        return this.hiddenId == s.hiddenId;
	}

    public String getHidden() {
        if (this.hiddenId == -1)
            return "#err";
        else
            return "@" + this.hiddenId;
    }

    public int getOffset() {
        return this.offset;
    }

    public static void clearLoc() {
        /* Clear location pointer, for new function procedure */
        locpoint = 0;
    }
}
