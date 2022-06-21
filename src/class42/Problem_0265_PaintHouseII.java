package class42;

/**
 * 给定一个二维数组cost[N][K],
 * N表示有0...N-1这么多房子，所有房子排成一条直线，i号房子和i-1号房子相邻、也和i+1号房子相邻
 * K表示有0...K-1这么多颜色，每个房子都必须选择一种颜色
 * cost[i][j]表示i号房子涂上j颜色的花费，并且要求相邻的房子不能是一种颜色
 * 返回所有房子都涂上颜色，最小的总花费
 *
 * 解题：
 * 	常规思路，动态规划
 * 	dp[i][j]: i号房子涂上j的颜色，需要的总体最小代价
 * 		求法：dp[i][j] = dp[i-1][所有位置，除了j]的最小值min + cost[i][j]
 * 	出现了枚举行为，取上一行，两边范围的最小值，取min，
 * 	用线段树可以快速add范围上的值，并维护范围内的最小值，省去的枚举行为
 *
 * 优化思路：
 * 	能否直接计算出第i行，整体最小值？
 * 	可以的
 * 	对于每一行，只需要维护最优和次优的所有信息，根据这两个信息就能推出下一行的最优和次优
 * 	这样推到n-1行，返回n-1行的最优
 * 这么多颜色，为什么只需要最优和次优的信息，次次优一定不行吗？
 * 	假设来到了i行，i行的最小值是红色，
 * 	i-1行只要拿到一个不是红色的最优就行了，i-1行的最优和次优解里一定有一个不是红色
 */
//leetcode题目 : https://leetcode.com/problems/paint-house-ii/
public class Problem_0265_PaintHouseII {

	// costs[i][k] i号房子用k颜色刷的花费
	// 要让0...N-1的房子相邻不同色
	// 返回最小花费
	public static int minCostII(int[][] costs) {
		int N = costs.length;
		if (N == 0) {
			return 0;
		}
		int K = costs[0].length;
		/*
		* 两组信息，最优，次优向后滚动
		* */
		// 之前取得的最小代价、取得最小代价时的颜色
		int preMin1 = 0;
		int preEnd1 = -1;
		// 之前取得的次小代价、取得次小代价时的颜色
		int preMin2 = 0;
		int preEnd2 = -1;
		for (int i = 0; i < N; i++) { // i房子
			int curMin1 = Integer.MAX_VALUE;
			int curEnd1 = -1;
			int curMin2 = Integer.MAX_VALUE;
			int curEnd2 = -1;
			/*
			* 如何计算出本行的最优和次优？
			*
			* */
			for (int j = 0; j < K; j++) { // j颜色！
				/*
				* 上一行的最优解和次优解和次优解都不是j，优先选择上一行的最优解，来计算出本行的最优和次优
				* */
				if (j != preEnd1) {
					/*
					* 上一行的最优不是j颜色
					* 那就将上一行的最优解拿出来，加上本行j颜色的cost，
					* 得到的值cur和本行之前的颜色得出来的最优和次优比较
					* */
					if (preMin1 + costs[i][j] < curMin1) {
						/*cur干过了本行目前得到最优，要先把最优赋值给次优，再把cur赋值给最优*/
						curMin2 = curMin1;
						curEnd2 = curEnd1;
						curMin1 = preMin1 + costs[i][j];
						curEnd1 = j;
					} else if (preMin1 + costs[i][j] < curMin2) {
						/*cur没干过最优，干过了次优，直接赋值给次优*/
						curMin2 = preMin1 + costs[i][j];
						curEnd2 = j;
					}
				} else if (j != preEnd2) {
					/*
					* 上一行的最优是j颜色,但是次优不是
					* 那就将上一行的次优解拿出来，加上本行j颜色的cost，
					* 得到的值cur和本行之前的颜色得出来的最优和次优比较
					* */
					if (preMin2 + costs[i][j] < curMin1) {
						curMin2 = curMin1;
						curEnd2 = curEnd1;
						curMin1 = preMin2 + costs[i][j];
						curEnd1 = j;
					} else if (preMin2 + costs[i][j] < curMin2) {
						curMin2 = preMin2 + costs[i][j];
						curEnd2 = j;
					}
				}
			}
			/*得到本行最优和次优后，后面将继续推出下一行的最优和次优*/
			preMin1 = curMin1;
			preEnd1 = curEnd1;
			preMin2 = curMin2;
			preEnd2 = curEnd2;
		}
		return preMin1;
	}

}
