package class38;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 请根据每日 气温 列表 temperatures ，请计算在每一天需要等几天才会有更高的温度。如果气温在这之后都不会升高，请在该位置用 0 来代替。
 * 示例 1:
 * 输入: temperatures = [73,74,75,71,69,72,76,73]
 * 输出: [1,1,4,2,1,1,0,0]
 * 示例 2:
 * 输入: temperatures = [30,40,50,60]
 * 输出: [1,1,1,0]
 * 示例 3:
 * 输入: temperatures = [30,60,90]
 * 输出: [1,1,0]
 * 提示：
 * 1 <= temperatures.length <= 10^5
 * 30 <= temperatures[i] <= 100
 *
 * 解题：
 * 	单调栈
 */
//Leetcode题目 : https://leetcode.com/problems/daily-temperatures/
public class Problem_0739_DailyTemperatures {

	public static int[] dailyTemperatures(int[] arr) {
		if (arr == null || arr.length == 0) {
			return new int[0];
		}
		int N = arr.length;
		int[] ans = new int[N];
		/*温度一样的位置，压在一起*/
		Stack<List<Integer>> stack = new Stack<>();
		for (int i = 0; i < N; i++) {
			while (!stack.isEmpty() && arr[stack.peek().get(0)] < arr[i]) {
				/*栈顶遇到比自己大的温度，都弹出来，温度相等的位置存在一个list里*/
				List<Integer> popIs = stack.pop();
				for (Integer popi : popIs) {
					/*这里运算的都是位置*/
					ans[popi] = i - popi;
				}
			}
			if (!stack.isEmpty() && arr[stack.peek().get(0)] == arr[i]) {
				stack.peek().add(Integer.valueOf(i));
			} else {
				ArrayList<Integer> list = new ArrayList<>();
				list.add(i);
				stack.push(list);
			}
		}
		/*栈里可能还有东西，为什么不清算了？清算也都是0，本来默认值就是0*/
		return ans;
	}

}
