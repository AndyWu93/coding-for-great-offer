package class17;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 本题老师没讲好，关于怎么判断一个前缀是不是回文问题
 * 给定一个字符串数组arr，里面都是互不相同的单词，找出所有不同的索引对(i, j)，使得列表中的两个单词，words[i] + words[j]，可拼接成回文串。
 * 解法一：
 * 	枚举arr中的所有两个单词O(N^2)，正着拼，反着拼，拼完看下是不是回文O(K)，所以O(N^2*K)
 * 解法二：
 * 	枚举arr中的1个单词x，O(N)
 * 	从前往后看，看前缀是不是回文，是的话，arr中找x剩下部分的逆序串，拿过来拼在x前面O(K^2)
 * 	从后往前看，看后缀是不是回文，是的话，arr中找x剩下部分的逆序串，拿过来拼在x后面O(K^2)
 * 	所以O(N*K^2)
 */
// 测试链接 : https://leetcode.com/problems/palindrome-pairs/
public class Code03_PalindromePairs {

	/**
	 * 解法二
	 */
	public static List<List<Integer>> palindromePairs(String[] words) {
		HashMap<String, Integer> wordset = new HashMap<>();
		for (int i = 0; i < words.length; i++) {
			wordset.put(words[i], i);
		}
		List<List<Integer>> res = new ArrayList<>();
		//{ [6,23] 、 [7,13] }
		for (int i = 0; i < words.length; i++) {
			// i words[i]
			// findAll(字符串，在i位置，wordset) 返回所有生成的结果返回
			res.addAll(findAll(words[i], i, wordset));
		}
		return res;
	}

	public static List<List<Integer>> findAll(String word, int index, HashMap<String, Integer> words) {
		List<List<Integer>> res = new ArrayList<>();
		String reverse = reverse(word);
		Integer rest = words.get("");
		if (rest != null && rest != index && word.equals(reverse)) {
			/*word是回文，可以前后拼接一个空串*/
			addRecord(res, rest, index);
			addRecord(res, index, rest);
		}
		int[] rs = manacherss(word);
		int mid = rs.length >> 1;
		/*看前缀*/
		for (int i = 1; i < mid; i++) {
			/*表示前缀是回文，rs[0]是#，所以从1开始，看回文半径的长度*/
			if (i - rs[i] == -1) {
				rest = words.get(reverse.substring(0, mid - i));
				if (rest != null && rest != index) {
					addRecord(res, rest, index);
				}
			}
		}
		/*看后缀*/
		for (int i = mid + 1; i < rs.length; i++) {
			if (i + rs[i] == rs.length) {
				rest = words.get(reverse.substring((mid << 1) - i));
				if (rest != null && rest != index) {
					addRecord(res, index, rest);
				}
			}
		}
		return res;
	}

	public static void addRecord(List<List<Integer>> res, int left, int right) {
		List<Integer> newr = new ArrayList<>();
		newr.add(left);
		newr.add(right);
		res.add(newr);
	}

	public static int[] manacherss(String word) {
		char[] mchs = manachercs(word);
		int[] rs = new int[mchs.length];
		int center = -1;
		int pr = -1;
		for (int i = 0; i != mchs.length; i++) {
			rs[i] = pr > i ? Math.min(rs[(center << 1) - i], pr - i) : 1;
			while (i + rs[i] < mchs.length && i - rs[i] > -1) {
				if (mchs[i + rs[i]] != mchs[i - rs[i]]) {
					break;
				}
				rs[i]++;
			}
			if (i + rs[i] > pr) {
				pr = i + rs[i];
				center = i;
			}
		}
		return rs;
	}

	public static char[] manachercs(String word) {
		char[] chs = word.toCharArray();
		char[] mchs = new char[chs.length * 2 + 1];
		int index = 0;
		for (int i = 0; i != mchs.length; i++) {
			mchs[i] = (i & 1) == 0 ? '#' : chs[index++];
		}
		return mchs;
	}

	public static String reverse(String str) {
		char[] chs = str.toCharArray();
		int l = 0;
		int r = chs.length - 1;
		while (l < r) {
			char tmp = chs[l];
			chs[l++] = chs[r];
			chs[r--] = tmp;
		}
		return String.valueOf(chs);
	}

}