package class20;

/**
 * 给定一个字符串str，当然可以生成很多子序列，返回有多少个子序列是回文子序列，空序列不算回文
 * 比如，str = “aba”，回文子序列：{a}、{a}、 {a,a}、 {b}、{a,b,a}，返回5
 *
 * 解题：本题为动态规划，范围尝试模型
 * dp[i][j]:从i位置到j位置的字符串的所有子序列中的回文串个数，注意构成子序列的位置不同代表不同的子序列，空字符串不是回文串
 * 规模dp[n][n]
 * 1. 第一条对角线：只有1个字符，只有1种方案，就是取该字符
 * 2. 第二条对角线：只有2个字符，
 * 如果两个字符相等，3个保留方案：保留第一个字符，保留第二个字符，两个都保留
 * 如果两个字符不相等，2个保留方案：保留第一个字符，保留第二个字符
 * 3. 普遍位置：dp[L][R]
 * 	a. 不保留L不保留R
 * 	b. 保留L不保留R
 * 	c. 不保留L保留R
 * 	d. 保留L保留R(成立条件:str[L]==str[R])
 * 	dp[L][R]abcd四中方案的方法数之和
 * 	但是依赖比较特殊：
 * 	dp[L+1][R-1]=a
 * 	dp[L+1][R]=a+c,因为L+1~R的回文串个数包含了L+1~R-1的回文串个数
 * 	dp[L][R-1]=a+b,因为L~R-1的回文串个数包含了L+1~R-1的回文串个数
 * 	d=a+1,LR位置都保留，就看L+1~R-1位置的回文串个数，+1是表示只保留LR，中间都略去，因为空字符串不算在a中，所以要+1
 */
public class Code04_PalindromeWays {

	public static int way(String str) {
		char[] s = str.toCharArray();
		int len = s.length;
		int[][] dp = new int[len + 1][len + 1];
		for (int i = 0; i <= len; i++) {
			dp[i][i] = 1;
		}
		// dp[i][j]，在空串不算回文串的情况下，求str[i..j]有多少不同的回文子序列
		// index -> dp
		// str[index-1]
		// dp 1 str 0
		// dp 2 str 1
		for (int subLen = 2; subLen <= len; subLen++) {
			for (int l = 1; l <= len - subLen + 1; l++) {
				int r = l + subLen - 1;
				dp[l][r] += dp[l + 1][r];
				dp[l][r] += dp[l][r - 1];
				if (s[l - 1] == s[r - 1])
					dp[l][r] += 1;
				else
					dp[l][r] -= dp[l + 1][r - 1];
			}
		}
		return dp[1][len];
	}


	/**
	 * 暴力解：生成所有的子序列，逐个验证
	 */
	public static int ways1(String str) {
		if (str == null || str.length() == 0) {
			return 0;
		}
		char[] s = str.toCharArray();
		char[] path = new char[s.length];
		return process(str.toCharArray(), 0, path, 0);
	}

	public static int process(char[] s, int si, char[] path, int pi) {
		if (si == s.length) {
			return isP(path, pi) ? 1 : 0;
		}
		int ans = process(s, si + 1, path, pi);
		path[pi] = s[si];
		ans += process(s, si + 1, path, pi + 1);
		return ans;
	}

	public static boolean isP(char[] path, int pi) {
		if (pi == 0) {
			return false;
		}
		int L = 0;
		int R = pi - 1;
		while (L < R) {
			if (path[L++] != path[R--]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * dp
	 */
	public static int ways2(String str) {
		if (str == null || str.length() == 0) {
			return 0;
		}
		char[] s = str.toCharArray();
		int n = s.length;
		int[][] dp = new int[n][n];
		for (int i = 0; i < n; i++) {
			/*第一条对角线*/
			dp[i][i] = 1;
		}
		for (int i = 0; i < n - 1; i++) {
			/*第二条对角线，i只能到n-2位置*/
			dp[i][i + 1] = s[i] == s[i + 1] ? 3 : 2;
		}
		/*从倒数第3行往上，每行从左往右填写*/
		for (int L = n - 3; L >= 0; L--) {
			/*第一条对角线(L)和第二条对角线(L+1)都填好了，所以R从L+2位置开始*/
			for (int R = L + 2; R < n; R++) {
				/*a+b+c*/
				dp[L][R] = dp[L + 1][R] + dp[L][R - 1] - dp[L + 1][R - 1];
				if (s[L] == s[R]) {
					/*+d*/
					dp[L][R] += dp[L + 1][R - 1] + 1;
				}
			}
		}
		return dp[0][n - 1];
	}

	public static String randomString(int len, int types) {
		char[] str = new char[len];
		for (int i = 0; i < str.length; i++) {
			str[i] = (char) ('a' + (int) (Math.random() * types));
		}
		return String.valueOf(str);
	}

	public static void main(String[] args) {
		int N = 10;
		int types = 5;
		int testTimes = 100000;
		System.out.println("测试开始");
		for (int i = 0; i < testTimes; i++) {
			int len = (int) (Math.random() * N);
			String str = randomString(len, types);
			int ans1 = ways1(str);
			int ans2 = ways2(str);
			if (ans1 != ans2) {
				System.out.println(str);
				System.out.println(ans1);
				System.out.println(ans2);
				System.out.println("Oops!");
				break;
			}
		}
		System.out.println("测试结束");
	}

}
