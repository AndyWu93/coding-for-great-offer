package class03;

import java.util.Arrays;

/**
 * Given a string s, find the length of the longest substring without repeating characters.
 *
 * 题意：求无重复字符的最长子串长度
 * 思路：窗口可以做，动态规划好理解
 * 	子串问题常规思路：必须以i位置结尾的最长无重复子串长度
 * 	枚举每个位置结尾，收集答案求max
 * 如何求出每个位置的答案呢？
 * 	假设以每个位置结尾的最长无重复子串长度保存在一张dp表中
 * 	dp[i]的值影响因素：
 * 		1. arr[i]上一次出现在什么位置？dp[i]的长度一定不能越过这个位置
 * 		2. dp[i-1]往前推了多少长度？dp[i]的长度一定不能越过这个位置
 * 	上面两个位置取离i近的位置，作为dp[i]的长度
 *
 * @see class09.Code03_LIS 最长递增子序列
 */
public class Code01_LongestSubstringWithoutRepeatingCharacters {

	/**
	 * 更简洁易懂
	 */
	public static int lengthOfLongestSubstring(String s) {
		if (s == null || s.equals("")) {
			return 0;
		}
		char[] str = s.toCharArray();
		int[] position = new int[256];
		Arrays.fill(position,-1);
		/*
		 * preCharFail:i-1位置结尾的情况下，往左推，推不动的位置是谁
		 * */
		int preCharFail = -1;
		int mathLength = 0;
		/*枚举每个位置结尾，求最长无重复子串长度*/
		for (int i = 0; i != str.length; i++) {
			/*上次str[i]出现的位置，和dp[i-1]往左推不动的位置，两个取大的，表示该位置离i更近*/
			preCharFail = Math.max(preCharFail, position[str[i]]);
			/*以i位置结尾的最长无重复子串长度*/
			mathLength = Math.max(mathLength,i - preCharFail);
			/*更新str[i]出现的位置*/
			position[str[i]] = i;
		}
		return mathLength;
	}




	public static int lengthOfLongestSubstring1(String s) {
		if (s == null || s.equals("")) {
			return 0;
		}
		char[] str = s.toCharArray();
		// map (a, ?) (b, ?)
		// a, 17
		// map[97] = 17
		/*记录字符上一次出现的位置，可以用map，这里用数组更快，长度128就够了*/
		int[] map = new int[256];
		for (int i = 0; i < 256; i++) {
			/*都设置成-1，表示这个字符上次出现的位置是-1*/
			map[i] = -1;
		}
		// 收集答案
		int len = 0;
		/*
		* 因为dp[i]只依赖dp[i-1],所以用一个变量就够了，不需要用一张dp表
		* pre:i-1位置结尾的情况下，往左推，推不动的位置是谁
		* */
		int pre = -1;
		int cur = 0;
		/*枚举每个位置结尾，求最长无重复子串长度*/
		for (int i = 0; i != str.length; i++) {
			// i位置结尾的情况下，往左推，推不动的位置是谁
			// pre (i-1信息) -> pre(i 结尾信息)
			/*上次str[i]出现的位置，和dp[i-1]往左推不动的位置，两个取大的，表示该位置离i更近*/
			pre = Math.max(pre, map[str[i]]);
			/*以i位置结尾的最长无重复子串长度*/
			cur = i - pre;
			/*收集结果*/
			len = Math.max(len, cur);
			/*更新str[i]出现的位置*/
			map[str[i]] = i;
		}
		return len;
	}

}
