package class25;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Given an integer array nums, return all the triplets [nums[i], nums[j], nums[k]] such that i != j, i != k, and j != k, and nums[i] + nums[j] + nums[k] == 0.
 *
 * Notice that the solution set must not contain duplicate triplets.
 * 题意：找到3数之和加起来等0返回，这个3元组不能重复（两个3元组，每个值都一样视为重复）
 * 解题：
 * 思路：
 * 	设计一个两数之和的函数f，返回不重复的二元组，和为k，
 * 	枚举arr中的每一个不同的值arr[i],作为3元组的第一个数，调用f函数，求arr剩下的数中和为-arr[i]的两个数
 * 	拼在一起就是一个3元组
 */
// 本题测试链接 : https://leetcode.com/problems/3sum/
public class Code02_3Sum {

	public static List<List<Integer>> threeSum(int[] nums) {
		/*这里排序了，那两数之和就可以用指针来解，而且方便去重。否则，两数之和要用map来解，去重需要重新设计*/
		Arrays.sort(nums);
		int N = nums.length;
		List<List<Integer>> ans = new ArrayList<>();
		/*
		* 为什么从后往前枚举？
		* 因为最后返回的是list，这样枚举就可以直接加到list的尾部，代价小
		* */
		for (int i = N - 1; i > 1; i--) { // 三元组最后一个数，是arr[i]   之前....二元组 + arr[i]
			/*枚举的每个数不相同*/
			if (i == N - 1 || nums[i] != nums[i + 1]) {
				List<List<Integer>> nexts = twoSum(nums, i - 1, -nums[i]);
				for (List<Integer> cur : nexts) {
					cur.add(nums[i]);
					ans.add(cur);
				}
			}
		}
		return ans;
	}

	// nums[0...end]这个范围上，有多少个不同二元组，相加==target，全返回
	// {-1,5}     K = 4
	// {1, 3}
	public static List<List<Integer>> twoSum(int[] nums, int end, int target) {
		int L = 0;
		int R = end;
		List<List<Integer>> ans = new ArrayList<>();
		while (L < R) {
			if (nums[L] + nums[R] > target) {
				R--;
			} else if (nums[L] + nums[R] < target) {
				L++;
			} else { // nums[L] + nums[R] == target
				/*收集答案的前提：因为排序了，L指针和前面一个位置的数不相等，就一定是不同的二元组*/
				if (L == 0 || nums[L - 1] != nums[L]) {
					List<Integer> cur = new ArrayList<>();
					cur.add(nums[L]);
					cur.add(nums[R]);
					ans.add(cur);
				}
				L++;
			}
		}
		return ans;
	}

	public static int findPairs(int[] nums, int k) {
		Arrays.sort(nums);
		int left = 0, right = 1;
		int result = 0;
		while (left < nums.length && right < nums.length) {
			if (left == right || nums[right] - nums[left] < k) {
				right++;
			} else if (nums[right] - nums[left] > k) {
				left++;
			} else {
				left++;
				result++;
				while (left < nums.length && nums[left] == nums[left - 1])
					left++;
			}
		}
		return result;
	}

}
