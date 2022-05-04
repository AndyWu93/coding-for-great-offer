package class23;

/**
 * 给定一个数组arr，长度为N > 1，从中间切一刀，保证左部分和右部分都有数字，一共有N-1种切法
 * 如此多的切法中，每一种都有:绝对值(左部分最大值 – 右部分最大值)，返回最大的绝对值是多少
 *
 * 解题：
 * 	思路1，枚举刀的位置，左右两边分别遍历取出最大值，相减，复杂度O(N^2)，没分
 * 	思路2，还是枚举刀的位置，在左右两边取最大值的时候，借助辅助数组，O(1)获得，复杂度O(N)，笔试应该能过了
 * 		辅助数组:
 * 		left[i]: 范围0~i上的最大值
 * 		right[i]: 范围i~n-1上的最大值
 * 	思路3，分析题意，左边最大值-右边最大值，不管怎么样，整个arr的max一定会参与计算。
 * 		先把arr中的max找出来，max要么划在左边，要么划在右边。
 * 		假设划在左边，那么就要给右边划一个范围，使得右面的maxR最小，如何能够使得maxR最小呢？
 * 		n-1位置一定逃不掉，除了n-1位置以外，右边的范围越大，maxR只可能升高，不可能降低。所以右边就划一个数
 * 		那么结果就是max-arr[n-1]
 * 		假设划在右边，同理可得max-arr[0]
 * 		两者取最大值，即max-min(arr[n-1],arr[0])
 *
 * 总结：分析并转化题意，将复杂逻辑转化成等价的简单逻辑
 */
public class Code02_MaxABSBetweenLeftAndRight {

	/**
	 * 思路1
	 * O(N^2),没分
	 */
	public static int maxABS1(int[] arr) {
		int res = Integer.MIN_VALUE;
		int maxLeft = 0;
		int maxRight = 0;
		for (int i = 0; i != arr.length - 1; i++) {
			maxLeft = Integer.MIN_VALUE;
			for (int j = 0; j != i + 1; j++) {
				maxLeft = Math.max(arr[j], maxLeft);
			}
			maxRight = Integer.MIN_VALUE;
			for (int j = i + 1; j != arr.length; j++) {
				maxRight = Math.max(arr[j], maxRight);
			}
			res = Math.max(Math.abs(maxLeft - maxRight), res);
		}
		return res;
	}

	/**
	 * 思路2，O(N)
	 */
	public static int maxABS2(int[] arr) {
		int[] lArr = new int[arr.length];
		int[] rArr = new int[arr.length];
		lArr[0] = arr[0];
		rArr[arr.length - 1] = arr[arr.length - 1];
		for (int i = 1; i < arr.length; i++) {
			lArr[i] = Math.max(lArr[i - 1], arr[i]);
		}
		for (int i = arr.length - 2; i > -1; i--) {
			rArr[i] = Math.max(rArr[i + 1], arr[i]);
		}
		/*上面是辅助数组生成*/
		int max = 0;
		/*枚举左边的范围0~i,右边的范围就是i+1~n-1*/
		for (int i = 0; i < arr.length - 1; i++) {
			/*O(1)获得的两个范围内的最大值*/
			max = Math.max(max, Math.abs(lArr[i] - rArr[i + 1]));
		}
		return max;
	}

	/**
	 * 思路3，O(1)
	 */
	public static int maxABS3(int[] arr) {
		int max = Integer.MIN_VALUE;
		for (int i = 0; i < arr.length; i++) {
			max = Math.max(arr[i], max);
		}
		return max - Math.min(arr[0], arr[arr.length - 1]);
	}

	// for test
	public static int[] generateRandomArray(int length) {
		int[] arr = new int[length];
		for (int i = 0; i != arr.length; i++) {
			arr[i] = (int) (Math.random() * 1000) - 499;
		}
		return arr;
	}

	public static void main(String[] args) {
		int[] arr = generateRandomArray(200);
		System.out.println(maxABS1(arr));
		System.out.println(maxABS2(arr));
		System.out.println(maxABS3(arr));
	}
}
