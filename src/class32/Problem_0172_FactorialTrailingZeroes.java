package class32;

/**
 * Given an integer n, return the number of trailing zeroes in n!.
 * 题意：求 n！末尾有几个0？
 * 解题：
 * 	假设n=20,
 * 	20*19*...*1
 * 	将每个数分解成因子后会发现因子2比因子5出现的更为频繁，所以每出现一个因子5，末尾就有1个0
 * 	该题化解为1~n中有多少个因子5
 * 		1） 每5个数就有一个因子5
 * 		2） 每(5*5)个数就额外有一个因子5
 * 		2） 每(5*5*5)个数就额外有一个因子5
 * 		...
 * 	所以，该题进一步转为求n/5 + n/(5*5) + n/(5*5*5) + ... + 0
 */
public class Problem_0172_FactorialTrailingZeroes {

	public static int trailingZeroes(int n) {
		int ans = 0;
		while (n != 0) {
			n /= 5;
			ans += n;
		}
		return ans;
	}

}
