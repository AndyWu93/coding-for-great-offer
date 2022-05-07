package class36;

/**
 * 来自网易
 * 规定：L[1]对应a，L[2]对应b，L[3]对应c，...，L[25]对应y
 * S1 = a
 * S(i) = S(i-1) + L[i] + reverse(invert(S(i-1)));
 * 解释invert操作：
 * S1 = a
 * S2 = aby
 * 假设invert(S(2)) = 甲乙丙
 * a + 甲 = 26, 那么 甲 = 26 - 1 = 25 -> y
 * b + 乙 = 26, 那么 乙 = 26 - 2 = 24 -> x
 * y + 丙 = 26, 那么 丙 = 26 - 25 = 1 -> a
 * 如上就是每一位的计算方式，所以invert(S2) = yxa
 * 所以S3 = S2 + L[3] + reverse(invert(S2)) = aby + c + axy = abycaxy
 * invert(abycaxy) = yxawyba, 再reverse = abywaxy
 * 所以S4 = abycaxy + d + abywaxy = abycaxydabywaxy
 * 直到S25结束
 * 给定两个参数n和k，返回Sn的第k位是什么字符，n从1开始，k从1开始
 * 比如n=4，k=2，表示S4的第2个字符是什么，返回b字符
 *
 * 题意：s(1)依赖s(i-1),通过一些列变换得到，其中一个变换invert(x)，是求x和谁加起来等于26
 * 解题：
 * 	分析，本题要求s(n)的第k个字符，首先的想法是暴力解，直接递归算出s(n),再拿第k个字符
 * 	分析一下复杂度，
 * 	s(n)的长度可以求出来，因为n最大25，可以计算出s1~s25的长度，放在一张map，说不定以后用得上
 * 	len1=1
 * 	len2=1+1+1=3
 * 	len3=3+1+3=7
 * 	len4=7+1+7=15
 * 	发现s25是好几万,sn的字符全部求出来复杂度太高，而且题目中只要求第k个字符，
 * 	所以就想到了只要追踪s(n)[k]是来自s(n-1)[?]，就能够一路追踪至s1，不用生成字符串了
 * 	追踪的代价最大25次，所以复杂度O(25),即O(1)
 *
 */
public class Code01_ReverseInvertString {

	public static int[] lens = null;

	public static void fillLens() {
		lens = new int[26];
		lens[1] = 1;
		for (int i = 2; i <= 25; i++) {
			lens[i] = (lens[i - 1] << 1) + 1;
		}
	}

	// 求sn中的第k个字符
	// O(n), s <= 25 O(1)
	public static char kth(int n, int k) {
		if (lens == null) {
			fillLens();
		}
		if (n == 1) { // 无视k
			/*追踪至第一个字符了，肯定是a*/
			return 'a';
		}
		/*拿到上一个字符的长度，和K作比较*/
		int half = lens[n - 1];
		if (k <= half) {
			/*说明是s(n-1)的第k个位置拷贝而来的*/
			return kth(n - 1, k);
		} else if (k == half + 1) {
			/*说明是L(n)*/
			return (char) ('a' + n - 1);
		} else {
			/*说明是s(n-1)的倒数第(k-half-1)个位置,即(half-(k-half-1)+1)位置，invert而来的*/
			// sn
			// 我需要右半区，从左往右的第a个
			// 需要找到，s(n-1)从右往左的第a个
			// 当拿到字符之后，invert一下，就可以返回了！
			return invert(kth(n - 1, ((half + 1) << 1) - k));
		}
	}

	public static char invert(char c) {
		return (char) (('a' << 1) + 24 - c);
	}

	// 为了测试
	public static String generateString(int n) {
		String s = "a";
		for (int i = 2; i <= n; i++) {
			s = s + (char) ('a' + i - 1) + reverseInvert(s);
		}
		return s;
	}

	// 为了测试
	public static String reverseInvert(String s) {
		char[] invert = invert(s).toCharArray();
		for (int l = 0, r = invert.length - 1; l < r; l++, r--) {
			char tmp = invert[l];
			invert[l] = invert[r];
			invert[r] = tmp;
		}
		return String.valueOf(invert);
	}

	// 为了测试
	public static String invert(String s) {
		char[] str = s.toCharArray();
		for (int i = 0; i < str.length; i++) {
			str[i] = invert(str[i]);
		}
		return String.valueOf(str);
	}

	// 为了测试
	public static void main(String[] args) {
		int n = 20;
		String str = generateString(n);
		int len = str.length();
		System.out.println("测试开始");
		for (int i = 1; i <= len; i++) {
			if (str.charAt(i - 1) != kth(n, i)) {
				System.out.println(i);
				System.out.println(str.charAt(i - 1));
				System.out.println(kth(n, i));
				System.out.println("出错了！");
				break;
			}
		}
		System.out.println("测试结束");
	}

}
