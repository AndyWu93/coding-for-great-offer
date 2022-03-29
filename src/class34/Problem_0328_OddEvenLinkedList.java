package class34;

/**
 * Given the head of a singly linked list, group all the nodes with odd indices together followed by the nodes with even indices, and return the reordered list.
 *
 * The first node is considered odd, and the second node is even, and so on.
 *
 * Note that the relative order inside both the even and odd groups should remain as it was in the input.
 *
 * You must solve the problem in O(1) extra space complexity and O(n) time complexity.
 * 题意：
 * 	给定一个单链表，调整为奇数下标放在一起，偶数下标放在一起
 * 	本题仅考查coding
 */
public class Problem_0328_OddEvenLinkedList {

	// 提交时不要提交这个类
	public static class ListNode {
		int val;
		ListNode next;
	}

	public ListNode oddEvenList(ListNode head) {
		ListNode firstOdd = null;
		ListNode firstEven = null;
		ListNode odd = null;
		ListNode even = null;
		ListNode next = null;
		int count = 1;
		while (head != null) {
			next = head.next;
			head.next = null;
			if ((count & 1) == 1) {
				firstOdd = firstOdd == null ? head : firstOdd;
				if (odd != null) {
					odd.next = head;
				}
				odd = head;
			} else {
				firstEven = firstEven == null ? head : firstEven;
				if (even != null) {
					even.next = head;
				}
				even = head;
			}
			count++;
			head = next;
		}
		if (odd != null) {
			odd.next = firstEven;
		}
		return firstOdd != null ? firstOdd : firstEven;
	}

}
