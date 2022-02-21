package class28;

/**
 * 罗马转数字
 */
public class Problem_0013_RomanToInteger {

	public static int romanToInt(String s) {
		/*第一步，将每一个罗马字符转成数字，形成一个数组*/
		// C     M     X   C     I   X
		// 100  1000  10   100   1   10
		int nums[] = new int[s.length()];
		for (int i = 0; i < s.length(); i++) {
			switch (s.charAt(i)) {
			case 'M':
				nums[i] = 1000;
				break;
			case 'D':
				nums[i] = 500;
				break;
			case 'C':
				nums[i] = 100;
				break;
			case 'L':
				nums[i] = 50;
				break;
			case 'X':
				nums[i] = 10;
				break;
			case 'V':
				nums[i] = 5;
				break;
			case 'I':
				nums[i] = 1;
				break;
			}
		}
		int sum = 0;
		for (int i = 0; i < nums.length - 1; i++) {
			if (nums[i] < nums[i + 1]) {
				/*i位置的数如果比i+1位置的数小，那i位置的数就代表负数*/
				sum -= nums[i];
			} else {
				/*i位置的数如果比i+1位置的数大或者相等，那i位置的数就代表正数*/
				sum += nums[i];
			}
		}
		/*最后一位数一定是正的*/
		return sum + nums[nums.length - 1];
	}

}
