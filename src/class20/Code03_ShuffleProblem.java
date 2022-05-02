package class20;

import java.util.Arrays;

/**
 * 完美洗牌问题
 * 给定一个长度为偶数的数组arr，假设长度为N*2
 * 左部分：arr[L1...Ln] 右部分： arr[R1...Rn]
 * 请把arr调整成arr[L1,R1,L2,R2,L3,R3,...,Ln,Rn]
 * 要求时间复杂度O(N)，额外空间复杂度O(1)
 *
 * 解题：
 * 	完美洗牌，观察洗牌前后的下标变换，可以知道，一定有一个公式存在，能够通过之前的下标计算出洗牌后的下标
 * 	公式还是比较简单的，左右两个部分的下标分开计算。
 * 	有了公式，可以通过下标循环推的方法，将所有的数都放到应该放的位置。但是下标循环推有可能不会覆盖到整个arr
 * 	经过数学验证，一个长度为3^K-1（如：2,8,26,...）的arr，其中会出现多个环，每个环的开始位置(下标从1开始)分别为1,3,9,...,3^(k-1)
 *	所以
 *	拿到一个长度为len的arr，可以将arr切割成多个	长度为3^K-1的arr 分别调用下标循环推
 *	如何切割？
 *	举例：
 * 	L1,L2,L3,L4,L5,L6, R1,R2,R3,R4,R5,R6
 * 	先找到最大的符合完美洗牌的长度：8，所以L1,L2,L3,L4,R1,R2,R3,R4这部分，也就是将L5,L6,R5,R6抽掉，剩下的部分可以调用下标循环推
 * 	那么就将L5~R4的子数组，沿着中心旋转一下，R1,R2,R3,R4转到前面去，L5,L6转到后面来
 * 	剩下的L5,L6,R5,R6，继续分段处理
 *
 * @see class34.Problem_0324_WiggleSortII
 */
public class Code03_ShuffleProblem {

	// 数组的长度为len，调整前的位置是i，返回调整之后的位置
	// 下标不从0开始，从1开始
	public static int modifyIndex1(int i, int len) {
		if (i <= len / 2) {
			return 2 * i;
		} else {
			return 2 * (i - (len / 2)) - 1;
		}
	}

	// 数组的长度为len，调整前的位置是i，返回调整之后的位置
	// 下标不从0开始，从1开始
	public static int modifyIndex2(int i, int len) {
		return (2 * i) % (len + 1);
	}

	/**
	 * 完美洗牌
	 * 复杂度：
	 * 找k：O(log(3,n)) -> 这个复杂度每一轮是固定的
	 * 旋转、下标循环推：O(len) -> 这个复杂度len递减的速度很快，一般以等比数列递减
	 * 把这些每一轮都加起来
	 * O(log(3,n)) 还是常数时间小于O(N)，
	 * O(len) 等比数列，len最大是N，所以O(N)
	 * 所有的加起来还是O(N)
	 */
	// 主函数
	// 数组必须不为空，且长度为偶数
	public static void shuffle(int[] arr) {
		if (arr != null && arr.length != 0 && (arr.length & 1) == 0) {
			shuffle(arr, 0, arr.length - 1);
		}
	}

	// 在arr[L..R]上做完美洗牌的调整（arr[L..R]范围上一定要是偶数个数字）
	public static void shuffle(int[] arr, int L, int R) {
		/*等待处理的范围L,R，长度为R - L + 1，这个长度一定要大于0*/
		while (R - L + 1 > 0) { // 切成一块一块的解决，每一块的长度满足(3^k)-1
			int len = R - L + 1;
			/*找到一个最大的符合 3^K-1 的长度*/
			int base = 3;
			int k = 1;
			// 计算小于等于len并且是离len最近的，满足(3^k)-1的数
			// 也就是找到最大的k，满足3^k <= len+1
			/*这里(len + 1)为什么要除3，因为跳出循环时，base>(len+1)/3，但base<=len+1*/
			while (base <= (len + 1) / 3) {
				base *= 3;
				k++;
			}
			// 3^k -1
			/*
			* 当前要解决长度为base-1的块。一半就是再除2。
			* 定位出每个half在arr左右两个部分的位置，
			* 第一个half就在arr的左半边不用处理
			* 第二个half要转的过来，把arr左边除了half以外的转过去
			* */
			int half = (base - 1) / 2;
			// [L..R]的中点位置
			int mid = (L + R) / 2;
			// 要旋转的左部分为[L+half...mid], 右部分为arr[mid+1..mid+half]
			// 注意在这里，arr下标是从0开始的
			rotate(arr, L + half, mid, mid + half);
			// 旋转完成后，从L开始算起，长度为base-1的部分进行下标连续推
			cycles(arr, L, base - 1, k);
			// 解决了前base-1的部分，剩下的部分继续处理
			L = L + base - 1; // L ->     [] [+1...R]
		}
	}

