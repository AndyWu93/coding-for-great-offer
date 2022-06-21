package class09;

/**
 * 最长递增子序列问题（重点考核）
 * 如[3,1,4,2,3] -> "123"
 * 解题：
 * 	借助ends数组
 *
 * 进阶：给定一组套娃的size数组,（长，高），求这些套娃中，随意选择，最多可以套几层？
 * 解题：
 * 	这道题可以转化为一个递增子序列问题，方法如下：
 * 		将套娃排序，长：由小大大，若长度相等：高度由大到小
 * 		排好序以后，把所有的高依次拿出来，求最长递增子序列。
 * 	为什么？
 * 		此时，长度一样的，高度不能形成递增子序列，因为 "长度相等：高度由大到小"
 */
// 本题测试链接 : https://leetcode.com/problems/longest-increasing-subsequence
public class Code03_LIS {

	/**
	 * 解法一：
	 * 遍历arr，来到i位置的时候，计算以arr[i]结尾的最长递增子序列，计入结果数组dp[i]中
	 * @param arr
	 * @return
	 */
	public static int[] lis1(int[] arr) {
		if (arr == null || arr.length == 0) {
			return null;
		}
		int[] dp = getdp1(arr);
		return generateLIS(arr, dp);
	}

	//复杂度：O(N^2)
	public static int[] getdp1(int[] arr) {
		/*dp[i]:必须以arr[i]结尾的最长递增子序列长度*/
		int[] dp = new int[arr.length];
		for (int i = 0; i < arr.length; i++) {
			//以arr[i]结尾的最长递增子序列，至少为1
			dp[i] = 1;
			//遍历已经看过数字
			for (int j = 0; j < i; j++) {
				if (arr[i] > arr[j]) {
					//如果当前位置的数，比前面一个位置的数字arr[j]大，那该位置的最长子序列最少为j位置的最长子序列+1，
					//但也有可能i位置之前已经计算过一次特别长的子序列，比较取max
					dp[i] = Math.max(dp[i], dp[j] + 1);
				}
			}
		}
		return dp;
	}

	/*
	* 严格来说，这里只返回了其中一个最长递增子序列
	* */
	public static int[] generateLIS(int[] arr, int[] dp) {
		int len = 0;
		int index = 0;
		for (int i = 0; i < dp.length; i++) {
			if (dp[i] > len) {
				/*
				* 事实上，dp中值等于len的可能有多个，即表明arr中有多个长度为了len的递增子序列，
				* 这里只返回了其中一种
				* */
				len = dp[i];
				index = i;
			}
		}
		int[] lis = new int[len];
		lis[--len] = arr[index];
		for (int i = index; i >= 0; i--) {
			if (arr[i] < arr[index] && dp[i] == dp[index] - 1) {
				lis[--len] = arr[i];
				index = i;
			}
		}
		return lis;
	}

	/**
	 * 解法二
	 * dp[i]:以arr[i]结尾的最长递增子序列长度
	 * dp[i]的值，通过arr[i]在ends数组中的应该所处的位置得到
	 *
	 * 预处理数组 ends[] 含义：
	 * ends[i]: 目前所有长度为i+1的递增子序列中，最小结尾；ends数组一定是有序的递增数组
	 * ends[i]用途：
	 * 	到目前为止，所有长度为i+1的递增子序列中，最小的结尾是ends[i],
	 * 	所以当遇到一个数arr[k]，
	 * 		比ends[i]小，但是比end[i-1]大：那么arr[k]就成了长度为i+1的递增子序列中的最小的结尾，同时得到了dp[k]的值，就是i+1
	 * 		与ends[i]相等：               直接可以得到了dp[k]的值，就是i+1
	 * 		ends中找不到：                说明arr[k]很大，赋值给ends[size]，目前为止收集到的最长递增子序列长度增加1个长度，且目前的最小结尾是arr[k]
	 *
	 * ends怎么求？
	 * 	遍历arr，来到i位置，到ends数组中找比arr[i]大或相等最左的位置，比如j，
	 * 	找到了更新ends[j]=arr[i],没找到，就在ends[]增加一个值为arr[i]
	 * 	dp[i] = j+1
	 *
	 * ends就能够得到最长递增子序列的长度了，就是end的size，为什么还要dp？
	 * 	dp可以得到这个最长递增子序列结尾的index，从而向前推出这个最长递增子序列来
	 * @param arr
	 * @return
	 */
	public static int[] lis2(int[] arr) {
		if (arr == null || arr.length == 0) {
			return null;
		}
		int[] dp = getdp2(arr);
		return generateLIS(arr, dp);
	}

	public static int[] getdp2(int[] arr) {
		int[] dp = new int[arr.length];
		int[] ends = new int[arr.length];
		//最长子递增序列长度为1，首次的最小值就是arr的第一个数
		ends[0] = arr[0];
		dp[0] = 1;
		//right记录ends[]扩充到了那个位置
		int right = 0; // 0....right   right往右无效
		int l = 0;
		int r = 0;
		int m = 0;
		for (int i = 1; i < arr.length; i++) {
			l = 0;
			r = right;
			//ends[]中查找大于arr[i]最左的数
			while (l <= r) {
				m = (l + r) / 2;
				if (arr[i] > ends[m]) {
					l = m + 1;
				} else {
					r = m - 1;
				}
			}
			/*
			 * 经过上面的二分，
			 * 没找到的话，l=right+1，扩充一下right
			 * 	为什么说没找到的话，l=right+1呢？
			 * 	因为没找到了话就会出现l=r=m，而arr[i]一直是大于ends数组任意一个数的，所以最后跳出while前，l=m=r=right,跳出后，l=right+1
			 * 找到了的话，l一定小于right，这里的right一直保持ends数组的最大index
			 * */
			right = Math.max(right, l);
			//更新ends[]
			ends[l] = arr[i];
			//最长子递增序列长度为l+1的最小结尾为arr[i],那以arr[i]结尾的最长递增子序列dp[i]=l+1
			dp[i] = l + 1;
		}
		return dp;
	}

	// for test
	public static void printArray(int[] arr) {
		for (int i = 0; i != arr.length; i++) {
			System.out.print(arr[i] + " ");
		}
		System.out.println();
	}

	public static void main(String[] args) {
		int[] arr = { 2, 1, 5, 3, 6, 4, 8, 9, 7 };
		printArray(arr);
		printArray(lis1(arr));
		printArray(lis2(arr));

	}
}