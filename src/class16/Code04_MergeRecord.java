package class16;

/**
 * 腾讯原题
 *
 * 给定整数power，给定一个数组arr，给定一个数组reverse。含义如下：
 * arr的长度一定是2的power次方，reverse中的每个值一定都在0~power范围。
 * 例如power = 2, arr = {3, 1, 4, 2}，reverse = {0, 1, 0, 2}
 * 任何一个在前的数字可以和任何一个在后的数组，构成一对数。可能是升序关系、相等关系或者降序关系。
 * 比如arr开始时有如下的降序对：(3,1)、(3,2)、(4,2)，一共3个。
 * 接下来根据reverse对arr进行调整：
 * reverse[0] = 0, 表示在arr中，划分每1(2的0次方)个数一组，然后每个小组内部逆序，那么arr变成
 * [3,1,4,2]，此时有3个逆序对。
 * reverse[1] = 1, 表示在arr中，划分每2(2的1次方)个数一组，然后每个小组内部逆序，那么arr变成
 * [1,3,2,4]，此时有1个逆序对
 * reverse[2] = 0, 表示在arr中，划分每1(2的0次方)个数一组，然后每个小组内部逆序，那么arr变成
 * [1,3,2,4]，此时有1个逆序对。
 * reverse[3] = 2, 表示在arr中，划分每4(2的2次方)个数一组，然后每个小组内部逆序，那么arr变成
 * [4,2,3,1]，此时有5个逆序对。
 * 所以返回[3,1,1,5]，表示每次调整之后的逆序对数量。
 *
 * 输入数据状况：
 * power的范围[0,20]
 * arr长度范围[1,10的7次方]
 * reverse长度范围[1,10的6次方]
 *
 * 解题：
 * 	本题要求每一步调整都要返回逆序对数量，肯定不可能每次调整完了，拿调整好的数组求逆序对
 * 	所以有没有一种方案，在调整之后能够O(1)得到逆序对数量
 * 设计方案：
 * 	对于原数组arr生成两个辅助数组recordDown、recordUp
 * 	recordDown[i]: arr中，每2^i个数为一组，每组内平均分成左右两个部分，求左边相对右边的逆序对（简称规模逆序对）；recordDown[i]是所有的组的逆序对之和
 * 	recordUp[i]  : arr中，每2^i个数为一组，每组内平均分成左右两个部分，求左边相对右边的正序对（简称规模正序对）；recordDown[i]是所有的组的正序对之和
 * 所以
 * 	如果来到reverse[i]=2，表示4个数一组，组内逆序。
 * 	那么
 * 	recordDown[2]的数量就变成了recordUp[2]，recordUp[2]的数量就变成了recordDown[2]
 * 	recordDown[1]的数量就变成了recordUp[1]，recordUp[1]的数量就变成了recordDown[1]
 * 	而，recordDown[3,4...]都不受影响
 * 这样
 * 	当通过reverse调整后，新的逆序对数量就可以通过交换recordDown、recordUp的值后，O(1)得到
 * */
public class Code04_MergeRecord {
	


	public static int[] reversePair1(int[] originArr, int[] reverseArr, int power) {
		int[] ans = new int[reverseArr.length];
		for (int i = 0; i < reverseArr.length; i++) {
			reverseArray(originArr, 1 << (reverseArr[i]));
			ans[i] = countReversePair(originArr);
		}
		return ans;
	}

	public static void reverseArray(int[] originArr, int teamSize) {
		if (teamSize < 2) {
			return;
		}
		for (int i = 0; i < originArr.length; i += teamSize) {
			reversePart(originArr, i, i + teamSize - 1);
		}
	}

	public static void reversePart(int[] arr, int L, int R) {
		while (L < R) {
			int tmp = arr[L];
			arr[L++] = arr[R];
			arr[R--] = tmp;
		}
	}

	public static int countReversePair(int[] originArr) {
		int ans = 0;
		for (int i = 0; i < originArr.length; i++) {
			for (int j = i + 1; j < originArr.length; j++) {
				if (originArr[i] > originArr[j]) {
					ans++;
				}
			}
		}
		return ans;
	}

	/**
	 * 解题
	 */
	public static int[] reversePair2(int[] originArr, int[] reverseArr, int power) {
		/*下面两行代码是一起的，为了获得reverse数组*/
		int[] reverse = copyArray(originArr);
		reversePart(reverse, 0, reverse.length - 1);
		/*
		* 两个辅助数组，存储规模逆序对之和
		* 注：recordDown[0]、recordUp[0]都没有值，因为最少2个数才能成为一个规模
		* */
		int[] recordDown = new int[power + 1];
		int[] recordUp = new int[power + 1];
		/*求出原数组arr的规模逆序对之和，放在recordDown[i]中*/
		process(originArr, 0, originArr.length - 1, power, recordDown);
		/*求出原数组arr的规模正序对之和，也就是reverse数组的规模逆序对之和，放在recordUp[i]中*/
		process(reverse, 0, reverse.length - 1, power, recordUp);
		int[] ans = new int[reverseArr.length];
		for (int i = 0; i < reverseArr.length; i++) {
			int curPower = reverseArr[i];
			/*小于等于curPower的规模逆序对之和都要和规模正序对之和交换*/
			for (int p = 1; p <= curPower; p++) {
				int tmp = recordDown[p];
				recordDown[p] = recordUp[p];
				recordUp[p] = tmp;
			}
			/*所有的规模逆序对之和求累加和，就是整个arr的逆序对之和*/
			for (int p = 1; p <= power; p++) {
				ans[i] += recordDown[p];
			}
		}
		return ans;
	}

