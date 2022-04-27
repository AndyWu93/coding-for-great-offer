package class17;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Given an n x n matrix where each of the rows and columns is sorted in ascending order, return the kth smallest element in the matrix.
 *
 * Note that it is the kth smallest element in the sorted order, not the kth distinct element.
 *
 * You must find a solution with a memory complexity better than O(n2).
 * 题意：在一个行列有序，整体不一定有序的matrix中找到第k小的数
 * 解题：
 * 	思路：
 * 	对matrix的最大值和最小值之间二分，每次二分得到的数a，在matrix查一下小于等于a的数有几个
 * 	如果小于k个，再往右侧去二分
 * 	如果大于等于k个，再往左侧去二分
 */
// 本题测试链接 : https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/
public class Code02_KthSmallestElementInSortedMatrix {

	// 堆的方法
	public static int kthSmallest1(int[][] matrix, int k) {
		int N = matrix.length;
		int M = matrix[0].length;
		PriorityQueue<Node> heap = new PriorityQueue<>(new NodeComparator());
		boolean[][] set = new boolean[N][M];
		heap.add(new Node(matrix[0][0], 0, 0));
		set[0][0] = true;
		int count = 0;
		Node ans = null;
		while (!heap.isEmpty()) {
			ans = heap.poll();
			if (++count == k) {
				break;
			}
			int row = ans.row;
			int col = ans.col;
			if (row + 1 < N && !set[row + 1][col]) {
				heap.add(new Node(matrix[row + 1][col], row + 1, col));
				set[row + 1][col] = true;
			}
			if (col + 1 < M && !set[row][col + 1]) {
				heap.add(new Node(matrix[row][col + 1], row, col + 1));
				set[row][col + 1] = true;
			}
		}
		return ans.value;
	}

	public static class Node {
		public int value;
		public int row;
		public int col;

		public Node(int v, int r, int c) {
			value = v;
			row = r;
			col = c;
		}

	}

	public static class NodeComparator implements Comparator<Node> {

		@Override
		public int compare(Node o1, Node o2) {
			return o1.value - o2.value;
		}

	}

	/**
	 * 最优解
	 */
	// 二分的方法
	public static int kthSmallest2(int[][] matrix, int k) {
		int N = matrix.length;
		int M = matrix[0].length;
		int left = matrix[0][0];
		int right = matrix[N - 1][M - 1];
		int ans = 0;
		/*取出最大值和最小值，在这中间二分*/
		while (left <= right) {
			int mid = left + ((right - left) >> 1);
			/*matrix中 <=mid 有几个； <= mid 在矩阵中真实出现的数，谁最接近mid*/
			Info info = noMoreNum(matrix, mid);
			if (info.num < k) {
				/*不足k个，说明二分的数小了，去右侧*/
				left = mid + 1;
			} else {
				/*记下此时最接近第k个的数*/
				ans = info.near;
				right = mid - 1;
			}
		}
		return ans;
	}

	public static class Info {
		public int near;
		public int num;

		public Info(int n1, int n2) {
			near = n1;
			num = n2;
		}
	}

	/*
	* matrix中 <=value 有几个； <= value 在矩阵中真实出现的数，谁最接近value
	* */
	public static Info noMoreNum(int[][] matrix, int value) {
		/*矩阵里小于等于value的数中，最接近value的数*/
		int near = Integer.MIN_VALUE;
		/*矩阵里小于等于value的数的个数*/
		int num = 0;
		int N = matrix.length;
		int M = matrix[0].length;
		int row = 0;
		int col = M - 1;
		/*m[i][j]从右上角开始，与value比对*/
		while (row < N && col >= 0) {
			if (matrix[row][col] <= value) {
				/*
				* 小于等于value：结算m[i][j]左边的所有数，因为左边的数一定比m[i][j]小，也就一定比value小
				* 结算的时候记录一下次此时的m[i][j],因为m[i][j]可能是小于等于value中最大的数
				* */
				near = Math.max(near, matrix[row][col]);
				num += col + 1;
				/*结算之后向下看*/
				row++;
			} else {
				/*m[i][j]大于value：不符合条件了，该方法求的是小于等于value的数有几个，所以往左边看*/
				col--;
			}
		}
		return new Info(near, num);
	}

}
