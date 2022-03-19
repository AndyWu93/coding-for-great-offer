package class33;

/**
 * Write a function to delete a node in a singly-linked list. You will not be given access to the head of the list, instead you will be given access to the node to be deleted directly.
 *
 * It is guaranteed that the node to be deleted is not a tail node in the list.
 * 题意：删除链表的一个节点
 * 解题：
 * 	本题可以拒绝做，因为参数不对，应该要给2个节点，头节点和要删除的节点
 * 	抖机灵的方式缺点：
 * 		1. 没有真正删除节点，工程上有危险
 * 		2. 无法删除最后一个节点
 */
public class Problem_0237_DeleteNodeInLinkedList {

	public static class ListNode {
		int val;
		ListNode next;
	}

	public void deleteNode(ListNode node) {
		node.val = node.next.val;
		node.next = node.next.next;
	}

}
