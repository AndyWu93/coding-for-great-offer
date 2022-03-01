package class29;

/**
 * Given an m x n integer matrix matrix, if an element is 0, set its entire row and column to 0's.
 *
 * You must do it in place.
 * 题意：将0所在位置的整行整列变成0
 * 解题：
 * 	准备两个数组
 * 	boolean[] row      row[i]=true 第i行都要变成0
 * 	boolean[] col      col[i]=true 第i列都要变成0
 * 	遍历matrix，填好这两个数组，再遍历一遍matrix，改值
 *
 * 进阶：
 * 	如何省空间？
 * 		1.用matrix的
 * 		  0行存储该列是否要变成0
 * 		  0列存储该行是否要变成0
 * 		  此时需要两个变量，记录0行0列是否需要变成0
 *
 */
public class Problem_0073_SetMatrixZeroes {

	public static void setZeroes1(int[][] matrix) {
		boolean row0Zero = false;
		boolean col0Zero = false;
		int i = 0;
		int j = 0;
		for (i = 0; i < matrix[0].length; i++) {
			if (matrix[0][i] == 0) {
				/*仅遍历0行，如果存在0，那0行最后都要改0*/
				row0Zero = true;
				break;
			}
		}
		for (i = 0; i < matrix.length; i++) {
			if (matrix[i][0] == 0) {
				/*仅遍历0列，如果存在0，那0列最后都要改0*/
				col0Zero = true;
				break;
			}
		}
		/*从1行1列开始遍历matrix*/
		for (i = 1; i < matrix.length; i++) {
			for (j = 1; j < matrix[0].length; j++) {
				if (matrix[i][j] == 0) {
					/*填充0行0列的值*/
					matrix[i][0] = 0;
					matrix[0][j] = 0;
				}
			}
		}
		/*从1行1列开始遍历，改值*/
		for (i = 1; i < matrix.length; i++) {
			for (j = 1; j < matrix[0].length; j++) {
				if (matrix[i][0] == 0 || matrix[0][j] == 0) {
					matrix[i][j] = 0;
				}
			}
		}
		/*单独处理0行*/
		if (row0Zero) {
			for (i = 0; i < matrix[0].length; i++) {
				matrix[0][i] = 0;
			}
		}
		/*单独处理0列*/
		if (col0Zero) {
			for (i = 0; i < matrix.length; i++) {
				matrix[i][0] = 0;
			}
		}
	}

	/**
	 * 能否再省空间？
	 * matrix[0][1..n-1]:仅表示该列要不要变成0
	 * matrix[1..m-1][0]:仅表示该行要不要变成0
	 * matrix[0][0]:表示第0行要不要变成0
	 * boolean col0：表示第0列要不要变成0
	 */
	public static void setZeroes2(int[][] matrix) {
		boolean col0 = false;
		int i = 0;
		int j = 0;
		for (i = 0; i < matrix.length; i++) {
			for (j = 0; j < matrix[0].length; j++) {
				if (matrix[i][j] == 0) {
					/*第i行要变成0，包括第0行*/
					matrix[i][0] = 0;
					if (j == 0) {
						/*第0列单独变量保存*/
						col0 = true;
					} else {
						/*第j列要变成0，不包括第0列*/
						matrix[0][j] = 0;
					}
				}
			}
		}
		/*改值，0列的值不用改*/
		for (i = matrix.length - 1; i >= 0; i--) {
			for (j = 1; j < matrix[0].length; j++) {
				if (matrix[i][0] == 0 || matrix[0][j] == 0) {
					matrix[i][j] = 0;
				}
			}
		}
		/*0列的值单独该*/
		if (col0) {
			for (i = 0; i < matrix.length; i++) {
				matrix[i][0] = 0;
			}
		}
	}

}
