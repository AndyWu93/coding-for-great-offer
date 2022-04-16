package class10;

/**
 * 给定一个布尔表达式和一个期望的布尔结果 result，布尔表达式由 0 (false)、1 (true)、& (AND)、 | (OR) 和 ^ (XOR) 符号组成。实现一个函数，算出有几种可使该表达式得出 result 值的括号方法。
 *
 * 题意：给表达式随意画括号，返回T/F的方法数
 * 解题：
 * 	思路：枚举每一个逻辑符号，作为最后一步运算，分别求出左右两边的状态和对应方法数，再根据当前枚举的逻辑符号做运算
 * 	即范围尝试模型
 */
// 本题测试链接 : https://leetcode-cn.com/problems/boolean-evaluation-lcci/
public class Code05_BooleanEvaluation {

	/**
	 * 范围尝试模型
	 */
	public static int countEval0(String express, int desired) {
		if (express == null || express.equals("")) {
			return 0;
		}
		char[] exp = express.toCharArray();
		int N = exp.length;
		Info[][] dp = new Info[N][N];
		Info allInfo = func(exp, 0, exp.length - 1, dp);
		return desired == 1 ? allInfo.t : allInfo.f;
	}

	public static class Info {
		/*返回T的方法数*/
		public int t;
		/*返回F的方法数*/
		public int f;

		public Info(int tr, int fa) {
			t = tr;
			f = fa;
		}
	}

	// 限制:
	// L...R上，一定有奇数个字符
	// L位置的字符和R位置的字符，非0即1，不能是逻辑符号！
	// 返回str[L...R]这一段，为true的方法数，和false的方法数
	public static Info func(char[] str, int L, int R, Info[][] dp) {
		if (dp[L][R] != null) {
			return dp[L][R];
		}
		int t = 0;
		int f = 0;
		if (L == R) {
			/*只有一个数，就看这个数是T还是F*/
			t = str[L] == '1' ? 1 : 0;
			f = str[L] == '0' ? 1 : 0;
		} else { // L..R >=3
			// 都去试试最后结合
			/*split：枚举str中的逻辑符号，L/R位置一定是数字，不能枚举，步长为2*/
			for (int split = L + 1; split < R; split += 2) {
				Info leftInfo = func(str, L, split - 1, dp);
				Info rightInfo = func(str, split + 1, R, dp);
				int a = leftInfo.t;
				int b = leftInfo.f;
				int c = rightInfo.t;
				int d = rightInfo.f;
				switch (str[split]) {
				case '&':
					/*注意是累加*/
					t += a * c;
					f += b * c + b * d + a * d;
					break;
				case '|':
					t += a * c + a * d + b * c;
					f += b * d;
					break;
				case '^':
					t += a * d + b * c;
					f += a * c + b * d;
					break;
				}
			}

		}
		dp[L][R] = new Info(t, f);
		return dp[L][R];
	}

	public static int countEval1(String express, int desired) {
		if (express == null || express.equals("")) {
			return 0;
		}
		char[] exp = express.toCharArray();
		return f(exp, desired, 0, exp.length - 1);
	}

	public static int f(char[] str, int desired, int L, int R) {
		if (L == R) {
			if (str[L] == '1') {
				return desired;
			} else {
				return desired ^ 1;
			}
		}
		int res = 0;
		if (desired == 1) {
			for (int i = L + 1; i < R; i += 2) {
				switch (str[i]) {
				case '&':
					res += f(str, 1, L, i - 1) * f(str, 1, i + 1, R);
					break;
				case '|':
					res += f(str, 1, L, i - 1) * f(str, 0, i + 1, R);
					res += f(str, 0, L, i - 1) * f(str, 1, i + 1, R);
					res += f(str, 1, L, i - 1) * f(str, 1, i + 1, R);
					break;
				case '^':
					res += f(str, 1, L, i - 1) * f(str, 0, i + 1, R);
					res += f(str, 0, L, i - 1) * f(str, 1, i + 1, R);
					break;
				}
			}
		} else {
			for (int i = L + 1; i < R; i += 2) {
				switch (str[i]) {
				case '&':
					res += f(str, 0, L, i - 1) * f(str, 1, i + 1, R);
					res += f(str, 1, L, i - 1) * f(str, 0, i + 1, R);
					res += f(str, 0, L, i - 1) * f(str, 0, i + 1, R);
					break;
				case '|':
					res += f(str, 0, L, i - 1) * f(str, 0, i + 1, R);
					break;
				case '^':
					res += f(str, 1, L, i - 1) * f(str, 1, i + 1, R);
					res += f(str, 0, L, i - 1) * f(str, 0, i + 1, R);
					break;
				}
			}
		}
		return res;
	}

	public static int countEval2(String express, int desired) {
		if (express == null || express.equals("")) {
			return 0;
		}
		char[] exp = express.toCharArray();
		int N = exp.length;
		int[][][] dp = new int[2][N][N];
		dp[0][0][0] = exp[0] == '0' ? 1 : 0;
		dp[1][0][0] = dp[0][0][0] ^ 1;
		for (int i = 2; i < exp.length; i += 2) {
			dp[0][i][i] = exp[i] == '1' ? 0 : 1;
			dp[1][i][i] = exp[i] == '0' ? 0 : 1;
			for (int j = i - 2; j >= 0; j -= 2) {
				for (int k = j; k < i; k += 2) {
					if (exp[k + 1] == '&') {
						dp[1][j][i] += dp[1][j][k] * dp[1][k + 2][i];
						dp[0][j][i] += (dp[0][j][k] + dp[1][j][k]) * dp[0][k + 2][i] + dp[0][j][k] * dp[1][k + 2][i];
					} else if (exp[k + 1] == '|') {
						dp[1][j][i] += (dp[0][j][k] + dp[1][j][k]) * dp[1][k + 2][i] + dp[1][j][k] * dp[0][k + 2][i];
						dp[0][j][i] += dp[0][j][k] * dp[0][k + 2][i];
					} else {
						dp[1][j][i] += dp[0][j][k] * dp[1][k + 2][i] + dp[1][j][k] * dp[0][k + 2][i];
						dp[0][j][i] += dp[0][j][k] * dp[0][k + 2][i] + dp[1][j][k] * dp[1][k + 2][i];
					}
				}
			}
		}
		return dp[desired][0][N - 1];
	}

}
