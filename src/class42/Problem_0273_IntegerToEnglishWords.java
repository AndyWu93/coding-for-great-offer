package class42;

/**
 * 给定一个整数num，返回一个字符串str，是num的英文表达
 * 解题：
 * 	本题考查业务分析能力
 */
//leetcode题目 : https://leetcode.com/problems/integer-to-english-words/
public class Problem_0273_IntegerToEnglishWords {

	public static String num1To19(int num) {
		if (num < 1 || num > 19) {
			return "";
		}
		String[] names = { "One ", "Two ", "Three ", "Four ", "Five ", "Six ", "Seven ", "Eight ", "Nine ", "Ten ",
				"Eleven ", "Twelve ", "Thirteen ", "Fourteen ", "Fifteen ", "Sixteen ", "Seventeen", "Eighteen ",
				"Nineteen " };
		return names[num - 1];
	}

	public static String num1To99(int num) {
		if (num < 1 || num > 99) {
			return "";
		}
		/*20以内的处理*/
		if (num < 20) {
			return num1To19(num);
		}
		/*20~99的处理*/
		int high = num / 10;
		String[] tyNames = { "Twenty ", "Thirty ", "Forty ", "Fifty ", "Sixty ", "Seventy ", "Eighty ", "Ninety " };
		return tyNames[high - 2] + num1To19(num % 10);
	}

	public static String num1To999(int num) {
		if (num < 1 || num > 999) {
			return "";
		}
		/*
		* 100以内单独处理
		* */
		if (num < 100) {
			return num1To99(num);
		}
		/*百位单独算一下*/
		int high = num / 100;
		return num1To19(high) + "Hundred " + num1To99(num % 100);
	}

	/**
	 * 主方法
	 */
	public static String numberToWords(int num) {
		if (num == 0) {
			return "Zero";
		}
		String res = "";
		if (num < 0) {
			res = "Negative ";
		}
		/*
		* 系统最小值无法转成正数，所以单独处理，
		* 怎么处理呢？
		* 系统最小是2billion打头，把billion段的先打印出来，剩下的磨掉2billion后正常计算
		* */
		if (num == Integer.MIN_VALUE) {
			res += "Two Billion ";
			/*num如果是系统最小值，磨掉billion段后再继续*/
			num %= -2000000000;
		}
		num = Math.abs(num);
		/*用来分段的，1000,000,000*/
		int high = 1000000000;
		int highIndex = 0;
		String[] names = { "Billion ", "Million ", "Thousand ", "" };
		while (num != 0) {
			/*取出num该段里的数字*/
			int cur = num / high;
			/*把num的该段磨掉*/
			num %= high;
			if (cur != 0) {
				/*该段有数，打印后加上段位*/
				res += num1To999(cur);
				res += names[highIndex];
			}
			/*分段的高度减去一段，继续计算下一段*/
			high /= 1000;
			highIndex++;
		}
		return res.trim();
	}

	public static void main(String[] args) {
		int test = Integer.MIN_VALUE;
		System.out.println(test);

		test = -test;
		System.out.println(test);

		int num = -10001;
		System.out.println(numberToWords(num));
	}

}
