package class22;

/**
 * 给定数组 nums 由正整数组成，找到三个互不重叠的子数组的最大和。每个子数组的长度为k，我们要使这3*k个项的和最大化。返回每个区间起始索引的列表（索引从 0 开始）。如果有多个结果，返回字典序最小的一个。
 *
 * 解题：
 * 	因为3个子数组互不重叠，所以可以使用窗口枚举出中间的子数组，长度为k，左右剩下的范围分别求出和最大的子数组
 * 	结果一定在某个枚举当中
 */
// 本题测试链接 : https://leetcode.com/problems/maximum-sum-of-3-non-overlapping-subarrays/
public class Code01_MaximumSumof3NonOverlappingSubarrays {

//	public static int[] maxSumArray1(int[] arr) {
//		int N = arr.length;
//		int[] help = new int[N];
//		// help[i] 子数组必须以i位置结尾的情况下，累加和最大是多少？
//		help[0] = arr[0];
//		for (int i = 1; i < N; i++) {
//			int p1 = arr[i];
//			int p2 = arr[i] + help[i - 1];
//			help[i] = Math.max(p1, p2);
//		}
//		// dp[i] 在0~i范围上，随意选一个子数组，累加和最大是多少？
//		int[] dp = new int[N];
//		dp[0] = help[0];
//		for (int i = 1; i < N; i++) {
//			int p1 = help[i];
//			int p2 = dp[i - 1];
//			dp[i] = Math.max(p1, p2);
//		}
//		return dp;
//	}
//
//	public static int maxSumLenK(int[] arr, int k) {
//		int N = arr.length;
//		// 子数组必须以i位置的数结尾，长度一定要是K，累加和最大是多少？
//		// help[0] help[k-2] ...
//		int sum = 0;
//		for (int i = 0; i < k - 1; i++) {
//			sum += arr[i];
//		}
//		// 0...k-2 k-1 sum
//		int[] help = new int[N];
//		for (int i = k - 1; i < N; i++) {
//			// 0..k-2 
//			// 01..k-1
//			sum += arr[i];
//			help[i] = sum;
//			// i == k-1  
//			sum -= arr[i - k + 1];
//		}
//		// help[i] - > dp[i]  0-..i  K
//
//	}

	public static int[] maxSumOfThreeSubarrays(int[] nums, int k) {
		int N = nums.length;
		/*range[0]=sum: 下标从0开始，长度为k的窗口内和为sum*/
		int[] range = new int[N];
		/*left[i]: 左边0~i范围内，最大累加和子数组的窗口开始位置*/
		int[] left = new int[N];
		int sum = 0;
		for (int i = 0; i < k; i++) {
			/*窗口达到k长度*/
			sum += nums[i];
		}
		range[0] = sum;
		left[k - 1] = 0;
		int max = sum;
		/*i：左边范围的右边界*/
		for (int i = k; i < N; i++) {
			/*窗口右移1个单位*/
			sum = sum - nums[i - k] + nums[i];
			/*记录当前窗口内的和*/
			range[i - k + 1] = sum;
			/*dp一定会继承之前范围内的最大子数组*/
			left[i] = left[i - 1];
			if (sum > max) {
				/*如果出现了累加和更大的子数组，更新dp*/
				max = sum;
				left[i] = i - k + 1;
			}
		}
		/*左边结束了，接下来计算右边*/
		sum = 0;
		for (int i = N - 1; i >= N - k; i--) {
			/*右边形成k的窗口*/
			sum += nums[i];
		}
		max = sum;
		/*右侧dp*/
		/*right[i]: 右边i~N-1范围内，最大累加和子数组的窗口开始位置*/
		int[] right = new int[N];
		right[N - k] = N - k;
		for (int i = N - k - 1; i >= 0; i--) {
			sum = sum - nums[i + k] + nums[i];
			right[i] = right[i + 1];
			if (sum >= max) {
				max = sum;
				right[i] = i;
			}
		}
		/*左右两个dp都准备好了，开始枚举中间的子数组*/
		int a = 0;
		int b = 0;
		int c = 0;
		max = 0;
		/*枚举中间的一块窗口的起始位置，所以左侧0~k-1,右侧N-K~N-1不在窗口活动的范围内*/
		for (int i = k; i < N - 2 * k + 1; i++) { // 中间一块的起始点 (0...k-1)选不了 i == N-1
			/*left[i - 1]：0~i-1范围内最大累加和子数组位置，再在range数组中拿到值*/
			int part1 = range[left[i - 1]];
			int part2 = range[i];
			/*right[i + k]：i+k~n-1范围内最大累加和子数组位置，再在range数组中拿到值*/
			int part3 = range[right[i + k]];
			if (part1 + part2 + part3 > max) {
				/*如果3个子数组的和大于max，收集3个子数组的开始位置*/
				max = part1 + part2 + part3;
				a = left[i - 1];
				b = i;
				c = right[i + k];
			}
		}
		return new int[] { a, b, c };
	}

}
