int recog(unsigned char image[64][64]) {
    int square_count = 0;

    for (int y = 0; y < 64; y++) {
        for (int x = 0; x < 64; x++) {
            // 1. 获取当前像素及其上方、左方、左上方像素的值
            int curr = image[y][x];
            int up   = (y > 0) ? image[y - 1][x] : 0;
            int left = (x > 0) ? image[y][x - 1] : 0;
            int ul   = (y > 0 && x > 0) ? image[y - 1][x - 1] : 0;

            // 2. 检测左上角点
            // 如果一个矩形在这里开始，curr 增加的幅度会比 up 和 left 叠加后的效果还要大
            // 判定公式：curr - up - left + ul > 0
            int diff = curr - up - left + ul;

            if (diff > 0) {
                // 找到了一个矩形的左上角，现在探测它的宽度和高度
                // 沿着边缘寻找，直到差分值发生变化
                int sx = x;
                int sy = y;
                
                // 探测宽度 ex - sx
                int ex = sx;
                while (ex + 1 < 64) {
                    int c_curr = image[sy][ex + 1];
                    int c_up   = (sy > 0) ? image[sy - 1][ex + 1] : 0;
                    int c_left = image[sy][ex];
                    int c_ul   = (sy > 0) ? image[sy - 1][ex] : 0;
                    if (c_curr - c_up - c_left + c_ul == diff) {
                        ex++;
                    } else {
                        break;
                    }
                }

                // 探测高度 ey - sy
                int ey = sy;
                while (ey + 1 < 64) {
                    int c_curr = image[ey + 1][sx];
                    int c_up   = image[ey][sx];
                    int c_left = (sx > 0) ? image[ey + 1][sx - 1] : 0;
                    int c_ul   = (sx > 0) ? image[ey][sx - 1] : 0;
                    if (c_curr - c_up - c_left + c_ul == diff) {
                        ey++;
                    } else {
                        break;
                    }
                }

                // 3. 验证是否为有效正方形
                int width = ex - sx;
                int height = ey - sy;

                // 严格遵循 build 中的判断条件：
                // if (ex - sx < 9) continue;
                // if (ey - sy < 9) continue;
                // if ((ex - sx) == (ey - sy)) COUNT++;
                if (width >= 9 && height >= 9 && width == height) {
                    square_count++;
                }
            }
        }
    }

    return square_count;
}