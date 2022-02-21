package class28;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Given an array of strings strs, group the anagrams together. You can return the answer in any order.
 *
 * An Anagram is a word or phrase formed by rearranging the letters of a different word or phrase, typically using all the original letters exactly once.
 * 题意：给定一个str数组，将其中的str分类，同类的在一起，返回
 * 		同类定义：由相同的字符组成的str
 * 解题：如何分类？
 * 	str的每个字符排序后一样的，属于同一类
 * 	或者将每个str统计词频，统计结果一样的属于同一类
 */
public class Problem_0049_GroupAnagrams {

	public static List<List<String>> groupAnagrams1(String[] strs) {
		/*key：词频组成的str，value：同一类的str*/
		HashMap<String, List<String>> map = new HashMap<String, List<String>>();
		for (String str : strs) {
			/*给每个str统计词频*/
			int[] record = new int[26];
			for (char cha : str.toCharArray()) {
				record[cha - 'a']++;
			}
			/*统计完了，把词频拼接成str，作为key*/
			StringBuilder builder = new StringBuilder();
			for (int value : record) {
				builder.append(String.valueOf(value)).append("_");
			}
			String key = builder.toString();
			if (!map.containsKey(key)) {
				map.put(key, new ArrayList<String>());
			}
			/*把当前的str添加到表里去*/
			map.get(key).add(str);
		}
		List<List<String>> res = new ArrayList<List<String>>();
		for (List<String> list : map.values()) {
			res.add(list);
		}
		return res;
	}

	public static List<List<String>> groupAnagrams2(String[] strs) {
		/*key：char重新排好序的str，value：同一类的str*/
		HashMap<String, List<String>> map = new HashMap<String, List<String>>();
		for (String str : strs) {
			char[] chs = str.toCharArray();
			Arrays.sort(chs);
			String key = String.valueOf(chs);
			if (!map.containsKey(key)) {
				map.put(key, new ArrayList<String>());
			}
			map.get(key).add(str);
		}
		List<List<String>> res = new ArrayList<List<String>>();
		for (List<String> list : map.values()) {
			res.add(list);
		}
		return res;
	}

}
