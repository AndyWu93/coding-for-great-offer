package class11;

import java.util.ArrayList;
import java.util.List;

/**
 * 给定一个字符串，如果可以在任意位置添加字符，最少添加几个能让字符串整体都是回文串。
 * 思路：该题为动态规划，范围尝试的模型
 * dp[i][j]:从i位置到j位置，补成回文需要几个字符
 * 对角线含义：0-0位置，1-1位置... 自然就是回文，所以都填0
 * 对角线往下为i>j没有意义，范围尝试i>=j
 * 第二条对角线含义：0-1位置，1-2位置... 判断str[i]==str[j]?0:1
 * 普遍位置：dp[l][r]
 * 1. l..r-1变成回文，r位置变成回文需要在最前方加上str[r],所以dp[l][r-1]+1
 * 2. l+1..r变成回文，l位置变成回文需要在最后方加上str[l],所以dp[l+1][r]+1
 * 3. l+1..r-1变成回文，条件：str[l]==str[r],所以dp[l+1][r-1]
 *	123取最小，dp[0][n-1]就是题解
 *
 * 总结
 * 范围尝试：一般需要考虑开头和结尾
 * 一个样本作行，一个样本作列：一般考虑结尾的各种情况
 *
 * 进阶：如果要将所有添加最少字符的回文串返回该怎么做？
 * 首先，将dp表生成，
 * 来到dp[0][n-1],看下这个结果是123哪个步骤得到的，
 * 如果是1，那就在结果串res开头加上str[n-1]，再去递归解决dp[0][n-2]
 * 如果是1，那就在结果串res结尾加上str[0]，再去递归解决dp[1][n-1]
 * 如果是3，那就在结果串res开头加上str[0],结尾加上str[n-1]，再去递归解决dp[1][n-2]
 * 直到来到dp[l][r] l==r
 *
 */
// 本题测试链接 : https://leetcode.com/problems/minimum-insertion-steps-to-make-a-string-palindrome/
public class Code01_MinimumInsertionStepsToMakeAStringPalindrome {

	// 测试链接只测了本题的第一问，直接提交可以通过
	public static int minInsertions(String s) {
		if (s == null || s.length() < 2) {
			return 0;
		}
		char[] str = s.toCharArray();
		int N = str.length;
		int[][] dp = new int[N][N];
		for (int i = 0; i < N - 1; i++) {
			dp[i][i + 1] = str[i] == str[i + 1] ? 0 : 1;
		}
		for (int i = N - 3; i >= 0; i--) {
			for (int j = i + 2; j < N; j++) {
				/*1，2种可能性先决策*/
				dp[i][j] = Math.min(dp[i][j - 1], dp[i + 1][j]) + 1;
				if (str[i] == str[j]) {
					dp[i][j] = Math.min(dp[i][j], dp[i + 1][j - 1]);
				}
			}
		}
		return dp[0][N - 1];
	}

	// 本题第二问，返回其中一种结果
	public static String minInsertionsOneWay(String s) {
		if (s == null || s.length() < 2) {
			return s;
		}
		char[] str = s.toCharArray();
		int N = str.length;
		int[][] dp = new int[N][N];
		for (int i = 0; i < N - 1; i++) {
			dp[i][i + 1] = str[i] == str[i + 1] ? 0 : 1;
		}
		for (int i = N - 3; i >= 0; i--) {
			for (int j = i + 2; j < N; j++) {
				dp[i][j] = Math.min(dp[i][j - 1], dp[i + 1][j]) + 1;
				if (str[i] == str[j]) {
					dp[i][j] = Math.min(dp[i][j], dp[i + 1][j - 1]);
				}
			}
		}
		/*以上是先生成dp，这里往下是生成一种结果*/
		int L = 0;
		int R = N - 1;
		/*收集结果的ans，长度需要加上dp[L][R]，因为dp[L][R]表示最少需要添加的字符*/
		char[] ans = new char[N + dp[L][R]];
		/*填写ans的两个指针*/
		int ansl = 0;
		int ansr = ans.length - 1;
		while (L < R) {
			/*来自可能性1*/
			if (dp[L][R - 1] == dp[L][R] - 1) {
				ans[ansl++] = str[R];
				ans[ansr--] = str[R--];
			} else if (dp[L + 1][R] == dp[L][R] - 1) {
				ans[ansl++] = str[L];
				ans[ansr--] = str[L++];
			} else {
				ans[ansl++] = str[L++];
				ans[ansr--] = str[R--];
			}
		}
		/*这里表示就剩1个字符，天然就是回文*/
		if (L == R) {
			ans[ansl] = str[L];
		}
		return String.valueOf(ans);
	}

