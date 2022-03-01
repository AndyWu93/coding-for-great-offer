package class29;

/**
 * Since the return type is an integer, the decimal digits are truncated, and only the integer part of the result is returned.
 *
 * Note: You are not allowed to use any built-in exponent function or operator, such as pow(x, 0.5) or x ** 0.5.
 * 题意：求根号下x的值，返回去精度的int
 * 解题：
 * 	使用二分
 *
 * 进阶：在给定一个正整数k，返回k个精度的double
 * 解题：
 * 	先把整数部分求出来，就是上面的求法。精度部分使用二分
 * 		举例，k=4，那就在0和9999之间二分，看哪个数的平方最接近x
 */
public class Problem_0069_SqrtX {

	// x一定非负，输入可以保证
	public static int mySqrt(int x) {
		if (x == 0) {
			return 0;
		}
		if (x < 3) {
			return 1;
		}
		// x >= 3
		long ans = 1;
		long L = 1;
		long R = x;
		long M = 0;
		while (L <= R) {
			M = (L + R) / 2;
			if (M * M <= x) {
				/*如果M*M小于x，记一下此时的答案，直到范围上只剩下一个数，跳出while返回*/
				ans = M;
				L = M + 1;
			} else {
				R = M - 1;
			}
		}
		return (int) ans;
	}

}
