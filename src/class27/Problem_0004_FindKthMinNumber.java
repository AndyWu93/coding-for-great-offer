package class27;

/**
 * Given two sorted arrays nums1 and nums2 of size m and n respectively, return the median of the two sorted arrays.
 * The overall run time complexity should be O(log (m+n)).
 *
 * 题意：求两个有序数组的整体中位数
 * 本题思路：
 * arr1，arr2均为有序数组
 * 	1.实现一个函数 getUpMedian(arr1,l1,r1,arr2,l2,r2):返回arr1，arr2的整体上中位数，前提arr1，arr2等长
 * 		函数复杂度要求是O(logN),所以不能排序
 * 		a.arr1的长度为偶数
 * 			求出arr1arr2的上中位数m1，m2，
 * 			如果这两个数相等，就将这个数返回。因为m1前面压了一半，m2前面也压了一半。m1m2右相等，所以这个数就是上中位数
 * 			如果这两个数不相等，假设m2<m1,那m2前面的数不可能，m1后面的数也不可能，那就变成在arr1的前半部分和arr2的后半部分求上中位数。递归调用
 * 		b.arr1的长度为奇数
 * 			前面的步骤和a一样
 * 			到了第三步如果这两个数不相等，假设m2<m1,排除掉m2前面的数和m1后面的数以后，发现m2无法排除，导致剩下的长度不相等，无法调用递归
 * 			这时候就要看一下m2是不是大于m1前面的一个数，如果是，m2就是结果，返回。如果不是，排除掉m2后，两个数组等长了，可以递归调用
 *
 * 	2.根据getUpMedian函数实现 findKthNum(arr1,arr2,k):返回arr1，arr2整体第k小的数
 *		k的取值范围是从1到arr1长度+arr2的长度
 *		a.k<=min(arr1.length,arr2.length)
 *			arr1，arr2各取出前k个数，直接调用getUpMedian函数，就是结果
 *		b.max(arr1.length,arr2.length)<k<=sum(arr1.length,arr2.length)
 *			这时候需要淘汰掉arr1arr2中的一部分数，那一部分？都是淘汰前面的位置。
 *			如何淘汰？
 *			假设arr1[0]>arr2[n-1],arr1前面的一直到k-arr2.length的位置都淘汰了
 *			假设arr2[0]>arr1[n-1],arr2前面的一直到k-arr1.length的位置都淘汰了
 *			这是假设arr1一共淘汰了x个数，arr1一共淘汰了y个数，
 *			arr1arr2剩下的数一样多。是不是直接求剩下的数的中位数呢？
 *			不是，arr1arr2需要各自再淘汰一个数，因为x+y+剩下的中位数，刚好是第k-1个，不是第k个
 *			arr1arr2如何各自再淘汰一个数？
 *			arr1看第x+1个数，和arr2[n-1]比较，如果大于等于arr2[n-1]，那第x+1个数就是结果，否则淘汰掉
 *			arr2看第y+1个数，和arr1[n-1]比较，如果大于等于arr1[n-1]，那第y+1个数就是结果，否则淘汰掉
 *			剩下的数就直接调用getUpMedian函数，结果返回
 *		c.min(arr1.length,arr2.length)<k<=max(arr1.length,arr2.length)
 *			这种情况只需要淘汰掉长数组中的一部分数就可以了，短数组不需要淘汰。
 *			长数组淘汰哪些？
 *			假设long[0]>short[n-1],arr1前面的一直到k-short.length的位置都淘汰了
 *			long[k..n]这部分的位置也是不可能的
 *			这时候长数组剩下的数比短数组还多一个，就看下长数组第x+1个数，和short[n-1]比较，如果大于等于，那x+1个数就是结果
 *			否则淘汰x+1个数，剩下的数和短数组，调用getUpMedian函数，结果返回
 *			为什么淘汰第x+1个数，因为这样淘汰掉的数加上剩下的中位数个数，结果刚好等于k
 *
 * 	3.用findKthNum求出中位数返回。
 */
// 本题测试链接 : https://leetcode.com/problems/median-of-two-sorted-arrays/
public class Problem_0004_FindKthMinNumber {

	public double findMedianSortedArrays(int[] nums1, int[] nums2) {
		int size = nums1.length + nums2.length;
		/*长度为偶数个，表示需要上下中位数两个数相加除二*/
		boolean even = (size & 1) == 0;
		if (nums1.length != 0 && nums2.length != 0) {
			if (even) {
				/*从1开始，取上下中位数，相加除二*/
				return (double) (findKthNum(nums1, nums2, size / 2) + findKthNum(nums1, nums2, size / 2 + 1)) / 2D;
			} else {
				/*从1开始，取中位数*/
				return findKthNum(nums1, nums2, size / 2 + 1);
			}
		} else if (nums1.length != 0) {
			/*nums2长度为0*/
			if (even) {
				/*下标从0开始，取上下中位数*/
				return (double) (nums1[(size - 1) / 2] + nums1[size / 2]) / 2;
			} else {
				/*下标从0开始，取中位数*/
				return nums1[size / 2];
			}
		} else if (nums2.length != 0) {
			/*nums1长度为0*/
			if (even) {
				return (double) (nums2[(size - 1) / 2] + nums2[size / 2]) / 2;
			} else {
				return nums2[size / 2];
			}
		} else {
			return 0;
		}
	}

