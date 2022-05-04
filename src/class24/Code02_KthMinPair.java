package class24;

import java.util.Arrays;
import java.util.Comparator;

/**
 * 字节面试题
 * 长度为N的数组arr，一定可以组成N^2个数字对。例如arr = [3,1,2]，数字对有(3,3) (3,1) (3,2) (1,3) (1,1) (1,2) (2,3) (2,1) (2,2)
 * 也就是任意两个数都可以，而且自己和自己也算数字对。数字对怎么排序？第一维数据从小到大；第一维数据一样的，第二维数组也从小到大
 * 所以上面的数值对排序的结果为：(1,1)(1,2)(1,3)(2,1)(2,2)(2,3)(3,1)(3,2)(3,3)。给定一个数组arr，和整数k，返回第k小的数值对
 *
 * 解题：
 * 思路：先定位x在arr中的位置，x确认好之后，再定位y的位置
 * 	假定该数组arr是有序的，长度为n，需要找第k小的二元组(x,y)。
 * 	arr[0]构成了n个元组（arr[0]作为第一元），arr[1]构成了n个元组...所以n个数一组，
 * 	根据k能够推出x在第几组，即x在arr中的位置：x=arr[i]=arr[(k-1)/n]（这里k为什么要-1，因为k是从1开始的），记为M。
 * 	遍历数组，找到小于M的数字为a个，等于M的数为b个，
 * 	因为第一个数用小于M的数组成的元组一定小于(M,y)
 * 	那就知道所有的元组中小于(M,y)的元组个数为a*n个，于是该问题就变成了第一元固定为M，找到第k-a*n（记为S）小的元组(M,y)
 * 	因为等于M的数有b个，arr[0]构成b个元组（arr[0]作为第二元），arr[1]构成b个元组...所以b个数一组，
 * 	根据S能够推出y在第几组，即y在arr中的位置：y=arr[j]=arr[(S-1)/b]（这里S-1，同样是因为S继承了k从1开始），即y=arr[(k-a*n-1)/b]
 *
 * 优化：
 * 	因为需要假定数组有序，排序提高了复杂度。但真的需要排序吗？
 * 	只是需要找到第arr[i],arr[j]，就是arr中第i和第j位置的数（如果排好序的话），这就是一个求无序arr中第k小的数。
 * 	最快的是模拟快排：O(n)或者bfprt：O(n)
 */
public class Code02_KthMinPair {

	public static class Pair {
		public int x;
		public int y;

		Pair(int a, int b) {
			x = a;
			y = b;
		}
	}

	public static class PairComparator implements Comparator<Pair> {

		@Override
		public int compare(Pair arg0, Pair arg1) {
			return arg0.x != arg1.x ? arg0.x - arg1.x : arg0.y - arg1.y;
		}

	}

