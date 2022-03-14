package class32;

import java.util.HashSet;
import java.util.TreeSet;

/**
 * Write an algorithm to determine if a number n is happy.
 *
 * A happy number is a number defined by the following process:
 *
 * Starting with any positive integer, replace the number by the sum of the squares of its digits.
 * Repeat the process until the number equals 1 (where it will stay), or it loops endlessly in a cycle which does not include 1.
 * Those numbers for which this process ends in 1 are happy.
 * Return true if n is a happy number, and false if not.
 * 题意：判断一个数是否是快乐数
 * 解题：
 * 	本题纯数学，任何一个数再拆解相加的过程中，一定会遇到4或者1，遇到4就会一直循环下去
 */
public class Problem_0202_HappyNumber {

	public static boolean isHappy1(int n) {
		HashSet<Integer> set = new HashSet<>();
		while (n != 1) {
			int sum = 0;
			while (n != 0) {
				int r = n % 10;
				sum += r * r;
				n /= 10;
			}
			n = sum;
			if (set.contains(n)) {
				break;
			}
			set.add(n);
		}
		return n == 1;
	}

	// 实验代码
	public static TreeSet<Integer> sum(int n) {
		TreeSet<Integer> set = new TreeSet<>();
		while (!set.contains(n)) {
			set.add(n);
			int sum = 0;
			while (n != 0) {
				sum += (n % 10) * (n % 10);
				n /= 10;
			}
			n = sum;
		}
		return set;
	}

	public static boolean isHappy2(int n) {
		while (n != 1 && n != 4) {
			/*sum就是n每个位上的数的平方，相加，怎么求？从个位到高位依次求*/
			int sum = 0;
			while (n != 0) {
				/*最右侧上的数的平方相加*/
				sum += (n % 10) * (n % 10);
				/*n消掉最右侧的数*/
				n /= 10;
			}
			/*n一定会遇到1或者4*/
			n = sum;
		}
		/*1就是快乐树*/
		return n == 1;
	}

	public static void main(String[] args) {
		for (int i = 1; i < 1000; i++) {
			System.out.println(sum(i));
		}
	}

}
