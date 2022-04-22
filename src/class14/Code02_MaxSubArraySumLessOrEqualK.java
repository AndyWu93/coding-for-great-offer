package class14;

import java.util.TreeSet;


/**
 * arr中求子数组的累加和是<=K的并且是最大的，返回这个最大的累加和
 * 思路：
 * 子数组问题，一般考虑以..结尾
 * 假设以子数组以i位置结尾，0..i位置的累加和为sum
 * 要求以i位置结尾子数组累加和小于等于k的最大，
 * 相当于求0..某个位置子数组累加和大于等于sum-k的最小
 * 最快的方式就是有序表求
 * 复杂度：O(N*logN)
 *
 * 转换：
 * 如果arr中都是正数
 * 数组的长度和累加建立了单调性，可以用窗口解
 */
public class Code02_MaxSubArraySumLessOrEqualK {

	// 请返回arr中，求个子数组的累加和，是<=K的，并且是最大的。
	// 返回这个最大的累加和
	public static int getMaxLessOrEqualK(int[] arr, int K) {
		// 记录i之前的，前缀和，按照有序表组织
		TreeSet<Integer> set = new TreeSet<Integer>();
		/*重要！！：一个数也没有的时候，就已经有一个前缀和是0了*/
		set.add(0);
		int max = Integer.MIN_VALUE;
		int sum = 0;
		// 每一步的i，都求子数组必须以i结尾的情况下，求个子数组的累加和，是<=K的，并且是最大的
		for (int i = 0; i < arr.length; i++) {
			sum += arr[i]; // sum -> arr[0..i];
			/*找set中大于等于sum-k最小的*/
			if (set.ceiling(sum - K) != null) {
				max = Math.max(max, sum - set.ceiling(sum - K));
			}
			set.add(sum); // 当前的前缀和加入到set中去
		}
		return max;

	}

}
