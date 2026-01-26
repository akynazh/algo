typedef struct {
	int y, x;
}Coordinates;

typedef struct {
	Coordinates src;
	Coordinates dest;
} Delivery;

extern bool deliver(int mID);

void process(Coordinates mRider, Delivery mDeliveries[])
{
	// 优化的贪心算法：考虑剩余时间和订单完成状态
	int currentX = mRider.x;
	int currentY = mRider.y;
	
	// 跟踪已完成的订单
	char completed[2000];
	for (int i = 0; i < 2000; i++) {
		completed[i] = 0;
	}
	
	while (1) {
		int bestOrder = -1;
		double bestRatio = -1.0;
		int bestTime = 0;
		
		// 遍历所有未完成的订单，找到最优的
		for (int i = 0; i < 2000; i++) {
			if (completed[i]) continue;
			
			// 计算到餐厅的时间
			int timeToRestaurant = (currentX - mDeliveries[i].src.x > 0 ? currentX - mDeliveries[i].src.x : mDeliveries[i].src.x - currentX)
			                     + (currentY - mDeliveries[i].src.y > 0 ? currentY - mDeliveries[i].src.y : mDeliveries[i].src.y - currentY);
			
			// 计算配送时间
			int deliveryTime = (mDeliveries[i].dest.x - mDeliveries[i].src.x > 0 ? mDeliveries[i].dest.x - mDeliveries[i].src.x : mDeliveries[i].src.x - mDeliveries[i].dest.x)
			                 + (mDeliveries[i].dest.y - mDeliveries[i].src.y > 0 ? mDeliveries[i].dest.y - mDeliveries[i].src.y : mDeliveries[i].src.y - mDeliveries[i].dest.y);
			
			int totalTime = timeToRestaurant + deliveryTime;
			
			// 计算得分
			int score = 3000 + 300 * deliveryTime;
			
			// 计算性价比：得分/时间
			// 数学原理：基于分数背包问题的贪心策略
			// 目标：最大化单位时间收益 score/time 当时间有限时，优先选择单位时间收益最高的任务
			// 这是解决带约束优化问题的经典贪心方法
			double ratio = (double)score / totalTime;
			
			// 优先选择性价比高的订单
			// 当时间有限时，优先完成单位时间收益最高的任务
			if (ratio > bestRatio) {
				bestRatio = ratio;
				bestOrder = i;
				bestTime = totalTime;
			}
		}
		
		// 如果没有找到合适的订单，结束
		if (bestOrder == -1) break;
		
		// 执行配送
		if (deliver(bestOrder)) {
			completed[bestOrder] = 1;
			currentX = mDeliveries[bestOrder].dest.x;
			currentY = mDeliveries[bestOrder].dest.y;
		} else {
			// 如果配送失败（时间不够或其他原因），标记为已完成并继续
			completed[bestOrder] = 1;
		}
	}
}
