package class09;

import java.util.ArrayList;
import java.util.List;

/**
 * 给你一个由若干括号和字母组成的字符串 s ，删除最小数量的无效括号，使得输入的字符串有效。返回所有可能的结果。答案可以按任意顺序返回
 * 解题：
 * 怎么删括号？
 * 	从左往右检查，遇到第一个多余的右括号，表示前面可以删除一个右括号来让当前的前缀合法
 * 前面有很多右括号怎么删？
 * 	从第一个右括号开始删，但是相邻的右括号，删哪个得到的结果都一样，所以相邻的右括号删任意一个只能收集到1种结果
 * 本题要求收集所有情况，所以一定是暴力过程，重点在于如何剪枝，以优化常数时间
 */
// 测试链接 : https://leetcode.com/problems/remove-invalid-parentheses/
public class Code02_RemoveInvalidParentheses {

	// 来自leetcode投票第一的答案，实现非常好，我们来赏析一下
	public static List<String> removeInvalidParentheses(String s) {
		List<String> ans = new ArrayList<>();
		remove(s, ans, 0, 0, new char[] { '(', ')' });
		return ans;
	}

	// deleteIndex <= checkIndex
	// 只查s[checkIndex....]的部分，因为之前的一定已经调整对了
	// 但是之前的部分是怎么调整对的，调整到了哪？就是deleteIndex
	// 比如：
	// ( ( ) ( ) ) ) ...
	// 0 1 2 3 4 5 6
	// 一开始当然checkIndex = 0，deleteIndex = 0
	// 当查到6的时候，发现不对了，
	// 然后可以去掉2位置、4位置的 )，都可以
	// 如果去掉2位置的 ), 那么下一步就是
	// ( ( ( ) ) ) ...
	// 0 1 2 3 4 5 6
	// checkIndex = 6 ，deleteIndex = 2
	// 如果去掉4位置的 ), 那么下一步就是
	// ( ( ) ( ) ) ...
	// 0 1 2 3 4 5 6
	// checkIndex = 6 ，deleteIndex = 4
	// 也就是说，
	// checkIndex和deleteIndex，分别表示查的开始 和 调整的开始，之前的都不用管了； par  (  )，par是点睛之笔
	public static void remove(String s, List<String> ans, int checkIndex, int deleteIndex, char[] par) {
		/*
		* count用来数数，当count变成-1表示需要调整了
		* */
		for (int count = 0, i = checkIndex; i < s.length(); i++) {
			if (s.charAt(i) == par[0]) {
				count++;
			}
			if (s.charAt(i) == par[1]) {
				count--;
			}
			// i check计数<0的第一个位置
			if (count < 0) {
				for (int j = deleteIndex; j <= i; ++j) {
					/*
					* 删除的右括号位置不能超过当前i的位置，即检查到的第一个不合法的位置
					* 哪些位置可以删？
					* 	当前位置是右括号，且当前位置左边不是
					* 删完以后，从(i,j)位置继续往后检查/删除
					* */
					if (s.charAt(j) == par[1] && (j == deleteIndex || s.charAt(j - 1) != par[1])) {
						/*删掉了j位置字符，传下去*/
						remove(
								s.substring(0, j) + s.substring(j + 1, s.length()),
								ans, i, j, par);
					}
				}
				/*大循环只找到了第一个不合法的右括号，就该return了*/
				return;
			}
		}
		/*上面的for里面没有return：说明没有不合规的左括号/右括号，那就反转一下*/
		String reversed = new StringBuilder(s).reverse().toString();
		if (par[0] == '(') {
			/*说明是没有不合规的右括号，反转之后寻找和调整不合规的左括号*/
			remove(reversed, ans, 0, 0, new char[] { ')', '(' });
		} else {
			/*说明是没有不合规的左括号，正反都查过了，反转之后直接添加答案*/
			ans.add(reversed);
		}
	}

}
