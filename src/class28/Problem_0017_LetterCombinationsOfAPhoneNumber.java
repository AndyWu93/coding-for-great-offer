package class28;

import java.util.ArrayList;
import java.util.List;

/**
 * 九宫格随意拨号，返回拨号的所有组合
 * 解题：
 * 	深度优先遍历，收集答案
 */
public class Problem_0017_LetterCombinationsOfAPhoneNumber {

	/*准备一个九宫格，代表2~9位置的字母*/
	public static char[][] phone = { 
			{ 'a', 'b', 'c' }, // 2    0
			{ 'd', 'e', 'f' }, // 3    1
			{ 'g', 'h', 'i' }, // 4    2
			{ 'j', 'k', 'l' }, // 5    3
			{ 'm', 'n', 'o' }, // 6    
			{ 'p', 'q', 'r', 's' }, // 7 
			{ 't', 'u', 'v' },   // 8
			{ 'w', 'x', 'y', 'z' }, // 9
	};

	// "23"
	public static List<String> letterCombinations(String digits) {
		List<String> ans = new ArrayList<>();
		if (digits == null || digits.length() == 0) {
			return ans;
		}
		char[] str = digits.toCharArray();
		/*深度优先遍历的路径，拨了几个数，这个路径就多长*/
		char[] path = new char[str.length];
		/*深度优先遍历，将结果存入ans*/
		process(str, 0, path, ans);
		return ans;
	}

	/*
	* str是拨号拨的数字
	* index：当前拨号的位置
	* path：之前拨号组成的一种路径
	* 方法含义：
	* 	index之前拨号的路径在path里，从index往后拨号，将所有的可能性都存入ans返回
	* */
	public static void process(char[] str, int index, char[] path, List<String> ans) {
		if (index == str.length) {
			/*来到最后的位置了，收集答案*/
			ans.add(String.valueOf(path));
		} else {
			/*当前拨号位置代表的字符集*/
			char[] cands = phone[str[index] - '2'];
			for (char cur : cands) {
				/*遍历，拿出一个放入path里*/
				path[index] = cur;
				/*去到下个拨号的位置，深度优先遍历*/
				process(str, index + 1, path, ans);
			}
		}
	}

}
