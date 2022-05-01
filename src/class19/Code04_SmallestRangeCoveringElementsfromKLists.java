package class19;

import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

/**
 * 你有k个非递减排列的整数列表。找到一个最小区间，使得k个列表中的每个列表至少有一个数包含在其中
 * 我们定义如果 b-a < d-c 或者在 b-a == d-c 时 a < c，则区间 [a,b] 比 [c,d] 小。
 *
 * 解题：
 * 	本题思路：先k个arr都拿出0位置的值，组成一个结构orderSet，
 * 	orderSet中拿出最大值最小值出来组成一个区间a，a含义：k个列表中都包含至少一个数，且必须以a[0]开头的区间是哪个
 * 	弹出a[0],定位a[0]来自arr，找到该arr中a[0]后面一个数，放入orderSet中，
 * 	重新计算最大值和最小值，组成区间b，b含义：k个列表中都包含至少一个数，且必须以b[0]开头的区间是哪个
 * 	弹出b[0]
 * 	...
 * 	直到某次弹出一个数之后，这个数所在位置是某个arr的最后一个数，停止
 * 	区间a，b，c...这些收集最窄区间
 *
 * 	结构orderSet需要同时获取最大值和最小值，什么结构？有序表
 */
// 本题测试链接 : https://leetcode.com/problems/smallest-range-covering-elements-from-k-lists/
public class Code04_SmallestRangeCoveringElementsfromKLists {

	public static class Node {
		/*值，排序1*/
		public int value;
		/*在哪个list里，排序2*/
		public int arrid;
		/*在list中的位置，为了取得下一个数*/
		public int index;

		public Node(int v, int ai, int i) {
			value = v;
			arrid = ai;
			index = i;
		}
	}

	public static class NodeComparator implements Comparator<Node> {

		@Override
		public int compare(Node o1, Node o2) {
			return o1.value != o2.value ? o1.value - o2.value : o1.arrid - o2.arrid;
		}

	}

	public static int[] smallestRange(List<List<Integer>> nums) {
		int N = nums.size();
		TreeSet<Node> orderSet = new TreeSet<>(new NodeComparator());
		for (int i = 0; i < N; i++) {
			/*放入所有的list[0]*/
			orderSet.add(new Node(nums.get(i).get(0), i, 0));
		}
		/*标记获取的是否是首个区间，来给a，b赋值形成初始区间[a,b]*/
		boolean set = false;
		int a = 0;
		int b = 0;
		/*
		* orderSet每次弹出最小值，再加入一个数，一直保持size为N，当一个list到末尾了，size就变小，表示该停了
		* */
		while (orderSet.size() == N) {
			Node min = orderSet.first();
			Node max = orderSet.last();
			if (!set || (max.value - min.value < b - a)) {
				/*遇到更窄的区间就更新*/
				set = true;
				a = min.value;
				b = max.value;
			}
			min = orderSet.pollFirst();
			int arrid = min.arrid;
			int index = min.index + 1;
			if (index != nums.get(arrid).size()) {
				orderSet.add(new Node(nums.get(arrid).get(index), arrid, index));
			}
		}
		return new int[] { a, b };
	}

}
