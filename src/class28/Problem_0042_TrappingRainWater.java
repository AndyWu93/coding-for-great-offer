package class28;

/**
 *
 * Given n non-negative integers representing an elevation map where the width of each bar is 1, compute how much water it can trap after raining.
 * 题意：给定 n 个非负整数表示每个宽度为 1 的柱子的高度图，计算按此排列的柱子，下雨之后能接多少雨水。
 *
 * 思路：
 * 假设来到了i位置，求出i位置能储多少水
 * 求出i位置左边的maxL，右边的maxR，则i位置的水位=max(min(maxL,maxR)-arr[i],0)
 * 所有位置的水位加起来就是题解。
 * 为了快速拿到maxL和maxR，准备两个最大值预处理数组
 * 如    [3,4,2,5,6,2,4] 得出
 * maxLs:[3,4,4,5,6,6,6]
 * maxRs:[6,6,6,6,6,4,4]
 *
 * 优化方案：
 * 不用预处理数组，改用两个指针L,R分别从两侧往中间走，用两个变量分别记住走过的maxL和maxR
 * 0和n-1位置不用考虑，不会储下水，直接来到1和n-2位置，这时候如果：
 * 1）maxL<maxR,就计算L位置的水位
 * 2）maxR<maxL,就计算R位置的水位
 * 3）相等，可以LR都计算一下。
 * 计算完移动计算过的指针，继续计算下个位置
 *
 * 总结：最大值预处理数组可以用指针方案优化
 */
public class Problem_0042_TrappingRainWater {

	/**
	 * 指针最优解
	 * @param arr
	 * @return
	 */
	public static int water4(int[] arr) {
		if (arr == null || arr.length < 2) {
			return 0;
		}
		int N = arr.length;
		/*左指针*/
		int L = 1;
		/*左指针滑过的最大值*/
		int leftMax = arr[0];
		int R = N - 2;
		int rightMax = arr[N - 1];
		int water = 0;
		while (L <= R) {
			if (leftMax <= rightMax) {
				/*如果左边的最大值小，结算左边，结算的雨水不可能为负数，最少也就是没有收集到水*/
				water += Math.max(0, leftMax - arr[L]);
				/*当前位置，更新一下左边的最大值*/
				leftMax = Math.max(leftMax, arr[L++]);
			} else {
				/*右边同理*/
				water += Math.max(0, rightMax - arr[R]);
				rightMax = Math.max(rightMax, arr[R--]);
			}
		}
		return water;
	}


	/*
	 * 给定一个正整数数组arr，把arr想象成一个直方图。返回这个直方图如果装水，能装下几格水？
	 */
	public static int water1(int[] arr) {
		if (arr == null || arr.length < 2) {
			return 0;
		}
		int N = arr.length;
		int water = 0;
		for (int i = 1; i < N - 1; i++) {
			int leftMax = Integer.MIN_VALUE;
			for (int j = 0; j < i; j++) {
				leftMax = Math.max(leftMax, arr[j]);
			}
			int rightMax = Integer.MIN_VALUE;
			for (int j = i + 1; j < N; j++) {
				rightMax = Math.max(rightMax, arr[j]);
			}
			water += Math.max(Math.min(leftMax, rightMax) - arr[i], 0);
		}
		return water;
	}

	/**
	 * 使用预处理数组
	 * @param arr
	 * @return
	 */
	public static int water2(int[] arr) {
		if (arr == null || arr.length < 2) {
			return 0;
		}
		int N = arr.length;
		int[] leftMaxs = new int[N];
		leftMaxs[0] = arr[0];
		for (int i = 1; i < N; i++) {
			leftMaxs[i] = Math.max(leftMaxs[i - 1], arr[i]);
		}

		int[] rightMaxs = new int[N];
		rightMaxs[N - 1] = arr[N - 1];
		for (int i = N - 2; i >= 0; i--) {
			rightMaxs[i] = Math.max(rightMaxs[i + 1], arr[i]);
		}
		int water = 0;
		for (int i = 1; i < N - 1; i++) {
			water += Math.max(Math.min(leftMaxs[i - 1], rightMaxs[i + 1]) - arr[i], 0);
		}
		return water;
	}

	public static int water3(int[] arr) {
		if (arr == null || arr.length < 2) {
			return 0;
		}
		int N = arr.length;
		int[] rightMaxs = new int[N];
		rightMaxs[N - 1] = arr[N - 1];
		for (int i = N - 2; i >= 0; i--) {
			rightMaxs[i] = Math.max(rightMaxs[i + 1], arr[i]);
		}
		int water = 0;
		int leftMax = arr[0];
		for (int i = 1; i < N - 1; i++) {
			water += Math.max(Math.min(leftMax, rightMaxs[i + 1]) - arr[i], 0);
			leftMax = Math.max(leftMax, arr[i]);
		}
		return water;
	}


	// for test
	public static int[] generateRandomArray(int len, int value) {
		int[] ans = new int[(int) (Math.random() * len) + 1];
		for (int i = 0; i < ans.length; i++) {
			ans[i] = (int) (Math.random() * value) + 1;
		}
		return ans;
	}

	public static void main(String[] args) {
		int len = 100;
		int value = 200;
		int testTimes = 100000;
		System.out.println("test begin");
		for (int i = 0; i < testTimes; i++) {
			int[] arr = generateRandomArray(len, value);
			int ans1 = water1(arr);
			int ans2 = water2(arr);
			int ans3 = water3(arr);
			int ans4 = water4(arr);
			if (ans1 != ans2 || ans3 != ans4 || ans1 != ans3) {
				System.out.println("Oops!");
			}
		}
		System.out.println("test finish");
	}

}
