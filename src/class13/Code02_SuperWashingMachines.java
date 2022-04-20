package class13;

/**
 * 阿里高频面试题
 * 假设有 n 台超级洗衣机放在同一排上。开始的时候，每台洗衣机内可能有一定量的衣服，也可能是空的
 * 在每一步操作中，你可以选择任意 m （1 ≤ m ≤ n） 台洗衣机，与此同时将每台洗衣机的一件衣服送到相邻的一台洗衣机
 * 给定一个非负整数数组代表从左至右每台洗衣机中的衣物数量，请给出能让所有洗衣机中剩下的衣物的数量相等的最少的操作步数
 * 如果不能使每台洗衣机中衣物的数量相等，则返回-1
 *
 * 解题：
 * 	本题需要注意，在平均洗衣机里的衣服时，是所有的洗衣机同时工作的
 * 	本题贪心解法
 * 	分析普通位置i
 * 	1）i左侧所有的洗衣机的衣服加起来欠10件，i右侧所有的洗衣机的衣服加起来富余15件 -> i位置需要动15轮
 * 	2）i左侧所有的洗衣机的衣服加起来富余10件，i右侧所有的洗衣机的衣服加起来欠15件 -> i位置需要动15轮
 * 	3）i左侧所有的洗衣机的衣服加起来富余10件，i右侧所有的洗衣机的衣服加起来富余15件 -> i位置需要动15轮
 * 	4）i左侧所有的洗衣机的衣服加起来欠10件，i右侧所有的洗衣机的衣服加起来欠15件 -> i位置需要动25轮
 * 求出每个位置的值，取最大值就是题解
 */
// 本题测试链接 : https://leetcode.com/problems/super-washing-machines/
public class Code02_SuperWashingMachines {

	public static int findMinMoves(int[] arr) {
		if (arr == null || arr.length == 0) {
			return 0;
		}
		int size = arr.length;
		int sum = 0;
		for (int i = 0; i < size; i++) {
			sum += arr[i];
		}
		if (sum % size != 0) {
			/*不能平均分，那怎么动都无法实现*/
			return -1;
		}
		int avg = sum / size;
		int leftSum = 0;
		int ans = 0;
		for (int i = 0; i < arr.length; i++) {
			/*左边的情况*/
			int leftRest = leftSum - i * avg;
			/*右边的情况*/
			int rightRest = (sum - leftSum - arr[i]) - (size - i - 1) * avg;
			if (leftRest < 0 && rightRest < 0) {
				/*只有左边右边都是欠才求和*/
				ans = Math.max(ans, Math.abs(leftRest) + Math.abs(rightRest));
			} else {
				/*否则，左右取较大的*/
				ans = Math.max(ans, Math.max(Math.abs(leftRest), Math.abs(rightRest)));
			}
			leftSum += arr[i];
		}
		return ans;
	}

}
