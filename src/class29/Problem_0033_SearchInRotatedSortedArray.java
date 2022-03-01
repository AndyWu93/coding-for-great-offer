package class29;

/**
 * There is an integer array nums sorted in ascending order (with distinct values).
 *
 * Prior to being passed to your function, nums is possibly rotated at an unknown pivot index k (1 <= k < nums.length) such that the resulting array is [nums[k], nums[k+1], ..., nums[n-1], nums[0], nums[1], ..., nums[k-1]] (0-indexed). For example, [0,1,2,4,5,6,7] might be rotated at pivot index 3 and become [4,5,6,7,0,1,2].
 *
 * Given the array nums after the possible rotation and an integer target, return the index of target if it is in nums, or -1 if it is not in nums.
 *
 * You must write an algorithm with O(log n) runtime complexity.
 * 题意：给定一个int有序数组arr，以某个任意支点旋转成为两个部分各自有序的arr，给定一个target，返回target下标
 * 解题：
 * 	本题使用二分，
 * 	对于本题，因为两部分各自有序，
 * 	二分的时候，需要先找到一个严格有序的半段，来定位target
 * 	能够找到一个严格有序的半段的条件：arr[L],arr[M],arr[R]不都相等，断点一定在某个半段中间，就能够定位区间了
 */
public class Problem_0033_SearchInRotatedSortedArray {

	// arr，原本是有序数组，旋转过，而且左部分长度不知道
	// 找num
	// num所在的位置返回
	public static int search(int[] arr, int num) {
		int L = 0;
		int R = arr.length - 1;
		int M = 0;
		while (L <= R) {
			// M = L + ((R - L) >> 1)
			M = (L + R) / 2;
			if (arr[M] == num) {
				/*中间这个数就是target，之间返回下标*/
				return M;
			}
			/*
			* arr[M] != num
			* [L] == [M] == [R] != num 无法二分
			* 那就右移L，直到打破都相等的格局，或者，L撞上了M，即L~M之间都排查完了，可以继续二分了
			* */
			if (arr[L] == arr[M] && arr[M] == arr[R]) {
				while (L != M && arr[L] == arr[M]) {
					L++;
				}
				/*
				* 跳出while后：
				* 1) L == M L...M 一路都相等，L撞上了M，即L~M之间都排查完了，可以继续二分了
				* 2) 从L到M终于找到了一个不等的位置
				* */
				if (L == M) {
					/*L...M 一路都相等，右侧继续二分*/
					L = M + 1;
					continue;
				}
			}
			/*
			* L到M之间找到了一个打破相等的位置
			* 该如何二分？注意此时arr[M] != num，因为最开始判断过了
			* 整体思路，需要找到一个严格递增区间，判断target是否在此区间内
			* */
			if (arr[L] != arr[M]) {
				if (arr[M] > arr[L]) {
					/*
					* arr[M] > arr[L]
					* 断点位置不可能再这个区间，可以举例证明
					* L...M 严格递增，就能够判断出target在哪一侧了
					* */
					if (num >= arr[L] && num < arr[M]) { //  3  [L] == 1    [M]   = 5   L...M - 1
						/*target在L..M中间，就在这里二分，否则就去另一侧二分*/
						R = M - 1;
					} else { // 9    [L] == 2    [M]   =  7   M... R
						L = M + 1;
					}
				} else { // [L] > [M]    L....M  存在断点
					/*
					* arr[M] < arr[L]
					* 断点位置一定在这个区间，可以举例证明
					* M...R 严格递增，就能够判断出target在哪一侧了
					* */
					if (num > arr[M] && num <= arr[R]) {
						L = M + 1;
					} else {
						R = M - 1;
					}
				}
			} else { /// [L] [M] [R] 不都一样，  [L] === [M] -> [M]!=[R]
				/*
				* 上面是arr[L]!=arr[M],这里往下是arr[M]!=arr[R]，
				* 同样可以找到严格递增的区间，判断target是否在这个区间
				* */
				if (arr[M] < arr[R]) {
					if (num > arr[M] && num <= arr[R]) {
						L = M + 1;
					} else {
						R = M - 1;
					}
				} else {
					if (num >= arr[L] && num < arr[M]) {
						R = M - 1;
					} else {
						L = M + 1;
					}
				}
			}
		}
		return -1;
	}

}
