package class06;

/**
 * 给定一个数组arr，想知道arr中哪两个数的异或结果最大，返回最大的异或结果
 * 解题：
 * 	同Code01_MaxXOR，使用前缀树做决策
 */
// 本题测试链接 : https://leetcode.com/problems/maximum-xor-of-two-numbers-in-an-array/
public class Code02_MaximumXorOfTwoNumbersInAnArray {

	public static class Node {
		public Node[] nexts = new Node[2];
	}

	public static class NumTrie {
		public Node head = new Node();

		public void add(int newNum) {
			Node cur = head;
			for (int move = 31; move >= 0; move--) {
				int path = ((newNum >> move) & 1);
				cur.nexts[path] = cur.nexts[path] == null ? new Node() : cur.nexts[path];
				cur = cur.nexts[path];
			}
		}

		public int maxXor(int sum) {
			Node cur = head;
			int res = 0;
			for (int move = 31; move >= 0; move--) {
				int path = (sum >> move) & 1;
				int best = move == 31 ? path : (path ^ 1);
				best = cur.nexts[best] != null ? best : (best ^ 1);
				res |= (path ^ best) << move;
				cur = cur.nexts[best];
			}
			return res;
		}
	}

	public static int findMaximumXOR(int[] arr) {
		if (arr == null || arr.length < 2) {
			return 0;
		}
		int max = Integer.MIN_VALUE;
		NumTrie numTrie = new NumTrie();
		/*先把第一个数放进去*/
		numTrie.add(arr[0]);
		for (int i = 1; i < arr.length; i++) {
			/*找出最优解*/
			max = Math.max(max, numTrie.maxXor(arr[i]));
			/*再把当前数放入前缀树*/
			numTrie.add(arr[i]);
		}
		return max;
	}

}
