package class28;

/**
 * Given an array of integers nums sorted in non-decreasing order, find the starting and ending position of a given target value.
 *
 * If target is not found in the array, return [-1, -1].
 *
 * You must write an algorithm with O(log n) runtime complexity.
 * 题意：返回arr中target的位置范围，用数组形式返回
 * 题解：
 * 	借助函数：返回小于x的最右位置
 */
public class Problem_0034_FindFirstAndLastPositionOfElementInSortedArray {

	public static int[] searchRange(int[] nums, int target) {
		if (nums == null || nums.length == 0) {
			return new int[] { -1, -1 };
		}
		/*小于target的最右位置的下个数，如果不是target，说明arr中没有target*/
		int L = lessMostRight(nums, target) + 1;
		if (L == nums.length || nums[L] != target) {
			return new int[] { -1, -1 };
		}
		/*左边是L，右边是小于target+1的最右位置*/
		return new int[] { L, lessMostRight(nums, target + 1) };
	}

	/**
	 * 小于num的最右位置，用二分
	 */
	public static int lessMostRight(int[] arr, int num) {
		int L = 0;
		int R = arr.length - 1;
		int M = 0;
		int ans = -1;
		while (L <= R) {
			M = L + ((R - L) >> 1);
			if (arr[M] < num) {
				ans = M;
				L = M + 1;
			} else {
				R = M - 1;
			}
		}
		return ans;
	}

}
