package class33;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * 二维数组board中能走出words中多少个单词（一个单词里不能走重复位置）
 * 思路：本题考查递归剪枝（应该指减少递归支路）的优化
 * 整体思路：
 * 枚举board中所有位置，往各个方向走，收集答案
 * 优化1 不能重复：
 * 可以复用board，来到board[i][j]位置，把该位置置0，在后续递归方法中来到该位置就看下是不是0，
 * 当前方法结束前，在把原来的字符放回去
 * 优化2 当前位置是否需要收集答案：
 * 使用前缀树，将所有words加入前缀树，递归时同步来到前缀树的位置，看是否有该方向的路
 * 优化3 board特别大，words比较少，如何避免收集重复答案：
 * 使用前缀树的pass，收集成功一个word，不仅减end，沿途pass也减掉。以后再来到一个点，如果pass已经没有了，就不要继续看了
 */
// 本题测试链接 : https://leetcode.com/problems/word-search-ii/
public class Problem_0212_WordSearchII {

	public static class TrieNode {
		public TrieNode[] nexts;
		public int pass;
		public int end;

		public TrieNode() {
			nexts = new TrieNode[26];
			pass = 0;
			end = 0;
		}

	}

	public static void fillWord(TrieNode head, String word) {
		head.pass++;
		char[] chs = word.toCharArray();
		int index = 0;
		TrieNode node = head;
		for (int i = 0; i < chs.length; i++) {
			index = chs[i] - 'a';
			if (node.nexts[index] == null) {
				node.nexts[index] = new TrieNode();
			}
			node = node.nexts[index];
			node.pass++;
		}
		node.end++;
	}

	public static String generatePath(LinkedList<Character> path) {
		char[] str = new char[path.size()];
		int index = 0;
		for (Character cha : path) {
			str[index++] = cha;
		}
		return String.valueOf(str);
	}

	/**
	 *
	 * @param board
	 * @param words
	 * @return
	 */
	public static List<String> findWords(char[][] board, String[] words) {
		TrieNode head = new TrieNode(); // 前缀树最顶端的头
		/*没有重复值的前缀树*/
		HashSet<String> set = new HashSet<>();
		for (String word : words) {
			if (!set.contains(word)) {
				fillWord(head, word);
				set.add(word);
			}
		}
		// 答案
		List<String> ans = new ArrayList<>();
		// 沿途走过的字符，收集起来，存在path里
		LinkedList<Character> path = new LinkedList<>();
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[0].length; col++) {
				// 枚举在board中的所有位置
				// 每一个位置出发的情况下，答案都收集
				process(board, row, col, path, head, ans);
			}
		}
		return ans;
	}

	// 从board[row][col]位置的字符出发，
	// 之前的路径上，走过的字符，记录在path里
	// cur还没有登上，有待检查能不能登上去的前缀树的节点
	// 如果找到words中的某个str，就记录在 res里
	// 返回值，从row,col 出发，一共找到了多少个word
	public static int process(
			char[][] board,
			int row, int col,
			LinkedList<Character> path,
			TrieNode cur,
			List<String> res) {
		/*取出该位置的值，后面要放回去*/
		char cha = board[row][col];
		if (cha == 0) { // 这个row col位置是之前走过的位置
			return 0;
		}
		// (row,col) 不是回头路   cha 有效

		int index = cha - 'a';
		if (cur.nexts[index] == null || cur.nexts[index].pass == 0) {
			// 如果没路，或者这条路上最终的word之前加入过结果里
			return 0;
		}
		// 没有走回头路且能登上去
		cur = cur.nexts[index];
		path.addLast(cha);// 当前位置的字符加到路径里去
		int fix = 0; // 从row和col位置出发，后续一共搞定了多少答案
		// 当我来到row col位置，如果决定不往后走了。是不是已经搞定了某个字符串了
		if (cur.end > 0) {
			/*搞定了一个word，加入结果再继续*/
			res.add(generatePath(path));
			cur.end--;
			fix++;
		}
		// 往上、下、左、右，四个方向尝试
		board[row][col] = 0;
		if (row > 0) {
			fix += process(board, row - 1, col, path, cur, res);
		}
		if (row < board.length - 1) {
			fix += process(board, row + 1, col, path, cur, res);
		}
		if (col > 0) {
			fix += process(board, row, col - 1, path, cur, res);
		}
		if (col < board[0].length - 1) {
			fix += process(board, row, col + 1, path, cur, res);
		}
		/*值放回去*/
		board[row][col] = cha;
		/*深度优先遍历还原现场：擦掉当前痕迹*/
		path.pollLast();
		/*沿路消除节点中的pass*/
		cur.pass -= fix;
		return fix;
	}

}
