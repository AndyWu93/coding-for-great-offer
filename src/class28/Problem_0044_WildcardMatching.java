package class28;

/**
 * 给定一个字符串 (s) 和一个字符模式 (p) ，实现一个支持 '?' 和 '*' 的通配符匹配。
 *
 * '?' 可以匹配任何单个字符。
 * '*' 可以匹配任意字符串（包括空字符串）。
 * 两个字符串完全匹配才算匹配成功。
 *
 * 说明:
 *
 * s 可能为空，且只包含从 a-z 的小写字母。
 * p 可能为空，且只包含从 a-z 的小写字母，以及字符 ? 和 *。
 *
 * 解题：
 * 	本题和第10题的区别，* 不需要依赖前面一个字符了
 * 	解题过程仍然可以使用第10题的方式，
 * 	设计函数f(i,p),p位置往后能不能将i位置往后的str配出来
 * 	函数中仍然是按p位置是不是 * 来分类讨论
 * @see Problem_0010_RegularExpressionMatch
 * 链接：https://leetcode.com/problems/wildcard-matching
 */
public class Problem_0044_WildcardMatching {

	public static boolean isMatch4(String str, String pattern) {
		char[] s = str.toCharArray();
		char[] p = pattern.toCharArray();
		return process(s, p, 0, 0);
	}

	private static boolean process(char[] str, char[] pattern, int si, int pi) {
		if (pi==pattern.length){
			return si==str.length;
		}

		if (pattern[pi]=='*'){
			while (si!=str.length){
				if (process(str, pattern, si, pi+1)){
					return true;
				}
				si++;
			}
			return process(str, pattern, si, pi+1);
		}else {
			return si!=str.length && (str[si]==pattern[pi] || pattern[pi]=='?') && process(str, pattern, si+1, pi+1);
		}
	}


	public static boolean isMatch1(String str, String pattern) {
		char[] s = str.toCharArray();
		char[] p = pattern.toCharArray();
		return process1(s, p, 0, 0);
	}

	// s[si....] 能否被 p[pi....] 匹配出来
	public static boolean process1(char[] s, char[] p, int si, int pi) {
		if (si == s.length) { // s -> ""
			if (pi == p.length) { // p -> ""
				return true;
			} else {
				// p -> "..."
				// p[pi] == '*' && p[pi+1...] -> "
				return p[pi] == '*' && process1(s, p, si, pi + 1);
			}
		}
		if (pi == p.length) { // p -> "" s
			return si == s.length;
		}
		// s从si出发.... p从pi出发...
		// s[si] -> 小写字母
		// p[pi] -> 小写、?、*
		if (p[pi] != '?' && p[pi] != '*') {
			return s[si] == p[pi] && process1(s, p, si + 1, pi + 1);
		}
		// si.. pi.. pi ? *
		if (p[pi] == '?') {
			return process1(s, p, si + 1, pi + 1);
		}
		for (int len = 0; len <= s.length - si; len++) {
			if (process1(s, p, si + len, pi + 1)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isMatch2(String str, String pattern) {
		char[] s = str.toCharArray();
		char[] p = pattern.toCharArray();
		int N = s.length;
		int M = p.length;
		boolean[][] dp = new boolean[N + 1][M + 1];
		dp[N][M] = true;
		for (int pi = M - 1; pi >= 0; pi--) {
			dp[N][pi] = p[pi] == '*' && dp[N][pi + 1];
		}
		for (int si = N - 1; si >= 0; si--) {
			for (int pi = M - 1; pi >= 0; pi--) {
				if (p[pi] != '?' && p[pi] != '*') {
					dp[si][pi] = s[si] == p[pi] && dp[si + 1][pi + 1];
					continue;
				}
				if (p[pi] == '?') {
					dp[si][pi] = dp[si + 1][pi + 1];
					continue;
				}
				// p[pi] == '*'
				dp[si][pi] = dp[si][pi + 1] || dp[si + 1][pi];
			}
		}
		return dp[0][0];
	}

	// 最终做的化简
	public static boolean isMatch3(String str, String pattern) {
		char[] s = str.toCharArray();
		char[] p = pattern.toCharArray();
		int N = s.length;
		int M = p.length;
		boolean[][] dp = new boolean[N + 1][M + 1];
		dp[N][M] = true;
		for (int pi = M - 1; pi >= 0; pi--) {
			dp[N][pi] = p[pi] == '*' && dp[N][pi + 1];
		}
		for (int si = N - 1; si >= 0; si--) {
			for (int pi = M - 1; pi >= 0; pi--) {
				if (p[pi] != '*') {
					dp[si][pi] = (p[pi] == '?' || s[si] == p[pi]) && dp[si + 1][pi + 1];
				} else {
					dp[si][pi] = dp[si][pi + 1] || dp[si + 1][pi];
				}
			}
		}
		return dp[0][0];
	}

}
