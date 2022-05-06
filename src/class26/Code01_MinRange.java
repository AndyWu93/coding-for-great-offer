package class26;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

/**
 * You have k lists of sorted integers in non-decreasing order. Find the smallest range that includes at least one number from each of the k lists.
 *
 * We define the range [a, b] is smaller than range [c, d] if b - a < d - c or a < c if b - a == d - c.
 * 你有k个非递减排列的整数列表。找到一个最小区间，使得k个列表中的每个列表至少有一个数包含在其中
 * 我们定义如果 b-a < d-c 或者在 b-a == d-c 时 a < c，则区间 [a,b] 比 [c,d] 小。
 *
 * 题意：给定了k条有序的list，求k条list中的最小区间，该区间必须包含每个list中的至少一个元素
 * 解题：
 * 	思路：
 * 	假设先把k条list的头取出来，这一定能组成一个区间，而且区间里包含了每个list中的至少一个元素
 * 	这个区间该如何缩小呢？肯定是让最小值变大，或者最大值变小。
 * 	这里因为都是有序list的头元素，最大值不可能变小（假设有一个list的头特别大，再小就不能够包含这条数组了），只能让最小值变大
 * 	所以最小值来到下一个元素，继续形成一个区间，与之前的区间比较
 *
 * 流程：
 * 	先k个arr都拿出0位置的值，组成一个结构orderSet，
 * 	orderSet中拿出最大值最小值出来组成一个区间a，a含义：k个列表中都包含至少一个数，且必须以a[0]开头的区间是哪个
 * 	弹出a[0],定位a[0]来自arr，找到该arr中a[0]后面一个数，放入orderSet中，
 * 	重新计算最大值和最小值，组成区间b，b含义：k个列表中都包含至少一个数，且必须以b[0]开头的区间是哪个
 * 	弹出b[0]
 * 	...
 * 	直到某次弹出一个数之后，这个数所在位置是某个arr的最后一个数，停止
 * 	区间a，b，c...这些收集最窄区间
 */
public class Code01_MinRange {

	// 本题为求最小包含区间
	// 测试链接 :
	// https://leetcode.com/problems/smallest-range-covering-elements-from-k-lists/
	/*区间里如何找到最大值和最小值，从而定义区间的大小？有序表*/
	public static class Node {
		/*值，排序1*/
		public int val;
		/*在哪个list里，排序2*/
		public int arr;
		/*在list中的位置，为了取得下一个数*/
		public int idx;

		public Node(int value, int arrIndex, int index) {
			val = value;
			arr = arrIndex;
			idx = index;
		}
	}

	public static class NodeComparator implements Comparator<Node> {

		@Override
		public int compare(Node a, Node b) {
			/*每个list中只有一个元素进有序表，所以即使值一样，arr一定不一样*/
			return a.val != b.val ? (a.val - b.val) : (a.arr - b.arr);
		}

	}

	public static int[] smallestRange(List<List<Integer>> nums) {
		int N = nums.size();
		TreeSet<Node> set = new TreeSet<>(new NodeComparator());
		for (int i = 0; i < N; i++) {
			/*把每个list的头放进有序表*/
			set.add(new Node(nums.get(i).get(0), i, 0));
		}
		/*区间长度*/
		int r = Integer.MAX_VALUE;
		/*区间首位*/
		int a = 0;
		int b = 0;
		/*当size<N,表明有list走到尾了，没有元素了*/
		while (set.size() == N) {
			Node max = set.last();
			Node min = set.pollFirst();
			/*
			* 什么时候更新？只有找到一个更小的区间时再更新，找到相等的区间不更新，
			* 这样就能符合题目中区间相等时比较区间首元素的值
			* */
			if (max.val - min.val < r) {
				r = max.val - min.val;
				a = min.val;
				b = max.val;
			}
			/*找到min所在list的下一个，放入set，没有下一个了就不放了*/
			if (min.idx < nums.get(min.arr).size() - 1) {
				set.add(new Node(nums.get(min.arr).get(min.idx + 1), min.arr, min.idx + 1));
			}
		}
		return new int[] { a, b };
	}

	// 以下为课堂题目，在main函数里有对数器
	public static int minRange1(int[][] m) {
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < m[0].length; i++) {
			for (int j = 0; j < m[1].length; j++) {
				for (int k = 0; k < m[2].length; k++) {
					min = Math.min(min,
							Math.abs(m[0][i] - m[1][j]) + Math.abs(m[1][j] - m[2][k]) + Math.abs(m[2][k] - m[0][i]));
				}
			}
		}
		return min;
	}

	public static int minRange2(int[][] matrix) {
		int N = matrix.length;
		TreeSet<Node> set = new TreeSet<>(new NodeComparator());
		for (int i = 0; i < N; i++) {
			set.add(new Node(matrix[i][0], i, 0));
		}
		int min = Integer.MAX_VALUE;
		while (set.size() == N) {
			min = Math.min(min, set.last().val - set.first().val);
			Node cur = set.pollFirst();
			if (cur.idx < matrix[cur.arr].length - 1) {
				set.add(new Node(matrix[cur.arr][cur.idx + 1], cur.arr, cur.idx + 1));
			}
		}
		return min << 1;
	}

	public static int[][] generateRandomMatrix(int n, int v) {
		int[][] m = new int[3][];
		int s = 0;
		for (int i = 0; i < 3; i++) {
			s = (int) (Math.random() * n) + 1;
			m[i] = new int[s];
			for (int j = 0; j < s; j++) {
				m[i][j] = (int) (Math.random() * v);
			}
			Arrays.sort(m[i]);
		}
		return m;
	}

	public static void main(String[] args) {
		int n = 20;
		int v = 200;
		int t = 1000000;
		System.out.println("测试开始");
		for (int i = 0; i < t; i++) {
			int[][] m = generateRandomMatrix(n, v);
			int ans1 = minRange1(m);
			int ans2 = minRange2(m);
			if (ans1 != ans2) {
				System.out.println("出错了!");
			}
		}
		System.out.println("测试结束");
	}

}
