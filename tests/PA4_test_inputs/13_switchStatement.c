int main() {
	int i;
	int n;
	
	for(i = 0 ; i < 10 ; i = 1 + i){

		switch(i) {
			case 0:
				n = 0;
			case 1:
				n = -1;
			case 2:
				n = -4;
				break;
			case 4:
				n = -16;
			case 6:
				n = -36;
			default:
				n = i;
		}
	}
	return 0;	
}