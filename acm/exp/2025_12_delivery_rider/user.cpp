typedef struct {
	int y, x;
} Coordinates;

typedef struct {
	Coordinates src;
	Coordinates dest;
} Delivery;

typedef struct {
	int id;
	int value;  // 价值 = 得分 / 时间消耗
	int time;   // 完成该任务所需时间
	int score;  // 完成该任务可获得的得分
} Task;

extern bool deliver(int mID);

// 计算曼哈顿距离
static int distance(Coordinates a, Coordinates b) {
	int dy = a.y - b.y;
	int dx = a.x - b.x;
	return (dy >= 0 ? dy : -dy) + (dx >= 0 ? dx : -dx);
}

// 快速排序函数，按价值降序排列
static void quickSort(Task arr[], int low, int high) {
	if (low >= high) return;
	
	int pivot = arr[high].value;
	int i = low - 1;
	
	for (int j = low; j < high; j++) {
		if (arr[j].value > pivot) {  // 按价值降序排列
			i++;
			Task temp = arr[i];
			arr[i] = arr[j];
			arr[j] = temp;
		}
	}
	
	Task temp = arr[i+1];
	arr[i+1] = arr[high];
	arr[high] = temp;
	
	quickSort(arr, low, i);
	quickSort(arr, i+2, high);
}

void process(Coordinates mRider, Delivery mDeliveries[]) {
	Task tasks[2000];
	int taskCount = 0;
	
	// 计算每个任务的价值
	for (int i = 0; i < 2000; i++) {
		// 计算完成该任务所需的时间
		int timeToSrc = distance(mRider, mDeliveries[i].src);
		int timeToDest = distance(mDeliveries[i].src, mDeliveries[i].dest);
		int totalTime = timeToSrc + timeToDest;
		
		// 计算该任务的得分
		int dist = timeToDest;
		int score = 3000 + 300 * dist;
		
		// 计算价值（得分/时间）
		if (totalTime > 0) {
			int value = score / totalTime;
			
			tasks[taskCount].id = i;
			tasks[taskCount].value = value;
			tasks[taskCount].time = totalTime;
			tasks[taskCount].score = score;
			taskCount++;
		}
	}
	
	// 按价值排序
	if (taskCount > 1) {
		quickSort(tasks, 0, taskCount - 1);
	}
	
	// 贪心选择任务
	for (int i = 0; i < taskCount; i++) {
		deliver(tasks[i].id);
	}
}
