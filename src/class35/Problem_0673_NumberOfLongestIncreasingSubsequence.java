package class35;

import class09.Code03_LIS;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Given an integer array nums, return the number of longest increasing subsequences.
 *
 * Notice that the sequence has to be strictly increasing.
 * 题意：求arr中最长递增子序列的数量
 * 解题：
 * 	之前一道题是求最长递增子序列长度，这里要求出数量
 * 	这里依然用到了ends数组的概念，这里需要按照题意稍作修改
 * 		之前ends[i]:长度i+1的最长递增子序列的最小结尾
 * 		现在ends[i]:长度i+1的最长递增子序列中，[(结尾x：x为结尾的最长递增子序列个数)]
 * @see Code03_LIS 最长递增子序列
 */
public class Problem_0673_NumberOfLongestIncreasingSubsequence {

	// 好理解的方法，时间复杂度O(N^2)
	public static int findNumberOfLIS1(int[] nums) {
		if (nums == null || nums.length == 0) {
			return 0;
		}
		int n = nums.length;
		int[] lens = new int[n];
		int[] cnts = new int[n];
		lens[0] = 1;
		cnts[0] = 1;
		int maxLen = 1;
		int allCnt = 1;
		for (int i = 1; i < n; i++) {
			int preLen = 0;
			int preCnt = 1;
			for (int j = 0; j < i; j++) {
				if (nums[j] >= nums[i] || preLen > lens[j]) {
					continue;
				}
				if (preLen < lens[j]) {
					preLen = lens[j];
					preCnt = cnts[j];
				} else {
					preCnt += cnts[j];
				}
			}
			lens[i] = preLen + 1;
			cnts[i] = preCnt;
			if (maxLen < lens[i]) {
				maxLen = lens[i];
				allCnt = cnts[i];
			} else if (maxLen == lens[i]) {
				allCnt += cnts[i];
			}
		}
		return allCnt;
	}

	// 优化后的最优解，时间复杂度O(N*logN)
	public static int findNumberOfLIS2(int[] nums) {
		if (nums == null || nums.length == 0) {
			return 0;
		}
		/*
		* 原本dp含义：
		* TreeMap<Integer, Integer> m = dp[i]: 长度为i+1的最长递增子序列中：
		* 	key：结尾x
		*   value：结尾==x的最长递增子序列个数
		* 	第一个key：长度为i+1的最长递增子序列中的最小结尾
		*
		* 升级后dp含义：仅改了value
		*   value：结尾>=x的最长递增子序列个数
		*
		* len：最小结尾刚刚大于等于arr[i]的最长子序列长度-1，即arr[i]将要成为长度为len+1的最长递增子序列的最小结尾
		* cnt：用来计算出value
		* */
		ArrayList<TreeMap<Integer, Integer>> dp = new ArrayList<>();
		int len = 0;
		int cnt = 0;
		for (int num : nums) {
			/*
			* len：最小结尾刚刚大于等于arr[i]的最长子序列长度-1，即arr[i]将要成为长度为len+1的最长递增子序列的最小结尾
			* */
			len = search(dp, num);
			// cnt : 最终要去加底下的记录，才是应该填入的value
			if (len == 0) {
				/*
				* 最长递增子序列长度为1，表示前面没有了，因为不会出现最长递增子序列长度为0，
				* 那num自己成为一个递增子序列
				* */
				cnt = 1;
			} else {
				/*
				* 否则，找到上一个长度的递增子序列中，结尾小于num的递增子序列个数，都加起来
				* 这里因为value是累加和，所以用两个数相减得到。
				* 为什么是cellingKey？cellingKey是要被减掉的部分
				* */
				TreeMap<Integer, Integer> p = dp.get(len - 1);
				cnt = p.firstEntry().getValue() - (p.ceilingKey(num) != null ? p.get(p.ceilingKey(num)) : 0);
			}
			if (len == dp.size()) {
				/*达到了一个新的长度*/
				dp.add(new TreeMap<Integer, Integer>());
				dp.get(len).put(num, cnt);
			} else {
				/*原有长度：num成为了新的最小结尾，所以加上原最小结尾的值，在加上上一个长度统计的cnt*/
				dp.get(len).put(num, dp.get(len).firstEntry().getValue() + cnt);
			}
		}
		return dp.get(dp.size() - 1).firstEntry().getValue();
	}

	// 二分查找，返回>=num最左的位置
	public static int search(ArrayList<TreeMap<Integer, Integer>> dp, int num) {
		int l = 0, r = dp.size() - 1, m = 0;
		int ans = dp.size();
		while (l <= r) {
			m = (l + r) / 2;
			if (dp.get(m).firstKey() >= num) {
				ans = m;
				r = m - 1;
			} else {
				l = m + 1;
			}
		}
		return ans;
	}

}
