package class32;

/**
 * Given a string columnTitle that represents the column title as appear in an Excel sheet, return its corresponding column number.
 *
 * For example:
 *
 * A -> 1
 * B -> 2
 * C -> 3
 * ...
 * Z -> 26
 * AA -> 27
 * AB -> 28
 * ...
 * 题意：EXCEL用字母表示数子，给定字母，返回对应的数字
 * 解题：
 * 	伪26进制
 */
public class Problem_0171_ExcelSheetColumnNumber {

	// 这道题反过来也要会写
	public static int titleToNumber(String s) {
		char[] str = s.toCharArray();
		int ans = 0;
		for (int i = 0; i < str.length; i++) {
			/*高位乘以26后加上低位，记得低位需要加1，因为没有0，最小的数就是1*/
			ans = ans * 26 + (str[i] - 'A') + 1;
		}
		return ans;
	}

}
