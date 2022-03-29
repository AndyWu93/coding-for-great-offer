package class34;

/**
 * 给定一个n*n的空matix，给2个人下棋。要求实现move(row,col,p)方法，判断p在该坐标放棋子后谁赢了
 * 解题：
 * 	用数组记录p在行列对角线所放置的棋子数
 */
public class Problem_0348_DesignTicTacToe {

	class TicTacToe {
		private int[][] rows;
		private int[][] cols;
		private int[] leftUp;
		private int[] rightUp;
		private boolean[][] matrix;
		private int N;

		public TicTacToe(int n) {
			// 0位置弃而不用
			// rows[a][1] : 1这个人，在a行上，下了几个
			// rows[b][2] : 2这个人，在b行上，下了几个
			rows = new int[n][3];
			cols = new int[n][3];
			// leftUp[2] = 7 : 2这个人，在左对角线上，下了7个
			leftUp = new int[3];
			// rightUp[1] = 9 : 1这个人，在右对角线上，下了9个
			rightUp = new int[3];
			/*记录这个位子有没有棋子*/
			matrix = new boolean[n][n];
			N = n;
		}

		public int move(int row, int col, int player) {
			if (matrix[row][col]) {
				/*这个地方有棋子，放在这里没人赢*/
				return 0;
			}
			/*记录一下这个位置放棋子了*/
			matrix[row][col] = true;
			/*p在row行放了一个棋子*/
			rows[row][player]++;
			cols[col][player]++;
			if (row == col) {
				leftUp[player]++;
			}
			if (row + col == N - 1) {
				rightUp[player]++;
			}
			if (rows[row][player] == N || cols[col][player] == N || leftUp[player] == N || rightUp[player] == N) {
				/*
				* 如果p在这一行，或者这一列，或者两条对角线棋子满了，p就赢了
				* 有没有可能其他人赢？不可能，这个设计决定了谁下棋，谁有可能赢
				* */
				return player;
			}
			/*否则没人赢*/
			return 0;
		}

	}

}
