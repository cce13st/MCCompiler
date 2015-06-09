int arr1[10];
int n;

int main() {
	int i;
	int arr2[10];
	for(i = 0 ; i < 10 ; i = 1 + i){
		arr1[i] = i * i;
	}
	
	for(i = 0 ; i < 10 ; i = 1 + i){
		n = arr1[i];
	}
	
	for(i = 0 ; i < 10 ; i = 1 + i){
		arr2[i] = arr1[i] - 1;
	}
	
	for(i = 0 ; i < 10 ; i = 1 + i){
		n = arr2[i];
	}
	return 0;	
}