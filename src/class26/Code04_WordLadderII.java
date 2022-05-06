package class26;

import class31.Problem_0127_WordLadder;

import java.util.*;

/**
 * 从start字符串，从list表中走，步长为1（只能走到跟自己有一个字符不一样的字符串），走到to字符串，把各种可能的路径中最短的返回。最短的可能存在多个
 * 这是一个宽度优先+深度优先遍历的问题
 * 思路：
 * 1. 建立邻居表：在list表中给每个字符串建立自己的邻居，这个仿佛是画了一棵树，每个字符串就像一个点，谁旁边是谁，一目了然，记为Map<String, List<String>>nexts
 * 建立这棵树的实现方法有两个
 * 	a）来到了第i个字符串，遍历list中所有字符串O(N^2)，比对收集距离为1的字符串O(k(k为字符串的平均长度))。复杂度：O(N^2*K)
 * 	b）将所有的字符串放入set中，来到第i个字符串a，将a转化为25*k个字符串O(N*K)，分别在set中寻找是否存在并收集O(K)。复杂度：O(N*K^2)
 * a),b)两种方案需要看N,K的大小，视情况而定
 * 2. 建立到start字符距离表：根据图nexts，从start开始宽度优先遍历，记录每个字符串到start的距离。记为Map<String, Integer> distances
 * 宽度优先：借助queue，每次poll出来的时候，遍历它的所有子，对每个子进行计算，完了后在把每个子放入queue中，直到queue空了为止
 * 3. 根据图nexts，从start开始深度优先遍历，每深入一层，记录深入的路径，借助distances保证深入的字符串距离start单调递增，直到深入到to字符串。收集走过的路径
 * 深度优先：借助递归，来到了start的第一个子节点curr，看curr是否符合条件需要继续深入。重点：离开curr的时候需要还原现场
 *
 * @see Problem_0127_WordLadder
 */
// 本题测试链接 : https://leetcode.com/problems/word-ladder-ii/
public class Code04_WordLadderII {

	public static List<List<String>> findLadders(
			String start, 
			String end,
			List<String> list) {
		list.add(start);
		HashMap<String, List<String>> nexts = getNexts(list);
		HashMap<String, Integer> distances = getDistances(start, nexts);
		LinkedList<String> pathList = new LinkedList<>();
		List<List<String>> res = new ArrayList<>();
		getShortestPaths(start, end, nexts, distances, pathList, res);
		return res;
	}

	public static HashMap<String, List<String>> getNexts(List<String> words) {
		Set<String> dict = new HashSet<>(words); // List 所有东西放入 set
		HashMap<String, List<String>> nexts = new HashMap<>();
		for (int i = 0; i < words.size(); i++) {
			nexts.put(words.get(i), getNext(words.get(i), dict));
		}
		return nexts;
	}

	private static List<String> getNext(String word, Set<String> dict) {
		ArrayList<String> res = new ArrayList<String>();
		char[] chs = word.toCharArray();
		/*curr就是遍历的字符*/
		for (char cur = 'a'; cur <= 'z'; cur++) {
			for (int i = 0; i < chs.length; i++) {
				/*枚举26*k个变换*/
				//如果当前字符时curr，就不要变了，直接下一个
				if (chs[i] != cur) {
					char tmp = chs[i];
					chs[i] = cur;
					if (dict.contains(String.valueOf(chs))) {
						res.add(String.valueOf(chs));
					}
					//比对完了后需要还原回去
					chs[i] = tmp;
				}
			}
		}
		return res;
	}

	public static HashMap<String, Integer> getDistances(String start, HashMap<String, List<String>> nexts) {
		HashMap<String, Integer> distances = new HashMap<>();
		distances.put(start, 0);
		//借助queue
		Queue<String> queue = new LinkedList<String>();
		queue.add(start);
		HashSet<String> set = new HashSet<String>();
		set.add(start);
		while (!queue.isEmpty()) {
			//poll的时候计算
			String cur = queue.poll();
			for (String next : nexts.get(cur)) {
				if (!set.contains(next)) {
					distances.put(next, distances.get(cur) + 1);
					//把子节点加进去
					queue.add(next);
					set.add(next);
				}
			}
		}
		return distances;
	}

	/*
	* 现在来到了什么：cur
	* 目的地：end
	* 邻居表：nexts
	* 最短距离表：distances
	* 沿途走过的路径：path上{....}
	* 答案往res里放，收集所有的最短路径
	* from到to的距在上一步的distance表中已经有了，这个方法是为了记录路径的
	* 只要保证深度优先遍历时是严格递增深入的，当距离到了的时候，一定会出现to字符串，所以一定是最短路径，不会出现距离更大的路径
	* */
	private static void getShortestPaths(
			String cur, String to,
			HashMap<String, List<String>> nexts,
			HashMap<String, Integer> distances,
			LinkedList<String> path,
			List<List<String>> res) {
		//来到了cur字符串，先将路径记下来。
		path.add(cur);
		if (to.equals(cur)) {
			res.add(new LinkedList<String>(path));
		} else {
			for (String next : nexts.get(cur)) {
				//if判断保证深入next的合法性：距离单调增1
				if (distances.get(next) == distances.get(cur) + 1) {
					getShortestPaths(next, to, nexts, distances, path, res);
				}
			}
		}
		//离开curr，将路径擦掉，才方便看前面还有没有合法的支路（还原现场）
		path.pollLast();
	}

	public static void main(String[] args) {
		String start = "abc";
		String end = "cab";
		String[] test = { "abc", "cab", "acc", "cbc", "ccc", "cac", "cbb",
				"aab", "abb" };
		ArrayList<String> list = new ArrayList<>();
		for (int i = 0; i < test.length; i++) {
			list.add(test[i]);
		}
		List<List<String>> res = findLadders(start, end, list);
		for (List<String> obj : res) {
			for (String str : obj) {
				System.out.print(str + " -> ");
			}
			System.out.println();
		}

	}

}
