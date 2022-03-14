package class32;

import java.util.HashMap;

/**
 * Given two integers representing the numerator and denominator of a fraction, return the fraction in string format.
 *
 * If the fractional part is repeating, enclose the repeating part in parentheses.
 *
 * If multiple answers are possible, return any of them.
 *
 * It is guaranteed that the length of the answer string is less than 104 for all the given inputs.
 * 题意：给定两个整数，返回这两个数相除的结果，小数部分需要将循环的部分括起来
 * 解题：
 * 	两个整数相除，结果一定是有理数，要么整除，要么就是循环小数
 *
 */
public class Problem_0166_FractionToRecurringDecimal {

	public static String fractionToDecimal(int numerator, int denominator) {
		if (numerator == 0) {
			return "0";
		}
		StringBuilder res = new StringBuilder();
		// "+" or "-"
		/*先确定符号*/
		res.append(((numerator > 0) ^ (denominator > 0)) ? "-" : "");
		/*被除数和除数，转成正数，用long是为了怕溢出*/
		long num = Math.abs((long) numerator);
		long den = Math.abs((long) denominator);
		// integral part
		res.append(num / den);
		/*拿到了余数num*/
		num %= den;
		if (num == 0) {
			/*整除的情况，直接返回*/
			return res.toString();
		}
		// fractional part
		/*没有整除，加. 开始小数部分的计算*/
		res.append(".");
		/*key:新的被除数（通常是余数乘10得到的一个被除数），value：这个被除数得到的结果是从商的哪个位置开始的*/
		HashMap<Long, Integer> map = new HashMap<Long, Integer>();
		map.put(num, res.length());
		while (num != 0) {
			num *= 10;
			res.append(num / den);
			/*得到了新的被除数*/
			num %= den;
			if (map.containsKey(num)) {
				/*
				* 新的被除数之前出现过了，表示商从这里往后开始循环，
				* 找到之前出现是商的位置，加上括号，就可以直接结束了
				* */
				int index = map.get(num);
				res.insert(index, "(");
				res.append(")");
				break;
			} else {
				/*否则，记录这个被除数的商是从哪里开始的*/
				map.put(num, res.length());
			}
		}
		return res.toString();
	}

	public static void main(String[] args) {
		System.out.println(fractionToDecimal(127, 999));
	}

}
