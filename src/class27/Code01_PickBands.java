package class27;

import java.util.Arrays;

/**
 * 每一个项目都有三个数，[a,b,c]表示这个项目a和b乐队参演，花费为c
 * 每一个乐队可能在多个项目里都出现了，但是只能被挑一次
 * nums是可以挑选的项目数量，所以一定会有nums*2只乐队被挑选出来
 * 返回一共挑nums轮(也就意味着一定请到所有的乐队)，最少花费是多少？
 * 如果怎么都无法在nums轮请到nums*2只乐队且每只乐队只能被挑一次，返回-1
 * nums<9，programs长度小于500，每组测试乐队的全部数量一定是nums*2，且标号一定是0 ~ nums*2-1
 *
 * 题意：
 * 	现在有nums*2支乐队，他们两两随意组合（一定存在重复的组合），给出了报价，记录在programs中，
 * 	要求从programs中挑出nums个报价，要求每组报价参与的乐队都不一样，且每组报价都是相同组合里最低的
 * 	返回nums个报价的和
 * 解题：
 * 	本题较难，考查暴力递归能力
 * 	首先想一下，programs里挑乐队，好像是背包问题，试着用这种尝试看下数据量
 * 	nums最大8，所以16支乐队，那么报价所有的可能性，一共C(2,16)=120种，programs里存在脏数据，洗一下
 * 	如果是背包，复杂度C(8,120),代入公式算一下，这个值超过了10^8，所以需要尝试改动态规划
 * 	先把递归憋出来
 * 	改dp时，发现有4个可变参数，改dp复杂度还是高，那么只剩下分治一条路
 * 	所以想着怎么把C(8,120)改成两个C(4,120)，再在两个C(4,120)里一对一对的求和，收集报价和最小的
 */
public class Code01_PickBands {

	/**
	 * 最优解
	 * 复杂度 2*C(4,120) + 2^16
	 */
	public static int minCost(int[][] programs, int nums) {
		if (nums == 0 || programs == null || programs.length == 0) {
			return 0;
		}
		/*洗数据*/
		int size = clean(programs);
		/*
		* 本题，无法改递归，所以采用了分治的思路
		* nums个组合，分两次挑出nums/2个，
		* 挑出来以后分别记在map1，map2中，
		* map的key:挑了哪些乐队，标记一下，这里用位的状态来标记挑了哪些乐队。一共最多16支乐队，0没挑，1挑了，所以需要16个位置，map的可以最大是16个1
		* map的value:这些组合报价总和的最低多少
		* 拿到两个map后，遍历map1，去map2中找出剩下乐队的报价，收集最小值
		* */
		/*初始化已给map，index最大值后面nums*2个1，所以是，map的length是1后面nums*2个0*/
		int[] map1 = init(1 << (nums << 1));
		int[] map2 = null;
		if ((nums & 1) == 0) {
			/*偶数，每次挑nums/2个组合，所以两次挑选的流程是一样的，返回的结果也一样，所以map1=map2*/
			// nums = 8 , 4
			f(programs, size, 0, 0, 0, nums >> 1, map1);
			map2 = map1;
		} else {
			/*奇数，那么每次挑组合个数不一样了，返回的map是不同的*/
			// nums == 7 4 -> map1 3 -> map2
			f(programs, size, 0, 0, 0, nums >> 1, map1);
			map2 = init(1 << (nums << 1));
			f(programs, size, 0, 0, 0, nums - (nums >> 1), map2);
		}
		/*
		* 拿到了两个map，遍历其中1个
		* 怎么在另一个中找到对应的呢
		* key的后面nums*2位上的数取反就行了，但是要注意，取反后要把int高位上的1变成0
		* 怎么变，用mask & 一下
		* */
		// 16 mask : 0..00 1111.1111(16个)
		// 12 mask : 0..00 1111.1111(12个)
		int mask = (1 << (nums << 1)) - 1;
		int ans = Integer.MAX_VALUE;
		for (int i = 0; i < map1.length; i++) {
			/*
			* i 挑的一半的乐队
			* mask & (~i)：剩下一半的乐队
			* 在map中都得有效，收集最小值
			* */
			if (map1[i] != Integer.MAX_VALUE && map2[mask & (~i)] != Integer.MAX_VALUE) {
				ans = Math.min(ans, map1[i] + map2[mask & (~i)]);
			}
		}
		/*说明怎么挑都挑不全num*2支乐队*/
		return ans == Integer.MAX_VALUE ? -1 : ans;
	}

	// [
	// [9, 1, 100]
	// [2, 9 , 50]
	// ...
	// ]
	public static int clean(int[][] programs) {
		int x = 0;
		int y = 0;
		/*前面两项：统一编号小的乐队排前面*/
		for (int[] p : programs) {
			x = Math.min(p[0], p[1]);
			y = Math.max(p[0], p[1]);
			p[0] = x;
			p[1] = y;
		}
		/*3阶排序：第一个乐队 ； 第二个乐队 ； 报价*/
		Arrays.sort(programs, (a, b) -> a[0] != b[0] ? (a[0] - b[0]) : (a[1] != b[1] ? (a[1] - b[1]) : (a[2] - b[2])));
		/*
		* 排好序了，就可以洗数据了。
		* 两个乐队都相同的，取排在最前面的，因为报价最低
		* */
		x = programs[0][0];
		y = programs[0][1];
		int n = programs.length;
		for (int i = 1; i < n; i++) {
			if (programs[i][0] == x && programs[i][1] == y) {
				/*2个乐队都相同了，标空*/
				programs[i] = null;
			} else {
				/*否则就是一个新的组合，记一下*/
				x = programs[i][0];
				y = programs[i][1];
			}
		}
		/*把标空的数据去掉*/
		int size = 1;
		for (int i = 1; i < n; i++) {
			if (programs[i] != null) {
				programs[size++] = programs[i];
			}
		}
		// programs[0...size-1]
		return size;
	}

