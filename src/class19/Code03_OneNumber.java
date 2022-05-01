package class19;

/**
 * 给定一个正数N，比如N = 13，在纸上把所有数都列出来如下：
 * 1 2 3 4 5 6 7 8 9 10 11 12 13
 * 可以数出1这个字符出现了6次，给定一个正数N，如果把1~N都列出来，返回1这个字符出现的多少次
 *
 * 题意：计算num中有几个1
 * 解题：本题为动态规划，树位dp模型（高难度）
 * 总体流程举例：
 * 13625
 * 分解为
 * 求3626 - 13625有几个1
 * 求626 - 3625有几个1
 * 求26 - 625有几个1
 * 求6 - 25有几个1
 * 求1 - 5有几个1：base case：1个
 * 各阶段结果相加就是题解
 *
 * 具体一个范围中几个1怎么求呢？分为两大类，高位是1？
 * 1. 高位是1
 * 举例
 * 求3626 - 13625有几个1
 * 万位是1时：10000-13625,一共3626个
 * 千位是1时：百位十位个位可以任意值，万位只能1个数，所以10^3 * 1个
 * 百位是1时：千位十位个位可以任意值，万位只能1个数，所以10^3 * 1个
 * 十位是1时：千位百位个位可以任意值，万位只能1个数，所以10^3 * 1个
 * 个位是1时：千位十位百位可以任意值，万位只能1个数，所以10^3 * 1个
 * （注：上面都是在计算万~个位贡献了几个1，比如11111这个数，上面每种情况都会出现这个数，但每次只贡献1个1）
 * 结果为3626 + 10^3 * 4
 * 所以抽象公式：
 * 对于任意高位为1的k位数x (k>1)
 * x % 10^(k-1) + 1 + 10^(k-2) * (k-1) * 1
 * 2. 高位不是1
 * 举例
 * 求626 - 3625有几个1
 * 千位是1时：1000-1999，一共1000个
 * 将626 - 3625分为3个阶段看：
 * 626 - 1625
 * 1626 - 2625
 * 2626 - 3625
 * 百位是1时：十位个位可以任意值，千位可以取到3个数，所以10^2 * 3个
 * 十位是1时：百位个位可以任意值，千位可以取到3个数，所以10^2 * 3个
 * 个位是1时：十位百位可以任意值，千位可以取到3个数，所以10^2 * 3个
 * 结果为 10^3 + 10^2 * 3
 * 所以抽象公式：
 * 对于任意高位为a(a!=1)的k位数x (k>1)
 * 10^(k-1) + 10^(k-2) * (k-1) * a
 *
 * 复杂度：O((lg n)^2)
 */

public class Code03_OneNumber {

	public static int solution1(int num) {
		if (num < 1) {
			return 0;
		}
		int count = 0;
		for (int i = 1; i != num + 1; i++) {
			count += get1Nums(i);
		}
		return count;
	}

	public static int get1Nums(int num) {
		int res = 0;
		while (num != 0) {
			if (num % 10 == 1) {
				res++;
			}
			num /= 10;
		}
		return res;
	}

	/**
     * 复杂度估计：
	 * 递归：每次剥离最高位，算一个范围的1，一共剥离多少轮？O(lg N)
	 * 每轮：算了范围有几位数，其他都是公式O(1)，复杂度最高 O(lg N)
	 * 所以 O((lg n)^2)
	 */
	public static int solution2(int num) {
		if (num < 1) {
			return 0;
		}
		/*返回了num有几位数*/
		int len = getLenOfNum(num);
		if (len == 1) {
			/*base case*/
			return 1;
		}
		// num 13625
		// tmp1 10000
		// num 7872328738273
		// tmp1 1000000000000
		/*tmp1：长度与num一样，但高位是1其他都是0的一个数*/
		int tmp1 = powerBaseOf10(len - 1);
		/*求出num最高位：num / tmp1*/
		int first = num / tmp1;
		/*
		* 最高位是1时，贡献了多少个1？
		* 最高1： N % tmp1 + 1
		* 最高位first：tmp1
		* */
		int firstOneNum = first == 1 ? num % tmp1 + 1 : tmp1;
		/*
		* 除去最高位之外，剩下1的数量
		* 10^(k-2) * (k-1) * a （a:最高位）
		*
		* tmp1: 10^(k-1)
		* tmp1/10: 10^(k-2)
		* */
		int otherOneNum = (tmp1 / 10) * (len - 1) * first ;
		/*本次范围，最高位贡献1的数量 + 本次范围，剩下位贡献1的数量 + 剩下范围贡献1的数量*/
		return firstOneNum + otherOneNum + solution2(num % tmp1);
	}

	/*复杂度：O(lg N)*/
	public static int getLenOfNum(int num) {
		int len = 0;
		while (num != 0) {
			len++;
			num /= 10;
		}
		return len;
	}

	public static int powerBaseOf10(int base) {
		return (int) Math.pow(10, base);
	}

	public static void main(String[] args) {
		int num = 50000000;
		long start1 = System.currentTimeMillis();
		System.out.println(solution1(num));
		long end1 = System.currentTimeMillis();
		System.out.println("cost time: " + (end1 - start1) + " ms");

		long start2 = System.currentTimeMillis();
		System.out.println(solution2(num));
		long end2 = System.currentTimeMillis();
		System.out.println("cost time: " + (end2 - start2) + " ms");

	}
}
