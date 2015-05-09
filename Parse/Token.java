package Parse;

import java_cup.runtime.*;

class Token extends java_cup.runtime.Symbol {
	int left, right;

	Token(int l, int r, int kind) {
		super(kind);
		left = l;
		right = r;
	}
}

class FloatToken extends Token {
	float val;

	FloatToken(int l, int r, int kind, float v) {
		super(l, r, kind);
		val = v;
	}
}

class IntToken extends Token {
	int val;

	IntToken(int l, int r, int kind, int v) {
		super(l, r, kind);
		val = v;
	}
}
