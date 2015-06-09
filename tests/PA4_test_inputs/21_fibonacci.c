int Fibonacci(int n) {
   if ( n == 0 )
      return 0;
   else if ( n == 1 )
      return 1;
   else
      return ( Fibonacci(n-1) + Fibonacci(n-2) );
} 

int main() {
	int n, i, c, k; 
	n = 5;
	i = 1;
	for ( c = 1 ; c <= n ; c = c + 1 )
	{
		k = Fibonacci(i);
		i = i + 1; 
	}

	return 0;
}