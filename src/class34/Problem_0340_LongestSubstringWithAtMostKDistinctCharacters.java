package class34;

/**
 * 给定一个字符串str，和一个正数k，返回字符种类不超过k种的最长子串长度。
 * 解题：
 * 	本题可以使用窗口，为什么？
 * 	因为str子串的长度和k存在单调性
 * 		思路：
 * 		可以用一个map统计窗口中的词频，map.size成为窗口滑动的依据
 */
//Leetcode题目 : https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/
public class Problem_0340_LongestSubstringWithAtMostKDistinctCharacters {

	public static int lengthOfLongestSubstringKDistinct(String s, int k) {
		if (s == null || s.length() == 0 || k < 1) {
			return 0;
		}
		char[] str = s.toCharArray();
		int N = str.length;
		int[] count = new int[256];
		int diff = 0;
		int R = 0;
		int ans = 0;
		for (int i = 0; i < N; i++) {
			// R 窗口的右边界
			/*R右滑的条件：R位置进窗口后，map.size<=k,R就可以一直滑动*/
			while (R < N && (diff < k || (diff == k && count[str[R]] > 0))) {
				diff += count[str[R]] == 0 ? 1 : 0;
				count[str[R++]]++;
			}
			// R 来到违规的第一个位置
			/*含义：以i位置开头的最长子串长度*/
			ans = Math.max(ans, R - i);
			/*i++:滑动窗口开头*/
			diff -= count[str[i]] == 1 ? 1 : 0;
			count[str[i]]--;
		}
		return ans;
	}

}
