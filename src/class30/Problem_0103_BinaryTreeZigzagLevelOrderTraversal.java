package class30;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Given the root of a binary tree, return the zigzag level order traversal of its nodes' values. (i.e., from left to right, then right to left for the next level and alternate between).
 * 题意：zigzag序打印二叉树。zigzag？宽度优先遍历下交替方向
 */
public class Problem_0103_BinaryTreeZigzagLevelOrderTraversal {

	public static class TreeNode {
		int val;
		TreeNode left;
		TreeNode right;
	}

	public static List<List<Integer>> zigzagLevelOrder(TreeNode root) {
		List<List<Integer>> ans = new ArrayList<>();
		if (root == null) {
			return ans;
		}
		LinkedList<TreeNode> deque = new LinkedList<>();
		deque.add(root);
		/*这一层的大小*/
		int size = 0;
		boolean isHead = true;
		while (!deque.isEmpty()) {
			size = deque.size();
			List<Integer> curLevel = new ArrayList<>();
			/*把这一层全部处理完毕*/
			for (int i = 0; i < size; i++) {
				/*
				* 根据当前层顺序决定
				* 是从前面拿出，后面加入
				* 还是从后面拿出，前面加入
				* */
				TreeNode cur = isHead ? deque.pollFirst() : deque.pollLast();
				curLevel.add(cur.val);
				if(isHead) {
					if (cur.left != null) {
						deque.addLast(cur.left);
					}
					if (cur.right != null) {
						deque.addLast(cur.right);
					}
				}else {
					if (cur.right != null) {
						deque.addFirst(cur.right);
					}
					if (cur.left != null) {
						deque.addFirst(cur.left);
					}
				}
			}
			ans.add(curLevel);
			isHead = !isHead;
		}
		return ans;
	}

}
