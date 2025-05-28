def solve(v, c, cm, n, rooms, graph):
    if c > cm:
        return
    if v > n:
        cm = min(c, cm)
        return
    # 尝试将 v 分配到 i 号教室
    for i in range(1, c + 1):
        if 1 in [graph[i][k] for k in rooms[i]]:
            continue
        rooms[i].append(v)
        solve(v + 1, c, cm, n, rooms, graph)
        rooms[i].pop()
    rooms[c + 1].append(v)
    solve(v + 1, c + 1, cm, n, rooms, graph)
    rooms[c + 1].pop()


n = 10
cm = n
rooms = [[] for _ in range(n + 1)]
graph = [[0 for _ in range(n + 1)] for _ in range(n + 1)]
solve(1, 1, cm, n, rooms, graph)
