package class33;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 给定str数组，这些str是按外星人的字典序排好序的。
 * 根据str，返回其中包含的字母的外星人字典序，如果是乱排的返回""
 *
 * 解题：
 * 	本题使用拓扑排序
 * 	思路：
 * 		将所有遇到的字母按小指向大，画成一张图，返回这张图的拓扑序。如果该图存在环，说明是乱排的
 */
public class Problem_0269_AlienDictionary {

	public static String alienOrder(String[] words) {
		if (words == null || words.length == 0) {
			return "";
		}
		int N = words.length;
		/*入度，用表，省去建立节点*/
		HashMap<Character, Integer> indegree = new HashMap<>();
		for (int i = 0; i < N; i++) {
			for (char c : words[i].toCharArray()) {
				indegree.put(c, 0);
			}
		}
		HashMap<Character, HashSet<Character>> graph = new HashMap<>();
		for (int i = 0; i < N - 1; i++) {
			/*相邻的两个str拿出来*/
			char[] cur = words[i].toCharArray();
			char[] nex = words[i + 1].toCharArray();
			/*比较短的长度就够了，后面的没用*/
			int len = Math.min(cur.length, nex.length);
			int j = 0;
			for (; j < len; j++) {
				if (cur[j] != nex[j]) {
					/*2个str中有两个字母不一样，就出了一条边了*/
					if (!graph.containsKey(cur[j])) {
						graph.put(cur[j], new HashSet<>());
					}
					if (!graph.get(cur[j]).contains(nex[j])) {
						graph.get(cur[j]).add(nex[j]);
						/*记得to节点入度++*/
						indegree.put(nex[j], indegree.get(nex[j]) + 1);
					}
					/*有一个字母比出大小了，后面字母的大小不影响字典序*/
					break;
				}
			}
			if (j < cur.length && j == nex.length) {
				/*这两表示目前这两个str前面的部分都一样，但是长一点的str反而排到了短的前面，这符合字典序的逻辑，直接返回*/
				return "";
			}
		}
		/*收集拓扑排序*/
		StringBuilder ans = new StringBuilder();
		Queue<Character> q = new LinkedList<>();
		for (Character key : indegree.keySet()) {
			if (indegree.get(key) == 0) {
				q.offer(key);
			}
		}
		while (!q.isEmpty()) {
			char cur = q.poll();
			ans.append(cur);
			if (graph.containsKey(cur)) {
				for (char next : graph.get(cur)) {
					indegree.put(next, indegree.get(next) - 1);
					if (indegree.get(next) == 0) {
						q.offer(next);
					}
				}
			}
		}
		return ans.length() == indegree.size() ? ans.toString() : "";
	}

}
