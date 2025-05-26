from collections import deque, defaultdict

"""
拓扑排序是对有向无环图的一种线性排序，保证每条边的起点都排在终点前。
如果图中有环，拓扑排序就不成立。

Kahn 算法（入度为 0）

原理：

1. 把所有入度为 0 的节点加入队列（这些没有任何依赖）。
2. 每从队列里拿出一个节点：
    加入结果序列
    把它指向的所有节点的入度减 1
    如果某个节点入度变为 0，加入队列
3. 最终：
    如果成功处理了所有节点 → 有效拓扑排序 ✅
    如果还有节点剩下（入度非 0）→ 图中有环 ❌
"""


def has_cycle_kahn(graph):
    # 统计每个节点的入度
    in_degree = defaultdict(int)  # default value: 0
    for node in graph:
        for neighbor in graph[node]:
            in_degree[neighbor] += 1

    # 所有节点（包括可能没有出边的）都要在图中
    # |: 是集合的并集运算（union）
    # {v1, v2, v3} 是集合
    all_nodes = set(graph.keys()) | {
        v for neighbors in graph.values() for v in neighbors
    }
    for node in all_nodes:
        in_degree.setdefault(node, 0)

    # 入度为 0 的节点加入队列
    queue = deque([node for node in in_degree if in_degree[node] == 0])
    visited_count = 0

    while queue:
        current = queue.popleft()
        visited_count += 1
        for neighbor in graph.get(current, []):
            in_degree[neighbor] -= 1
            if in_degree[neighbor] == 0:
                queue.append(neighbor)

    # 如果处理过的节点数量少于图中总节点数，说明有环
    return visited_count != len(all_nodes)


# 有环：1 → 2 → 3 → 1
graph_with_cycle = {1: [2], 2: [3], 3: [1]}

# 无环：1 → 2 → 3
graph_without_cycle = {1: [2], 2: [3], 3: []}

print(has_cycle_kahn(graph_with_cycle))  # True
print(has_cycle_kahn(graph_without_cycle))  # False
