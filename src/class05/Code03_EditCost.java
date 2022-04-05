package class05;

/**
 * 给你两个单词 word1 和 word2， 请返回将 word1 转换成 word2 所使用的最少操作数  。
 * 你可以对一个单词进行如下三种操作：
 * 插入一个字符
 * 删除一个字符
 * 替换一个字符
 *
 * 编辑距离问题：
 * s1,s2一个样本作行，一个样本作列的动态规划问题
 * new dp[s1.length+1][s2.length+1]表，含义：dp[i][j]:将 长度 为i的s1转成 长度 为j的s2需要的cost
 * 情况讨论：来到了dp[i][j]
 * 1）i和j长度的s1s2最后一个字符是否相同？相同：dp[i][j]=dp[i-1][j-1],不相同：dp[i][j]=dp[i-1][j-1]+replaceCost
 * 2）i转成j-1 + insertCost
 * 3) i-1转成j + deleteCost
 * 三者取最小就是dp[i][j]的值
 * dp[s1.length][s2.length] 就是题解
 */
public class Code03_EditCost {

	public static int minCost1(String s1, String s2, int ic, int dc, int rc) {
		if (s1 == null || s2 == null) {
			return 0;
		}
		char[] str1 = s1.toCharArray();
		char[] str2 = s2.toCharArray();
		//讨论的是s1s2的长度问题，所以需要+1
		int N = str1.length + 1;
		int M = str2.length + 1;
		int[][] dp = new int[N][M];
		// dp[0][0]  = 0
		for (int i = 1; i < N; i++) {
			//s1转成空，删除就行
			dp[i][0] = dc * i;
		}
		for (int j = 1; j < M; j++) {
			//空转成s2，插入就行
			dp[0][j] = ic * j;
		}
		for (int i = 1; i < N; i++) {
			for (int j = 1; j < M; j++) {
				dp[i][j] = dp[i - 1][j - 1] + (str1[i - 1] == str2[j - 1] ? 0 : rc);
				dp[i][j] = Math.min(dp[i][j], dp[i][j - 1] + ic);
				dp[i][j] = Math.min(dp[i][j], dp[i - 1][j] + dc);
			}
		}
		return dp[N - 1][M - 1];
	}

	/**
	 * 空间压缩而已
	 */
	public static int minCost2(String str1, String str2, int ic, int dc, int rc) {
		if (str1 == null || str2 == null) {
			return 0;
		}
		char[] chs1 = str1.toCharArray();
		char[] chs2 = str2.toCharArray();
		char[] longs = chs1.length >= chs2.length ? chs1 : chs2;
		char[] shorts = chs1.length < chs2.length ? chs1 : chs2;
		if (chs1.length < chs2.length) {
			int tmp = ic;
			ic = dc;
			dc = tmp;
		}
		int[] dp = new int[shorts.length + 1];
		for (int i = 1; i <= shorts.length; i++) {
			dp[i] = ic * i;
		}
		for (int i = 1; i <= longs.length; i++) {
			int pre = dp[0];
			dp[0] = dc * i;
			for (int j = 1; j <= shorts.length; j++) {
				int tmp = dp[j];
				if (longs[i - 1] == shorts[j - 1]) {
					dp[j] = pre;
				} else {
					dp[j] = pre + rc;
				}
				dp[j] = Math.min(dp[j], dp[j - 1] + ic);
				dp[j] = Math.min(dp[j], tmp + dc);
				pre = tmp;
			}
		}
		return dp[shorts.length];
	}

	public static void main(String[] args) {
		String str1 = "ab12cd3";
		String str2 = "abcdf";
		System.out.println(minCost1(str1, str2, 5, 3, 2));
		System.out.println(minCost2(str1, str2, 5, 3, 2));

		str1 = "abcdf";
		str2 = "ab12cd3";
		System.out.println(minCost1(str1, str2, 3, 2, 4));
		System.out.println(minCost2(str1, str2, 3, 2, 4));

		str1 = "";
		str2 = "ab12cd3";
		System.out.println(minCost1(str1, str2, 1, 7, 5));
		System.out.println(minCost2(str1, str2, 1, 7, 5));

		str1 = "abcdf";
		str2 = "";
		System.out.println(minCost1(str1, str2, 2, 9, 8));
		System.out.println(minCost2(str1, str2, 2, 9, 8));

	}

}
