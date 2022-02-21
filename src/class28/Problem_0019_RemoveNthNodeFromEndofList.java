package class28;

/**
 * 删除倒数第n个节点
 *
 */
public class Problem_0019_RemoveNthNodeFromEndofList {

	public static class ListNode {
		public int val;
		public ListNode next;
	}

	/**
	 * 快慢指针
	 * 快指针先走n步，
	 * 下一次快指针在n+1步，慢指针在头位置
	 * 快指针走完了，慢指针的下个位置就是要删除的位置
	 */
	public static ListNode removeNthFromEnd(ListNode head, int n) {
		ListNode cur = head;
		ListNode pre = null;
		while (cur != null) {
			n--;
			if (n == -1) {
				/*快指针cur走了n+1步了，慢指针pre指向头*/
				pre = head;
			}
			if (n < -1) {
				/*接下来快慢指针一起走*/
				pre = pre.next;
			}
			cur = cur.next;
		}
		if (n > 0) {
			/*一共x个节点，删除倒数第n个，n还大于x，就是不用删除*/
			return head;
		}
		if (pre == null) {
			/*此时n==0，表示删除倒数第n个节点，n就是数组长度，所以删除头节点*/
			return head.next;
		}
		/*删除掉了慢指针的下一个节点*/
		pre.next = pre.next.next;
		return head;
	}

}