	// originArr[L...R]完成排序！
	// L...M左  M...R右  merge
	// L...R  2的power次方
	/*
	* originArr中从L..R，LR的范围规模为power，即LR中元素个数为2^power个
	* LR中分为左右两组，左组相对右组的逆序对的数量累加到record[power]中
	* */
	public static void process(int[] originArr, int L, int R, int power, int[] record) {
		if (L == R) {
			return;
		}
		int mid = L + ((R - L) >> 1);
		process(originArr, L, mid, power - 1, record);
		process(originArr, mid + 1, R, power - 1, record);
		record[power] += merge(originArr, L, mid, R);
	}

	/*
	* 归并排序下计算逆序对的个数
	* */
	public static int merge(int[] arr, int l, int m, int r) {
		int[] help = new int[r - l + 1];
		int i = 0;
		int p1 = l;
		int p2 = m + 1;
		int ans = 0;
		while (p1 <= m && p2 <= r) {
			/*左组指针大于右组指针，那么左组单前指针以及后面的数都可以与右组指针形成逆序对*/
			ans += arr[p1] > arr[p2] ? (m - p1 + 1) : 0;
			help[i++] = arr[p1] <= arr[p2] ? arr[p1++] : arr[p2++];
		}
		while (p1 <= m) {
			help[i++] = arr[p1++];
		}
		while (p2 <= r) {
			help[i++] = arr[p2++];
		}
		for (i = 0; i < help.length; i++) {
			arr[l + i] = help[i];
		}
		return ans;
	}

	// for test
	public static int[] generateRandomOriginArray(int power, int value) {
		int[] ans = new int[1 << power];
		for (int i = 0; i < ans.length; i++) {
			ans[i] = (int) (Math.random() * value);
		}
		return ans;
	}

	// for test
	public static int[] generateRandomReverseArray(int len, int power) {
		int[] ans = new int[len];
		for (int i = 0; i < ans.length; i++) {
			ans[i] = (int) (Math.random() * (power + 1));
		}
		return ans;
	}

	// for test
	public static void printArray(int[] arr) {
		System.out.println("arr size : " + arr.length);
		for (int i = 0; i < arr.length; i++) {
			System.out.print(arr[i] + " ");
		}
		System.out.println();
	}

	// for test
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

	// for test
	public static boolean isEqual(int[] arr1, int[] arr2) {
		if ((arr1 == null && arr2 != null) || (arr1 != null && arr2 == null)) {
			return false;
		}
		if (arr1 == null && arr2 == null) {
			return true;
		}
		if (arr1.length != arr2.length) {
			return false;
		}
		for (int i = 0; i < arr1.length; i++) {
			if (arr1[i] != arr2[i]) {
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args) {
		int powerMax = 8;
		int msizeMax = 10;
		int value = 30;
		int testTime = 50000;
		System.out.println("test begin");
		for (int i = 0; i < testTime; i++) {
			int power = (int) (Math.random() * powerMax) + 1;
			int msize = (int) (Math.random() * msizeMax) + 1;
			int[] originArr = generateRandomOriginArray(power, value);
			int[] originArr1 = copyArray(originArr);
			int[] originArr2 = copyArray(originArr);
			int[] reverseArr = generateRandomReverseArray(msize, power);
			int[] reverseArr1 = copyArray(reverseArr);
			int[] reverseArr2 = copyArray(reverseArr);
			int[] ans1 = reversePair1(originArr1, reverseArr1, power);
			int[] ans2 = reversePair2(originArr2, reverseArr2, power);
			if (!isEqual(ans1, ans2)) {
				System.out.println("Oops!");
			}
		}
		System.out.println("test finish!");

		powerMax = 20;
		msizeMax = 1000000;
		value = 1000;
		int[] originArr = generateRandomOriginArray(powerMax, value);
		int[] reverseArr = generateRandomReverseArray(msizeMax, powerMax);
		// int[] ans1 = reversePair1(originArr1, reverseArr1, powerMax);
		long start = System.currentTimeMillis();
		reversePair2(originArr, reverseArr, powerMax);
		long end = System.currentTimeMillis();
		System.out.println("run time : " + (end - start) + " ms");
	}

}
