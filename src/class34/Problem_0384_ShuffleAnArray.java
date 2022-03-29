package class34;

/**
 * Given an integer array nums, design an algorithm to randomly shuffle the array. All permutations of the array should be equally likely as a result of the shuffling.
 *
 * Implement the Solution class:
 *
 * Solution(int[] nums) Initializes the object with the integer array nums.
 * int[] reset() Resets the array to its original configuration and returns it.
 * int[] shuffle() Returns a random shuffling of the array.
 * 题意：把牌打乱
 * 解题：
 * 	从arr[0..n-1]随机抽取一个数，与n-1位置交换
 * 	从arr[0..n-2]随机抽取一个数，与n-2位置交换
 * 	...
 */
public class Problem_0384_ShuffleAnArray {

	class Solution {
		private int[] origin;
		private int[] shuffle;
		private int N;

		public Solution(int[] nums) {
			origin = nums;
			N = nums.length;
			shuffle = new int[N];
			for (int i = 0; i < N; i++) {
				shuffle[i] = origin[i];
			}
		}

		public int[] reset() {
			return origin;
		}

		public int[] shuffle() {
			for (int i = N - 1; i >= 0; i--) {
				int r = (int) (Math.random() * (i + 1));
				int tmp = shuffle[r];
				shuffle[r] = shuffle[i];
				shuffle[i] = tmp;
			}
			return shuffle;
		}
	}

}
