package class33;

/**
 * 完成一个二维数组的迭代器
 * 	int next();
 * 	boolean hasNext();
 * 解题：该题考查代码设计能力
 * 	要注意该二维数组存在以下两种情况：
 * 		1. 每行的长度不一样
 * 		2. 可能有某些行的长度为0
 *
 */
public class Problem_0251_Flatten2DVector {

	public static class Vector2D {
		private int[][] matrix;
		/*
		* (row,col)当前指针的位置，
		* */
		private int row;
		private int col;
		/*指针位置的数有没有被使用过*/
		private boolean curUse;

		public Vector2D(int[][] v) {
			matrix = v;
			/*一开始来到初始位置，并且使用过了*/
			row = 0;
			col = -1;
			curUse = true;
			hasNext();
		}

		public int next() {
			int ans = matrix[row][col];
			/*用过了以后，该位置标true了，调用hasNext移动指针*/
			curUse = true;
			hasNext();
			return ans;
		}

		/**
		 * 来到下一个没有被使用过的位置，并且用curUse标记该位置没有被使用
		 */
		public boolean hasNext() {
			if (row == matrix.length) {
				return false;
			}
			if (!curUse) {
				/*该位置没有被使用，频繁的调用hasNext()不会移动指针*/
				return true;
			}
			// (row，col)用过了
			if (col < matrix[row].length - 1) {
				/*还有下一列*/
				col++;
			} else {
				/*没有下一列了，跳到下一行去*/
				col = 0;
				do {
					row++;
					/*一直到这一行真正有数为止*/
				} while (row < matrix.length && matrix[row].length == 0);
			}
			// 新的(row，col)
			if (row != matrix.length) {
				curUse = false;
				return true;
			} else {
				/*没有下一行了，也就没有数了*/
				return false;
			}
		}

	}

}
