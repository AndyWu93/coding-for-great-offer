package class33;

import class14.Code06_MissingNumber;

/**
 * Given an unsorted integer array nums, return the smallest missing positive integer.
 *
 * You must implement an algorithm that runs in O(n) time and uses constant extra space.
 *
 * 整体思路：将arr数组调整为如下规则：arr[i]=i
 *
 * @see Code06_MissingNumber
 */
public class Problem_0268_MissingNumber {

	public static int missingNumber(int[] arr) {
		int l = 0;
		int r = arr.length;
		while (l < r) {
			if (arr[l] == l) {
				l++;
			} else if (arr[l] < l || arr[l] >= r || arr[arr[l]] == arr[l]) {
				swap(arr, l, --r);
			} else {
				swap(arr, l, arr[l]);
			}
		}
		return l;
	}

	public static void swap(int[] arr, int i, int j) {
		int tmp = arr[i];
		arr[i] = arr[j];
		arr[j] = tmp;
	}

}
