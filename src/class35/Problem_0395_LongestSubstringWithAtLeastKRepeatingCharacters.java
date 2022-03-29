package class35;

/**
 * Given a string s and an integer k, return the length of the longest substring of s such that the frequency of each character in this substring is greater than or equal to k.
 * 题意：找出字符串s中的一个最长的子串，其中每种字符数量需要大于k，返回该子串长度
 * 解题：
 * 	本题首先看下窗口能不能解，但是窗口的大小和每种字符数量没有直接的单调性，
 * 	如何让他们产生单调性？固定字符种类，即可让窗口大小和字符数量产生单调性，从而找到最长的窗口
 * 	枚举字符种类数量：1种~26种，收集每次枚举后得到的最大窗口，收集最大窗口
 */
public class Problem_0395_LongestSubstringWithAtLeastKRepeatingCharacters {

	public static int longestSubstring1(String s, int k) {
		char[] str = s.toCharArray();
		int N = str.length;
		int max = 0;
		for (int i = 0; i < N; i++) {
			int[] count = new int[256];
			int collect = 0;
			int satisfy = 0;
			for (int j = i; j < N; j++) {
				if (count[str[j]] == 0) {
					collect++;
				}
				if (count[str[j]] == k - 1) {
					satisfy++;
				}
				count[str[j]]++;
				if (collect == satisfy) {
					max = Math.max(max, j - i + 1);
				}
			}
		}
		return max;
	}

	/**
	 * 最优解
	 */
	public static int longestSubstring2(String s, int k) {
		char[] str = s.toCharArray();
		int N = str.length;
		int max = 0;
		for (int require = 1; require <= 26; require++) {
			// 3种
			// a~z 出现次数
			int[] count = new int[26];
			// 目前窗口内收集了几种字符了
			int collect = 0;
			// 目前窗口内出现次数>=k次的字符，满足了几种
			int satisfy = 0;
			// 窗口右边界
			int R = -1;
			for (int L = 0; L < N; L++) { // L要尝试每一个窗口的最左位置
				// [L..R] R+1
				/*
				* 还有字符，且
				* 目前拥有的字符种类不到require个，
				* 或者到了require个，但是下个字符进来不会增加字符种类
				* count[str[R + 1] - 'a'] == 0：R+1位置的词频为0，表示这是一个新的字符种类
				* */
				while (R + 1 < N && !(collect == require && count[str[R + 1] - 'a'] == 0)) {
					/*窗口右移，注意此时R的值变化了*/
					R++;
					if (count[str[R] - 'a'] == 0) {
						/*一个新的字符种类，种类数++*/
						collect++;
					}
					if (count[str[R] - 'a'] == k - 1) {
						/*一个原有字符种类即将达标，达标种类数++*/
						satisfy++;
					}
					/*R位置的数进来了，词频统计*/
					count[str[R] - 'a']++;
				}
				// [L...R]
				if (satisfy == require) {
					/*当前枚举的种类数都达标了，收集require个种类的字符都达标，且此时L位置开头下，的最大长度*/
					max = Math.max(max, R - L + 1);
				}
				// L++
				/*
				* L即将右移：计算一个新的窗口开始位置下，收集到的最大长度
				* L位置的数的词频
				* 此时收集和满足的种类都要看下是否发生变化
				* */
				if (count[str[L] - 'a'] == 1) {
					collect--;
				}
				if (count[str[L] - 'a'] == k) {
					satisfy--;
				}
				count[str[L] - 'a']--;
			}
		}
		return max;
	}

	// 会超时，但是思路的确是正确的
	public static int longestSubstring3(String s, int k) {
		return process(s.toCharArray(), 0, s.length() - 1, k);
	}

	public static int process(char[] str, int L, int R, int k) {
		if (L > R) {
			return 0;
		}
		int[] counts = new int[26];
		for (int i = L; i <= R; i++) {
			counts[str[i] - 'a']++;
		}
		char few = 0;
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < 26; i++) {
			if (counts[i] != 0 && min > counts[i]) {
				few = (char) (i + 'a');
				min = counts[i];
			}
		}
		if (min >= k) {
			return R - L + 1;
		}
		int pre = 0;
		int max = Integer.MIN_VALUE;
		for (int i = L; i <= R; i++) {
			if (str[i] == few) {
				max = Math.max(max, process(str, pre, i - 1, k));
				pre = i + 1;
			}
		}
		if (pre != R + 1) {
			max = Math.max(max, process(str, pre, R, k));
		}
		return max;
	}

}
