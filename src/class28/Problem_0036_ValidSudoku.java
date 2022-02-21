package class28;

/**
 * Determine if a 9 x 9 Sudoku board is valid. Only the filled cells need to be validated according to the following rules:
 *
 * Each row must contain the digits 1-9 without repetition.
 * Each column must contain the digits 1-9 without repetition.
 * Each of the nine 3 x 3 sub-boxes of the grid must contain the digits 1-9 without repetition.
 * Note:
 *
 * A Sudoku board (partially filled) could be valid but is not necessarily solvable.
 * Only the filled cells need to be validated according to the mentioned rules.
 * 题意：判断当前数独是否合法
 * 解题：
 * 	准备3个bool类型的二维数组：
 * 	A[i][j]:第i行，j这个数字有没有出现过
 * 	B[i][j]:第i列，j这个数字有没有出现过
 * 	C[i][j]:第i号桶，j这个数字有没有出现过
 * 	然后，遍历数独，填写这3个二维数组，填写之前先看下这个数有没有出现过，如果出现过直接返回false
 */
public class Problem_0036_ValidSudoku {

	public static boolean isValidSudoku(char[][] board) {
		/*
		* 9：1到9行
		* 10：1到9这个数，0位置不用
		* */
		boolean[][] row = new boolean[9][10];
		boolean[][] col = new boolean[9][10];
		boolean[][] bucket = new boolean[9][10];
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				/*
				* 遍历数独，来到（i,j）位置
				* 当前位置一定是i行，j列
				* 但是是几号桶呢？直接用公式 3 * (i / 3) + (j / 3);
				* */
				int bid = 3 * (i / 3) + (j / 3);
				if (board[i][j] != '.') {
					/*如果该位置时数字，拿到这个数*/
					int num = board[i][j] - '0';
					if (row[i][num] || col[j][num] || bucket[bid][num]) {
						/*
						* 这个数如果在i行出现过
						* 这个数如果在j列出现过
						* 这个数如果在big桶出现过
						* 都是不合法的
						* */
						return false;
					}
					/*记录一下，这个数出现过了*/
					row[i][num] = true;
					col[j][num] = true;
					bucket[bid][num] = true;
				}
			}
		}
		/*遍历到最后了，说明都是合法的，返回true*/
		return true;
	}

}
