package class37;

/**
 * 给你二叉树的根结点 root ，请你将它展开为一个单链表：
 *
 * 展开后的单链表应该同样使用 TreeNode ，其中 right 子指针指向链表中下一个结点，而左子指针始终为 null 。
 * 展开后的单链表应该与二叉树 先序遍历 顺序相同。
 *
 * 解题：
 * 	本题思路与下面这题相同
 * @see class10.Code04_BSTtoDoubleLinkedList 这个是转双向链表
 *
 *
 */
// 注意，我们课上讲了一个别的题，并不是leetcode 114
// 我们课上讲的是，把一棵搜索二叉树变成有序链表，怎么做
// 而leetcode 114是，把一棵树先序遍历的结果串成链表
// 所以我更新了代码，这个代码是leetcode 114的实现
// 利用morris遍历
//https://leetcode-cn.com/problems/flatten-binary-tree-to-linked-list/
public class Problem_0114_FlattenBinaryTreeToLinkedList {

	// 这个类不用提交
	public static class TreeNode {
		public int val;
		public TreeNode left;
		public TreeNode right;

		public TreeNode(int value) {
			val = value;
		}
	}

	// 普通解
	public static void flatten1(TreeNode root) {
		process(root);
	}

	public static class Info {
		public TreeNode head;
		public TreeNode tail;

		public Info(TreeNode h, TreeNode t) {
			head = h;
			tail = t;
		}
	}

	public static Info process(TreeNode head) {
		if (head == null) {
			return null;
		}
		Info leftInfo = process(head.left);
		Info rightInfo = process(head.right);
		/*拿到信息后，按照头左右的顺序，建立好单链表*/
		head.left = null;
		head.right = leftInfo == null ? null : leftInfo.head;
		TreeNode tail = leftInfo == null ? head : leftInfo.tail;
		tail.right = rightInfo == null ? null : rightInfo.head;
		tail = rightInfo == null ? tail : rightInfo.tail;
		return new Info(head, tail);
	}

	// Morris遍历的解
	public static void flatten2(TreeNode root) {
		if (root == null) {
			return;
		}
		TreeNode pre = null;
		TreeNode cur = root;
		TreeNode mostRight = null;
		while (cur != null) {
			mostRight = cur.left;
			if (mostRight != null) {
				while (mostRight.right != null && mostRight.right != cur) {
					mostRight = mostRight.right;
				}
				if (mostRight.right == null) {
					mostRight.right = cur;
					if (pre != null) {
						pre.left = cur;
					}
					pre = cur;
					cur = cur.left;
					continue;
				} else {
					mostRight.right = null;
				}
			} else {
				if (pre != null) {
					pre.left = cur;
				}
				pre = cur;
			}
			cur = cur.right;
		}
		cur = root;
		TreeNode next = null;
		while (cur != null) {
			next = cur.left;
			cur.left = null;
			cur.right = next;
			cur = next;
		}
	}

}
