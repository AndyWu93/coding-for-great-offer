package class27;

/**
 * 给你一个32位的有符号整数x，返回将x中的数字部分反转后的结果，如果反转后整数超过 32 位的有符号整数的范围，就返回0
 * 假设环境不允许存储 64 位整数（有符号或无符号）
 *
 * 题意解析：比如将123转为321，但是不能用long类型来防止溢出，只能用int转。切忌不能转成String再调api
 * 思路：
 * 	整体思路就是
 * 		将x%10，得到数a，乘以10得到res；
 * 		将x/10，即砍个位数，再%10，得到b，加到res里去
 * 		如此反复，直到x等于0
 * 	整体思路比较简单，但是注意点特别多：
 * 		1. 负数的范围比正数大，所以将x转为负数来计算，溢出的可能性小，更安全
 * 		2. res乘以10，以及加b的操作有溢出的风险，需要提前判断，溢出就要返回0
 */
public class Problem_0007_ReverseInteger {

	public static int reverse(int x) {
		/*判断x是不是负数，不是的话转为负数*/
		boolean neg = ((x >>> 31) & 1) == 1;
		x = neg ? x : -x;
		/*m，o是两个帮助变量，用来防止计算过程溢出*/
		int m = Integer.MIN_VALUE / 10;
		int o = Integer.MIN_VALUE % 10;
		/*
		* 整体流程就是res*10，加上x的余数，x砍掉一位，直到x变成0
		* 两个判断：
		* res < m ： 说明res*10必溢出
		* res == m && x % 10 < o ：说明res*10不溢出，但是余数比o小，再加上余数就溢出了
		* */
		int res = 0;
		while (x != 0) {
			if (res < m || (res == m && x % 10 < o)) {
				return 0;
			}
			res = res * 10 + x % 10;
			x /= 10;
		}
		/*x是正数的话需要转回去*/
		return neg ? res : Math.abs(res);
	}

}
