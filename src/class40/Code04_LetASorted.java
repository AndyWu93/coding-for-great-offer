package class40;

/**
 * 给定两个数组A和B，长度都是N
 * A[i]不可以在A中和其他数交换，只可以选择和B[i]交换(0<=i<n)
 * 你的目的是让A有序，返回你能不能做到
 *
 * 解题：
 * 	写递归
 */
public class Code04_LetASorted {

	public static boolean letASorted(int[] A, int[] B) {
		/*A[0]之前没有数了，所以传了系统最小值，不影响方法的计算*/
		return process(A, B, 0, Integer.MIN_VALUE);
	}

	// 当前推进到了i位置，对于A和B都是i位置
	// A[i]前一个数字，lastA
	// 能否通过题意中的操作，A[i] B[i] 让A整体有序
	public static boolean process(int[] A, int[] B, int i, int lastA) {
		if (i == A.length) {
			return true;
		}
		// 第一种选择 : A[i]不和B[i]交换
		if (A[i] >= lastA && process(A, B, i + 1, A[i])) {
			return true;
		}
		// 第二种选择 : A[i]和B[i]交换
		if (B[i] >= lastA && process(A, B, i + 1, B[i])) {
			return true;
		}
		/*两种选择都不行，那就是你不行*/
		return false;
	}

	/**
	 * 如果题意换成降序
	 */
	public static boolean process2(int[] A, int[] B, int i, int lastA) {
		if (i == A.length) {
			return true;
		}
		// 第一种选择 : A[i]不和B[i]交换
		if (A[i] <= lastA && process2(A, B, i + 1, A[i])) {
			return true;
		}
		// 第一种选择 : A[i]和B[i]交换
		if (B[i] <= lastA && process2(A, B, i + 1, B[i])) {
			return true;
		}
		return false;
	}

	// A B 操作 : A[i] 与 B[i] 交换！
	// 目的 : 让A和B都有序，能不能做到
//	public static boolean process3(int[] A, int[] B, int i, int lastA, int lastB) {
//
//	}

}
