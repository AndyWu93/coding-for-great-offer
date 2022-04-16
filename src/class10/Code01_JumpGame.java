package class10;

/**
 * Given an array of non-negative integers nums, you are initially positioned at the first index of the array.
 *
 * Each element in the array represents your maximum jump length at that position.
 *
 * Your goal is to reach the last index in the minimum number of jumps.
 *
 * You can assume that you can always reach the last index.
 * 题意：给定一个正数arr，arr[i]：从当前位置最远能跳几步，问从0位置到n-1位置，最少需要跳多少步
 * 解题：
 * 	本题逐步推算，刷新右边界的思路。
 * 	用3个变量来推算结果
 * 		step:
 * 		cur: step步内能够到达的最远位置
 * 		next: step+1步能够到达的最远位置
 */
// 本题测试链接 : https://leetcode.com/problems/jump-game-ii/
public class Code01_JumpGame {

	public static int jump(int[] arr) {
		if (arr == null || arr.length == 0) {
			return 0;
		}
		int step = 0;
		int cur = 0;
		int next = 0;
		for (int i = 0; i < arr.length; i++) {
			/*
			* i: 当前的位置
			* cur<i:当前位置step步内到不了，所以step++，cur范围也扩展了，扩展多少？之前next已经算好了
			* */
			if (cur < i) {
				step++;
				cur = next;
			}
			/*目前i位置step步内是能到的，假设借用当前位置的的值再跳一步，能否把范围推的更远，能的话更新一下范围*/
			next = Math.max(next, i + arr[i]);
		}
		return step;
	}

}
