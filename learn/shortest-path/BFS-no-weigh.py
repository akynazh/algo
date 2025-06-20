from collections import deque

"""
每走一步的“代价”都是相同的（即每一条边权为 1）。

BFS 本质就是在无权图中求最短路径的最优解法。
"""


def shortest_path_in_maze(n, m, maze):
    # 找到起点
    for i in range(n):
        for j in range(m):
            if maze[i][j] == "S":
                start = (i, j)
            if maze[i][j] == "E":
                end = (i, j)

    # 方向向量：上下左右
    directions = [(-1, 0), (1, 0), (0, -1), (0, 1)]

    # 初始化队列和访问记录
    queue = deque()
    visited = [[False] * m for _ in range(n)]

    queue.append((start[0], start[1], 0))  # (x, y, steps)
    visited[start[0]][start[1]] = True

    while queue:
        x, y, steps = queue.popleft()

        # 找到终点
        if (x, y) == end:
            return steps

        for dx, dy in directions:
            nx, ny = x + dx, y + dy

            # 边界检查和访问检查
            if 0 <= nx < n and 0 <= ny < m:
                if not visited[nx][ny] and maze[nx][ny] != "#":
                    visited[nx][ny] = True
                    queue.append((nx, ny, steps + 1))

    return -1  # 无法到达


# 示例输入
if __name__ == "__main__":
    n, m = map(int, input().split())
    maze = [input().strip() for _ in range(n)]
    result = shortest_path_in_maze(n, m, maze)
    print(result)