	/*
	* 在两个都有序的数组中，找整体第K小的数
	* 可以做到O(log(Min(M,N))),为什么？因为最终调用getUpMedian时，传入的数组长度不会超过短数组长度
	* */
	public static int findKthNum(int[] arr1, int[] arr2, int kth) {
		int[] longs = arr1.length >= arr2.length ? arr1 : arr2;
		int[] shorts = arr1.length < arr2.length ? arr1 : arr2;
		int l = longs.length;
		int s = shorts.length;
		/*k小于等于短数组长度，直接拿各自前k个数调用getUpMedian*/
		if (kth <= s) { // 1)
			return getUpMedian(shorts, 0, kth - 1, longs, 0, kth - 1);
		}
		/*
		* k小于长数组长度
		* 长短数组各自淘汰掉前面的数以后，都要看下紧接着后面的一个数，是不是中位数
		* 剩下的调用算法，这样淘汰掉的数和中位数位置加起来刚好等于k
		* */
		if (kth > l) { // 3)
			/*短数组：淘汰了k-长数组长度个位置，接下来验证下一个，和长数组最后一个值比较*/
			if (shorts[kth - l - 1] >= longs[l - 1]) {
				return shorts[kth - l - 1];
			}
			/*长数组：淘汰了k-短数组长度个位置，接下来验证下一个，和短数组最后一个值比较*/
			if (longs[kth - s - 1] >= shorts[s - 1]) {
				return longs[kth - s - 1];
			}
			/*到淘汰完了，剩下的数调用算法*/
			return getUpMedian(shorts, kth - l, s - 1, longs, kth - s, l - 1);
		}
		// 2)  s < k <= l
		/*
		* k在中间位置
		* 仅长数组需要淘汰首尾两部分，首部淘汰了k-短数组长度个位置，后面一个数验一下，和短数组最后一个值比较
		* */
		if (longs[kth - s - 1] >= shorts[s - 1]) {
			return longs[kth - s - 1];
		}
		/*剩下的数：短数组全部，长数组：k-s:k减短数组长度，k-1：长数组后面淘汰的部分是k到n-1位置，所以传入到k-1位置为止*/
		return getUpMedian(shorts, 0, s - 1, longs, kth - s, kth - 1);
	}


	/*
	* A[s1...e1]
	* B[s2...e2]
	* AB一定等长！
	* 返回整体的，上中位数！8（4） 10（5） 12（6）
	* 这里加了s1e1s2e2参数是为了下面递归调用的时候改成了迭代
	* 复杂度为什么是O(logN)，因为每次迭代arr1arr2砍掉了一半
	* */
	public static int getUpMedian(int[] A, int s1, int e1, int[] B, int s2, int e2) {
		int mid1 = 0;
		int mid2 = 0;
		while (s1 < e1) {
			// mid1 = s1 + (e1 - s1) >> 1
			/*求出两个arr的上中位数位置*/
			mid1 = (s1 + e1) / 2;
			mid2 = (s2 + e2) / 2;
			if (A[mid1] == B[mid2]) {
				/*如果相等，m1m2前面压着的数加起来正好是整体的一半，m1或者m2正好是上中位数的位置，直接返回*/
				return A[mid1];
			}
			// 两个中点一定不等！
			/*不相等，就要分情况讨论，淘汰掉arr1arr2不可能的位置，将剩下的数递归调用求中位数*/
			if (((e1 - s1 + 1) & 1) == 1) {
				/*
				* 奇数长度
				* 不能直接淘汰arr1 arr2中不可能出现中位数的一半，然后递归，较小的那个中位数无法淘汰，需要看一下
				* */
				if (A[mid1] > B[mid2]) {
					/*m2较小，无法淘汰，看下m2是不是比m1前面的数大，如果是的话，m2刚好就在中位数位置*/
					if (B[mid2] >= A[mid1 - 1]) {
						return B[mid2];
					}
					/*arr1后面的一半被淘汰掉了，包含m1*/
					e1 = mid1 - 1;
					/*arr2的前面一半被淘汰掉了，包含m2*/
					s2 = mid2 + 1;
				} else { // A[mid1] < B[mid2]
					/*m1较小，无法淘汰，看下m1是不是比m2前面的数大，如果是的话，m1刚好就在中位数位置*/
					if (A[mid1] >= B[mid2 - 1]) {
						return A[mid1];
					}
					/*arr2后面的一半被淘汰掉了，包含m2*/
					e2 = mid2 - 1;
					/*arr1前面的一半被淘汰掉了，包含m1*/
					s1 = mid1 + 1;
				}
			} else {
				/*
				* 偶数长度
				* 直接淘汰掉一半，后递归
				* */
				if (A[mid1] > B[mid2]) {
					/*m1大，m1后面的数砍掉，m1要留下来；m2前面的数砍掉，包括m2*/
					e1 = mid1;
					s2 = mid2 + 1;
				} else {
					e2 = mid2;
					s1 = mid1 + 1;
				}
			}
		}
		/*没能够在循环中返回，跳出循环时，arr1arr2只剩下一个数，这两个数一定有一个是中位数，为什么取小的，因为要上中位数*/
		return Math.min(A[s1], B[s2]);
	}

}
