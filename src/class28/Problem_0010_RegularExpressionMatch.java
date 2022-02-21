package class28;

/**
 * Given an input string s and a pattern p, implement regular expression matching with support for '.' and '*' where:
 *
 * '.' Matches any single character.​​​​
 * '*' Matches zero or more of the preceding element.
 * The matching should cover the entire input string (not partial).
 *
 * 题意：正则匹配，如果能匹配返回true，否则false
 * 		正则规则：
 * 			. 可以代表任何字符
 * 			* 不能单独存在，需要和前面一个字符x一起作为一个整体，这个整体代表0个或者多个x字符
 * 解题：
 * 	本题是动态规划，样本对应模型
 * 	暴力递归+缓存法能过。
 *
 */
// 测试链接 : https://leetcode.com/problems/regular-expression-matching/
public class Problem_0010_RegularExpressionMatch {

	/*
	* 校验字符
	* */
	public static boolean isValid(char[] s, char[] e) {
		/* s中不能有'.' or '*' */
		for (int i = 0; i < s.length; i++) {
			if (s[i] == '*' || s[i] == '.') {
				return false;
			}
		}
		/* 开头的e[0]不能是'*'，没有相邻的'*' */
		for (int i = 0; i < e.length; i++) {
			if (e[i] == '*' && (i == 0 || e[i - 1] == '*')) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 初始尝试版本，暴力递归
	 */
	public static boolean isMatch1(String str, String exp) {
		if (str == null || exp == null) {
			return false;
		}
		char[] s = str.toCharArray();
		char[] e = exp.toCharArray();
		return isValid(s, e) && process(s, e, 0, 0);
	}

	/*
	* str[si.....] 能不能被 exp[ei.....]配出来?
	* */
	public static boolean process(char[] s, char[] e, int si, int ei) {
		if (ei == e.length) {
			/*exp 没了 str如果还有，那肯定不行*/
			return si == s.length;
		}
		/*
		* exp[ei]还有字符,分两种情况讨论
		* 1. ei + 1位置的字符，不是*
		* 2. ei + 1位置的字符，是*
		* */
		if (ei + 1 == e.length || e[ei + 1] != '*') {
			/*
			* ei + 1 不是*
			* str[si] 必须和 exp[ei] 能配上！
			* si != s.length ：s不能结束
			* e[ei] == s[si] || e[ei] == '.'：能够匹配
			* process(s, e, si + 1, ei + 1)：且后面的位置也能匹配
			* */
			return si != s.length && (e[ei] == s[si] || e[ei] == '.') && process(s, e, si + 1, ei + 1);
		}
		/*
		* ei + 1位置的字符，是*
		* s如果结束，或者此时不匹配，那exp此时的字符可以和后面的*变成空，继续exp下面2个位置去匹配：process(s, e, si, ei + 2);
		* 否则
		* 	先把exp此时的字符和后面的*变成空，让后面的去匹配，能匹配的话直接返回true
		* 	后面的不能匹配的话，si右移一位，即让（x*）变成一个x，后面的继续匹配
		* 直到当前两个位置不匹配了，或者si到底了，那此时的si再和exp下面2个位置去匹配
		* */
		while (si != s.length && (e[ei] == s[si] || e[ei] == '.')) {
			/*注意每次进入是si的值都不一样*/
			if (process(s, e, si, ei + 2)) {
				/*只有要一种情况返回true，就可以返回了，后面的不用看了*/
				return true;
			}
			si++;
		}
		return process(s, e, si, ei + 2);
	}

	/**
	 * 改记忆化搜索已经能过了
	 * 改记忆化搜索+斜率优化
	 */
	public static boolean isMatch2(String str, String exp) {
		if (str == null || exp == null) {
			return false;
		}
		char[] s = str.toCharArray();
		char[] e = exp.toCharArray();
		if (!isValid(s, e)) {
			return false;
		}
		int[][] dp = new int[s.length + 1][e.length + 1];
		// dp[i][j] = 0, 没算过！
		// dp[i][j] = -1 算过，返回值是false
		// dp[i][j] = 1 算过，返回值是true
		return isValid(s, e) && process2(s, e, 0, 0, dp);
	}

	public static boolean process2(char[] s, char[] e, int si, int ei, int[][] dp) {
		if (dp[si][ei] != 0) {
			return dp[si][ei] == 1;
		}
		boolean ans = false;
		if (ei == e.length) {
			ans = si == s.length;
		} else {
			if (ei + 1 == e.length || e[ei + 1] != '*') {
				ans = si != s.length && (e[ei] == s[si] || e[ei] == '.') && process2(s, e, si + 1, ei + 1, dp);
			} else {
//				while (si != s.length && (e[ei] == s[si] || e[ei] == '.')) {
//					if (process2(s, e, si, ei + 2,dp)) {
//						ans =  true;
//						break;
//					}
//					si++;
//				}
//				ans = ans || process2(s, e, si, ei + 2,dp);

				/*
				* 上面的代码如何斜率优化？
				* 可以通过列举f函数来看，有没有可以复用的
				* */
				if (si == s.length) { // ei ei+1 *
					ans = process2(s, e, si, ei + 2, dp);
				} else { // si没结束
					if (s[si] != e[ei] && e[ei] != '.') {
						ans = process2(s, e, si, ei + 2, dp);
					} else { // s[si] 可以和 e[ei]配上
						/*
						* 省掉了while：
						* 如果此时能够匹配，那
						* 	si不动，ei去下面2个位置（x*变成了空）
						* 或者
						* 	si动一下，ei不变（x*变成了一个目标字符）
						* */
						ans = process2(s, e, si, ei + 2, dp) || process2(s, e, si + 1, ei, dp);
					}
				}
			}
		}
		dp[si][ei] = ans ? 1 : -1;
		return ans;
	}

	/**
	 * 动态规划版本 + 斜率优化
	 * 可以不看
	 */
	public static boolean isMatch3(String str, String pattern) {
		if (str == null || pattern == null) {
			return false;
		}
		char[] s = str.toCharArray();
		char[] p = pattern.toCharArray();
		if (!isValid(s, p)) {
			return false;
		}
		int N = s.length;
		int M = p.length;
		boolean[][] dp = new boolean[N + 1][M + 1];
		dp[N][M] = true;
		for (int j = M - 1; j >= 0; j--) {
			dp[N][j] = (j + 1 < M && p[j + 1] == '*') && dp[N][j + 2];
		}
		// dp[0..N-2][M-1]都等于false，只有dp[N-1][M-1]需要讨论
		if (N > 0 && M > 0) {
			dp[N - 1][M - 1] = (s[N - 1] == p[M - 1] || p[M - 1] == '.');
		}
		for (int i = N - 1; i >= 0; i--) {
			for (int j = M - 2; j >= 0; j--) {
				if (p[j + 1] != '*') {
					dp[i][j] = ((s[i] == p[j]) || (p[j] == '.')) && dp[i + 1][j + 1];
				} else {
					if ((s[i] == p[j] || p[j] == '.') && dp[i + 1][j]) {
						dp[i][j] = true;
					} else {
						dp[i][j] = dp[i][j + 2];
					}
				}
			}
		}
		return dp[0][0];
	}

}
