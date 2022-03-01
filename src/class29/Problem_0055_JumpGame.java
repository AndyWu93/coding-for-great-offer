package class29;

/**
 * You are given an integer array nums. You are initially positioned at the array's first index, and each element in the array represents your maximum jump length at that position.
 *
 * Return true if you can reach the last index, or false otherwise.
 *
 * 题意：
 * 	给定一个非负整数数组 nums ，你最初位于数组的 第一个下标 。
 * 	数组中的每个元素代表你在该位置可以跳跃的最大长度。
 * 	判断你是否能够到达最后一个下标。
 *
 */
public class Problem_0055_JumpGame {

	public static boolean canJump(int[] nums) {
		if (nums == null || nums.length < 2) {
			return true;
		}
		/*能够到达的最远距离*/
		int max = nums[0];
		for (int i = 1; i < nums.length; i++) {
//			if (max >= nums.length - 1) {
//				return true;
//			}
			if (i > max) {
				return false;
			}
			max = Math.max(max, i + nums[i]);
		}
		return true;
	}

}