	// 本题第三问，返回所有可能的结果
	public static List<String> minInsertionsAllWays(String s) {
		List<String> ans = new ArrayList<>();
		if (s == null || s.length() < 2) {
			ans.add(s);
		} else {
			char[] str = s.toCharArray();
			int N = str.length;
			int[][] dp = new int[N][N];
			for (int i = 0; i < N - 1; i++) {
				dp[i][i + 1] = str[i] == str[i + 1] ? 0 : 1;
			}
			for (int i = N - 3; i >= 0; i--) {
				for (int j = i + 2; j < N; j++) {
					dp[i][j] = Math.min(dp[i][j - 1], dp[i + 1][j]) + 1;
					if (str[i] == str[j]) {
						dp[i][j] = Math.min(dp[i][j], dp[i + 1][j - 1]);
					}
				}
			}
			int M = N + dp[0][N - 1];
			char[] path = new char[M];
			process(str, dp, 0, N - 1, path, 0, M - 1, ans);
		}
		return ans;
	}

	// 当前来到的动态规划中的格子，(L,R)
	// path ....  [pl....pr] ....
	public static void process(char[] str, int[][] dp, int L, int R, char[] path, int pl, int pr, List<String> ans) {
		if (L >= R) { // L > R  L==R
			if (L == R) {
				path[pl] = str[L];
			}
			ans.add(String.valueOf(path));
		} else {
			if (dp[L][R - 1] == dp[L][R] - 1) {
				path[pl] = str[R];
				path[pr] = str[R];
				process(str, dp, L, R - 1, path, pl + 1, pr - 1, ans);
			}
			/*深度优先遍历，上面一个案例制造完了以后为什么没有清理现场，因为后续可以将之前的值都覆盖了，不影响结果收集*/
			if (dp[L + 1][R] == dp[L][R] - 1) {
				path[pl] = str[L];
				path[pr] = str[L];
				process(str, dp, L + 1, R, path, pl + 1, pr - 1, ans);
			}
			/*
			* 来自可能性3的判断：
			* str[L] == str[R]:必须LR位置字符相等才有
			* L == R - 1：第二条对角线，结合上一个条件就是，LR是最后的两个相等的字符
			* dp[L + 1][R - 1] == dp[L][R]：LR位置自消化，里面的范围有没有加字符，所以来自可能3是有可能的
			 * */
			if (str[L] == str[R] && (L == R - 1 || dp[L + 1][R - 1] == dp[L][R])) {
				path[pl] = str[L];
				path[pr] = str[R];
				process(str, dp, L + 1, R - 1, path, pl + 1, pr - 1, ans);
			}
		}
	}

	public static void main(String[] args) {
		String s = null;
		String ans2 = null;
		List<String> ans3 = null;

		System.out.println("本题第二问，返回其中一种结果测试开始");
		s = "mbadm";
		ans2 = minInsertionsOneWay(s);
		System.out.println(ans2);

		s = "leetcode";
		ans2 = minInsertionsOneWay(s);
		System.out.println(ans2);

		s = "aabaa";
		ans2 = minInsertionsOneWay(s);
		System.out.println(ans2);
		System.out.println("本题第二问，返回其中一种结果测试结束");

		System.out.println();

		System.out.println("本题第三问，返回所有可能的结果测试开始");
		s = "mbadm";
		ans3 = minInsertionsAllWays(s);
		for (String way : ans3) {
			System.out.println(way);
		}
		System.out.println();

		s = "leetcode";
		ans3 = minInsertionsAllWays(s);
		for (String way : ans3) {
			System.out.println(way);
		}
		System.out.println();

		s = "aabaa";
		ans3 = minInsertionsAllWays(s);
		for (String way : ans3) {
			System.out.println(way);
		}
		System.out.println();
		System.out.println("本题第三问，返回所有可能的结果测试结束");
	}

}
