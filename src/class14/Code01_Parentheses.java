package class14;

/**
 * 给定一个只由左括号和右括号的字符串，返回最长的有效括号子串的长度
 *
 */
public class Code01_Parentheses {

	/**
	 * 判断是否有效括号
	 * 用一个变量来统计 '('=1 ; ')'=-1
	 * 如果为负数，则肯定多了')'
	 */
	public static boolean valid(String s) {
		char[] str = s.toCharArray();
		int count = 0;
		for (int i = 0; i < str.length; i++) {
			count += str[i] == '(' ? 1 : -1;
			if (count < 0) {
				return false;
			}
		}
		return count == 0;
	}

	/**
	 * 计算需要多少个括号变合法
	 * 用一个变量来统计 '('=1 ; ')'=-1
	 * 用另一个变量统计当多出 ）时需要的（ 数量
	 */
	public static int needParentheses(String s) {
		char[] str = s.toCharArray();
		int count = 0;
		int need = 0;
		for (int i = 0; i < str.length; i++) {
			if (str[i] == '(') {
				count++;
			} else { // 遇到的是')'
				if (count == 0) {
					need++;
				} else {
					count--;
				}
			}
		}
		return count + need;
	}

	public static boolean isValid(char[] str) {
		if (str == null || str.length == 0) {
			return false;
		}
		int status = 0;
		for (int i = 0; i < str.length; i++) {
			if (str[i] != ')' && str[i] != '(') {
				return false;
			}
			if (str[i] == ')' && --status < 0) {
				return false;
			}
			if (str[i] == '(') {
				status++;
			}
		}
		return status == 0;
	}

	/**
	 * 求有效括号的嵌套层数：
	 * 如
	 * （（））：2
	 * （（（）））：3
	 */
	public static int deep(String s) {
		char[] str = s.toCharArray();
		if (!isValid(str)) {
			return 0;
		}
		int count = 0;
		int max = 0;
		for (int i = 0; i < str.length; i++) {
			if (str[i] == '(') {
				max = Math.max(max, ++count);
			} else {
				count--;
			}
		}
		return max;
	}

	/**
	 * 给定一个只由左括号和右括号的字符串，返回最长的有效括号子串的长度
	 * 解题：
	 * dp[i] : 子串必须以i位置结尾的情况下，往左最远能扩出多长的有效区域
	 */
	// s只由(和)组成
	// 求最长有效括号子串长度
	// 本题测试链接 : https://leetcode.com/problems/longest-valid-parentheses/
    public static int longestValidParentheses(String s) {
		if (s == null || s.length() < 2) {
			return 0;
		}
		char[] str = s.toCharArray();
		// dp[i] : 子串必须以i位置结尾的情况下，往左最远能扩出多长的有效区域
		int[] dp = new int[str.length];
		// dp[0] = 0; （  ）
		int pre = 0;
		int ans = 0;
		for (int i = 1; i < str.length; i++) {
			/*遇到'(',dp[i]=0,因为不可能以'('结尾*/
			if (str[i] == ')') {
				/*当遇到')'时，求前面一个配对的'(',但需要跳过已经配对成功的*/
				pre = i - dp[i - 1] - 1; // 与str[i]配对的左括号的位置 pre
				/*
				* pre >= 0：如果能够与自己配对的位置越界了，那么自己不可能是一个有效的')'
				* */
				if (pre >= 0 && str[pre] == '(') {
					/*记得要再往前看一下，把前面有效的括号数量加上*/
					dp[i] = dp[i - 1] + 2 + (pre > 0 ? dp[pre - 1] : 0);
				}
			}
			ans = Math.max(ans, dp[i]);
		}
		return ans;
	}

}
