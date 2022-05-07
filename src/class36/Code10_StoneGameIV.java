package class36;

/**
 * 来自哈喽单车(Leetcode原题)
 * Alice 和 Bob 两个人轮流玩一个游戏，Alice 先手。
 * 一开始，有 n 个石子堆在一起。每个人轮流操作，正在操作的玩家可以从石子堆里拿走 任意 非零 平方数 个石子。
 * 如果石子堆里没有石子了，则无法操作的玩家输掉游戏。
 * 给你正整数 n ，且`已知两个人都采取最优策略。如果 Alice 会赢得比赛，那么返回 True ，否则返回 False 。
 *
 * 见 体系学习班class38.Code02_EatGrass 相似的解题思路
 */
// 本题是leetcode原题 : https://leetcode.com/problems/stone-game-iv/
public class Code10_StoneGameIV {

	/**
	 * 复杂度：1~n，几乎每个数都要在循环里尝试 根号下n次，
	 * 所以复杂度O(N*根号下N)
	 */
	// 当前的！先手，会不会赢
	// 打表，不能发现规律
	public static boolean winnerSquareGame1(int n) {
		if (n == 0) {
			return false;
		}
		// 当前的先手，会尝试所有的情况，1，4，9，16，25，36....
		for (int i = 1; i * i <= n; i++) {
			// 当前的先手，决定拿走 i * i 这个平方数
			// 它的对手会不会赢？ winnerSquareGame1(n - i * i)
			if (!winnerSquareGame1(n - i * i)) {
				return true;
			}
		}
		return false;
	}

	public static boolean winnerSquareGame2(int n) {
		int[] dp = new int[n + 1];
		dp[0] = -1;
		return process2(n, dp);
	}

	public static boolean process2(int n, int[] dp) {
		if (dp[n] != 0) {
			return dp[n] == 1 ? true : false;
		}
		boolean ans = false;
		for (int i = 1; i * i <= n; i++) {
			if (!process2(n - i * i, dp)) {
				ans = true;
				break;
			}
		}
		dp[n] = ans ? 1 : -1;
		return ans;
	}

	/**
	 *  由于winnerSquareGame1打表不能找打规律，所以用一维动态规划，也过了
	 */
	public static boolean winnerSquareGame3(int n) {
		boolean[] dp = new boolean[n + 1];
		for (int i = 1; i <= n; i++) {
			for (int j = 1; j * j <= i; j++) {
				if (!dp[i - j * j]) {
					dp[i] = true;
					break;
				}
			}
		}
		return dp[n];
	}
	
	public static void main(String[] args) {
		int n = 10000000;
		System.out.println(winnerSquareGame3(n));
	}

}
