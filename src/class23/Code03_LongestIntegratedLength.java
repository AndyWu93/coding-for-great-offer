package class23;

import java.util.Arrays;
import java.util.HashSet;

/**
 * 定义什么是可整合数组：一个数组排完序之后，除了最左侧的数外，有arr[i] = arr[i-1]+1
 * 则称这个数组为可整合数组比如{5,1,2,4,3}、{6,2,3,1,5,4}都是可整合数组，返回arr中最长可整合子数组的长度
 *
 * 解题：
 * 	思路1：枚举所有的子数组，将每个子数组拷贝出来，排序后判断是否可整合数组，复杂度O(N^3*logN)，没分
 * 	思路2：枚举所有的子数组，
 * 		可整合数组，可以转化成等价的两个条件：
 * 		1. 数组内没有重复值
 * 		2. 数组内的max-min = length-1
 * 		这样，在判断该子数组是否可整合数组时可以O(1)获得，整体复杂度O(N^2)
 *
 * 总结：分析并转化题意，将复杂逻辑转化成等价的简单逻辑
 */
public class Code03_LongestIntegratedLength {

	/**
	 * 思路2， O(N^2)
	 */
	public static int maxLen(int[] arr) {
		if (arr == null || arr.length == 0) {
			return 0;
		}
		int N = arr.length;
		HashSet<Integer> set = new HashSet<>();
		/*可整合数组至少1个数*/
		int ans = 1;
		for (int L = 0; L < N; L++) {
			/*子数组换了开头，需要新的set，来统计这一段有没有重复值*/
			set.clear();
			/*最大值和最小值，一开始都是arr[L]*/
			int min = arr[L];
			int max = arr[L];
			set.add(arr[L]);
			// L..R
			for (int R = L + 1; R < N; R++) {
				// L....R
				if(set.contains(arr[R])) {
					/*遇到重复值了，该子数组肯定不是可整合，换个开头吧*/
					break;
				}
				/*否则满足条件1，收集max，min*/
				set.add(arr[R]);
				min = Math.min(min, arr[R]);
				max = Math.max(max, arr[R]);
				/*判断是否满足条件2，满足的话，说明是可整合，收集下长度*/
				if(max - min == R - L) {
					ans = Math.max(ans, R - L + 1);
				}
			}
		}
		return ans;

	}

	public static int getLIL1(int[] arr) {
		if (arr == null || arr.length == 0) {
			return 0;
		}
		int len = 0;
		// O(N^3 * log N)
		for (int start = 0; start < arr.length; start++) { // 子数组所有可能的开头
			for (int end = start; end < arr.length; end++) { // 开头为start的情况下，所有可能的结尾
				// arr[s ... e]
				// O(N * logN)
				if (isIntegrated(arr, start, end)) {
					len = Math.max(len, end - start + 1);
				}
			}
		}
		return len;
	}

	public static boolean isIntegrated(int[] arr, int left, int right) {
		int[] newArr = Arrays.copyOfRange(arr, left, right + 1); // O(N)
		Arrays.sort(newArr); // O(N*logN)
		for (int i = 1; i < newArr.length; i++) {
			if (newArr[i - 1] != newArr[i] - 1) {
				return false;
			}
		}
		return true;
	}

	public static int getLIL2(int[] arr) {
		if (arr == null || arr.length == 0) {
			return 0;
		}
		int len = 0;
		int max = 0;
		int min = 0;
		HashSet<Integer> set = new HashSet<Integer>();
		for (int L = 0; L < arr.length; L++) { // L 左边界
			// L .......
			set.clear();
			max = Integer.MIN_VALUE;
			min = Integer.MAX_VALUE;
			for (int R = L; R < arr.length; R++) { // R 右边界
				// arr[L..R]这个子数组在验证 l...R L...r+1 l...r+2
				if (set.contains(arr[R])) {
					// arr[L..R]上开始 出现重复值了，arr[L..R往后]不需要验证了，
					// 一定不是可整合的
					break;
				}
				// arr[L..R]上无重复值
				set.add(arr[R]);
				max = Math.max(max, arr[R]);
				min = Math.min(min, arr[R]);
				if (max - min == R - L) { // L..R 是可整合的
					len = Math.max(len, R - L + 1);
				}
			}
		}
		return len;
	}

	public static void main(String[] args) {
		int[] arr = { 5, 5, 3, 2, 6, 4, 3 };
		System.out.println(getLIL1(arr));
		System.out.println(getLIL2(arr));

	}

}
