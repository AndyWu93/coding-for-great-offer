package class42;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * 给定一棵搜索二叉树的头节点root，搜索二叉树默认是没有重复值的，给定一个double类型的数target，给定一个正数k
 * 请返回在这棵二叉树里最接近target的k个值，作为链表返回
 * 要求：如果二叉树的节点个数为n，而k相比于n较小的话，实现时间复杂度低于O(n)的方法
 *
 * 解题：
 * 	要求复杂度低于O(n)，遍历别想了，所以，很快能想到
 * 	先找到bst中离target最近的两个节点，即大于target里最小的节点a，小于target里最大的节点b
 * 	ab这两个节点作为此时的候选，比较一下，收集候选里离target近的节点，
 * 	如果收集的是a，a的后继节点列为候选，再次比较收集
 * 	如果收集的是b，b的前驱节点列为候选，再次比较收集
 * 	所以，本题难点：如果快速获取一个节点的前驱节点、后继节点；以及它前驱的前驱、后继的后继
 *
 * 重要概念：
 * 	前驱节点：中序遍历下，x节点的前面一个节点，称为x的前驱节点
 * 位置：
 * 	x节点有左树：x左树的最右节点
 * 	x节点无左树：x的第一个以右孩子作为身份的曾曾曾...祖父
 *
 * 	后继节点：中序遍历下，x节点的后面一个节点，称为x的后继节点
 * 位置：
 * 	x节点有右树：x右树的最左节点
 * 	x节点无右树：x的第一个以左孩子作为身份的曾曾曾...祖父
 *
 */
//leetcode题目 : https://leetcode.com/problems/closest-binary-search-tree-value-ii/
public class Problem_0272_ClosestBinarySearchTreeValueII {

	public static class TreeNode {
		public int val;
		public TreeNode left;
		public TreeNode right;

		public TreeNode(int val) {
			this.val = val;
		}
	}

	// 这个解法来自讨论区的回答，最优解实现的很易懂且漂亮
	public static List<Integer> closestKValues(TreeNode root, double target, int k) {
		List<Integer> ret = new LinkedList<>();
		// >=8，最近的节点，而且需要快速找后继的这么一种结构
		Stack<TreeNode> moreTops = new Stack<>();
		// <=8，最近的节点，而且需要快速找前驱的这么一种结构
		Stack<TreeNode> lessTops = new Stack<>();
		/*
		* 因为二叉树没有父指针，为了以后快速找到前驱、后继节点
		* 从root开始，在不断接近target的时候，
		* 把涉及父节点的所有后继，依次压入moreTops
		* 把涉及父节点的所有前驱，依次压入lessTops
		* 下面两个方法结束后，两个栈顶都是最近接target的值
		* moreTops 越往底，值比target越大
		* lessTops 越往底，值比target越小
		* */
		getMoreTops(root, target, moreTops);
		getLessTops(root, target, lessTops);
		if (!moreTops.isEmpty() && !lessTops.isEmpty() && moreTops.peek().val == lessTops.peek().val) {
			/*此时两个栈顶是一个值，都是target，后续流程是要找两个候选进行比较，所以找任意一个栈走一步*/
			getPredecessor(lessTops);
		}
		/*找满k个*/
		while (k-- > 0) {
			if (moreTops.isEmpty()) {
				/*后继没了，在所有前驱里继续找*/
				ret.add(getPredecessor(lessTops));
			} else if (lessTops.isEmpty()) {
				/*前驱没了，在所有后继里继续找*/
				ret.add(getSuccessor(moreTops));
			} else {
				/*前驱和后继都有，就看哪个离得近，收集离的近的*/
				double diffs = Math.abs((double) moreTops.peek().val - target);
				double diffp = Math.abs((double) lessTops.peek().val - target);
				if (diffs < diffp) {
					ret.add(getSuccessor(moreTops));
				} else {
					ret.add(getPredecessor(lessTops));
				}
			}
		}
		return ret;
	}

	// 在root为头的树上
	// 找到>=target，且最接近target的节点
	// 并且找的过程中，只要某个节点x往左走了，就把x放入moreTops里
	public static void getMoreTops(TreeNode root, double target, Stack<TreeNode> moreTops) {
		while (root != null) {
			if (root.val == target) {
				moreTops.push(root);
				break;
			} else if (root.val > target) {
				moreTops.push(root);
				root = root.left;
			} else {
				root = root.right;
			}
		}
	}

	// 在root为头的树上
	// 找到<=target，且最接近target的节点
	// 并且找的过程中，只要某个节点x往右走了，就把x放入lessTops里
	public static void getLessTops(TreeNode root, double target, Stack<TreeNode> lessTops) {
		while (root != null) {
			if (root.val == target) {
				lessTops.push(root);
				break;
			} else if (root.val < target) {
				lessTops.push(root);
				root = root.right;
			} else {
				root = root.left;
			}
		}
	}

	// 返回moreTops的头部的值
	// 并且调整moreTops : 为了以后能很快的找到返回节点的后继节点
	public static int getSuccessor(Stack<TreeNode> moreTops) {
		/*当前的后继节点*/
		TreeNode cur = moreTops.pop();
		int ret = cur.val;
		/*
		* 下个后继节点，应该是当前节点右树的最左节点
		* 这里就在准备下个后继节点，方便下次直接获取
		* 为什么将右树的左边界整条就压进去呢？
		* 因为这些都是后继节点
		* */
		cur = cur.right;
		while (cur != null) {
			moreTops.push(cur);
			cur = cur.left;
		}
		return ret;
	}

	// 返回lessTops的头部的值
	// 并且调整lessTops : 为了以后能很快的找到返回节点的前驱节点
	public static int getPredecessor(Stack<TreeNode> lessTops) {
		TreeNode cur = lessTops.pop();
		int ret = cur.val;
		cur = cur.left;
		while (cur != null) {
			lessTops.push(cur);
			cur = cur.right;
		}
		return ret;
	}

}
