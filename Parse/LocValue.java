package Parse;

class LocValue {
	public Object value;
	public int line;
	public int pos;

	public LocValue(int l, int p, Object v) {
		line = l;
		pos = p;
		value = v;
	}
}