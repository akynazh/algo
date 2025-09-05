package pro.pro0808e;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.util.*;

class UserSolution {
    ArrayList<Integer>[] Map = new ArrayList[8];
    int[][] rmMap = new int[8][8];
    int[][] crMap = new int[8][8];

    void init(int N, int mJewels[][]) {
        for (int i = 0; i < 8; ++i) {
            Map[i] = new ArrayList<>();
            for (int j = 0; j < N; ++j)
                Map[i].add(mJewels[j][i]);
        }
    }

    int calculate() {
        int score = 0;
        rmMap = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 6; ) {
                int current = crMap[i][j], count = 1;
                while (j + count < 8 && crMap[i][j + count] == current) count++;
                if (count >= 3) {
                    score += convert(count);
                    for (int k = 0; k < count; k++) rmMap[i][j + k] = 1;
                }
                j += count;
            }
        }
        for (int j = 0; j < 8; j++) {
            for (int i = 0; i < 6; ) {
                int current = crMap[i][j], count = 1;
                while (i + count < 8 && crMap[i + count][j] == current) count++;
                if (count >= 3) {
                    score += convert(count);
                    for (int k = 0; k < count; k++) rmMap[i + k][j] = 1;
                }
                i += count;
            }
        }
        return score;
    }

    private int convert(int c) {
        return (c == 3) ? 1 : (c == 4) ? 4 : 9;
    }

    void findTheBest(int ret[]) {
        int maxScore = -1, ni, nj;
        int[] di = {1, 0}, dj = {0, 1};

        getMap();
        for (int j = 0; j < 8; j++) {
            for (int i = 0; i < 8; i++) {
                for (int k = 0; k < 2; k++) {
                    ni = i + di[k];
                    nj = j + dj[k];
                    if (ni < 8 && nj < 8 && crMap[i][j] != crMap[ni][nj]) {
                        int temp = crMap[i][j];
                        crMap[i][j] = crMap[ni][nj];
                        crMap[ni][nj] = temp;

                        int currentScore = calculate();
                        if (currentScore > maxScore) {
                            maxScore = currentScore;
                            ret[2] = i;
                            ret[1] = j;
                            ret[4] = ni;
                            ret[3] = nj;
                        }

                        crMap[ni][nj] = crMap[i][j];
                        crMap[i][j] = temp;
                    }
                }
            }
        }
        ret[0] = maxScore;
    }

    private void removeMap(Boolean isClearAll) {
        for (int i = 0; i < 8; i++) {
            int hold = 0;
            for (int j = 0; j < 8; j++) {
                if (rmMap[i][j] == 1 || isClearAll) {
                    Map[i].remove(j - hold++);
                }
            }
        }
    }

    private void getMap() {
        for (int i = 0; i < 8; ++i) {
            for (int j = 7; j >= 0; --j) {
                crMap[i][j] = Map[i].get(j);
            }
        }
    }

    int[] takeTurn() {
        int[] ret = new int[5];
        int totalScore = 0, score;
        do {
            getMap();
            if (calculate() == 0) {
                findTheBest(ret);
                if (ret[0] == 0)
                    removeMap(true);
            } else
                removeMap(false);
        } while (ret[0] == 0);

        int temp = Map[ret[2]].get(ret[1]);
        Map[ret[2]].set(ret[1], Map[ret[4]].get(ret[3]));
        Map[ret[4]].set(ret[3], temp);

        do {
            getMap();
            score = calculate();
            totalScore += score;
            removeMap(false);
        } while (score != 0);

        ret[0] = totalScore;
        return ret;
    }
}

public class Solution {
    private static BufferedReader br;
    private static UserSolution userSolution = new UserSolution();

    private final static int WIDTH = 8;

    private final static int CMD_INIT = 100;
    private final static int CMD_TAKETURN = 200;

    private static int jewels[][] = new int[10000][WIDTH];

    private static boolean run() throws Exception {
        StringTokenizer stdin = new StringTokenizer(br.readLine(), " ");

        boolean okay = false;
        int Q = Integer.parseInt(stdin.nextToken());

        for (int q = 0; q < Q; ++q) {
            stdin = new StringTokenizer(br.readLine(), " ");
            int cmd = Integer.parseInt(stdin.nextToken());
            switch (cmd) {
                case CMD_INIT:
                    int N = Integer.parseInt(stdin.nextToken());
                    for (int y = 0; y < N; y++) {
                        stdin = new StringTokenizer(br.readLine(), " ");
                        for (int x = 0; x < WIDTH; x++) {
                            jewels[y][x] = Integer.parseInt(stdin.nextToken());
                        }
                    }
                    userSolution.init(N, jewels);
                    okay = true;
                    break;
                case CMD_TAKETURN:
                    int[] user_ans = userSolution.takeTurn();
                    for (int i = 0; i < 5; i++) {
                        int correct_ans = Integer.parseInt(stdin.nextToken());
                        if (user_ans[i] != correct_ans) {
                            okay = false;
                        }
                    }
                    break;
                default:
                    okay = false;
                    break;
            }
        }

        return okay;
    }

    public static void main(String[] args) throws Exception {
        int T, MARK;

        System.setIn(new java.io.FileInputStream("acm/pro0808e/sample_input.txt"));
        br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer stinit = new StringTokenizer(br.readLine(), " ");
        T = Integer.parseInt(stinit.nextToken());
        MARK = Integer.parseInt(stinit.nextToken());

        long s = System.currentTimeMillis();
        for (int tc = 1; tc <= T; tc++) {
            int score = run() ? MARK : 0;
            System.out.println("#" + tc + " " + score);
        }
        System.out.println(System.currentTimeMillis() - s);

        br.close();
    }
}
