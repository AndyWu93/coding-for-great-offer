package class34;

/**
 * Given a string s, find the first non-repeating character in it and return its index. If it does not exist, return -1.
 * 题意：返回s中第一个不重复的字符
 * 解题：
 * 对s中26个字母统计词频
 * 再次遍历s，看s[i]的词频，是1的话返回i
 */
//https://leetcode.com/problems/first-unique-character-in-a-string/
public class Problem_0387_FirstUniqueCharacterInString {

	public int firstUniqChar(String s) {
		char[] str = s.toCharArray();
		int N = str.length;
		int count[] = new int[26];
		for (int i = 0; i < N; i++) {
			count[str[i] - 'a']++;
		}
		for (int i = 0; i < N; i++) {
			if (count[str[i] - 'a'] == 1) {
				return i;
			}
		}
		return -1;
	}

}
