#include <bits/stdc++.h>
using namespace std;

int main()
{
	string S, T;
	cin >> S >> T;
	int i = 0, j = 0, slen = S.length(), tlen = T.length();
	
	while (i < slen && j < tlen){
		if (S[i] == T[j]){
			i++; j++;
		} else {
			j = 0; i = (i - j) + 1; 
		}
	}
	if(j == tlen)	cout << i-tlen << endl;
	else cout << "no" << endl;
	
	return 0;
}
