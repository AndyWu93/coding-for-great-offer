package class03;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 电子游戏“辐射4”中，任务“通向自由”要求玩家到达名为“Freedom Trail Ring”的金属表盘，并使用表盘拼写特定关键词才能开门
 * 给定一个字符串 ring，表示刻在外环上的编码；给定另一个字符串 key，表示需要拼写的关键词。您需要算出能够拼写关键词中所有字符的最少步数
 * 最初，ring 的第一个字符与12:00方向对齐。您需要顺时针或逆时针旋转 ring 以使 key 的一个字符在 12:00 方向对齐，然后按下中心按钮，以此逐个拼写完 key 中的所有字符
 * 旋转 ring 拼出 key 字符 key[i] 的阶段中：
 * 您可以将 ring 顺时针或逆时针旋转一个位置，计为1步。旋转的最终目的是将字符串 ring 的一个字符与 12:00 方向对齐，并且这个字符必须等于字符 key[i] 。
 * 如果字符 key[i] 已经对齐到12:00方向，您需要按下中心按钮进行拼写，这也将算作 1 步。按完之后，您可以开始拼写 key 的下一个字符（下一阶段）, 直至完成所有拼写。
 *
 * 题意：
 * 	给定一个字符串r，这个字符串是环形的，有一个指针指向r[0]，问想要拨出k里面的字符串，怎么拨号代价最小
 * 	代价：移动一个位置为1个代价，拨号当前位置为1个代价
 * 解题：
 * 	经典动态规划，写尝试
 */
// 本题测试链接 : https://leetcode.com/problems/freedom-trail/
public class Code07_FreedomTrail {

	public static int findRotateSteps(String r, String k) {
		char[] ring = r.toCharArray();
		int N = ring.length;
		/*
		* 表盘字符的位置
		* key: 表盘中的字符
		* value: 该字符都出现在哪些位置
		* */
		HashMap<Character, ArrayList<Integer>> map = new HashMap<>();
		for (int i = 0; i < N; i++) {
			if (!map.containsKey(ring[i])) {
				map.put(ring[i], new ArrayList<>());
			}
			map.get(ring[i]).add(i);
		}
		char[] str = k.toCharArray();
		int M = str.length;
		int[][] dp = new int[N][M + 1];
		// hashmap
		// dp[][] == -1 : 表示没算过！
		for (int i = 0; i < N; i++) {
			for (int j = 0; j <= M; j++) {
				dp[i][j] = -1;
			}
		}
		return process(0, 0, str, map, N, dp);
	}

	/*
	* preButton：电话里指针指着的上一个按键
	* index：目标里此时要搞定哪个字符？
	* map : key 一种字符 value: 哪些位置拥有这个字符
	* N: 电话大小
	* 返回从index搞定到最后最小代价多少
	* */
	public static int process(int preButton, int index, char[] str, HashMap<Character, ArrayList<Integer>> map, int N,
			int[][] dp) {
		if (dp[preButton][index] != -1) {
			return dp[preButton][index];
		}
		int ans = Integer.MAX_VALUE;
		if (index == str.length) {
			ans = 0;
		} else {
			// 还有字符需要搞定呢！
			char cur = str[index];
			/*拿到需要搞定字符都在哪些位置*/
			ArrayList<Integer> nextPositions = map.get(cur);
			for (int next : nextPositions) {
				/*每个位置：先拨过去，再确认，再搞定后面的*/
				int cost = dial(preButton, next, N) + 1 + process(next, index + 1, str, map, N, dp);
				/*结算最小的*/
				ans = Math.min(ans, cost);
			}
		}
		dp[preButton][index] = ans;
		return ans;
	}

	/*
	* 一个size大小的环中从index1到index2的距离怎么算？
	* 1. 大-小
	* 2. 小-大+size
	* */
	public static int dial(int i1, int i2, int size) {
		return Math.min(Math.abs(i1 - i2), Math.min(i1, i2) + size - Math.max(i1, i2));
	}

}
