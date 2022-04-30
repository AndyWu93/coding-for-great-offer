package class18;

/**
 * 给定一个矩阵matrix，先从左上角开始，每一步只能往右或者往下走，走到右下角。然后从右下角出发，每一步只能往上或者往左走，再回到左上角。任何一个位置的数字，只能获得一遍。返回最大路径和。
 * 输入描述:
 * 第一行输入两个整数M和N，M,N<=200
 * 接下来M行，每行N个整数，表示矩阵中元素
 * 输出描述:
 * 输出一个整数，表示最大路径和
 *
 * 解题：
 * 	本题贪心来回都是拿最多的桃子是不行的，只能通过尝试来做，怎么尝试的方案最佳？
 * 	一个人走来回，可以变成2个人同时走一回，最后一定同时到了右下角，都是只能往下或者往右走。这样就转成了一张表的dp
 * 思路：
 * 准备2个小人A,B，A,B一起走，从左上来到右下，就相当于题意中走了个来回
 * 	A来到的位置是 Ar,Ac
 * 	B来到的位置是 Br, Ar + Ac - Br
 * 	A和B，一定迈出的步数，一样多，同步走的
 * 	两人会共同到达右下角，返回两个人路径的最大累加和
 * 	重要限制：来到同一个位置时，只获得一份
 */
// 牛客的测试链接：
// https://www.nowcoder.com/questionTerminal/8ecfe02124674e908b2aae65aad4efdf
// 把如下的全部代码拷贝进java编辑器
// 把文件大类名字改成Main，可以直接通过
import java.util.Scanner;

public class Code03_CherryPickup {




	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int N = sc.nextInt();
		int M = sc.nextInt();
		int[][] matrix = new int[N][M];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				matrix[i][j] = sc.nextInt();
			}
		}
		int ans = cherryPickup(matrix);
		System.out.println(ans);
		sc.close();
	}


	/**
	 * 尝试
	 */
	// 从matrix左上角，走到右下角，过程中只能向右或者向下
	// 到达后，回来，过程中只能向左或者向上，沿途数字只能获得一遍
	// 返回，最大路径和
	public static int comeGoMaxPathSum(int[][] matrix) {
		return process(matrix, 0, 0, 0);
	}

	/*
	 * matrix中，只有0，1
	 * A来到的位置是 Ar,Ac
	 * B来到的位置是 Br, Ar + Ac - Br
	 * A和B，一定迈出的步数，一样多，同步走的
	 * 两人会共同到达右下角，返回两个人路径的最大累加和
	 * 重要限制：来到同一个位置时，只获得一份
	 * */
	public static int process(int[][] matrix, int Ar, int Ac, int Br) {
		int N = matrix.length;
		int M = matrix[0].length;
		if (Ar == N - 1 && Ac == M - 1) {
			/*A走到了右下角,B一定也到了*/
			return matrix[Ar][Ac];
		}
		/*
		 * 还没到右下角，AB只要下面4种走法
		 * A 下 B 右
		 * A 下 B 下
		 * A 右 B 右
		 * A 右 B 下
		 * */
		int Bc = Ar + Ac - Br;
		int ADownBRight = -1;
		if (Ar + 1 < N && Bc + 1 < M) {
			ADownBRight = process(matrix, Ar + 1, Ac, Br);
		}
		int ADownBDown = -1;
		if (Ar + 1 < N && Br + 1 < N) {
			ADownBDown = process(matrix, Ar + 1, Ac, Br + 1);
		}

		int ARightBRight = -1;
		if (Ac + 1 < M && Bc + 1 < M) {
			ARightBRight = process(matrix, Ar, Ac + 1, Br);
		}
		int ARightBDown = -1;
		if (Ac + 1 < M && Br + 1 < N) {
			ARightBDown = process(matrix, Ar, Ac + 1, Br + 1);
		}
		/*4种走法取最大值，表示AB后续走出的最大路径和*/
		int nextBest = Math.max(Math.max(ADownBRight, ADownBDown), Math.max(ARightBRight, ARightBDown));
		/*
		 * 后面只要加上现在的位置的数即可
		 * */
		if (Ar == Br) {
			/*A B，在同一个位置*/
			return matrix[Ar][Ac] + nextBest;
		}
		/*A B，不同位置*/
		return matrix[Ar][Ac] + matrix[Br][Bc] + nextBest;
	}


	/**
	 * 改成动态规划，3个可变参数，记忆化搜索
	 */
	public static int cherryPickup(int[][] grid) {
		int N = grid.length;
		int M = grid[0].length;
		int[][][] dp = new int[N][M][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				for (int k = 0; k < N; k++) {
					/*dp默认最小值*/
					dp[i][j][k] = Integer.MIN_VALUE;
				}
			}
		}
		int ans = process(grid, 0, 0, 0, dp);
		return ans < 0 ? 0 : ans;
	}

	public static int process(int[][] grid, int x1, int y1, int x2, int[][][] dp) {
		if (x1 == grid.length || y1 == grid[0].length || x2 == grid.length || x1 + y1 - x2 == grid[0].length) {
			/*越界返回最小值*/
			return Integer.MIN_VALUE;
		}
		if (dp[x1][y1][x2] != Integer.MIN_VALUE) {
			/*有效缓存，直接返回*/
			return dp[x1][y1][x2];
		}
		if (x1 == grid.length - 1 && y1 == grid[0].length - 1) {
			/*到了右下角*/
			dp[x1][y1][x2] = grid[x1][y1];
			return dp[x1][y1][x2];
		}
		int next = Integer.MIN_VALUE;
		next = Math.max(next, process(grid, x1 + 1, y1, x2 + 1, dp));
		next = Math.max(next, process(grid, x1 + 1, y1, x2, dp));
		next = Math.max(next, process(grid, x1, y1 + 1, x2, dp));
		next = Math.max(next, process(grid, x1, y1 + 1, x2 + 1, dp));
		/*这个if应该没用，grid中不可能有-1，next可能有最下值，但也没有-1*/
		if (grid[x1][y1] == -1 || grid[x2][x1 + y1 - x2] == -1 || next == -1) {
			dp[x1][y1][x2] = -1;
			return dp[x1][y1][x2];
		}
		if (x1 == x2) {
			/*AB同格*/
			dp[x1][y1][x2] = grid[x1][y1] + next;
			return dp[x1][y1][x2];
		}
		dp[x1][y1][x2] = grid[x1][y1] + grid[x2][x1 + y1 - x2] + next;
		return dp[x1][y1][x2];
	}

}