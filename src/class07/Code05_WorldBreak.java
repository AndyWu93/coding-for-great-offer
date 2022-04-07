package class07;

import java.util.HashSet;

/**
 * 假设所有字符都是小写字母. 大字符串是str. arr是去重的单词表, 每个单词都不是空字符串且可以使用任意次.
 * 使用arr中的单词有多少种拼接str的方式. 返回方法数.
 *
 * 思路：本题为动态规划，从左往右的尝试模型
 * 规模：dp[str.length+1]
 * dp[i]含义：从大字符串str的i位置开始一直到结尾的子串，能够被arr单词表中单词分解的方法数
 * dp[n] = 1 (str没有子串了，被分解的方式只有1种，就是什么单词都不用)
 * dp[i]:
 * 枚举从i到n-1的位置作为子串的end位置，
 * 对于每个前缀串都要看一下arr中是不是存在该单词(arr可以做出set)O(N)，如果存在，就将后面剩下来的子串拿过去分解，看分解到最后一个字符有多少种方案O(N^2)
 * 填写dp[i]需要从后往前填写，对于每一个i有：
 * for(int end = i, end<n,end ++){
 * 	   String pre = String.subString(i,end+1);
 *     if(set.contains(pre)){
 *         dp[i] += dp[end+1];
 *     }
 * }
 *
 * 复杂度：1张dp表套了1个for循环O(N^2)，里面套了一个set查询O(N),一共O(N^3)
 *
 * 优化：
 * 将set查询变成O(1),此外枚举end的过程能否提前结束
 * 方式：前缀树
 * 优化后复杂度：建立前缀树O(M) + dp套for循环O(N^2)
 *
 */
public class Code05_WorldBreak {


	public static int ways(String str, String[] arr) {
		HashSet<String> set = new HashSet<>();
		for (String candidate : arr) {
			set.add(candidate);
		}
		return process(str, 0, set);
	}

	// 所有的贴纸，都已经放在了set中
	// str[i....] 能够被set中的贴纸分解的话，返回分解的方法数
	public static int process(String str, int i, HashSet<String> set) {
		if (i == str.length()) { // 没字符串需要分解了！
			return 1;
		}
		//  i....还有字符串需要分解
		int ways = 0;
		// [i ... end] 前缀串 每一个前缀串
		for (int end = i; end < str.length(); end++) {
			String pre = str.substring(i, end + 1);// [)
			if (set.contains(pre)) {
				ways += process(str, end + 1, set);
			}
		}
		return ways;
	}

	public static int ways1(String str, String[] arr) {
		if (str == null || str.length() == 0 || arr == null || arr.length == 0) {
			return 0;
		}
		HashSet<String> map = new HashSet<>();
		for (String s : arr) {
			map.add(s);
		}
		return f(str, map, 0);
	}

	public static int f(String str, HashSet<String> map, int index) {
		if (index == str.length()) {
			return 1;
		}
		int ways = 0;
		for (int end = index; end < str.length(); end++) {
			if (map.contains(str.substring(index, end + 1))) {
				ways += f(str, map, end + 1);
			}
		}
		return ways;
	}

	public static int ways2(String str, String[] arr) {
		if (str == null || str.length() == 0 || arr == null || arr.length == 0) {
			return 0;
		}
		HashSet<String> map = new HashSet<>();
		for (String s : arr) {
			map.add(s);
		}
		int N = str.length();
		int[] dp = new int[N + 1];
		dp[N] = 1;
		for (int i = N - 1; i >= 0; i--) {
			for (int end = i; end < N; end++) {
				if (map.contains(str.substring(i, end + 1))) {
					dp[i] += dp[end + 1];
				}
			}
		}
		return dp[0];
	}

	public static class Node {
		public boolean end;
		public Node[] nexts;

		public Node() {
			end = false;
			nexts = new Node[26];
		}
	}

