package class31;

import java.util.List;

/**
 * Given a string s and a dictionary of strings wordDict, return true if s can be segmented into a space-separated sequence of one or more dictionary words.
 *
 * Note that the same word in the dictionary may be reused multiple times in the segmentation.
 * 题意：判断word能否通过分词在dict中拼接起来
 * 解题：
 * 	设计递归函数f(i):表示word从i位置往后能不能被dict分解。
 * 	然后枚举前缀，如果前缀存在于dict中调用f(i),
 * 	只要有一种枚举的情况返回true就可以停了
 * 优化
 * 	枚举前缀树，并查询是否在dict中，这个可以使用前缀树加速
 *
 */
// lintcode也有测试，数据量比leetcode大很多 : https://www.lintcode.com/problem/107/
public class Problem_0139_WordBreak {

	public static class Node {
		public boolean end;
		public Node[] nexts;

		public Node() {
			end = false;
			nexts = new Node[26];
		}
	}

	public static boolean wordBreak1(String s, List<String> wordDict) {
		Node root = new Node();
		/*建立前缀树*/
		for (String str : wordDict) {
			char[] chs = str.toCharArray();
			Node node = root;
			int index = 0;
			for (int i = 0; i < chs.length; i++) {
				index = chs[i] - 'a';
				if (node.nexts[index] == null) {
					node.nexts[index] = new Node();
				}
				node = node.nexts[index];
			}
			node.end = true;
		}
		char[] str = s.toCharArray();
		int N = str.length;
		boolean[] dp = new boolean[N + 1];
		dp[N] = true; // dp[i]  word[i.....] 能不能被分解
		// dp[N] word[N...]  -> ""  能不能够被分解 
		// dp[i] ... dp[i+1....]
		/*dp[i]依赖dp[i+1,i+2...],所以从后往前推*/
		for (int i = N - 1; i >= 0; i--) {
			// i
			// word[i....] 能不能够被分解
			// i..i    i+1....
			// i..i+1  i+2...
			/*求dp[i]的过程：即word从i位置往后能不能被dict分解*/
			Node cur = root;
			/*end：从i位置往后开始枚举前缀，end是前缀的end*/
			for (int end = i; end < N; end++) {
				/*前缀树往下跳一格*/
				cur = cur.nexts[str[end] - 'a'];
				if (cur == null) {
					/*没路了，表示dict中没有从str[i..end]再往后的前缀了，不用枚举了*/
					break;
				}
				// 有路！
				if (cur.end) {
					// i...end 真的是一个有效的前缀串  end+1....  能不能被分解
					/*
					* 这里，只要后面dp[end+1]返回true了，dp[i]的值就是true了，后面都不用试了，
					* 表示当前的前缀，以及后面的字符能够被dict分解
					* */
					dp[i] |= dp[end + 1];
				}
				if (dp[i]) {
					/*如上所述*/
					break;
				}
			}
		}
		return dp[0];
	}

	/**
	 * 进阶：
	 * 	要返回s能够被dict分解的方法数
	 * 设计：
	 * 	f(i)/dp[i]:word从i位置往后能够被dict分解的方法数
	 * 	求法：枚举word前缀，调用f(i),枚举过程中每次f(i)的返回结果相加
	 */
	public static int wordBreak2(String s, List<String> wordDict) {
		Node root = new Node();
		for (String str : wordDict) {
			char[] chs = str.toCharArray();
			Node node = root;
			int index = 0;
			for (int i = 0; i < chs.length; i++) {
				index = chs[i] - 'a';
				if (node.nexts[index] == null) {
					node.nexts[index] = new Node();
				}
				node = node.nexts[index];
			}
			node.end = true;
		}
		char[] str = s.toCharArray();
		int N = str.length;
		int[] dp = new int[N + 1];
		dp[N] = 1;
		for (int i = N - 1; i >= 0; i--) {
			Node cur = root;
			for (int end = i; end < N; end++) {
				cur = cur.nexts[str[end] - 'a'];
				if (cur == null) {
					break;
				}
				if (cur.end) {
					/*相加*/
					dp[i] += dp[end + 1];
				}
			}
		}
		return dp[0];
	}

}
