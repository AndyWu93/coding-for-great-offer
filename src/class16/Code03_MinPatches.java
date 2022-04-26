package class16;

import java.util.Arrays;

/**
 * 用正数数组arr中的数，组成1~range
 *
 * 思路：
 * 假设没有arr，要组成1~range怎么办？
 * 什么都没有缺1，[1]可以组成1~1
 * 有了1~1，缺2，[1,2]可以组成1~3
 * 有了1~3，缺4，[1,2,4]可以组成1~7
 * 有了1~7，缺8，[1,2,4,8]可以组成1~15
 * 有了1~15，缺16，[1,2,4,8,16]可以组成1~31
 * ...
 * 直到有了1~range
 * 所以假设范围有了1~r,数组中加上r+1，范围就可以变成1~(r+r+1)
 * 那么
 * 针对现有数组arr
 * 先将数组排好序，然后遍历arr，来到了i位置
 * 为了最大限度使用arr[i],先将范围1~(arr[i]-1)搞定，
 * 为了搞定范围1~(arr[i]-1)，范围最后可能扩充到了1~r(r一定大于arr[i]-1)
 * 然后范围再加arr[i],范围扩大至1~r+arr[i]
 */
public class Code03_MinPatches {

	// arr请保证有序，且正数
	public static int minPatches(int[] arr, int aim) {
		/*需要补多少的数字*/
		int patches = 0;
		/*范围(已经完成了1 ~ range的目标)，用long类型*/
		long range = 0;
		/*先排序*/
		Arrays.sort(arr);
		for (int i = 0; i != arr.length; i++) {
			// 1~range
			/*需要range先来到 1 ~ arr[i]-1*/
			while (arr[i] - 1 > range) { // arr[i] 1 ~ arr[i]-1
				/*扩充range*/
				range += range + 1; // range + 1 是缺的数字
				/*表示补了一个数字：range+1*/
				patches++;
				/*其间如果range达到了aim，就可以返回了*/
				if (range >= aim) {
					return patches;
				}
			}
			/*加上现在数组里的数*/
			range += arr[i];
			/*再次看下到了aim没*/
			if (range >= aim) {
				return patches;
			}
		}
		/*跳出循环还是没到aim，就继续往后补数字*/
		while (aim >= range + 1) {
			range += range + 1;
			patches++;
		}
		return patches;
	}

	/**
	 * 如果不想用long类型...
	 * 可以不看
	 */
	public static int minPatches2(int[] arr, int K) {
		int patches = 0; // 缺多少个数字
		int range = 0; // 已经完成了1 ~ range的目标
		for (int i = 0; i != arr.length; i++) {
			// 1~range
			// 1 ~ arr[i]-1
			while (arr[i] > range + 1) { // arr[i] 1 ~ arr[i]-1

				if (range > Integer.MAX_VALUE - range - 1) {
					return patches + 1;
				}

				range += range + 1; // range + 1 是缺的数字
				patches++;
				if (range >= K) {
					return patches;
				}
			}
			if (range > Integer.MAX_VALUE - arr[i]) {
				return patches;
			}
			range += arr[i];
			if (range >= K) {
				return patches;
			}
		}
		while (K >= range + 1) {
			if (K == range && K == Integer.MAX_VALUE) {
				return patches;
			}
			if (range > Integer.MAX_VALUE - range - 1) {
				return patches + 1;
			}
			range += range + 1;
			patches++;
		}
		return patches;
	}

	public static void main(String[] args) {
		int[] test = { 1, 2, 31, 33 };
		int n = 2147483647;
		System.out.println(minPatches(test, n));

	}

}
