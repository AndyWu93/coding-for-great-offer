package class38;

import java.util.ArrayList;
import java.util.List;

/**
 * 给你一个含 n 个整数的数组 nums ，其中 nums[i] 在区间 [1, n] 内。请你找出所有在 [1, n] 范围内但没有出现在 nums 中的数字，并以数组的形式返回结果。
 * 示例 1：
 * 输入：nums = [4,3,2,7,8,2,3,1]
 * 输出：[5,6]
 * 示例 2：
 * 输入：nums = [1,1]
 * 输出：[2]
 * 提示：
 * n == nums.length
 * 1 <= n <= 10^5
 * 1 <= nums[i] <= n
 * 进阶：你能在不使用额外空间且时间复杂度为 O(n) 的情况下解决这个问题吗? 你可以假定返回的数组不算在额外空间内。
 *
 * 题意：arr[0~n-1]的值范围是1~n，找出其中缺失的数字
 * 解题：调整数组，做到arr[i] = i+1,都调整完了以后，遍历arr，收集arr[i]!=i+1的位置，这些位置上的值就是缺失的数
 */
//Leetcode题目 : https://leetcode.com/problems/find-all-numbers-disappeared-in-an-array/
public class Problem_0448_FindAllNumbersDisappearedInAnArray {

	/**
	 * 时间复杂度O(N)
	 * 空间复杂度O(1)
	 */
	public static List<Integer> findDisappearedNumbers(int[] nums) {
		List<Integer> ans = new ArrayList<>();
		if (nums == null || nums.length == 0) {
			return ans;
		}
		int N = nums.length;
		/*
		* 每个位置调整
		* */
		for (int i = 0; i < N; i++) {
			// 从i位置出发，去玩下标循环怼
			walk(nums, i);
		}
		for (int i = 0; i < N; i++) {
			if (nums[i] != i + 1) {
				ans.add(i + 1);
			}
		}
		return ans;
	}

	/*
	* 这里用了交换的方式
	* */
	public static void walk(int[] nums, int i) {
		while (nums[i] != i + 1) { // 不断从i发货
			/*找到i该去的位置：nums[i] - 1*/
			int nexti = nums[i] - 1;
			if (nums[nexti] == nexti + 1) {
				/*如果这个位置已经符合条件了，可以return了*/
				break;
			}
			/*交换之后，继续盯着arr[i]位置*/
			swap(nums, i, nexti);
		}
	}

	public static void swap(int[] nums, int i, int j) {
		int tmp = nums[i];
		nums[i] = nums[j];
		nums[j] = tmp;
	}

}
