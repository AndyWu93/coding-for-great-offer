package class31;

import java.util.ArrayList;
import java.util.List;

/**
 * Given a string s, partition s such that every substring of the partition is a palindrome.
 *
 * Return the minimum cuts needed for a palindrome partitioning of s.
 * 题意：给定字符串str，问最少切几刀能都变成回文
 * 解题：
 * 	动态规划，从左往右的尝试模型
 * 	尝试过程是枚举str[i]开始，一直到某个位置，这一部分是回文，分成1个部分，剩下的去递归得到x个部分。收集最小值
 * 	根据尝试，得到dp[i]依赖i后面的位置，求max，所以从后往前推
 * 	其中，关于求str[i..j]位置是不是回文，可以通过另一张dp表求得
 *
 * 	另一张dp表：dp[i][j]:str[i..j]是不是回文，是范围尝试的模型
 *
 */
// 本题测试链接 : https://leetcode.com/problems/palindrome-partitioning-ii/
public class Problem_0131_PalindromePartitioningII {

	// 测试链接只测了本题的第一问，直接提交可以通过
	public static int minCut(String s) {
		if (s == null || s.length() < 2) {
			return 0;
		}
		char[] str = s.toCharArray();
		int N = str.length;
		/*生成dp表：dp[i][j]:str[i..j]是否回文*/
		boolean[][] checkMap = createCheckMap(str, N);
		/*dp[i]:从str[i]开始往后，可以分成几个部分的回文*/
		int[] dp = new int[N + 1];
		/*从N开始往后没有字符，0个部分的回文*/
		dp[N] = 0;
		/*
		* 尝试过程是枚举str[i]开始，一直到某个位置，这一部分是回文，分成1个部分，剩下的去递归得到x个部分。收集最小值
		* 根据尝试，得到dp[i]依赖i后面的位置，求max，所以从后往前推
		* 其中，关于求str[i..j]位置是不是回文，可以通过另一张dp表求得
		* */
		for (int i = N - 1; i >= 0; i--) {
			if (checkMap[i][N - 1]) {
				/*如果这一部分整体是回文，就分成1个部分*/
				dp[i] = 1;
			} else {
				int next = Integer.MAX_VALUE;
				/*从i往后到j位置，枚举是回文的情况，j往后的str可以分成几个回文直接从表里拿*/
				for (int j = i; j < N; j++) {
					if (checkMap[i][j]) {
						next = Math.min(next, dp[j + 1]);
					}
				}
				/*1就是i到j位置作为1个部分*/
				dp[i] = 1 + next;
			}
		}
		/*返回切了几刀，是回文部分数-1*/
		return dp[0] - 1;
	}

	public static boolean[][] createCheckMap(char[] str, int N) {
		boolean[][] ans = new boolean[N][N];
		for (int i = 0; i < N - 1; i++) {
			/*对角线，以及只有两个字符时的情况一起填写*/
			ans[i][i] = true;
			ans[i][i + 1] = str[i] == str[i + 1];
		}
		ans[N - 1][N - 1] = true;

		for (int i = N - 3; i >= 0; i--) {
			for (int j = i + 2; j < N; j++) {
				/*i位置和j位置相等，且其他位置也是回文*/
				ans[i][j] = str[i] == str[j] && ans[i + 1][j - 1];
			}
		}
		return ans;
	}

	// 本题第二问，返回其中一种结果
	public static List<String> minCutOneWay(String s) {
		List<String> ans = new ArrayList<>();
		if (s == null || s.length() < 2) {
			ans.add(s);
		} else {
			char[] str = s.toCharArray();
			int N = str.length;
			boolean[][] checkMap = createCheckMap(str, N);
			int[] dp = new int[N + 1];
			dp[N] = 0;
			for (int i = N - 1; i >= 0; i--) {
				if (checkMap[i][N - 1]) {
					dp[i] = 1;
				} else {
					int next = Integer.MAX_VALUE;
					for (int j = i; j < N; j++) {
						if (checkMap[i][j]) {
							next = Math.min(next, dp[j + 1]);
						}
					}
					dp[i] = 1 + next;
				}
			}
			/*以上都是和上一个问题代码一样*/
			// dp[i]  (0....5) 回文！  dp[0] == dp[6] + 1
			//  (0....5)   6
			/*
			* 如何回溯？
			* 通过i，j指针枚举出回文，
			* 如果i到j是回文，且j往后的回文部分数+1正好是i往后的回文部分数
			* 表明i到j这里切了1刀，作为了1个部分，先收集到答案里去，再去j往后收集
			* */
			for (int i = 0, j = 1; j <= N; j++) {
				/*两个条件同时成立*/
				if (checkMap[i][j - 1] && dp[i] == dp[j] + 1) {
					ans.add(s.substring(i, j));
					/*i来到j的位置，继续收集后面的*/
					i = j;
				}
			}
		}
		return ans;
	}

