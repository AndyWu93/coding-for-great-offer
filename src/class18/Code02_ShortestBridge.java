package class18;


/**
 * 在给定的二维二进制数组 A 中，存在两座岛。（岛是由四面相连的 1 形成的一个最大组。）现在，我们可以将 0 变为 1，以使两座岛连接起来，变成一座岛。
 * 返回必须翻转的 0 的最小数目。（可以保证答案至少是1）
 *
 * 解题：
 * 	思路：对二维矩阵的一个岛屿，玩宽度优先遍历。
 * 	申请两张二维距离表，存储两片岛屿的宽度优先距离，
 * 	dis1[i][j],这片岛上每个点的距离都是1，此外，每往外扩一层，距离都是2
 * 	dis2[i][j],这片岛上每个点的距离都是1，此外，每往外扩一层，距离都是2
 * 	所以：dis1[i][j]+dis2[i][j]含义：m[i][j]到达岛1的距离 加 m[i][j]到达岛1的距离，
 * 		因为：m[i][j]被算了2遍，且岛屿的初始距离为1
 * 		所以岛1岛2必须经过m[i][j]点的最短距离为: dis1[i][j] + dis2[i][j] - 3
 *	本题难点在于：宽度优先遍历的设计和coding
 */
// 本题测试链接 : https://leetcode.com/problems/shortest-bridge/
public class Code02_ShortestBridge {

	public static int shortestBridge(int[][] m) {
		int N = m.length;
		int M = m[0].length;
		int all = N * M;
		/*岛屿编号，一共两片岛，所以编号就只有0和1*/
		int island = 0;
		/*
		* 宽度优先遍历过程中：（注：二维坐标转成了1维度坐标，简化了数组结构，且节省了常数时间）
		* curs: 遍历到的岛屿四周的第i层的点
		* nexts: 遍历到的岛屿四周的第i+1层的点
		* 这两个数组就是模拟了宽度优先遍历过程
		* records: 记录matrix中所有的点的距离
		* records[0][5]=3: 坐标为5的点，到0号岛屿的距离为3
		* */
		int[] curs = new int[all];
		int[] nexts = new int[all];
		int[][] records = new int[2][all];
		/*
		* 1. 先找到一片岛屿，
		* 记录下这片岛屿的每个点，坐标放入curs中，作为第一批节点
		* 记录所有坐标的距离，默认岛屿上所有点的初始距离都是1
		* 2. 根据curs，得到新的一批坐标，放入nexts中，同时距离都加了1
		* */
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				if (m[i][j] == 1) { /*发现了一片岛屿*/
					// 把这一片的1，都变成2，同时，抓上来了，这一片1组成的初始队列
					// curs, 把这一片的1到自己的距离，都设置成1了，records
					/*
					* 记录下这片岛屿的每个点，坐标放入curs中，作为第一批节点
					* 记录所有坐标的距离，默认岛屿上所有点的初始距离都是1
					* */
					int queueSize = infect(m, i, j, N, M, curs, 0, records[island]);
					int V = 1;
					/*
					* 重复根据一批次的curs，生成新的一批nexts
					* 且在records更新这些点的距离V，距离就是当前遍历的层数
					* */
					while (queueSize != 0) {
						V++;
						// curs里面的点，上下左右，records[点]==0， nexts
						queueSize = bfs(N, M, all, V, curs, queueSize, nexts, records[island]);
						int[] tmp = curs;
						curs = nexts;
						nexts = tmp;
					}
					/*跳出循环：整个矩阵宽度优先遍历结束，再去发现下一个岛*/
					island++;
				}
			}
		}
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < all; i++) {
			min = Math.min(min, records[0][i] + records[1][i]);
		}
		return min - 3;
	}

	// 当前来到m[i][j] , 总行数是N，总列数是M
	// m[i][j]感染出去(找到这一片岛所有的1),把每一个1的坐标，放入到int[] curs队列！
	// 1 (a,b) -> curs[index++] = (a * M + b)
	// 1 (c,d) -> curs[index++] = (c * M + d)
	// 二维已经变成一维了， 1 (a,b) -> a * M + b
	// 设置距离record[a * M +b ] = 1
	public static int infect(int[][] m, int i, int j, int N, int M, int[] curs, int index, int[] record) {
		if (i < 0 || i == N || j < 0 || j == M || m[i][j] != 1) {
			return index;
		}
		// m[i][j] 不越界，且m[i][j] == 1
		/*
		* 为什么感染成2？为了不走回头路
		* 最后为什么不用还原现场？为了不影响主函数中发现下一片岛屿
		* */
		m[i][j] = 2;
		int p = i * M + j;
		record[p] = 1;
		// 收集到不同的1
		curs[index++] = p;
		index = infect(m, i - 1, j, N, M, curs, index, record);
		index = infect(m, i + 1, j, N, M, curs, index, record);
		index = infect(m, i, j - 1, N, M, curs, index, record);
		index = infect(m, i, j + 1, N, M, curs, index, record);
		return index;
	}

	// 二维原始矩阵中，N总行数，M总列数
	// all 总 all = N * M
	// V 要生成的是第几层 curs V-1 nexts V
	// record里面拿距离
	public static int bfs(int N, int M, int all, int V, 
			int[] curs, int size, int[] nexts, int[] record) {
		int nexti = 0; // 我要生成的下一层队列成长到哪了？
		/*遍历curs数组，生成nexts数组*/
		for (int i = 0; i < size; i++) {
			/*
			* curs里的点，如何往外扩一层？
			* 	curs里每个点，上下左右4个点找到
			* 	然后看在records中距离，如果是0，表示就是下一层的
			* 一维的坐标x如何快速定位到上下左右？
			* x<M: 		表示在第0行，没有上
			* x+M>=all: 表示在第N-1行，没有下
			* x%M==0: 	表示在第0列
			* x%M==M-1: 表示在第M-1列
			* */
			int up = curs[i] < M ? -1 : curs[i] - M;
			int down = curs[i] + M >= all ? -1 : curs[i] + M;
			int left = curs[i] % M == 0 ? -1 : curs[i] - 1;
			int right = curs[i] % M == M - 1 ? -1 : curs[i] + 1;
			if (up != -1 && record[up] == 0) {
				record[up] = V;
				nexts[nexti++] = up;
			}
			if (down != -1 && record[down] == 0) {
				record[down] = V;
				nexts[nexti++] = down;
			}
			if (left != -1 && record[left] == 0) {
				record[left] = V;
				nexts[nexti++] = left;
			}
			if (right != -1 && record[right] == 0) {
				record[right] = V;
				nexts[nexti++] = right;
			}
		}
		/*返回nexts数组中用到了哪个位置*/
		return nexti;
	}

}
