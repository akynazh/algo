"""
✅ 有向图判断是否有环：使用 DFS + 状态标记法

我们给每个节点设置三种状态：

0: 未访问（unvisited）
1: 正在访问（visiting，处于递归栈中）
2: 已访问完成（visited，递归完成）

🚨 原理关键：

当 DFS 过程中我们发现一个邻居节点是 “正在访问”（状态是 1），
说明我们从某个节点出发又回到了当前递归路径中的某个祖先节点 —— 这就形成了一个环。
"""


def dfs_directed(node, graph, state):
    state[node] = 1  # 进入中间状态：visiting
    for neighbor in graph.get(node, []):
        if state[neighbor] == 0:
            if dfs_directed(neighbor, graph, state):
                return True
        elif state[neighbor] == 1:
            # 如果遇到“正在访问”的节点，说明有环
            return True
    state[node] = 2  # 退出，标记为 visited
    return False


def has_cycle_directed(graph):
    state = {node: 0 for node in graph}  # 初始所有节点都是 unvisited
    for node in graph:
        if state[node] == 0:
            if dfs_directed(node, graph, state):
                return True
    return False


# 有环：1 → 2 → 3 → 1
graph_with_cycle = {1: [2], 2: [3], 3: [1]}

# 无环：1 → 2 → 3
graph_without_cycle = {1: [2], 2: [3], 3: []}

print(has_cycle_directed(graph_with_cycle))  # True
print(has_cycle_directed(graph_without_cycle))  # False
