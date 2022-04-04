package class03;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 给定整数数组nums和目标值goal，需要从nums中选出一个子序列，使子序列元素总和最接近goal
 * 也就是说如果子序列元素和为sum ，需要最小化绝对差abs(sum - goal)，返回 abs(sum - goal)可能的最小值
 * 题意：找个一个累加和最接近goal的子序列，返回累加和和goal的差值
 * 解题：
 * 	从数据量看，dp表是不行的，傻缓存是不是可以呢？是不是可以考虑一下
 * 进过实践：
 * 该方法即使用了dp还是会超时
 * 为何，因为每个位置要/不要两种可能性，复杂度2^n,n=40超时了，但是如果n=20就不会超时，
 * 所以
 * 可以采用分治，20个长度一组，计算出所有的累加和
 * 再一个组取一个数a，另一组取出最接近goal-a的数，可以二分查
 * 这样的复杂度就是O(2*2^20 + 2^20*log2^20) 化简得到O(22*2^20)
 */
// 本题测试链接 : https://leetcode.com/problems/closest-subsequence-sum/
// 本题数据量描述:
// 1 <= nums.length <= 40
// -10^7 <= nums[i] <= 10^7
// -10^9 <= goal <= 10^9
// 通过这个数据量描述可知，需要用到分治，因为数组长度不大
// 而值很大，用动态规划的话，表会爆
public class Code06_ClosestSubsequenceSum {





	public static int[] l = new int[1 << 20];
	public static int[] r = new int[1 << 20];

	public static int minAbsDifference(int[] nums, int goal) {
		if (nums == null || nums.length == 0) {
			return goal;
		}
		int le = process(nums, 0, nums.length >> 1, 0, 0, l);
		int re = process(nums, nums.length >> 1, nums.length, 0, 0, r);
		Arrays.sort(l, 0, le);
		Arrays.sort(r, 0, re--);
		int ans = Math.abs(goal);
		for (int i = 0; i < le; i++) {
			int rest = goal - l[i];
			while (re > 0 && Math.abs(rest - r[re - 1]) <= Math.abs(rest - r[re])) {
				re--;
			}
			ans = Math.min(ans, Math.abs(rest - r[re]));
		}
		return ans;
	}

	public static int process(int[] nums, int index, int end, int sum, int fill, int[] arr) {
		if (index == end) {
			arr[fill++] = sum;
		} else {
			fill = process(nums, index + 1, end, sum, fill, arr);
			fill = process(nums, index + 1, end, sum + nums[index], fill, arr);
		}
		return fill;
	}

	/**
	 * 该方法即使用了dp还是会超时
	 * 为何，因为复杂度接近2^n,n=40超时了，但是如果n=20就不会超时，
	 * 所以
	 * 可以采用分治，20个长度一组，计算出所有的累加和
	 * 再两个组各取一个数加起来
	 */
	public static int minAbsDifference1(int[] nums, int goal) {
		if (nums == null || nums.length == 0) {
			return goal;
		}
		Map<Integer,Map<Integer,Integer>> dp =new HashMap<>();
		return process(nums, goal, 0, 0,dp);
	}
	private static int process(int[] nums, int goal,int index,int sum,Map<Integer,Map<Integer,Integer>> dp  ){
		if (dp.get(index)!=null && dp.get(index).get(sum)!=null){
			return dp.get(index).get(sum);
		}

		int abs;
		if(index==nums.length){
			abs = Math.abs(goal-sum);
		}else {
			int p1 = process(nums, goal, index+1, sum,dp);
			int p2 = process(nums, goal, index+1, sum+nums[index],dp);
			abs = Math.min(p1,p2);
		}
		Map<Integer,Integer> value = new HashMap<>();
		value.put(sum,abs);
		dp.put(index,value);
		return abs;
	}

}
