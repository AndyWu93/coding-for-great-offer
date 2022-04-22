package class14;

/**
 * 给定一个棵完全二叉树，返回这棵树的节点个数，要求时间复杂度小于O(树的节点数)
 * 解题：
 * 	本题要求复杂度小于O(N)，表示一定不能通过遍历数节点的方式
 * 	设计函数process(node,curLevel,h)求出以node为头的整颗树节点数量
 * 	函数中需要判断哪一侧是满二叉树，直接用公式，另一侧递归调用
 */
public class Code04_CompleteTreeNodeNumber {

	public static class Node {
		public int value;
		public Node left;
		public Node right;

		public Node(int data) {
			this.value = data;
		}
	}

	/**
	 * 复杂度：
	 * 递归只走了一侧，走了一个树的高度，logN
	 * 每次递归里，需要计算右树的最左节点，又是树的高度，logN
	 * 所以复杂度O((logN)^2)
	 */
	// 请保证head为头的树，是完全二叉树
	public static int nodeNum(Node head) {
		if (head == null) {
			return 0;
		}
		return bs(head, 1, mostLeftLevel(head, 1));
	}

	/*
	* 当前来到node节点，node节点在level层，总层数是h
	* 返回node为头的子树(必是完全二叉树)，有多少个节点
	* */
	public static int bs(Node node, int Level, int h) {
		if (Level == h) {
			/*叶子节点*/
			return 1;
		}
		/*
		* 右树的最左节点触底，左树一定是满二叉树，节点数为2^h-1,加上自己，加上右树递归
		* 右树的最左节点没有触底，右树一定是满二叉树，且高度为h-1，节点数为2^(h-1)-1,加上自己，加上左树递归
		* */
		if (mostLeftLevel(node.right, Level + 1) == h) {
			return (1 << (h - Level)) + bs(node.right, Level + 1, h);
		} else {
			return (1 << (h - Level - 1)) + bs(node.left, Level + 1, h);
		}
	}

	// 如果node在第level层，
	// 求以node为头的子树，最大深度是多少
	// node为头的子树，一定是完全二叉树
	public static int mostLeftLevel(Node node, int level) {
		while (node != null) {
			level++;
			node = node.left;
		}
		/*level在循环为空时才跳出，要的是最后一次不为空的level*/
		return level - 1;
	}

	public static void main(String[] args) {
		Node head = new Node(1);
		head.left = new Node(2);
		head.right = new Node(3);
		head.left.left = new Node(4);
		head.left.right = new Node(5);
		head.right.left = new Node(6);
		System.out.println(nodeNum(head));

	}

}
