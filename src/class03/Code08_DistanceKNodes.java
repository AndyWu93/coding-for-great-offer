package class03;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 近期面试题系列
 * 给定三个参数，二叉树的头节点head，树上某个节点target，正数K。从target开始，可以向上走或者向下走，返回与target的距离是K的所有节点
 * 解题：
 * 	本题思路：递归套路能不能做？可以尝试一下，应该是不能的，
 * 	那如何计算距离呢？可以通过宽度优先遍历来计算，这里可以往上走，所以需要知道一个节点的父是谁，怎么办？建立一张map。
 * 	这样通过一张map和左右节点就可以获得所有的邻居，从而宽度优先遍历
 */
public class Code08_DistanceKNodes {

	public static class Node {
		public int value;
		public Node left;
		public Node right;

		public Node(int v) {
			value = v;
		}
	}

	public static List<Node> distanceKNodes(Node root, Node target, int K) {
		/*父节点map*/
		HashMap<Node, Node> parents = new HashMap<>();
		parents.put(root, null);
		createParentMap(root, parents);
		/*开始宽度优先遍历*/
		Queue<Node> queue = new LinkedList<>();
		HashSet<Node> visited = new HashSet<>();
		queue.offer(target);
		visited.add(target);
		/*统计当前到了第几层*/
		int curLevel = 0;
		List<Node> ans = new ArrayList<>();
		while (!queue.isEmpty()) {
			/*一次遍历一层，方便curLevel++*/
			int size = queue.size();
			while (size-- > 0) {
				Node cur = queue.poll();
				if (curLevel == K) {
					/*到达k层了，收集答案*/
					ans.add(cur);
				}
				if (cur.left != null && !visited.contains(cur.left)) {
					visited.add(cur.left);
					queue.offer(cur.left);
				}
				if (cur.right != null && !visited.contains(cur.right)) {
					visited.add(cur.right);
					queue.offer(cur.right);
				}
				if (parents.get(cur) != null && !visited.contains(parents.get(cur))) {
					visited.add(parents.get(cur));
					queue.offer(parents.get(cur));
				}
			}
			curLevel++;
			if (curLevel > K) {
				/*超过k层的不用遍历了*/
				break;
			}
		}
		return ans;
	}

	/*
	* 如何建立二叉树的父map？
	* 递归到cur时，建立cur子的map
	* */
	public static void createParentMap(Node cur, HashMap<Node, Node> parents) {
		if (cur == null) {
			return;
		}
		if (cur.left != null) {
			/*左孩子的父是自己*/
			parents.put(cur.left, cur);
			/*左孩子递归去*/
			createParentMap(cur.left, parents);
		}
		if (cur.right != null) {
			parents.put(cur.right, cur);
			createParentMap(cur.right, parents);
		}
	}

	public static void main(String[] args) {
		Node n0 = new Node(0);
		Node n1 = new Node(1);
		Node n2 = new Node(2);
		Node n3 = new Node(3);
		Node n4 = new Node(4);
		Node n5 = new Node(5);
		Node n6 = new Node(6);
		Node n7 = new Node(7);
		Node n8 = new Node(8);

		n3.left = n5;
		n3.right = n1;
		n5.left = n6;
		n5.right = n2;
		n1.left = n0;
		n1.right = n8;
		n2.left = n7;
		n2.right = n4;

		Node root = n3;
		Node target = n5;
		int K = 2;

		List<Node> ans = distanceKNodes(root, target, K);
		for (Node o1 : ans) {
			System.out.println(o1.value);
		}

	}

}
