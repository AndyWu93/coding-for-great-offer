package class03;

/**
 * 给定一个只有0和1组成的二维数组，返回边框全是1（内部无所谓）的最大正方形面积
 * 解题：
 * 	如何枚举一个长方形？两个点 O(N^2)*O(N^2)=O(N^4)
 * 	如何枚举一个正方形？一个点+一个边 O(N^2)*O(N)=O(N^3)
 * 	思路：
 * 	枚举出一个正方形，注意本题一个点也是一个正方形，变成为1，面积为1，枚举边长的时候要小心
 * 	for (int i = 0; i < N; i++) {
 * 			for (int j = 0; j < M; j++) {
 * 				这里的min函数，是为了取该点距离右边/下边的最小值
 * 				for (int border = 1; border <=Math.min(N-i,M-j) ; border++) {
 *                  这里判断这个正方形的边是不是都是由1构成，如何优化成O(1)是复杂度的关键
 *               }
 *           }
 * 	}
 *
 * 	优化思路：
 * 		对于任意一个点(i,j)如果知道其右边/下边有几个连续的1，即可判断这个点能否构成一个边长为border的正方形
 * 		所以用预处理数组
 *
 */
// 本题测试链接 : https://leetcode.com/problems/largest-1-bordered-square/
public class Code03_Largest1BorderedSquare {
	public static void main(String[] args) {

	}

	public static int largest1BorderedSquare(int[][] m) {
		/*
		* 预处理：
		* right[i][j]:m[i][j]右边几个连续的1
		* down[i][j]:m[i][j]下边几个连续的1
		* */
		int[][] right = new int[m.length][m[0].length];
		int[][] down = new int[m.length][m[0].length];
		setBorderMap(m, right, down);
		for (int size = Math.min(m.length, m[0].length); size != 0; size--) {
			if (hasSizeOfBorder(size, right, down)) {
				return size * size;
			}
		}
		return 0;
	}

	/*
	* 预处理数组计算技巧
	* 计算出两个数组最右边和最下边的结果，再从下往上，从右往左遍历，计算每个位置的值
	* */
	public static void setBorderMap(int[][] m, int[][] right, int[][] down) {
		int r = m.length;
		int c = m[0].length;
		if (m[r - 1][c - 1] == 1) {
			right[r - 1][c - 1] = 1;
			down[r - 1][c - 1] = 1;
		}
		/*遍历最右一列*/
		for (int i = r - 2; i != -1; i--) {
			/*有1才计算，否则可以直接跳过，默认就是0*/
			if (m[i][c - 1] == 1) {
				right[i][c - 1] = 1;
				down[i][c - 1] = down[i + 1][c - 1] + 1;
			}
		}
		/*最下边一行*/
		for (int i = c - 2; i != -1; i--) {
			if (m[r - 1][i] == 1) {
				right[r - 1][i] = right[r - 1][i + 1] + 1;
				down[r - 1][i] = 1;
			}
		}
		for (int i = r - 2; i != -1; i--) {
			for (int j = c - 2; j != -1; j--) {
				if (m[i][j] == 1) {
					right[i][j] = right[i][j + 1] + 1;
					down[i][j] = down[i + 1][j] + 1;
				}
			}
		}
	}

	public static boolean hasSizeOfBorder(int size, int[][] right, int[][] down) {
		for (int i = 0; i != right.length - size + 1; i++) {
			for (int j = 0; j != right[0].length - size + 1; j++) {
				if (right[i][j] >= size && down[i][j] >= size && right[i + size - 1][j] >= size
						&& down[i][j + size - 1] >= size) {
					return true;
				}
			}
		}
		return false;
	}

}
