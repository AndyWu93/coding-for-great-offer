package class17;

import java.util.HashMap;

/**
 * 给定一个字符串str，返回str的所有子序列中有多少不同的字面值
 * 解题：
 * 	从左往右的尝试
 * 	来到的字符str[i]
 * 	上一步生成的所有子序列，每个后面加上字符str[i]，形成了一批新的子序列
 * 	但是经过观察就会发现，有一部分的子序列重了
 * 	重的是哪一部分呢？
 * 	是上次来到与str[i]相同的字符时，他在他的上一步生成的所有子序列，每个后面加上字符，形成了一批新的子序列
 * 	这一部分重的子序列需要删掉
 *
 */
// 本题测试链接 : https://leetcode.com/problems/distinct-subsequences-ii/
public class Code05_DistinctSubseqValue {

	public static int distinctSubseqII(String s) {
		if (s == null || s.length() == 0) {
			return 0;
		}
		int m = 1000000007;
		char[] str = s.toCharArray();
		int[] count = new int[26];
		int all = 1; // 算空集
		for (char x : str) {
			int add = (all - count[x - 'a'] + m) % m;
			all = (all + add) % m;
			count[x - 'a'] = (count[x - 'a'] + add) % m;
		}
		return all - 1;
	}

	/**
	 * 课上版本
	 */
	public static int zuo(String s) {
		if (s == null || s.length() == 0) {
			return 0;
		}
		char[] str = s.toCharArray();
		HashMap<Character, Integer> map = new HashMap<>();
		int all = 1; // 一个字符也没遍历的时候，有空集
		for (char x : str) {
			/*新增的一批子序列，和上一个字符的子序列数量一样*/
			int newAdd = all;
			/*原来的all，加上新增的，减去x字符上次新增的一批子序列数量*/
			all = all + newAdd - (map.containsKey(x) ? map.get(x) : 0);
			/*更新一下x字符新增的子序列数量*/
			map.put(x, newAdd);
		}
		return all;
	}

	/**
	 *
	 */
	public static int zuo1(String s) {
		if (s == null || s.length() == 0) {
			return 0;
		}
		int m = 1000000007;
		char[] str = s.toCharArray();
		HashMap<Character, Integer> map = new HashMap<>();
		int all = 1; // 一个字符也没遍历的时候，有空集
		for (char x : str) {
			int newAdd = all;
			int curAll = all;
			curAll = (curAll + newAdd) % m;
			/*相减的时候一定要记得加上一个m，因为要保证值为正数，才能与m取模*/
			curAll = (curAll - (map.containsKey(x) ? map.get(x) : 0) + m) % m;
			all = curAll;
			map.put(x, newAdd);
		}
		return all;
	}
	public static void main(String[] args) {
		String s = "bccaccbaabbc";
		System.out.println(distinctSubseqII(s) + 1);
		System.out.println(zuo(s));
	}

}
