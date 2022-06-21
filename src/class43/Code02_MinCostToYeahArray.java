package class43;

import java.util.Arrays;

/**
 * 来自360笔试
 * 给定一个正数数组arr，长度为n，下标0~n-1
 * arr中的0、n-1位置不需要达标，它们分别是最左、最右的位置
 * 中间位置i需要达标，达标的条件是 : arr[i-1] > arr[i] 或者 arr[i+1] > arr[i]哪个都可以
 * 你每一步可以进行如下操作：对任何位置的数让其-1
 * 你的目的是让arr[1~n-2]都达标，这时arr称之为yeah！数组
 * 返回至少要多少步可以让arr变成yeah！数组
 * 数据规模 : 数组长度 <= 10000，数组中的值<=500
 *
 * 解题：
 * 	本题能够比较容易的想到贪心解，但是动态规划的解可以作为一个优秀的解题技巧
 * 贪心：
 * 	数组arr要想达标，调整之后一定不能再中间出现山峰，因为山峰肯定不达标。所以arr最后会有3中状态
 * 	一路下降、一路上升、先下降后上升
 * 	我们可以把一路下降、一路上升 作为 先下降后上升 的一种特殊情况，即下降/上升的区间为0
 * 	怎么求呢？
 * 	枚举arr[i]作为山谷时，调整的最小代价。
 * 	具体实现遇到的问题：
 * 	1. 如何快速求出arr[i]山谷的最小代价？
 * 		两个预处理数组：
 * 		left[i]:0~i位置一路下降，总的最小代价
 * 		right[i]:n-1~i位置一路下降，总的最小代价
 * 	2. 真的要求出arr[i]位置作为山谷吗？
 * 		因为i位置达标的要求是小于一边即可，所以两边向中间下降的时候，没必要汇聚到一个点，可以左边汇聚到i，右边汇聚到i+1
 * 		i和i+1位置一定是达标的，这样代价是最小的
 * 	3. 0和n-1位置自动达标怎么让他们不需要做-1的调整？
 * 		在原arr的首尾加一个系统最大值，这样0和n-1位置肯定不需要调整就达标
 *
 * 动态规划（高难度重要技巧，具体在代码中显示）
 * 	业务限制模型中的普通解限制模型
 * 	如何优化枚举？
 * 	通过观察生成辅助数组
 */
public class Code02_MinCostToYeahArray {

	public static final int INVALID = Integer.MAX_VALUE;

	// 纯暴力方法，只是为了结果对
	// 时间复杂度极差
	public static int minCost0(int[] arr) {
		if (arr == null || arr.length < 3) {
			return 0;
		}
		int n = arr.length;
		int min = INVALID;
		for (int num : arr) {
			min = Math.min(min, num);
		}
		int base = min - n;
		return process0(arr, base, 0);
	}

	public static int process0(int[] arr, int base, int index) {
		if (index == arr.length) {
			for (int i = 1; i < arr.length - 1; i++) {
				if (arr[i - 1] <= arr[i] && arr[i] >= arr[i + 1]) {
					return INVALID;
				}
			}
			return 0;
		} else {
			int ans = INVALID;
			int tmp = arr[index];
			for (int cost = 0; arr[index] >= base; cost++, arr[index]--) {
				int next = process0(arr, base, index + 1);
				if (next != INVALID) {
					ans = Math.min(ans, cost + next);
				}
			}
			arr[index] = tmp;
			return ans;
		}
	}

	/**
	 * 第一步：先把递归写出来
	 */
	// 递归方法，已经把尝试写出
	public static int minCost1(int[] arr) {
		if (arr == null || arr.length < 3) {
			return 0;
		}
		int min = INVALID;
		for (int num : arr) {
			min = Math.min(min, num);
		}
		for (int i = 0; i < arr.length; i++) {
			/*
			* 为什么这么处理，下面有解释
			* */
			arr[i] += arr.length - min;
		}
		return process1(arr, 1, arr[0], true);
	}

