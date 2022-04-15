package class09;

import java.util.HashMap;

/**
 * 近期面试题
 * 定义何为step sum？比如680，680 + 68 + 6 = 754，680的step sum叫754。给定一个正数num，判断它是不是某个数的step sum
 *
 * 解题：
 * 	和求平方根一样，利用单调性的特点，在1~num二分，找到那个步骤和恰好等于num的数
 * 	复杂度：log(2,x)（二分）  *   log(10,x)（计算步骤和）
 */
public class Code05_IsStepSum {

	public static boolean isStepSum(int stepSum) {
		int L = 0;
		int R = stepSum;
		int M = 0;
		int cur = 0;
		while (L <= R) {
			M = L + ((R - L) >> 1);
			cur = stepSum(M);
			if (cur == stepSum) {
				return true;
			} else if (cur < stepSum) {
				L = M + 1;
			} else {
				R = M - 1;
			}
		}
		return false;
	}

	public static int stepSum(int num) {
		int sum = 0;
		while (num != 0) {
			sum += num;
			num /= 10;
		}
		return sum;
	}

	// for test
	public static HashMap<Integer, Integer> generateStepSumNumberMap(int numMax) {
		HashMap<Integer, Integer> map = new HashMap<>();
		for (int i = 0; i <= numMax; i++) {
			map.put(stepSum(i), i);
		}
		return map;
	}

	// for test
	public static void main(String[] args) {
		int max = 1000000;
		int maxStepSum = stepSum(max);
		HashMap<Integer, Integer> ans = generateStepSumNumberMap(max);
		System.out.println("测试开始");
		for (int i = 0; i <= maxStepSum; i++) {
			if (isStepSum(i) ^ ans.containsKey(i)) {
				System.out.println("出错了！");
			}
		}
		System.out.println("测试结束");
	}

}
