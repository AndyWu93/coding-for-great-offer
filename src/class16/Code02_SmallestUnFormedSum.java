package class16;

import java.util.Arrays;
import java.util.HashSet;

/**
 * 求正数数组最小不可组成和
 * 题意：假设数组arr最大子数组累加和max，最小子数组累加min，求min-max中间arr无法组成的和中最小的组成和，如果都能组成，返回max+1
 * 思路：背包二维dp问题
 * 行：累加和0..max累加和
 * 列：数组
 * dp[i][j]：arr[0..i]上的数能否组成累加和sum[j]
 * dp最后一行找最先出现true的sum
 *
 * @see class14.Code06_MissingNumber 注意：和这道题不一样
 *
 *
 * 进阶：求正数数组最小不可组成和，已知数组中含有1
 * 思路：数组中有1就创建了一个连续range的基础，解题就变得简单了
 * 1.先将arr排序，第一个位置一定是1
 * 2.定义变量range：含义：遍历到i位置，0..i-1位置的数可以组合0-range中的任意一个值
 * 3.遍历arr，来到了i位置
 * 如果arr[i]<=range+1,range=range+arr[i]。（如果之前range是可以凑到了，当前数a小于等于range+1，那a+range一定能凑到）
 * 如果arr[i]>range+1,停止遍历，range+1就是题解
 */
public class Code02_SmallestUnFormedSum {

	public static int unformedSum1(int[] arr) {
		if (arr == null || arr.length == 0) {
			return 1;
		}
		HashSet<Integer> set = new HashSet<Integer>();
		process(arr, 0, 0, set);
		int min = Integer.MAX_VALUE;
		for (int i = 0; i != arr.length; i++) {
			min = Math.min(min, arr[i]);
		}
		for (int i = min + 1; i != Integer.MIN_VALUE; i++) {
			if (!set.contains(i)) {
				return i;
			}
		}
		return 0;
	}

	public static void process(int[] arr, int i, int sum, HashSet<Integer> set) {
		if (i == arr.length) {
			set.add(sum);
			return;
		}
		process(arr, i + 1, sum, set);
		process(arr, i + 1, sum + arr[i], set);
	}

	/**
     * 普通版
	 */
	public static int unformedSum2(int[] arr) {
		if (arr == null || arr.length == 0) {
			return 1;
		}
		int sum = 0;
		int min = Integer.MAX_VALUE;
		for (int i = 0; i != arr.length; i++) {
			sum += arr[i];
			min = Math.min(min, arr[i]);
		}
		// boolean[][] dp ...
		int N = arr.length;
		boolean[][] dp = new boolean[N][sum + 1];
		for (int i = 0; i < N; i++) {// arr[0..i] 0
			/*arr[0..i]上的数能否求出子数组和为0，一定可以，1个都不取就行*/
			dp[i][0] = true;
		}
		/*arr上一个数，能否组成这一个数的sum，一定可以*/
		dp[0][arr[0]] = true;
		for (int i = 1; i < N; i++) {
			for (int j = 1; j <= sum; j++) {
				dp[i][j] = dp[i - 1][j] || ((j - arr[i] >= 0) ? dp[i - 1][j - arr[i]] : false);
			}
		}
		for (int j = min; j <= sum; j++) {
			/*看最后一行中，找到第一不能凑出的sum，注意j从min开始*/
			if (!dp[N - 1][j]) {
				return j;
			}
		}
		return sum + 1;
	}

	/**
	 * 进阶
	 */
	// 已知arr中肯定有1这个数
	public static int unformedSum3(int[] arr) {
		if (arr == null || arr.length == 0) {
			return 0;
		}
		Arrays.sort(arr); // O (N * logN)
		/*来到arr[0]=1，1一定能凑到*/
		int range = 1;
		// arr[0] == 1
		for (int i = 1; i != arr.length; i++) {
			if (arr[i] > range + 1) {
				/*大于了就返回结果*/
				return range + 1;
			} else {
				range += arr[i];
			}
		}
		return range + 1;
	}

	public static int[] generateArray(int len, int maxValue) {
		int[] res = new int[len];
		for (int i = 0; i != res.length; i++) {
			res[i] = (int) (Math.random() * maxValue) + 1;
		}
		return res;
	}

	public static void printArray(int[] arr) {
		for (int i = 0; i != arr.length; i++) {
			System.out.print(arr[i] + " ");
		}
		System.out.println();
	}

	public static void main(String[] args) {
		int len = 27;
		int max = 30;
		int[] arr = generateArray(len, max);
		printArray(arr);
		long start = System.currentTimeMillis();
		System.out.println(unformedSum1(arr));
		long end = System.currentTimeMillis();
		System.out.println("cost time: " + (end - start) + " ms");
		System.out.println("======================================");

		start = System.currentTimeMillis();
		System.out.println(unformedSum2(arr));
		end = System.currentTimeMillis();
		System.out.println("cost time: " + (end - start) + " ms");
		System.out.println("======================================");

		System.out.println("set arr[0] to 1");
		arr[0] = 1;
		start = System.currentTimeMillis();
		System.out.println(unformedSum3(arr));
		end = System.currentTimeMillis();
		System.out.println("cost time: " + (end - start) + " ms");

	}
}
