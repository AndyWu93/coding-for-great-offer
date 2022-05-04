package class24;

import java.util.HashMap;
import java.util.HashSet;

/**
 * 给定一个正数数组arr，长度一定大于6（>=7），一定要选3个数字做分割点，从而分出4个部分，并且每部分都有数
 * 分割点的数字直接删除，不属于任何4个部分中的任何一个。返回有没有可能分出的4个部分累加和一样大
 * 如：{3,2,3,7,4,4,3,1,1,6,7,1,5,2}。可以分成{3,2,3}、{4,4}、{1,1,6}、{1,5,2}。分割点是不算的！
 *
 * 题意： 将正数数组arr，切成4个相等的部分，切的位置不算。切的位置一定是arr[某个位置]
 * 思路：
 * 遍历arr枚举第一刀的位置，第一刀只能从1位置开始，到n-6位置位置，因为要给前面留一个数，后面要留2刀和3个数
 * 第一刀来到了i位置
 * 1. 需要知道该位置的leftSum，可以用一个变量，在遍历arr时不断累积leftSum
 * 2. 需要知道有没有第二刀以及其位置，按照题意，第二刀的leftSum=第一刀的leftSum * 2 + arr[i];可以准备一个预处理map将每个位置的leftSum和index
 * 放入map，方便快速查找
 * 3. 如果3刀都找到了，切最后一块数的和也一样，返回true
 */
public class Code01_Split4Parts {

	public static boolean canSplits1(int[] arr) {
		if (arr == null || arr.length < 7) {
			return false;
		}
		HashSet<String> set = new HashSet<String>();
		int sum = 0;
		for (int i = 0; i < arr.length; i++) {
			sum += arr[i];
		}
		int leftSum = arr[0];
		for (int i = 1; i < arr.length - 1; i++) {
			set.add(String.valueOf(leftSum) + "_" + String.valueOf(sum - leftSum - arr[i]));
			leftSum += arr[i];
		}
		int l = 1;
		int lsum = arr[0];
		int r = arr.length - 2;
		int rsum = arr[arr.length - 1];
		while (l < r - 3) {
			if (lsum == rsum) {
				String lkey = String.valueOf(lsum * 2 + arr[l]);
				String rkey = String.valueOf(rsum * 2 + arr[r]);
				if (set.contains(lkey + "_" + rkey)) {
					return true;
				}
				lsum += arr[l++];
			} else if (lsum < rsum) {
				lsum += arr[l++];
			} else {
				rsum += arr[r--];
			}
		}
		return false;
	}

	/**
	 * 最优解
	 */
	public static boolean canSplits2(int[] arr) {
		if (arr == null || arr.length < 7) {
			return false;
		}
		/*key 某一个累加和， 累加和为key的下一个位置*/
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		int sum = arr[0];
		for (int i = 1; i < arr.length; i++) {
			/*此时累加和是arr[0],但加入的是1*/
			map.put(sum, i);
			sum += arr[i];
		}
		int lsum = arr[0]; // 第一刀左侧的累加和
		for (int s1 = 1; s1 < arr.length - 5; s1++) { // s1是第一刀的位置
			int checkSum = lsum * 2 + arr[s1]; // 100 x 100   100*2 + x
			if (map.containsKey(checkSum)) {
				int s2 = map.get(checkSum); // j -> y
				checkSum += lsum + arr[s2];
				if (map.containsKey(checkSum)) { // 100 * 3 + x + y
					int s3 = map.get(checkSum); // k -> z
					if (checkSum + arr[s3] + lsum == sum) {
						return true;
					}
				}
			}
			/*更新lsum，接下里枚举下个第一刀*/
			lsum += arr[s1];
		}
		return false;
	}

	public static int[] generateRondomArray() {
		int[] res = new int[(int) (Math.random() * 10) + 7];
		for (int i = 0; i < res.length; i++) {
			res[i] = (int) (Math.random() * 10) + 1;
		}
		return res;
	}

	public static void main(String[] args) {
		int testTime = 3000000;
		for (int i = 0; i < testTime; i++) {
			int[] arr = generateRondomArray();
			if (canSplits1(arr) ^ canSplits2(arr)) {
				System.out.println("Error");
			}
		}
	}
}
