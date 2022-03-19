package class33;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * There are a total of numCourses courses you have to take, labeled from 0 to numCourses - 1. You are given an array prerequisites where prerequisites[i] = [ai, bi] indicates that you must take course bi first if you want to take course ai.
 *
 * For example, the pair [0, 1], indicates that to take course 0 you have to first take course 1.
 * Return true if you can finish all courses. Otherwise, return false.
 * 题意：判断给定结构是否能拓扑排序
 * 解题：
 * 	拓扑排序有3中方式，最简单的方式就依次消掉入度为0的节点，及其影响
 */
public class Problem_0207_CourseSchedule {

	// 一个node，就是一个课程
	// name是课程的编号
	// in是课程的入度
	public static class Course {
		public int name;
		public int in;
		public ArrayList<Course> nexts;

		public Course(int n) {
			name = n;
			in = 0;
			nexts = new ArrayList<>();
		}
	}

	/**
	 * 可以参考这个方法，面向对象
	 */
	public static boolean canFinish1(int numCourses, int[][] prerequisites) {
		if (prerequisites == null || prerequisites.length == 0) {
			return true;
		}
		/*把图建立好*/
		// 一个编号 对应 一个课的实例
		HashMap<Integer, Course> nodes = new HashMap<>();
		for (int[] arr : prerequisites) {
			int to = arr[0];
			int from = arr[1]; // from -> to
			if (!nodes.containsKey(to)) {
				nodes.put(to, new Course(to));
			}
			if (!nodes.containsKey(from)) {
				nodes.put(from, new Course(from));
			}
			Course t = nodes.get(to);
			Course f = nodes.get(from);
			f.nexts.add(t);
			t.in++;
		}
		int needPrerequisiteNums = nodes.size();
		/*建立好图以后，如何依次消掉入度为0的节点？宽度优先遍历，每次将入度为0的节点放入queue中*/
		Queue<Course> zeroInQueue = new LinkedList<>();
		for (Course node : nodes.values()) {
			if (node.in == 0) {
				zeroInQueue.add(node);
			}
		}
		int count = 0;
		while (!zeroInQueue.isEmpty()) {
			Course cur = zeroInQueue.poll();
			/*弹出的时候记录一下收集到的入度为0的节点个数*/
			count++;
			for (Course next : cur.nexts) {
				/*邻居，消减入度，且如果入度为0，加入队列*/
				if (--next.in == 0) {
					zeroInQueue.add(next);
				}
			}
		}
		return count == needPrerequisiteNums;
	}

	/**
	 * 优化了常数时间，打破了面向对象了，感觉不太好
	 */
	// 和方法1算法过程一样
	// 但是写法优化了
	public static boolean canFinish2(int courses, int[][] relation) {
		if (relation == null || relation.length == 0) {
			return true;
		}
		// 3 :  0 1 2
		// nexts :   0   {}
		//           1   {}
		//           2   {}
		//           3   {0,1,2}
		ArrayList<ArrayList<Integer>> nexts = new ArrayList<>();
		for (int i = 0; i < courses; i++) {
			nexts.add(new ArrayList<>());
		}
		// 3 入度 1  in[3] == 1
		int[] in = new int[courses];
		for (int[] arr : relation) {
			// arr[1] from   arr[0] to
			nexts.get(arr[1]).add(arr[0]);
			in[arr[0]]++;
		}
		
		// 队列
		int[] zero = new int[courses];
		// 该队列有效范围是[l,r)
		// 新来的数，放哪？r位置，r++
		// 出队列的数，从哪拿？l位置，l++
		// l == r 队列无元素  l < r 队列有元素
		int l = 0;
		int r = 0;
		for (int i = 0; i < courses; i++) {
			if (in[i] == 0) {
				zero[r++] = i;
			}
		}
		int count = 0;
		while (l != r) {
			count++; // zero[l] 出队列   l++
			for (int next : nexts.get(zero[l++])) {
				if (--in[next] == 0) {
					zero[r++] = next;
				}
			}
		}
		return count == nexts.size();
	}

}
