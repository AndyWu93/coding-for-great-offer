package class12;


import java.util.HashMap;

/**
 * Given an unsorted array of integers nums, return the length of the longest consecutive elements sequence.
 *
 * You must write an algorithm that runs in O(n) time.
 * 题意：找出无序arr中的最长连续子序列长度
 * 解题：
 * 	可以用map来存储以arr[i]开头的最长连续子序列长度
 * 	遍历arr，遍历的过程中不断合并长度，并收集答案
 * 	一个map coding复杂且不好理解，可以用两个map，
 * 		一个map收集以arr[i]开头的最长连续子序列长度
 * 		一个map收集以arr[i]结尾的最长连续子序列长度
 *
 * @see class31.Problem_0128_LongestConsecutive
 */
// 本题测试链接 : https://leetcode.com/problems/longest-consecutive-sequence/
public class Code03_LongestConsecutive {

	/**
	 * 1个map的方法，不推荐
	 */
	public static int longestConsecutive(int[] nums) {
		/*
		* key: arr中的数a，
		* value：如果a是一个最长子序列的开头，即以a开头的最长连续子序列长度
		* value：如果a是一个最长子序列的中间位置，即该序列到自己的长度
		* */
		HashMap<Integer, Integer> map = new HashMap<>();
		int len = 0;
		for (int num : nums) {
			/*
			 * 假设存在[5,9]的连续子序列
			 * map中会出现
			 * （5，5）
			 * （6，2）
			 * （7，3）
			 * （8，4）
			 * （9，5）
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

	public static void main(String[] args) {
		int[] nums = {5,6,7,8,9};
		longestConsecutive(nums);
	}


}
