package class05;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * 字节面试题
 * 给定两个字符串s1和s2，问s2最少删除多少字符可以成为s1的子串？比如 s1 = "abcde"，s2 = "axbc"，s2删掉'x'即可，返回1
 *
 *解题：
 * 该题目有多种解法，需要根据数据量估算复杂度，决定采取哪种方案
 * 方案一：
 * 枚举s2的所有子序列O(2^M)，针对对每个子序列用KMP算法查找在s1中是否存在O(N)，如果存在，用s2长度-s2子序列长度，
 * 按s2子序列长度由大到小遍历所有子序列收集答案取最小值即题解
 * 时间复杂度：O(N*2^M) -> M较小时采用
 *
 * 方案二：
 * 枚举s1的所有子串O(N^2)，对每个子串用，看s2需要多少编辑距离变成该子串，编辑距离中只有删除操作O(N*M)（一张N*M的dp表）
 * 收集所有的编辑距离，取最小值即题解
 * 时间复杂度：O(N^3*M)
 * 方案二优化1：枚举s1所有子串时，首字母是同一个（前缀相同）的所有子串，共用一张DP表，时间复杂度收敛到O(N^2*M)
 * 方案二优化2：这是基于优化1的具体实现，优化一些细节。
 * 整体一张DP表就可以了。
 * 外侧一个大循环，表示开始的列
 * 从第0列开始，求s2转为s1[0->0]的代价，s2转为s1[0->1]的代价...s2转为s1[0->X]的代价，0->X的长度等于s2的长度，后面的都不用填了，s2不可能通过删除转为一个长度大于它的字符
 * 然后从第1列开始，求s2转为s1[1->1]的代价，s2转为s1[1->2]的代价...s2转为s1[1->Y]的代价，...
 * ...
 * 优化后复杂度O(N^2*M) -> M较大时采用
 */
public class Code04_DeleteMinCost {

	// 题目：
	// 给定两个字符串s1和s2，问s2最少删除多少字符可以成为s1的子串？
	// 比如 s1 = "abcde"，s2 = "axbc"
	// 返回 1

	/**
	 * 解法一: O(N*2^M)
	 * N: KMP
	 * 2^M: 枚举子序列
	 */
	// 求出str2所有的子序列，然后按照长度排序，长度大的排在前面。
	// 然后考察哪个子序列字符串和s1的某个子串相等(KMP)，答案就出来了。
	// 分析：
	// 因为题目原本的样本数据中，有特别说明s2的长度很小。所以这么做也没有太大问题，也几乎不会超时。
	// 但是如果某一次考试给定的s2长度远大于s1，这么做就不合适了。
	public static int minCost1(String s1, String s2) {
		List<String> s2Subs = new ArrayList<>();
		process(s2.toCharArray(), 0, "", s2Subs);
		/*子序列按长度由大到小排列*/
		s2Subs.sort(new LenComp());
		for (String str : s2Subs) {
			if (s1.indexOf(str) != -1) { // indexOf底层和KMP算法代价几乎一样，也可以用KMP代替
				return s2.length() - str.length();
			}
		}
		/*表示s2没有任何有值的子序列是s1的子串，那就把s2删成空串，空串一定是s1的子串*/
		return s2.length();
	}

	//枚举所有的子序列，来到了index位置，index的值要还是不要
	public static void process(char[] str2, int index, String path, List<String> list) {
		if (index == str2.length) {
			list.add(path);
			return;
		}
		process(str2, index + 1, path, list);
		process(str2, index + 1, path + str2[index], list);
	}



	public static class LenComp implements Comparator<String> {

		@Override
		public int compare(String o1, String o2) {
			return o2.length() - o1.length();
		}

	}

	/**
	 * 解法二
	 * O(N^3*M)
	 * N^2:子串
	 * N*M:编辑距离dp
	 */
	// 我的方法，看的时间比较短，希望同学们积极反馈
	// 生成所有s1的子串
	// 然后考察每个子串和s2的编辑距离(假设编辑距离只有删除动作且删除一个字符的代价为1)
	// 如果s1的长度较小，s2长度较大，这个方法比较合适
	public static int minCost2(String s1, String s2) {
		if (s1.length() == 0 || s2.length() == 0) {
			return s2.length();
		}
		//默认系统最大值
		int ans = Integer.MAX_VALUE;
		char[] str2 = s2.toCharArray();
		for (int start = 0; start < s1.length(); start++) {
			for (int end = start + 1; end <= s1.length(); end++) {
				// str1[start....end]
				// substring方法左闭右开，所以end需要+1
				ans = Math.min(ans, distance(str2, s1.substring(start, end).toCharArray()));
			}
		}
		return ans == Integer.MAX_VALUE ? s2.length() : ans;
	}

