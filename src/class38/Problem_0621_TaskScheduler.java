package class38;

/**
 * 给你一个用字符数组 tasks 表示的 CPU 需要执行的任务列表。其中每个字母表示一种不同种类的任务。任务可以以任意顺序执行，并且每个任务都可以在 1 个单位时间内执行完。
 * 在任何一个单位时间，CPU 可以完成一个任务，或者处于待命状态。
 * 然而，两个 相同种类 的任务之间必须有长度为整数 n 的冷却时间，因此至少有连续 n 个单位时间内 CPU 在执行不同的任务，或者在待命状态。
 * 你需要计算完成所有任务所需要的 最短时间 。
 * 示例 1：
 * 输入：tasks = ["A","A","A","B","B","B"], n = 2
 * 输出：8
 * 解释：A -> B -> (待命) -> A -> B -> (待命) -> A -> B
 * 在本示例中，两个相同类型任务之间必须间隔长度为 n = 2 的冷却时间，而执行一个任务只需要一个单位时间，所以中间出现了（待命）状态。
 * 示例 2：
 * 输入：tasks = ["A","A","A","B","B","B"], n = 0
 * 输出：6
 * 解释：在这种情况下，任何大小为 6 的排列都可以满足要求，因为 n = 0
 * ["A","A","A","B","B","B"]
 * ["A","B","A","B","A","B"]
 * ["B","B","B","A","A","A"]
 * ...
 * 诸如此类
 * 示例 3：
 * 输入：tasks = ["A","A","A","A","A","A","B","C","D","E","F","G"], n = 2
 * 输出：16
 * 解释：一种可能的解决方案是：
 * A -> B -> C -> A -> D -> E -> A -> F -> G -> A -> (待命) -> (待命) -> A -> (待命) -> (待命) -> A
 *
 * 题意：给定一些任务，有重复的，在数组arr中，每个任务执行占1个时间；给定参数k，表示重复的任务之间必须间隔k个时间
 * 问执行完arr的最短时间
 * 解题：
 * 	本题很难，考贪心算法
 * 	思路：把重复最多的任务做完，假设重复任务最多的a个，k * (a-1) 的时间是逃不了的，
 * 	所以就先把a个重复的任务一字排好，每个任务中间间隔k个空格，剩下的任务就想办法都填到这些空格里，当然一定有某种填法能够满足k的要求
 * 	如果空格填不满，一共n个任务，n个时间，再加上空格的时间，就是最短时间
 * 	如果空格都填满了，被a个任务分出来的每一组后面可以继续填，就没有空格，n个任务执行的时间就是最短时间
 */
//Leetcode题目 : https://leetcode.com/problems/task-scheduler/
public class Problem_0621_TaskScheduler {

	// ['A', 'B', 'A']
	public static int leastInterval(char[] tasks, int free) {
		int[] count = new int[256];
		// 出现最多次的任务，到底是出现了几次
		int maxCount = 0;
		for (char task : tasks) {
			count[task]++;
			maxCount = Math.max(maxCount, count[task]);
		}
		// 有多少种任务，都出现最多次
		int maxKinds = 0;
		for (int task = 0; task < 256; task++) {
			if (count[task] == maxCount) {
				maxKinds++;
			}
		}
		/*
		* 上面找出了重复次数最多的任务，可能有多个。
		* 所以是有maxKinds个任务，每个任务重复maxCount次
		* 先把maxKinds个任务，一组一组排好，中间都要有空格
		* 接下来要做的就是找到前面这些空格最后能剩几个，怎么算呢？
		* 总任务数n 减去最后一组的任务maxKinds 还剩rest个任务
		* 前面每一组的maxKinds，连上空格的数量，即总的占位b：(k+1) * 总组数-1
		* 所以最后留下来的空格数量：b-rest
		* 总时间：
		* max(b-rest,0) + n
		* */
		// maxKinds : 有多少种任务，都出现最多次
		// maxCount : 最多次，是几次？
		// 砍掉最后一组剩余的任务数
		int tasksExceptFinalTeam = tasks.length - maxKinds;
		int spaces = (free + 1) * (maxCount - 1);
		// 到底几个空格最终会留下！
		int restSpaces = Math.max(0, spaces - tasksExceptFinalTeam);
		return tasks.length + restSpaces;
	}
	

}
