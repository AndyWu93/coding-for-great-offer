package class27;

/**
 * You are given two non-empty linked lists representing two non-negative integers. The digits are stored in reverse order, and each of their nodes contains a single digit. Add the two numbers and return the sum as a linked list.
 * You may assume the two numbers do not contain any leading zero, except the number 0 itself.
 *
 * 题意：两个链表代表的数相加，结果以链表形式返回
 * 思路：
 * 	将短链表的值加到长链表里面去。
 * 	需要注意进位信息，以及最后一次进位需要补节点的问题
 *
 */
// 测试链接：https://leetcode.com/problems/add-two-numbers/
public class Problem_0002_AddTwoNumbers {

	// 不要提交这个类
	public static class ListNode {
		public int val;
		public ListNode next;

		public ListNode(int val) {
			this.val = val;
		}

		public ListNode(int val, ListNode next) {
			this.val = val;
			this.next = next;
		}
	}

	public static ListNode addTwoNumbers(ListNode head1, ListNode head2) {
		int len1 = listLength(head1);
		int len2 = listLength(head2);
		/*先分辨出长短链表*/
		ListNode l = len1 >= len2 ? head1 : head2;
		ListNode s = l == head1 ? head2 : head1;
		/*准备两个指针，分别指向长短链表的头*/
		ListNode curL = l;
		ListNode curS = s;
		/*
		* last变量是指向遍历到的最后一个节点，
		* 因为curL curS最后指向了null，但如果有进位信息还要补节点。就在last后面补节点
		* */
		ListNode last = curL;
		/*进位*/
		int carry = 0;
		/*当前两个节点相加的和*/
		int curNum = 0;
		/*阶段一：长短指针都有值*/
		while (curS != null) {
			curNum = curL.val + curS.val + carry;
			/*取模后将值赋给了长链表*/
			curL.val = (curNum % 10);
			carry = curNum / 10;
			last = curL;
			/*长短指针往后*/
			curL = curL.next;
			curS = curS.next;
		}
		/*阶段二：仅长指针有值*/
		while (curL != null) {
			curNum = curL.val + carry;
			curL.val = (curNum % 10);
			carry = curNum / 10;
			last = curL;
			/*长指针往后*/
			curL = curL.next;
		}
		if (carry != 0) {
			/*最右了还有进位，需要补节点*/
			last.next = new ListNode(1);
		}
		/*返回长链表的头节点*/
		return l;
	}

	// 求链表长度
	public static int listLength(ListNode head) {
		int len = 0;
		while (head != null) {
			len++;
			head = head.next;
		}
		return len;
	}

}
