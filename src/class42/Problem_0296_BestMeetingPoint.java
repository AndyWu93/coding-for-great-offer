package class42;

/**
 * 二维矩阵中只有0和1，每个1可以上、下、左、右的移动
 * 想让所有的1汇聚在一个点上开会，请返回所有1移动的最小距离和
 *
 * 解题：
 * 	思路：
 * 	针对所有的行，从顶部和底部往中间，使用排除法确定最优的行
 * 	针对所有的列，从左部和右部往中间，使用排除法确定最优的列
 * 	matrix[最优行][最优列] 就是题解
 * 如何用排除法？
 * 	首先可以证明出0行和n-1行，1多的移动代价大，应该移动1少的
 * 	移动的时候需要将经过行，所有的1累加，作为一个整体来做下次的决定
 *
 * @see class44.Problem_0317_ShortestDistanceFromAllBuildings 有障碍的情况
 */
//leetcode题目 : https://leetcode.com/problems/best-meeting-point/
public class Problem_0296_BestMeetingPoint {

	public static int minTotalDistance(int[][] grid) {
		int N = grid.length;
		int M = grid[0].length;
		/*
		* 统计每行所有的1
		* 统计每列所有的1
		* 这里的统计方法值得借鉴
		* */
		int[] iOnes = new int[N];
		int[] jOnes = new int[M];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				if (grid[i][j] == 1) {
					iOnes[i]++;
					jOnes[j]++;
				}
			}
		}
		/*
		* 顶部和底部往中间移动，择出最优，结算代价到total
		* */
		int total = 0;
		int i = 0;
		int j = N - 1;
		/*顶部移动代价累计*/
		int iRest = 0;
		/*底部移动代价累计*/
		int jRest = 0;
		while (i < j) {
			/*谁的1少，谁移动，1少移动代价低；注意这里比较的是当行+累计代价*/
			if (iOnes[i] + iRest <= iOnes[j] + jRest) {
				/*结算代价，注意需要结算的部分，不能算少了*/
				total += iOnes[i] + iRest;
				/*决胜行的代价加到累计中去，并往中间移动*/
				iRest += iOnes[i++];
			} else {
				total += iOnes[j] + jRest;
				jRest += iOnes[j--];
			}
		}
		/*
		* 左右两边列往中间移动，择出最优，结算代价到total
		* total是之前的变量累计，其他变量清零后复用
		* */
		i = 0;
		j = M - 1;
		iRest = 0;
		jRest = 0;
		while (i < j) {
			if (jOnes[i] + iRest <= jOnes[j] + jRest) {
				total += jOnes[i] + iRest;
				iRest += jOnes[i++];
			} else {
				total += jOnes[j] + jRest;
				jRest += jOnes[j--];
			}
		}
		/*返回结算*/
		return total;
	}

}
