package class13;

/**
 * 有一个 m x n 的二元网格，其中 1 表示砖块，0 表示空白。砖块 稳定（不会掉落）的前提是：
 * 一块砖直接连接到网格的顶部，或者至少有一块相邻（4 个方向之一）砖块稳定不会掉落时
 * 给你一个数组 hits ，这是需要依次消除砖块的位置。每当消除 hits[i] = (rowi, coli) 位置上的砖块时，对应位置的砖块（若存在）会消失
 * 然后其他的砖块可能因为这一消除操作而掉落。一旦砖块掉落，它会立即从网格中消失（即，它不会落在其他稳定的砖块上）
 * 返回一个数组 result ，其中 result[i] 表示第 i 次消除操作对应掉落的砖块数目
 * 注意，消除可能指向是没有砖块的空白位置，如果发生这种情况，则没有砖块掉落。
 *
 * 解题：
 * 	本题思路，首先看到什么与什么连在一起，右因为什么原因断开连接了，就要想到并查集能不能做
 * 	但是，并查集只能将两个集合合并，不能将两个集合拆分，
 * 	所以，本题可以想一下，怎么调度才能只产生合并，不产生拆分的情况
 * 	将本题的过程逆序，就可以只产生合并，不产生拆分：
 * 		将所有被打到的位置都标记一下，都标记好了以后，计算一下连在天花板上的砖头
 * 		逆序将被打到的位置还原，并还原砖块之间的连接，这样就能计算出这块砖影响的砖块数量
 */
// 本题测试链接 : https://leetcode.com/problems/bricks-falling-when-hit/
public class Code04_BricksFallingWhenHit {

	public static int[] hitBricks(int[][] grid, int[][] hits) {
		/*给打中的有效的位置做标记*/
		for (int i = 0; i < hits.length; i++) {
			if (grid[hits[i][0]][hits[i][1]] == 1) {
				grid[hits[i][0]][hits[i][1]] = 2;
			}
		}
		/*给每个1建立好并查集，并合并1*/
		UnionFind unionFind = new UnionFind(grid);
		int[] ans = new int[hits.length];
		/*逆向遍历打中的位置*/
		for (int i = hits.length - 1; i >= 0; i--) {
			if (grid[hits[i][0]][hits[i][1]] == 2) {
				/*点2成1，并将影响数量返回，收集*/
				ans[i] = unionFind.finger(hits[i][0], hits[i][1]);
			}
		}
		return ans;
	}

	/*
	* 并查集，合并的是砖块，每个砖块是用（row,col）来标记的
	* 这里想简单一点，每个砖块用一个编号来标记
	* */
	public static class UnionFind {
		private int N;
		private int M;
		// 有多少块砖，连到了天花板上
		private int cellingAll;
		// 原始矩阵，因为炮弹的影响，1 -> 2
		private int[][] grid;
		// cellingSet[i] = true; i 是代表节点，所在的集合是天花板集合
		private boolean[] cellingSet;
		/*节点之间的关系*/
		private int[] fatherMap;
		/*代表节点所代表的集合size*/
		private int[] sizeMap;
		/*合并时，用来将沿路的节点挂到代表节点，开一个共用的stack，节省空间且提高速度*/
		private int[] stack;

		public UnionFind(int[][] matrix) {
			initSpace(matrix);
			initConnect();
		}

		private void initSpace(int[][] matrix) {
			grid = matrix;
			N = grid.length;
			M = grid[0].length;
			int all = N * M;
			cellingAll = 0;
			cellingSet = new boolean[all];
			fatherMap = new int[all];
			sizeMap = new int[all];
			stack = new int[all];
			/*给每个1建立集合*/
			for (int row = 0; row < N; row++) {
				for (int col = 0; col < M; col++) {
					if (grid[row][col] == 1) {
						int index = row * M + col;
						fatherMap[index] = index;
						sizeMap[index] = 1;
						if (row == 0) {
							/*标记一下，这块砖是天花板上的*/
							cellingSet[index] = true;
							cellingAll++;
						}
					}
				}
			}
		}

		private void initConnect() {
			/*每个节点，上下左右合并*/
			for (int row = 0; row < N; row++) {
				for (int col = 0; col < M; col++) {
					union(row, col, row - 1, col);
					union(row, col, row + 1, col);
					union(row, col, row, col - 1);
					union(row, col, row, col + 1);
				}
			}
		}

		private int find(int row, int col) {
			int stackSize = 0;
			int index = row * M + col;
			while (index != fatherMap[index]) {
				stack[stackSize++] = index;
				index = fatherMap[index];
			}
			while (stackSize != 0) {
				fatherMap[stack[--stackSize]] = index;
			}
			return index;
		}

		private void union(int r1, int c1, int r2, int c2) {
			/*只合并不越界的，且值是1*/
			if (valid(r1, c1) && valid(r2, c2)) {
				int father1 = find(r1, c1);
				int father2 = find(r2, c2);
				/*找到代表节点，不是同一个，小挂大*/
				if (father1 != father2) {
					int size1 = sizeMap[father1];
					int size2 = sizeMap[father2];
					boolean status1 = cellingSet[father1];
					boolean status2 = cellingSet[father2];
					if (size1 <= size2) {
						fatherMap[father1] = father2;
						sizeMap[father2] = size1 + size2;
						/*
						* 小挂大时，什么情况下回影响连在天花板上的砖块数量？
						* 只有两个集合，一个是天花板集合，一个不是的时候，才会影响，其他情况都不会影响
						* */
						if (status1 ^ status2) {
							cellingSet[father2] = true;
							/*总量加的是不在天花板里的数量*/
							cellingAll += status1 ? size2 : size1;
						}
					} else {
						fatherMap[father2] = father1;
						sizeMap[father1] = size1 + size2;
						if (status1 ^ status2) {
							cellingSet[father1] = true;
							cellingAll += status1 ? size2 : size1;
						}
					}
				}
			}
		}

		private boolean valid(int row, int col) {
			return row >= 0 && row < N && col >= 0 && col < M && grid[row][col] == 1;
		}

		public int cellingNum() {
			return cellingAll;
		}

		public int finger(int row, int col) {
			grid[row][col] = 1;
			int cur = row * M + col;
			if (row == 0) {
				/*点中的位置是天花板位置，天花板砖数量++*/
				cellingSet[cur] = true;
				cellingAll++;
			}
			/*刚刚点中的1生成集合*/
			fatherMap[cur] = cur;
			sizeMap[cur] = 1;
			/*记录一下之前天花板砖数量*/
			int pre = cellingAll;
			/*将点中的1上下左右合并*/
			union(row, col, row - 1, col);
			union(row, col, row + 1, col);
			union(row, col, row, col - 1);
			union(row, col, row, col + 1);
			/*合并之后，天花板砖数量*/
			int now = cellingAll;
			if (row == 0) {
				/*如果点中的位置是天花板，这个位置的影响合在pre里，减掉了*/
				return now - pre;
			} else {
				/*不是天花板砖，要单独减*/
				return now == pre ? 0 : now - pre - 1;
			}
		}
	}

}
