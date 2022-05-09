package class37;

/**
 * 翻转一棵二叉树
 * 任何节点的左右两个孩子交换
 *
 * @see class30.Problem_0101_SymmetricTree 参考
 */
//Leetcode题目 : https://leetcode.com/problems/invert-binary-tree/
public class Problem_0226_InvertBinaryTree {

	public class TreeNode {
		public int val;
		public TreeNode left;
		public TreeNode right;
	}

	public static TreeNode invertTree(TreeNode root) {
		if (root == null) {
			return null;
		}
		TreeNode left = root.left;
		/*
		* 左树转后，给右指针
		* 右树转后，给左指针
		* */
		root.left = invertTree(root.right);
		root.right = invertTree(left);
		return root;
	}

}
