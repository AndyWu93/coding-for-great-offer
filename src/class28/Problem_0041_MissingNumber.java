package class28;

/**
 * Given an unsorted integer array nums, return the smallest missing positive integer.
 *
 * You must implement an algorithm that runs in O(n) time and uses constant extra space.
 * 题意：一个任意数组成的数组arr，返回arr中缺失的最小正整数，要求时间复杂度O(n)额外空间复杂度O(1)
 * 解题：
 * 整体思路：将arr数组调整为如下规则：arr[i]=i+1
 * 流程：
 * 	将数组arr划分为3个区域，有效期，未知区，垃圾区
 * 	有效区：遵循规则arr[i]=i+1,一开始的区域是-1往左
 * 	垃圾区：小于等于有效区的数、大于R(垃圾区的开始位置)的数、在未知区域已经合规的数
 * 	遍历arr，将合适的数发配到有效区和垃圾区，最终R+1就是题解
 */
// 测试链接：https://leetcode.com/problems/first-missing-positive/
public class Problem_0041_MissingNumber {

	public static int firstMissingPositive(int[] arr) {
		// l是盯着的位置
		// 0 ~ L-1有效区
		int L = 0;
		int R = arr.length;
		/*遍历过程中一直盯着L位置的数*/
		while (L != R) {
			if (arr[L] == L + 1) {
				/*已经合规，有效区扩展*/
				L++;
			} else if (arr[L] <= L || arr[L] > R || arr[arr[L] - 1] == arr[L]) { // 垃圾的情况
				/*
				* 垃圾的情况：
				* arr[L] <= L：小于等于有效区的数
				* arr[L] > R：指望在前面arr[0..R-1]位置收集1~R，大于R的数没用
				* arr[L] == arr[arr[L] - 1]：arr[L]已经在合规的位置上了，即arr[L]-1的位置上，那此时L位置上的数就是多余的
				* 垃圾数统一发到垃圾区
				 * */
				swap(arr, L, --R);
			} else {
				/*arr[L]不是垃圾，只是没有放在合规的位置上，把它放到合规的位置上的数交换一下，下一层继续盯着这个位置*/
				swap(arr, L, arr[L] - 1);
			}
		}
		return L + 1;
	}

	public static void swap(int[] arr, int i, int j) {
		int tmp = arr[i];
		arr[i] = arr[j];
		arr[j] = tmp;
	}

}