	/*
	* 当前来到index位置，值arr[index]
	* pre : 前一个位置的值，可能减掉了一些，所以不能用arr[index-1]
	* preOk : 前一个位置的值，是否已经是有效状态？只对比它左边的数
	* 返回 : 让arr都变有效，最小代价是什么？
	* */
	public static int process1(int[] arr, int index, int pre, boolean preOk) {
		if (index == arr.length - 1) { // 已经来到最后一个数了
			/*
			* 来到了最后一个数
			* 最后一个数天然有效，
			* 	a.如果之前的数已经有效，不需要代价
			* 	b.如果之前的数无效状态，需要当前的数来补救，当前的数只能减，所以pre不能比当前的数大或者相等，
			* 	  所以pre只能比当前小，此时依然不需要代价
			* */
			return preOk || pre < arr[index] ? 0 : INVALID;
		}
		// 当前index，不是最后一个数！
		int ans = INVALID;
		if (preOk) {
			/*
			* pre已经ok了，那cur可以随意减小，所有的情况取最小值
			* 那cur减小到什么程度可以停了呢？这就需要用到普通解来限制
			* 	现在在index位置做决定，index位置最差最差会减到什么位置呢？
			* 	假设arr里都变成了min，index位置又是山谷，那么index位置就要从min减n-1个
			* 	所以index位置的最小值是 min-(n-1)
			* 	为了方便代码，我们把 min-(n-1) 平移到0， 所以arr中所有的值都-min+(n-1)，即 +n-min-1
			* 		这里coding中-1没有减，这样就是把最小可能平移到了1位置，这里cur的最小值依然取到0，不影响复杂度
			* */
			for (int cur = arr[index]; cur >= 0; cur--) {
				int next = process1(arr, index + 1, cur, cur < pre);
				if (next != INVALID) {
					/*
					* 代价怎么收集：
					* 	arr[index] - cur： 当前cur减少的代价
					* 	next：后续代价
					* */
					ans = Math.min(ans, arr[index] - cur + next);
				}
			}
		} else {
			/*
			* pre 不ok，即需要cur来补救，cur只有大于pre才起到补救作用，所以如果cur<=pre,直接返回INVALID
			* cur就不能随意减少了，因为cur要比pre大
			* */
			for (int cur = arr[index]; cur > pre; cur--) {
				/*cur不能依靠pre来ok了，cur为了补救pre，比pre大了，所以false，让后面的来补救cur*/
				int next = process1(arr, index + 1, cur, false);
				if (next != INVALID) {
					ans = Math.min(ans, arr[index] - cur + next);
				}
			}
		}
		return ans;
	}

	/**
	 * 第二步，改成dp
	 */
	// 初改动态规划方法，就是参考minCost1，改出来的版本
	public static int minCost2(int[] arr) {
		if (arr == null || arr.length < 3) {
			return 0;
		}
		int min = INVALID;
		for (int num : arr) {
			min = Math.min(min, num);
		}
		int n = arr.length;
		for (int i = 0; i < n; i++) {
			arr[i] += n - min;
		}
		/*3个可变参数：index、preOk、pre，index的变化范围：1~n*/
		int[][][] dp = new int[n][2][];
		for (int i = 1; i < n; i++) {
			dp[i][0] = new int[arr[i - 1] + 1];
			dp[i][1] = new int[arr[i - 1] + 1];
			Arrays.fill(dp[i][0], INVALID);
			Arrays.fill(dp[i][1], INVALID);
		}
		/*base case*/
		for (int pre = 0; pre <= arr[n - 2]; pre++) {
			dp[n - 1][0][pre] = pre < arr[n - 1] ? 0 : INVALID;
			dp[n - 1][1][pre] = 0;
		}
		for (int index = n - 2; index >= 1; index--) {
			for (int pre = 0; pre <= arr[index - 1]; pre++) {
				/*
				* preOk的两个分支的填写，这里很妙，可以参考
				* */
				for (int cur = arr[index]; cur > pre; cur--) {
					int next = dp[index + 1][0][cur];
					if (next != INVALID) {
						dp[index][0][pre] = Math.min(dp[index][0][pre], arr[index] - cur + next);
					}
				}
				for (int cur = arr[index]; cur >= 0; cur--) {
					int next = dp[index + 1][cur < pre ? 1 : 0][cur];
					if (next != INVALID) {
						dp[index][1][pre] = Math.min(dp[index][1][pre], arr[index] - cur + next);
					}
				}
			}
		}
		return dp[1][1][arr[0]];
	}

