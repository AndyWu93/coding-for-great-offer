package class30;

/**
 * 给定一个数组（0~9）组成的str，将数字（1~26）转化为字母（a~z），问有多少种转化方式
 * 思路：动态dp，从左往右的尝试模型
 * dp[i]:i位置之前的字符都转好了，str[i..n-1]有多少种转化方式
 * 规模：dp[n+1]
 * dp[n]=1：没有字符了，收集到了1种转化结果
 * 普遍位置dp[i]:
 * a.str[i]=='0' dp[i]=0:以0开头的字符串无法转
 * b.str[i]!='0' dp[i]=dp[i+1]+dp[i+2] (dp[i+2]需要判断一下i和i+1位置组合起立的数字是否小于26才加上去)
 * 从左往右填dp
 */
public class Problem_0091_DecodeWays {

	public static int numDecodings1(String s) {
		if (s == null || s.length() == 0) {
			return 0;
		}
		char[] str = s.toCharArray();
		return process(str, 0);
	}

	// 潜台词：str[0...index-1]已经转化完了，不用操心了
	// str[index....] 能转出多少有效的，返回方法数
	public static int process(char[] str, int index) {
		if (index == str.length) {
			return 1;
		}
		if (str[index] == '0') {
			return 0;
		}
		int ways = process(str, index + 1);
		if (index + 1 == str.length) {
			return ways;
		}
		int num = (str[index] - '0') * 10 + str[index + 1] - '0';
		if (num < 27) {
			ways += process(str, index + 2);
		}
		return ways;
	}

	public static int numDecodings2(String s) {
		if (s == null || s.length() == 0) {
			return 0;
		}
		char[] str = s.toCharArray();
		int N = str.length;
		// dp[i] -> process(str, index)返回值 index 0 ~ N
		int[] dp = new int[N + 1];
		dp[N] = 1;

		// dp依次填好 dp[i] dp[i+1] dp[i+2]
		for (int i = N - 1; i >= 0; i--) {
			if (str[i] != '0') {
				dp[i] = dp[i + 1];
				if (i + 1 == str.length) {
					continue;
				}
				int num = (str[i] - '0') * 10 + str[i + 1] - '0';
				if (num <= 26) {
					dp[i] += dp[i + 2];
				}
			}
		}
		return dp[0];
	}

	public static int numDecodings(String s) {
		if (s == null || s.length() == 0) {
			return 0;
		}
		char[] str = s.toCharArray();
		int N = str.length;
		int[] dp = new int[N + 1];
		dp[N] = 1;
		for (int i = N - 1; i >= 0; i--) {
			if (str[i] == '0') {
				dp[i] = 0;
			} else if (str[i] == '1') {
				dp[i] = dp[i + 1];
				if (i + 1 < N) {
					dp[i] += dp[i + 2];
				}
			} else if (str[i] == '2') {
				dp[i] = dp[i + 1];
				if (i + 1 < str.length && (str[i + 1] >= '0' && str[i + 1] <= '6')) {
					dp[i] += dp[i + 2];
				}
			} else {
				dp[i] = dp[i + 1];
			}
		}
		return dp[0];
	}

}
