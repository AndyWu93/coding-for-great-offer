package class31;

import java.util.HashMap;

/**
 * Given an unsorted array of integers nums, return the length of the longest consecutive elements sequence.
 *
 * You must write an algorithm that runs in O(n) time.
 * 题意：找出无序arr中的最长连续子序列长度
 * 解题：
 * 	可以用map来存储以arr[i]开头的最长连续子序列长度
 * 	遍历arr，遍历的过程中不断合并长度，并收集答案
 */
// 本题测试链接 : https://leetcode.com/problems/longest-consecutive-sequence/
public class Problem_0128_LongestConsecutive {

	public static int longestConsecutive(int[] nums) {
		/*
		* key: arr中的数a，value：以a开头的最长连续子序列长度
		* 这张表会对每个数都会保留一份从自己出发，最长的连续子序列长度
		* */
		HashMap<Integer, Integer> map = new HashMap<>();
		int len = 0;
		for (int num : nums) {
			/*
			 * 假设存在[5,9]的连续子序列
			 * map中会出现
			 * （5，5）
			 * （6，4）
			 * （7，3）
			 * （8，2）
			 * （9，1）
			 * 所以需要!map.containsKey(num)这个条件
			 * */
			if (!map.containsKey(num)) {
				map.put(num, 1);
				int preLen = map.containsKey(num - 1) ? map.get(num - 1) : 0;
				int posLen = map.containsKey(num + 1) ? map.get(num + 1) : 0;
				int all = preLen + posLen + 1;
				map.put(num - preLen, all);
				map.put(num + posLen, all);
				len = Math.max(len, all);
			}
		}
		return len;
	}



}
