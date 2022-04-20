package class13;

import java.math.BigDecimal;

/**
 * 谷歌面试题，面值为1~10的牌组成一组，每次你从组里等概率的抽出1~10中的一张，下次抽会换一个新的组，有无限组
 * 当累加和<17时，你将一直抽牌；当累加和>=17且<21时，你将获胜；当累加和>=21时，你将失败
 * 返回获胜的概率
 *
 * 解题：
 * 	本题需要理解一个概率原理：
 * 假设目前手里有累加和为x，接下来会等概率拿到一张牌1~n,获胜的概率是多少？
 * 	枚举抽中的牌，1~n，计算获胜的概率，这个概率需要除以枚举量
 * 		假设抽中1，获胜的概率为a
 * 		假设抽中2，获胜的概率为b
 * 		假设抽中3，获胜的概率为c
 * 		...
 * 		假设抽中n，获胜的概率为y
 * 	那结算一下，总的获胜的概率是(a+b+c+...+y)/n；
 * 	1. 为什么要除n？
 * 		因为每次抽中一张牌的概率为1/n
 * 	2. a,b,c..概率怎么计算？
 * 		假设抽中1后，目前手里有的累加和就是x+1，递归计算，直到累加和达到赢/输的范围（base case）
 */
public class Code01_NCardsABWin {

	/**
	 * 谷歌面试题
	 */
	// 面值为1~10的牌组成一组，
	// 每次你从组里等概率的抽出1~10中的一张
	// 下次抽会换一个新的组，有无限组
	// 当累加和<17时，你将一直抽牌
	// 当累加和>=17且<21时，你将获胜
	// 当累加和>=21时，你将失败
	// 返回获胜的概率
	public static double f1() {
		return p1(0);
	}

	/*
	* 目前手里有的牌的累加和为cur
	* 1~n等概率抽牌
	* 赢的概率是多少返回
	* */
	public static double p1(int cur) {
		if (cur >= 17 && cur < 21) {
			/*赢的边界*/
			return 1.0;
		}
		if (cur >= 21) {
			/*输的边界*/
			return 0.0;
		}
		double w = 0.0;
		/*枚举抽中的牌*/
		for (int i = 1; i <= 10; i++) {
			/*概率累加，最后要除n后再返回*/
			w += p1(cur + i);
		}
		return w / 10;
	}

	/**
	 * 谷歌面试题扩展版
	 */
	// 面值为1~N的牌组成一组，
	// 每次你从组里等概率的抽出1~N中的一张
	// 下次抽会换一个新的组，有无限组
	// 当累加和<a时，你将一直抽牌
	// 当累加和>=a且<b时，你将获胜
	// 当累加和>=b时，你将失败
	// 返回获胜的概率，给定的参数为N，a，b
	public static double f2(int N, int a, int b) {
		if (N < 1 || a >= b || a < 0 || b < 0) {
			/*这些都不可能会赢*/
			return 0.0;
		}
		if (b - a >= N) {
			/*必定会赢，为什么？赢的区域太长，怎么抽牌都一定能命中区域*/
			return 1.0;
		}
		// 所有参数都合法，并且b-a < N
		return p2(0, N, a, b);
	}

	// 游戏规则，如上，int N, int a, int b，固定参数！
	// cur，目前到达了cur的累加和
	// 返回赢的概率
	public static double p2(int cur, int N, int a, int b) {
		if (cur >= a && cur < b) {
			return 1.0;
		}
		if (cur >= b) {
			return 0.0;
		}
		double w = 0.0;
		/*
		* 这里的枚举行为，当数据量大的情况下，需要优化掉，如何优化？
		* 举例，n=3, a=10,b=12
		* f(1)=(f(2)+f(3)+f(4))/3
		* f(2)=(f(3)+f(4)+f(5))/3
		* so
		* f(1)=(f(2) + f(2)*3 -f(5))/3
		* 抽象得到 f(cur) = (f(cur+1) + f(cur+1)*n - f(cur+1+n))/n
		*
		* 需要考虑边界：假设n=5, a=10,b=12
		* f(9)=(f(10)+f(11))/3 :单独列举,为什么f(12)后面的不用加了，因为f(12)往后都是0
		* f(8)=(f(9)+f(10)+f(11))/3 :f(8)=(f(9)+f(9)*3)/3 ->不减尾巴
		* f(7)=(f(8)+f(9)+f(10)+f(11))/3 :f(7)=(f(8)+f(9)*3)/3 ->不减尾巴
		* f(6)=(f(7)+f(8)+f(9)+f(10)+f(11))/3 :f(6)=(f(7)+f(7)*3)/3 ->不减尾巴
		* f(5)=(f(6)+f(7)+f(8)+f(9)+f(10))/3 :f(5)=(f(6)+f(6)*3-f(11))/3 ->减尾巴
		* so
		* f(cur) = f(cur+1) + f(cur+1)*n
		* 当 cur+1+n<b时
		* f(cur) -= f(cur+1+n)
		* f(cur) /= 3
		* */
		for (int i = 1; i <= N; i++) {
			w += p2(cur + i, N, a, b);
		}
		return w / N;
	}