	// x字符串只通过删除的方式，变到y字符串
	// 返回至少要删几个字符
	// 如果变不成，返回Integer.Max
	public static int distance(char[] x, char[] y) {
		if (x.length < y.length) {
			return Integer.MAX_VALUE;
		}
		int N = x.length;
		int M = y.length;
		int[][] dp = new int[N + 1][M + 1];
		for (int i = 0; i <= N; i++) {
			for (int j = 0; j <= M; j++) {
				dp[i][j] = Integer.MAX_VALUE;
			}
		}
		dp[0][0] = 0;
		// dp[i][j]表示前缀长度
		for (int i = 1; i <= N; i++) {
			dp[i][0] = i;
		}
		for (int xlen = 1; xlen <= N; xlen++) {
			for (int ylen = 1; ylen <= Math.min(M, xlen); ylen++) {
				if (dp[xlen - 1][ylen] != Integer.MAX_VALUE) {
					dp[xlen][ylen] = dp[xlen - 1][ylen] + 1;
				}
				if (x[xlen - 1] == y[ylen - 1] && dp[xlen - 1][ylen - 1] != Integer.MAX_VALUE) {
					dp[xlen][ylen] = Math.min(dp[xlen][ylen], dp[xlen - 1][ylen - 1]);
				}
			}
		}
		return dp[N][M];
	}

	// 这个方法可以不看，上面的distance方法更简单
	// 求str2到s1sub的编辑距离
	// 假设编辑距离只有删除动作且删除一个字符的代价为1
	public static int distance1(char[] str2, char[] s1sub) {
		int row = str2.length;
		int col = s1sub.length;
		int[][] dp = new int[row][col];
		// dp[i][j]的含义：
		// str2[0..i]仅通过删除行为变成s1sub[0..j]的最小代价
		// 可能性一：
		// str2[0..i]变的过程中，不保留最后一个字符(str2[i])，
		// 那么就是通过str2[0..i-1]变成s1sub[0..j]之后，再最后删掉str2[i]即可 -> dp[i][j] = dp[i-1][j] + 1
		// 可能性二：
		// str2[0..i]变的过程中，想保留最后一个字符(str2[i])，然后变成s1sub[0..j]，
		// 这要求str2[i] == s1sub[j]才有这种可能, 然后str2[0..i-1]变成s1sub[0..j-1]即可
		// 也就是str2[i] == s1sub[j] 的条件下，dp[i][j] = dp[i-1][j-1]
		dp[0][0] = str2[0] == s1sub[0] ? 0 : Integer.MAX_VALUE;
		for (int j = 1; j < col; j++) {
			//当s2只有一个字符，s1sub有j+1个字符时，把s2减成s1sub，那是不可能的
			dp[0][j] = Integer.MAX_VALUE;
		}
		for (int i = 1; i < row; i++) {
			//当s1sub只有一个字符，s2有j+1个字符时，把s2减成s1sub，只有两种情况，
			// 只要之前出现了s1sub[0]位置的字符，或者当前s2[i]==s1sub[0]，那删除的代价就是s2的当前长度-1，就是i
			dp[i][0] = (dp[i - 1][0] != Integer.MAX_VALUE || str2[i] == s1sub[0]) ? i : Integer.MAX_VALUE;
		}
		for (int i = 1; i < row; i++) {
			for (int j = 1; j < col; j++) {
				//默认系统最大
				dp[i][j] = Integer.MAX_VALUE;
				if (dp[i - 1][j] != Integer.MAX_VALUE) {
					//s2前面的能够减成s1sub，那就把s2[i]位置减掉就好了
					dp[i][j] = dp[i - 1][j] + 1;
				}
				//s2前面的能够减成s1sub前面的，而且当前位置右相等，那代价就是s2前面的能够减成s1sub前面的
				if (str2[i] == s1sub[j] && dp[i - 1][j - 1] != Integer.MAX_VALUE) {
					dp[i][j] = Math.min(dp[i][j], dp[i - 1][j - 1]);
				}

			}
		}
		return dp[row - 1][col - 1];
	}

