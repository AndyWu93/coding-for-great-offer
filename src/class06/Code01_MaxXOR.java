package class06;

/**
 * 数组中最大子数组异或和
 * 解法一：暴力解
 * 思路：异或运算不具备单调性，要求最大子数组，只能枚举出所有的子数组，将每个子数组的异或和作对比，收集最大的
 * 	如果枚举出所有的子数组O(N^2),再将子数组的值异或起来O(N)，复杂度就是O(N^3)。
 * 	这里可以用预处理数组，将复杂度降到O(N^2):
 * 	准备一个前缀异或和数组eor
 * 	eor[i]:0->i位置的数异或和
 * 	遍历arr数组，来到了j位置，即枚举以j位置结尾的所有子数组，
 * 	即遍历0->j，来到了其中的i位置，含义：从i到j位置的子数组，求该子数组的异或和：eor[j]^eor[i](0->i位置的数跟自己异或变为0，剩下i->j位置的数)
 * 	收集结果求最大值
 *
 * 解法二：经典子数组思路，以i位置结尾的子数组中最大异或和是多少
 * 思路：
 * 	如果有了0到i位置的异或和eor[i],
 * 	就在看前面的前缀eor[0]->eor[i]中,哪个与eor[i]异或起来最大，
 * 流程：
 * 	遍历arr数组，来到了j位置，接下来需要枚举以j位置结尾的所有子数组，假设，现在拿到了eor[j]的二进制结果，
 * 	现在求eor[0]->eor[j]中与eor[j]异或起来最大的数，规则就是要一个数，符号位与eor[j]相同，其他的相反，
 * 	异或起来得到的结果就是符号位尽量为0，数字位尽量为1，这样的结果就是最大。
 * 	这需要从第一位往后作决策，最符合的结构，就是前缀树
 * 	前缀树提供两个方法：
 * 	add(num)：将num加入前缀树中。前缀树每个节点只有两个方向的路0、1；树的深度32
 * 	maxXor(sum)：前缀树中现有的数，谁和sum异或起来最大，返回异或后的值
 * 	复杂度：O(32*N)=O(N)
 */
public class Code01_MaxXOR {

	// O(N^2)
	public static int maxXorSubarray1(int[] arr) {
		if (arr == null || arr.length == 0) {
			return 0;
		}
		// 准备一个前缀异或和数组eor
		// eor[i] = arr[0...i]的异或结果
		int[] eor = new int[arr.length];
		eor[0] = arr[0];
		// 生成eor数组，eor[i]代表arr[0..i]的异或和
		for (int i = 1; i < arr.length; i++) {
			eor[i] = eor[i - 1] ^ arr[i];
		}
		int max = Integer.MIN_VALUE;
		/*枚举所有的子数组，以j结尾，以i开头*/
		for (int j = 0; j < arr.length; j++) {
			for (int i = 0; i <= j; i++) { // 依次尝试arr[0..j]、arr[1..j]..arr[i..j]..arr[j..j]
				/*
				* i == 0 ? eor[j]：开头是0的话，子数组异或和就是eor[j]
				* */
				max = Math.max(max, i == 0 ? eor[j] : eor[j] ^ eor[i - 1]);
			}
		}
		return max;
	}

	/*
	* 前缀树的节点类型，每个节点向下只可能有走向0或1的路
	* node.nexts[0] == null 0方向没路
	* node.nexts[0] != null 0方向有路
	* */
	public static class Node {
		public Node[] nexts = new Node[2];
	}

	// 基于本题，定制前缀树的实现
	public static class NumTrie {
		// 头节点
		public Node head = new Node();

		/*
		* 把某个数字newNum加入到这棵前缀树里
		* （把数字理解成是一个32长度的str，该str里只有0、1）
		* */
		public void add(int newNum) {
			Node cur = head;
			/*树的深度；32*/
			for (int move = 31; move >= 0; move--) {
				/*
				* 从高位到低位，取出每一位的状态，
				* 如果当前状态是0，
				* 走nexts[0] 的路
				* 如果当前状态是1
				* 走nexts[1] 的路
				* */
				int path = ((newNum >> move) & 1);
				// 无路新建、有路复用
				cur.nexts[path] = cur.nexts[path] == null ? new Node() : cur.nexts[path];
				cur = cur.nexts[path];
			}
		}

