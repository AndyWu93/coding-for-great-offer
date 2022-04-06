package class06;

/**
 * 给定一个非负整数组成的数组nums。另有一个查询数组queries，其中queries[i]=[xi, mi]
 * 第i个查询的答案是xi和任何nums数组中不超过mi的元素按位异或（XOR）得到的最大值
 * 换句话说，答案是max(nums[j] XOR xi)，其中所有j均满足nums[j]<= mi。如果nums中的所有元素都大于mi，最终答案就是-1
 * 返回一个整数数组answer作为查询的答案，其中answer.length==queries.length且answer[i]是第i个查询的答案
 *
 * 题意：给定一个数组nums，实现一个方法
 * 	maxXor(x,m)：找出nums中哪个数与x异或后值最大，要求，这个数不能大于m，把异或后的值返回
 * 解题：
 * 	还是使用Code01_MaxXOR中的前缀树结构，
 * 	但是这里在做决策的时候这里，加了限制，怎么办？可以在前缀树的节点上加东西的方法来达到满足限制的要求
 * 	这一题，可以在节点中加一个信息，加入的所有num的最小值，
 * 	这样可以通过判断该节点的信息是否大于m，来判断是否要选择这个节点
 */
// 测试链接 : https://leetcode.com/problems/maximum-xor-with-an-element-from-array/
public class Code03_MaximumXorWithAnElementFromArray {

	public static int[] maximizeXor(int[] nums, int[][] queries) {
		int N = nums.length;
		NumTrie trie = new NumTrie();
		for (int i = 0; i < N; i++) {
			/*按题意，把nums都加进去*/
			trie.add(nums[i]);
		}
		int M = queries.length;

		/*按题意，生成一个M大小的结果数组*/
		int[] ans = new int[M];
		for (int i = 0; i < M; i++) {
			/*按题意，调用方法，生成结果*/
			ans[i] = trie.maxXorWithXBehindM(queries[i][0], queries[i][1]);
		}
		return ans;
	}
    
	public static class Node {
		/*
		* 节点里增加信息，表示到目前为止，加入前缀树的最小值
		* 这样起到的效果：
		* root节点记录的是加入的所有的数的最小
		* 树中的节点记录的是，进过自己的所有的数中最小
		* */
		public int min;
		public Node[] nexts;

		public Node() {
			/*初始化，最小值为系统最大*/
			min = Integer.MAX_VALUE;
			nexts = new Node[2];
		}
	}

	public static class NumTrie {
		public Node head = new Node();

		public void add(int num) {
			Node cur = head;
			/*头节点记录最下值*/
			head.min = Math.min(head.min, num);
			/*为什么只把num的数字位加入前缀树？因为题目中告知都是正数，所以符号位不用做决策*/
			for (int move = 30; move >= 0; move--) {
				int path = ((num >> move) & 1);
				cur.nexts[path] = cur.nexts[path] == null ? new Node() : cur.nexts[path];
				cur = cur.nexts[path];
				/*来到了cur节点，记录一下此时的最小值*/
				cur.min = Math.min(cur.min, num);
			}
		}

		/*
		* 这个结构中，已经收集了一票数字
		* 请返回哪个数字与X异或的结果最大，返回最大结果
		* 但是，只有<=m的数字，可以被考虑
		* */
		public int maxXorWithXBehindM(int x, int m) {
			if (head.min > m) {
				/*表示前缀树中没有小于等于m的数，直接返回*/
				return -1;
			}
			// 否则一定存在某个数可以和x结合
			Node cur = head;
			int ans = 0;
			for (int move = 30; move >= 0; move--) {
				int path = (x >> move) & 1;
				/*best: 期待遇到的东西*/
				int best = (path ^ 1);
				/*
				* best: 实际变成的；
				* (cur.nexts[best] == null || cur.nexts[best].min > m)：如果没有期待的路，或者期待的路上的值大于m，
				* 							best只能选另一条路（best此时是1只能选0，此时是0只能选1），否则就还是自己
				* */
				best ^= (cur.nexts[best] == null || cur.nexts[best].min > m) ? 1 : 0;
				ans |= (path ^ best) << move;
				cur = cur.nexts[best];
			}
			return ans;
		}
	}

}
