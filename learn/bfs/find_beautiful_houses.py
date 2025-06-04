def solve(n, m, mp):
    """
    Determine the number of "beautiful houses" in an n x m grid.

    A "beautiful house" is a connected group of cells with value 1 that is
    completely surrounded by vacant plots (cells with value 0) on all sides:
    horizontally, vertically, and diagonally.

    Args:
        n (int): Number of rows in the grid
        m (int): Number of columns in the grid
        mp (list): 2D grid representing the residential area

    Returns:
        int: Number of beautiful houses
    """
    # Define directions for the 8 neighbors (horizontal, vertical, diagonal)
    directions = [(-1, -1), (-1, 0), (-1, 1), (0, -1), (0, 1), (1, -1), (1, 0), (1, 1)]

    # Create a visited matrix to keep track of cells we've already processed
    visited = [[False for _ in range(m)] for _ in range(n)]

    # Function to check if a cell is within the grid boundaries
    def is_valid(i, j):
        return 0 <= i < n and 0 <= j < m

    def test(i, j):
        stack = [(i, j)]
        house_cells = []

        # bfs to find all cells in the house
        while stack:
            row, col = stack.pop()
            if visited[row][col]:
                continue

            visited[row][col] = True
            house_cells.append((row, col))

            # Check all 4 adjacent cells (horizontally and vertically)
            for dr, dc in [(0, 1), (1, 0), (0, -1), (-1, 0)]:
                ni, nj = row + dr, col + dc
                if is_valid(ni, nj) and mp[ni][nj] == 1 and not visited[ni][nj]:
                    stack.append((ni, nj))

        # Check if the house is beautiful by checking all surrounding cells
        for row, col in house_cells:
            for dr, dc in directions:
                ni, nj = row + dr, col + dc
                if is_valid(ni, nj) and mp[ni][nj] == 1 and (ni, nj) not in house_cells:
                    return False

        return True

    # Count the number of beautiful houses
    beautiful_houses = 0
    for i in range(n):
        for j in range(m):
            if mp[i][j] == 1 and not visited[i][j]:
                if test(i, j):
                    beautiful_houses += 1

    return beautiful_houses


# The input reading code was already provided
t = [int(t) for t in input().split()]
n, m = t[0], t[1]
mp = []
for _ in range(n):
    mp.append([int(t) for t in input().split()])
print(solve(n, m, mp))
