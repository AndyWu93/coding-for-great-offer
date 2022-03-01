package class29;

/**
 *Given two strings s and t of lengths m and n respectively, return the minimum window substring of s such that every character in t (including duplicates) is included in the window. If there is no such substring, return the empty string "".
 *
 * The testcases will be generated such that the answer is unique.
 *
 * A substring is a contiguous sequence of characters within the string.
 * 题意：给定两个字符串 s t，返回s中包含t的所有字符的最短子串，顺序无所谓
 * 解题：
 * 	本题可以用窗口法，因为s中的窗口越大，能够包含的t中的字符越多，存在单调性。所以窗口
 * 	具体方式：
 * 		给t中的每一个字符建立词频，建立词频总量all
 * 		根据窗口在s中的变化，更新词频和all，当all等于0时，调整窗口至最佳位置，收集长度
 */
public class Problem_0076_MinWindowLength {

	public static int minLength(String s1, String s2) {
		if (s1 == null || s2 == null || s1.length() < s2.length()) {
			return Integer.MAX_VALUE;
		}
		char[] str1 = s1.toCharArray();
		char[] str2 = s2.toCharArray();
		int[] map = new int[256]; // map[37] = 4 37 4次
		for (int i = 0; i != str2.length; i++) {
			map[str2[i]]++;
		}
		int all = str2.length;

		// [L,R-1] R
		// [L,R) -> [0,0)
		int L = 0;
		int R = 0;
		int minLen = Integer.MAX_VALUE;
		while (R != str1.length) {
			map[str1[R]]--;
			if (map[str1[R]] >= 0) {
				all--;
			}
			if (all == 0) { // 还完了
				while (map[str1[L]] < 0) {
					map[str1[L++]]++;
				}
				// [L..R]
				minLen = Math.min(minLen, R - L + 1);
				all++;
				map[str1[L++]]++;
			}
			R++;
		}
		return minLen == Integer.MAX_VALUE ? 0 : minLen;
	}

	// 测试链接 : https://leetcode.com/problems/minimum-window-substring/
	public static String minWindow(String s, String t) {
		if (s.length() < t.length()) {
			return "";
		}
		char[] str = s.toCharArray();
		char[] target = t.toCharArray();
		/*词频的建立，可以用int数组，下标是字符的ASCII*/
		int[] map = new int[256];
		for (char cha : target) {
			map[cha]++;
		}
		/*需要包含的t的字符总个数*/
		int all = target.length;
		/*窗口左边界*/
		int L = 0;
		/*窗口右边界的下一个位置*/
		int R = 0;
		int minLen = Integer.MAX_VALUE;
		/*保存最短子串的两个边界，用来返回结果*/
		int ansl = -1;
		int ansr = -1;
		while (R != str.length) {
			/*R位置的字符进窗口了*/
			map[str[R]]--;
			if (map[str[R]] >= 0) {
				/*表示R位置进窗口后，如果该位置字符词频成了负数，all不用变，否则all也要减*/
				all--;
			}
			if (all == 0) {
				/*已经包含了t的所有字符*/
				while (map[str[L]] < 0) {
					/*
					* 调整窗口左边，直到L位置的字符词频不是负数，此时是最佳位置
					* map[str[L++]]++：L位置的字符出窗口
					* */
					map[str[L++]]++;
				}
				/*结算窗口大小，收集最短子串，及其位置*/
				if (minLen > R - L + 1) {
					minLen = R - L + 1;
					ansl = L;
					ansr = R;
				}
				/*L位置字符的词频原来是0，L位置的字符出窗口，词频++了，所以all也要++*/
				map[str[L++]]++;
				all++;
			}
			/*窗口右侧移动*/
			R++;
		}
		return minLen == Integer.MAX_VALUE ? "" : s.substring(ansl, ansr + 1);
	}

}
