package class42;

/**
 * 给定一个含有 n 个正数的数组 x。从点 (0,0) 开始，先向北移动 x[0] 米，然后向西移动 x[1] 米，向南移动 x[2] 米，向东移动 x[3] 米，持续移动
 * 也就是说，每次移动后你的方位会发生逆时针变化
 * 编写一个 O(1) 空间复杂度的一趟扫描算法，判断你所经过的路径是否相交。
 *
 * 题意解析：
 * 	从任意点开始，逆时针方向用线段画圈，每个线段的长度记在arr中，问线段之间有没有撞上
 * 本题考查业务分析能力，需要很有细心的分析出各种情况
 *
 * 分析：
 * 	来到第i条线段，判断其与以下线段有没有可能相撞
 * 	i-1: 不可能，两个成90的方向
 * 	i-2: 不可能，与i是平行线
 * 	i-3: 有可能，条件：i>=i-2 且 i-3>=i-1
 * 	i-4: 有可能，i有可能追尾i-4，条件：i-1=i-3 且 i + i-4 >= i-2
 * 	i-5: 有可能：i-3>=i-1 且 i + i-4 >= i-2
 * 	i-6以及往后: 想要撞i-6以及往后,必须要撞上了前面5个围起来的城墙，所以不用讨论了
 * 解题：
 * 	0~4位置的线段，单独分析相撞的可能性
 * 	从5位置开始，分析判断其与 i-3/i-4/i-5 位置相撞的可能性，可以统一处理
 */
//leetcode题目 : https://leetcode.com/problems/self-crossing/
public class Problem_0335_SelfCrossing {

	public static boolean isSelfCrossing(int[] x) {
		if (x == null || x.length < 4) {
			return false;
		}
		if ((x.length > 3 && x[2] <= x[0] && x[3] >= x[1])
				|| (x.length > 4 
						&& ((x[3] <= x[1] && x[4] >= x[2]) || (x[3] == x[1] && x[0] + x[4] >= x[2])))) {
			return true;
		}
		for (int i = 5; i < x.length; i++) {
			if (x[i - 1] <= x[i - 3] && ((x[i] >= x[i - 2])
					|| (x[i - 2] >= x[i - 4] && x[i - 5] + x[i - 1] >= x[i - 3] && x[i - 4] + x[i] >= x[i - 2]))) {
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {
		int[] arr = { 2, 2, 3, 2, 2 };
		System.out.println(isSelfCrossing(arr));
	}

}
