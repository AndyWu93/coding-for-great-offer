package class33;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * There are a total of numCourses courses you have to take, labeled from 0 to numCourses - 1. You are given an array prerequisites where prerequisites[i] = [ai, bi] indicates that you must take course bi first if you want to take course ai.
 *
 * For example, the pair [0, 1], indicates that to take course 0 you have to first take course 1.
 * Return the ordering of courses you should take to finish all courses. If there are many valid answers, return any of them. If it is impossible to finish all courses, return an empty array.
 * 题意：本题和207题一样，只是这里需要收集拓扑排序
 * @see Problem_0207_CourseSchedule
 */
public class Problem_0210_CourseScheduleII {

	public static class Node {
		public int name;
		public int in;
		public ArrayList<Node> nexts;

		public Node(int n) {
			name = n;
			in = 0;
			nexts = new ArrayList<>();
		}
	}

	public int[] findOrder(int numCourses, int[][] prerequisites) {
		int[] ans = new int[numCourses];
		for (int i = 0; i < numCourses; i++) {
			ans[i] = i;
		}
		if (prerequisites == null || prerequisites.length == 0) {
			return ans;
		}
		HashMap<Integer, Node> nodes = new HashMap<>();
		for (int[] arr : prerequisites) {
			int to = arr[0];
			int from = arr[1];
			if (!nodes.containsKey(to)) {
				nodes.put(to, new Node(to));
			}
			if (!nodes.containsKey(from)) {
				nodes.put(from, new Node(from));
			}
			Node t = nodes.get(to);
			Node f = nodes.get(from);
			f.nexts.add(t);
			t.in++;
		}
		int index = 0;
		Queue<Node> zeroInQueue = new LinkedList<>();
		for (int i = 0; i < numCourses; i++) {
			if (!nodes.containsKey(i)) {
				/*不在nodes里的课程，都是不需要前置课的，所以先收集到答案里去*/
				ans[index++] = i;
			} else {
				/*在nodes里的，入度为0的课，进入队列，开始遍历*/
				if (nodes.get(i).in == 0) {
					zeroInQueue.add(nodes.get(i));
				}
			}
		}
		int needPrerequisiteNums = nodes.size();
		int count = 0;
		while (!zeroInQueue.isEmpty()) {
			Node cur = zeroInQueue.poll();
			/*弹出时收集课程*/
			ans[index++] = cur.name;
			count++;
			for (Node next : cur.nexts) {
				if (--next.in == 0) {
					zeroInQueue.add(next);
				}
			}
		}
		/*能够完成拓扑排序，就返回答案*/
		return count == needPrerequisiteNums ? ans : new int[0];
	}

}
