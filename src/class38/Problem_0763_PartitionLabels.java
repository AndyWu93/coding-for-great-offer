package class38;

import java.util.ArrayList;
import java.util.List;

/**
 * 字符串 S 由小写字母组成。我们要把这个字符串划分为尽可能多的片段，同一字母最多出现在一个片段中。返回一个表示每个字符串片段的长度的列表。
 * 示例：
 * 输入：S = "ababcbacadefegdehijhklij"
 * 输出：[9,7,8]
 * 解释：
 * 划分结果为 "ababcbaca", "defegde", "hijhklij"。
 * 每个字母最多出现在一个片段中。
 * 像 "ababcbacadefegde", "hijhklij" 的划分是错误的，因为划分的片段数较少。
 * 提示：
 * S的长度在[1, 500]之间。
 * S只包含小写字母 'a' 到 'z' 。
 *
 * 题意：相同的字符只能划到一个部分，问最多能划几个部分，返回每个部分的长度
 * 解题：
 * 	从左往右递推。
 * 	首先用一个map，记录一下arr[i]最右的位置。
 * 	根据map，从左往右，划出部分
 */
//Leetcode题目 : https://leetcode.com/problems/partition-labels/
public class Problem_0763_PartitionLabels {

	public static List<Integer> partitionLabels(String S) {
		char[] str = S.toCharArray();
		/*记录str中每个字符出现的位置*/
		int[] far = new int[26];
		for (int i = 0; i < str.length; i++) {
			/*每个字符的位置，在far中只保留一份，就是出现的最后一次*/
			far[str[i] - 'a'] = i;
		}
		List<Integer> ans = new ArrayList<>();
		/*两个指针*/
		int left = 0;
		int right = far[str[0] - 'a'];
		/*尝试划出的部分，从left到i，right为暂时预计划分的位置*/
		for (int i = 1; i < str.length; i++) {
			if (i > right) {
				/*一旦i越过right，表示right的划分是有效的，收集答案*/
				ans.add(right - left + 1);
				/*开始下个部分的划分*/
				left = i;
			}
			/*i滑过str的每个位置，都要看下right的位置还能不能往右推*/
			right = Math.max(right, far[str[i] - 'a']);
		}
		/*不要忘了最后一个部分的收集*/
		ans.add(right - left + 1);
		return ans;
	}

}
