#include<stdio.h>
#include<string.h>

int main()
{
	char S[100] = {0}, T[100] = {0};	
	
	gets(S);
	gets(T);
	char *ans = strstr(S, T);
	if(ans != NULL)	printf("%d\n", int(ans - S));
	else printf("-1\n");
	
	return 0;
} 