	/**
	 * 解法二的优化
	 * 解法二中，真的每次都要生成一张dp表吗？
	 * 其实可以一次性生成一张N*M的dp表，
	 * 从0开始的子串：
	 * 从第0列开始，每计算一次distance，生成一列，下次计算distance的时候，再在后面继续生成一列
	 * 从1开始的子串：
	 * 从第1列开始，每计算一次distance，生成一列，下次计算distance的时候，再在后面继续生成一列
	 * ...
	 *
	 * 复杂度：O(N^2*M)
	 * 填了多少列：O(N^2)
	 * 每列填写过了一遍M
	 *
	 */
	public static int minCost3(String s1, String s2) {
		if (s1.length() == 0 || s2.length() == 0) {
			return s2.length();
		}
		char[] str2 = s2.toCharArray();
		char[] str1 = s1.toCharArray();
		int M = str2.length;
		int N = str1.length;
		int[][] dp = new int[M][N];
		//M就是系统最大值
		int ans = M;
		for (int start = 0; start < N; start++) { // 开始的列数
			//这就是左上角位置，除非两个值相等，否则不能转化完成
			dp[0][start] = str2[0] == str1[start] ? 0 : M;
			for (int row = 1; row < M; row++) {
				//依次填写往下的格子
				dp[row][start] = (str2[row] == str1[start] || dp[row - 1][start] != M) ? row : M;
			}
			ans = Math.min(ans, dp[M - 1][start]);
			// 以上已经把start列，填好
			// 以下要把dp[...][start+1....N-1]的信息填好
			// start...end end - start + 1
			// 这里从有效的第二列开始，往后依次填s2.length个长度（不越界的情况下）
			for (int end = start + 1; end < N && end - start < M; end++) {
				// 0... first-1 行 不用管
				// 从有效的第二列开始，上面有一定的长度不要管，因为短的s2不可能通过删除变为长的s1
				int first = end - start;
				// 这里的第一个值，因为s2，s1长度一样，所以值要么是0，要么就是M
				dp[first][end] = (str2[first] == str1[end] && dp[first - 1][end - 1] == 0) ? 0 : M;
				for (int row = first + 1; row < M; row++) {
					// for循环是依次往下填写
					dp[row][end] = M;
					if (dp[row - 1][end] != M) {
						dp[row][end] = dp[row - 1][end] + 1;
					}
					if (dp[row - 1][end - 1] != M && str2[row] == str1[end]) {
						dp[row][end] = Math.min(dp[row][end], dp[row - 1][end - 1]);
					}
				}
				ans = Math.min(ans, dp[M - 1][end]);
			}
		}
		return ans;
	}

	// 来自学生的做法，时间复杂度O(N * M平方)
	// 复杂度和方法三一样，但是思路截然不同
	public static int minCost4(String s1, String s2) {
		char[] str1 = s1.toCharArray();
		char[] str2 = s2.toCharArray();
		HashMap<Character, ArrayList<Integer>> map1 = new HashMap<>();
		for (int i = 0; i < str1.length; i++) {
			ArrayList<Integer> list = map1.getOrDefault(str1[i], new ArrayList<Integer>());
			list.add(i);
			map1.put(str1[i], list);
		}
		int ans = 0;
		// 假设删除后的str2必以i位置开头
		// 那么查找i位置在str1上一共有几个，并对str1上的每个位置开始遍历
		// 再次遍历str2一次，看存在对应str1中i后续连续子串可容纳的最长长度
		for (int i = 0; i < str2.length; i++) {
			if (map1.containsKey(str2[i])) {
				ArrayList<Integer> keyList = map1.get(str2[i]);
				for (int j = 0; j < keyList.size(); j++) {
					int cur1 = keyList.get(j) + 1;
					int cur2 = i + 1;
					int count = 1;
					for (int k = cur2; k < str2.length && cur1 < str1.length; k++) {
						if (str2[k] == str1[cur1]) {
							cur1++;
							count++;
						}
					}
					ans = Math.max(ans, count);
				}
			}
		}
		return s2.length() - ans;
	}

	public static String generateRandomString(int l, int v) {
		int len = (int) (Math.random() * l);
		char[] str = new char[len];
		for (int i = 0; i < len; i++) {
			str[i] = (char) ('a' + (int) (Math.random() * v));
		}
		return String.valueOf(str);
	}

	public static void main(String[] args) {

		char[] x = { 'a', 'b', 'c', 'd' };
		char[] y = { 'a', 'd' };

		System.out.println(distance(x, y));

		int str1Len = 20;
		int str2Len = 10;
		int v = 5;
		int testTime = 10000;
		boolean pass = true;
		System.out.println("test begin");
		for (int i = 0; i < testTime; i++) {
			String str1 = generateRandomString(str1Len, v);
			String str2 = generateRandomString(str2Len, v);
			int ans1 = minCost1(str1, str2);
			int ans2 = minCost2(str1, str2);
			int ans3 = minCost3(str1, str2);
			int ans4 = minCost4(str1, str2);
			if (ans1 != ans2 || ans3 != ans4 || ans1 != ans3) {
				pass = false;
				System.out.println(str1);
				System.out.println(str2);
				System.out.println(ans1);
				System.out.println(ans2);
				System.out.println(ans3);
				System.out.println(ans4);
				break;
			}
		}
		System.out.println("test pass : " + pass);
	}

}
