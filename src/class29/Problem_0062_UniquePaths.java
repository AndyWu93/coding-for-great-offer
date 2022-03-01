package class29;

/**
 *
 There is a robot on an m x n grid. The robot is initially located at the top-left corner (i.e., grid[0][0]). The robot tries to move to the bottom-right corner (i.e., grid[m - 1][n - 1]). The robot can only move either down or right at any point in time.

 Given the two integers m and n, return the number of possible unique paths that the robot can take to reach the bottom-right corner.

 The test cases are generated so that the answer will be less than or equal to 2 * 109.
 * 题意：给定m*n的矩阵，从左上角到右下角有多少条不同的路径
 * 解题：
 * 	本题无需动态规划，是一个数学题目：
 * 	左上角到右下角，不管怎么走，都是向右走了n-1步，向下走了m-1步，所以
 * 	结果就是
 * 	从n+m-2步中，任意拿出n-1步向右
 * 或者
 * 	从n+m-2步中，任意拿出m-1步向下
 * 	即C (n-1) @ (n+m-2)
 */
public class Problem_0062_UniquePaths {

	// m 行
	// n 列
	// 下：m-1
	// 右：n-1
	public static int uniquePaths(int m, int n) {
		int right = n - 1;
		int all = m + n - 2;
		/*
		* 计算C right @ all 时，阶乘计算可能会溢出，所以，先将一些数约掉，成了两个乘句相除
		* o1：上面遇到的乘数
		* o2：下面遇到的乘数
		* o1乘进去的个数 一定等于 o2乘进去的个数
		* */
		long o1 = 1;
		long o2 = 1;
		for (int i = right + 1, j = 1; i <= all; i++, j++) {
			o1 *= i;
			o2 *= j;
			/*o1o2乘完后，先约到最大公约数，再继续乘*/
			long gcd = gcd(o1, o2);
			o1 /= gcd;
			o2 /= gcd;
		}
		return (int) o1;
	}

	/*
	 * 最大公约数的求法，需要背诵
	 * 调用的时候，请保证初次调用时，m和n都不为0
	* */
	public static long gcd(long m, long n) {
		return n == 0 ? m : gcd(n, m % n);
	}

}
