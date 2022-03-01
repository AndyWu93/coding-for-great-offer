package class29;

/**
 * Implement pow(x, n), which calculates x raised to the power n (i.e., xn).
 * 题意：x的n次方，n可能是负数
 * 解题
 * 	x^n专用调度
 */
public class Problem_0050_PowXN {

	/*
	* x^n专用调度,体系班讲过
	* */
	public static int pow(int a, int n) {
		int ans = 1;
		int t = a;
		while (n != 0) {
			if ((n & 1) != 0) {
				ans *= t;
			}
			t *= t;
			n >>= 1;
		}
		return ans;
	}

	// x的n次方，n可能是负数
	public static double myPow(double x, int n) {
		if (n == 0) {
			return 1D;
		}
		/*拿到绝对值，如果是系统最小，绝对值就是系统最大值*/
		int pow = Math.abs(n == Integer.MIN_VALUE ? n + 1 : n);
		double t = x;
		double ans = 1D;
		while (pow != 0) {
			if ((pow & 1) != 0) {
				ans *= t;
			}
			pow >>= 1;
			t = t * t;
		}
		if (n == Integer.MIN_VALUE) {
			/*如果是系统最小，需要再单独乘一遍x*/
			ans *= x;
		}
		return n < 0 ? (1D / ans) : ans;
	}

}
