package class12;

import java.util.Arrays;

/**
 * 给定长度为m的字符串aim，以及一个长度为n的字符串str，问能否在str中找到一个长度为m的连续子串，
 * 使得这个子串刚好由aim的m个字符组成，顺序无所谓，返回任意满足条件的一个子串的起始位置，未找到返回-1
 *
 * @see class24.Code05_MinWindowLength 这里求的是最短子串，本题要求长度相等，稍微更改一下流程，
 * 									   窗口达到m长度后，移动时保持窗口大小即可
 */
// 本题测试链接 : https://leetcode.com/problems/permutation-in-string/
public class Code01_ContainAllCharExactly {

	public static int containExactly1(String s, String a) {
		if (s == null || a == null || s.length() < a.length()) {
			return -1;
		}
		char[] aim = a.toCharArray();
		Arrays.sort(aim);
		String aimSort = String.valueOf(aim);
		for (int L = 0; L < s.length(); L++) {
			for (int R = L; R < s.length(); R++) {
				char[] cur = s.substring(L, R + 1).toCharArray();
				Arrays.sort(cur);
				String curSort = String.valueOf(cur);
				if (curSort.equals(aimSort)) {
					return L;
				}
			}
		}
		return -1;
	}

	public static int containExactly2(String s, String a) {
		if (s == null || a == null || s.length() < a.length()) {
			return -1;
		}
		char[] str = s.toCharArray();
		char[] aim = a.toCharArray();
		for (int L = 0; L <= str.length - aim.length; L++) {
			if (isCountEqual(str, L, aim)) {
				return L;
			}
		}
		return -1;
	}

	public static boolean isCountEqual(char[] str, int L, char[] aim) {
		int[] count = new int[256];
		for (int i = 0; i < aim.length; i++) {
			count[aim[i]]++;
		}
		for (int i = 0; i < aim.length; i++) {
			if (count[str[L + i]]-- == 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 窗口，最优解
	 */
	public static int containExactly3(String s1, String s2) {
		if (s1 == null || s2 == null || s1.length() < s2.length()) {
			return -1;
		}
		char[] str2 = s2.toCharArray();
		int M = str2.length;
		int[] count = new int[256];
		for (int i = 0; i < M; i++) {
			count[str2[i]]++;
		}
		int all = M;
		char[] str1 = s1.toCharArray();
		int R = 0;
		// 0~M-1
		/*先要哦形成一个M长度的窗口*/
		for (; R < M; R++) { // 最早的M个字符，让其窗口初步形成
			if (count[str1[R]]-- > 0) {
				all--;
			}
		}
		// 窗口初步形成了，并没有判断有效无效，决定下一个位置一上来判断
		// 接下来的过程，窗口右进一个，左吐一个
		for (; R < str1.length; R++) {
			if (all == 0) {
				return R - M;
			}
			/*注意，这里++是在逻辑判断之后再执行的*/
			if (count[str1[R]]-- > 0) {
				all--;
			}
			if (count[str1[R - M]]++ >= 0) {
				all++;
			}
		}
		/*最后一个窗口还要判断一下*/
		return all == 0 ? R - M : -1;
	}

	// for test
	public static String getRandomString(int possibilities, int maxSize) {
		char[] ans = new char[(int) (Math.random() * maxSize) + 1];
		for (int i = 0; i < ans.length; i++) {
			ans[i] = (char) ((int) (Math.random() * possibilities) + 'a');
		}
		return String.valueOf(ans);
	}

	public static void main(String[] args) {
		int possibilities = 5;
		int strMaxSize = 20;
		int aimMaxSize = 10;
		int testTimes = 500000;
		System.out.println("test begin, test time : " + testTimes);
		for (int i = 0; i < testTimes; i++) {
			String str = getRandomString(possibilities, strMaxSize);
			String aim = getRandomString(possibilities, aimMaxSize);
			int ans1 = containExactly1(str, aim);
			int ans2 = containExactly2(str, aim);
			int ans3 = containExactly3(str, aim);
			if (ans1 != ans2 || ans2 != ans3) {
				System.out.println("Oops!");
				System.out.println(str);
				System.out.println(aim);
				System.out.println(ans1);
				System.out.println(ans2);
				System.out.println(ans3);
				break;
			}
		}
		System.out.println("test finish");

	}

}
