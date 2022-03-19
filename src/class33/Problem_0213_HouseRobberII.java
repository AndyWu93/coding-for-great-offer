package class33;

import class32.Problem_0198_SubArrayMaxSumFollowUp;

/**
 * You are a professional robber planning to rob houses along a street. Each house has a certain amount of money stashed. All houses at this place are arranged in a circle. That means the first house is the neighbor of the last one. Meanwhile, adjacent houses have a security system connected, and it will automatically contact the police if two adjacent houses were broken into on the same night.
 *
 * Given an integer array nums representing the amount of money of each house, return the maximum amount of money you can rob tonight without alerting the police.
 * 题意：打家劫舍系列之环形数组
 * @see Problem_0198_SubArrayMaxSumFollowUp 打家劫舍I
 * 解题：
 * 	本题是打家劫舍I的延伸
 * 	竟然是环形的，那么就分一下两者情况考虑：
 * 		a.打劫 0~n-2 位置的房子 -- 转为打家劫舍I问题
 * 		b.打劫 1~n-1 位置的房子 -- 转为打家劫舍I问题
 *	max(a,b)就是题解
 */
public class Problem_0213_HouseRobberII {

	/**
	 * 打家劫舍I
	 */
	// arr 长度大于等于1
	public static int pickMaxSum(int[] arr) {
		int n = arr.length;
		// dp[i] : arr[0..i]范围上，随意选择，但是，任何两数不能相邻。得到的最大累加和是多少？
		int[] dp = new int[n];
		dp[0] = arr[0];
		dp[1] = Math.max(arr[0], arr[1]);
		for (int i = 2; i < n; i++) {
			int p1 = arr[i];
			int p2 = dp[i - 1];
			int p3 = arr[i] + dp[i - 2];
			dp[i] = Math.max(p1, Math.max(p2, p3));
		}
		return dp[n - 1];
	}

	/**
	 * 这里是优化后的，为了考虑代码的易读性，不建议优化
	 */
	public static int rob(int[] nums) {
		if (nums == null || nums.length == 0) {
			return 0;
		}
		if (nums.length == 1) {
			return nums[0];
		}
		if (nums.length == 2) {
			return Math.max(nums[0], nums[1]);
		}
		int pre2 = nums[0];
		int pre1 = Math.max(nums[0], nums[1]);
		for (int i = 2; i < nums.length - 1; i++) {
			int tmp = Math.max(pre1, nums[i] + pre2);
			pre2 = pre1;
			pre1 = tmp;
		}
		int ans1 = pre1;
		pre2 = nums[1];
		pre1 = Math.max(nums[1], nums[2]);
		for (int i = 3; i < nums.length; i++) {
			int tmp = Math.max(pre1, nums[i] + pre2);
			pre2 = pre1;
			pre1 = tmp;
		}
		int ans2 = pre1;
		return Math.max(ans1, ans2);
	}

}
