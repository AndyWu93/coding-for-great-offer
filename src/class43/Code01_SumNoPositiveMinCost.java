package class43;

import java.util.Arrays;

/**
 * 来自微软面试
 * 给定一个正数数组arr长度为n、正数x、正数y
 * 你的目标是让arr整体的累加和<=0
 * 你可以对数组中的数num执行以下三种操作中的一种，且每个数最多能执行一次操作 :
 * 1）不变
 * 2）可以选择让num变成0，承担x的代价
 * 3）可以选择让num变成-num，承担y的代价
 * 返回你达到目标的最小代价
 * 数据规模 : 面试时面试官没有说数据规模
 *
 * 解析：
 * 	本题是某次微软面试的最后一题，看似动态规划，实际上是贪心，考查人的智力，智力不足可以用技巧来填补
 * 解题：
 * 	动态规划：一个背包问题，解题过程略，复杂度O(n*sum(arr))
 * 	dp有一个危险，就是如果sum太高，这个复杂度会有问题，所以dp的复杂度严重依赖arr的数据情况
 * 	所以要分析有没有贪心解
 * 贪心解：
 * 	先让arr排个倒序，为什么要排序，arr上的任意一个数，x和y的操作代价是一样的，为什么不先操作大的数呢，
 * 	所以arr中执行的操作一定是前面的大数执行y，中间一部分执行x，最后一部分执行无
 * 	如果，x的代价比y高或者相等，那x就很不划算，因为x操作的收益低，那就只做y操作和无，排好序的arr中从大到小，看哪个位置y操作后sum变成<=0了，结算代价
 * 	如果，x的代价比y低，
 * 	那就需要找出arr中执行y操作和x操作的范围，可以用两个for循环来枚举x操作和y操作的长度，选出代价最低的方案，复杂度O(n^2)
 * 枚举可以优化吗？
 * 	当然可以，可以只枚举y操作的长度范围，x操作的范围通过计算获得
 * 	y操作的范围如果是l，操作的收益能够计算出来，就是范围内的累加和s，一个指针从arr尾部向前，计算能够抵消s的长度
 * 	剩下的部分就x操作的范围，指针不回退，枚举过程复杂度O(n),加上排序，整体复杂度O(N*logN)
 */
public class Code01_SumNoPositiveMinCost {

	// 动态规划
	public static int minOpStep1(int[] arr, int x, int y) {
		int sum = 0;
		for (int num : arr) {
			sum += num;
		}
		return process1(arr, x, y, 0, sum);
	}

	// arr[i...]自由选择，每个位置的数可以执行三种操作中的一种！
	// 执行变0的操作，x操作，代价 -> x
	// 执行变相反数的操作，y操作，代价 -> y
	// 还剩下sum这么多累加和，需要去搞定！
	// 返回搞定了sum，最低代价是多少？
	public static int process1(int[] arr, int x, int y, int i, int sum) {
		if (sum <= 0) {
			return 0;
		}
		// sum > 0 没搞定
		if (i == arr.length) {
			return Integer.MAX_VALUE;
		}
		// 第一选择，什么也不干！
		int p1 = process1(arr, x, y, i + 1, sum);
		// 第二选择，执行x的操作，变0 x + 后续
		int p2 = Integer.MAX_VALUE;
		int next2 = process1(arr, x, y, i + 1, sum - arr[i]);
		if (next2 != Integer.MAX_VALUE) {
			p2 = x + next2;
		}
		// 第三选择，执行y的操作，变相反数 x + 后续 7 -7 -14
		int p3 = Integer.MAX_VALUE;
		int next3 = process1(arr, x, y, i + 1, sum - (arr[i] << 1));
		if (next3 != Integer.MAX_VALUE) {
			p3 = y + next3;
		}
		return Math.min(p1, Math.min(p2, p3));
	}

