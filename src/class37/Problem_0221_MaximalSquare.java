package class37;

/**
 * 在一个由 '0' 和 '1' 组成的二维矩阵内，找到只包含 '1' 的最大正方形，并返回其面积。
 *
 * 解题：
 * 	本题动态规划
 * 	dp[i][j]:以matrix[i][j]作为右下角的最大正方形面积
 * 	所以自然而然想到dp[i][j]和邻近几个点之间的关系
 * 	本题通过画图观察能够看出：dp[i][j]=min(dp[左边],dp[上边],dp[左上角]) + 1
 *
 * @see class03.Code03_Largest1BorderedSquare 区别于这道题
 */
//Leetcode题目 : https://leetcode.com/problems/maximal-square/
public class Problem_0221_MaximalSquare {

	public static int maximalSquare(char[][] m) {
		if (m == null || m.length == 0 || m[0].length == 0) {
			return 0;
		}
		int N = m.length;
		int M = m[0].length;
		int[][] dp = new int[N + 1][M + 1];
		/*收集最大面积*/
		int max = 0;
		/*第一列，要求是正方形，所以如果有，那么这个最大正方形只能是自己*/
		for (int i = 0; i < N; i++) {
			if (m[i][0] == '1') {
				dp[i][0] = 1;
				max = 1;
			}
		}
		/*第一行，同上*/
		for (int j = 1; j < M; j++) {
			if (m[0][j] == '1') {
				dp[0][j] = 1;
				max = 1;
			}
		}
		/*普遍位置*/
		for (int i = 1; i < N; i++) {
			for (int j = 1; j < M; j++) {
				if (m[i][j] == '1') {
					dp[i][j] = Math.min(
							Math.min(dp[i - 1][j],
									dp[i][j - 1]), 
							dp[i - 1][j - 1]) 
							+ 1;
					max = Math.max(max, dp[i][j]);
				}
			}
		}
		return max * max;
	}

}
