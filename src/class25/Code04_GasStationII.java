package class25;

/**
 * 本题和体系学习班gasStation的题目一样，但是要求额外空间复杂度O(1)
 * 解题：
 * 	前面的和之前一样，用cost-gas，生成一个纯能数组
 * 	这里引入联通区的概念
 * 	联通区：
 * 		[start,end):从start能够顺利走到end-1
 * 		rest: 从start到end-1，能够余下多少gas
 * 		need：之前的节点接上start需要多少gas
 * 	先找到一个正数能值作为出发点，尽量扩
 * 	扩不动了，start往前移动，同时更新need，直到找到一个点，能值很大，把need归零了，还有剩余的gas带给rest，能够继续扩
 * 	如果扩成功了，出发点之前的点只需要能够顺利连上联通区就都是良好出发点了
 * 	如果一直都没能够扩成功，已经扩出来的联通区里的点都不可能是良好出发点了，因为联通区就是集合大家力量积蓄rest，单个点的力量肯定没有大家的力量大
 *
 * 	本题只要讲出思路就可以了
 */
// 本题测试链接 : https://leetcode.com/problems/gas-station/
// 注意本题的实现比leetcode上的问法更加通用
// leetcode只让返回其中一个良好出发点的位置
// 本题是返回结果数组，每一个出发点是否是良好出发点都求出来了
// 得到结果数组的过程，时间复杂度O(N)，额外空间复杂度O(1)
public class Code04_GasStationII {

	/**
	 * 这个方法的时间复杂度O(N)，额外空间复杂度O(1)
	 */
	public static int canCompleteCircuit(int[] gas, int[] cost) {
		if (gas == null || gas.length == 0) {
			return -1;
		}
		if (gas.length == 1) {
			return gas[0] < cost[0] ? -1 : 0;
		}
		boolean[] good = stations(cost, gas);
		for (int i = 0; i < gas.length; i++) {
			if (good[i]) {
				return i;
			}
		}
		return -1;
	}

	public static boolean[] stations(int[] cost, int[] gas) {
		if (cost == null || gas == null || cost.length < 2 || cost.length != gas.length) {
			return null;
		}
		/*生成纯能值数组，并找到一个正数作为出发点*/
		int init = changeDisArrayGetInit(cost, gas);
		/*没找到？那都是false，否则，从这个出发点开始生成联通区，求出结果*/
		return init == -1 ? new boolean[cost.length] : enlargeArea(cost, init);
	}

	public static int changeDisArrayGetInit(int[] dis, int[] oil) {
		int init = -1;
		for (int i = 0; i < dis.length; i++) {
			dis[i] = oil[i] - dis[i];
			if (dis[i] >= 0) {
				init = i;
			}
		}
		return init;
	}

	public static boolean[] enlargeArea(int[] dis, int init) {
		boolean[] res = new boolean[dis.length];
		/*联通区头部*/
		int start = init;
		/*联通区尾部的下一个*/
		int end = nextIndex(init, dis.length);
		/*接上联通区头部需要多少gas，一开始是0，因为此时联通区头部是个正数，不需要gas*/
		int need = 0;
		/*联通区整体剩余多少gas*/
		int rest = 0;
		/*
		* while中，start在往前扩，
		* start往前扩的含义：目前联通区的rest不足以继续扩了，需要往前找个gas多的点
		* 所以，在start往前的时候，每次都能够求出start是不是良好的出发点
		* 加入start一直撞到联通区的尾巴，都没找到一个gas多的点，表示联通区根本无法扩到整圈
		* */
		do {
			// 当前来到的start已经在连通区域中，可以确定后续的开始点一定无法转完一圈
			if (start != init && start == lastIndex(end, dis.length)) {
				break;
			}
			// 当前来到的start不在连通区域中，就扩充连通区域
			// start(5) ->  联通区的头部(7) -> 2
			// start(-2) -> 联通区的头部(7) -> 9
			if (dis[start] < need) { // 当前start无法接到连通区的头部
				/*找到了一个start都连不上联通区的头部，只能再往前找*/
				need -= dis[start];
			} else { // 当前start可以接到连通区的头部，开始扩充连通区域的尾巴
				// start(7) -> 联通区的头部(5)
				/*找到了一个start，gas很多，把need还清了，还有剩余的gas加给联通区*/
				rest += dis[start] - need;
				need = 0;
				/*
				* 联通区就继续尝试往下扩，这个循环出来了表示gas用完了，或者gas没用完，但是联通区首尾相连了
				* 这里的start上的gas一定是正数
				* */
				while (rest >= 0 && end != start) {
					rest += dis[end];
					end = nextIndex(end, dis.length);
				}
				// 如果连通区域已经覆盖整个环，当前的start是良好出发点，进入2阶段
				if (rest >= 0) {
					/*联通区整个扩通了，在现在联通区之前的一直到最开始选中的正纯能点之间的所有点，只要能够连上目前联通区的start就能够跑完一圈*/
					res[start] = true;
					connectGood(dis, lastIndex(start, dis.length), init, res);
					break;
				}
			}
			start = lastIndex(start, dis.length);
		} while (start != init);
		return res;
	}

	// 已知start的next方向上有一个良好出发点
	// start如果可以达到这个良好出发点，那么从start出发一定可以转一圈
	public static void connectGood(int[] dis, int start, int init, boolean[] res) {
		/*只关注能否连上start，所以只有need*/
		int need = 0;
		/*init之前的一些个start都已经求过了，现在只要求start之前的start*/
		while (start != init) {
			if (dis[start] < need) {
				need -= dis[start];
			} else {
				res[start] = true;
				need = 0;
			}
			start = lastIndex(start, dis.length);
		}
	}

	public static int lastIndex(int index, int size) {
		return index == 0 ? (size - 1) : index - 1;
	}

	public static int nextIndex(int index, int size) {
		return index == size - 1 ? 0 : (index + 1);
	}

}
