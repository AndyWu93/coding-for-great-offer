package class32;

import class15.Code04_BestTimeToBuyAndSellStockIV;

/**
 * 交易次数在k次以内（买入+卖出视为1次交易）
 * 思路：
 * 如果k>=2/N,意味着能够抓住每一个可能的上升，就和不限交易次数结果一样
 * 否则，该题是一个动态规划
 * dp[i][j]:从0~i号时间点，严格限制交易次数为j次，此时获得的最大利润
 * 规模dp[n][k+1](k+1:因为有0次、k次交易的情况)
 * dp[0..n-1][0]=0 不交易没利润
 * dp[0][0..k]=0 只停留在0号时间点，交易多少次都没利润
 * dp[i][j]
 * 1. i号时间点不交易：dp[i-1][j]
 * 2. i号时间点交易：一定有卖出行为才可能获得最大利润，最后一次卖出一定是在i：
 * 	a. 最后一次买入在i：dp[i][j-1] + arr[i] - arr[i]
 * 	a. 最后一次买入在i-1：dp[i-1][j-1] + arr[i] - arr[i-1]
 * 	b. 最后一次买入在i-2：dp[i-2][j-1] + arr[i] - arr[i-2]
 * 	c. 最后一次买入在i-3：dp[i-3][j-1] + arr[i] - arr[i-3]
 *  ...
 *  a,b,c... pk取最大值
 * 1,2 pk取最大值
 * 所有的dp表中的最大值就是题解
 *
 * 优化：第2种情况可以斜率优化
 * dp[i][j]：
 * dp[i][j-1] + arr[i] - arr[i]
 * dp[i-1][j-1] + arr[i] - arr[i-1]
 * dp[i-2][j-1] + arr[i] - arr[i-2]
 * dp[i-3][j-1] + arr[i] - arr[i-3]
 * ...PK时把arr[i]先拿掉，最后加上
 *
 * dp[i-1][j]：
 * dp[i-1][j-1] + arr[i-1] - arr[i-1]
 * dp[i-2][j-1] + arr[i-1] - arr[i-2]
 * dp[i-3][j-1] + arr[i-1] - arr[i-3]
 * dp[i-4][j-1] + arr[i-1] - arr[i-4]
 * ...PK时把arr[i-1]先拿掉，最后加上
 *
 * 上面两个dp的值获取过程中，部分值已经作过比较
 *
 * 进阶与思考
 * 1.
 * 	关于dp[i][j]其实不需要严格定义为arr[0..i]交易j次，获取的最大利润。因为交易次数越多，利润只可能变大，
 * 	所以dp[i][j]可以定义为arr[0..i]交易不大于j次，获取的最大利润，这样就可以返回dp[N-1][K]，即方法maxProfit2
 * 2.
 * 	下面代买中需要pk的部分，即dp[x][j-1]-arr[x] x∈[0,i-1];
 * 	其实质就是一直到i时间点，（交易不超过j-1次数获得的最佳收益 + 一个最佳买入时机）整体最优
 * 	这个思路可以启发BestTimeToBuyAndSellStockWithCooldown的模型
 *
 * @see Code04_BestTimeToBuyAndSellStockIV
 *
 */
//leetcode 188
public class Problem_0188_BestTimeToBuyAndSellStockIV {



	/**
	 * 课后版本
	 */
	public static int maxProfit(int K, int[] prices) {
		if (prices == null || prices.length == 0) {
			return 0;
		}
		int N = prices.length;
		if (K >= N / 2) {
			return allTrans(prices);
		}
		int[][] dp = new int[K + 1][N];
		int ans = 0;
		for (int tran = 1; tran <= K; tran++) {
			int pre = dp[tran][0];
			int best = pre - prices[0];
			for (int index = 1; index < N; index++) {
				pre = dp[tran - 1][index];
				dp[tran][index] = Math.max(dp[tran][index - 1], prices[index] + best);
				best = Math.max(best, pre - prices[index]);
				ans = Math.max(dp[tran][index], ans);
			}
		}
		return ans;
	}

