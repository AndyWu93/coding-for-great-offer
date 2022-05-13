package class38;

/**
 * 来自字节
 * 给定两个数a和b
 * 第1轮，把1选择给a或者b
 * 第2轮，把2选择给a或者b
 * ...
 * 第i轮，把i选择给a或者b
 * 想让a和b的值一样大，请问至少需要多少轮？
 *
 * 解题：
 * 	本题考查数学能力
 * 分析：
 * 	i轮之内，a和b拿到数总和分别是x,y，
 * 	那
 * 	x+y:1到i累加和sum(i)，
 * 	x-y:一定是补平了a和b的初始差s（假设x>y）
 * 	化简计算出x，y
 * 	x = (sum + s)/2
 * 	y = (sum - s)/2
 * 	x,y一定都是大于等于0的int，因为x>y，所以y大于等于0的int就行了
 * 	所以
 * 	(sum - s)/2 >= 0(int)
 * 	进一步化简
 * 	sum - s >= 0(偶数)
 * 代入i，题目转化为：
 * 	i*(i+1)/2 - s >= 0(偶数)
 * 	求出最小的i
 * 怎么求？
 * 	方法1：i从1开始，一个一个试，O(N)，能过
 * 	方法2：复杂度O(logN)
 * 		先找出i*(i+1)/2 - s >= 0，不谈偶数的事，怎么找最快呢？
 * 		i不是一个一个试，i可以是1,2,4,8,16,32，这样每一步很大，这样能够锁定一个i的范围，比如i可以确定在16~32之间
 * 		接下来确定i的具体位置，可以在16~32范围内二分
 * 		最终i拿到一个结果比如是20，那就从20开始代入 i*(i+1)/2 - s 看结果是不是偶数
 * 		因为
 * 		s的奇偶性是固定了，i*(i+1)/2通过观察奇偶变化周期为4，
 * 		所以20往后最多4次，就能找到与s奇偶性一致的i，即答案
 */
public class Code01_FillGapMinStep {

	// 暴力方法
	// 不要让a、b过大！
	public static int minStep0(int a, int b) {
		if (a == b) {
			return 0;
		}
		int limit = 15;
		return process(a, b, 1, limit);
	}

	public static int process(int a, int b, int i, int n) {
		if (i > n) {
			return Integer.MAX_VALUE;
		}
		if (a + i == b || a == b + i) {
			return i;
		}
		return Math.min(process(a + i, b, i + 1, n), process(a, b + i, i + 1, n));
	}

	public static int minStep1(int a, int b) {
		if (a == b) {
			return 0;
		}
		int s = Math.abs(a - b);
		int num = 1;
		int sum = 0;
		for (; !(sum >= s && (sum - s) % 2 == 0); num++) {
			sum += num;
		}
		return num - 1;
	}

	/**
	 * O(logN)
	 */
	public static int minStep2(int a, int b) {
		if (a == b) {
			/*相等的话，0轮就可以了*/
			return 0;
		}
		/*先找到s*/
		int s = Math.abs(a - b);
		/*
		* 找到sum >= s, 最小的i，
		* 这里s为什么*2，因为sum的公式是i(i+1)/2,为了方便，best函数中只要计算i(i+1)就行了
		* */
		int begin = best(s << 1);
		for (; (begin * (begin + 1) / 2 - s) % 2 != 0;) {
			begin++;
		}
		return begin;
	}

	/*
	* 返回 i(i+1)>=s2 中最小的i
	* */
	public static int best(int s2) {
		/*先划定一个范围L~R*/
		int L = 0;
		int R = 1;
		for (; R * (R + 1) < s2;) {
			L = R;
			R *= 2;
		}
		/*L~R范围内二分，找出最小的i*/
		int ans = 0;
		while (L <= R) {
			int M = (L + R) / 2;
			if (M * (M + 1) >= s2) {
				ans = M;
				R = M - 1;
			} else {
				L = M + 1;
			}
		}
		return ans;
	}

	public static void main(String[] args) {
		System.out.println("功能测试开始");
		for (int a = 1; a < 100; a++) {
			for (int b = 1; b < 100; b++) {
				int ans1 = minStep0(a, b);
				int ans2 = minStep1(a, b);
				int ans3 = minStep2(a, b);
				if (ans1 != ans2 || ans1 != ans3) {
					System.out.println("出错了！");
					System.out.println(a + " , " + b);
					break;
				}
			}
		}
		System.out.println("功能测试结束");

		int a = 19019;
		int b = 8439284;
		int ans2 = minStep1(a, b);
		int ans3 = minStep2(a, b);
		System.out.println(ans2);
		System.out.println(ans3);

	}

}
