package class07;

import java.util.Arrays;

/**
 * 无序数组arr，求如果排序后最大的相邻差，要求O(n)（经典难题）
 * 解题：
 * 	要求O(N)肯定不能排序了，那要怎么找出最大相邻差呢？
 * 	假设arr中每个距离都相等，这个距离是(max-min)/n，这个差一定不小于这个值，小于这个值的不用看了
 * 	借用桶排序思想，用一个空桶设计（鸽笼原理）可以过滤掉小于这个值的情况
 *
 * 流程：
 * 	求出数组中的min，max
 * 	准备n+1个桶，每个桶的长度是(max-min)/(n+1)，即把arr的最大距离n+1等分，一个桶负责一个range
 * 	遍历arr，把每个数放到各个桶中，桶里只保留最大值和最小值
 * 	分别求相邻两个非空桶之间的max-min，收集最大值即题解
 * 怎么确定当前arr[i]在哪个桶？
 * 	用arr[i]与最小值比较，就知道他们之间有多少个桶的长度
 *
 * 流程解析：
 * 	数组长度为n，但是有n+1个桶，意味着至少有一个空桶，那么两个数之间的差最少也是夸了一个空桶的长度
 * 	所以同一个桶内的数就不用看了，肯定不是最大差
 * 那为什么不直接看隔了一个空桶的两个桶之间的差值呢？
 * 	两个相邻的桶，如果有个桶的max特别大，一个桶的min特别小，就有可能垮了接近两个桶的长度
 * 	而隔了一个空桶的两个桶，如果有个桶的max特别小，一个桶的min特别大，也就垮了一个桶的长度而已
 * 	所以还是要都看过去
 */
// 测试链接 : https://leetcode.com/problems/maximum-gap/
public class Code03_MaxGap {

	public static int maxGap(int[] nums) {
		if (nums == null || nums.length < 2) {
			return 0;
		}
		int len = nums.length;
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;
		for (int i = 0; i < len; i++) {
			min = Math.min(min, nums[i]);
			max = Math.max(max, nums[i]);
		}
		/*先求出了最大值，最小值*/
		if (min == max) {
			/*数组里全是一个数*/
			return 0;
		}
		/*
		* 不止一种数，min~max一定有range,  len个数据，准备len+1个桶
		* 因为只需要知道桶是不是空的，和桶的最大值最小值，3个数组就能存储全部信息
		* */
		boolean[] hasNum = new boolean[len + 1]; // hasNum[i] i号桶是否进来过数字
		int[] maxs = new int[len + 1];  // maxs[i] i号桶收集的所有数字的最大值
		int[] mins = new int[len + 1];  // mins[i] i号桶收集的所有数字的最小值
		
		int bid = 0; // 桶号
		for (int i = 0; i < len; i++) {
			bid = bucket(nums[i], len, min, max);
			/*如果bucket还是空，那nums[i]即是最大值也是最小值，不是空，就比对一下*/
			mins[bid] = hasNum[bid] ? Math.min(mins[bid], nums[i]) : nums[i];
			maxs[bid] = hasNum[bid] ? Math.max(maxs[bid], nums[i]) : nums[i];
			hasNum[bid] = true;
		}
		int res = 0;
		/*0号桶的最大值*/
		int lastMax = maxs[0]; // 上一个非空桶的最大值
		int i = 1;
		/*从1号桶开始看，用下一个桶的最小值减前一个桶的最大值，就能收集到最大差*/
		for (; i <= len; i++) {
			/*跳过空桶*/
			if (hasNum[i]) {
				res = Math.max(res, mins[i] - lastMax);
				lastMax = maxs[i];
			}
		}
		return res;
	}

	public static int bucket(long num, long len, long min, long max) {
		return (int) ((num - min) * len / (max - min));
	}

	// for test
	public static int comparator(int[] nums) {
		if (nums == null || nums.length < 2) {
			return 0;
		}
		Arrays.sort(nums);
		int gap = Integer.MIN_VALUE;
		for (int i = 1; i < nums.length; i++) {
			gap = Math.max(nums[i] - nums[i - 1], gap);
		}
		return gap;
	}

	// for test
	public static int[] generateRandomArray(int maxSize, int maxValue) {
		int[] arr = new int[(int) ((maxSize + 1) * Math.random())];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (int) ((maxValue + 1) * Math.random()) - (int) (maxValue * Math.random());
		}
		return arr;
	}

	// for test
	public static int[] copyArray(int[] arr) {
		if (arr == null) {
			return null;
		}
		int[] res = new int[arr.length];
		for (int i = 0; i < arr.length; i++) {
			res[i] = arr[i];
		}
		return res;
	}

	// for test
	public static void main(String[] args) {
		int testTime = 500000;
		int maxSize = 100;
		int maxValue = 100;
		boolean succeed = true;
		for (int i = 0; i < testTime; i++) {
			int[] arr1 = generateRandomArray(maxSize, maxValue);
//			int[] arr1 = {1,2,3,4,5,6,7,8,9};
			int[] arr2 = copyArray(arr1);
			if (maxGap(arr1) != comparator(arr2)) {
				succeed = false;
				break;
			}
		}
		System.out.println(succeed ? "Nice!" : "Fucking fucked!");
	}

}
