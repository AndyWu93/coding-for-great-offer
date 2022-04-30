package class18;

/**
 * 给定一个数组arr，长度为N，arr中的值只有1，2，3三种
 * arr[i] == 1，代表汉诺塔问题中，从上往下第i个圆盘目前在左
 * arr[i] == 2，代表汉诺塔问题中，从上往下第i个圆盘目前在中
 * arr[i] == 3，代表汉诺塔问题中，从上往下第i个圆盘目前在右
 * 那么arr整体就代表汉诺塔游戏过程中的一个状况，如果这个状况不是汉诺塔最优解运动过程中的状况，返回-1
 * 如果这个状况是汉诺塔最优解运动过程中的状态，返回它是第几个状态
 *
 * 解题：
 * 	本题考查递归的设计，参考体系班汉诺塔问题
 */
public class Code01_HanoiProblem {

	/**
	 * 课上版本
	 */
	public static int kth(int[] arr) {
		int N = arr.length;
		/*
		 * N-1:从最底层开始看起
		 * from：按题意用1表示
		 * to：按题意用3表示
		 * other：按题意用2表示
		 * */
		return step(arr, N - 1, 1, 3, 2);
	}

	/*
	 * arr: n层圆盘各自的位置，从0开始
	 * arr[0]=1: 第0层圆盘在左边，arr[5]=3:第5层圆盘在右边
	 * index：需要移动的圆盘：0~index
	 * 怎么移动：
	 * 	在哪？from 去哪？to 另一个是啥？other
	 * 返回：根据arr[0~index]的位置，判定如果是最优解，应该是第几步
	 *
	 * 复杂度：
	 * 递归只走了一侧，所以index从0~N-1，走了一遍arr，复杂度O(N)
	 * */
	public static int step(int[] arr, int index, int from, int to, int other) {
		if (index == -1) {
			/*圆盘是从最底层往前看，如果都看完了，表示当前在第0步*/
			return 0;
		}
		if (arr[index] == other) {
			/*
			 * 该方法永远是要将第index的圆盘从from移动到to去，所以要么在from，要么已经移动到to了，
			 * 如果在other一定是一个错误的状态
			 * */
			return -1;
		}
		// arr[index] == from arr[index] == to;
		if (arr[index] == from) {
			/*如果在from，表示还没到第index的盘子，还在处理之前的盘子，那就看看之前的盘子的状态在第几步*/
			return step(arr, index - 1, from, other, to);
		} else {
			/*
			 * 如果在to了，如果是最优解，
			 * 那么之前0~index-1的盘子移动了other，走了2^index-1步
			 * index从from到to算1步，
			 * 接下来看下index-1的位置，判断其是走到了第几步了
			 * 3个加起来，就是目前所有盘子的状态在第几步
			 * */
			int p1 = (1 << index) - 1;
			int p2 = 1;
			int p3 = step(arr, index - 1, other, to, from);
			if (p3 == -1) {
				/*这里表示index-1的盘子，在错误的位置上*/
				return -1;
			}
			return p1 + p2 + p3;
		}
	}

	public static int step1(int[] arr) {
		if (arr == null || arr.length == 0) {
			return -1;
		}
		return process(arr, arr.length - 1, 1, 2, 3);
	}

	// 目标是: 把0~i的圆盘，从from全部挪到to上
	// 返回，根据arr中的状态arr[0..i]，它是最优解的第几步？
	// f(i, 3 , 2, 1) f(i, 1, 3, 2) f(i, 3, 1, 2)
	public static int process(int[] arr, int i, int from, int other, int to) {
		if (i == -1) {
			return 0;
		}
		if (arr[i] != from && arr[i] != to) {
			return -1;
		}
		if (arr[i] == from) { // 第一大步没走完
			return process(arr, i - 1, from, to, other);
		} else { // arr[i] == to
			// 已经走完1，2两步了，
			int rest = process(arr, i - 1, other, from, to); // 第三大步完成的程度
			if (rest == -1) {
				return -1;
			}
			return (1 << i) + rest;
		}
	}

	/**
	 * 递归改迭代
	 */
	public static int step2(int[] arr) {
		if (arr == null || arr.length == 0) {
			return -1;
		}
		int from = 1;
		int mid = 2;
		int to = 3;
		int i = arr.length - 1;
		int res = 0;
		int tmp = 0;
		while (i >= 0) {
			if (arr[i] != from && arr[i] != to) {
				return -1;
			}
			if (arr[i] == to) {
				res += 1 << i;
				tmp = from;
				from = mid;
			} else {
				tmp = to;
				to = mid;
			}
			mid = tmp;
			i--;
		}
		return res;
	}


	public static void main(String[] args) {
		int[] arr = { 3, 3, 2, 1 };
		System.out.println(step1(arr));
		System.out.println(step2(arr));
		System.out.println(kth(arr));
	}
}
