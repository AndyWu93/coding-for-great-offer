package class37;

import java.util.HashMap;

/**
 * 给定一个二叉树的根节点 root ，和一个整数 targetSum ，求该二叉树里节点值之和等于 targetSum 的 路径 的数目。
 * 路径 不需要从根节点开始，也不需要在叶子节点结束，但是路径方向必须是向下的（只能从父节点到子节点）。
 *
 * 解题：
 * 	本题解题思路参考数组三连第二问，那一题记录的是某个前缀和最早出现的位置，因为是要求累加和等于k的最长子数组
 * 	本题要求的是累加和等于k的子数组个数
 * 	所以本题应该缓存的是某个前缀和出现的次数，同样需要初始化前缀和为0出现的次数
 * 	本题的难点:
 * 	如何在二叉树上用前缀和数组的思路?
 * 	既然只是前缀和，可以在遍历的过程中累加遍历过的数，就能够得到前缀和，再加入到map缓存中，而且本题只要缓存个数
 * 	不需要缓存位置，所以相对更简单一点。只是在递归推出该节点的时候，map中对应前缀和的个数需要清理现场
 *
 * 解题思路参考数组三连第二问class40.Code02_LongestSumSubArrayLength
 */
//Leetcode题目 : https://leetcode.com/problems/path-sum-iii/
public class Problem_0437_PathSumIII {

	public class TreeNode {
		public int val;
		public TreeNode left;
		public TreeNode right;
	}

	public static int pathSum(TreeNode root, int sum) {
		HashMap<Integer, Integer> preSumMap = new HashMap<>();
		/*表示前缀和等于0的个数为1个*/
		preSumMap.put(0, 1);
		return process(root, sum, 0, preSumMap);
	}

	/*
	* sum: 目标累积和，固定参数
	* preAll：目前位置收集到的累加和
	* preSumMap：累加和个数缓存
	* 返回：路径以 x为头的整颗树中的任意节点 结尾，累加和等于sum的路径个数
	* */
	public static int process(TreeNode x, int sum, int preAll, HashMap<Integer, Integer> preSumMap) {
		if (x == null) {
			/*必须要以一个存在的节点结尾，否则0个路径*/
			return 0;
		}
		/*当前的累积和*/
		int all = preAll + x.val;
		int ans = 0;
		/*要求以x结尾的累加和等于sum的路径个数，就是求前缀和等于all-sum的路径个数*/
		if (preSumMap.containsKey(all - sum)) {
			ans = preSumMap.get(all - sum);
		}
		/*将当前累加和添加/注册到map中去*/
		if (!preSumMap.containsKey(all)) {
			preSumMap.put(all, 1);
		} else {
			preSumMap.put(all, preSumMap.get(all) + 1);
		}
		/*去左右树收集个数*/
		ans += process(x.left, sum, all, preSumMap);
		ans += process(x.right, sum, all, preSumMap);
		/*清理现场*/
		if (preSumMap.get(all) == 1) {
			/*只有1个前缀和为all，就直接删掉*/
			preSumMap.remove(all);
		} else {
			/*否则数量减-1*/
			preSumMap.put(all, preSumMap.get(all) - 1);
		}
		return ans;
	}

}
