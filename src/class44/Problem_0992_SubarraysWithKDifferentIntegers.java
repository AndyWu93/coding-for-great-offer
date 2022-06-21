package class44;

import java.util.HashMap;

/**
 * 给定一个正整数数组arr，和正数k
 * 如果arr的某个子数组中含有k种不同的数，称这个子数组为有效子数组
 * 返回arr中有效子数组的数量
 *
 * 解题：
 * 	本题使用窗口，模型稍微复杂一点
 * 模型1：
 * 	来到i位置
 * 	窗口1：以arr[i]结尾的子数组，种类=k，子数组开头的位置为x
 * 	窗口2：以arr[i]结尾的子数组，种类=k-1，子数组开头的位置为y
 * 	结算答案ans += y-x
 * 	去到i+1位置，继续
 * 模型2：
 * 	来到i位置
 * 	窗口左边界是i，窗口向右扩，直到窗口中有k种数，且不能再往右扩，
 * 	结算此时窗口内必须以arr[i]开头的子数组个数，这些子数组表示以arr[i]开头的种类小于等于k的子数组个数
 * 	来到i+1位置，继续
 * 	结束以后，得到arr中，种类小于等于k的子数组个数：x，
 * 	同样的模型再来一遍，计算出arr中，种类小于等于k-1的子数组个数：y，
 * 	x-y就是题解
 */
//leetcode题目 : https://leetcode.com/problems/subarrays-with-k-different-integers/
public class Problem_0992_SubarraysWithKDifferentIntegers {

	/**
	 * 模型1
	 */
	// nums 数组，题目规定，nums中的数字，不会超过nums的长度
	// [ ]长度为5，0~5
	public static int subarraysWithKDistinct1(int[] nums, int k) {
		int n = nums.length;
		// k-1种数的窗口词频统计
		int[] lessCounts = new int[n + 1];
		// k种数的窗口词频统计
		int[] equalCounts = new int[n + 1];
		/*k-1种数窗口的左边界*/
		int lessLeft = 0;
		/*k种数窗口的左边界*/
		int equalLeft = 0;
		/*k-1种数窗口内数字种数*/
		int lessKinds = 0;
		/*k种数窗口内数字种数*/
		int equalKinds = 0;
		int ans = 0;
		for (int r = 0; r < n; r++) {
			// 当前刚来到r位置！
			/*r位置词频如果是0，两个窗口内种数++，r的词频++*/
			if (lessCounts[nums[r]] == 0) {
				lessKinds++;
			}
			if (equalCounts[nums[r]] == 0) {
				equalKinds++;
			}
			lessCounts[nums[r]]++;
			equalCounts[nums[r]]++;
			/*k-1种数窗口内数组种类数大于k-1了，缩窗口，直到种类是k-1*/
			while (lessKinds == k) {
				if (lessCounts[nums[lessLeft]] == 1) {
					lessKinds--;
				}
				lessCounts[nums[lessLeft++]]--;
			}
			/*k种数窗口内数组种类数大于k了，缩窗口，直到种类是k*/
			while (equalKinds > k) {
				if (equalCounts[nums[equalLeft]] == 1) {
					equalKinds--;
				}
				equalCounts[nums[equalLeft++]]--;
			}
			/*结算，以r位置结尾的k种数子数组个数，k-1种数子数组个数，数量差*/
			ans += lessLeft - equalLeft;
		}
		return ans;
	}

	/**
	 * 模型2
	 */
	public static int subarraysWithKDistinct2(int[] arr, int k) {
		return numsMostK(arr, k) - numsMostK(arr, k - 1);
	}

	/*
	* arr中，<=k种数的子数组个数总数
	* */
	public static int numsMostK(int[] arr, int k) {
		int i = 0, res = 0;
		HashMap<Integer, Integer> count = new HashMap<>();
		for (int j = 0; j < arr.length; ++j) {
			/*j位置入窗口*/
			if (count.getOrDefault(arr[j], 0) == 0) {
				k--;
			}
			count.put(arr[j], count.getOrDefault(arr[j], 0) + 1);
			/*窗口内种数大于k了，缩小窗口左边界，始终维持窗口内种数小于等于k*/
			while (k < 0) {
				count.put(arr[i], count.get(arr[i]) - 1);
				if (count.get(arr[i]) == 0) {
					k++;
				}
				i++;
			}
			/*结算：必须以arr[j]位置结尾的子数组，种类数小于等于k的子数组个数*/
			res += j - i + 1;
		}
		return res;
	}

}
