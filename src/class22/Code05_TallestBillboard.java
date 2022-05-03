package class22;

import java.util.HashMap;

/**
 * 你正在安装一个广告牌，并希望它高度最大。这块广告牌将有两个钢制支架，两边各一个。每个钢支架的高度必须相等。
 * 你有一堆可以焊接在一起的钢筋 rods。举个例子，如果钢筋的长度为 1、2 和 3，则可以将它们焊接在一起形成长度为 6 的支架。
 * 返回广告牌的最大可能安装高度。如果没法安装广告牌，请返回 0。
 *
 * 解题：
 * 	要的的是最高的广告牌，并不关心是由哪些材料组成的两对，所以用map来递推
 * 	map:
 * 		key: 任意的两组材料的高度差
 * 		value: 目前为止收集到的高度差为key的所有组材料中，高度最高的一组，记下小的高度，大的高度通过key也就能得到了
 * 	遍历arr，来到i位置
 * 	把之前的map里的记录拿出来，逐条看，推出新的记录，放到新map中
 * 	比如任意一条记录(k,v)
 * 	(k,v)可以转成两组高度，分别是v+k,v; arr[i]可以加到这两组高度里边去，分别加进去看一下，生成两条记录，放入新map中
 * 	就map都看完后，旧map的记录也合并到新记录里
 * 	合并时需要注意，同样的key，只保留更大的高度
 * 	最终，map(0)，表示高度差为0，即一样高的两组材料，且是目前收集到的最大高度，就是结果。
 *
 * 	动态规划，每遍历arr一个位置，用老的map推出新的map
 */
// 本题测试链接 : https://leetcode.com/problems/tallest-billboard/
public class Code05_TallestBillboard {

	public int tallestBillboard(int[] rods) {
		// key 集合对的某个差
		// value 满足差值为key的集合对中，最好的一对，较小集合的累加和
		// 较大 -> value + key
		/*dp，cur新老map依次推*/
		HashMap<Integer, Integer> dp = new HashMap<>(), cur;
		/*高度差为0，最大高度0*/
		dp.put(0, 0);// 空集 和 空集
		for (int num : rods) {
			/*只看高度不是0的材料*/
			if (num != 0) {
				/*dp里的数拷贝到cur里，cur作为老的map，逐条生成新纪录进入dp中*/
				cur = new HashMap<>(dp); // 考虑x之前的集合差值状况拷贝下来
				/*遍历高度差，逐条看*/
				for (int d : cur.keySet()) {
					/*把高度拿出来*/
					int diffMore = cur.get(d); // 最好的一对，较小集合的累加和
					/*
					* x决定放入，高度比较大的那个，即(diffMore + d)
					* 此时高度差将会变的更大，变成了d+x
					* 更新d+x记录，现在的值diffMore，和老的值pk一下，留大的
					* */
					dp.put(d + num, Math.max(diffMore, dp.getOrDefault(num + d, 0)));
					/*
					* x决定放入，比较小的那个,即diffMore
					* x 加入了小的，所以高度差将要变小，因为map中value只记这一组材料里较小的高度，
					* 较小的高度增加了x之后有可能变成较大的高度了
					* 加入之前的材料高度(diffMore,diffMore+d)
					* 加入之后的材料高度(diffMore+x,diffMore+d)
					* 所以分两种情况
					* 	1. x<=d,增加后低材料没有超过较高的材料，value不变，生记录(d-x,diffMore+x)
					* 	2. x>d,增加后低材料超过较高的材料，value就要换成增加后较低的材料了，生记录(x-d,diffMore+d)
					* */
					int diffXD = dp.getOrDefault(Math.abs(num - d), 0);
					if (d >= num) {
						/*生成的记录put时还是要和旧map的记录pk一下，留大的*/
						dp.put(d - num, Math.max(diffMore + num, diffXD));
					} else {
						dp.put(num - d, Math.max(diffMore + d, diffXD));
					}
				}
			}
		}
		/*返回的是高度差是0的一组材料，且收集到最高的*/
		return dp.get(0);
	}

}
