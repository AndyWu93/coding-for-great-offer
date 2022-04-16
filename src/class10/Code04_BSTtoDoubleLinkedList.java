package class10;

/**
 * 给定一棵搜索二叉树头节点，转化成首尾相接的有序双向链表（节点都有两个方向的指针）
 * 解题
 * 	二叉树的递归套路
 * 	二叉树变双向链表，node节点可以复用
 * 	process(x): 返回以x为头的整颗树，串成链表后，返回头和尾
 */
// 本题测试链接 : https://leetcode.com/problems/convert-binary-search-tree-to-sorted-doubly-linked-list/
public class Code04_BSTtoDoubleLinkedList {

	// 提交时不要提交这个类
	public static class Node {
		public int value;
		public Node left;
		public Node right;

		public Node(int data) {
			this.value = data;
		}
	}

	// 提交下面的代码
	public static Node treeToDoublyList(Node head) {
		if (head == null) {
			return null;
		}
		Info allInfo = process(head);
		/*按题意，把头和尾串起来*/
		allInfo.end.right = allInfo.start;
		allInfo.start.left = allInfo.end;
		return allInfo.start;
	}

	public static class Info {
		public Node start;
		public Node end;

		public Info(Node start, Node end) {
			this.start = start;
			this.end = end;
		}
	}

	/**
	 * 以x为头的整颗树，串成链表后，返回头和尾
	 */
	public static Info process(Node X) {
		if (X == null) {
			return new Info(null, null);
		}
		Info lInfo = process(X.left);
		Info rInfo = process(X.right);
		if (lInfo.end != null) {
			/*左树的尾指向x*/
			lInfo.end.right = X;
		}
		/*x的左指回左树的尾*/
		X.left = lInfo.end;
		/*x的右指向右树的头*/
		X.right = rInfo.start;
		if (rInfo.start != null) {
			/*右树的头指回x*/
			rInfo.start.left = X;
		}
		// 整体链表的头    lInfo.start != null ? lInfo.start : X
		// 整体链表的尾    rInfo.end != null ? rInfo.end : X
		return new Info(lInfo.start != null ? lInfo.start : X, rInfo.end != null ? rInfo.end : X);
	}

}