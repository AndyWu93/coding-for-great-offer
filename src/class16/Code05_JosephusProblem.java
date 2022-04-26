package class16;

/**
 * 本题为纯数学推理题目，列为竞赛题，但是面试非常高频
 * 给定一个链表头节点head，和一个正数m，从头开始，每次数到m就杀死当前节点，然后被杀节点的下一个节点从1开始重新数，周而复始直到只剩一个节点，返回最后的节点
 * 解题：
 * 暴力解，模拟杀人过程，即每n个节点，数到m杀人，O(n*m)
 * 最优解：
 * 	现在有的参数是：
 * 	n：最大节点编号，编号从1开始
 * 	m：数到m杀人
 * 	通过一个函数f(n,m)直接求出最后活下来的编号，可以吗？想了很久会发现想不出来
 * 	那退而求其次，看杀人前后同一个节点的编号对应关系，如果这个函数搞出来了，
 * 	那最后一个活下来的人的编号最后一定是1，就看上一轮它的编号用公式算出来，一直推到第一轮
 *
 * 	本题核心公式：y=x%i
 * 	该公式的图形为指向右上方的无线条平行线
 * 	公式的变形为平移图形，左加右减，上加下减
 *
 * 	画出前后编号对应的图形，通过平移y=x%i函数，得到对应函数
 * 		y=(x+s)%i+1
 *  s:别杀的编号：(m-1)%i
 *  代入公式
 *  	y=(x+(m-1)%i)%i+1
 *  上面的公司化简一下，得到：
 *  	y=(x+m-1)%i+1
 *  i:杀人前的最大编号
 *  x:杀人后的编号
 *  y:杀人前的编号
 *
 *  这个公式就是任意节点再杀人前后的编号对应关系
 *  已知最后一轮，留下来的人的编号是1
 *  通过公式求出在上一轮，这个人的编号是b
 *  再通过b，通过公式求出在上一轮，这个人的编号是c
 *  再通过c，通过公式求出在上一轮，这个人的编号是d
 *  ...
 *  直到第一轮，就得出这人在第一轮的编号
 */
// 本题测试链接 : https://leetcode-cn.com/problems/yuan-quan-zhong-zui-hou-sheng-xia-de-shu-zi-lcof/
public class Code05_JosephusProblem {

	// 提交直接通过
	// 给定的编号是0~n-1的情况下，数到m就杀
	// 返回谁会活？
	public int lastRemaining1(int n, int m) {
		return getLive(n, m) - 1;
	}

	// 课上题目的设定是，给定的编号是1~n的情况下，数到m就杀
	// 返回谁会活？
	public static int getLive(int n, int m) {
		if (n == 1) {
			return 1;
		}
		return (getLive(n - 1, m) + m - 1) % n + 1;
	}

	// 提交直接通过
	// 给定的编号是0~n-1的情况下，数到m就杀
	// 返回谁会活？
	// 这个版本是迭代版
	public int lastRemaining2(int n, int m) {
		int ans = 1;
		int r = 1;
		while (r <= n) {
			ans = (ans + m - 1) % (r++) + 1;
		}
		return ans - 1;
	}

	// 以下的code针对单链表，不要提交
	public static class Node {
		public int value;
		public Node next;

		public Node(int data) {
			this.value = data;
		}
	}

	public static Node josephusKill1(Node head, int m) {
		if (head == null || head.next == head || m < 1) {
			return head;
		}
		Node last = head;
		while (last.next != head) {
			last = last.next;
		}
		int count = 0;
		while (head != last) {
			if (++count == m) {
				last.next = head.next;
				count = 0;
			} else {
				last = last.next;
			}
			head = last.next;
		}
		return head;
	}

	public static Node josephusKill2(Node head, int m) {
		if (head == null || head.next == head || m < 1) {
			return head;
		}
		Node cur = head.next;
		int size = 1; // tmp -> list size
		while (cur != head) {
			size++;
			cur = cur.next;
		}
		int live = getLive(size, m); // tmp -> service node position
		while (--live != 0) {
			head = head.next;
		}
		head.next = head;
		return head;
	}

	public static void printCircularList(Node head) {
		if (head == null) {
			return;
		}
		System.out.print("Circular List: " + head.value + " ");
		Node cur = head.next;
		while (cur != head) {
			System.out.print(cur.value + " ");
			cur = cur.next;
		}
		System.out.println("-> " + head.value);
	}

	public static void main(String[] args) {
		Node head1 = new Node(1);
		head1.next = new Node(2);
		head1.next.next = new Node(3);
		head1.next.next.next = new Node(4);
		head1.next.next.next.next = new Node(5);
		head1.next.next.next.next.next = head1;
		printCircularList(head1);
		head1 = josephusKill1(head1, 3);
		printCircularList(head1);

		Node head2 = new Node(1);
		head2.next = new Node(2);
		head2.next.next = new Node(3);
		head2.next.next.next = new Node(4);
		head2.next.next.next.next = new Node(5);
		head2.next.next.next.next.next = head2;
		printCircularList(head2);
		head2 = josephusKill2(head2, 3);
		printCircularList(head2);

	}

}