package class34;

import java.util.ArrayList;
import java.util.List;

/**
 *You are given an integer array nums and you have to return a new counts array. The counts array has the property where counts[i] is the number of smaller elements to the right of nums[i].
 *
 * Example 1:
 * Input: nums = [5,2,6,1]
 * Output: [2,1,1,0]
 * Explanation:
 * To the right of 5 there are 2 smaller elements (2 and 1).
 * To the right of 2 there is only 1 smaller element (1).
 * To the right of 6 there is 1 smaller element (1).
 * To the right of 1 there is 0 smaller element.
 *
 * 题意：收集数组arr中任意位置右边可以与之构成逆序对的数量，
 * 用List返回，arr[i]的结果：list.get(i)
 *
 * 解题：
 * 	参考之前求逆序对的总数，这个求每个数的逆序对，整体流程一样，结果收集方式不一样
 * 	收集技巧：可以将arr中的index和value作为节点参与归并和收集
 *
 * @see 体系学习班 class04.Code03_ReversePair
 */
public class Problem_0315_CountOfSmallerNumbersAfterSelf {

	public static class Node {
		public int value;
		public int index;

		public Node(int v, int i) {
			value = v;
			index = i;
		}
	}

	public static List<Integer> countSmaller(int[] nums) {
		List<Integer> ans = new ArrayList<>();
		if (nums == null) {
			return ans;
		}
		for (int i = 0; i < nums.length; i++) {
			ans.add(0);
		}
		if (nums.length < 2) {
			return ans;
		}
		Node[] arr = new Node[nums.length];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = new Node(nums[i], i);
		}
		process(arr, 0, arr.length - 1, ans);
		return ans;
	}

	public static void process(Node[] arr, int l, int r, List<Integer> ans) {
		if (l == r) {
			return;
		}
		int mid = l + ((r - l) >> 1);
		process(arr, l, mid, ans);
		process(arr, mid + 1, r, ans);
		merge(arr, l, mid, r, ans);
	}

	public static void merge(Node[] arr, int l, int m, int r, List<Integer> ans) {
		Node[] help = new Node[r - l + 1];
		int i = help.length - 1;
		int p1 = m;
		int p2 = r;
		while (p1 >= l && p2 >= m + 1) {
			if (arr[p1].value > arr[p2].value) {
				/*
				* 之前是计算数组中所有的逆序对的总数
				* 这里收集数组中单个数的逆序对数量：
				* 	统计方法：
				* 	1. 将数组变成node，为了在排序过程中记住该数字初始的index
				* 	2. 题目中需要返回ArrayList，收集时调用set(index,value),set的时候是累加set
				* */
				ans.set(arr[p1].index, ans.get(arr[p1].index) + p2 - m);
			}
			help[i--] = arr[p1].value > arr[p2].value ? arr[p1--] : arr[p2--];
		}
		while (p1 >= l) {
			help[i--] = arr[p1--];
		}
		while (p2 >= m + 1) {
			help[i--] = arr[p2--];
		}
		for (i = 0; i < help.length; i++) {
			arr[l + i] = help[i];
		}
	}

}
