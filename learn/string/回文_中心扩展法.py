def expand_center(s, left, right):
    while left >= 0 and right < len(s) and s[left] == s[right]:
        left -= 1
        right += 1
    # 返回回文子串
    return s[left + 1 : right]


def find_two_unique_palindromes(s):
    palindromes = set()

    for i in range(len(s)):
        # 奇数长度回文（中心是一个字符）
        palindrome1 = expand_center(s, i, i)
        if palindrome1 and palindrome1 not in palindromes:
            palindromes.add(palindrome1)

        # 偶数长度回文（中心是两个相邻字符之间）
        palindrome2 = expand_center(s, i, i + 1)
        if palindrome2 and palindrome2 not in palindromes:
            palindromes.add(palindrome2)

    # 按长度排序，取出最长的两个不同回文子串
    unique_palindromes = sorted(palindromes, key=len, reverse=True)[:2]

    return unique_palindromes


# 测试
s = "babadab"
result = find_two_unique_palindromes(s)
print("找到的两个不重复回文子串：", result)
