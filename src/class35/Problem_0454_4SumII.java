package class35;

import java.util.HashMap;

/**
 * Given four integer arrays nums1, nums2, nums3, and nums4 all of length n, return the number of tuples (i, j, k, l) such that:
 *
 * 0 <= i, j, k, l < n
 * nums1[i] + nums2[j] + nums3[k] + nums4[l] == 0
 * 题意：四个数组中各取一个数，加起来是0为一个组合，返回不同的组合数
 * 解题：
 * 	一个map
 * 	枚举A,B集合，各取一个数，组成一个组合sum1，map中记录sum1此时拥有的组合数
 * 	枚举C,D集合，各取一个数，组成一个组合sum2，map中找打-sum2拥有的组合数，累积起来
 */
public class Problem_0454_4SumII {

	public static int fourSumCount(int[] A, int[] B, int[] C, int[] D) {
		/*
		* key：A,B集合各取一个数，加起来的sum
		* value：A,B集合各取一个数，加起来等于key的组合数
		* */
		HashMap<Integer, Integer> map = new HashMap<>();
		int sum = 0;
		for (int i = 0; i < A.length; i++) {
			for (int j = 0; j < B.length; j++) {
				sum = A[i] + B[j];
				if (!map.containsKey(sum)) {
					map.put(sum, 1);
				} else {
					map.put(sum, map.get(sum) + 1);
				}
			}
		}
		int ans = 0;
		for (int i = 0; i < C.length; i++) {
			for (int j = 0; j < D.length; j++) {
				/*C,D各取一个数加起来等于sum'，在map中找到-sum'的组合数，累加起来*/
				sum = C[i] + D[j];
				if (map.containsKey(-sum)) {
					ans += map.get(-sum);
				}
			}
		}
		return ans;
	}

}
