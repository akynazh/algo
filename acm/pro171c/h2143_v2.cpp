#include <queue>
#include <iostream>
#include <set>
#include <unordered_map>
#define ri register int
using namespace std;
// 好消息是这个题目没有删除操作 - 至少不用考虑时间问题了
// 关键帧更新有问题 - 先从1s1s这种更新开始试试
// 按照HQ上的某份答案的思路修改 - Arpit - 20230816
// 1. 使用set作为总的waitlist 按照 1. time, 2. line id排序
// 2. 使用 逐步更行 - 而不是关键帧分析 - 感觉这个可以优化
// 3. 注意时刻不能重复
// 看看能不能再原来的基础上改, 通过全局变量记录是否增加 和 当前时间 
struct lines
{
	// -1 表示空闲
	int lid;
	queue<int> waitlist;
	int woid;
	int endtime;
	void init(int ll)
	{
		lid = ll;
		waitlist = queue<int>();
		woid = -1;
		endtime = -1;
	}
}linelist[501];
int linenum;
bool equipempty[501];
struct orders
{
	int mid, eid, lid;
	int rtime, endtime;
	int state; //存贮当前订单状态 1 - 等待, 2 - 正在生产, 3 - 生产完成
}orderlist[20001];
int ordernum;
struct ordercomp
{
	bool operator()(int toid1, int toid2) const
	{
		orders& o1 = orderlist[toid1];
		orders& o2 = orderlist[toid2];
		if (o1.endtime != o2.endtime) return o1.endtime < o2.endtime;
		return toid1 < toid2;
	}
};
set<int, ordercomp> h1;
unordered_map<int, int> m2oid(20001);
int timenow;
bool rnew;
// update要点 = 要保证, 每个时刻只更新一次, 因此每次update运行结束, timenow应该指向本次mTime的下一时刻 - 也就是 mTime + 1;
void update(int mTime) {
	for (; timenow <= mTime; ++timenow)
	{
		bool completed = false;

		// 把再timenow之前的全部处理
		auto it = h1.begin();
		while (it != h1.end() && orderlist[*it].endtime <= timenow)
		{
			int toid = *it;
			orders& o1 = orderlist[toid];
			o1.state = 3;
			equipempty[o1.eid] = true;
			linelist[o1.lid].woid = -1;
			completed = true;
			it = h1.erase(it);
		}

		if (rnew || completed) {
			rnew = false; 

			// 按 line number 升序尝试启动
			for (int i = 0; i < linenum; i++) {
				lines& l = linelist[i];
				if (l.woid != -1 || l.waitlist.empty()) continue;

				int toid = l.waitlist.front();
				orders& o = orderlist[toid];
				if (!equipempty[o.eid]) continue;
				l.waitlist.pop();
				o.state = 2;
				o.endtime = timenow + o.rtime;
				l.woid = toid;
				h1.insert(toid);
				equipempty[o.eid] = false;
			}
		}
	}
	timenow = mTime + 1; // 保证timenow 总是从本次的mTime下一时刻开始
}
void init(int L, int M) {
	ordernum = 0;
	m2oid.clear();
	linenum = L;
	timenow = 0;
	rnew = false;
	h1.clear();
	for (ri i = 0; i < M; i++) equipempty[i] = true;
	for (ri i = 0; i < L; i++) linelist[i].init(i);
	return;
}

int request(int tStamp, int pId, int mLine, int eId, int mTime) {
	// 双更新
	update(tStamp - 1); // 先更新到tStamp的前一时刻
	// 是不是考虑先插入, 再更新?
	orderlist[ordernum].eid = eId;
	orderlist[ordernum].lid = mLine;
	orderlist[ordernum].mid = pId;
	orderlist[ordernum].rtime = mTime;
	orderlist[ordernum].state = 1;
	linelist[mLine].waitlist.push(ordernum);
	m2oid[pId] = ordernum++;
	//
	rnew = true;
	update(tStamp); //在插入后在更新一次
	int toid = linelist[mLine].woid;
	if (toid == -1) return -1;
	return orderlist[toid].mid; // 返回插入的这个line的正在工作的状态
}

int status(int tStamp, int pId) {
	update(tStamp);
	if (m2oid.count(pId) == 0) return 0;
	int toid = m2oid[pId];
	return orderlist[toid].state;
}
