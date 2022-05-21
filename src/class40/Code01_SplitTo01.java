package class40;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 腾讯
 * 分裂问题
 * 一个数n，可以分裂成一个数组[n/2, n%2, n/2]
 * 这个数组中哪个数不是1或者0，就继续分裂下去
 * 比如 n = 5，一开始分裂成[2, 1, 2]
 * [2, 1, 2]这个数组中不是1或者0的数，会继续分裂下去，比如两个2就继续分裂
 * [2, 1, 2] -> [1, 0, 1, 1, 1, 0, 1]
 * 那么我们说，5最后分裂成[1, 0, 1, 1, 1, 0, 1]
 * 每一个数都可以这么分裂，在最终分裂的数组中，假设下标从1开始
 * 给定三个数n、l、r，返回n的最终分裂数组里[l,r]范围上有几个1
 * n <= 2 ^ 50，n是long类型
 * r - l <= 50000，l和r是int类型
 * 我们的课加个码:
 * n是long类型随意多大都行
 * l和r也是long类型随意多大都行，但要保证l<=r
 *
 * 题意：n裂变成1，0数组后，数组下标从1开始，[l,r]范围内几个1
 * 本题
 * N:NUM,L:len(l,r)
 * 最优解，采用线段树的设计思路，O(logN)
 * 次优解，
 * 	写出一个递归函数，position(n,i):求出n裂变最终形态的i位置是不是1，复杂度O(logN)
 * 	对于范围[l,r]，每个位置调用position，收集，O(L*logN)
 * 普通解，采用分治的思想，将N裂变后的范围[l,r]，落实的N/2裂变后的范围，再继续往下递归，复杂度O(N)
 * 暴力解，生成裂变数组后，再计算，复杂度太大，为正无穷
 */
public class Code01_SplitTo01 {

//	public static long nums3(long n, long l, long r) {
//		HashMap<Long, Long> lenMap = new HashMap<>();
//		len(n, lenMap);
//		HashMap<Long, Long> onesMap = new HashMap<>();
//		ones(n, onesMap);
//	}

	// n = 100
	// n = 100, 最终裂变的数组，长度多少？
	// n = 50, 最终裂变的数组，长度多少？
	// n = 25, 最终裂变的数组，长度多少？
	// ..
	// n = 1 ,.最终裂变的数组，长度多少？
	// 请都填写到lenMap中去！
	public static long len(long n, HashMap<Long, Long> lenMap) {
		if (n == 1 || n == 0) {
			lenMap.put(n, 1L);
			return 1;
		} else {
			// n > 1
			long half = len(n / 2, lenMap);
			long all = half * 2 + 1;
			lenMap.put(n, all);
			return all;
		}
	}

	// n = 100
	// n = 100, 最终裂变的数组中，一共有几个1
	// n = 50, 最终裂变的数组，一共有几个1
	// n = 25, 最终裂变的数组，一共有几个1
	// ..
	// n = 1 ,.最终裂变的数组，一共有几个1
	// 请都填写到onesMap中去！
	public static long ones(long num, HashMap<Long, Long> onesMap) {
		if (num == 1 || num == 0) {
			onesMap.put(num, num);
			return num;
		}
		// n > 1
		long half = ones(num / 2, onesMap);
		long mid = num % 2 == 1 ? 1 : 0;
		long all = half * 2 + mid;
		onesMap.put(num, all);
		return all;
	}

	//

	/**
	 * 普通解O(n)，复杂度太高，过不了
	 * 按题意，下标从1开始
	 */
	public static long nums1(long n, long l, long r) {
		if (n == 1 || n == 0) {
			/*1或者0无法裂变，所以1的有效范围上只有1个1，0的有效范围上没有1*/
//			return n == 1 ? 1 : 0;
			return n;
		}
		/*
		* n可以裂变成左中右三部分
		* 看范围[l,r]包含了3个部分里的那些
		* */
		long half = size(n / 2);
		 /*
		 * n可以裂变成左中右三个范围分别是[1,half], [half+1,half+1], [half+1,size(n)]
		 * 看[l,r]包含了n可以裂变左部分里多少范围
		 * l > half:左部分没范围了
		 * 否则，递归调用，注意r不能超过half
		 * */
		long left = l > half ? 0 : nums1(n / 2, l, Math.min(half, r));
		/*
		* l，r如果包含了half+1的位置，看下这个位置，即n%2是1还是0
		* */
		long mid = (l > half + 1 || r < half + 1) ? 0 : (n & 1);
		/*
		* 看[l,r]包含了n可以裂变右部分里多少范围
		* 注意l不一定从1开始，即[l,r]不一定包含了右侧范围的开始部分
		* */
		long right = r > half + 1 ? nums1(n / 2, Math.max(l - half - 1, 1), r - half - 1) : 0;
		return left + mid + right;
	}

