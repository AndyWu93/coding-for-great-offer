package class31;

import java.util.HashMap;
import java.util.Map;

/**
 * Given an array of points where points[i] = [xi, yi] represents a point on the X-Y plane, return the maximum number of points that lie on the same straight line.
 * 题意：给定一系列的点，求这些点最多的共线点数
 * 解题：
 * 	思路：两个点共线后形成一个斜率来标记这条线
 * 	流程：
 * 		假设有a,b,c,d,e,f,g 7个点，
 * 		求必须经过点a，共线的最大点数
 * 	怎么求？
 * 		a与后面所有的点形成一条斜率，斜率一样的表示共线，收集最多的点数
 * 		再求必须经过点b，共线的最大点数
 * 	注意：求b时，不需要再求与a共线的情况，因为求a时求过了。同理求c时也不需要求a和b，只需要往后求，不需要往前求
 *
 */
// 本题测试链接: https://leetcode.com/problems/max-points-on-a-line/
public class Problem_0149_MaxPointsOnALine {

	// [
	//    [1,3]
	//    [4,9]
	//    [5,7]
	//   ]
	
	public static int maxPoints(int[][] points) {
		if (points == null) {
			return 0;
		}
		if (points.length <= 2) {
			/*如果就1个点或者2个点，直接返回个数*/
			return points.length;
		}
		// key = 3
		// value = {7 , 10}  -> 斜率为3/7的点 有10个
		//         {5,  15}  -> 斜率为3/5的点 有15个
		/*
		* 斜率怎么表示？
		* 可以是map套map，第一个key是分子，第二个key是分母
		* 也可以用String：分子_分母 来表示
		* */
		Map<Integer, Map<Integer, Integer>> map = new HashMap<Integer, Map<Integer, Integer>>();
		int result = 0;
		/*枚举必须进过points[i]的最多共线点数*/
		for (int i = 0; i < points.length; i++) {
			/*换了个点求共线点数，要清理一下map，以及下面共点，共x，共y，普通斜率都需要重置初始值*/
			map.clear();
			/*共点的单独拿出来*/
			int samePosition = 1;
			/*斜率为0的共线点数量*/
			int sameX = 0;
			/*斜率为1*/
			int sameY = 0;
			/*其他斜率的*/
			int line = 0;
			/*从i点往后的点开始求*/
			for (int j = i + 1; j < points.length; j++) { // i号点，和j号点，的斜率关系
				/*两个点x轴和y轴的差值*/
				int x = points[j][0] - points[i][0];
				int y = points[j][1] - points[i][1];
				if (x == 0 && y == 0) {
					samePosition++;
				} else if (x == 0) {
					sameX++;
				} else if (y == 0) {
					sameY++;
				} else { // 普通斜率
					int gcd = gcd(x, y);
					/*约掉一个最大公约数*/
					x /= gcd;
					y /= gcd;
					// x / y
					if (!map.containsKey(x)) {
						map.put(x, new HashMap<Integer, Integer>());
					}
					if (!map.get(x).containsKey(y)) {
						map.get(x).put(y, 0);
					}
					map.get(x).put(y, map.get(x).get(y) + 1);
					/*刷新普通斜率的最大点点数*/
					line = Math.max(line, map.get(x).get(y));
				}
			}
			/*
			* 刷新必须经过i这个点时的最大答案
			* sameX, sameY, line 3个斜率取最大数量，加上共点即必须经过i这个点时的最大答案
			* */
			result = Math.max(result, Math.max(Math.max(sameX, sameY), line) + samePosition);
		}
		return result;
	}

	// 保证初始调用的时候，a和b不等于0
	// O(1)
	public static int gcd(int a, int b) {
		return b == 0 ? a : gcd(b, a % b);
	}

}
