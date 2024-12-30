#include <bits/stdc++.h>
using namespace std;
const int maxLen = 100;
/*
ababbabaababaabababaaababaaaba
ababaaababaa
0
*/
void get_next(string T, int *next) {
	next[0] = -1; // 下标从0开始则标记开始处为-1(从1开始则标记为0) 因为该处不能回溯
	int i = 0, j = -1, tlen = T.length(); // 求取字符T[0..i-1]前后缀字符串相似度 j为匹配开始处
	while (i < tlen) { 
		if (j == -1 || T[i] == T[j]) { // 若j为-1表示只能回溯到下标0
			i++; j++;
			// next[i] = j; // 标记回溯点 T[i-1]==T[j-1] (0) (改进前的操作)
			if (T[i] != T[j]) next[i] = j; 
			else next[i] = next[j]; // 当前T[i]==T[j] 到时匹配时若只回溯到j则也一定匹配不相等 所以直接匹配到不相等的位置 
		}
		else j = next[j]; // 利用已经生成了的next数组进行回溯
	}
}

int kmp(string S, string T, int pos) {
	int i = pos, j = 0, slen = S.length(), tlen = T.length();
	int next[maxLen];
	get_next(T, next); // 得到T的next数组
	while (i < slen && j < tlen) {
		if (j == -1 || T[j] == S[i]) {
			i++; j++;
		} 
		else j = next[j]; //对j进行回溯
	}
	for (int i = 0; i < T.length(); i++) cout << next[i] << " ";cout << endl;
	if (j == tlen) return i - tlen;
	else return -1;
}

int main()
{
	string S, T;
	int pos, ans; //从S[pos]这个位置开始匹配

	cin >> S >> T >> pos;
	ans = kmp(S, T, pos);
	cout << ans << endl;


	return 0;
}