	// 贪心（最优解）
	public static int minOpStep2(int[] arr, int x, int y) {
		Arrays.sort(arr); // 小 -> 大
		int n = arr.length;
		/*下面这个部分是arr中做逆序，其实可以在排序时就做好从大到小排序*/
		for (int l = 0, r = n - 1; l <= r; l++, r--) {
			int tmp = arr[l];
			arr[l] = arr[r];
			arr[r] = tmp;
		}
		// arr 大 -> 小
		if (x >= y) {
			/*x操作更贵：没有任何必要执行x操作*/
			int sum = 0;
			for (int num : arr) {
				sum += num;
			}
			int cost = 0;
			for (int i = 0; i < n && sum > 0; i++) {
				/*找到能让sum<=0的y操作范围就可以停了，结算代价返回*/
				sum -= arr[i] << 1;
				cost += y;
			}
			return cost;
		} else {
			/*转成后缀和数组*/
			for (int i = n - 2; i >= 0; i--) {
				arr[i] += arr[i + 1];
			}
			int benefit = 0;
			// 注意，可以不二分，用不回退的方式！
			/*执行Y操作的数，有0个的时候*/
			int left = mostLeft(arr, 0, benefit);
			/*计算出操作范围的长度，这里就是left，将x操作的代价累加到cost中去*/
			int cost = left * x;
			/*枚举y操作的长度*/
			for (int i = 0; i < n - 1; i++) {
				// 0..i 这些数，都执行Y
				/*收益加上原arr[i]的值，现在arr是后缀和数组，arr[i]这么算？arr[i] - arr[i + 1]*/
				benefit += arr[i] - arr[i + 1];
				left = mostLeft(arr, i + 1, benefit);
				/*结算cost，收集最小值*/
				cost = Math.min(cost, (i + 1) * y + (left - i - 1) * x);
			}
			return cost;
		}
	}

	// arr是后缀和数组， arr[l...]中找到值<=v的最左位置
	public static int mostLeft(int[] arr, int l, int v) {
		int r = arr.length - 1;
		int m = 0;
		/*ans给了一个初始值，当arr[l...]中找不到<=v的最左位置时，返回N，即一个取不到的位置*/
		int ans = arr.length;
		while (l <= r) {
			m = (l + r) / 2;
			if (arr[m] <= v) {
				ans = m;
				r = m - 1;
			} else {
				l = m + 1;
			}
		}
		return ans;
	}

	// 贪心的第二种方式：不回退
	public static int minOpStep3(int[] arr, int x, int y) {
		// 系统排序，小 -> 大
		Arrays.sort(arr);
		int n = arr.length;
		// 如何变成 大 -> 小
		for (int l = 0, r = n - 1; l <= r; l++, r--) {
			int tmp = arr[l];
			arr[l] = arr[r];
			arr[r] = tmp;
		}
		if (x >= y) {
			int sum = 0;
			for (int num : arr) {
				sum += num;
			}
			int cost = 0;
			for (int i = 0; i < n && sum > 0; i++) {
				sum -= arr[i] << 1;
				cost += y;
			}
			return cost;
		} else {
			// 0个数执行Y
			int benefit = 0;
			// 全部的数都需要执行x，才能让累加和<=0
			int cost = arr.length * x;
			int holdSum = 0;
			for (int yRight = 0, holdLeft = n; yRight < holdLeft - 1; yRight++) {
				benefit += arr[yRight];
				while (holdLeft - 1 > yRight && holdSum + arr[holdLeft - 1] <= benefit) {
					holdSum += arr[holdLeft - 1];
					holdLeft--;
				}
				// 0...yRight x holdLeft....
				cost = Math.min(cost, (yRight + 1) * y + (holdLeft - yRight - 1) * x);
			}
			return cost;
		}
	}

	// 为了测试
	public static int[] randomArray(int len, int v) {
		int[] arr = new int[len];
		for (int i = 0; i < len; i++) {
			arr[i] = (int) (Math.random() * v) + 1;
		}
		return arr;
	}

	// 为了测试
	public static int[] copyArray(int[] arr) {
		int[] ans = new int[arr.length];
		for (int i = 0; i < arr.length; i++) {
			ans[i] = arr[i];
		}
		return ans;
	}

	// 为了测试
	public static void main(String[] args) {
		int n = 12;
		int v = 20;
		int c = 10;
		int testTime = 10000;
		System.out.println("测试开始");
		for (int i = 0; i < testTime; i++) {
			int len = (int) (Math.random() * n);
			int[] arr = randomArray(len, v);
			int[] arr1 = copyArray(arr);
			int[] arr2 = copyArray(arr);
			int[] arr3 = copyArray(arr);
			int x = (int) (Math.random() * c);
			int y = (int) (Math.random() * c);
			int ans1 = minOpStep1(arr1, x, y);
			int ans2 = minOpStep2(arr2, x, y);
			int ans3 = minOpStep3(arr3, x, y);
			if (ans1 != ans2 || ans1 != ans3) {
				System.out.println("出错了!");
			}
		}
		System.out.println("测试结束");

	}

}
