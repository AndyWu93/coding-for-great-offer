package class28;

/**
 * You are given an integer array height of length n. There are n vertical lines drawn such that the two endpoints of the ith line are (i, 0) and (i, height[i]).
 *
 * Find two lines that together with the x-axis form a container, such that the container contains the most water.
 *
 * Return the maximum amount of water a container can store.
 *
 * Notice that you may not slant the container.
 *
 * 题意：给定一个int[],每个值代表高度，求两个高度围成的最大面积
 * 解题：
 * 	本题借用数组3连的第3问的重要思想：舍弃可能性
 * 	准备两个指针LR，
 * 	从两头向中间，谁小，结算谁的面积，然后指针移动。收集最大值。
 * 解析：
 * 	每次计算的时候，并没有严格纠结这个位置能够达到的最大面积（因为前面位置算的时候已经包含了需要算的位置），
 * 	而只是看有没有推高答案的可能性
 */
// 本题测试链接 : https://leetcode.com/problems/container-with-most-water/
public class Problem_0011_ContainerWithMostWater {

	public static int maxArea1(int[] h) {
		int max = 0;
		int N = h.length;
		for (int i = 0; i < N; i++) { // h[i]
			for (int j = i + 1; j < N; j++) { // h[j]
				max = Math.max(max, Math.min(h[i], h[j]) * (j - i));
			}
		}
		return max;
	}

	/**
	 * 最优解，O(N)
	 */
	public static int maxArea2(int[] h) {
		int max = 0;
		/*左右指针*/
		int l = 0;
		int r = h.length - 1;
		while (l < r) {
			/*Math.min(h[l], h[r]) * (r - l):谁小结算谁的面积*/
			max = Math.max(max, Math.min(h[l], h[r]) * (r - l));
			if (h[l] > h[r]) {
				/*谁小，谁先移动，移动方式右指针左移，左指针右移*/
				r--;
			} else {
				l++;
			}
		}
		return max;
	}

}
