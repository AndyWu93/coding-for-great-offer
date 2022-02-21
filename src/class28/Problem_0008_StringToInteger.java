package class28;

/**
 * Implement the myAtoi(string s) function, which converts a string to a 32-bit signed integer (similar to C/C++'s atoi function).
 * The algorithm for myAtoi(string s) is as follows:
 * 	Read in and ignore any leading whitespace.
 * 	Check if the next character (if not already at the end of the string) is '-' or '+'. Read this character in if it is either. This determines if the final result is negative or positive respectively. Assume the result is positive if neither is present.
 * 	Read in next the characters until the next non-digit character or the end of the input is reached. The rest of the string is ignored.
 * 	Convert these digits into an integer (i.e. "123" -> 123, "0032" -> 32). If no digits were read, then the integer is 0. Change the sign as necessary (from step 2).
 * 	If the integer is out of the 32-bit signed integer range [-231, 231 - 1], then clamp the integer so that it remains in the range. Specifically, integers less than -231 should be clamped to -231, and integers greater than 231 - 1 should be clamped to 231 - 1.
 * 	Return the integer as the final result.
 * Note:
 * Only the space character ' ' is considered a whitespace character.
 * Do not ignore any characters other than the leading whitespace or the rest of the string after the digits.
 *
 * 题意：string转int
 * 主逻辑：
 * 	将读到的数字用负数计算出来。最后按正常的要求返回
 * 	为什么？因为负数可以兼顾正数，正数不能兼顾负数的范围
 *
 */
public class Problem_0008_StringToInteger {

	public static int myAtoi(String s) {
		if (s == null || s.equals("")) {
			return 0;
		}
		/*前面是0过滤掉*/
		s = removeHeadZero(s.trim());
		if (s == null || s.equals("")) {
			return 0;
		}
		char[] str = s.toCharArray();
		if (!isValid(str)) {
			return 0;
		}
		/*主逻辑开始*/
		// str 是符合日常书写的，正经整数形式
		boolean posi = str[0] == '-' ? false : true;
		int minq = Integer.MIN_VALUE / 10;
		int minr = Integer.MIN_VALUE % 10;
		int res = 0;
		int cur = 0;
		/*i从几开始？第一个数如果是符号，就从1开始，否则从0开始*/
		for (int i = (str[0] == '-' || str[0] == '+') ? 1 : 0; i < str.length; i++) {
			// 3  cur = -3   '5'  cur = -5    '0' cur = 0
			/*如果将读到的string转为负数？用'0'-*/
			cur = '0' - str[i];
			if ((res < minq) || (res == minq && cur < minr)) {
				/*过滤一下溢出的数*/
				return posi ? Integer.MAX_VALUE : Integer.MIN_VALUE;
			}
			/*之前的结果乘10，加现在的*/
			res = res * 10 + cur;
		}
		// res 负
		if (posi && res == Integer.MIN_VALUE) {
			/*res是系统最小值，但符号值正*/
			return Integer.MAX_VALUE;
		}
		return posi ? -res : res;
	}

	public static String removeHeadZero(String str) {
		boolean r = (str.startsWith("+") || str.startsWith("-"));
		int s = r ? 1 : 0;
		for (; s < str.length(); s++) {
			if (str.charAt(s) != '0') {
				break;
			}
		}
		// s 到了第一个不是'0'字符的位置
		int e = -1;
		// 左<-右
		for (int i = str.length() - 1; i >= (r ? 1 : 0); i--) {
			if (str.charAt(i) < '0' || str.charAt(i) > '9') {
				e = i;
			}
		}
		// e 到了最左的 不是数字字符的位置
		return (r ? String.valueOf(str.charAt(0)) : "") + str.substring(s, e == -1 ? str.length() : e);
	}

	public static boolean isValid(char[] chas) {
		if (chas[0] != '-' && chas[0] != '+' && (chas[0] < '0' || chas[0] > '9')) {
			return false;
		}
		if ((chas[0] == '-' || chas[0] == '+') && chas.length == 1) {
			return false;
		}
		// 0 +... -... num
		for (int i = 1; i < chas.length; i++) {
			if (chas[i] < '0' || chas[i] > '9') {
				return false;
			}
		}
		return true;
	}
	
	public static void main(String[] args) {
		System.out.println(Integer.MAX_VALUE);
	}
	
	

}
