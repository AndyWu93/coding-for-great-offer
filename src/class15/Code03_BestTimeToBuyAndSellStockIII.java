package class15;

/**
 * BestTimeToBuyAndSellStockIV，k=2的情况，返回最大利润
 */
//leetcode 123
public class Code03_BestTimeToBuyAndSellStockIII {

	public static int maxProfit(int[] prices) {
		if (prices == null || prices.length < 2) {
			return 0;
		}
		int ans = 0;
		/*
		* 含义：做完1次交易，并一个最佳买入时机，整体最优的利润
		* 初始值，在arr[0..0]范围上做完1次交易获得的最佳利润为0，0位置买入后利润为-arr[0]
		* */
		int doneOnceMinusBuyMax = -prices[0];
		/*做一次交易的最大利润*/
		int doneOnceMax = 0;
		int min = prices[0];
		for (int i = 1; i < prices.length; i++) {
			min = Math.min(min, prices[i]);
			/*当前价格，减去之前（最大利润及最佳买入）就是题解*/
			ans = Math.max(ans, doneOnceMinusBuyMax + prices[i]);
			/*下面这两个是为下一次的ans计算做准备，其实最后一次计算是不需要的*/
			doneOnceMax = Math.max(doneOnceMax, prices[i] - min);
			doneOnceMinusBuyMax = Math.max(doneOnceMinusBuyMax, doneOnceMax - prices[i]);
		}
		return ans;
	}

}
