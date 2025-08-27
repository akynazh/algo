#include<iostream>

using namespace std;

void BubbleSort(int *a, int len)
{
	for(int i = 0; i < len - 1; i++){
		int flag = 0;
		for(int j = len - 1; j >= 1; j--){
			if(a[j] < a[j-1]){
				swap(a[j-1],a[j]);
				flag = 1;
			}
		}
		if(flag == 0)	break;
	}
}

void SelectSort(int *a, int len)
{
	int i, j, k;
	for(i = 0; i < len-1; i++){
		for(j = i+1, k = i; j < len; j++)
			if(a[j] < a[k])	k = j;
		if(k != i)	swap(a[i], a[k]);
	}
}

void InsertSort(int *a, int len)
{
	int temp, i, j;
	for(i = 1; i < len; i++){
		if(a[i] < a[i-1]){
			temp = a[i];
			for(j = i-1; j >= 0 && a[j] > temp; j--)	
				a[j+1] = a[j];
			a[j+1] = temp;
		}
	}
}

void ShellSort(int *a, int len)
{
	int i, j, temp, increment = len;
	do{
		increment = increment/3 + 1;//增量序列 
		for(i = increment; i < len; i++){
			if(a[i] < a[i-increment]){
				temp = a[i];
				for(j = i - increment; j >= 0 && a[j] > temp; j-=increment)
					a[j+increment] = a[j];
				a[j+increment] = temp;
		}
		}
	}while(increment > 1);
}

//选取枢轴并把数组分区 
int Partition(int *a, int low, int high)
{
	int pivotkey; 
	int m = low + (high - low) / 2;
	//优化选取枢轴值，三数取中 
	if(a[low] > a[high])	swap(a[low], a[high]);
	if(a[m] > a[high])	swap(a[m], a[high]);
	if(a[m] > a[low])	swap(a[m], a[low]);
	pivotkey = a[low];
	while(low < high){
		while(low < high && a[high] >= pivotkey)	high--;
		a[low] = a[high];
		while(low < high && a[low] <= pivotkey)	low++;
		a[high] = a[low];
	}
	a[low] = pivotkey;
	return low;//返回枢轴位置下标 
}
void QuickSort(int *a, int low, int high)//low为初始位置下标，high为末位置下标 
{
	int pivot;//枢轴位置下标 
	if(low < high){
		pivot = Partition(a, low, high);
		QuickSort(a, low, pivot-1);
		QuickSort(a, pivot+1, high); 
	}
}
void QuickSort_update(int *a, int low, int high)//low为初始位置下标，high为末位置下标 
{
	int pivot;//枢轴 
	if(high - low > 50){
		while(low < high){
		pivot = Partition(a, low, high);
		QuickSort(a, low, pivot-1);
		low = pivot + 1; //尾递归操作 
		}
	}
	else//改用插入排序 
		InsertSort(a, high+1);//high+1就是len 
}

void HeapAdjust(int *a, int m, int len)//堆调整 
{
	int temp = a[m], j;
	for(j = 2*m; j <= len; j *= 2){
		if(j < len && a[j+1] > a[j])	j++;
		if(temp > a[j])	break;
		a[m] = a[j];
		m = j;
	}
	a[m] = temp;
}
void HeapSort(int *a, int len)
{
	//生成一个大顶堆
	for(int i = len/2; i > 0; i--)
		HeapAdjust(a, i, len);
	//进行排序	 
	for(int i = len; i > 1; i--){
		swap(a[i], a[1]);
		HeapAdjust(a, 1, i-1);//剩余元素继续生成一个大顶堆 
	}
}

//归并函数 
void Merge(int *in, int *out, int i, int m, int n)//i起点，m中点，n终点 
{
	int j, k;//k为out的移动下标
	for(j = m+1, k = i; i <= m && j <= n; k++){
		if(in[i] < in[j])	out[k] = in[i++];
		else out[k] = in[j++];
	}
	//剩余有序元素直接并入out数组 
	if(i <= m){
		for(int p = 0; p <= m-i; p++)
			out[k+p] = in[i+p];
	}
	if(j <= n){
		for(int p = 0; p <= n-j; p++)
			out[k+p] = in[j+p];
	}
}
//迭代函数
//s为一个增量，变化为1，2，4，8，16
//i为起点，i+s-1指向中点左，i+2s-1指向终点  
void MergePass(int *in, int *out, int s, int len)
{ 
	int i = 1; 
	while(i+2*s-1 <= len){ 
		Merge(in, out, i, i+s-1, i+2*s-1);
		i += 2*s;
	}
	//若最后还剩两个序列（但是i+2s-1>len,表明后一个序列达不到终点）
	if(i+s-1 < len)	Merge(in, out, i, i+s-1, len);//归并最后两个序列  
	else//若最后只剩下单个子序列（不存在后序列）	
		for(int j = i; j <= len; j++)	
			out[j] = in[j];
}
//归并排序&迭代 
void MergeSort_Iteration(int *a, int len)
{
	int b[len] = {0}, k = 1;
	while(k < len){
		MergePass(a, b, k, len);
		k*=2;
		MergePass(b, a, k, len);
		k*=2;
	}
}
//归并排序&递归 
void MergeSort_Recursion(int *p, int *q, int s, int t)
{
	int m, mid[11];
	if(s == t)	q[s] = p[s];
	//MergeSort递归终止条件 
	//即单个单词存在时，存入mid媒介数组 
	else{
		m = (s+t) / 2;
		MergeSort_Recursion(p, mid, s, m);//左递归 
		MergeSort_Recursion(p, mid, m+1, t);//右递归 
		Merge(mid, q, s, m, t);//左右归并递归 
	}	
} 

int main()
{
	int array[10] = {8,7,6,5,4,2,12,5,9,1};
//	BubbleSort(array, 10);
//	SelectSort(array, 10);
//	InsertSort(array, 10);
//	ShellSort(array, 10);
//  QuickSort(array, 0, 9);
//  QuickSort_update(array, 0, 9);
//	for(int i = 0; i < 10; i++)
//		cout << array[i] << ' '; 

	int array_tree[11] = {0,8,7,6,5,4,2,12,5,9,1};
//	HeapSort(array_tree, 10);
//	MergeSort_Recursion(array_tree, array_tree, 1, 10);
//	MergeSort_Iteration(array_tree, 10);
//	for(int i = 1; i < 11; i++)
//		cout << array_tree[i] << ' ';
	return 0;
	
}
