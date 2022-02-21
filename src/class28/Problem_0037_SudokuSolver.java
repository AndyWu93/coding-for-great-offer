package class28;

/**
 * Write a program to solve a Sudoku puzzle by filling the empty cells.
 *
 * A sudoku solution must satisfy all of the following rules:
 *
 * Each of the digits 1-9 must occur exactly once in each row.
 * Each of the digits 1-9 must occur exactly once in each column.
 * Each of the digits 1-9 must occur exactly once in each of the 9 3x3 sub-boxes of the grid.
 * The '.' character indicates empty cells.
 *
 * 题意：把数独解出来，填好返回
 * 解题：
 * 	每一个空格，深度优先遍历，直到有成功填完的
 * 	注意：
 * 		深度优先遍历过程中，如果一个分支是死路，需要返回重新尝试，所以涉及恢复现场
 *
 */
public class Problem_0037_SudokuSolver {

	public static void solveSudoku(char[][] board) {
		/*和上一题一样，准备3张表，表示这个范围内这个数出现了*/
		boolean[][] row = new boolean[9][10];
		boolean[][] col = new boolean[9][10];
		boolean[][] bucket = new boolean[9][10];
		initMaps(board, row, col, bucket);
		process(board, 0, 0, row, col, bucket);
	}

	/*
	* 3张表的填写，参考上一题
	* */
	public static void initMaps(char[][] board, boolean[][] row, boolean[][] col, boolean[][] bucket) {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				int bid = 3 * (i / 3) + (j / 3);
				if (board[i][j] != '.') {
					int num = board[i][j] - '0';
					row[i][num] = true;
					col[j][num] = true;
					bucket[bid][num] = true;
				}
			}
		}
	}

	/*
	* 当前来到(i,j)这个位置，
	* 如果已经有数字，跳到下一个位置上
	* 如果没有数字，尝试1~9，不能和row、col、bucket冲突
	* 返回true：这个位置以及后面的位置都填好了，且没有冲突
	* 返回false: 这个位置，或者后面的位置不能填
	* */
	public static boolean process(char[][] board, int i, int j, boolean[][] row, boolean[][] col, boolean[][] bucket) {
		if (i == 9) {
			/*0~8行都填完了，表示都填好了，返回true*/
			return true;
		}
		// 当离开(i，j)，应该去哪？(nexti, nextj)
		/*
		* 下个位置如何计算？
		* 行号：当前列小于8，行号不变，否则下一行
		* 列号：当前列小于8，列号+1，否则跳回到第0列
		* */
		int nexti = j != 8 ? i : i + 1;
		int nextj = j != 8 ? j + 1 : 0;
		if (board[i][j] != '.') {
			/*当前位置有数，去下个位置*/
			return process(board, nexti, nextj, row, col, bucket);
		} else {
			// 可以尝试1~9
			int bid = 3 * (i / 3) + (j / 3);
			/*当前位置尝试1~9，后续深度优先遍历*/
			for (int num = 1; num <= 9; num++) { // 尝试每一个数字1~9
				if ((!row[i][num]) && (!col[j][num]) && (!bucket[bid][num])) {
					/*可以尝试num条件：num都不冲突*/
					row[i][num] = true;
					col[j][num] = true;
					bucket[bid][num] = true;
					board[i][j] = (char) (num + '0');
					if (process(board, nexti, nextj, row, col, bucket)) {
						/*下个位置，及以后的位置填完了都不冲突，提前返回*/
						return true;
					}
					/*否则，恢复现场后，这个位置再尝试下一个数*/
					row[i][num] = false;
					col[j][num] = false;
					bucket[bid][num] = false;
					board[i][j] = '.';
				}
			}
			return false;
		}
	}

}
