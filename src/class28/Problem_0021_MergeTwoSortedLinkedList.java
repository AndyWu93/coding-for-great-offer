package class28;

/**
 * You are given the heads of two sorted linked lists list1 and list2.
 *
 * Merge the two lists in a one sorted list. The list should be made by splicing together the nodes of the first two lists.
 *
 * Return the head of the merged linked list.
 *
 * 题意：合并两个有序链表
 * 解题：
 * 	本题考coding
 */
// 测试链接：https://leetcode.com/problems/merge-two-sorted-lists
public class Problem_0021_MergeTwoSortedLinkedList {

	// 不要提交这个类
	public static class ListNode {
		public int val;
		public ListNode next;
	}

	public static ListNode mergeTwoLists(ListNode head1, ListNode head2) {
		if (head1 == null || head2 == null) {
			/*有一个是null，就不用合了*/
			return head1 == null ? head2 : head1;
		}
		/*合并后的新头：较小的节点*/
		ListNode head = head1.val <= head2.val ? head1 : head2;
		/*
		* 两个指针，一个指向头节点下个位置
		* 另一个指向等待合并进来的链表头节点
		* */
		ListNode cur1 = head.next;
		ListNode cur2 = head == head1 ? head2 : head1;
		/*pre，跟着走*/
		ListNode pre = head;
		while (cur1 != null && cur2 != null) {
			if (cur1.val <= cur2.val) {
				/*哪个值小，pre的next指向谁，且指针往下一步*/
				pre.next = cur1;
				cur1 = cur1.next;
			} else {
				pre.next = cur2;
				cur2 = cur2.next;
			}
			/*pre跟着下一步*/
			pre = pre.next;
		}
		/*跳出while，表示cur1cur2有一个指向null了，所以pre的next指向不是null的那个*/
		pre.next = cur1 != null ? cur1 : cur2;
		return head;
	}

}
