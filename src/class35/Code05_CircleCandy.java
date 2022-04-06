package class35;


// 来自网易
// 给定一个正数数组arr，表示每个小朋友的得分
// 任何两个相邻的小朋友，如果得分一样，怎么分糖果无所谓，但如果得分不一样，分数大的一定要比分数少的多拿一些糖果
// 假设所有的小朋友坐成一个环形，返回在不破坏上一条规则的情况下，需要的最少糖果数
/**
 * 如果没有环，小朋友怎么分糖果？
 * 把小朋友的得分看成抛物线，每个峰得到的糖果一定要大于两边的小朋友的糖果
 * 所以考虑用两个辅助数组
 * left[i]:i号小朋友糖的数量，只相对于左边比较
 * right[i]:i号小朋友糖的数量，只相对于右边比较
 * 计算结果：
 * ans[i] = max(left[i],right[i])
 *
 * 有环？
 * 先把最低谷x找到，以x为断点，旋转数组，最右边也拷贝一下x，这样头尾都是最低谷
 * 这样就把环拆成了数组模式
 */
public class Code05_CircleCandy {

	public static int minCandy(int[] arr) {
		if (arr == null || arr.length == 0) {
			return 0;
		}
		if (arr.length == 1) {
			return 1;
		}
		int n = arr.length;
		int minIndex = 0;
		for (int i = 0; i < n; i++) {
			if (arr[i] <= arr[lastIndex(i, n)] && arr[i] <= arr[nextIndex(i, n)]) {
				minIndex = i;
				break;
			}
		}
		int[] nums = new int[n + 1];
		for (int i = 0; i <= n; i++, minIndex = nextIndex(minIndex, n)) {
			nums[i] = arr[minIndex];
		}
		int[] left = new int[n + 1];
		left[0] = 1;
		for (int i = 1; i <= n; i++) {
			left[i] = nums[i] > nums[i - 1] ? (left[i - 1] + 1) : 1;
		}
		int[] right = new int[n + 1];
		right[n] = 1;
		for (int i = n - 1; i >= 0; i--) {
			right[i] = nums[i] > nums[i + 1] ? (right[i + 1] + 1) : 1;
		}
		int ans = 0;
		for (int i = 0; i < n; i++) {
			ans += Math.max(left[i], right[i]);
		}
		return ans;
	}

	public static int nextIndex(int i, int n) {
		return i == n - 1 ? 0 : (i + 1);
	}

	public static int lastIndex(int i, int n) {
		return i == 0 ? (n - 1) : (i - 1);
	}

	public static void main(String[] args) {
		int[] arr = { 3, 4, 2, 3, 2 };
		System.out.println(minCandy(arr));
	}

}