	// f2的改进版本，用到了观察位置优化枚举的技巧
	// 可以课上讲一下
	public static double f3(int N, int a, int b) {
		if (N < 1 || a >= b || a < 0 || b < 0) {
			return 0.0;
		}
		if (b - a >= N) {
			return 1.0;
		}
		return p3(0, N, a, b);
	}

	public static double p3(int cur, int N, int a, int b) {
		if (cur >= a && cur < b) {
			return 1.0;
		}
		if (cur >= b) {
			return 0.0;
		}
		if (cur == a - 1) {
			/*a前面一个位置：赢的区域概率都是1，再往后概率都是0*/
			return 1.0 * (b - a) / N;
		}
		double w = p3(cur + 1, N, a, b) + p3(cur + 1, N, a, b) * N;
		if (cur + 1 + N < b) {
			w -= p3(cur + 1 + N, N, a, b);
		}
		return w / N;
	}

	// f3的改进版本的动态规划
	// 可以课上讲一下
	public static double f4(int N, int a, int b) {
		if (N < 1 || a >= b || a < 0 || b < 0) {
			return 0.0;
		}
		if (b - a >= N) {
			return 1.0;
		}
		double[] dp = new double[b];
		for (int i = a; i < b; i++) {
			dp[i] = 1.0;
		}
		if (a - 1 >= 0) {
			dp[a - 1] = 1.0 * (b - a) / N;
		}
		for (int cur = a - 2; cur >= 0; cur--) {
			double w = dp[cur + 1] + dp[cur + 1] * N;
			if (cur + 1 + N < b) {
				w -= dp[cur + 1 + N];
			}
			dp[cur] = w / N;
		}
		return dp[0];
	}

	public static void main(String[] args) {
		int N = 10;
		int a = 17;
		int b = 21;
		System.out.println("N = " + N + ", a = " + a + ", b = " + b);
		System.out.println(f1());
		System.out.println(f2(N, a, b));
		System.out.println(f3(N, a, b));
		System.out.println(f4(N, a, b));

		int maxN = 15;
		int maxM = 20;
		int testTime = 100000;
		System.out.println("测试开始");
		System.out.print("比对double类型答案可能会有精度对不准的问题, ");
		System.out.print("所以答案一律只保留小数点后四位进行比对, ");
		System.out.println("如果没有错误提示, 说明验证通过");
		for (int i = 0; i < testTime; i++) {
			N = (int) (Math.random() * maxN);
			a = (int) (Math.random() * maxM);
			b = (int) (Math.random() * maxM);
			double ans2 = new BigDecimal(f2(N, a, b)).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
			double ans3 = new BigDecimal(f3(N, a, b)).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
			double ans4 = new BigDecimal(f4(N, a, b)).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
			if (ans2 != ans3 || ans2 != ans4) {
				System.out.println("Oops!");
				System.out.println(N + " , " + a + " , " + b);
				System.out.println(ans2);
				System.out.println(ans3);
				System.out.println(ans4);
			}
		}
		System.out.println("测试结束");

		N = 10000;
		a = 67834;
		b = 72315;
		System.out.println("N = " + N + ", a = " + a + ", b = " + b + "时, 除了方法4外都超时");
		System.out.print("方法4答案: ");
		System.out.println(f4(N, a, b));
	}

}
