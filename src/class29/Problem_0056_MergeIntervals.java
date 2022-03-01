package class29;

import java.util.Arrays;

/**
 * Given an array of intervals where intervals[i] = [starti, endi], merge all overlapping intervals, and return an array of the non-overlapping intervals that cover all the intervals in the input.
 * 题意：把给定的区间数组，能合并的合并，返回新的区间数组
 * 解题：
 * 	可以吧区间数组变成对象数组，对象就是区间，有start，end，方便理解
 * 	本题没有用对象，直接在数组上操作
 */
public class Problem_0056_MergeIntervals {

	public static int[][] merge(int[][] intervals) {
		if (intervals.length == 0) {
			return new int[0][0];
		}
		/*先把数组排个序，按区间的start位置排*/
		Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
		/*维护两个变量，结算当前合并出来的区间*/
		int s = intervals[0][0];
		int e = intervals[0][1];
		/*size是为了复用intervals，储存结果*/
		int size = 0;
		for (int i = 1; i < intervals.length; i++) {
			if (intervals[i][0] > e) {
				/*如果遍历到的一个区间的start值大于之前结算的end值了，就储存之前的区间，新开一个区间来结算*/
				intervals[size][0] = s;
				/*size不要忘了++*/
				intervals[size++][1] = e;
				/*新开一个区间*/
				s = intervals[i][0];
				e = intervals[i][1];
			} else {
				/*如果遍历到的一个区间的start值不大于之前结算的end值了，就推高结算的区间*/
				e = Math.max(e, intervals[i][1]);
			}
		}
		/*最后一次新开的区间不要忘了存进结果*/
		intervals[size][0] = s;
		intervals[size++][1] = e;
		/*返回intervals中size长度的copy*/
		return Arrays.copyOf(intervals, size);
	}

}
