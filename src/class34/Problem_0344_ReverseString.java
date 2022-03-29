package class34;

/**
 * Write a function that reverses a string. The input string is given as an array of characters s.
 *
 * You must do this by modifying the input array in-place with O(1) extra memory.
 */
//https://leetcode.com/problems/reverse-string/
public class Problem_0344_ReverseString {

	public void reverseString(char[] s) {
		int l = 0;
		int r = s.length - 1;
		while (l < r) {
			char tmp = s[l];
			s[l++] = s[r];
			s[r--] = tmp;
		}
	}

}
