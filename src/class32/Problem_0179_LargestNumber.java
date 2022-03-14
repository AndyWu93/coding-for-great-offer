package class32;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Given a list of non-negative integers nums, arrange them such that they form the largest number and return it.
 *
 * Since the result may be very large, so you need to return a string instead of an integer.
 *
 * Example 1:
 * Input: nums = [10,2]
 * Output: "210"
 *
 * Example 2:
 * Input: nums = [3,30,34,5,9]
 * Output: "9534330"
 *
 * 参考下面这个题目的解法
 * @see Code05_LowestLexicography
 */
public class Problem_0179_LargestNumber {

	public static class MyComparator implements Comparator<String> {

		@Override
		public int compare(String o1, String o2) {
			return (o2 + o1).compareTo(o1 + o2);
		}

	}

	public String largestNumber(int[] nums) {
		String[] strs = new String[nums.length];
		for (int i = 0; i < nums.length; i++) {
			strs[i] = String.valueOf(nums[i]);
		}
		Arrays.sort(strs, new MyComparator());
		StringBuilder builder = new StringBuilder();
		for (String str : strs) {
			builder.append(str);
		}
		String ans = builder.toString();
		char[] str = ans.toCharArray();
		int index = -1;
		for (int i = 0; i < str.length; i++) {
			if (str[i] != '0') {
				index = i;
				break;
			}
		}
		return index == -1 ? "0" : ans.substring(index);
	}

}
