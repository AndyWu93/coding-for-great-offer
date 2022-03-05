package class30;

/**
 * You are given two integer arrays nums1 and nums2, sorted in non-decreasing order, and two integers m and n, representing the number of elements in nums1 and nums2 respectively.
 *
 * Merge nums1 and nums2 into a single array sorted in non-decreasing order.
 *
 * The final sorted array should not be returned by the function, but instead be stored inside the array nums1. To accommodate this, nums1 has a length of m + n, where the first m elements denote the elements that should be merged, and the last n elements are set to 0 and should be ignored. nums2 has a length of n.
 *
 * 题意：有序的小数组与有序的大数组，各自取前面若干个元素，合并，要求额外空间复杂度O(1)
 * 解题：
 * 	归并，都生成到大数组里
 * 	利用大数组空间时需要注意，尽量释放大数组空间，所以遇到相等的先拷贝大数组的元素
 *
 */
public class Problem_0088_MergeSortedArray {

	public static void merge(int[] nums1, int m, int[] nums2, int n) {
		int index = nums1.length;
		while (m > 0 && n > 0) {
			if (nums1[m - 1] >= nums2[n - 1]) {
				nums1[--index] = nums1[--m];
			} else {
				nums1[--index] = nums2[--n];
			}
		}
		while (m > 0) {
			nums1[--index] = nums1[--m];
		}
		while (n > 0) {
			nums1[--index] = nums2[--n];
		}
	}

}
