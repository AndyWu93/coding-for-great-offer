package class04;

/**
 * 给定三个字符串s1、s2、s3，请你帮忙验证s3是否是由s1和s2交错组成的
 * 交错组成：指s1s2在s3中错开放置，但是s1s2各自的字符相等顺序不变
 *
 * 解题：
 * 	本题样本对应模型
 * dp[i][j]: s1[0..i] 和 s2[0..j] 是否是 s3[0..i+j]的交错组成
 * 样本对应模型可以直接画dp表来尝试，更直观
 */
// 本题测试链接 : https://leetcode.com/problems/interleaving-string/
public class Code07_InterleavingString {

	public static boolean isInterleave(String s1, String s2, String s3) {
		if (s1 == null || s2 == null || s3 == null) {
			return false;
		}
		char[] str1 = s1.toCharArray();
		char[] str2 = s2.toCharArray();
		char[] str3 = s3.toCharArray();
		if (str3.length != str1.length + str2.length) {
			/*s1长度+s2长度一定是s3的长度，否则不是交错组成*/
			return false;
		}
		/*s1s2可以一个都不取*/
		boolean[][] dp = new boolean[str1.length + 1][str2.length + 1];
		dp[0][0] = true;
		for (int i = 1; i <= str1.length; i++) {
			if (str1[i - 1] != str3[i - 1]) {
				break;
			}
			/*只拿s1组成s3*/
			dp[i][0] = true;
		}
		for (int j = 1; j <= str2.length; j++) {
			if (str2[j - 1] != str3[j - 1]) {
				break;
			}
			/*只拿s2组成s3**/
			dp[0][j] = true;
		}
		for (int i = 1; i <= str1.length; i++) {
			for (int j = 1; j <= str2.length; j++) {
				if (
						/*当前s1解结尾组成当前s3的结尾 ，s1s2其余的组成s3其余的，是交错组成*/
						(str1[i - 1] == str3[i + j - 1] && dp[i - 1][j])
						||
						/*当前s2解结尾组成当前s3的结尾 ，s1s2其余的组成s3其余的，是交错组成*/
						(str2[j - 1] == str3[i + j - 1] && dp[i][j - 1])
						) {
					dp[i][j] = true;
				}
			}
		}
		return dp[str1.length][str2.length];
	}

}
