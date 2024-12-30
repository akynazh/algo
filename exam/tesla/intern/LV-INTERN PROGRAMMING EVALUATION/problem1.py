#!/usr/bin/env python3
import time


def solve_problem1(n: int):
    vis = [[0] * (n + 1) for _ in range(n + 1)]
    vis[0][0] = vis[1][0] = 1
    return dfs(1, 0, n - 1, vis, n)


def dfs(x, y, c, vis, n):
    if c == 0:
        return 1
    t = 0
    dx, dy = [1, 0, 0, -1], [0, 1, -1, 0]
    for i in range(4):
        nx, ny = x + dx[i], y + dy[i]
        if nx >= 0 and nx <= n and ny >= 0 and ny <= n and vis[nx][ny] != 1:
            vis[nx][ny] = 1
            t += dfs(nx, ny, c - 1, vis, n)
            vis[nx][ny] = 0
    return t


if __name__ == "__main__":
    for i in range(21):
        s = time.time()
        ans = solve_problem1(i + 5)
        e = time.time()
        print(f"{i + 5}: {ans} - {e - s}s")
