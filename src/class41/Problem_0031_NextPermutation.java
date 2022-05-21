package class41;

/**
 * 实现获取 下一个排列 的函数，算法需要将给定数字序列重新排列成字典序中下一个更大的排列（即，组合出下一个更大的整数）。
 * 如果不存在下一个更大的排列，则将数字重新排列成最小的排列（即升序排列）。
 * 必须 原地 修改，只允许使用额外常数空间。
 * 示例 1：
 * 输入：nums = [1,2,3]
 * 输出：[1,3,2]
 * 示例 2：
 * 输入：nums = [3,2,1]
 * 输出：[1,2,3]
 * 示例 3：
 * 输入：nums = [1,1,5]
 * 输出：[1,5,1]
 * 示例 4：
 * 输入：nums = [1]
 * 输出：[1]
 *
 * 题意：
 * 	有一组数字，将这些数字全排列，排列之间按字典序排列，给定一组排列，求出下一组排列。如果当前排列是字典序最大值，就返回排列中字典序最小的
 * 解题：
 * 	分析题目，本题是贪心
 * 	从右往左，找到第一个降序的数字a，
 * 	表明a，包含a往右的部分，还没有达到字典序最大，所以，下一组中，a往左的部分和当前组一样
 * 	现在就要看a往右的这部分，下一组是什么
 * 	因为a是第一个降序的，所以a右边的部分都是升序，即已经是最大字典序
 * 	所以a作为头，下一组要换头了，换一个比a大的数b，b要从a的右边选出，
 * 	然后a和b互换一下
 * 	换完后，b做头了，b右边应当要以最小字典序的形式出现，
 * 	而当前是最大字典序的状态，所以b的右边逆序，成为最小字典序
 *
 */
//leetcode题目 : https://leetcode.com/problems/next-permutation/
public class Problem_0031_NextPermutation {

	public static void nextPermutation(int[] nums) {
		int N = nums.length;
		// 从右往左第一次降序的位置
		int firstLess = -1;
		for (int i = N - 2; i >= 0; i--) {
			if (nums[i] < nums[i + 1]) {
				firstLess = i;
				/*找到了就停*/
				break;
			}
		}
		if (firstLess < 0) {
			/*如果一直都没找到，那么表明当前组是最大字典序，整体逆序变成最小字典序返回*/
			reverse(nums, 0, N - 1);
		} else {
			int rightClosestMore = -1;
			// 找最靠右的、同时比nums[firstLess]大的数，位置在哪
			// 这里其实也可以用二分优化，但是这种优化无关紧要了
			for (int i = N - 1; i > firstLess; i--) {
				/*在firstLess右边找到第一个大于自己的数*/
				if (nums[i] > nums[firstLess]) {
					rightClosestMore = i;
					break;
				}
			}
			/*找到后交换*/
			swap(nums, firstLess, rightClosestMore);
			/*剩下的部分逆序，变成最小字典序*/
			reverse(nums, firstLess + 1, N - 1);
		}
	}

	public static void reverse(int[] nums, int L, int R) {
		while (L < R) {
			swap(nums, L++, R--);
		}
	}

	public static void swap(int[] nums, int i, int j) {
		int tmp = nums[i];
		nums[i] = nums[j];
		nums[j] = tmp;
	}

}
