package class45;

import java.util.Arrays;

/**
 * 来自京东笔试
 * 小明手中有n块积木，并且小明知道每块积木的重量。现在小明希望将这些积木堆起来
 * 要求是任意一块积木如果想堆在另一块积木上面，那么要求：
 * 1) 上面的积木重量不能小于下面的积木重量
 * 2) 上面积木的重量减去下面积木的重量不能超过x
 * 3) 每堆中最下面的积木没有重量要求
 * 现在小明有一个机会，除了这n块积木，还可以获得k块任意重量的积木。
 * 小明希望将积木堆在一起，同时希望积木堆的数量越少越好，你能帮他找到最好的方案么？
 * 输入描述:
 * 第一行三个整数n,k,x，1<=n<=200000，0<=x,k<=1000000000
 * 第二行n个整数，表示积木的重量，任意整数范围都在[1,1000000000]
 * 样例输出：
 * n = 13 k = 1 x = 38
 * arr : 20 20 80 70 70 70 420 5 1 5 1 60 90
 * 输出：2
 * 解释：
 * 两堆分别是
 * 1 1 5 5 20 20 x 60 70 70 70 80 90
 * 420
 * 其中x是一个任意重量的积木，夹在20和60之间可以让积木继续往上搭
 *
 * 解题：
 * 	本题是京东最后一题，最优解是贪心。本题的贪心不难想，但只要是贪心，就需要对数器，需要智力
 * 思路：
 * 	首先看数据量，本题解法与x，k没有关系，所以只和n有关，应该是一个O(N)或者O(N*？)
 * 	很多同学会从动态规划开始
 */
public class Code01_SplitBuildingBlock {

	/**
	 * 从动态规划开始
	 * 发现可变参数里有一个参数r：表示剩余魔法积木的数量
	 * 这个值的变化范围太大
	 * 这个参数的用途是表示用掉了多少块魔法积木，还剩多少块魔法积木
	 * 最优解一定与r没有关系，表明不是所有的魔法积木都要用完
	 * 所以一定有一种方式，可以知道在哪里用魔法积木
	 */
	// 这是启发解
	// arr是从小到大排序的，x是限制，固定参数
	// 当前来到i位置，积木重量arr[i]
	// 潜台词 : 当前i位置的积木在一个堆里，堆的开头在哪？之前已经决定了
	// i i+1 该在一起 or 该用魔法积木弥合 or 该分家
	// 返回值：arr[i....]最少能分几个堆？
	public static int zuo(int[] arr, int x, int i, int r) {
		if (i == arr.length - 1) {
			/*
			* 到最后一个积木了，这是目前这一堆的最后一个积木，
			* 是否需要开新的堆不是当前的积木操心的，是前一个积木就计算好了的
			* 所以返回1，表明这是这一堆里最后一块
			* */
			return 1;
		}
		// i没到最后一个数
		if (arr[i + 1] - arr[i] <= x) { // 一定贴在一起
			/*下一块和当前块差值很小，可以连在一起*/
			return zuo(arr, x, i + 1, r);
		} else { // 弥合！分家
			// 分家
			/* 1+ 表示当前这一堆结束，从下一块开始开新堆*/
			int p1 = 1 + zuo(arr, x, i + 1, r);
			// 弥合
			int p2 = Integer.MAX_VALUE;
			/*弥合需要的积木数*/
			int need = (arr[i + 1] - arr[i] - 1) / x;
			if (r >= need) {
				/*剩余的积木数够的话，去下个积木，还是与当前一堆的*/
				p2 = zuo(arr, x, i + 1, r - need);
			}
			return Math.min(p1, p2);
		}
	}

	/**
	 * 最优解：
	 * 贪心1： 从小的积木开水搭，只要两个积木之前的差值没有超标，没必要开新的堆
	 * 贪心2： 魔法积木用来弥合差值小的两个堆
	 */
	// 这是最优解
	// arr里装着所有积木的重量
	// k是魔法积木的数量，每一块魔法积木都能变成任何重量
	// x差值，后 - 前 <= x
	public static int minSplit(int[] arr, int k, int x) {
		/*排完序后，从小到大开始搭*/
		Arrays.sort(arr);
		int n = arr.length;
		/*needs[0]=10: 第一堆和第二堆的差值是10*/
		int[] needs = new int[n];
		/*和needs配和使用，记录堆之间的差值*/
		int size = 0;
		/*分成了几个堆，记录一下*/
		int splits = 1;
		for (int i = 1; i < n; i++) {
			if (arr[i] - arr[i - 1] > x) {
				/*一旦连个挨在一起的积木超标，就分家，记录堆数*/
				needs[size++] = arr[i] - arr[i - 1];
				splits++;
			}
		}
		/*到这里，堆已经都分好了*/
		if (splits == 1 || x == 0 || k == 0) {
			/*
			* splits == 1 只分了1堆
			* x == 0 指标是0，表示重量一样的积木才能堆在一起，这样的话魔法积木不能弥补两个堆的差值
			* k == 0 没有魔法积木
			* */
			return splits;
		}
		/*试图去利用魔法积木，弥合堆！从间隙小的两个堆开始弥合*/
		Arrays.sort(needs, 0, size);
		for (int i = 0; i < size; i++) {
			/*
			* 间隙需要的魔法积木块数
			* 间隙：s
			* 标准间隙：x
			* 需要的块数：(s-x)/x 向上取整
			* 			即 (s-x + x-1)/x
			* 			即 (s-1)/x
			* */
			int need = (needs[i] - 1) / x;
			if (k >= need) {
				splits--;
				k -= need;
			} else {
				break;
			}
		}
		return splits;
	}

}
