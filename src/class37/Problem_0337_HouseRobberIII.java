package class37;

/**
 * 小偷又发现了一个新的可行窃的地区。这个地区只有一个入口，我们称之为 root 。
 * 除了 root 之外，每栋房子有且只有一个“父“房子与之相连。一番侦察之后，聪明的小偷意识到“这个地方的所有房屋的排列类似于一棵二叉树”。 如果 两个直接相连的房子在同一天晚上被打劫 ，房屋将自动报警。
 * 给定二叉树的 root 。返回 在不触动警报的情况下 ，小偷能够盗取的最高金额 。
 *
 * 解题思路：
 * 	员工快乐值模型
 * 见 体系班class13.Code04_MaxHappy
 */
//https://leetcode-cn.com/problems/house-robber-iii
public class Problem_0337_HouseRobberIII {

	public static class TreeNode {
		public int val;
		public TreeNode left;
		public TreeNode right;
	}

	public static int rob(TreeNode root) {
		Info info = process(root);
		return Math.max(info.no, info.yes);
	}

	public static class Info {
		public int no;
		public int yes;

		public Info(int n, int y) {
			no = n;
			yes = y;
		}
	}

	public static Info process(TreeNode x) {
		if (x == null) {
			return new Info(0, 0);
		}
		Info leftInfo = process(x.left);
		Info rightInfo = process(x.right);
		int no = Math.max(leftInfo.no, leftInfo.yes) + Math.max(rightInfo.no, rightInfo.yes);
		int yes = x.val + leftInfo.no + rightInfo.no;
		return new Info(no, yes);
	}

}