	/**
	 * 前缀树来实现
	 */
	public static int ways3(String str, String[] arr) {
		if (str == null || str.length() == 0 || arr == null || arr.length == 0) {
			return 0;
		}
		Node root = new Node();
		for (String s : arr) {
			char[] chs = s.toCharArray();
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
		return g(str.toCharArray(), root, 0);
	}

	// str[i...] 被分解的方法数，返回
	public static int g(char[] str, Node root, int i) {
		if (i == str.length) {
			return 1;
		}
		int ways = 0;
		Node cur = root;
		// i...end
		for (int end = i; end < str.length; end++) {
			int path = str[end] - 'a';
			if (cur.nexts[path] == null) {
				/*
				* 没路了，以str[i}开头的str不用试了，字典里没有了,
				* 所以以str[i]开头的所有方法数就都凑齐了，可以返回了
				* */
				break;
			}
			cur = cur.nexts[path];
			if (cur.end) { // i...end
				ways += g(str, root, end + 1);
			}
		}
		return ways;
	}

	/**
	 * 最优解
	 */
	public static int ways4(String s, String[] arr) {
		if (s == null || s.length() == 0 || arr == null || arr.length == 0) {
			return 0;
		}
		/*建立前缀树，该树的节点只需要带有一个boolean的end信息*/
		Node root = new Node();
		for (String str : arr) {
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
			//最后一个节点的end
			node.end = true;
		}

		char[] str = s.toCharArray();
		int N = str.length;
		int[] dp = new int[N + 1];
		/*str没有子串了，被分解的方式只有1种，就是什么单词都不用*/
		dp[N] = 1;
		/*从后往前填写*/
		for (int i = N - 1; i >= 0; i--) {
			Node cur = root;
			/*枚举从i开头的子串*/
			for (int end = i; end < N; end++) {
				int path = str[end] - 'a';
				if (cur.nexts[path] == null) {
					/*如果没路了不需要继续枚举了，前缀树的特点，以当前前缀开头的单词不会再有了*/
					break;
				}
				/*来到这条路后面的节点*/
				cur = cur.nexts[path];
				if (cur.end) {
					/*该路构成了一个word，就看str剩下的部分构成的方案数*/
					dp[i] += dp[end + 1];
				}
			}
		}
		return dp[0];
	}

	// for test
	public static class RandomSample {
		public String str;
		public String[] arr;

		public RandomSample(String s, String[] a) {
			str = s;
			arr = a;
		}
	}

	// for test
	// 随机样本产生器
	public static RandomSample generateRandomSample(char[] candidates, int num, int len, int joint) {
		String[] seeds = randomSeeds(candidates, num, len);
		HashSet<String> set = new HashSet<>();
		for (String str : seeds) {
			set.add(str);
		}
		String[] arr = new String[set.size()];
		int index = 0;
		for (String str : set) {
			arr[index++] = str;
		}
		StringBuilder all = new StringBuilder();
		for (int i = 0; i < joint; i++) {
			all.append(arr[(int) (Math.random() * arr.length)]);
		}
		return new RandomSample(all.toString(), arr);
	}

	// for test
	public static String[] randomSeeds(char[] candidates, int num, int len) {
		String[] arr = new String[(int) (Math.random() * num) + 1];
		for (int i = 0; i < arr.length; i++) {
			char[] str = new char[(int) (Math.random() * len) + 1];
			for (int j = 0; j < str.length; j++) {
				str[j] = candidates[(int) (Math.random() * candidates.length)];
			}
			arr[i] = String.valueOf(str);
		}
		return arr;
	}

	// for test
	public static void main(String[] args) {
		char[] candidates = { 'a', 'b' };
		int num = 20;
		int len = 4;
		int joint = 5;
		int testTimes = 30000;
		boolean testResult = true;
		for (int i = 0; i < testTimes; i++) {
			RandomSample sample = generateRandomSample(candidates, num, len, joint);
			int ans1 = ways1(sample.str, sample.arr);
			int ans2 = ways2(sample.str, sample.arr);
			int ans3 = ways3(sample.str, sample.arr);
			int ans4 = ways4(sample.str, sample.arr);
			if (ans1 != ans2 || ans3 != ans4 || ans2 != ans4) {
				testResult = false;
			}
		}
		System.out.println(testTimes + "次随机测试是否通过：" + testResult);
	}

}