	// O(N^2 * log (N^2))的复杂度，你肯定过不了
	// 返回的int[] 长度是2，{3,1} int[2] = [3,1]
	public static int[] kthMinPair1(int[] arr, int k) {
		int N = arr.length;
		if (k > N * N) {
			return null;
		}
		Pair[] pairs = new Pair[N * N];
		int index = 0;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				pairs[index++] = new Pair(arr[i], arr[j]);
			}
		}
		Arrays.sort(pairs, new PairComparator());
		return new int[] { pairs[k - 1].x, pairs[k - 1].y };
	}

	// O(N*logN)的复杂度，你肯定过了
	public static int[] kthMinPair2(int[] arr, int k) {
		int N = arr.length;
		if (k > N * N) {
			return null;
		}
		// O(N*logN)
		Arrays.sort(arr);
		// 第K小的数值对，第一维数字，是什么 是arr中
		int fristNum = arr[(k - 1) / N];
		int lessFristNumSize = 0;// 数出比fristNum小的数有几个
		int fristNumSize = 0; // 数出==fristNum的数有几个
		// <= fristNum
		for (int i = 0; i < N && arr[i] <= fristNum; i++) {
			if (arr[i] < fristNum) {
				lessFristNumSize++;
			} else {
				fristNumSize++;
			}
		}
		int rest = k - (lessFristNumSize * N);
		return new int[] { fristNum, arr[(rest - 1) / fristNumSize] };
	}

	/**
	 * O(N)的复杂度
	 */
	public static int[] kthMinPair3(int[] arr, int k) {
		int N = arr.length;
		if (k > N * N) {
			return null;
		}
		// 在无序数组中，找到第K小的数，返回值
		// 第K小，以1作为开始
		/*直接定位第一元的index*/
		int fristNum = getMinKth(arr, (k - 1) / N);
		/*小于x的数统计*/
		int lessFristNumSize = 0;
		/*等于x的数统计*/
		int fristNumSize = 0;
		for (int i = 0; i < N; i++) {
			if (arr[i] < fristNum) {
				lessFristNumSize++;
			}
			if (arr[i] == fristNum) {
				fristNumSize++;
			}
		}
		/*在第一元是x的元组中排第rest，从1开始*/
		int rest = k - (lessFristNumSize * N);
		/*直接定位第二元的index，(rest-a*n-1)/b*/
		return new int[] { fristNum, getMinKth(arr, (rest - 1) / fristNumSize) };
	}

	// 改写快排，时间复杂度O(N)
	// 在无序数组arr中，找到，如果排序的话，arr[index]的数是什么？
	public static int getMinKth(int[] arr, int index) {
		int L = 0;
		int R = arr.length - 1;
		int pivot = 0;
		int[] range = null;
		while (L < R) {
			pivot = arr[L + (int) (Math.random() * (R - L + 1))];
			range = partition(arr, L, R, pivot);
			if (index < range[0]) {
				R = range[0] - 1;
			} else if (index > range[1]) {
				L = range[1] + 1;
			} else {
				return pivot;
			}
		}
		return arr[L];
	}

	public static int[] partition(int[] arr, int L, int R, int pivot) {
		int less = L - 1;
		int more = R + 1;
		int cur = L;
		while (cur < more) {
			if (arr[cur] < pivot) {
				swap(arr, ++less, cur++);
			} else if (arr[cur] > pivot) {
				swap(arr, cur, --more);
			} else {
				cur++;
			}
		}
		return new int[] { less + 1, more - 1 };
	}

	public static void swap(int[] arr, int i, int j) {
		int tmp = arr[i];
		arr[i] = arr[j];
		arr[j] = tmp;
	}

	// 为了测试，生成值也随机，长度也随机的随机数组
	public static int[] getRandomArray(int max, int len) {
		int[] arr = new int[(int) (Math.random() * len) + 1];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (int) (Math.random() * max) - (int) (Math.random() * max);
		}
		return arr;
	}

	// 为了测试
	public static int[] copyArray(int[] arr) {
		if (arr == null) {
			return null;
		}
		int[] res = new int[arr.length];
		for (int i = 0; i < arr.length; i++) {
			res[i] = arr[i];
		}
		return res;
	}

	// 随机测试了百万组，保证三种方法都是对的
	public static void main(String[] args) {
		int max = 100;
		int len = 30;
		int testTimes = 100000;
		System.out.println("test bagin, test times : " + testTimes);
		for (int i = 0; i < testTimes; i++) {
			int[] arr = getRandomArray(max, len);
			int[] arr1 = copyArray(arr);
			int[] arr2 = copyArray(arr);
			int[] arr3 = copyArray(arr);
			int N = arr.length * arr.length;
			int k = (int) (Math.random() * N) + 1;
			int[] ans1 = kthMinPair1(arr1, k);
			int[] ans2 = kthMinPair2(arr2, k);
			int[] ans3 = kthMinPair3(arr3, k);
			if (ans1[0] != ans2[0] || ans2[0] != ans3[0] || ans1[1] != ans2[1] || ans2[1] != ans3[1]) {
				System.out.println("Oops!");
			}
		}
		System.out.println("test finish");
	}

}
