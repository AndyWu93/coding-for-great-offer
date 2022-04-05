package class04;

/**
 * 生成长度为size的达标数组
 * 达标：对于任意的 i<k<j，满足   [i] + [j]  != [k] * 2
 *
 * 解题：可以用分治下的递归思维
 * 	要生成size长度的达标数组arr，先拿到size/2的达标数组arrH
 * 	再将arrH变成两个arrH2，左边为arrH求奇数，右边为arrH求偶数
 * 	求奇数：2*arrH[i]+1
 * 	求偶数：2*arrH[i]
 *
 * 	为何可以这么做？
 * 	原来达标的数组，乘2，或者乘2+1后依然达标
 * 	奇数和偶数再拼接再一起以后，一个奇数和一个偶数加起来不可能是某个数的两倍
 *
 */
public class Code06_MakeNo {


	public static int[] makeNo(int size) {
		if (size == 1) {
			return new int[] { 1 };
		}
		/*
		* 一半长达标怎么计算？
		* 7   :   4
		* 8   :   4
		* [4个奇数] [3个偶]
		* */
		int halfSize = (size + 1) / 2;
		int[] base = makeNo(halfSize);
		// base -> 等长奇数达标来
		// base -> 等长偶数达标来
		int[] ans = new int[size];
		int index = 0;
		for(; index < halfSize;index++) {
			ans[index] = base[index] * 2 - 1;
		}
		for(int i = 0 ;index < size;index++,i++) {
			ans[index] = base[i] * 2; 
		}
		return ans;
	}

	// 检验函数
	public static boolean isValid(int[] arr) {
		int N = arr.length;
		for (int i = 0; i < N; i++) {
			for (int k = i + 1; k < N; k++) {
				for (int j = k + 1; j < N; j++) {
					if (arr[i] + arr[j] == 2 * arr[k]) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public static void main(String[] args) {
		System.out.println("test begin");
		for (int N = 1; N < 1000; N++) {
			int[] arr = makeNo(N);
			if (!isValid(arr)) {
				System.out.println("Oops!");
			}
		}
		System.out.println("test end");
		System.out.println(isValid(makeNo(1042)));
		System.out.println(isValid(makeNo(2981)));
	}

}