	// 本题第三问，返回所有结果
	public static List<List<String>> minCutAllWays(String s) {
		List<List<String>> ans = new ArrayList<>();
		if (s == null || s.length() < 2) {
			List<String> cur = new ArrayList<>();
			cur.add(s);
			ans.add(cur);
		} else {
			char[] str = s.toCharArray();
			int N = str.length;
			boolean[][] checkMap = createCheckMap(str, N);
			int[] dp = new int[N + 1];
			dp[N] = 0;
			for (int i = N - 1; i >= 0; i--) {
				if (checkMap[i][N - 1]) {
					dp[i] = 1;
				} else {
					int next = Integer.MAX_VALUE;
					for (int j = i; j < N; j++) {
						if (checkMap[i][j]) {
							next = Math.min(next, dp[j + 1]);
						}
					}
					dp[i] = 1 + next;
				}
			}
			/*收集所有的就用深度优先遍历*/
			process(s, 0, 1, checkMap, dp, new ArrayList<>(), ans);
		}
		return ans;
	}

	// s[0....i-1]  存到path里去了
	// s[i..j-1]考察的分出来的第一份
	public static void process(String s, int i, int j, boolean[][] checkMap, int[] dp, 
			List<String> path,
			List<List<String>> ans) {
		if (j == s.length()) { // s[i...N-1]
			if (checkMap[i][j - 1] && dp[i] == dp[j] + 1) {
				path.add(s.substring(i, j));
				ans.add(copyStringList(path));
				/*这里要还原现场*/
				path.remove(path.size() - 1);
			}
		} else {// s[i...j-1]
			if (checkMap[i][j - 1] && dp[i] == dp[j] + 1) {
				path.add(s.substring(i, j));
				process(s, j, j + 1, checkMap, dp, path, ans);
				path.remove(path.size() - 1);
			}
			/*i到j+1位置成为一整体的部分继续深度优先遍历讨论*/
			process(s, i, j + 1, checkMap, dp, path, ans);
		}
	}

	public static List<String> copyStringList(List<String> list) {
		List<String> ans = new ArrayList<>();
		for (String str : list) {
			ans.add(str);
		}
		return ans;
	}

	public static void main(String[] args) {
		String s = null;
		List<String> ans2 = null;
		List<List<String>> ans3 = null;

		System.out.println("本题第二问，返回其中一种结果测试开始");
		s = "abacbc";
		ans2 = minCutOneWay(s);
		for (String str : ans2) {
			System.out.print(str + " ");
		}
		System.out.println();

		s = "aabccbac";
		ans2 = minCutOneWay(s);
		for (String str : ans2) {
			System.out.print(str + " ");
		}
		System.out.println();

		s = "aabaa";
		ans2 = minCutOneWay(s);
		for (String str : ans2) {
			System.out.print(str + " ");
		}
		System.out.println();
		System.out.println("本题第二问，返回其中一种结果测试结束");
		System.out.println();
		System.out.println("本题第三问，返回所有可能结果测试开始");
		s = "cbbbcbc";
		ans3 = minCutAllWays(s);
		for (List<String> way : ans3) {
			for (String str : way) {
				System.out.print(str + " ");
			}
			System.out.println();
		}
		System.out.println();

		s = "aaaaaa";
		ans3 = minCutAllWays(s);
		for (List<String> way : ans3) {
			for (String str : way) {
				System.out.print(str + " ");
			}
			System.out.println();
		}
		System.out.println();

		s = "fcfffcffcc";
		ans3 = minCutAllWays(s);
		for (List<String> way : ans3) {
			for (String str : way) {
				System.out.print(str + " ");
			}
			System.out.println();
		}
		System.out.println();
		System.out.println("本题第三问，返回所有可能结果测试结束");
	}

}