	public static int[] init(int size) {
		int[] arr = new int[size];
		for (int i = 0; i < size; i++) {
			arr[i] = Integer.MAX_VALUE;
		}
		return arr;
	}

	/*
	* programs size: programs[0~size-1]是待挑选的组合和报价
	* index: 来到第index个组合了
	* status：已经挑好的乐队，在对应位上标1了
	* cost：已经挑好的组合，总报价
	* rest：还能挑rest个组合
	* map: 挑满后，记录总报价
	* */
	public static void f(int[][] programs, int size, int index, int status, int cost, int rest, int[] map) {
		if (rest == 0) {
			/*挑完了，记录一下挑了status这些乐队，收集最低报价*/
			map[status] = Math.min(map[status], cost);
		} else {
			/*还能挑，看index,不能到size，只能到size-1，一共就这些报价可以挑*/
			if (index != size) {
				/*index的报价忽略*/
				f(programs, size, index + 1, status, cost, rest, map);
				/*
				* index的报价不忽略
				* 要在status中看下index报价里的两支乐队，之前有没有挑过
				* 所以先生成pick，标记一下这两支乐队
				* 1 << programs[index][0] ： 因为乐队的编号从0~num*2,所以编号就是标记位的地方
				* */
				int pick = (1 << programs[index][0]) | (1 << programs[index][1]);
				if ((pick & status) == 0) {
					/*
					* (pick & status) == 0 ： 说明现在挑的之前没挑过，是有效的
					* pick要标记要合到status里去，
					* cost加上该报价
					* rest额度减1
					* */
					f(programs, size, index + 1, status | pick, cost + programs[index][2], rest - 1, map);
				}
			}
		}
	}

//	// 如果nums，2 * nums 只乐队
//	// (1 << (nums << 1)) - 1
//	// programs 洗数据 size
//	// nums = 8   16只乐队
//	
//	// process(programs, size, (1 << (nums << 1)) - 1, 0, 4, 0, 0)
//	
//	public static int minCost = Integer.MAX_VALUE;
//	
//	public static int[] map = new int[1 << 16]; // map初始化全变成系统最大
//	
//	
//
//	public static void process(int[][] programs, int size, int index, int rest, int pick, int cost) {
//		if (rest == 0) {
//			
//			map[pick] = Math.min(map[pick], cost);
//			
//		} else { // 还有项目可挑
//			if (index != size) {
//				// 不考虑当前的项目！programs[index];
//				process(programs, size, index + 1, rest, pick, cost);
//				// 考虑当前的项目！programs[index];
//				int x = programs[index][0];
//				int y = programs[index][1];
//				int cur = (1 << x) | (1 << y);
//				if ((pick & cur) == 0) { // 终于可以考虑了！
//					process(programs, size, index + 1, rest - 1, pick | cur, cost + programs[index][2]);
//				}
//			}
//		}
//	}
//	
//	
//	public static void zuo(int[] arr, int index, int rest) {
//		if(rest == 0) {
//			停止
//		}
//		if(index != arr.length) {
//			zuo(arr, index + 1, rest);
//			zuo(arr, index + 1, rest - 1);
//		}
//	}

	// 为了测试
	public static int right(int[][] programs, int nums) {
		min = Integer.MAX_VALUE;
		r(programs, 0, nums, 0, 0);
		return min == Integer.MAX_VALUE ? -1 : min;
	}

	public static int min = Integer.MAX_VALUE;

	public static void r(int[][] programs, int index, int rest, int pick, int cost) {
		if (rest == 0) {
			min = Math.min(min, cost);
		} else {
			if (index < programs.length) {
				r(programs, index + 1, rest, pick, cost);
				int cur = (1 << programs[index][0]) | (1 << programs[index][1]);
				if ((pick & cur) == 0) {
					r(programs, index + 1, rest - 1, pick | cur, cost + programs[index][2]);
				}
			}
		}
	}

	// 为了测试
	public static int[][] randomPrograms(int N, int V) {
		int nums = N << 1;
		int n = nums * (nums - 1);
		int[][] programs = new int[n][3];
		for (int i = 0; i < n; i++) {
			int a = (int) (Math.random() * nums);
			int b = 0;
			do {
				b = (int) (Math.random() * nums);
			} while (b == a);
			programs[i][0] = a;
			programs[i][1] = b;
			programs[i][2] = (int) (Math.random() * V) + 1;
		}
		return programs;
	}

	// 为了测试
	public static void main(String[] args) {
		int N = 4;
		int V = 100;
		int T = 10000;
		System.out.println("测试开始");
		for (int i = 0; i < T; i++) {
			int nums = (int) (Math.random() * N) + 1;
			int[][] programs = randomPrograms(nums, V);
			int ans1 = right(programs, nums);
			int ans2 = minCost(programs, nums);
			if (ans1 != ans2) {
				System.out.println("Oops!");
				break;
			}
		}
		System.out.println("测试结束");

		long start;
		long end;
		int[][] programs;

		programs = randomPrograms(7, V);
		start = System.currentTimeMillis();
		right(programs, 7);
		end = System.currentTimeMillis();
		System.out.println("right方法，在nums=7时候的运行时间(毫秒) : " + (end - start));

		programs = randomPrograms(10, V);
		start = System.currentTimeMillis();
		minCost(programs, 10);
		end = System.currentTimeMillis();
		System.out.println("minCost方法，在nums=10时候的运行时间(毫秒) : " + (end - start));

	}

}
