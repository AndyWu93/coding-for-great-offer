package class25;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *An IP address is a formatted 32-bit unsigned integer where each group of 8 bits is printed as a decimal number and the dot character '.' splits the groups.
 * For example, the binary number 00001111 10001000 11111111 01101011 (spaces added for clarity) formatted as an IP address would be "15.136.255.107".
 * A CIDR block is a format used to denote a specific set of IP addresses. It is a string consisting of a base IP address, followed by a slash, followed by a prefix length k.
 * The addresses it covers are all the IPs whose first k bits are the same as the base IP address.
 * For example, "123.45.67.89/20" is a CIDR block with a prefix length of 20. Any IP address whose binary representation matches
 * 01111011 00101101 0100xxxx xxxxxxxx, where x can be either 0 or 1, is in the set covered by the CIDR block.
 * You are given a start IP address ip and the number of IP addresses we need to cover n.
 * Your goal is to use as few CIDR blocks as possible to cover all the IP addresses in the inclusive range [ip, ip + n - 1] exactly
 * No other IP addresses outside of the range should be covered.
 * Return the shortest list of CIDR blocks that covers the range of IP addresses. If there are multiple answers, return any of them.
 *
 * 解题：
 * 	本题题意较难，因为是会员题，没能够拿到example
 */
// 本题测试链接 : https://leetcode.com/problems/ip-to-cidr/
public class Code01_IPToCIDR {

	public static List<String> ipToCIDR(String ip, int n) {
		/*
		* 将ip(如127.11.21.2)转成int
		* */
		int cur = status(ip);
		// cur这个状态，最右侧的1，能表示下2的几次方
		/*
		* 拿到了cur，看下最右侧的1是2的几次方
		* */
		int maxPower = 0;
		// 临时变量，已经解决了多少ip了
		int solved = 0;
		// 临时变量
		int power = 0;
		List<String> ans = new ArrayList<>();
		while (n > 0) {
			// cur最右侧的1，能搞定2的几次方个ip
			// cur : 000...000 01001000
			// 3
			maxPower = mostRightPower(cur);
			// cur : 0000....0000 00001000 -> cur现有最右侧的1，能搞定2的3次方的ip
			// sol : 0000....0000 00000001 -> 1 2的0次方
			// sol : 0000....0000 00000010 -> 2 2的1次方
			// sol : 0000....0000 00000100 -> 4 2的2次方
			// sol : 0000....0000 00001000 -> 8 2的3次方
			solved = 1;
			power = 0;
			// 怕溢出
			// solved
			/*
			* (power + 1) <= maxPower: power不能大于maxPower
			* (solved << 1) <= n：power增加了，解决的ip数量增加了
			* */
			while ((solved << 1) <= n && (power + 1) <= maxPower) {
				/*
				* 因为power++了，表示假想的最右侧的1（从最低位开始的）左移了，那么能够搞定的ip数量增加了
				* 左移1位，右侧多出来1个0，这个0可以随意变，能够生产的ip数量给sol，所以sol左移
				* */
				solved <<= 1;
				power++;
			}
			/*将目前的cur，以及用掉的最右侧的1，生产ip*/
			ans.add(content(cur, power));
			/*减掉已经生成的sol，这时候n可能为0，如果为0，下一次直接进不来了*/
			n -= solved;
			/*如果n还有剩余，表示cur目前最右侧的1不够用，所以cur最右侧的1要升，正好加上solved就可以升了*/
			cur += solved;
		}
		return ans;
	}

	// ip -> int(32位状态)
	public static int status(String ip) {
		int ans = 0;
		int move = 24;
		for (String str : ip.split("\\.")) {
			// 17.23.16.5 "17" "23" "16" "5"
			// "17" -> 17 << 24
			// "23" -> 23 << 16
			// "16" -> 16 << 8
			// "5" -> 5 << 0
			/*
			* 从ip的高位到低位，一次转成int后，左移，或起来
			* */
			ans |= Integer.valueOf(str) << move;
			move -= 8;
		}
		return ans;
	}

	public static HashMap<Integer, Integer> map = new HashMap<>();
	// 1 000000....000000 -> 2的32次方

	public static int mostRightPower(int num) {
		// map只会生成1次，以后直接用
		if (map.isEmpty()) {
			map.put(0, 32);
			for (int i = 0; i < 32; i++) {
				// 00...0000 00000001 2的0次方
				// 00...0000 00000010 2的1次方
				// 00...0000 00000100 2的2次方
				// 00...0000 00001000 2的3次方
				map.put(1 << i, i);
			}
		}
		// num & (-num) -> num & (~num+1) -> 提取出最右侧的1
		return map.get(num & (-num));
	}

	/*
	* 将现有的cur转成string的ip，power表示用了右侧的几个位
	* */
	public static String content(int status, int power) {
		StringBuilder builder = new StringBuilder();
		for (int move = 24; move >= 0; move -= 8) {
			builder.append(((status & (255 << move)) >>> move) + ".");
		}
		builder.setCharAt(builder.length() - 1, '/');
		builder.append(32 - power);
		return builder.toString();
	}

}
