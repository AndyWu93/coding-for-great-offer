package class34;

/**
 * Given an array of integers nums containing n + 1 integers where each integer is in the range [1, n] inclusive.
 *
 * There is only one repeated number in nums, return this repeated number.
 *
 * You must solve the problem without modifying the array nums and uses only constant extra space.
 * 题意：数组arr中包含n+1个数，每个数的范围都是[1,n]，所以必定有一个数字的重复的，找到并返回该重复数字
 * 解题：
 * 	将数组想象成单链表结构，即每个数字是一个节点，节点的next指针指向当前节点值作为索引所在的位置
 * 	这样，这个单链表一定是一个有环的单lb，有环单链表如何求入环节点？
 */
public class Problem_0287_FindTheDuplicateNumber {

	public static int findDuplicate(int[] nums) {
		if (nums == null || nums.length < 2) {
			return -1;
		}
		int slow = nums[0];
		int fast = nums[nums[0]];
		while (slow != fast) {
			slow = nums[slow];
			fast = nums[nums[fast]];
		}
		fast = 0;
		while (slow != fast) {
			fast = nums[fast];
			slow = nums[slow];
		}
		return slow;
	}

}
