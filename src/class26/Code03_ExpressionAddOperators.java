package class26;

import java.util.LinkedList;
import java.util.List;

/**
 * 给定一个仅包含数字 0-9 的字符串和一个目标值，在数字之间添加 二元 运算符（不是一元）+、- 或 * ，返回所有能够得到目标值的表达式。
 * 输入: num = "123", target = 6
 * 输出: ["1+2+3", "1*2*3"]
 * 示例 2:
 * 输入: num = "232", target = 8
 * 输出: ["2*3+2", "2+3*2"]
 * 示例 3:
 * 输入: num = "105", target = 5
 * 输出: ["1*0+5","10-5"]
 * 示例 4:
 * 输入: num = "00", target = 0
 * 输出: ["0+0", "0-0", "0*0"]
 *
 * 解题：
 * 	本题考查递归能力，本题的递归较难
 * 思路：
 * 	设计递归方法process(arr,index):来到index位置，
 * 	在index位置前面添加3种符号，继续往后
 * 	或者index和之前的数合成一个数字后，在前面添加符合，继续往后
 */
// 本题测试链接 : https://leetcode.com/problems/expression-add-operators/
public class Code03_ExpressionAddOperators {

	public static List<String> addOperators(String num, int target) {
		List<String> ret = new LinkedList<>();
		if (num.length() == 0) {
			return ret;
		}
		/*沿途的数字和+ - * 的决定，放在path里*/
		char[] path = new char[num.length() * 2 - 1];
		// num -> char[]
		char[] digits = num.toCharArray();
		long n = 0;
		for (int i = 0; i < digits.length; i++) { // 尝试0~i前缀作为第一部分
			/*准备好第一个数*/
			n = n * 10 + digits[i] - '0';
			path[i] = digits[i];
			dfs(ret, path, i + 1, 0, n, digits, i + 1, target); // 后续过程
			/*如果第一个数字是0，后面的数组不能够与其组成一个数组再整体继续*/
			if (n == 0) {
				break;
			}
		}
		return ret;
	}

	/*
	* res : 收集的答案
	* path: 之前做的决定，已经从左往右依次填写的字符在其中，可能含有'0'~'9' 与 * - +
	* len: path中用了多少空间，path[0..len-1]已经填写好，len是终止
	* 递归优化参数left: path前面计算后值固定的部分，计算结果；
	* 递归优化参数cur: path后面计算后值不固定的部分，计算结果
	* num ：原数组
	* index: 来到了index位置，决定在index前面加符号
	* aim：固定参数
	* */
	public static void dfs(List<String> res, char[] path, int len,
			long left, long cur, /*这两个参数是递归的优化参数*/
			char[] num, int index, int aim) {
		if (index == num.length) {
			if (left + cur == aim) {
				/*收集答案*/
				res.add(new String(path, 0, len));
			}
			return;
		}
		long n = 0;
		/*目前len位置还是空的，等着填符合，j是后面的位置*/
		int j = len + 1;
		/*试每一个可能的前缀，作为第一个数字！*/
		for (int i = index; i < num.length; i++) { // pos ~ i
			// num[index...i] 作为第一个数字！
			n = n * 10 + num[i] - '0';
			/*j位置，填入num[i]后，随着循环的继续，j后面的位置都会填上num后面的数*/
			path[j++] = num[i];
			/*len的值从来没变过*/
			/*在n的前面填了+*/
			path[len] = '+';
			/*n属于不稳定的值，n前面的计算，即path中的公式都是稳定值，所以cur之前是不稳定的，因为cur后面是+，cur也稳定了*/
			dfs(res, path, j, left + cur, n, num, i + 1, aim);
			path[len] = '-';
			/*注意n的值，因为base case中只计算和，所以这里要加上-*/
			dfs(res, path, j, left + cur, -n, num, i + 1, aim);
			path[len] = '*';
			/*cur依然是不稳定的值*/
			dfs(res, path, j, left, cur * n, num, i + 1, aim);

			/*第一个数字是0，那就不能和后面的数字组合，只能作为单个数字，经历上面的计算流程*/
			if (num[index] == '0') {
				break;
			}
		}
	}

}
