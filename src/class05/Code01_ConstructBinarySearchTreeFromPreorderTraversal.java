package class05;

import java.util.Stack;

/**
 * 已知一棵搜索二叉树上没有重复值的节点，现在有一个数组arr，是这棵搜索二叉树先序遍历的结果，请根据arr生成整棵树并返回头节点
 * 题意：给定一个BST先序遍历的数组，将其反序列化
 * 解题：二叉树反序列化基本功，带点小技巧即可
 */
// 本题测试链接 : https://leetcode.com/problems/construct-binary-search-tree-from-preorder-traversal/
public class Code01_ConstructBinarySearchTreeFromPreorderTraversal {

	// 不用提交这个类
	public static class TreeNode {
		public int val;
		public TreeNode left;
		public TreeNode right;

		public TreeNode() {
		}

		public TreeNode(int val) {
			this.val = val;
		}

		public TreeNode(int val, TreeNode left, TreeNode right) {
			this.val = val;
			this.left = left;
			this.right = right;
		}
	}

	/**
	 * 常规解，O(N^2)已经很优秀了
	 */
	public static TreeNode bstFromPreorder1(int[] pre) {
		if (pre == null || pre.length == 0) {
			return null;
		}
		return process1(pre, 0, pre.length - 1);
	}

	/*
	* pre中L..R是一个BST的先序遍历，建好二叉树返回head
	* */
	public static TreeNode process1(int[] pre, int L, int R) {
		if (L > R) {
			/*这里表示范围上已经没数了，因为返回一个空节点*/
			return null;
		}
		/*
		* 因为中序，L位置肯定是head，接下来，直到第一个一个大于head的数之前都是左树，为什么？因为bst
		* firstBig：通过从L+1遍历找到第一个大于L位置的数
		* */
		int firstBig = L + 1;
		for (; firstBig <= R; firstBig++) {
			if (pre[firstBig] > pre[L]) {
				break;
			}
		}
		TreeNode head = new TreeNode(pre[L]);
		/*如果L后面都是大于它的数，firstBig=L+1，下面process就会返回空节点*/
		head.left = process1(pre, L + 1, firstBig - 1);
		head.right = process1(pre, firstBig, R);
		return head;
	}

	/**
	 * 最优解O(N)
	 * 找任意一个位置i后面第一个大于它的数，可以用单调栈提前生成，单调栈复杂度O(N)
	 */
	// 已经是时间复杂度最优的方法了，但是常数项还能优化
	public static TreeNode bstFromPreorder2(int[] pre) {
		if (pre == null || pre.length == 0) {
			return null;
		}
		int N = pre.length;
		int[] nearBig = new int[N];
		for (int i = 0; i < N; i++) {
			nearBig[i] = -1;
		}
		Stack<Integer> stack = new Stack<>();
		for (int i = 0; i < N; i++) {
			while (!stack.isEmpty() && pre[stack.peek()] < pre[i]) {
				nearBig[stack.pop()] = i;
			}
			stack.push(i);
		}
		return process2(pre, 0, N - 1, nearBig);
	}

	public static TreeNode process2(int[] pre, int L, int R, int[] nearBig) {
		if (L > R) {
			return null;
		}
		int firstBig = (nearBig[L] == -1 || nearBig[L] > R) ? R + 1 : nearBig[L];
		TreeNode head = new TreeNode(pre[L]);
		head.left = process2(pre, L + 1, firstBig - 1, nearBig);
		head.right = process2(pre, firstBig, R, nearBig);
		return head;
	}

	/**
	 * 单调栈用数组实现
	 */
	// 最优解
	public static TreeNode bstFromPreorder3(int[] pre) {
		if (pre == null || pre.length == 0) {
			return null;
		}
		int N = pre.length;
		int[] nearBig = new int[N];
		for (int i = 0; i < N; i++) {
			nearBig[i] = -1;
		}
		int[] stack = new int[N];
		int size = 0;
		for (int i = 0; i < N; i++) {
			while (size != 0 && pre[stack[size - 1]] < pre[i]) {
				nearBig[stack[--size]] = i;
			}
			stack[size++] = i;
		}
		return process3(pre, 0, N - 1, nearBig);
	}

	public static TreeNode process3(int[] pre, int L, int R, int[] nearBig) {
		if (L > R) {
			return null;
		}
		int firstBig = (nearBig[L] == -1 || nearBig[L] > R) ? R + 1 : nearBig[L];
		TreeNode head = new TreeNode(pre[L]);
		head.left = process3(pre, L + 1, firstBig - 1, nearBig);
		head.right = process3(pre, firstBig, R, nearBig);
		return head;
	}

}
