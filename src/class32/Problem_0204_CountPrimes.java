package class32;

/**
 * Given an integer n, return the number of prime numbers that are strictly less than n.
 * 解题：
 * 	大体思路是从1到n-1,如果这个数是质数，就收集
 * 	代码里作了优化
 */
public class Problem_0204_CountPrimes {

	public static int countPrimes(int n) {
		if (n < 3) {
			/*不含n，所以这里只有1个数就是1，1不是质数*/
			return 0;
		}
		// j已经不是素数了，f[j] = true;
		boolean[] f = new boolean[n];
		/*count用于统计质数数量，一开始就除2，略过所有偶数*/
		int count = n / 2; // 所有偶数都不要，还剩几个数
		// 跳过了1、2    3、5、7、
		/*i列出所有的奇数，不含1、2。i * i < n？这里不判断，里面一个for循环里也会判断*/
		for (int i = 3; i * i < n; i += 2) {
			if (f[i]) {
				continue;
			}
			// i=3 -> 3 * 3 = 9   3 * 5 = 15   3 * 7 = 21
			// i=7 -> 7 * 7 = 49  7 * 9 = 63
			// i=13 -> 13 * 13  13 * 15
			/*能够算出来的j都不是质数*/
			for (int j = i * i; j < n; j += 2 * i) {
				if (!f[j]) {
					/*之前没收集过j，收集一下，统计*/
					--count;
					f[j] = true;
				}
			}
		}
		return count;
	}

}
