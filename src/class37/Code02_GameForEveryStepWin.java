package class37;

/**
 * 来自字节
 * 扑克牌中的红桃J和梅花Q找不到了，为了利用剩下的牌做游戏，小明设计了新的游戏规则
 * 1) A,2,3,4....10,J,Q,K分别对应1到13这些数字，大小王对应0
 * 2) 游戏人数为2人，轮流从牌堆里摸牌，每次摸到的牌只有“保留”和“使用”两个选项，且当前轮必须做出选择
 * 3) 如果选择“保留”当前牌，那么当前牌的分数加到总分里，并且可以一直持续到游戏结束
 * 4) 如果选择“使用”当前牌，那么当前牌的分数*3，加到总分上去，但是只有当前轮，下一轮，下下轮生效，之后轮效果消失。
 * 5) 每一轮总分大的人获胜
 * 假设小明知道每一轮对手做出选择之后的总分，返回小明在每一轮都赢的情况下，最终的最大分是多少
 * 如果小明怎么都无法保证每一轮都赢，返回-1
 *
 * 解题：
 * 	字节的题就是难！
 * 	小明开启上帝模式，每轮摸的牌自己都知道：int[] cands；每轮对方的得分都知道：int[] scores;
 */
public class Code02_GameForEveryStepWin {

//	public static max(int[] cands, int[] sroces) {
//		return f(cands, sroces, 0, 0, 0, 0);
//	}

	// 当前来到index位置，牌是cands[index]值
	// 对手第i轮的得分，sroces[i]
	// int hold : i之前保留的牌的总分
	// int cur : 当前轮得到的，之前的牌只算上使用的效果，加成是多少
	// int next : 之前的牌，对index的下一轮，使用效果加成是多少
	// 返回值：如果i...最后，不能全赢，返回-1
	// 如果i...最后，能全赢，返回最后一轮的最大值
	
	// index -> 26种
	// hold -> (1+2+3+..13) -> 91 -> 91 * 4 - (11 + 12) -> 341
	// cur -> 26 * 3 = 72
	// next -> 13 * 3 = 39
	// 26 * 341 * 72 * 39 -> 2 * (10 ^ 7)
	/*
	*
	* 本题的递归设计中，总分从来没有作为参数往下传，之后hold在往下传，为什么传hold，题目中都说了hold可以持续到结束，爆发的不能
	* 所以用另外两个参数来记录本局爆发的状态下对下一局和下下局的影响
	* */
	public static int f(int[] cands, int[] sroces, int index, int hold, int cur, int next) {
		/*题目开始交代少了2张牌，是在暗示总牌数量52张，所以cands只要26条数据*/
		if (index == 25) { // 最后一张
			/*肯定选择爆发*/
			int all = hold + cur + cands[index] * 3;
			if (all <= sroces[index]) {
				return -1;
			}
			return all;
		}
		// 不仅最后一张
		// 1. 保留
		/*每一局，all是通过计算得来的*/
		int all1 = hold + cur + cands[index];
		/*默认没干过*/
		int p1 = -1;
		if (all1 > sroces[index]) {
			/*
			* 干过了，可以在继续下一局了，
			* 下一局：
			* hold: 上一局的hold + 本局的决定，这里加入了值
			* cur: 上一局的next继承过来 + 本局的影响，这里本局的影响为0，因为没爆发
			* next：本局的影响，这里本局的影响为0，因为没爆发
			* */
			p1 = f(cands, sroces, index + 1, hold + cands[index], next, 0);
		}
		// 2. 爆发
		int all2 = hold + cur + cands[index] * 3;
		int p2 = -1;
		if (all2 > sroces[index]) {
			/*
			 * 干过了，可以在继续下一局了，
			 * 下一局：
			 * hold: 上一局的hold + 本局的决定，这里不加
			 * cur: 上一局的next继承过来 + 本局的影响，这里本局的影响为3倍的值，因为爆发了
			 * next：本局的影响，里本局的影响为3倍的值，因为爆发了
			 * */
			p2 = f(cands, sroces, index + 1, hold, next + cands[index] * 3, cands[index] * 3);
		}
		return Math.max(p1, p2);
	}


	
	// cur -> 牌点数    ->  * 3 之后是效果
	// next -> 牌点数   ->  * 3之后是效果
	// index -> 26种
	// hold -> (1+2+3+..13) -> 91 -> 91 * 4 - (11 + 12) -> 341
	// cur -> 26
	// next -> 13
	// 26 * 341 * 26 * 13 -> ? * (10 ^ 5)
	/*
	* 如何优化？减少可变参数的取值范围，乘3放到方法里面去做，不放在参数里
	* */
	public static int p(int[] cands, int[] sroces, int index, int hold, int cur, int next) {
		if (index == 25) { // 最后一张
			int all = hold + cur * 3 + cands[index] * 3;
			if (all <= sroces[index]) {
				return -1;
			}
			return all;
		}
		// 不仅最后一张
		// 保留
		int all1 = hold + cur * 3 + cands[index];
		int p1 = -1;
		if (all1 > sroces[index]) {
			p1 = f(cands, sroces, index + 1, hold + cands[index], next, 0);
		}
		// 爆发
		int all2 = hold + cur * 3 + cands[index] * 3;
		int p2 = -1;
		if (all2 > sroces[index]) {
			p2 = f(cands, sroces, index + 1, hold, next + cands[index], cands[index]);
		}
		return Math.max(p1, p2);
	}
	
	// 改出动态规划，记忆化搜索！
	
	

}
