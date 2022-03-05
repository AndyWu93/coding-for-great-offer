package class30;

import java.util.ArrayList;
import java.util.List;

/**
 * Given an integer numRows, return the first numRows of Pascal's triangle.
 *
 * In Pascal's triangle, each number is the sum of the two numbers directly above it as shown:
 * 题意：生成杨辉三角返回
 * 解题：
 * 	观察得到每个位置依赖上面和左上角的位置，从上往下生成即可
 */
public class Problem_0118_PascalTriangle {

	public static List<List<Integer>> generate(int numRows) {
		List<List<Integer>> ans = new ArrayList<>();
		/*一共生成n行，都生成好，每一行开头都是1*/
		for (int i = 0; i < numRows; i++) {
			ans.add(new ArrayList<>());
			ans.get(i).add(1);
		}
		/*逐行生成，第一行已经生成好了，就一个1，从第二行开始*/
		for (int i = 1; i < numRows; i++) {
			/*第i行一共i+1个元素，最后一个一定是1，前面i个元素可以通过依赖获得*/
			for (int j = 1; j < i; j++) {
				ans.get(i).add(ans.get(i - 1).get(j - 1) + ans.get(i - 1).get(j));
			}
			ans.get(i).add(1);
		}
		return ans;
	}

}
