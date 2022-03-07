package class01;

import java.util.Arrays;

/**
 * 给定一个有序数组arr，代表坐落在X轴上的点
 * 给定一个正数K，代表绳子的长度
 * 返回绳子最多压中几个点？
 * 即使绳子边缘处盖住点也算盖住
 * 解法一：贪心O(N*logN)
 * 	枚举arr中的每一个位置的数作为绳子的末尾，（贪心：绳子的末尾一定压中了1个点，而不是x轴上一些无关紧要的数）
 * 	找到绳子开头的位置（二分），收集答案
 *
 * 解法二：使用窗口
 * 	窗口LR，L从0位置开始往右，每次L在一个位置时，R往右到不能再往右，收集答案再移动L
 * 	LR不回退，O(N)
 *
 */
public class Code01_CordCoverMaxPoint {

	/**
	 * 贪心解
	 */
	public static int maxPoint1(int[] arr, int L) {
		int res = 1;
		for (int i = 0; i < arr.length; i++) {
			int nearest = nearestIndex(arr, i, arr[i] - L);
			res = Math.max(res, i - nearest + 1);
		}
		return res;
	}

	public static int nearestIndex(int[] arr, int R, int value) {
		int L = 0;
		int index = R;
		while (L <= R) {
			int mid = L + ((R - L) >> 1);
			if (arr[mid] >= value) {
				index = mid;
				R = mid - 1;
			} else {
				L = mid + 1;
			}
		}
		return index;
	}

	/**
	 * 窗口
	 */
	public static int maxPoint2(int[] arr, int L) {
		int left = 0;
		int right = 0;
		int N = arr.length;
		int max = 0;
		while (left < N) {
			while (right < N && arr[right] - arr[left] <= L) {
				/*R不越界，且L-R之间的距离小于等于k*/
				right++;
			}
			/*出while后收集答案，L++*/
			max = Math.max(max, right - (left++));
		}
		return max;
	}

	// for test
	public static int test(int[] arr, int L) {
		int max = 0;
		for (int i = 0; i < arr.length; i++) {
			int pre = i - 1;
			while (pre >= 0 && arr[i] - arr[pre] <= L) {
				pre--;
			}
			max = Math.max(max, i - pre);
		}
		return max;
	}

	// for test
	public static int[] generateArray(int len, int max) {
		int[] ans = new int[(int) (Math.random() * len) + 1];
		for (int i = 0; i < ans.length; i++) {
			ans[i] = (int) (Math.random() * max);
		}
		Arrays.sort(ans);
		return ans;
	}

	public static void main(String[] args) {
		int len = 100;
		int max = 1000;
		int testTime = 100000;
		System.out.println("测试开始");
		for (int i = 0; i < testTime; i++) {
			int L = (int) (Math.random() * max);
			int[] arr = generateArray(len, max);
			int ans1 = maxPoint1(arr, L);
			int ans2 = maxPoint2(arr, L);
			int ans3 = test(arr, L);
			if (ans1 != ans2 || ans2 != ans3) {
				System.out.println("oops!");
				break;
			}
		}

	}

}
