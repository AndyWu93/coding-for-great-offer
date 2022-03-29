package class34;

/**
 * Given two integers a and b, return the sum of the two integers without using the operators + and -.
 * @see class28.Problem_0029_BitAddMinusMultiDiv
 */
//https://leetcode.com/problems/sum-of-two-integers/
public class Problem_0371_SumOfTwoIntegers {

	public static int getSum(int a, int b) {
		int sum = a;
		while (b != 0) {
			sum = a ^ b;
			b = (a & b) << 1;
			a = sum;
		}
		return sum;
	}

}