		/*
		* 该前缀数中，已经收集了一些数字
		* 这些收集的数字中，sum 和 谁 ^ 结果最大（把结果返回）
		* */
		public int maxXor(int sum) {
			Node cur = head;
			int res = 0;
			for (int move = 31; move >= 0; move--) {
				/*
				* 为什么要从高位开始决策？
				* 	想要结果尽量大，肯定优先高位
				* path：拿到当前位
				* */
				int path = (sum >> move) & 1;
				/*期待的路best：符号位期待当前位置相同的路，数字位期待不同的路*/
				int best = move == 31 ? path : (path ^ 1);
				/*
				* 实际走的路best：如果有期待的路，就走期待的路，没有就走现有的路
				* 假如符号位最终决策下来只能是1，后面的数字位决策方式要变吗？
				* 	不需要，因为负数和正数一样，高位是1，比高位是0大
				* */
				best = cur.nexts[best] != null ? best : (best ^ 1);
				/*
				* 收集结果
				* (path ^ best) 当前位位异或完的结果
				* 当前位的数和期待的数异或起来，放到该位上，32次每次决策一个位上的数，将数都|起来，形成一个32位的结果
				* */
				res |= (path ^ best) << move;
				/*去下个位置做决策*/
				cur = cur.nexts[best];
			}
			return res;
		}
	}

	public static int maxXorSubarray2(int[] arr) {
		if (arr == null || arr.length == 0) {
			return 0;
		}
		int max = Integer.MIN_VALUE;
		int eor = 0; // 0..i 异或和
		// 前缀树 -> numTrie
		NumTrie numTrie = new NumTrie();
		numTrie.add(0); // 一个数也没有的时候，异或和是0
		/*枚举以i位置结尾的子数组*/
		for (int i = 0; i < arr.length; i++) {
			/*eor[i]: 0..i异或和*/
			eor ^= arr[i];
			/*
			* 结算以arr[i]结尾的所有子数组中，最大异或和：
			* eor[i]和之前eor[?]（以及0）哪个异或起来最大：eor[?->i]，必须以i结尾的最大异或和
			* eor[i]^eor[?](0->?位置的数跟自己异或变为0，剩下?->i位置的数)
			* */
			max = Math.max(max, numTrie.maxXor(eor));
			/*eor[i]加入到前缀树中，给下个位置的计算做准备*/
			numTrie.add(eor);
		}
		return max;
	}

	// for test
	public static int[] generateRandomArray(int maxSize, int maxValue) {
		int[] arr = new int[(int) ((maxSize + 1) * Math.random())];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (int) ((maxValue + 1) * Math.random()) - (int) (maxValue * Math.random());
		}
		return arr;
	}

	// for test
	public static void printArray(int[] arr) {
		if (arr == null) {
			return;
		}
		for (int i = 0; i < arr.length; i++) {
			System.out.print(arr[i] + " ");
		}
		System.out.println();
	}

	// for test
	public static void main(String[] args) {
		int testTime = 500000;
		int maxSize = 30;
		int maxValue = 50;
		boolean succeed = true;
		for (int i = 0; i < testTime; i++) {
			int[] arr = generateRandomArray(maxSize, maxValue);
			int comp = maxXorSubarray1(arr);
			int res = maxXorSubarray2(arr);
			if (res != comp) {
				succeed = false;
				printArray(arr);
				System.out.println(res);
				System.out.println(comp);
				break;
			}
		}
		System.out.println(succeed ? "Nice!" : "Fucking fucked!");
		//
		// // int[] arr = generateRandomArray(6, maxValue);
		// int[] arr = { 3, -28, -29, 2};
		//
		// for (int i = 0; i < arr.length; i++) {
		// System.out.println(arr[i] + " ");
		// }
		// System.out.println("=========");
		// System.out.println(maxXorSubarray(arr));
		// System.out.println((int) (-28 ^ -29));

	}
}
