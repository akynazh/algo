//const int SIZE = 64;

const int SIZE = 64;

int recog(unsigned char image[SIZE][SIZE]) {
	// 重新设计算法：基于矩形生成原理
	// 每个矩形将其内部所有像素+1，所以矩形的边界像素值会有特定模式
	// 通过分析像素值的差异来识别矩形边界
	
	int squareCount = 0;
	
	// 寻找所有可能的矩形
	for (int y1 = 0; y1 < SIZE; y1++) {
		for (int x1 = 0; x1 < SIZE; x1++) {
			for (int y2 = y1 + 9; y2 < SIZE; y2++) {
				for (int x2 = x1 + 9; x2 < SIZE; x2++) {
					// 检查是否为正方形
					if ((y2 - y1) != (x2 - x1)) continue;
					
					// 检查这个区域是否构成一个矩形
					// 矩形的特征：边界像素值比周围高1
					
					bool isRectangle = 1;
					int boundaryValue = -1;
					
					// 检查上边界
					for (int x = x1; x <= x2; x++) {
						if (boundaryValue == -1) {
							boundaryValue = image[y1][x];
						} else if (image[y1][x] != boundaryValue) {
							isRectangle = 0;
							break;
						}
					}
					
					if (!isRectangle) continue;
					
					// 检查下边界
					for (int x = x1; x <= x2; x++) {
						if (image[y2][x] != boundaryValue) {
							isRectangle = 0;
							break;
						}
					}
					
					if (!isRectangle) continue;
					
					// 检查左边界
					for (int y = y1; y <= y2; y++) {
						if (image[y][x1] != boundaryValue) {
							isRectangle = 0;
							break;
						}
					}
					
					if (!isRectangle) continue;
					
					// 检查右边界
					for (int y = y1; y <= y2; y++) {
						if (image[y][x2] != boundaryValue) {
							isRectangle = 0;
							break;
						}
					}
					
					if (isRectangle) {
						// 检查内部像素是否比边界小1（表示这是矩形内部）
						bool interiorValid = 1;
						for (int y = y1 + 1; y < y2; y++) {
							for (int x = x1 + 1; x < x2; x++) {
								if (image[y][x] != boundaryValue - 1) {
									interiorValid = 0;
									break;
								}
							}
							if (!interiorValid) break;
						}
						
						if (interiorValid) {
							squareCount++;
						}
					}
				}
			}
		}
	}
	
	return squareCount;
}
