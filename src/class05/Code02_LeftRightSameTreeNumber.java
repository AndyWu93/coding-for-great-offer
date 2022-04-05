package class05;

/**
 * 阿里面试题
 * 如果一个节点X，它左树结构和右树结构完全一样，那么我们说以X为头的子树是相等子树
 * 给定一棵二叉树的头节点head，返回head整棵树上有多少棵相等子树
 * 解题：本题多种解题方法，经典方法为二叉树的递归套路
 */
public class Code02_LeftRightSameTreeNumber {

	public static class Node {
		public int value;
		public Node left;
		public Node right;

		public Node(int v) {
			value = v;
		}
	}

	/**
	 * 递归套路解法
	 * T(N) = 2T(N/2) + O(N)
	 * 	2T(N/2)：2个规模相等的递归
	 * 	O(N)：两个树的比对，最差需要遍历整棵树
	 * 通过master计算，复杂度O(N * logN)
	 */
	public static int sameNumber1(Node head) {
		if (head == null) {
			return 0;
		}
		return sameNumber1(head.left) + sameNumber2(head.right) + (same(head.left, head.right) ? 1 : 0);
	}

	/*h1和h2是不是相等树*/
	public static boolean same(Node h1, Node h2) {
		if (h1 == null ^ h2 == null) {
			return false;
		}
		if (h1 == null && h2 == null) {
			return true;
		}
		// 两个都不为空
		return h1.value == h2.value && same(h1.left, h2.left) && same(h1.right, h2.right);
	}

	/**
	 * 优化比对过程
	 * 将比对过程变成O(1)
	 */
	// 时间复杂度O(N)
	public static int sameNumber2(Node head) {
		String algorithm = "SHA-256";
		Hash hash = new Hash(algorithm);
		return process(head, hash).ans;
	}

	public static class Info {
		/*该树有多少个相等树*/
		public int ans;
		/*
		* 该树以hash形式序列化之后，再计算hash值
		* 为什么计算hash？
		* 因为hash可以变成固定长度的比对
		* */
		public String str;

		public Info(int a, String s) {
			ans = a;
			str = s;
		}
	}

	public static Info process(Node head, Hash hash) {
		if (head == null) {
			/*空数的hash用"#,"*/
			return new Info(0, hash.hashCode("#,"));
		}
		Info l = process(head.left, hash);
		Info r = process(head.right, hash);
		int ans = (l.str.equals(r.str) ? 1 : 0) + l.ans + r.ans;
		/*
		* 结构的hash值怎么算：
		* 自己的值 拼接 左树的hash 拼接 右树的hash，得到的str整体取hash
		* */
		String str = hash.hashCode(String.valueOf(head.value) + "," + l.str + r.str);
		return new Info(ans, str);
	}


	//for test
	public static Node randomBinaryTree(int restLevel, int maxValue) {
		if (restLevel == 0) {
			return null;
		}
		Node head = Math.random() < 0.2 ? null : new Node((int) (Math.random() * maxValue));
		if (head != null) {
			head.left = randomBinaryTree(restLevel - 1, maxValue);
			head.right = randomBinaryTree(restLevel - 1, maxValue);
		}
		return head;
	}

	//for test
	public static void main(String[] args) {
		int maxLevel = 8;
		int maxValue = 4;
		int testTime = 100000;
		System.out.println("测试开始");
		for (int i = 0; i < testTime; i++) {
			Node head = randomBinaryTree(maxLevel, maxValue);
			int ans1 = sameNumber1(head);
			int ans2 = sameNumber2(head);
			if (ans1 != ans2) {
				System.out.println("出错了！");
				System.out.println(ans1);
				System.out.println(ans2);
			}
		}
		System.out.println("测试结束");

	}

}
