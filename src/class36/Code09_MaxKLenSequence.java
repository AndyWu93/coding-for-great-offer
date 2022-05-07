package class36;

import java.util.TreeSet;

/**
 * 来自腾讯
 * 给定一个字符串str，和一个正数k
 * 返回长度为k的所有子序列中，字典序最大的子序列
 *
 * 解题：
 * 	生成所有的子序列肯定不行。
 * 	所以一定要O(N)判断当前字符是否去留
 * 	字典序最大最大，就是要求前缀尽量大。本题规定子序列长度必须为k
 * 	所以需要保证k个字符里前缀尽量大，什么结构可以做呢？单调栈
 * 	单调栈能够保证压在栈底的是到目前为止遇到的最大的，倒数第二大...
 *  所以只要保证后面的字符还能够凑足k个，就可以用单调栈筛选出排名前几的字符串
 */
public class Code09_MaxKLenSequence {

	public static String maxString(String s, int k) {
		if (k <= 0 || s.length() < k) {
			return "";
		}
		char[] str = s.toCharArray();
		int n = str.length;
		char[] stack = new char[n];
		int size = 0;
		for (int i = 0; i < n; i++) {
			/*单调栈，需要标准i后面的字符加上栈里存着的大于k个，才可以弹出*/
			while (size > 0 && stack[size - 1] < str[i] && size + n - i > k) {
				size--;
			}
			if (size + n - i == k) {
				/*栈里，和i后面的字符加起来刚好k个，不用筛选了，都拿进来，直接返回*/
				return String.valueOf(stack, 0, size) + s.substring(i);
			}
			/*遇到相等的char可以压在上面*/
			stack[size++] = str[i];
		}
		/*栈里面排名前几的数量很多，大于k个，选前k个*/
		return String.valueOf(stack, 0, k);
	}

	// 为了测试
	public static String test(String str, int k) {
		if (k <= 0 || str.length() < k) {
			return "";
		}
		TreeSet<String> ans = new TreeSet<>();
		process(0, 0, str.toCharArray(), new char[k], ans);
		return ans.last();
	}

	// 为了测试
	public static void process(int si, int pi, char[] str, char[] path, TreeSet<String> ans) {
		if (si == str.length) {
			if (pi == path.length) {
				ans.add(String.valueOf(path));
			}
		} else {
			process(si + 1, pi, str, path, ans);
			if (pi < path.length) {
				path[pi] = str[si];
				process(si + 1, pi + 1, str, path, ans);
			}
		}
	}

	// 为了测试
	public static String randomString(int len, int range) {
		char[] str = new char[len];
		for (int i = 0; i < len; i++) {
			str[i] = (char) ((int) (Math.random() * range) + 'a');
		}
		return String.valueOf(str);
	}

	public static void main(String[] args) {
		int n = 12;
		int r = 5;
		int testTime = 10000;
		System.out.println("测试开始");
		for (int i = 0; i < testTime; i++) {
			int len = (int) (Math.random() * (n + 1));
			String str = randomString(len, r);
			int k = (int) (Math.random() * (str.length() + 1));
			String ans1 = maxString(str, k);
			String ans2 = test(str, k);
			if (!ans1.equals(ans2)) {
				System.out.println("出错了！");
				System.out.println(str);
				System.out.println(k);
				System.out.println(ans1);
				System.out.println(ans2);
				break;
			}
		}
		System.out.println("测试结束");
	}

}
