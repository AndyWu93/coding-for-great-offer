package class30;

/**
 * 校验是不是搜索二叉树
 * 这里采用morris遍历，保证中序下值上升即可
 */
public class Problem_0098_ValidateBinarySearchTree {

	public static class TreeNode {
		int val;
		TreeNode left;
		TreeNode right;
	}

	public boolean isValidBST(TreeNode root) {
		if (root == null) {
			return true;
		}
		TreeNode cur = root;
		TreeNode mostRight = null;
		Integer pre = null;
		boolean ans = true;
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
			if (pre != null && pre >= cur.val) {
				/*这里不能直接return，需要把Morris遍历跑完，因为遍历会改树的结构*/
				ans = false;
			}
			pre = cur.val;
			cur = cur.right;
		}
		return ans;
	}

}
