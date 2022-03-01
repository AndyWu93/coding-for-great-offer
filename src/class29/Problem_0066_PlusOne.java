package class29;

/**
 * You are given a large integer represented as an integer array digits, where each digits[i] is the ith digit of the integer. The digits are ordered from most significant to least significant in left-to-right order. The large integer does not contain any leading 0's.
 *
 * Increment the large integer by one and return the resulting array of digits.
 * 题意：给定一个数组，表示了一个数，返回这个数加1后的结果，同样用数组返回
 * 解题：
 * 	什么时候需要生成一个新数组返回？只有都是9的时候，需要生成一个新数组，高位是1，其他都是0，返回
 */
public class Problem_0066_PlusOne {

	public static int[] plusOne(int[] digits) {
		int n = digits.length;
		for (int i = n - 1; i >= 0; i--) {
			if (digits[i] < 9) {
				/*从低位往高位遍历，如果当前位小9，++后直接返回*/
				digits[i]++;
				return digits;
			}
			/*如果等于9，当前位变成0，去到更高位，去++了*/
			digits[i] = 0;
		}
		/*跳出循环都没有返回，表示遇到的都是9，直接返回新数组，高位是1，其他都是0*/
		int[] ans = new int[n + 1];
		ans[0] = 1;
		return ans;
	}

}