	/*
	* n这个数裂变的最终形态，数组有多长？
	* */
	public static long size(long n) {
		if (n == 1 || n == 0) {
			/*不裂变*/
			return 1;
		} else {
			/*
			* 长度是
			* n/2 裂变的长度   n%2的值，长度为1   n/2 裂变的长度
			* 3个部分的长度之和
			* */
			long half = size(n / 2);
			return (half << 1) + 1;
		}
	}

	/**
	 * 最优解，线段树的设计思路
	 */
	public static long nums2(long n, long l, long r) {
		HashMap<Long, Long> allMap = new HashMap<>();
		return dp(n, l, r, allMap);
	}

	/*
	* allMap就如同线段树里的区间树的概念
	* 在计算的过程中，记录了n、n/2、n/4、n/8、n/16...这些数会裂变出1几个1
	* 一开始要查n的[l,r]范围上有几个1，
	* 会逐步落实到左右两个n/2,以及对应的范围
	* 直到落实到一个num，范围包含了其全部，就可以直接返回这个数上有几个1
	* 递归走了一个n的区间数的高度，O(logN)
	* */
	public static long dp(long n, long l, long r, HashMap<Long, Long> allMap) {
		if (n == 1 || n == 0) {
			return n == 1 ? 1 : 0;
		}
		long half = size(n / 2);
		long all = (half << 1) + 1;
		long mid = n & 1;
		if (l == 1 && r >= all) {
			if (allMap.containsKey(n)) {
				return allMap.get(n);
			} else {
				/*
				* [l,r]包含了n裂变出的整个范围
				* 递归求出n/2上裂变出几个1
				* n就是上面的结果*2 + n&1
				* */
				long count = dp(n / 2, 1, half, allMap);
				long ans = (count << 1) + mid;
				/*计算中收集结果，下次就直接拿了*/
				allMap.put(n, ans);
				return ans;
			}
		} else {
			/*分发[l,r]的任务*/
			mid = (l > half + 1 || r < half + 1) ? 0 : mid;
			long left = l > half ? 0 : dp(n / 2, l, Math.min(half, r), allMap);
			long right = r > half + 1 ? dp(n / 2, Math.max(l - half - 1, 1), r - half - 1, allMap) : 0;
			return left + mid + right;
		}
	}

	// 为了测试
	// 彻底生成n的最终分裂数组返回
	public static ArrayList<Integer> test(long n) {
		ArrayList<Integer> arr = new ArrayList<>();
		process(n, arr);
		return arr;
	}

	public static void process(long n, ArrayList<Integer> arr) {
		if (n == 1 || n == 0) {
			arr.add((int) n);
		} else {
			process(n / 2, arr);
			arr.add((int) (n % 2));
			process(n / 2, arr);
		}
	}

	public static void main(String[] args) {
		long num = 671;
		ArrayList<Integer> ans = test(num);
		int testTime = 10000;
		System.out.println("功能测试开始");
		for (int i = 0; i < testTime; i++) {
			int a = (int) (Math.random() * ans.size()) + 1;
			int b = (int) (Math.random() * ans.size()) + 1;
			int l = Math.min(a, b);
			int r = Math.max(a, b);
			int ans1 = 0;
			for (int j = l - 1; j < r; j++) {
				if (ans.get(j) == 1) {
					ans1++;
				}
			}
			long ans2 = nums1(num, l, r);
			long ans3 = nums2(num, l, r);
			if (ans1 != ans2 || ans1 != ans3) {
				System.out.println("出错了!");
			}
		}
		System.out.println("功能测试结束");
		System.out.println("==============");

		System.out.println("性能测试开始");
		num = (2L << 50) + 22517998136L;
		long l = 30000L;
		long r = 800000200L;
		long start;
		long end;
		start = System.currentTimeMillis();
		System.out.println("nums1结果 : " + nums1(num, l, r));
		end = System.currentTimeMillis();
		System.out.println("nums1花费时间(毫秒) : " + (end - start));

		start = System.currentTimeMillis();
		System.out.println("nums2结果 : " + nums2(num, l, r));
		end = System.currentTimeMillis();
		System.out.println("nums2花费时间(毫秒) : " + (end - start));
		System.out.println("性能测试结束");
		System.out.println("==============");

		System.out.println("单独展示nums2方法强悍程度测试开始");
		num = (2L << 55) + 22517998136L;
		l = 30000L;
		r = 6431000002000L;
		start = System.currentTimeMillis();
		System.out.println("nums2结果 : " + nums2(num, l, r));
		end = System.currentTimeMillis();
		System.out.println("nums2花费时间(毫秒) : " + (end - start));
		System.out.println("单独展示nums2方法强悍程度测试结束");
		System.out.println("==============");

	}

}