	// 从start位置开始，往右len的长度这一段，做下标连续推
	// 出发位置依次为1,3,9...
	public static void cycles(int[] arr, int start, int len, int k) {
		// 找到每一个出发位置trigger，一共k个
		// 每一个trigger都进行下标连续推
		// 出发位置是从1开始算的，而数组下标是从0开始算的。
		for (int i = 0, trigger = 1; i < k; i++, trigger *= 3) {
			/*找到出发位置，-1是因为trigger从1开始算，而应该要从0开始算*/
			int preValue = arr[trigger + start - 1];
			/*通过trigger算出应该要到哪个位置*/
			int cur = modifyIndex2(trigger, len);
			/*下边循环推*/
			while (cur != trigger) {
				/*要加上start*/
				int tmp = arr[cur + start - 1];
				arr[cur + start - 1] = preValue;
				preValue = tmp;
				cur = modifyIndex2(cur, len);
			}
			/*跳出while，cur来到了trigger位置，赋值后就结束*/
			arr[cur + start - 1] = preValue;
		}
	}

	// [L..M]为左部分，[M+1..R]为右部分，左右两部分互换
	public static void rotate(int[] arr, int L, int M, int R) {
		reverse(arr, L, M);
		reverse(arr, M + 1, R);
		reverse(arr, L, R);
	}

	// [L..R]做逆序调整
	public static void reverse(int[] arr, int L, int R) {
		while (L < R) {
			int tmp = arr[L];
			arr[L++] = arr[R];
			arr[R--] = tmp;
		}
	}

	public static void wiggleSort(int[] arr) {
		if (arr == null || arr.length == 0) {
			return;
		}
		// 假设这个排序是额外空间复杂度O(1)的，当然系统提供的排序并不是，你可以自己实现一个堆排序
		Arrays.sort(arr);
		if ((arr.length & 1) == 1) {
			shuffle(arr, 1, arr.length - 1);
		} else {
			shuffle(arr, 0, arr.length - 1);

			for (int i = 0; i < arr.length; i += 2) {
				int tmp = arr[i];
				arr[i] = arr[i + 1];
				arr[i + 1] = tmp;
			}
		}
	}

	// for test
	public static boolean isValidWiggle(int[] arr) {
		for (int i = 1; i < arr.length; i++) {
			if ((i & 1) == 1 && arr[i] < arr[i - 1]) {
				return false;
			}
			if ((i & 1) == 0 && arr[i] > arr[i - 1]) {
				return false;
			}
		}
		return true;
	}

	// for test
	public static void printArray(int[] arr) {
		for (int i = 0; i < arr.length; i++) {
			System.out.print(arr[i] + " ");
		}
		System.out.println();
	}

	// for test
	public static int[] generateArray() {
		int len = (int) (Math.random() * 10) * 2;
		int[] arr = new int[len];
		for (int i = 0; i < len; i++) {
			arr[i] = (int) (Math.random() * 100);
		}
		return arr;
	}

	public static void main(String[] args) {
		for (int i = 0; i < 5000000; i++) {
			int[] arr = generateArray();
			wiggleSort(arr);
			if (!isValidWiggle(arr)) {
				System.out.println("ooops!");
				printArray(arr);
				break;
			}
		}
	}

}
