package class34;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Given two integer arrays nums1 and nums2, return an array of their intersection. Each element in the result must appear as many times as it shows in both arrays and you may return the result in any order.
 * intersection:交集
 */
//https://leetcode.com/problems/intersection-of-two-arrays-ii/
public class Problem_0350_IntersectionOfTwoArraysII {

	public static int[] intersect(int[] nums1, int[] nums2) {
		HashMap<Integer, Integer> map1 = new HashMap<>();
		for (int num : nums1) {
			if (!map1.containsKey(num)) {
				map1.put(num, 1);
			} else {
				map1.put(num, map1.get(num) + 1);
			}
		}
		HashMap<Integer, Integer> map2 = new HashMap<>();
		for (int num : nums2) {
			if (!map2.containsKey(num)) {
				map2.put(num, 1);
			} else {
				map2.put(num, map2.get(num) + 1);
			}
		}
		ArrayList<Integer> list = new ArrayList<>();
		for (int key : map1.keySet()) {
			if (map2.containsKey(key)) {
				int n = Math.min(map1.get(key), map2.get(key));
				for (int i = 0; i < n; i++) {
					list.add(key);
				}
			}
		}
		int[] ans = new int[list.size()];
		for (int i = 0; i < ans.length; i++) {
			ans[i] = list.get(i);
		}
		return ans;
	}

}
