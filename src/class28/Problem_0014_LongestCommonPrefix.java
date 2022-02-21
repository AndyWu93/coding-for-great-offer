package class28;

/**
 * 求几个str的最长公共前缀
 * 遍历str,从第一个str开始，记录当前最长的前缀
 * 到了第二个str，和第一个比对，记录当前最长的公共前缀
 * 到了第三个str，和当前最长的公共前缀比对，更新当前最长的公共前缀
 * 到了第四个str，和当前最长的公共前缀比对，更新当前最长的公共前缀
 * ...
 */
public class Problem_0014_LongestCommonPrefix {

	public static String longestCommonPrefix(String[] strs) {
		if (strs == null || strs.length == 0) {
			return "";
		}
		char[] chs = strs[0].toCharArray();
		int min = Integer.MAX_VALUE;
		for (String str : strs) {
			char[] tmp = str.toCharArray();
			int index = 0;
			while (index < tmp.length && index < chs.length) {
				if (chs[index] != tmp[index]) {
					break;
				}
				index++;
			}
			min = Math.min(index, min);
			if (min == 0) {
				return "";
			}
		}
		return strs[0].substring(0, min);
	}

}
