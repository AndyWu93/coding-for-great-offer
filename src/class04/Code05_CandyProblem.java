package class04;

/**
 *
 * 老师想给孩子们分发糖果，有N个孩子站成了一条直线，老师会根据每个孩子的表现，预先给他们评分
 * 你需要按照以下要求，帮助老师给这些孩子分发糖果：
 * 每个孩子至少分配到 1 个糖果。
 * 评分更高的孩子必须比他两侧的邻位孩子获得更多的糖果。
 * 那么这样下来，返回老师至少需要准备多少颗糖果
 *
 * 进阶：在原来要求的基础上，增加一个要求，相邻的孩子间如果分数一样，分的糖果数必须一样，返回至少需要准备多少颗糖果
 *
 * 解题：
 * 	把小朋友的得分看成抛物线，每个峰得到的糖果一定要大于两边的小朋友的糖果
 * 	所以考虑用两个辅助数组
 * 	left[i]:i号小朋友糖的数量，只相对于左边比较
 * 	right[i]:i号小朋友糖的数量，只相对于右边比较
 * 	计算结果：
 * 	ans[i] = max(left[i],right[i])
 *
 * @see class35.Code05_CircleCandy
 */
// 测试链接 : https://leetcode.com/problems/candy/
public class Code05_CandyProblem {

	/**
	 * 这是原问题的优良解
	 * 时间复杂度O(N)，额外空间复杂度O(N)
	 */
	public static int candy1(int[] arr) {
		if (arr == null || arr.length == 0) {
			return 0;
		}
		int N = arr.length;
		/*
		* 这里的两个辅助数组为什么第一个位置是0?
		* 这应该是做了优化：
		* 	因为判断大小时，正常的写法是：
		* 	left[i] = nums[i] > nums[i - 1] ? (left[i - 1] + 1) : 1;
		* 	这里赋值时没有判断
		* */
		int[] left = new int[N];
		for (int i = 1; i < N; i++) {
			if (arr[i - 1] < arr[i]) {
				left[i] = left[i - 1] + 1;
			}
		}
		int[] right = new int[N];
		for (int i = N - 2; i >= 0; i--) {
			if (arr[i] > arr[i + 1]) {
				right[i] = right[i + 1] + 1;
			}
		}
		int ans = 0;
		for (int i = 0; i < N; i++) {
			ans += Math.max(left[i], right[i]);
		}
		return ans + N;
	}

	/**
	 * 优化：辅助数组可不可以省掉？
	 * 可以将arr分为多个坡，一上一下为一个完整的坡
	 * 一个坡里面遍历来回遍历，计算出糖果总数
	 *
	 * 时间复杂度O(N)，额外空间复杂度O(1)
	 */
	public static int candy2(int[] arr) {
		if (arr == null || arr.length == 0) {
			return 0;
		}
		int index = nextMinIndex2(arr, 0);
		int res = rightCands(arr, 0, index++);
		int lbase = 1;
		int next = 0;
		int rcands = 0;
		int rbase = 0;
		while (index != arr.length) {
			if (arr[index] > arr[index - 1]) {
				res += ++lbase;
				index++;
			} else if (arr[index] < arr[index - 1]) {
				next = nextMinIndex2(arr, index - 1);
				rcands = rightCands(arr, index - 1, next++);
				rbase = next - index + 1;
				res += rcands + (rbase > lbase ? -lbase : -rbase);
				lbase = 1;
				index = next;
			} else {
				res += 1;
				lbase = 1;
				index++;
			}
		}
		return res;
	}

	public static int nextMinIndex2(int[] arr, int start) {
		for (int i = start; i != arr.length - 1; i++) {
			if (arr[i] <= arr[i + 1]) {
				return i;
			}
		}
		return arr.length - 1;
	}

	public static int rightCands(int[] arr, int left, int right) {
		int n = right - left + 1;
		return n + n * (n - 1) / 2;
	}


	/**
	 * 进阶：相同得分，分糖一样
	 * 这里做了优化，也可以不优化：
	 * 	辅助数组中，遇到相等的分数，糖果分一样多就行
	 */
	// 这是进阶问题的最优解，不要提交这个
	// 时间复杂度O(N), 额外空间复杂度O(1)
	public static int candy3(int[] arr) {
		if (arr == null || arr.length == 0) {
			return 0;
		}
		int index = nextMinIndex3(arr, 0);
		int[] data = rightCandsAndBase(arr, 0, index++);
		int res = data[0];
		int lbase = 1;
		int same = 1;
		int next = 0;
		while (index != arr.length) {
			if (arr[index] > arr[index - 1]) {
				res += ++lbase;
				same = 1;
				index++;
			} else if (arr[index] < arr[index - 1]) {
				next = nextMinIndex3(arr, index - 1);
				data = rightCandsAndBase(arr, index - 1, next++);
				if (data[1] <= lbase) {
					res += data[0] - data[1];
				} else {
					res += -lbase * same + data[0] - data[1] + data[1] * same;
				}
				index = next;
				lbase = 1;
				same = 1;
			} else {
				res += lbase;
				same++;
				index++;
			}
		}
		return res;
	}

	public static int nextMinIndex3(int[] arr, int start) {
		for (int i = start; i != arr.length - 1; i++) {
			if (arr[i] < arr[i + 1]) {
				return i;
			}
		}
		return arr.length - 1;
	}

	public static int[] rightCandsAndBase(int[] arr, int left, int right) {
		int base = 1;
		int cands = 1;
		for (int i = right - 1; i >= left; i--) {
			if (arr[i] == arr[i + 1]) {
				cands += base;
			} else {
				cands += ++base;
			}
		}
		return new int[] { cands, base };
	}

	public static void main(String[] args) {
		int[] test1 = { 3, 0, 5, 5, 4, 4, 0 };
		System.out.println(candy2(test1));

		int[] test2 = { 3, 0, 5, 5, 4, 4, 0 };
		System.out.println(candy3(test2));
	}

}
