package class33;

/**
 * 给定n表示有0~n-1号人
 * 给定一个函数，告诉你x是不是认识i
 * 找出这些人中的明星
 * 明星含义：它不认识所有人，但是所有人认识他
 *
 * 解题：
 * 本题采用排除法，找到一个可能的候选后再验证这个候选人
 * 如何使用排除法呢？
 * 	先把0号人设为候选
 * 	遍历arr到i，看0是不是认识i
 * 		不认识i，说明i肯定不是候选
 * 		认识i，那i具备候选的可能性，同时0认识i了，0就不再是候选了，所以将i设为候选
 * 	继续遍历
 */
public class Problem_0277_FindTheCelebrity {

	// 提交时不要提交这个函数，因为默认系统会给你这个函数
	// knows方法，自己认识自己
	public static boolean knows(int x, int i) {
		return true;
	}

	// 只提交下面的方法 0 ~ n-1
	public int findCelebrity(int n) {
		// 谁可能成为明星，谁就是cand
		/*
		* 这第一个for循环结束以后，可以知道
		* candi之前的人都因为有人不认识他，而不能成为候选人
		* candi之后的人，candi都不认识。
		* 所以candi最有可能成为明星
		* */
		int cand = 0;
		for (int i = 0; i < n; ++i) {
			if (knows(cand, i)) {
				cand = i;
			}
		}

		// cand是什么？唯一可能是明星的人！
		// 下一步就是验证，它到底是不是明星
		// 1) cand是不是不认识所有的人 cand...（右侧cand都不认识）
		// 所以，只用验证 ....cand的左侧即可
		for (int i = 0; i < cand; ++i) {
			if (knows(cand, i)) {
				return -1;
			}
		}
		// 2) 是不是所有的人都认识cand
		for (int i = 0; i < n; ++i) {
			if (!knows(i, cand)) {
				return -1;
			}
		}
		return cand;
	}

}
