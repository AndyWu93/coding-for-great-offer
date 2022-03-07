package class02;

/**
 * 一个无序数组arr，如果排序，需要排序的部分最少是多少长度：
 * 思路：找出数组左侧不用排序的部分，和右侧不用排序的部分，中间必定要排序
 * 左侧：
 * 正向遍历arr，来到数a，与a左侧的max左比较如果a比max左小，记下a的位置，否则更新max左
 * 逆向遍历arr，来到数b，与b右侧的min右比较如果b比min右大，记下b的位置，否则更新min右
 * 最终b-a+1就是题解
 */
// 本题测试链接 : https://leetcode.com/problems/shortest-unsorted-continuous-subarray/
public class Code06_MinLengthForSort {

	public static int getMinLength(int[] arr) {
		if (arr == null || arr.length < 2) {
			return 0;
		}
		int min = arr[arr.length - 1];
		int noMinIndex = -1;
		for (int i = arr.length - 2; i != -1; i--) {
			if (arr[i] > min) {
				noMinIndex = i;
			} else {
				min = Math.min(min, arr[i]);
			}
		}
		if (noMinIndex == -1) {
			return 0;
		}
		int max = arr[0];
		int noMaxIndex = -1;
		for (int i = 1; i != arr.length; i++) {
			if (arr[i] < max) {
				noMaxIndex = i;
			} else {
				max = Math.max(max, arr[i]);
			}
		}
		return noMaxIndex - noMinIndex + 1;
	}

	public static void main(String[] args) {
		int[] arr = { 1, 2, 4, 7, 10, 11, 7, 12, 6, 7, 16, 18, 19 };
		System.out.println(getMinLength(arr));

	}

}
