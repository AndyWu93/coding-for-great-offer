package class30;

import java.util.ArrayList;
import java.util.List;

/**
 * Given an integer rowIndex, return the rowIndexth (0-indexed) row of the Pascal's triangle.
 *
 * In Pascal's triangle, each number is the sum of the two numbers directly above it as shown:
 * 题意：杨辉三角返回第n行
 * 进阶：额外空间负责度O(1)
 * 解题：
 * 	二维数组空间压缩技巧
 *
 */
public class Problem_0119_PascalTriangleII {

	public List<Integer> getRow(int rowIndex) {
		List<Integer> ans = new ArrayList<>();
		/*
		* 还是逐行生成，但是，是数组自更新
		* 因为依赖上面和左上角的位置，所以自更新时可以从后往前更新
		* */
		for (int i = 0; i <= rowIndex; i++) {
			/*从第2行开始才涉及到计算，第i行i+1个元素，小标从0到i，需要计算的是1到i-1位置*/
			for (int j = i - 1; j > 0; j--) {
				ans.set(j, ans.get(j - 1) + ans.get(j));
			}
			ans.add(1);
		}
		return ans;
	}

}
