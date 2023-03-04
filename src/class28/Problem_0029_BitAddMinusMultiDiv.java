package class28;

/**
 * Given two integers dividend and divisor, divide two integers without using multiplication, division, and mod operator.
 *
 * The integer division should truncate toward zero, which means losing its fractional part. For example, 8.345 would be truncated to 8, and -2.7335 would be truncated to -2.
 *
 * Return the quotient after dividing dividend by divisor.
 *
 * Note: Assume we are dealing with an environment that could only store integers within the 32-bit signed integer range: [−231, 231 − 1]. For this problem, if the quotient is strictly greater than 231 - 1, then return 231 - 1, and if the quotient is strictly less than -231, then return -231.
 * 题意：不使用运算符，仅使用位运算实现除法，note：返回值大于系统最大值时返回系统最大值，小于系统最小值时返回系统最小值
 * 解题：
 * 	加法是一切运算的基础
 *
 */
// 测试链接：https://leetcode.com/problems/divide-two-integers
public class Problem_0029_BitAddMinusMultiDiv {

	/**
	 * 位运算实现加法
	 * a+b <==> a,b五进位相加 + a,b的进位信息
	 */
	public static int add(int a, int b) {
		/*sum一开始等于a，如果b=0，直接返回了*/
		int sum = a;
		/*
		* 将a+b,变成
		* a^b + (a+b)的进位信息
		* 直到(a+b)的进位信息变成0
		* */
		while (b != 0) {
			sum = a ^ b;
			/*b成了 a,b的进位信息*/
			b = (a & b) << 1;
			/*a成了 a,b的五进位相加*/
			a = sum;
		}
		return sum;
	}

	/**
	 * 相反数：取反加一
	 */
	public static int negNum(int n) {
		return add(~n, 1);
	}

	/**
	 * 减法：加相反数
	 */
	public static int minus(int a, int b) {
		return add(a, negNum(b));
	}

	/**
	 * 乘法：
	 *  	  0110
	 *  	* 0110
	 *  	----------
	 *  	  0000
	 * +     0110
	 * + 	0110
	 * +   0000
	 * -------------
	 *     0100100
	 */
	public static int multi(int a, int b) {
		int res = 0;
		while (b != 0) {
			/*b的右侧是1，将此时的a加入结果里去，不是1就不用加*/
			if ((b & 1) != 0) {
				res = add(res, a);
			}
			/*每次进来，都是a右侧补0，b右侧消掉一位*/
			a <<= 1;
			b >>>= 1;
		}
		return res;
	}

	public static boolean isNeg(int n) {
		return n < 0;
	}

	/**
	 * 除法
	 * c=1001
	 * a/b =c
	 * a = b * c
	 *   = b * (1001)
	 *   = b * 2^3 + b * 2^0
	 * 所以，只要把2^3、2^0找到，就知道c在3、0位上是1
	 * 怎么找呢？
	 * 拿a与b比较，看b乘以2的几次方是小于a的最大值，求出x后，a=a-b*2^x
	 * 继续与b比较，直到b>a
	 */
	public static int div(int a, int b) {
		/*不能用负数计算，都转成正数计算，最后符号单独计算*/
		int x = isNeg(a) ? negNum(a) : a;
		int y = isNeg(b) ? negNum(b) : b;
		int res = 0;
		/*i从30开始，因为x的最高位肯定是0*/
		for (int i = 30; i >= 0; i = minus(i, 1)) {
			/*
			* 如果x右移了i位，开始大于等于y了
			* 说明，y*2^i是小于x的数里最大那个
			* 那结果里i的位置一定有个1
			* x-y*2^i，以后继续
			* */
			if ((x >> i) >= y) {/*这里x>>i没有赋值行为，x的值不变*/
				res |= (1 << i);
				x = minus(x, y << i);
			}
		}
		return isNeg(a) ^ isNeg(b) ? negNum(res) : res;
	}

	/**
	 *  https://leetcode.com/problems/divide-two-integers
	 */
	public static int divide(int a, int b) {
		/*都是系统最小，返回1*/
		if (a == Integer.MIN_VALUE && b == Integer.MIN_VALUE) {
			return 1;
		} else if (b == Integer.MIN_VALUE) {
			/*除数是系统最小，结果一定是(-1,1),不能是小数，所以是0*/
			return 0;
		} else if (a == Integer.MIN_VALUE) {
			/*被除数是系统最小，除数不是*/
			if (b == negNum(1)) {
				/*除数等于-1，结果应该是系统最小值的相反数，即系统最大值+1，按题意返回系统最大值*/
				return Integer.MAX_VALUE;
			} else {
				/*
				* a+1以后再来除以b，这样a，b都不是系统最小了
				* (a+1)/b=c
				* c*b=d
				* a-d=e e:因为是a+1后除的，所以这时候计算一下a和d之间的差，这个差是需要补偿的
				* e/b=f f:需要补偿进去的值
				* c+f ：原来的结果c加上补偿的值f，就是结果
				* */
				int c = div(add(a, 1), b);
				return add(c, div(minus(a, multi(c, b)), b));
			}
		} else {
			/*a和b都不是系统最小*/
			return div(a, b);
		}
	}

}
