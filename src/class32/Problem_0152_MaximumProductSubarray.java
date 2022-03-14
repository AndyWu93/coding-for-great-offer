package class32;

/**
 * Given an integer array nums, find a contiguous non-empty subarray within the array that has the largest product, and return the product.
 *
 * The test cases are generated so that the answer will fit in a 32-bit integer.
 *
 * A subarray is a contiguous subsequence of the array.
 * 题意：求最大子数组的累乘积
 * 解题：
 * 	子数组问题，必须以i位置结尾的最大子数组累乘积是多少
 * 	假设来的i位置，i位置结尾的最大子数组累乘积有3种可能性：
 * 	1. ans = arr[i]
 * 	2. ans = arr[i] * dp[i-1]
 * 	3. 第三种情况最难想，arr[i]位置可能是负数，所以需要乘以i-1位置的最小累乘积，以得到最大累乘积
 * 		ans = arr[i] * dpMin[i-1]
 * 	所以来到i位置时需要求以i位置结尾的最大/最小子数组累乘积
 */
public class Problem_0152_MaximumProductSubarray {

	
	public static double max(double[] arr) {
		if(arr == null || arr.length == 0) {
			return 0; // 报错！
		}
		int n = arr.length;
		// 上一步的最大
		double premax = arr[0];
		// 上一步的最小
		double premin = arr[0];
		double ans = arr[0];
		for(int i = 1; i < n; i++) {
			double p1 = arr[i];
			double p2 = arr[i] * premax;
			double p3 = arr[i] * premin;
			double curmax = Math.max(Math.max(p1, p2), p3);
			double curmin = Math.min(Math.min(p1, p2), p3);
			ans = Math.max(ans, curmax);
			premax = curmax;
			premin = curmin;
		}
		return ans;
	}	
	
	
	
	public static int maxProduct(int[] nums) {
		int ans = nums[0];
		int min = nums[0];
		int max = nums[0];
		for (int i = 1; i < nums.length; i++) {
			int curmin = Math.min(nums[i], Math.min(min * nums[i], max * nums[i]));
			int curmax = Math.max(nums[i], Math.max(min * nums[i], max * nums[i]));
			min = curmin;
			max = curmax;
			ans  = Math.max(ans, max);
		}
		return ans;
	}

}
