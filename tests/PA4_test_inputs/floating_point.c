float a;

float f1(float p) {
	a = 1.1 * p;
	return a - 1.2;
}

int main() {
	float x,  y;
	x = 2.3 * 12;
	y = f1(x);
	return 0;
}