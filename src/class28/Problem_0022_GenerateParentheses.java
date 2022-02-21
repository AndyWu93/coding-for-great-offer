package class28;

import java.util.ArrayList;
import java.util.List;

/**
 * Given n pairs of parentheses, write a function to generate all combinations of well-formed parentheses.
 *
 * 题意：给n对括号，生成所有合法的形式
 * 解题：
 * 	本题考量递归设计和剪枝能力
 */
public class Problem_0022_GenerateParentheses {

	public static List<String> generateParenthesis(int n) {
		/*生成的括号*/
		char[] path = new char[n << 1];
		/*收集答案*/
		List<String> ans = new ArrayList<>();
		process(path, 0, 0, n, ans);
		return ans;
	}


	/*
	* path 做的决定  path[0....index-1]做完决定的！
	* path[index.....] 还没做决定，当前轮到index位置做决定
	* leftMinusRight:左括号-右括号的数量
	* leftRest：还是几个左括号可以使用
	* */
	public static void process(char[] path, int index, int leftMinusRight, int leftRest, List<String> ans) {
		if (index == path.length) {
			ans.add(String.valueOf(path));
		} else {
			// index (   )
			/*
			* index位置只能做两个决定：放左括号，或者右括号
			* 放左括号的条件：
			* 	还有剩余的左括号可以放
			* 放右括号的条件：
			* 	左括号的数量大于右括号的数量
			* */
			if (leftRest > 0) {
				path[index] = '(';
				/*index位置放好后，注意index+1位置后面两个参数的变化*/
				process(path, index + 1, leftMinusRight + 1, leftRest - 1, ans);
			}
			if (leftMinusRight > 0) {
				path[index] = ')';
				process(path, index + 1, leftMinusRight - 1, leftRest, ans);
			}
		}
	}

	// 不剪枝的做法
	public static List<String> generateParenthesis2(int n) {
		char[] path = new char[n << 1];
		List<String> ans = new ArrayList<>();
		process2(path, 0, ans);
		return ans;
	}

	public static void process2(char[] path, int index, List<String> ans) {
		if (index == path.length) {
			if (isValid(path)) {
				ans.add(String.valueOf(path));
			}
		} else {
			path[index] = '(';
			process2(path, index + 1, ans);
			path[index] = ')';
			process2(path, index + 1, ans);
		}
	}

	public static boolean isValid(char[] path) {
		int count = 0;
		for (char cha : path) {
			if (cha == '(') {
				count++;
			} else {
				count--;
			}
			if (count < 0) {
				return false;
			}
		}
		return count == 0;
	}

}
