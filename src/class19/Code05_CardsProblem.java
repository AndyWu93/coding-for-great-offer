package class19;

import java.util.LinkedList;

/**
 * 大厂面试题
 * 一张扑克有3个属性，每种属性有3种值（A、B、C）
 * 比如"AAA"，第一个属性值A，第二个属性值A，第三个属性值A
 * 比如"BCA"，第一个属性值B，第二个属性值C，第三个属性值A
 * 给定一个字符串类型的数组cards[]，每一个字符串代表一张扑克
 * 从中挑选三张扑克，一个属性达标的条件是：这个属性在三张扑克中全一样，或全不一样
 * 挑选的三张扑克达标的要求是：每种属性都满足上面的条件
 * 比如："ABC"、"CBC"、"BBC"
 * 第一张第一个属性为"A"、第二张第一个属性为"C"、第三张第一个属性为"B"，全不一样
 * 第一张第二个属性为"B"、第二张第二个属性为"B"、第三张第二个属性为"B"，全一样
 * 第一张第三个属性为"C"、第二张第三个属性为"C"、第三张第三个属性为"C"，全一样
 * 每种属性都满足在三张扑克中全一样，或全不一样，所以这三张扑克达标
 * 返回在cards[]中任意挑选三张扑克，达标的方法数
 *
 * 解题：
 * 	首先，看数据量，一共有10W张牌，说明绝对不是通过牌的数量去做的
 * 	每张牌有3个属性，每个属性3个值，可以用3进制，一共就3位，范围[000,222]，即[0，26]
 * 	根据题意，随意拿3张牌
 * 	如果有两张牌是一样的，那么剩下一张牌必须一样才能符合"每种属性都满足在三张扑克中全一样，或全不一样"
 * 	所以如果有两张牌不一样，那么剩下一张牌必须不一样才行
 * 		（1）3张牌都一样的，C(3,n):n张一样的牌中任意拿3张的方法数
 * 		（2）3张牌不一样，可以写个递归函数，在27种不同的牌中随意取3张不同的牌，3张不同的牌的数量分别是a,b,c张
 * 			那判断一下，如果这3张牌符合"每种属性都满足在三张扑克中全一样，或全不一样"，那抽到这种组合的方法数就是a*b*c
 * */
public class Code05_CardsProblem {

	public static int ways1(String[] cards) {
		LinkedList<String> picks = new LinkedList<>();
		return process1(cards, 0, picks);
	}

	public static int process1(String[] cards, int index, LinkedList<String> picks) {
		if (picks.size() == 3) {
			return getWays1(picks);
		}
		if (index == cards.length) {
			return 0;
		}
		int ways = process1(cards, index + 1, picks);
		picks.addLast(cards[index]);
		ways += process1(cards, index + 1, picks);
		picks.pollLast();
		return ways;
	}

	public static int getWays1(LinkedList<String> picks) {
		char[] s1 = picks.get(0).toCharArray();
		char[] s2 = picks.get(1).toCharArray();
		char[] s3 = picks.get(2).toCharArray();
		for (int i = 0; i < 3; i++) {
			if ((s1[i] != s2[i] && s1[i] != s3[i] && s2[i] != s3[i]) || (s1[i] == s2[i] && s1[i] == s3[i])) {
				continue;
			}
			return 0;
		}
		return 1;
	}

	/**
	 * 解题
	 */
	public static int ways2(String[] cards) {
		/*27中牌面，都转成了int，统计频次*/
		int[] counts = new int[27];
		for (String s : cards) {
			char[] str = s.toCharArray();
			/*3进制转10进制，放入int数组中，统计频次*/
			counts[(str[0] - 'A') * 9 + (str[1] - 'A') * 3 + (str[2] - 'A') * 1]++;
		}
		int ways = 0;
		/*
		* 1. 27中牌面，每个牌面的数量拿出来，计算C(3,n)
		* */
		for (int status = 0; status < 27; status++) {
			int n = counts[status];
			if (n > 2) {
				/*
				* n<=2 :牌都不够3张，不用计算了
				* n==3 :就1中方法，3张全拿
				* n>3  :(n * (n-1) * (n-2)) / (3 * 2)
				* */
				ways += n == 3 ? 1 : (n * (n - 1) * (n - 2) / 6);
			}
		}
		/*
		* 2. 3张牌那不一样的，统计符合条件的方法数：
		* 枚举3张牌的第一个牌的牌面（27个牌面）
		* 剩下26个牌面随意选择，返回方法数，累加
		* */
		LinkedList<Integer> path = new LinkedList<>();
		for (int i = 0; i < 27; i++) {
			if (counts[i] != 0) {
				/*第一张牌面*/
				path.addLast(i);
				/*累加到ways*/
				ways += process2(counts, i, path);
				/*还原现场，接下来枚举下一个牌面*/
				path.pollLast();
			}
		}
		return ways;
	}

