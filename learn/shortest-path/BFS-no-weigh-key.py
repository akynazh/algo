"""
🧩 题目名称：钥匙与门的迷宫

💡 题目描述：

你仍然被困在一个 N × M 的迷宫中，这一次：
        •	'S' 是起点，'E' 是终点；
        •	'.' 是可以通行的路径；
        •	'#' 是墙，不能通行；
        •	'a' - 'f' 是钥匙，共有 6 把；
        •	'A' - 'F' 是门，必须持有对应的小写字母钥匙才可通过。

你可以上下左右移动一步，问你最少走多少步可以从 'S' 到达 'E'。

⸻

🧾 输入格式：

第一行两个整数 N, M （1 ≤ N, M ≤ 100）

接下来的 N 行是迷宫地图。


⸻

📤 输出格式：

一个整数：最短步数，若无法到达，输出 -1。


⸻

🧪 输入样例：

5 6
S.#...
.a.#A.
..#.#.
..#.E.
......


⸻

📥 输出样例：

12


⸻

🧠 解题思路（使用 BFS）：

我们还是用 BFS，不过要加入“钥匙状态”作为状态的一部分。

状态定义：

每个状态包含：
        •	当前坐标 (x, y)
        •	当前持有钥匙的集合（可用一个 6 位二进制数表示）

举例：
        •	如果你拿到了 a 和 c，用二进制表示为 101（也就是整数 5）。

关键点：
        •	BFS 中同一个坐标 (x, y) 在不同钥匙状态下可能需要重新访问。
        •	所以 visited[x][y][key_mask] 要开三维记录状态。

⸻

为什么仍然是 BFS？——把“钥匙状态”也当成位置的一部分

在“普通迷宫”里，一个坐标只需要走一次即可，因为你再次回到同一个格子，步数一定更多。但在“门‑钥匙”迷宫里，同一个坐标在“钥匙不同”时意义不同：

位置	手上的钥匙	能否开门？	是否值得再来？
(3,4)	没有 a	不能通过 A 门	可能卡住
(3,4)	有 a	可以通过 A 门	路彻底打开

因此：

真正的状态 = (x, y, keys_mask)
        •	(x, y)：坐标
        •	keys_mask：6 位二进制，哪把钥匙拿到了就把对应位设 1

⸻

BFS 的核心四步（加粗是与普通 BFS 的差异）
        1.	状态入队：起点 (sx, sy, 0)，钥匙集合为空 (0b000000)。
        2.	每次 pop 队首 (x, y, keys)：
        •	如果当前格子是 E → 返回步数。
        3.	扩展四个方向：
        1.	走到 (nx, ny)，先判断是否越界/撞墙 #。
        2.	遇到钥匙 a‑f → 把对应位 OR 进 keys_mask。
        3.	遇到门 A‑F → 必须检查 keys_mask 里有没有对应位 1；没有就 continue。
        4.	三维 visited：visited[x][y][keys_mask]；
        •	只有当这个三元组没出现过才入队，防止死循环。

⸻

为什么三维而不是二维？
        •	如果只用二维 visited[x][y]，那么“没钥匙”时走到门口就会把该格子标记已访问；
        •	拿到钥匙后想回来，再次到这格子却被当作“访问过”，路线白白被封死。

⸻

形象小例子（4 × 4，钥匙 a，门 A）

S . . A
# # . #
. a . .
. # . E

步数	队列 front	说明
0	(0,0,000000)	起点
1	(0,1,000000) (1,0  被墙)	往右
2	(0,2,000000)	再往右
3	(0,3,000000)💥	遇到门 A，但 mask=0，没有钥匙 → 不能入队
3	(1,2,000000)	往下到 (1,2)
4	(2,1,000000)🔑	再下到 (2,1)，拾到钥匙 a → 新状态 (2,1,000001)
5	(2,2,000001)	右
6	(1,2,000001)	上
7	(0,3,000001)🚪	拿钥匙后再到门口，mask 有 a，可通过
8	(1,3,000001) 下
9	(2,3,000001)
10	(3,3,000001)🎯	到达终点 E

        •	关键：在步 7 时，同坐标 (0,3) 因为 keys_mask 不同被允许再次访问，最终找到最短 10 步。
        •	如果只二维 visited，步 3 已把 (0,3) 标记，步 7 永远不能进门 → 找不到答案。

⸻

与普通 BFS 代码相比只改了两处

# 1. visited 三维
visited = [[[False]*64 for _ in range(M)] for _ in range(N)]

# 2. 入队/判重时带上 new_keys
if not visited[nx][ny][new_keys]:
    visited[nx][ny][new_keys] = True
    queue.append((nx, ny, steps+1, new_keys))

其余模板完全一样。把“钥匙集合”附加到状态里，就能用 同一套 BFS 逻辑 求最短路径。希望这样层层拆解后，思路就清晰啦！

如果还有哪一步没理解到位，随时追问 🙂
"""

from collections import deque


def shortest_path_with_keys(n, m, maze):
    dirs = [(-1, 0), (1, 0), (0, -1), (0, 1)]
    visited = [[[False] * 64 for _ in range(m)] for _ in range(n)]

    # 寻找起点
    for i in range(n):
        for j in range(m):
            if maze[i][j] == "S":
                sx, sy = i, j

    queue = deque()
    queue.append((sx, sy, 0, 0))  # (x, y, steps, keys_mask)
    visited[sx][sy][0] = True

    while queue:
        x, y, steps, keys = queue.popleft()

        if maze[x][y] == "E":
            return steps

        for dx, dy in dirs:
            nx, ny = x + dx, y + dy
            if not (0 <= nx < n and 0 <= ny < m):
                continue

            cell = maze[nx][ny]
            new_keys = keys

            # 墙，跳过
            if cell == "#":
                continue

            # 是钥匙，就更新 key mask
            if "a" <= cell <= "f":
                new_keys |= 1 << (ord(cell) - ord("a"))

            # 是门，必须检查是否有对应钥匙
            if "A" <= cell <= "F":
                if not (keys & (1 << (ord(cell) - ord("A")))):
                    continue

            if not visited[nx][ny][new_keys]:
                visited[nx][ny][new_keys] = True
                queue.append((nx, ny, steps + 1, new_keys))

    return -1  # 无法到达


# 示例输入
if __name__ == "__main__":
    n, m = map(int, input().split())
    maze = [input().strip() for _ in range(n)]
    print(shortest_path_with_keys(n, m, maze))
