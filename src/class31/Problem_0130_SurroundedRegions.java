package class31;

/**
 * Given an m x n matrix board containing 'X' and 'O', capture all regions that are 4-directionally surrounded by 'X'.
 *
 * A region is captured by flipping all 'O's into 'X's in that surrounded region.
 * 题意：将二维数组中上下左右都被'x'包裹的区域都变成'x'
 * 解题：
 * 	感染函数
 * 	从边界的'O'向内部感染，所到区域的'O'都改成'F'，剩下没有被感染到的'O'就是被'X'包裹的
 */
public class Problem_0130_SurroundedRegions {

//	// m -> 二维数组， 不是0就是1
//	//
//	public static void infect(int[][] m, int i, int j) {
//		if (i < 0 || i == m.length || j < 0 || j == m[0].length || m[i][j] != 1) {
//			return;
//		}
//		// m[i][j] == 1
//		m[i][j] = 2;
//		infect(m, i - 1, j);
//		infect(m, i + 1, j);
//		infect(m, i, j - 1);
//		infect(m, i, j + 1);
//	}

	public static void solve1(char[][] board) {
		boolean[] ans = new boolean[1];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if (board[i][j] == 'O') {
					ans[0] = true;
					can(board, i, j, ans);
					board[i][j] = ans[0] ? 'T' : 'F';
				}
			}
		}
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				char can = board[i][j];
				if (can == 'T' || can == 'F') {
					board[i][j] = '.';
					change(board, i, j, can);
				}
			}
		}

	}

	public static void can(char[][] board, int i, int j, boolean[] ans) {
		if (i < 0 || i == board.length || j < 0 || j == board[0].length) {
			ans[0] = false;
			return;
		}
		if (board[i][j] == 'O') {
			board[i][j] = '.';
			can(board, i - 1, j, ans);
			can(board, i + 1, j, ans);
			can(board, i, j - 1, ans);
			can(board, i, j + 1, ans);
		}
	}

	public static void change(char[][] board, int i, int j, char can) {
		if (i < 0 || i == board.length || j < 0 || j == board[0].length) {
			return;
		}
		if (board[i][j] == '.') {
			board[i][j] = can == 'T' ? 'X' : 'O';
			change(board, i - 1, j, can);
			change(board, i + 1, j, can);
			change(board, i, j - 1, can);
			change(board, i, j + 1, can);
		}
	}

	/**
	 * 最佳实现
	 */
	// 从边界开始感染的方法，比第一种方法更好
	public static void solve2(char[][] board) {
		if (board == null || board.length == 0 || board[0] == null || board[0].length == 0) {
			return;
		}
		int N = board.length;
		int M = board[0].length;
		for (int j = 0; j < M; j++) {
			/*上下边界的O向内感染*/
			if (board[0][j] == 'O') {
				free(board, 0, j);
			}
			if (board[N - 1][j] == 'O') {
				free(board, N - 1, j);
			}
		}
		for (int i = 1; i < N - 1; i++) {
			/*左右边界的O先内感染*/
			if (board[i][0] == 'O') {
				free(board, i, 0);
			}
			if (board[i][M - 1] == 'O') {
				free(board, i, M - 1);
			}
		}
		/*没有被感染的O变成X，被感染的O恢复成O*/
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				if (board[i][j] == 'O') {
					board[i][j] = 'X';
				}
				if (board[i][j] == 'F') {
					board[i][j] = 'O';
				}
			}
		}
	}

	public static void free(char[][] board, int i, int j) {
		if (i < 0 || i == board.length || j < 0 || j == board[0].length || board[i][j] != 'O') {
			return;
		}
		board[i][j] = 'F';
		free(board, i + 1, j);
		free(board, i - 1, j);
		free(board, i, j + 1);
		free(board, i, j - 1);
	}

}
