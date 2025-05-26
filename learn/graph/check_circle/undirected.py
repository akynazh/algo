"""
DFS + 记录 parent

判断无向图中是否存在环，常用方法是深度优先搜索（DFS），利用递归遍历图的节点，并检测回边。
无向图中的环可以通过访问已访问过且不是当前节点父节点的邻居节点来判断。
"""


def dfs(node, parent, graph, visited):
    visited.add(node)
    for neighbor in graph[node]:
        if neighbor in visited:
            if neighbor != parent:
                return True
        else:
            if dfs(neighbor, node, graph, visited):
                return True
        # if neighbor not in visited:
        #     if dfs(neighbor, node, graph, visited):
        #         return True
        # elif neighbor != parent:
        #     return True
    return False


def has_cycle(graph):
    visited = set()
    for node in graph:
        if node not in visited:
            if dfs(node, None, graph, visited):
                return True
    return False


# 测试
graph_with_cycle = {1: [2, 3], 2: [1, 4], 3: [1, 4], 4: [2, 3]}

graph_without_cycle = {1: [2], 2: [1, 3], 3: [2]}

print(has_cycle(graph_with_cycle))  # True
print(has_cycle(graph_without_cycle))  # False
