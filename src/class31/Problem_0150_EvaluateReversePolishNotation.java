package class31;

import java.util.Stack;

/**
 * Evaluate the value of an arithmetic expression in Reverse Polish Notation.
 *
 * Valid operators are +, -, *, and /. Each operand may be an integer or another expression.
 *
 * Note that division between two integers should truncate toward zero.
 *
 * It is guaranteed that the given RPN expression is always valid. That means the expression would always evaluate to a result, and there will not be any division by zero operation.
 * 题意：计算逆波兰表达式
 * 解题：
 *  本题其实考查对逆波兰式的理解
 *  即遇到操作符，就将前面的两个数字计算，变成一个数字，后继续
 *  用栈就可以解决
 */
public class Problem_0150_EvaluateReversePolishNotation {

	public static int evalRPN(String[] tokens) {
		Stack<Integer> stack = new Stack<>();
		for (String str : tokens) {
			if (str.equals("+") || str.equals("-") || str.equals("*") || str.equals("/")) {
				/*遇到操作符，弹出两个元素，计算后压入*/
				compute(stack, str);
			} else {
				/*遇到数字压栈*/
				stack.push(Integer.valueOf(str));
			}
		}
		return stack.peek();
	}

	public static void compute(Stack<Integer> stack, String op) {
		int num2 = stack.pop();
		int num1 = stack.pop();
		int ans = 0;
		switch (op) {
		case "+":
			ans = num1 + num2;
			break;
		case "-":
			ans = num1 - num2;
			break;
		case "*":
			ans = num1 * num2;
			break;
		case "/":
			ans = num1 / num2;
			break;
		}
		stack.push(ans);
	}

}
