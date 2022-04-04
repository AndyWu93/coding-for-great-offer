package class03;

/**
 * arr数组中是人的体重，给定一个数k，为船的载重。一条船最多可以坐两个人。问arr中所有人同时过河，最少需要几只船
 * 思路：本题看起来像背包问题，实际是一个贪心
 * 解题过程：
 * 将arr排序，找到<=k/2最右侧的位置a
 * 准备两个指针L,R，L指向a，R指向a+1
 * 看当前arr[L]+arr[R]<=k?
 * 是：R右移，看最多能滑过几个数，假设滑过i个数，L同样向左滑i个数，表示这左右各i个可以两两一对上船
 * 否，或者i=0：当前位置记一下是无用位置，然后L--，再看R能不能往右滑，直到L<0结束
 * 此时结算：
 * 左右两两配对的个数 x
 * 左侧无用的位置个数 y
 * 右侧R还剩余没滑的个数 z
 *     x     +     (y+1)/2      +        z
 * 两两配对的   左侧无用的两个一组   右侧没滑过的一人一条船
 *
 */
// 测试链接 : https://leetcode.com/problems/boats-to-save-people/
public class Code05_BoatsToSavePeople {

	// 请保证arr有序
	public static int minBoat(int[] arr, int limit) {
		if (arr == null || arr.length == 0) {
			return 0;
		}
		int N = arr.length;
		// Arrays.sort(arr);
		if(arr[N - 1] > limit) {
			/*有人体重超过船的最大载重，来多少只船都不行*/
			return -1;
		}
		int lessR = -1;
		// 所有的人体重都不超过limit，继续讨论,  <= limit / 2  最右的位置
		for (int i = N  - 1; i >= 0; i--) {
			/*从右往左遍历，找到a位置*/
			if (arr[i] <= (limit / 2)) {
				lessR = i;
				break;
			}
		}
		if (lessR == -1) {
			/*没找到a位置，那所有人的体重都>k/2,就一人一条船*/
			return N;
		}
		//  <= limit / 2
		int L = lessR;
		int R = lessR + 1;
		int noUsed = 0; // 画X的数量统计，画对号的量(加工出来的)
		while (L >= 0) {
			int solved = 0; // 此时的[L]，让R画过了几个数
			while (R < N && arr[L] + arr[R] <= limit) {
				/*R右滑，同时记录滑过几个数*/
				R++;
				solved++;
			}
			// R来到又不达标的位置
			if (solved == 0) {
				/*滑过0个数，表示要么右边没数了，要么右边数太大，这时L--*/
				noUsed++;
				L--;
			} else { // 此时的[L]，让R画过了solved（>0）个数
				/*L也往左滑动i个位置，但是不能越界，越界就停*/
				L = Math.max(-1, L - solved);
			}
		}
		/*L已越界，此时开始结算*/
		int leftAll = lessR + 1;// 左半区总个数  <= limit /2 的区域
		/*两两配对的个数*/
		int used = leftAll - noUsed; // 画对号的量
		/*右侧剩下没滑过的个数*/
		int moreUnsolved = (N - leftAll) - used; // > limit/2 区中，没搞定的数量
		/*
		* 两两配对的 + 左侧无用的两个人一组 + 右侧没滑过的一人一条船
		* (noUsed + 1) >> 1:为何向上取整？多了一个人，那个人就一人一条船
		* */
		return used + ((noUsed + 1) >> 1) + moreUnsolved;
	}

	public static void main(String[] args) {
		int[] arr = { 1, 2, 2, 2, 2, 3, 3, 3, 3, 3, 4, 4, 4, 4, 5 };
		int weight = 6;
		System.out.println(minBoat(arr, weight));
	}

}
