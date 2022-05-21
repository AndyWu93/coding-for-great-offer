package class41;

/**
 * 给定一个无序数组，每个位置给定一个属性a,a的值时arr[i]与arr[i-1]的绝对值
 * 问如何调整数组中，让所有位置的属性加起来最小，求调整最少次数
 *
 * 解题：
 * 	观察一下，想要属性和最小，当arr有序的时候，不管正序还是倒序，总属性和一定是最小的
 * 	所以问题就变成了，调整arr变有序的最小代价
 * 	又因为，每个数的大小与数的值范围无关，所以原数组可以做离散化，值就是0~n-1
 * 		如何做离散化？因为arr中每个数都不同，可以把arr copy一份后排序，然后用一张表把值和index对应起来，再在arr中对应着改
 * 	所以原问题变为：
 * 		一个无序数组长度为n，所有数字都不一样，并且值都在[0...n-1]范围上
 * 		返回让这个无序数组变成有序数组的最小交换次数（可以是正序也可以是倒叙，本题解题只给了正序一种方式，倒叙可以自己求，最后取最小值）
 * 本题是贪心算法
 * 	将arr，调整为arr[i]=i,采用下标循环怼的方式，
 * 	假设arr中有k个下标循环怼，其中任意一个怼涉及j个数，交换的次数一定是j-1
 * 	那么一共k个怼，每个怼交换j-1次，所有的怼加起来交换的次数一定是n-k次，这就是最少调整代价
 */
// 来自小红书
// 一个无序数组长度为n，所有数字都不一样，并且值都在[0...n-1]范围上
// 返回让这个无序数组变成有序数组的最小交换次数
public class Code01_MinSwapTimes {

	/**
	 * 对数器
	 */
	// 纯暴力，arr长度大一点都会超时
	// 但是绝对正确
	public static int minSwap1(int[] arr) {
		return process1(arr, 0);
	}

	// 让arr变有序，最少的交换次数是多少！返回
	// times, 之前已经做了多少次交换
	public static int process1(int[] arr, int times) {
		boolean sorted = true;
		for (int i = 1; i < arr.length; i++) {
			if (arr[i - 1] > arr[i]) {
				sorted = false;
				break;
			}
		}
		if (sorted) {
			/*有序了，返回之前调整的代价*/
			return times;
		}
		// 数组现在是无序的状态！
		if (times >= arr.length - 1) {
			/*代价不应该超过n-1,因为每个位置去到该去的位置，整体代价不会超过n-1*/
			return Integer.MAX_VALUE;
		}
		int ans = Integer.MAX_VALUE;
		/*
		* 任意挑选2个位置
		* */
		for (int i = 0; i < arr.length; i++) {
			for (int j = i + 1; j < arr.length; j++) {
				swap(arr, i, j);
				ans = Math.min(ans, process1(arr, times + 1));
				swap(arr, i, j);
			}
		}
		return ans;
	}

	public static void swap(int[] arr, int i, int j) {
		int tmp = arr[i];
		arr[i] = arr[j];
		arr[j] = tmp;
	}

	/**
	 * 贪心
	 */
	// 已知arr中，只有0~n-1这些值，并且都出现1次
	public static int minSwap2(int[] arr) {
		int ans = 0;
		for (int i = 0; i < arr.length; i++) {
			while (i != arr[i]) {
				/*换一次，ans++，换完后，换完后，还是盯着i位置*/
				swap(arr, i, arr[i]);
				ans++;
			}
		}
		return ans;
	}

	// 为了测试
	public static int[] randomArray(int len) {
		int[] arr = new int[len];
		for (int i = 0; i < len; i++) {
			arr[i] = i;
		}
		for (int i = 0; i < len; i++) {
			swap(arr, i, (int) (Math.random() * len));
		}
		return arr;
	}

	public static void main(String[] args) {
		int n = 6;
		int testTime = 2000;
		System.out.println("测试开始");
		for (int i = 0; i < testTime; i++) {
			int len = (int) (Math.random() * n) + 1;
			int[] arr = randomArray(len);
			int ans1 = minSwap1(arr);
			int ans2 = minSwap2(arr);
			if (ans1 != ans2) {
				System.out.println("出错了!");
			}
		}
		System.out.println("测试结束");
	}

}
