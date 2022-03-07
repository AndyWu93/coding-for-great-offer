package class30;

/**
 * You are given an integer array prices where prices[i] is the price of a given stock on the ith day.
 *
 * On each day, you may decide to buy and/or sell the stock. You can only hold at most one share of the stock at any time. However, you can buy it then immediately sell it on the same day.
 *
 * Find and return the maximum profit you can achieve.
 * 题意：交易次数不限，手里最多只能1股
 * 解题：
 * 	实际上是要求每一次上升趋势的利润之和
 * 	遍历arr，来到了i，看i比i—1大？获取利润：继续往后遍历
 * 	利润之和就是题解
 */
//leetcode 122
public class Problem_0122_BestTimeToBuyAndSellStockII {

	public static int maxProfit(int[] prices) {
		if (prices == null || prices.length == 0) {
			return 0;
		}
		int ans = 0;
		for (int i = 1; i < prices.length; i++) {
            ans += Math.max(prices[i] - prices[i-1], 0);
		}
		return ans;
	}
	
}
