package class08;

import java.util.LinkedList;

/**
 * 算术表达式解析并求值
 * 思路：可以使用递归的设计思路，该思路可以用于解决所有语法解析的问题
 * 递归函数 [] f(x):从x位置往后，一直到第一个 ) 的位置j，计算出结果ans，返回ans和j
 * 函数流程：
 * 1. 普通加减乘除
 * 申请一个本地变量cur，和一个栈stack
 * 从x位置开始往后遍历，当x位置的字符是数字，值给cur，一直到：
 * x位置是算术符号，将cur收集到的值和符号一起压入栈中，在压入栈之前，看一下栈顶：
 * 如果栈顶是加号或者减号，直接将cur和符号压入栈中
 * 如果栈顶是乘号或者除号，弹出符号以及后面一个数字，与当前cur结算以后，再将cur和符号压入栈中
 * 直到遍历到最后一个位置，依次弹出栈中的符号和数字，依次结算出ans，返回ans和当前位置n-1
 * 2. 存在（）
 * 当遍历到x位置是 ( 时，cur等待递归调用f(x+1)，拿到f函数到它遇到第一个 ) 时返回的ans和j
 * 从j+1位置继续往后遍历，重复1的流程
 */
//https://leetcode.com/problems/basic-calculator-ii/
public class Code01_ExpressionCompute {

	public static int getValue(String str) {
		return f(str.toCharArray(), 0)[0];
	}

	// 请从str[i...]往下算，遇到字符串终止位置或者右括号，就停止
	// 返回两个值，长度为2的数组
	// 0) 负责的这一段的结果是多少
	// 1) 负责的这一段计算到了哪个位置
	public static int[] f(char[] str, int i) {
		LinkedList<String> que = new LinkedList<String>();
		int cur = 0;
		int[] bra = null;
		/*从i出发，遇到 ） 或者越界了停止*/
		while (i < str.length && str[i] != ')') {
			if (str[i] >= '0' && str[i] <= '9') {
				/*如果遇到的是0~9，之前的cur*10+现在遇到的数算好后给cur，然后i++*/
				cur = cur * 10 + str[i++] - '0';
			} else if (str[i] != '(') { // 遇到的是运算符号
				/*遇到的是算术符号，先将数字加到容器中去，再将符号加进去，cur清零，然后i++*/
				addNum(que, cur);
				que.addLast(String.valueOf(str[i++]));
				cur = 0;
			} else { // 遇到左括号了
				/*递归调用f(i+1)*/
				bra = f(str, i + 1);
				/*cur接住返回值*/
				cur = bra[0];
				/*i接着返回的位置，继续往下算*/
				i = bra[1] + 1;
			}
		}
		/*str中的最后一个数字*/
		addNum(que, cur);
		/*结算que，返回ans和i*/
		return new int[] { getNum(que), i };
	}

	/*
	* 将num加入到que中去
	* que中已经存放着数字和运算符号，末尾一定是运算符号
	* 加入之前会先看下队里末尾的运算符，如果是乘除，会先后当前加入的num结算
	* */
	public static void addNum(LinkedList<String> que, int num) {
		if (!que.isEmpty()) {
			int cur = 0;
			String top = que.pollLast();
			if (top.equals("+") || top.equals("-")) {
				que.addLast(top);
			} else {
				cur = Integer.valueOf(que.pollLast());
				num = top.equals("*") ? (cur * num) : (cur / num);
			}
		}
		que.addLast(String.valueOf(num));
	}

	/*
	* 加减法运算
	* */
	public static int getNum(LinkedList<String> que) {
		int res = 0;
		/*初始值是true*/
		boolean add = true;
		String cur = null;
		int num = 0;
		while (!que.isEmpty()) {
			cur = que.pollFirst();
			if (cur.equals("+")) {
				add = true;
			} else if (cur.equals("-")) {
				add = false;
			} else {
				num = Integer.valueOf(cur);
				res += add ? num : (-num);
			}
		}
		return res;
	}

	public static void main(String[] args) {
		String exp = "48*((70-65)-43)+8*1";
		System.out.println(getValue(exp));

		exp = "4*(6+78)+53-9/2+45*8";
		System.out.println(getValue(exp));

		exp = "10-5*3";
		System.out.println(getValue(exp));

		exp = "-3*4";
		System.out.println(getValue(exp));

		exp = "3+1*4";
		System.out.println(getValue(exp));

	}

}
