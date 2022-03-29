package class35;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * Given an integer array nums and an integer k, return the k most frequent elements. You may return the answer in any order.
 * 题意：返回前K个词频最高的数
 * 解题：采用门槛堆
 * 	对arr里每个数词频统计
 * 	对统计结果采用门槛堆，留下最大的k个
 */
public class Problem_0347_TopKFrequentElements {

	/*node，便于在堆中好比较，以及结果收集*/
	public static class Node {
		public int num;
		public int count;

		public Node(int k) {
			num = k;
			count = 1;
		}
	}

	public static class CountComparator implements Comparator<Node> {

		@Override
		public int compare(Node o1, Node o2) {
			return o1.count - o2.count;
		}

	}

	public static int[] topKFrequent(int[] nums, int k) {
		HashMap<Integer, Node> map = new HashMap<>();
		for (int num : nums) {
			if (!map.containsKey(num)) {
				map.put(num, new Node(num));
			} else {
				map.get(num).count++;
			}
		}
		/*小根堆，按词频比较*/
		PriorityQueue<Node> heap = new PriorityQueue<>(new CountComparator());
		for (Node node : map.values()) {
			/*堆不满，或者堆满了但堆顶词频小于当前node，堆满了，此时的堆顶就是门槛*/
			if (heap.size() < k || (heap.size() == k && node.count > heap.peek().count)) {
				heap.add(node);
			}
			/*堆的个数可能是k+1，如果是，弹出堆顶*/
			if (heap.size() > k) {
				heap.poll();
			}
		}
		int[] ans = new int[k];
		int index = 0;
		while (!heap.isEmpty()) {
			ans[index++] = heap.poll().num;
		}
		return ans;
	}

}
