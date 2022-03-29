package class34;

/**
 * 给定一个二维数组matrix，
 * 你可以从任何位置出发，走向上下左右四个方向
 * 返回能走出来的最长的递增链长度
 *
 * 思路：
 * 	本题是动态规划，使用记忆化搜索
 * 流程：
 * 	枚举matrix中的每一个位置为起始位置，计算它的后续最长递增链，收集取最大值
 * 	如何计算一个位置的后续最长递增链？上下左右尝试
 */
//https://leetcode.com/problems/longest-increasing-path-in-a-matrix/
public class Problem_0329_LongestIncreasingPath {

	public static int longestIncreasingPath1(int[][] matrix) {
		int ans = 0;
		int N = matrix.length;
		int M = matrix[0].length;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				ans = Math.max(ans, process1(matrix, i, j));
			}
		}
		return ans;
	}

	 /*
	 * 从m[i][j]开始走，走出来的最长递增链，返回！
	 * 该方法有两个可变参数i,j，一定存在重复值的调用，
	 * 如果改成严格位置的依赖会比较麻烦，因为不知道依赖关系。所以使用记忆化搜索解题会比较快
	 * */
	public static int process1(int[][] m, int i, int j) {
		int up = i > 0 && m[i][j] < m[i - 1][j] ? process1(m, i - 1, j) : 0;
		int down = i < (m.length - 1) && m[i][j] < m[i + 1][j] ? process1(m, i + 1, j) : 0;
		int left = j > 0 && m[i][j] < m[i][j - 1] ? process1(m, i, j - 1) : 0;
		int right = j < (m[0].length - 1) && m[i][j] < m[i][j + 1] ? process1(m, i, j + 1) : 0;
		return Math.max(Math.max(up, down), Math.max(left, right)) + 1;
	}

	public static int longestIncreasingPath2(int[][] matrix) {
		int ans = 0;
		int N = matrix.length;
		int M = matrix[0].length;
		int[][] dp = new int[N][M];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				ans = Math.max(ans, process2(matrix, i, j, dp));
			}
		}
		return ans;
	}

	// 从m[i][j]开始走，走出来的最长递增链，返回！
	public static int process2(int[][] m, int i, int j, int[][] dp) {
		if (dp[i][j] != 0) {
			return dp[i][j];
		}
		// (i,j)不越界
		int up = i > 0 && m[i][j] < m[i - 1][j] ? process2(m, i - 1, j, dp) : 0;
		int down = i < (m.length - 1) && m[i][j] < m[i + 1][j] ? process2(m, i + 1, j, dp) : 0;
		int left = j > 0 && m[i][j] < m[i][j - 1] ? process2(m, i, j - 1, dp) : 0;
		int right = j < (m[0].length - 1) && m[i][j] < m[i][j + 1] ? process2(m, i, j + 1, dp) : 0;
		int ans = Math.max(Math.max(up, down), Math.max(left, right)) + 1;
		dp[i][j] = ans;
		return ans;
	}

}
