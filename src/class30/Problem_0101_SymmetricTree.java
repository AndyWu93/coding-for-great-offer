package class30;

/**
 * Given the root of a binary tree, check whether it is a mirror of itself (i.e., symmetric around its center).
 * 题意：判断一棵二叉树是否为镜像
 * 解题：
 * 	递归思维
 */
public class Problem_0101_SymmetricTree {

	public static class TreeNode {
		int val;
		TreeNode left;
		TreeNode right;
	}

	public boolean isSymmetric(TreeNode root) {
		return isMirror(root, root);
	}

	// 一棵树是原始树  head1
	// 另一棵是翻面树  head2
	public static boolean isMirror(TreeNode head1, TreeNode head2) {
		if (head1 == null && head2 == null) {
			return true;
		}
		if (head1 != null && head2 != null) {
			return head1.val == head2.val 
					&& isMirror(head1.left, head2.right) 
					&& isMirror(head1.right, head2.left);
		}
		// 一个为空，一个不为空  false
		return false;
	}

}
