package class32;

/**
 * Given an array, rotate the array to the right by k steps, where k is non-negative.
 * 题意：将数组向右平移k步
 * 解题：
 * 	将3个部分逆序，自动实现了数组的平移。
 *
 */
public class Problem_0189_RotateArray {

	public void rotate1(int[] nums, int k) {
		int N = nums.length;
		k = k % N;
		/*将要被平移到右边的数*/
		reverse(nums, 0, N - k - 1);
		/*后面k个数被挤到前面去的*/
		reverse(nums, N - k, N - 1);
		/*整体*/
		reverse(nums, 0, N - 1);
	}

	/*
	* 如何逆序？两个指针首位交换
	* */
	public static void reverse(int[] nums, int L, int R) {
		while (L < R) {
			int tmp = nums[L];
			nums[L++] = nums[R];
			nums[R--] = tmp;
		}
	}

	public static void rotate2(int[] nums, int k) {
		int N = nums.length;
		k = k % N;
		if (k == 0) {
			return;
		}
		int L = 0;
		int R = N - 1;
		int lpart = N - k;
		int rpart = k;
		int same = Math.min(lpart, rpart);
		int diff = lpart - rpart;
		exchange(nums, L, R, same);
		while (diff != 0) {
			if (diff > 0) {
				L += same;
				lpart = diff;
			} else {
				R -= same;
				rpart = -diff;
			}
			same = Math.min(lpart, rpart);
			diff = lpart - rpart;
			exchange(nums, L, R, same);
		}
	}

	public static void exchange(int[] nums, int start, int end, int size) {
		int i = end - size + 1;
		int tmp = 0;
		while (size-- != 0) {
			tmp = nums[start];
			nums[start] = nums[i];
			nums[i] = tmp;
			start++;
			i++;
		}
	}

}
