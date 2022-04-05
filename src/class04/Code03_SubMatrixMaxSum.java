package class04;

/**
 * 求最大子矩阵累加和
 * 子矩阵：矩阵中一个矩形的矩阵，用两个点可以表示出来
 * 解题：
 * 	枚举所有列的长度为M的所有子矩阵，压缩成数组后求最大子数组累加和
 * 	复杂度O(N^2 * M)
 */
public class Code03_SubMatrixMaxSum {

	public static int maxSum(int[][] m) {
		if (m == null || m.length == 0 || m[0].length == 0) {
			return 0;
		}
		// O(N^2 * M)
		int N = m.length;
		int M = m[0].length;
		int max = Integer.MIN_VALUE;
		for (int i = 0; i < N; i++) {
			/*从第i行开始，往下压缩数组*/
			int[] s = new int[M];
			for (int j = i; j < N; j++) {
				for (int k = 0; k < M; k++) {
					/*存入s数组中*/
					s[k] += m[j][k];
				}
				/*每压入一行进去以后，计算并收集一下此时最大子数组累加和*/
				max = Math.max(max, maxSubArray(s));
			}
		}
		return max;
	}
	
	public static int maxSubArray(int[] arr) {
		if (arr == null || arr.length == 0) {
			return 0;
		}
		int max = Integer.MIN_VALUE;
		int cur = 0;
		for (int i = 0; i < arr.length; i++) {
			cur += arr[i];
			max = Math.max(max, cur);
			cur = cur < 0 ? 0 : cur;
		}
		return max;
	}

	/**
	 * 返回最大子矩阵的左上角和右下角下标
	 */
	// 本题测试链接 : https://leetcode-cn.com/problems/max-submatrix-lcci/
	public static int[] getMaxMatrix(int[][] m) {
		int N = m.length;
		int M = m[0].length;
		int max = Integer.MIN_VALUE;
		int cur = 0;
		int a = 0;
		int b = 0;
		int c = 0;
		int d = 0;
		for (int i = 0; i < N; i++) {
			int[] s = new int[M];
			for (int j = i; j < N; j++) {
				/*
				* cur：此时数组中的最大子数组的累加和
				* begin：这个累加和从哪一列开始的
				* */
				cur = 0;
				int begin = 0;
				for (int k = 0; k < M; k++) {
					s[k] += m[j][k];
					cur += s[k];
					if (max < cur) {
						/*收集的时候记录一下此时的两个点*/
						max = cur;
						a = i;
						b = begin;
						c = j;
						d = k;
					}
					if (cur < 0) {
						/*累加和成了负数了，cur归零，换个开头*/
						cur = 0;
						begin = k + 1;
					}
				}
			}
		}
		return new int[] { a, b, c, d };
	}

}
