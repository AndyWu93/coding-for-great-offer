package class34;

/**
 * According to Wikipedia's article: "The Game of Life, also known simply as Life, is a cellular automaton devised by the British mathematician John Horton Conway in 1970."
 *
 * The board is made up of an m x n grid of cells, where each cell has an initial state: live (represented by a 1) or dead (represented by a 0). Each cell interacts with its eight neighbors (horizontal, vertical, diagonal) using the following four rules (taken from the above Wikipedia article):
 *
 * Any live cell with fewer than two live neighbors dies as if caused by under-population.
 * Any live cell with two or three live neighbors lives on to the next generation.
 * Any live cell with more than three live neighbors dies, as if by over-population.
 * Any dead cell with exactly three live neighbors becomes a live cell, as if by reproduction.
 * The next state is created by applying the above rules simultaneously to every cell in the current state, where births and deaths occur simultaneously. Given the current state of the m x n grid board, return the next state.
 * 题意：给定一个生命游戏的状态，计算出下一个状态，要求时额外空间复杂度O(1)
 * 解题：
 * 	每个格子里都是一个32位的整数，目前只用到了最后一位
 * 	所以规定：
 * 		最后一位是这个数原始的状态
 * 		倒数第二位是这个数下一个状态
 */
// 有关这个游戏更有意思、更完整的内容：
// https://www.bilibili.com/video/BV1rJ411n7ri
// 也推荐这个up主
public class Problem_0289_GameOfLife {

	public static void gameOfLife(int[][] board) {
		int N = board.length;
		int M = board[0].length;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				/*遍历每个元素，计算其周边的细胞数量*/
				int neighbors = neighbors(board, i, j);
				/*
				* 4个生命游戏的规则可以转化为下面两个规则：
				* 1. 有3个邻居，不管现在是什么状态，下次一定活
				* 2. 现在是活的，其周围有2个邻居，下次还是活着
				* */
				if (neighbors == 3 || (board[i][j] == 1 && neighbors == 2)) {
					/*下次会活，所以倒数第二位不管现在是几，要变成1*/
					board[i][j] |= 2;
				}
			}
		}
		/*最后，整体右移1位，将原始状态移除*/
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				board[i][j] >>= 1;
			}
		}
	}

	// b[i][j] 这个位置的数，周围有几个1
	public static int neighbors(int[][] b, int i, int j) {
		/*将周边8个位置的细胞数量加起来*/
		return f(b, i - 1, j - 1)
				+ f(b, i - 1, j)
				+ f(b, i - 1, j + 1)
				+ f(b, i, j - 1)
				+ f(b, i, j + 1)
				+ f(b, i + 1, j - 1)
				+ f(b, i + 1, j)
				+ f(b, i + 1, j + 1);
	}

	// b[i][j] 上面有1，就返回1，上面不是1，就返回0
	public static int f(int[][] b, int i, int j) {
		/*
		* 最后一位是原始状态，所以只要看这个位置最后的位上的数
		* i >= 0 && i < b.length && j >= 0 && j < b[0].length ： 不越界
		*  (b[i][j] & 1) == 1)：且最后一位是1，才是1
		* 其他情况都是0
		 * */
		return (i >= 0 && i < b.length && j >= 0 && j < b[0].length && (b[i][j] & 1) == 1) ? 1 : 0;
	}

}
