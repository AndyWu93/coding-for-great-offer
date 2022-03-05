package class30;

import java.util.HashMap;

/**
 * Given two integer arrays preorder and inorder where preorder is the preorder traversal of a binary tree and inorder is the inorder traversal of the same tree, construct and return the binary tree.
 * 题意：根据先序遍历和中序遍历生成二叉树
 * 解题：
 * 	本题使用递归，建立头节点，左树，右树
 * 	先序第一个数一定是头节点，中序头节点左边就是左树，右边就是右树
 * 	再通过中序中的左树右树定位先序中的左树和右树，建立节点
 */
//测试链接：https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal
public class Problem_0105_ConstructBinaryTreeFromPreorderAndInorderTraversal {

	public static class TreeNode {
		int val;
		TreeNode left;
		TreeNode right;

		TreeNode(int val) {
			this.val = val;
		}
	}

	public static TreeNode buildTree1(int[] pre, int[] in) {
		if (pre == null || in == null || pre.length != in.length) {
			return null;
		}
		return f(pre, 0, pre.length - 1, in, 0, in.length - 1);
	}

	// 有一棵树，先序结果是pre[L1...R1]，中序结果是in[L2...R2]
	// 请建出整棵树返回头节点
	public static TreeNode f(int[] pre, int L1, int R1, int[] in, int L2, int R2) {
		/*
		* 这个是为了处理空树的情况，比如
		*     1
		*      \
		* 		2
		* 		 \
		* 		  3
		* 这棵数在递归的过程中会出现类似f(pre,2,1...)的情况，就直接返回null表示是个空树
		* */
		if (L1 > R1) {
			return null;
		}
		TreeNode head = new TreeNode(pre[L1]);
		if (L1 == R1) {
			/*范围上只有一个数，一定就是头节点*/
			return head;
		}
		/*
		* 找到in中的头，头就是pre[L]
		* 这里用的遍历查找，也可以一开始就将in中的每个数建立索引，查找的时候直接查询，代码见buildTree2
		* */
		int find = L2;
		while (in[find] != pre[L1]) {
			find++;
		}
		/*找到了头，分别划出左树右树，递归建立去*/
		head.left = f(pre, L1 + 1, L1 + find - L2, in, L2, find - 1);
		head.right = f(pre, L1 + find - L2 + 1, R1, in, find + 1, R2);
		return head;
	}

	public static TreeNode buildTree2(int[] pre, int[] in) {
		if (pre == null || in == null || pre.length != in.length) {
			return null;
		}
		HashMap<Integer, Integer> valueIndexMap = new HashMap<>();
		for (int i = 0; i < in.length; i++) {
			valueIndexMap.put(in[i], i);
		}
		return g(pre, 0, pre.length - 1, in, 0, in.length - 1, valueIndexMap);
	}

	// 有一棵树，先序结果是pre[L1...R1]，中序结果是in[L2...R2]
	// 请建出整棵树返回头节点
	public static TreeNode g(int[] pre, int L1, int R1, int[] in, int L2, int R2,
			HashMap<Integer, Integer> valueIndexMap) {
		if (L1 > R1) {
			return null;
		}
		TreeNode head = new TreeNode(pre[L1]);
		if (L1 == R1) {
			return head;
		}
		int find = valueIndexMap.get(pre[L1]);
		head.left = g(pre, L1 + 1, L1 + find - L2, in, L2, find - 1, valueIndexMap);
		head.right = g(pre, L1 + find - L2 + 1, R1, in, find + 1, R2, valueIndexMap);
		return head;
	}

}
