package class23;

/**
 * 有 N 堆石头排成一排，第 i 堆中有 stones[i] 块石头。每次移动（move）需要将连续的 K 堆石头合并为一堆，而这个移动的成本为这 K 堆石头的总数。
 * 找出把所有石头合并成一堆的最低成本。如果不可能，返回 -1 。
 *
 * 解题：
 * 	本题和霍夫曼编码分金的区别：霍夫曼编码分金，可以不管arr中的顺序，而本题，要求arr中的顺序不能改变。
 * 	本题是动态规划，范围尝试+业务限制模型。
 * 	启发函数：int process(L,R,P):将范围L~R分成P份，代价是多少？
 * 	process(L,R,1)：L~R范围上有很多数，现在只能分成1份，所以先想办法吧L~R分成k份，因为题目中限制，k份可以合并成1份
 * 	process(L,R,P!=1)：L~R范围上有很多数，现在只能分成P份，那就可以在L~R上枚举出第一份的范围，剩下的范围分成P-1份
 * 						两个范围分别计算出代价，再加起来。所有枚举收集min
 */
// 本题测试链接 : https://leetcode.com/problems/minimum-cost-to-merge-stones/
public class Code05_MinimumCostToMergeStones {

//	// arr[L...R]一定要整出P份，合并的最小代价，返回！
//	public static int f(int[] arr, int K, int L, int R, int P) {
//		if(从L到R根本不可能弄出P份) {
//			return Integer.MAX_VALUE;
//		}
//		// 真的有可能的！
//		if(P == 1) {
//			return L == R ? 0 : (f(arr, K, L, R, K) + 最后一次大合并的代价);
//		}
//		int ans = Integer.MAX_VALUE;
//		// 真的有可能，P > 1
//		for(int i = L; i < R;i++) {
//			// L..i(1份)  i+1...R(P-1份)
//			int left = f(arr, K, L, i, 1);
//			if(left == Integer.MAX_VALUE) {
//				continue;
//			}
//			int right = f(arr, K, i+1,R,P-1);
//			if(right == Integer.MAX_VALUE) {
//				continue;
//			}
//			int all = left + right;
//			ans = Math.min(ans, all);
//		}
//		return ans;
//	}

	/**
	 * 尝试
	 */
	public static int mergeStones1(int[] stones, int K) {
		int n = stones.length;
		/*通过观察，只有k，n满足下面关系时，才有可能把stones合并到一起去*/
		if ((n - 1) % (K - 1) > 0) {
			return -1;
		}
		/*前缀和数组，这里presum[i]:是0~i-1的前缀和*/
		int[] presum = new int[n + 1];
		for (int i = 0; i < n; i++) {
			presum[i + 1] = presum[i] + stones[i];
		}
		return process1(0, n - 1, 1, stones, K, presum);
	}

	// part >= 1
	// arr[L..R] 一定要弄出part份，返回最低代价
	// arr、K、presum（前缀累加和数组，求i..j的累加和，就是O(1)了）
	public static int process1(int L, int R, int P, int[] arr, int K, int[] presum) {
		if (L == R) { // arr[L..R]
			/*范围只有1个数，只能分成1份，这里没有合并行为，不生成代价。否则无法分成>1份*/
			return P == 1 ? 0 : -1;
		}
		// L ... R 不只一个数
		if (P == 1) {
			/*范围上有多个数，要合并成1份，那范围上先搞出k个数来，最后k个数合并成1份*/
			int next = process1(L, R, K, arr, K, presum);
			if (next == -1) {
				/*没办法分成k份*/
				return -1;
			} else {
				/*份成k份代价 + L~R合并到一起的代价*/
				return next + presum[R + 1] - presum[L];
			}
		} else { // P > 1
			int ans = Integer.MAX_VALUE;
			/*
			* 枚举第一份的范围：L~mid,剩下的范围P-1份
			* 步长为什么是k-1？这是一个优化举例观察的，步长是1也没问题，只是优化了常数项
			* 0 1 2 3 4 5 6 7 8
			* k=3
			* 0~0 i=0 第一份1个数，可以的
			* 0~2 i=2 第一份3个数，一定可以
			* 0~4 i=4 第一份5个数，先3个数合并成1份，再和剩下的2个数合并成1份，也可以
			* 0~6 i=6 第一份6个数，三三合并
			* */
			for (int mid = L; mid < R; mid += K - 1) {
				// L..mid(一份) mid+1...R(part - 1)
				int next1 = process1(L, mid, 1, arr, K, presum);
				int next2 = process1(mid + 1, R, P - 1, arr, K, presum);
				if (next1 != -1 && next2 != -1) {
					/*两种划分都有效，收集最小值*/
					ans = Math.min(ans, next1 + next2);
				}
			}
			return ans;
		}
	}

	/**
	 * 添加记忆化搜索
	 */
	public static int mergeStones2(int[] stones, int K) {
		int n = stones.length;
		if ((n - 1) % (K - 1) > 0) { // n个数，到底能不能K个相邻的数合并，最终变成1个数！
			return -1;
		}
		int[] presum = new int[n + 1];
		for (int i = 0; i < n; i++) {
			presum[i + 1] = presum[i] + stones[i];
		}
		int[][][] dp = new int[n][n][K + 1];
		return process2(0, n - 1, 1, stones, K, presum, dp);
	}

	public static int process2(int L, int R, int P, int[] arr, int K, int[] presum, int[][][] dp) {
		if (dp[L][R][P] != 0) {
			return dp[L][R][P];
		}
		if (L == R) {
			return P == 1 ? 0 : -1;
		}
		if (P == 1) {
			int next = process2(L, R, K, arr, K, presum, dp);
			if (next == -1) {
				dp[L][R][P] = -1;
				return -1;
			} else {
				dp[L][R][P] = next + presum[R + 1] - presum[L];
				return next + presum[R + 1] - presum[L];
			}
		} else {
			int ans = Integer.MAX_VALUE;
			// L...mid是第1块，剩下的是part-1块
			for (int mid = L; mid < R; mid += K - 1) {
				int next1 = process2(L, mid, 1, arr, K, presum, dp);
				int next2 = process2(mid + 1, R, P - 1, arr, K, presum, dp);
				if (next1 == -1 || next2 == -1) {
					dp[L][R][P] = -1;
					return -1;
				} else {
					ans = Math.min(ans, next1 + next2);
				}
			}
			dp[L][R][P] = ans;
			return ans;
		}
	}

	// for test
	public static int[] generateRandomArray(int maxSize, int maxValue) {
		int[] arr = new int[(int) (maxSize * Math.random()) + 1];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (int) ((maxValue + 1) * Math.random());
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

	public static void main(String[] args) {
		int maxSize = 12;
		int maxValue = 100;
		System.out.println("Test begin");
		for (int testTime = 0; testTime < 100000; testTime++) {
			int[] arr = generateRandomArray(maxSize, maxValue);
			int K = (int) (Math.random() * 7) + 2;
			int ans1 = mergeStones1(arr, K);
			int ans2 = mergeStones2(arr, K);
			if (ans1 != ans2) {
				System.out.println(ans1);
				System.out.println(ans2);
			}
		}

	}

}
