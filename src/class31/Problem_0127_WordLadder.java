package class31;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * A transformation sequence from word beginWord to word endWord using a dictionary wordList is a sequence of words beginWord -> s1 -> s2 -> ... -> sk such that:
 *
 * Every adjacent pair of words differs by a single letter.
 * Every si for 1 <= i <= k is in wordList. Note that beginWord does not need to be in wordList.
 * sk == endWord
 * Given two words, beginWord and endWord, and a dictionary wordList, return the number of words in the shortest transformation sequence from beginWord to endWord, or 0 if no such sequence exists.
 * 题意：从from到to，返回最小距离（距离是指整个变换过程中所有的单词数量，包括from和to），前提设定参考Code05_WordMinPaths
 * 解题：
 * 	本题只需要距离，宽度优先遍历就可以求出，而Code04_WordLadderII需要收集所有的最短路径，所以需要宽度+深度优先
 *
 * 优化：
 * 	宽度优先可以从from和to同时开始，找邻居少的一侧发散，直到有邻居撞上就能连起来了，优化了常数项
 *
 * @see class26.Code04_WordLadderII
 */
public class Problem_0127_WordLadder {

	// start，出发的单词
	// to, 目标单位
	// list, 列表
	// to 一定属于list
	// start未必
	// 返回变幻的最短路径长度
	public static int ladderLength1(String start, String to, List<String> list) {
		list.add(start);

		// key : 列表中的单词，每一个单词都会有记录！
		// value : key这个单词，有哪些邻居！
		HashMap<String, ArrayList<String>> nexts = getNexts(list);
		// abc  出发     abc  -> abc  0
		// 
		// bbc  1
		HashMap<String, Integer> distanceMap = new HashMap<>();
		/*为什么是1，因为假设list中就两个单词分别是from和to，距离按题意是2*/
		distanceMap.put(start, 1);
		HashSet<String> set = new HashSet<>();
		set.add(start);
		Queue<String> queue = new LinkedList<>();
		queue.add(start);
		while (!queue.isEmpty()) {
			String cur = queue.poll();
			Integer distance = distanceMap.get(cur);
			for (String next : nexts.get(cur)) {
				if (next.equals(to)) {
					/*宽度优先遍历，如果一个邻居中找到了to，直接返回*/
					return distance + 1;
				}
				/*否则，将没有访问过的邻居注册进queue和distanceMap*/
				if (!set.contains(next)) {
					set.add(next);
					queue.add(next);
					distanceMap.put(next, distance + 1);
				}
			}

		}
		/*遍历完了都没找到to*/
		return 0;
	}

	public static HashMap<String, ArrayList<String>> getNexts(List<String> words) {
		HashSet<String> dict = new HashSet<>(words);
		HashMap<String, ArrayList<String>> nexts = new HashMap<>();
		for (int i = 0; i < words.size(); i++) {
			nexts.put(words.get(i), getNext(words.get(i), dict));
		}
		return nexts;
	}

	// 应该根据具体数据状况决定用什么来找邻居
	// 1)如果字符串长度比较短，字符串数量比较多，以下方法适合
	// 2)如果字符串长度比较长，字符串数量比较少，以下方法不适合
	public static ArrayList<String> getNext(String word, HashSet<String> dict) {
		ArrayList<String> res = new ArrayList<String>();
		char[] chs = word.toCharArray();
		for (int i = 0; i < chs.length; i++) {
			for (char cur = 'a'; cur <= 'z'; cur++) {
				if (chs[i] != cur) {
					char tmp = chs[i];
					chs[i] = cur;
					if (dict.contains(String.valueOf(chs))) {
						res.add(String.valueOf(chs));
					}
					chs[i] = tmp;
				}
			}
		}
		return res;
	}

	/**
	 * 优化版本
	 */
	public static int ladderLength2(String beginWord, String endWord, List<String> wordList) {
		HashSet<String> dict = new HashSet<>(wordList);
		if (!dict.contains(endWord)) {
			return 0;
		}
		/*
		* 宽度优先遍历，这里没有使用queue，而是用set来存储cur遍历到的邻居
		* startSet：从start开始每次遍历到的邻居
		* endSet：从end开始每次遍历到的邻居
		* visit：所有访问过的word
		* */
		HashSet<String> startSet = new HashSet<>();
		HashSet<String> endSet = new HashSet<>();
		HashSet<String> visit = new HashSet<>();
		startSet.add(beginWord);
		endSet.add(endWord);
		/*len：收集from到to的距离，一开始就是2，startSet.isEmpty()：直到发现某一侧的邻居没有了，表示都是访问过的了*/
		for (int len = 2; !startSet.isEmpty(); len++) {
			/*
			* 按照优化思维，每次从邻居较少的一侧展开，
			* 这里都是展开的startSet，因为
			* 每次就是先将start的邻居放到nextSet中，最后和endSet比较，谁小谁做startSet
			* 所以
			* startSet是较小的，endSet是较大的
			* */
			HashSet<String> nextSet = new HashSet<>();
			for (String w : startSet) {
				// w -> a(nextSet)
				// a b c
				// 0 
				//   1
				//     2
				/*
				* w是curSet中的一个邻居，如何找到w的邻居放入nextSet中呢？
				* 直接变化w（一个字母一个字母的变），看在dict存在不，存在，且没被访问过就是一个有效的邻居
				* */
				for (int j = 0; j < w.length(); j++) {
					char[] ch = w.toCharArray();
					for (char c = 'a'; c <= 'z'; c++) {
						if (c != w.charAt(j)) {
							ch[j] = c;
							String next = String.valueOf(ch);
							/*
							* 如果变出来的word，和to依次发散出来的邻居撞上了，表示连起来了
							* 为什么直接返回len：每次遍历邻居不管是from一侧还是to一侧的len都++了
							* */
							if (endSet.contains(next)) {
								return len;
							}
							if (dict.contains(next) && !visit.contains(next)) {
								/*注册有效邻居*/
								nextSet.add(next);
								visit.add(next);
							}
						}
					}
				}
			}
			// startSet(小) -> nextSet(某个大小)   和 endSet大小来比
			startSet = (nextSet.size() < endSet.size()) ? nextSet : endSet;
			endSet = (startSet == nextSet) ? endSet : nextSet;
		}
		return 0;
	}

}
