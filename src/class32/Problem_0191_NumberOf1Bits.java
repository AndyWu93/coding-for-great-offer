package class32;

/**
 * Write a function that takes an unsigned integer and returns the number of '1' bits it has (also known as the Hamming weight).
 */
public class Problem_0191_NumberOf1Bits {
    
	// n的二进制形式，有几个1？
	public static int hammingWeight1(int n) {
		int bits = 0;
		int rightOne = 0;
		while(n != 0) {
			bits++;
			/*拿到最右侧的1*/
			rightOne = n & (-n);
			/*无进位相加，相当于消掉了最右侧的1，然后继续*/
			n ^= rightOne;	
		}
		return bits;
	}


	public static int hammingWeight2(int n) {
		/*没两位统计一下1的个数，结果用二进制表示*/
		n = (n & 0x55555555) + ((n >>> 1) & 0x55555555);
		/*基于上面的统计结果，每4位统计1的个数，结果用二进制表示*/
		n = (n & 0x33333333) + ((n >>> 2) & 0x33333333);
		n = (n & 0x0f0f0f0f) + ((n >>> 4) & 0x0f0f0f0f);
		n = (n & 0x00ff00ff) + ((n >>> 8) & 0x00ff00ff);
		n = (n & 0x0000ffff) + ((n >>> 16) & 0x0000ffff);
		return n;
	}
	
}
