package class31;

/**
 * A phrase is a palindrome if, after converting all uppercase letters into lowercase letters and removing all non-alphanumeric characters, it reads the same forward and backward. Alphanumeric characters include letters and numbers.
 *
 * Given a string s, return true if it is a palindrome, or false otherwise.
 * 题意：判断一个可能包含数字和字母的str是否回文，忽略大小写、空格
 * 解题：
 * 	左右指针，过滤掉无效的字符，遇到有效的进行比对
 */
public class Problem_0125_ValidPalindrome {

	// 忽略空格、忽略大小写 -> 是不是回文
	// 数字不在忽略大小写的范围内
	public static boolean isPalindrome(String s) {
		if (s == null || s.length() == 0) {
			return true;
		}
		char[] str = s.toCharArray();
		int L = 0;
		int R = str.length - 1;
		/*两个指针不需要相等，因为如果有相等的情况，一定是中间一个字符，天然回文*/
		while (L < R) {
			/*都是有效的才比对*/
			// 英文（大小写） + 数字
			if (validChar(str[L]) && validChar(str[R])) {
				if (!equal(str[L], str[R])) {
					return false;
				}
				L++;
				R--;
			} else {
				/*遇到无效的char就移动指针*/
				L += validChar(str[L]) ? 0 : 1;
				R -= validChar(str[R]) ? 0 : 1;
			}
		}
		return true;
	}

	public static boolean validChar(char c) {
		return isLetter(c) || isNumber(c);
	}

	/*
	* 两个char大小写字母相差32个字符
	* */
	public static boolean equal(char c1, char c2) {
		if (isNumber(c1) || isNumber(c2)) {
			return c1 == c2;
		}
		// a  A   32
		// b  B   32
		// c  C   32
		return (c1 == c2) || (Math.max(c1, c2) - Math.min(c1, c2) == 32);
	}

	public static boolean isLetter(char c) {
		return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
	}

	public static boolean isNumber(char c) {
		return (c >= '0' && c <= '9');
	}

}
