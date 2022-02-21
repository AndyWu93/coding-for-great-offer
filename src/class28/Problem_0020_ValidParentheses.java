package class28;

/**
 * Given a string s containing just the characters '(', ')', '{', '}', '[' and ']', determine if the input string is valid.
 *
 * An input string is valid if:
 *
 * Open brackets must be closed by the same type of brackets.
 * Open brackets must be closed in the correct order.
 *
 * 题意：判断给定括号str是否合法
 * 解题：
 * 	用一个栈，遇到左括号压栈，遇到右括号弹栈，和弹出的比对
 * 	注意：
 * 		在某一步栈空了，说明不合法
 * 		到最后了栈不空，说明不合法
 */
public class Problem_0020_ValidParentheses {

	public static boolean isValid(String s) {
		if (s == null || s.length() == 0) {
			return true;
		}
		char[] str = s.toCharArray();
		int N = str.length;
		/*用数组代替栈*/
		char[] stack = new char[N];
		int size = 0;
		for (int i = 0; i < N; i++) {
			char cha = str[i];
			if (cha == '(' || cha == '[' || cha == '{') {
				/*遇到左括号，压栈，压栈的时候可以压对应的右括号，方便比对*/
				stack[size++] = cha == '(' ? ')' : (cha == '[' ? ']' : '}');
			} else {
				if (size == 0) {
					/*还没到最后，栈空了，说明右括号多了*/
					return false;
				}
				/*弹栈，比对*/
				char last = stack[--size];
				if (cha != last) {
					/*一定是相等的*/
					return false;
				}
			}
		}
		/*最后，栈一定是空了，才合法*/
		return size == 0;
	}

}
