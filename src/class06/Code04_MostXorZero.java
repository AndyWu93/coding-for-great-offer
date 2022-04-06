package class06;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 求数组中，最多异或和为0的切割方案下，一共多少块的异或和为0
 * 思路：采用动态规划
 * dp[i]:如果数组长度只到i，这时候最多有多少块的异或和为0：
 * 假设有以下切法为最优切法：
 * 	切法1：i所在的那一块，异或和不为0，所以dp[i] = dp[i-1]
 * 	切法2：i所在的那一块，异或和为0，那需要知道那一块从哪里开始，假设从pre+1开始。所以dp[i] = dp[pre] + 1 (这里的1代表pre->i的异或和为0，作为一个部分)
 * pre怎么求？
 * 	因为a^0=a;
 * 	假设0->i的异或和为sum，只要遍历数组，找出0到最后一个异或和为sum的位置。
 *  可以用map优化一下，key：sum，value：异或和为sum的最后一个下标
 * 	这样就能快速找到pre，
 * dp[i] = max(切法1,切法2)
 * dp[arr.length-1]就是题解
 */
public class Code04_MostXorZero {

	/**
	 * 暴力方法
	 * 时间复杂度：每个index都要决定切或者不切：O(2^N)
	 */
	public static int comparator(int[] arr) {
		if (arr == null || arr.length == 0) {
			return 0;
		}
		int N = arr.length;
		/*前缀异或和数组*/
		int[] eor = new int[N];
		eor[0] = arr[0];
		for (int i = 1; i < N; i++) {
			eor[i] = eor[i - 1] ^ arr[i];
		}
		return process(eor, 1, new ArrayList<>());
	}

	/*
	* eor:前缀异或和数组
	* index:决定index是不是要成为一个隔断，表示在index前面切一刀
	* parts：如果在index前面切一刀，index就放到parts里去
	* */
	public static int process(int[] eor, int index, ArrayList<Integer> parts) {
		int ans = 0;
		if (index == eor.length) {
			/*index来到最后，把这最后一刀放入parts中去*/
			parts.add(eor.length);
			/*计算异或和为0的个数*/
			ans = eorZeroParts(eor, parts);
			/*恢复现场*/
			parts.remove(parts.size() - 1);
		} else {
			/*可能性1：index前不切*/
			int p1 = process(eor, index + 1, parts);
			/*可能性1：index前切一刀，记得恢复现场*/
			parts.add(index);
			int p2 = process(eor, index + 1, parts);
			parts.remove(parts.size() - 1);
			/*两着取最大*/
			ans = Math.max(p1, p2);
		}
		return ans;
	}

	/*
	* eor：用前缀和数组加速
	* 计算parts的这些隔断中，有几个异或和为0
	* */
	public static int eorZeroParts(int[] eor, ArrayList<Integer> parts) {
		int L = 0;
		int ans = 0;
		for (Integer end : parts) {
			if ((eor[end - 1] ^ (L == 0 ? 0 : eor[L - 1])) == 0) {
				ans++;
			}
			L = end;
		}
		return ans;
	}


	/**
	 * 最优解
	 * 时间复杂度O(N)
	 */
	public static int mostXor(int[] arr) {
		if (arr == null || arr.length == 0) {
			return 0;
		}
		int N = arr.length;
		/*都是0表示没有异或和为0的部分*/
		int[] dp = new int[N];
		/*
		* key 某一个前缀异或和
		* value 这个前缀异或和上次出现的位置(最晚！)
		* */
		HashMap<Integer, Integer> map = new HashMap<>();
		/*一个数也没有，异或和一定是0*/
		map.put(0, -1);
		int sum = 0;
		for (int i = 0; i < arr.length; i++) {
			/*来到了i位置，表示数组的长度为i*/
			sum ^= arr[i];
			/*计算出切法2的值*/
			if (map.containsKey(sum)) {
				/*表示0->pre的异或和为sum，0->i的异或和也是sum*/
				int pre = map.get(sum);
				/*pre=-1表示只有0->i的异或和为0，否则pre->i的异或和为0*/
				dp[i] = pre == -1 ? 1 : (dp[pre] + 1);
			}
			if (i > 0) {
				/*两中切法取最大值*/
				dp[i] = Math.max(dp[i - 1], dp[i]);
			}
			/*更新sum的最后一个下标*/
			map.put(sum, i);
		}
		return dp[dp.length - 1];
	}

	// for test
	public static int comparator1(int[] arr) {
		if (arr == null || arr.length == 0) {
			return 0;
		}
		int[] eors = new int[arr.length];
		int eor = 0;
		for (int i = 0; i < arr.length; i++) {
			eor ^= arr[i];
			eors[i] = eor;
		}
		int[] mosts = new int[arr.length];
		mosts[0] = arr[0] == 0 ? 1 : 0;
		for (int i = 1; i < arr.length; i++) {
			mosts[i] = eors[i] == 0 ? 1 : 0;
			for (int j = 0; j < i; j++) {
				if ((eors[i] ^ eors[j]) == 0) {
					mosts[i] = Math.max(mosts[i], mosts[j] + 1);
				}
			}
			mosts[i] = Math.max(mosts[i], mosts[i - 1]);
		}
		return mosts[mosts.length - 1];
	}

	// for test
	public static int[] generateRandomArray(int maxSize, int maxValue) {
		int[] arr = new int[(int) ((maxSize + 1) * Math.random())];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (int) ((maxValue + 1) * Math.random());
		}
		return arr;
	}

	// for test
	public static void printArray(int[] arr) {
		if (arr == null) {
			return;
		}
		for (int i = 0; i < arr.length; i++) {
			System.out.print(arr[i] + " ");
		}
		System.out.println();
	}

	// for test
	public static void main(String[] args) {
		int testTime = 500000;
		int maxSize = 300;
		int maxValue = 100;
		boolean succeed = true;
		for (int i = 0; i < testTime; i++) {
			int[] arr = generateRandomArray(maxSize, maxValue);
			int res = mostXor(arr);
			int comp = comparator1(arr);
			if (res != comp) {
				succeed = false;
				printArray(arr);
				System.out.println(res);
				System.out.println(comp);
				break;
			}
		}
		System.out.println(succeed ? "Nice!" : "Fucking fucked!");
	}

}