	/*
	* counts: 牌面count
	* path: 之前的牌面选好了，放在path里
	* pre: path里的这些牌面，最大的面值
	* 返回 选好剩下的牌面凑足3张后，如果这3张是有效组合，返回方法数
	*
	* pre：参数用途：
	* 身下的牌面只能选择面值比pre大的，这样才能保证3张面值不同的牌的组合值出现过一次
	* */
	public static int process2(int[] counts, int pre, LinkedList<Integer> path) {
		if (path.size() == 3) {
			/*选好3张了，计算方法数*/
			return getWays2(counts, path);
		}
		int ways = 0;
		/*枚举下一张牌面，面值一定比pre大*/
		for (int next = pre + 1; next < 27; next++) {
			/*选的这个牌面，必须要有牌才行*/
			if (counts[next] != 0) {
				path.addLast(next);
				ways += process2(counts, next, path);
				/*记得还原现场*/
				path.pollLast();
			}
		}
		return ways;
	}

	/*
	* 3张牌面在path里，每种面值的数量在counts中，返回这3个面值的组合下，牌的方法数
	* */
	public static int getWays2(int[] counts, LinkedList<Integer> path) {
		/*取出3个面值*/
		int v1 = path.get(0);
		int v2 = path.get(1);
		int v3 = path.get(2);
		/*从高位到低位，分位讨论*/
		for (int i = 9; i > 0; i /= 3) {
			/*拿出3张牌面，当前位的值*/
			int cur1 = v1 / i;
			int cur2 = v2 / i;
			int cur3 = v3 / i;
			/*把当前位代表的值磨掉，接下来要拿下一位的值了*/
			v1 %= i;
			v2 %= i;
			v3 %= i;
			/*如果当前位的值都一样，或者都不一样，继续讨论下一位*/
			if ((cur1 != cur2 && cur1 != cur3 && cur2 != cur3) || (cur1 == cur2 && cur1 == cur3)) {
				continue;
			}
			/*否则，这3个牌面是不符合要求，返回0种方法*/
			return 0;
		}
		/*跳出循环了，表示3个牌面每一位上的值都符合要求，分别拿出每个牌面的张数，相乘后返回*/
		v1 = path.get(0);
		v2 = path.get(1);
		v3 = path.get(2);
		return counts[v1] * counts[v2] * counts[v3];
	}

	// for test
	public static String[] generateCards(int size) {
		int n = (int) (Math.random() * size) + 3;
		String[] ans = new String[n];
		for (int i = 0; i < n; i++) {
			char cha0 = (char) ((int) (Math.random() * 3) + 'A');
			char cha1 = (char) ((int) (Math.random() * 3) + 'A');
			char cha2 = (char) ((int) (Math.random() * 3) + 'A');
			ans[i] = String.valueOf(cha0) + String.valueOf(cha1) + String.valueOf(cha2);
		}
		return ans;
	}

	// for test
	public static void main(String[] args) {
		int size = 20;
		int testTime = 100000;
		System.out.println("test begin");
		for (int i = 0; i < testTime; i++) {
			String[] arr = generateCards(size);
			int ans1 = ways1(arr);
			int ans2 = ways2(arr);
			if (ans1 != ans2) {
				for (String str : arr) {
					System.out.println(str);
				}
				System.out.println(ans1);
				System.out.println(ans2);
				break;
			}
		}
		System.out.println("test finish");

		long start = 0;
		long end = 0;
		String[] arr = generateCards(10000000);
		System.out.println("arr size : " + arr.length + " runtime test begin");
		start = System.currentTimeMillis();
		ways2(arr);
		end = System.currentTimeMillis();
		System.out.println("run time : " + (end - start) + " ms");
		System.out.println("runtime test end");
	}

}
