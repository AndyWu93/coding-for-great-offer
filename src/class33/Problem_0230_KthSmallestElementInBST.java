package class33;

import java.util.Stack;

/**
 * Given the root of a binary search tree, and an integer k, return the kth smallest value (1-indexed) of all the values of the nodes in the tree.
 */
public class Problem_0230_KthSmallestElementInBST {

	public static class TreeNode {
		public int val;
		public TreeNode left;
		public TreeNode right;
	}


	public int kthSmallest1(TreeNode root, int k) {
		TreeNode cur =root;
		int index=0;
		Stack<TreeNode> stack = new Stack<>();
		while(cur!=null || !stack.isEmpty()){
			if(cur!=null){
				stack.push(cur);
				cur=cur.left;
			}else{
				cur = stack.pop();
				index++;
				if(index==k){
					return cur.val;
				}
				cur= cur.right;
			}

		}
		return -1;
	}



	public static int kthSmallest(TreeNode head, int k) {
		if (head == null) {
			return -1;
		}
		TreeNode cur = head;
		TreeNode mostRight = null;
		int index = 1;
		while (cur != null) {
			mostRight = cur.left;
			if (mostRight != null) {
				while (mostRight.right != null && mostRight.right != cur) {
					mostRight = mostRight.right;
				}
				if (mostRight.right == null) {
					mostRight.right = cur;
					cur = cur.left;
					continue;
				} else {
					mostRight.right = null;
				}
			}
			if (index++ == k) {
				return cur.val;
			}
			cur = cur.right;
		}
		return -1;
	}

}
