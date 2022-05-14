package class39;

/**
 * 来自腾讯
 * 给定一个长度为n的数组arr，求有多少个子数组满足:
 * 子数组两端的值，是这个子数组的最小值和次小值，最小值和次小值谁在最左和最右无所谓
 * n<=100000
 *
 * 解题：
 * 	数据量：O(N)的解比较保险
 *	本题肯定是用单调栈解，但是单调栈怎么解，还是要注意一下
 *	参考山峰对的问题，结算的时候需要考虑内部和外部
 * @see class22.Code04_VisibleMountains
 * 流程：
 * 	从左往右遍历arr，通过单调栈结算以arr[i]结尾的子数组个数
 * 	在从右往左遍历arr，通过单调栈结算以arr[i]开头的子数组个数
 * 	栈顶到栈底，严格从小到大，一样大的放一起，记下个数
 *
 * 	从左往右遍历，
 * 	遇到了arr[i]，弹出A，数量是K
 * 		arr[i]作为结尾，与任意一个A都能组成有效的子数组，收集到K个
 * 		K个A之间，两两作为头和尾，也能组成有效的子数组，收集到C(2,K)个
 * 	遍历结束后，
 * 		栈中的数据，没有arr[i]与之组成子数组了，所以他们只能两两组成子数组
 *
 * 	从右往左遍历，
 * 	遇到了arr[i]，弹出A，数量是K
 * 		arr[i]作为开头，与任意一个A都能组成有效的子数组，收集到K个
 * 		K个A之间，之前算过了这里就不用算了
 * 	遍历结束后，
 * 		栈中的数据，没有arr[i]与之组成子数组了，他们两两之前也算过了，所以可以不用考虑了
 */
public class Code02_ValidSequence {
	

	public static int nums(int[] arr) {
		if (arr == null || arr.length < 2) {
			return 0;
		}
		int n = arr.length;
		int[] values = new int[n];
		int[] times = new int[n];
		int size = 0;
		int ans = 0;
		for (int i = 0; i < arr.length; i++) {
			/*栈顶遇到比自己小的弹出*/
			while (size != 0 && values[size - 1] > arr[i]) {
				size--;
				/*弹出时结算，外部内部都要算*/
				ans += times[size] + cn2(times[size]);
			}
			if (size != 0 && values[size - 1] == arr[i]) {
				times[size - 1]++;
			} else {
				values[size] = arr[i];
				times[size++] = 1;
			}
		}
		while (size != 0) {
			/*剩下栈中的内部的算*/
			ans += cn2(times[--size]);
		}
		/*第二次遍历*/
		for (int i = arr.length - 1; i >= 0; i--) {
			/*还是栈顶遇到比自己小的弹出*/
			while (size != 0 && values[size - 1] > arr[i]) {
				/*只计算外部的*/
				ans += times[--size];
			}
			if (size != 0 && values[size - 1] == arr[i]) {
				times[size - 1]++;
			} else {
				values[size] = arr[i];
				times[size++] = 1;
			}
		}
		/*遍历完了栈中剩下的元素不用考虑了*/
		/*返回结果*/
		return ans;
	}

	public static int cn2(int n) {
		return (n * (n - 1)) >> 1;
	}

	// 为了测试
	// 暴力方法
	public static int test(int[] arr) {
		if (arr == null || arr.length < 2) {
			return 0;
		}
		int ans = 0;
		for (int s = 0; s < arr.length; s++) {
			for (int e = s + 1; e < arr.length; e++) {
				int max = Math.max(arr[s], arr[e]);
				boolean valid = true;
				for (int i = s + 1; i < e; i++) {
					if (arr[i] < max) {
						valid = false;
						break;
					}
				}
				ans += valid ? 1 : 0;
			}
		}
		return ans;
	}

	// 为了测试
	public static int[] randomArray(int n, int v) {
		int[] arr = new int[n];
		for (int i = 0; i < n; i++) {
			arr[i] = (int) (Math.random() * v);
		}
		return arr;
	}

	// 为了测试
	public static void printArray(int[] arr) {
		for (int num : arr) {
			System.out.print(num + " ");
		}
		System.out.println();
	}

	// 为了测试
	public static void main(String[] args) {
		int n = 30;
		int v = 30;
		int testTime = 100000;
		System.out.println("测试开始");
		for (int i = 0; i < testTime; i++) {
			int m = (int) (Math.random() * n);
			int[] arr = randomArray(m, v);
			int ans1 = nums(arr);
			int ans2 = test(arr);
			if (ans1 != ans2) {
				System.out.println("出错了!");
				printArray(arr);
				System.out.println(ans1);
				System.out.println(ans2);
			}
		}
		System.out.println("测试结束");
	}

}
