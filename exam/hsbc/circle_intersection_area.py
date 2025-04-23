# Sample code to read input and write output:

"""
NAME = input()                  # Read input from STDIN
print ('Hello %s' % NAME)       # Write output to STDOUT
"""

# Warning: Printing unwanted or ill-formatted
# data to output will cause the test cases to fail

# Write your code here
import math


def circle_intersection_area(x1, y1, r1, x2, y2, r2):
    """
    Calculate the intersection area of two circles with 6 decimal precision.

    Parameters:
    x1, y1: coordinates of the first circle's center
    r1: radius of the first circle
    x2, y2: coordinates of the second circle's center
    r2: radius of the second circle

    Returns:
    float: area of intersection rounded to 6 decimal places
    """
    # Calculate the distance between the centers
    d = math.sqrt((x2 - x1) ** 2 + (y2 - y1) ** 2)

    # If circles are completely separate or one is inside the other
    if d >= r1 + r2:  # No intersection
        return 0.000000
    if d <= abs(r1 - r2):  # One circle is inside the other
        # Return area of the smaller circle
        return round(math.pi * min(r1, r2) ** 2, 6)

    # Calculate intersection area using the formula
    r1_sq = r1**2
    r2_sq = r2**2

    # Calculate the angles
    angle1 = 2 * math.acos((r1_sq + d**2 - r2_sq) / (2 * r1 * d))
    angle2 = 2 * math.acos((r2_sq + d**2 - r1_sq) / (2 * r2 * d))

    # Calculate the intersection area
    area = 0.5 * r1_sq * (angle1 - math.sin(angle1)) + 0.5 * r2_sq * (
        angle2 - math.sin(angle2)
    )

    return round(area, 6)


if __name__ == "__main__":
    lst = []
    for _ in range(6):
        lst.append(int(input()))
    intersection_area = circle_intersection_area(
        lst[0], lst[1], lst[2], lst[3], lst[4], lst[5]
    )
    print(intersection_area)
