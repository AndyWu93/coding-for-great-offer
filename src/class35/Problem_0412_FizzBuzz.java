package class35;

import java.util.ArrayList;
import java.util.List;

/**
 * Given an integer n, return a string array answer (1-indexed) where:
 *
 * answer[i] == "FizzBuzz" if i is divisible by 3 and 5.
 * answer[i] == "Fizz" if i is divisible by 3.
 * answer[i] == "Buzz" if i is divisible by 5.
 * answer[i] == i (as a string) if none of the above conditions are true.
 * 水题，逗你玩的
 */
public class Problem_0412_FizzBuzz {

	public static List<String> fizzBuzz(int n) {
		ArrayList<String> ans = new ArrayList<>();
		for (int i = 1; i <= n; i++) {
			if (i % 15 == 0) {
				ans.add("FizzBuzz");
			} else if (i % 5 == 0) {
				ans.add("Buzz");
			} else if (i % 3 == 0) {
				ans.add("Fizz");
			} else {
				ans.add(String.valueOf(i));
			}
		}
		return ans;
	}

}
