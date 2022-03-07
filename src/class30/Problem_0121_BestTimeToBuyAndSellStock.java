package class30;

/**
 *You are given an array prices where prices[i] is the price of a given stock on the ith day.
 *
 * You want to maximize your profit by choosing a single day to buy one stock and choosing a different day in the future to sell that stock.
 *
 * Return the maximum profit you can achieve from this transaction. If you cannot achieve any profit, return 0.
 * 题意：股票价格在数组prices中，如果只能买卖一次，能获取的最大利润是多少
 * 解题：
 * 	枚举每个位置的股票价格为卖出时间点，结算利润，收集最大利润
 * 	遍历arr
 * 	来到i，假设是卖出时间点，减去左边收集到的最小值，就是此时的最大利润
 * 	依次向后
 * 	取最大利润
 */
// leetcode 121
public class Problem_0121_BestTimeToBuyAndSellStock {

	public static int maxProfit(int[] prices) {
		if (prices == null || prices.length == 0) {
			return 0;
		}
		/*在0时刻买入再卖掉，最少能挣0元*/
		int ans = 0;
		// arr[0...0]
		int min = prices[0];
		for (int i = 1; i < prices.length; i++) {
			/*遍历到此时的最小值*/
			min = Math.min(min, prices[i]);
			/*当前位置-最小值，和之前的答案，取最大的*/
			ans = Math.max(ans, prices[i] - min);
		}
		return ans;
	}

}
