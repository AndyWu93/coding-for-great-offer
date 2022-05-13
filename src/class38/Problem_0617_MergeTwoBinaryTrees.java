package class38;

/**
 * 给定两个二叉树，想象当你将它们中的一个覆盖到另一个上时，两个二叉树的一些节点便会重叠。
 * 你需要将他们合并为一个新的二叉树。合并的规则是如果两个节点重叠，那么将他们的值相加作为节点合并后的新值，否则不为 NULL 的节点将直接作为新二叉树的节点。
 *
 * 解题：
 * 	二叉树递归
 */
//Leetcode题目 : https://leetcode.com/problems/merge-two-binary-trees/
public class Problem_0617_MergeTwoBinaryTrees {

	public static class TreeNode {
		public int val;
		public TreeNode left;
		public TreeNode right;

		public TreeNode(int val) {
			this.val = val;
		}
	}

	// 当前，一棵树的头是t1，另一颗树的头是t2
	// 请返回，整体merge之后的头
	public static TreeNode mergeTrees(TreeNode t1, TreeNode t2) {
		if (t1 == null) {
			/*t1是空返回t2,因为空不用merge了*/
			return t2;
		}
		if (t2 == null) {
			return t1;
		}
		// t1和t2都不是空
		TreeNode merge = new TreeNode(t1.val + t2.val);
		merge.left = mergeTrees(t1.left, t2.left);
		merge.right = mergeTrees(t1.right, t2.right);
		return merge;
	}

}
