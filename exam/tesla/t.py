# tesla
# vis

ans = 0
mp = {}


def dfs(lst, vis, res):
    if len(res) == len(lst):
        print(res)
        return
    for i in range(len(lst)):
        if vis[i] == 0:
            vis[i] = 1
            dfs(lst, vis, res + lst[i])
            vis[i] = 0


def solve(lst):
    vis = [0 for _ in range(len(lst))]
    dfs(lst, vis, "")


solve("tesla")
