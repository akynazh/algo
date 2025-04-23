package wind;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 
 * 请实现如下函数： bool isAlwaysWin(int N, int Target) ，判断给定 N 和 Target
 * 的情况下，先报数的人是否一定取胜，True 表示一定取胜，False 表示不是。
 */
public class CountingGame {
    public static boolean isAlwaysWin(int N, int Target) {
        if ((N * (N + 1)) / 2 < Target) {
            return false; // 如果所有数加起来都达不到 Target，先手无论如何都无法获胜
        }
        return canWin(N, Target, 0, new boolean[N + 1], new HashMap<>());
    }

    private static boolean canWin(int N, int Target, int currentSum, boolean[] used, Map<Integer, Boolean> memo) {
        int state = getState(used);
        if (memo.containsKey(state)) {
            return memo.get(state);
        }

        for (int i = 1; i <= N; i++) {
            if (!used[i]) {
                used[i] = true;
                // 轮到我报数，获胜的情况就是只有我报出的数达标或者下一个人报出的数不达标（因为最终必有一人达标）
                if (currentSum + i >= Target || !canWin(N, Target, currentSum + i, used, memo)) {
                    memo.put(state, true);
                    used[i] = false;
                    return true;
                }
                used[i] = false;
            }
        }
        memo.put(state, false);
        return false;
    }

    private static int getState(boolean[] used) {
        int state = 0;
        for (boolean b : used) {
            state = (state << 1) | (b ? 1 : 0);
        }
        return state;
    }

    public static void main(String[] args) {
//        System.out.println(isAlwaysWin(5, 6));   // false
//        System.out.println(isAlwaysWin(5, 8));   // true
        System.out.println(isAlwaysWin(20, 100)); // 计算 N=20, Target=100 的结果
//        Scanner sc = new Scanner(System.in);
//        int N = sc.nextInt();
//        int Target = sc.nextInt();
//        System.out.println(isAlwaysWin(N, Target));
    }
}