	/**
	 * 第三步：优化枚举
	 * 如何优化枚举？还是需要观察
	 * dp[8][0][15]，pre=9的依赖：
	 * dp[9][0][15] + 0
	 * dp[9][0][14] + 1
	 * dp[9][0][13] + 2
	 * ...
	 * dp[9][0][10] + 5
	 * 取min
	 * 以上这些值在第9行时都已经算过了，所以在算第9行的时候直接生成best数组
	 * best[0][v]: pre到v时，最小代价
	 */
	// minCost2动态规划 + 枚举优化
	// 改出的这个版本，需要一些技巧，但很可惜不是最优解
	// 虽然不是最优解，也足以通过100%的case了，
	// 这种技法的练习，非常有意义
	public static int minCost3(int[] arr) {
		if (arr == null || arr.length < 3) {
			return 0;
		}
		int min = INVALID;
		for (int num : arr) {
			min = Math.min(min, num);
		}
		int n = arr.length;
		for (int i = 0; i < n; i++) {
			arr[i] += n - min;
		}
		int[][][] dp = new int[n][2][];
		for (int i = 1; i < n; i++) {
			dp[i][0] = new int[arr[i - 1] + 1];
			dp[i][1] = new int[arr[i - 1] + 1];
		}
		for (int p = 0; p <= arr[n - 2]; p++) {
			dp[n - 1][0][p] = p < arr[n - 1] ? 0 : INVALID;
		}
		/*n-1行已经填好了，先生成n-1行的best*/
		int[][] best = best(dp, n - 1, arr[n - 2]);
		for (int i = n - 2; i >= 1; i--) {
			for (int p = 0; p <= arr[i - 1]; p++) {
				if (arr[i] < p) {
					dp[i][1][p] = best[1][arr[i]];
				} else {
					dp[i][1][p] = Math.min(best[0][p], p > 0 ? best[1][p - 1] : INVALID);
				}
				dp[i][0][p] = arr[i] <= p ? INVALID : best[0][p + 1];
			}
			/*再生成best，为下次做准备*/
			best = best(dp, i, arr[i - 1]);
		}
		return dp[1][1][arr[0]];
	}

	public static int[][] best(int[][][] dp, int i, int v) {
		int[][] best = new int[2][v + 1];
		/*
		* 依然是根据preOk的情况来填写，因为preOk的情况不同，各自填写
		* */
		/*preOk：no*/
		best[0][v] = dp[i][0][v];
		for (int p = v - 1; p >= 0; p--) {
			/*
			* best中再p的变化范围中，保留到p为止的最小值
			* v - p + dp[i][0][p]：这里pk的不只是dp中的数，而是dp中加上v变成p的代价
			* */
			best[0][p] = dp[i][0][p] == INVALID ? INVALID : v - p + dp[i][0][p];
			best[0][p] = Math.min(best[0][p], best[0][p + 1]);
		}
		/*preOk：yes*/
		best[1][0] = dp[i][1][0] == INVALID ? INVALID : v + dp[i][1][0];
		for (int p = 1; p <= v; p++) {
			best[1][p] = dp[i][1][p] == INVALID ? INVALID : v - p + dp[i][1][p];
			best[1][p] = Math.min(best[1][p], best[1][p - 1]);
		}
		return best;
	}

