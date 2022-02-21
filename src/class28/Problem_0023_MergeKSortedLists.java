package class28;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * You are given an array of k linked-lists lists, each linked-list is sorted in ascending order.
 *
 * Merge all the linked-lists into one sorted linked-list and return it.
 *
 * 题意：合并k个有序链表
 * 解题：
 * 	借助堆，将目前遍历到的所有链表的头节点放入堆中，拿出最小值，生成链表
 * 	没拿出一个，放入一个。堆的大小始终保持k，直到有链表结束了。
 * 	复杂：O(N*logK)
 */
// 测试链接：https://leetcode.com/problems/merge-k-sorted-lists/
public class Problem_0023_MergeKSortedLists {

	public static class ListNode {
		public int val;
		public ListNode next;
	}

	public static class ListNodeComparator implements Comparator<ListNode> {

		@Override
		public int compare(ListNode o1, ListNode o2) {
			return o1.val - o2.val; 
		}

	}

	public static ListNode mergeKLists(ListNode[] lists) {
		if (lists == null) {
			return null;
		}
		/*堆结构，小根堆*/
		PriorityQueue<ListNode> heap = new PriorityQueue<>(new ListNodeComparator());
		for (int i = 0; i < lists.length; i++) {
			if (lists[i] != null) {
				/*先把头都放进去*/
				heap.add(lists[i]);
			}
		}
		if (heap.isEmpty()) {
			return null;
		}
		/*此时的堆顶就是大链表的头*/
		ListNode head = heap.poll();
		/*pre，跟着走*/
		ListNode pre = head;
		if (pre.next != null) {
			/*刚刚poll了一个链表的头，现在把这个链表的第二个节点放进来*/
			heap.add(pre.next);
		}
		/*
		* 堆弹出一个，生成链表
		* 再放入一个
		* 链表中始终保持着k个链表的节点
		* 直到链表空了
		* */
		while (!heap.isEmpty()) {
			/*弹出一个*/
			ListNode cur = heap.poll();
			/*生成链表*/
			pre.next = cur;
			pre = cur;
			/*放入一个*/
			if (cur.next != null) {
				heap.add(cur.next);
			}
		}
		return head;
	}

}
