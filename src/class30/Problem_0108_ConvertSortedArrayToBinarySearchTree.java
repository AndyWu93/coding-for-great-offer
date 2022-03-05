package class30;

/**
 * Given an integer array nums where the elements are sorted in ascending order, convert it to a height-balanced binary search tree.
 *
 * A height-balanced binary tree is a binary tree in which the depth of the two subtrees of every node never differs by more than one.
 * 题意：将有序数组转成平衡搜索二叉树
 * 解题：
 * 	递归的设计
 *
 */
public class Problem_0108_ConvertSortedArrayToBinarySearchTree {

	public static class TreeNode {
		int val;
		TreeNode left;
		TreeNode right;

		TreeNode(int val) {
			this.val = val;
		}
	}

	public TreeNode sortedArrayToBST(int[] nums) {
		return process(nums, 0, nums.length - 1);
	}


	/*
	* 将nums[L..R]转成平衡搜索二叉树，返回头节点
	* */
	public static TreeNode process(int[] nums, int L, int R) {
		if (L > R) {
			return null;
		}
		if (L == R) {
			return new TreeNode(nums[L]);
		}
		/*中点位置作头*/
		int M = (L + R) / 2;
		TreeNode head = new TreeNode(nums[M]);
		head.left = process(nums, L, M - 1);
		head.right = process(nums, M + 1, R);
		return head;
	}

}