	/**
	 * 贪心
	 */
	// 最终的最优解，贪心
	// 时间复杂度O(N)
	// 请注意，重点看上面的方法
	// 这个最优解容易理解，但让你学到的东西不是很多
	public static int yeah(int[] arr) {
		if (arr == null || arr.length < 3) {
			return 0;
		}
		int n = arr.length;
		int[] nums = new int[n + 2];
		nums[0] = Integer.MAX_VALUE;
		nums[n + 1] = Integer.MAX_VALUE;
		for (int i = 0; i < arr.length; i++) {
			nums[i + 1] = arr[i];
		}
		int[] leftCost = new int[n + 2];
		int pre = nums[0];
		int change = 0;
		for (int i = 1; i <= n; i++) {
			change = Math.min(pre - 1, nums[i]);
			leftCost[i] = nums[i] - change + leftCost[i - 1];
			pre = change;
		}
		int[] rightCost = new int[n + 2];
		pre = nums[n + 1];
		for (int i = n; i >= 1; i--) {
			change = Math.min(pre - 1, nums[i]);
			rightCost[i] = nums[i] - change + rightCost[i + 1];
			pre = change;
		}
		int ans = Integer.MAX_VALUE;
		/*只需要计算1~n位置，取相邻两个位置作为左右的山谷，计算出总代价。枚举所有的相邻位置，收集最小代价*/
		for (int i = 1; i <= n; i++) {
			ans = Math.min(ans, leftCost[i] + rightCost[i + 1]);
		}
		return ans;
	}

	// 为了测试
	public static int[] randomArray(int len, int v) {
		int[] arr = new int[len];
		for (int i = 0; i < len; i++) {
			arr[i] = (int) (Math.random() * v) + 1;
		}
		return arr;
	}

	// 为了测试
	public static int[] copyArray(int[] arr) {
		int[] ans = new int[arr.length];
		for (int i = 0; i < arr.length; i++) {
			ans[i] = arr[i];
		}
		return ans;
	}

	// 为了测试
	public static void main(String[] args) {
		int len = 7;
		int v = 10;
		int testTime = 100;
		System.out.println("==========");
		System.out.println("功能测试开始");
		for (int i = 0; i < testTime; i++) {
			int n = (int) (Math.random() * len) + 1;
			int[] arr = randomArray(n, v);
			int[] arr0 = copyArray(arr);
			int[] arr1 = copyArray(arr);
			int[] arr2 = copyArray(arr);
			int[] arr3 = copyArray(arr);
			int[] arr4 = copyArray(arr);
			int ans0 = minCost0(arr0);
			int ans1 = minCost1(arr1);
			int ans2 = minCost2(arr2);
			int ans3 = minCost3(arr3);
			int ans4 = yeah(arr4);
			if (ans0 != ans1 || ans0 != ans2 || ans0 != ans3 || ans0 != ans4) {
				System.out.println("出错了！");
			}
		}
		System.out.println("功能测试结束");
		System.out.println("==========");

		System.out.println("性能测试开始");

		len = 10000;
		v = 500;
		System.out.println("生成随机数组长度：" + len);
		System.out.println("生成随机数组值的范围：[1, " + v + "]");
		int[] arr = randomArray(len, v);
		int[] arr3 = copyArray(arr);
		int[] arrYeah = copyArray(arr);
		long start;
		long end;
		start = System.currentTimeMillis();
		int ans3 = minCost3(arr3);
		end = System.currentTimeMillis();
		System.out.println("minCost3方法:");
		System.out.println("运行结果: " + ans3 + ", 时间(毫秒) : " + (end - start));

		start = System.currentTimeMillis();
		int ansYeah = yeah(arrYeah);
		end = System.currentTimeMillis();
		System.out.println("yeah方法:");
		System.out.println("运行结果: " + ansYeah + ", 时间(毫秒) : " + (end - start));

		System.out.println("性能测试结束");
		System.out.println("==========");

	}

}
