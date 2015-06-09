int f1(int a) {
	if (a == 1) {
		return 2;
	} else if (a == 2) {
		return 4;
	} else if (a == 3) {
		return 5;
	}
}

int main() {
	f1(1);
	f1(2);
	f1(3);
	f1(4);
	return 0;	
}