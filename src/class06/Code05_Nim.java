package class06;

/**
 * Nim博弈
 * 给定一个正数数组arr(可以理解为每个位置都是一块钱叠起来的高度)，
 * 先手和后手每次可以选择在一个位置拿走若干值（拿走1块钱或者几块钱，每次只能在1个位置拿），这个值要大于0，但是要小于该处的剩余，
 * 谁最先拿空arr谁赢，根据arr返回谁赢
 *
 * 思路：
 * 赢的定义：拿光所有的数，可以转为让对方无数可拿，也就是让对方面对着一个都是0的arr
 * 如果每次都能让对方面对一个潜在的都是0的arr，就一定能赢。怎么确定？一个数组如果所有的数异或和是0，那就是潜在的都是0的arr
 */
public class Code05_Nim {

	// 保证arr是正数数组
	public static void printWinner(int[] arr) {
		int eor = 0;
		for (int num : arr) {
			eor ^= num;
		}
		if (eor == 0) {
			/*先手面对着一个潜在的都是0的arr*/
			System.out.println("后手赢");
		} else {
			System.out.println("先手赢");
		}
	}

}
