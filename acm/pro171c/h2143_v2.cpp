#include <queue>
#include <iostream>
#include <set>
#include <unordered_map>
#define ri register int
using namespace std;
// ����Ϣ�������Ŀû��ɾ������ - ���ٲ��ÿ���ʱ��������
// �ؼ�֡���������� - �ȴ�1s1s���ָ��¿�ʼ����
// ����HQ�ϵ�ĳ�ݴ𰸵�˼·�޸� - Arpit - 20230816
// 1. ʹ��set��Ϊ�ܵ�waitlist ���� 1. time, 2. line id����
// 2. ʹ�� �𲽸��� - �����ǹؼ�֡���� - �о���������Ż�
// 3. ע��ʱ�̲����ظ�
// �����ܲ�����ԭ���Ļ����ϸ�, ͨ��ȫ�ֱ�����¼�Ƿ����� �� ��ǰʱ�� 
struct lines
{
	// -1 ��ʾ����
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
	int state; //������ǰ����״̬ 1 - �ȴ�, 2 - ��������, 3 - �������
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
// updateҪ�� = Ҫ��֤, ÿ��ʱ��ֻ����һ��, ���ÿ��update���н���, timenowӦ��ָ�򱾴�mTime����һʱ�� - Ҳ���� mTime + 1;
void update(int mTime) {
	for (; timenow <= mTime; ++timenow)
	{
		bool completed = false;

		// ����timenow֮ǰ��ȫ������
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

			// �� line number ����������
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
	timenow = mTime + 1; // ��֤timenow ���Ǵӱ��ε�mTime��һʱ�̿�ʼ
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
	// ˫����
	update(tStamp - 1); // �ȸ��µ�tStamp��ǰһʱ��
	// �ǲ��ǿ����Ȳ���, �ٸ���?
	orderlist[ordernum].eid = eId;
	orderlist[ordernum].lid = mLine;
	orderlist[ordernum].mid = pId;
	orderlist[ordernum].rtime = mTime;
	orderlist[ordernum].state = 1;
	linelist[mLine].waitlist.push(ordernum);
	m2oid[pId] = ordernum++;
	//
	rnew = true;
	update(tStamp); //�ڲ�����ڸ���һ��
	int toid = linelist[mLine].woid;
	if (toid == -1) return -1;
	return orderlist[toid].mid; // ���ز�������line�����ڹ�����״̬
}

int status(int tStamp, int pId) {
	update(tStamp);
	if (m2oid.count(pId) == 0) return 0;
	int toid = m2oid[pId];
	return orderlist[toid].state;
}
