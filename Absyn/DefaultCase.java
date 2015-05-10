package Absyn;

public class DefaultCase extends Case {
	public boolean def = true;
	public DefaultCase(StmtList sl, boolean b) {
		super(0, sl, b);
	}
}