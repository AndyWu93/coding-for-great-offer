package class34;

/**
 * Given an integer n, return true if it is a power of three. Otherwise, return false.
 *
 * An integer n is a power of three, if there exists an integer x such that n == 3x.
 * 题意：判断n是否是3的某次幂
 * 解题：
 * 	用3的最大次幂 % n, 结果等于0就是，否则就不是
 */
public class Problem_0326_PowerOfThree {

	// 如果一个数字是3的某次幂，那么这个数一定只含有3这个质数因子
	// 1162261467是int型范围内，最大的3的幂，它是3的19次方
	// 这个1162261467只含有3这个质数因子，如果n也是只含有3这个质数因子，那么
	// 1162261467 % n == 0
	// 反之如果1162261467 % n != 0 说明n一定含有其他因子
	public static boolean isPowerOfThree(int n) {
		return (n > 0 && 1162261467 % n == 0);
	}

}
