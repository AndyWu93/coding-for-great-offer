package class36;

/**
 * 来自美团
 * 给定两个字符串s1和s2
 * 返回在s1中有多少个子串等于s2
 *
 * 解题：
 * 	本题改造一下kmp就可以
 * 	如何改造？
 * 	s1s2每个字符比对，当来到s2来到末尾，收集一下count
 * 	然后，s2上的指针，跳至s2最后一个字符的next数组信息位置，
 * 	从那里开始继续和s1比较，来到s2来到末尾，继续收集一下count
 * 	所以，需要改造的地方：
 * 		1. next数组，s2最后一个字符也需要有信息
 * 		2. 主方法里x,y比较时的逻辑
 */
public class Code03_MatchCount {

	public static int sa(String s1, String s2) {
		if (s1 == null || s2 == null || s1.length() < s2.length()) {
			return 0;
		}
		char[] str1 = s1.toCharArray();
		char[] str2 = s2.toCharArray();
		return count(str1, str2);
	}

	// 改写kmp为这道题需要的功能
	public static int count(char[] str1, char[] str2) {
		int x = 0;
		int y = 0;
		int count = 0;
		int[] next = getNextArray(str2);
		while (x < str1.length) {
			if (str1[x] == str2[y]) {
				x++;
				y++;
				/*需要关注一下y指针，来到末尾位置的时候，y跳至信息位置，继续比较，继续收集count*/
				if (y == str2.length) {
					count++;
					y = next[y];
				}
			} else if (next[y] == -1) {
				x++;
			} else {
				y = next[y];
			}
		}
		return count;
	}

	// next数组多求一位
	// 比如：str2 = aaaa
	// 那么，next = -1,0,1,2,3
	// 最后一个3表示，终止位置之前的字符串最长前缀和最长后缀的匹配长度
	// 也就是next数组补一位
	public static int[] getNextArray(char[] str2) {
		if (str2.length == 1) {
			return new int[] { -1, 0 };
		}
		/*长度多了1位*/
		int[] next = new int[str2.length + 1];
		next[0] = -1;
		next[1] = 0;
		int i = 2;
		int cn = 0;
		while (i < next.length) {
			if (str2[i - 1] == str2[cn]) {
				next[i++] = ++cn;
			} else if (cn > 0) {
				cn = next[cn];
			} else {
				next[i++] = 0;
			}
		}
		return next;
	}

}
