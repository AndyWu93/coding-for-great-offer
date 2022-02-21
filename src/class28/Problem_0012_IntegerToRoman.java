package class28;

/**
 * 将数字转成罗马数字
 * 罗马数不含0,所以遇到0不作处理
 */
public class Problem_0012_IntegerToRoman {

	public static String intToRoman(int num) {
		/*准备一张表*/
		String[][] c = { 
				{ "", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX" },/*0~9*/
				{ "", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC" },/*10,20..90*/
				{ "", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM" },/*100,200..900*/
				{ "", "M", "MM", "MMM" } };									   /*1000,2000,3000*/
		StringBuilder roman = new StringBuilder();
		roman
		.append(c[3][num / 1000 % 10])/*加入千位数，如果千位是0，加入的是""*/
		.append(c[2][num / 100 % 10])
		.append(c[1][num / 10 % 10])
		.append(c[0][num % 10]);
		return roman.toString();
	}

}
