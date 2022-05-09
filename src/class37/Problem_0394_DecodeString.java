package class37;

/**
 * 给定一个经过编码的字符串，返回它解码后的字符串。
 * 编码规则为: k[encoded_string]，表示其中方括号内部的 encoded_string 正好重复 k 次。注意 k 保证为正整数。
 * 你可以认为输入字符串总是有效的；输入字符串中没有额外的空格，且输入的方括号总是符合格式要求的。
 * 此外，你可以认为原始数据不包含数字，所有的数字只表示重复的次数 k ，例如不会出现像 3a 或 2[4] 的输入。
 *
 * @see class08.Code01_ExpressionCompute 解析递归模型
 */
//Leetcode题目 : https://leetcode.com/problems/decode-string/
public class Problem_0394_DecodeString {

	public static String decodeString(String s) {
		char[] str = s.toCharArray();
		return process(str, 0).ans;
	}

	public static class Info {
		/*阶段求值*/
		public String ans;
		/*阶段结束位置*/
		public int stop;

		public Info(String a, int e) {
			ans = a;
			stop = e;
		}
	}

	// s[i....]  何时停？遇到   ']'  或者遇到 s的终止位置，停止
	// 返回Info
	// 0) 串
	// 1) 算到了哪
	public static Info process(char[] s, int i) {
		StringBuilder ans = new StringBuilder();
		/*解析出前面的数字*/
		int times = 0;
		/*直到遇到']'，或者结束*/
		while (i < s.length && s[i] != ']') {
			if ((s[i] >= 'a' && s[i] <= 'z') || (s[i] >= 'A' && s[i] <= 'Z')) {
				/*遇到字母，继续*/
				ans.append(s[i++]);
			} else if (s[i] >= '0' && s[i] <= '9') {
				/*遇到数字，转成int*/
				times = times * 10 + s[i++] - '0';
			} else { // str[index] = '['
				/*遇到'['，开下个位置的递归*/
				Info next = process(s, i + 1);
				/*拿到结果，翻times倍*/
				ans.append(timesString(times, next.ans));
				/*归零，i继续*/
				times = 0;
				i = next.stop + 1;
			}
		}
		return new Info(ans.toString(), i);
	}

	public static String timesString(int times, String str) {
		StringBuilder ans = new StringBuilder();
		for (int i = 0; i < times; i++) {
			ans.append(str);
		}
		return ans.toString();
	}

}