	public static int allTrans(int[] prices) {
		int ans = 0;
		for (int i = 1; i < prices.length; i++) {
			ans += Math.max(prices[i] - prices[i - 1], 0);
		}
		return ans;
	}

	/**
     * 课上写的版本，对了
	 */
	public static int maxProfit2(int K, int[] arr) {
		if (arr == null || arr.length == 0 || K < 1) {
			return 0;
		}
		int N = arr.length;
		if (K >= N / 2) {
			return allTrans(arr);
		}
		int[][] dp = new int[N][K + 1];
		// dp[...][0] = 0
		// dp[0][...] = arr[0.0] 0
		for (int j = 1; j <= K; j++) {
			// dp[1][j]
			int p1 = dp[0][j];
			int best = Math.max(dp[1][j - 1] - arr[1], dp[0][j - 1] - arr[0]);
			dp[1][j] = Math.max(p1, best + arr[1]);
			// dp[1][j] 准备好一些枚举，接下来准备好的枚举
			for (int i = 2; i < N; i++) {
				p1 = dp[i - 1][j];
				int newP = dp[i][j - 1] - arr[i];
				best = Math.max(newP, best);
				dp[i][j] = Math.max(p1, best + arr[i]);
			}
		}
		return dp[N - 1][K];
	}



	/**
	 * 适配类注释的解法
	 */
	public static int maxProfit3(int K, int[] prices) {
		if (prices == null || prices.length == 0) {
			return 0;
		}
		int N = prices.length;
		if (K >= N / 2) {
			return allTrans(prices);
		}
		int[][] dp = new int[N][K + 1];
//		int ans = 0;
		//因为dp[i][j]依赖dp[i-1][j]、dp[0..i-1][j-1]，所以先确定交易次数，再依次按时间点求解
		for (int j = 1; j <= K; j++) {
			/*
			 * 下面的循环里一定从dp[1][j]开始求值
			 * 需要以下两个值的pk
			 * dp[1][j-1] - arr[1] 这个是它自己的
			 * dp[0][j-1] - arr[0] 这个是以往的，可以传递的
			 * 所以现在给了一个临时变量t，存储dp[0][j-1] - arr[0]
			 * 因为求dp[i][j]时，要将i-1时间点以前，所有交易j-1次值pk，t用于存储以往pk出的最大值
			 * */
			int t = dp[0][j - 1] - prices[0];
			for (int i = 1; i < N; i++) {
				//随着i++，t一路pk，将以往的最大值传递了下去
				t = Math.max(t, dp[i][j - 1] - prices[i]);
				// 需要跟i时间点不交易的情况PK下，需要的注意t不包含arr[i]的值，需要加上
				dp[i][j] = Math.max(dp[i - 1][j], t + prices[i]);
//				ans = Math.max(ans, dp[i][j]);
			}
		}
//		return ans;
		return dp[N-1][K];
	}

	/**
	 * maxProfit3空间压缩
	 */
	public static int maxProfit4(int K, int[] prices) {
		if (prices == null || prices.length == 0) {
			return 0;
		}
		int N = prices.length;
		if (K >= N / 2) {
			return allTrans(prices);
		}
		// dp一维表，做了空间压缩
		int[] dp = new int[N];
		int ans = 0;
		for (int tran = 1; tran <= K; tran++) {
			int pre = dp[0];
			int best = pre - prices[0];
			for (int index = 1; index < N; index++) {
				pre = dp[index];
				dp[index] = Math.max(dp[index - 1], prices[index] + best);
				best = Math.max(best, pre - prices[index]);
				ans = Math.max(dp[index], ans);
			}
		}
		return ans;
	}

	public static void main(String[] args) {
		int[] test = { 4, 1, 231, 21, 12, 312, 312, 3, 5, 2, 423, 43, 146 };
		int K = 3;
		System.out.println(maxProfit2(K, test));
		System.out.println(maxProfit(K, test));

	}

}
