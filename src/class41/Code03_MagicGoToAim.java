package class41;

import java.util.PriorityQueue;

/**
 * 来自网易互娱
 * N个结点之间，表世界存在双向通行的道路，里世界存在双向通行的传送门.
 * 若走表世界的道路，花费一分钟.
 * 若走里世界的传送门，不花费时间，但是接下来一分钟不能走传送门.
 * 输入： T为测试用例的组数，对于每组数据:
 * 第一行：N M1 M2 N代表结点的个数1到N
 * 接下来M1行 每行两个数，u和v，表示表世界u和v之间存在道路
 * 接下来M2行 每行两个数，u和v，表示里世界u和v之间存在传送门
 * 现在处于1号结点，最终要到达N号结点，求最小的到达时间 保证所有输入均有效，不存在环等情况
 *
 * 解题：
 * 	本题解题思路需要用到迪克斯特拉算法，见class17.Code01_Dijkstra
 * 	因为两个节点之间可以通过走路或者传送，就相当于有两条变，而且走路和传送的选择需要取决于之前到达这个节点的方式
 * 	所以和Dijkstra不同的是，最终需要求出来的一个到点x的distanceMap是一个二维的表
 * 	distanceMap[0][i]: 最后一步是通过走路到达i的代价
 * 	distanceMap[1][i]: 最后一步是通过传送到达i的代价
 */
// 来自网易互娱
// N个结点之间，表世界存在双向通行的道路，里世界存在双向通行的传送门.
// 若走表世界的道路，花费一分钟.
// 若走里世界的传送门，不花费时间，但是接下来一分钟不能走传送门.
// 输入： T为测试用例的组数，对于每组数据:
// 第一行：N M1 M2 N代表结点的个数1到N
// 接下来M1行 每行两个数，u和v，表示表世界u和v之间存在道路
// 接下来M2行 每行两个数，u和v，表示里世界u和v之间存在传送门
// 现在处于1号结点，最终要到达N号结点，求最小的到达时间 保证所有输入均有效，不存在环等情况 
public class Code03_MagicGoToAim {

	/*
	* 向把题中的输入整理这里的两个二维数组，作为入参
	* */
	// 城市编号从0开始，编号对应0~n-1
	// roads[i]是一个数组，表示i能走路达到的城市有哪些，每条路都花费1分钟
	// gates[i]是一个数组，表示i能传送达到的城市有哪些
	// 返回从0到n-1的最少用时
	public static int fast(int n, int[][] roads, int[][] gates) {
		// distance[0][i] -> 0 : 前一个城市到达i，是走路的方式, 最小代价，distance[0][i]
		// distance[1][i] -> 1 : 前一个城市到达i，是传送的方式, 最小代价，distance[1][i]
		/*就是要将这张表填好*/
		int[][] distance = new int[2][n];
		// 因为从0开始走，所以distance[0][0] = 0, distance[1][0] = 0
		for (int i = 1; i < n; i++) {
			/*除了0，其他都先设为max*/
			distance[0][i] = Integer.MAX_VALUE;
			distance[1][i] = Integer.MAX_VALUE;
		}
		// 小根堆，根据距离排序，距离小的点，在上！
		PriorityQueue<Node> heap = new PriorityQueue<>((a, b) -> a.cost - b.cost);
		/*走路到达了0，代价是0，先加入堆里，为什么选走路，因为0都后面一个节点就既可以选走路也可以选传送了*/
		heap.add(new Node(0, 0, 0));
		/*收集弹出过的点，即当前距离最小的点，该点的距离确定了，将不再更新*/
		boolean[][] visited = new boolean[2][n];
		while (!heap.isEmpty()) {
			Node cur = heap.poll();
			if (visited[cur.preTransfer][cur.city]) {
				continue;
			}
			visited[cur.preTransfer][cur.city] = true;
			// cur通过走路的方式到达他的邻居们，去更新代价
			for (int next : roads[cur.city]) {
				if (distance[0][next] > cur.cost + 1) {
					distance[0][next] = cur.cost + 1;
					/*
					* 原本是应该要将节点(0,next,cost)改成(0,next,cost')，然后调整它在堆里的位置
					* 但是这里简单处理了一下，效果是一样的：
					* 就是加入了一个新的节点(0,next,cost')，
					* 以后(0,next,cost)，(0,next,cost')弹出时都会被忽略，因为已经在visit里做了记号
					* */
					heap.add(new Node(0, next, distance[0][next]));
				}
			}
			// cur通过传送的方式到达他的邻居们，去更新代价
			if (cur.preTransfer == 0) {
				/*只有是通过走路来到cur时，cur去邻居才能传送*/
				for (int next : gates[cur.city]) {
					if (distance[1][next] > cur.cost) {
						distance[1][next] = cur.cost;
						heap.add(new Node(1, next, distance[1][next]));
					}
				}
			}
		}
		return Math.min(distance[0][n - 1], distance[1][n - 1]);
	}

	public static class Node {
		/*怎么来到当前节点的，0走路，1传送*/
		public int preTransfer;
		/*当前节点是谁*/
		public int city;
		/*从1节点到单前节点的代价*/
		public int cost;

		public Node(int a, int b, int c) {
			preTransfer = a;
			city = b;
			cost = c;
		}
	}

}