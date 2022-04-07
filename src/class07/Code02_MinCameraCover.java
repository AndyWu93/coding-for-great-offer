package class07;

/**
 * 给定一个二叉树，我们在树的节点上安装摄像头，节点上的每个摄影头都可以监视其父对象、自身及其直接子对象，计算监控树的所有节点所需的最小摄像头数量
 * 解题：
 * 	二叉树的递归套路O(N)
 * 	本题在分析可能性时，常规思路会发现无法覆盖所有信息
 * 常规思路：
 * 	从左右树获取相机个数
 * 	1.x上有相机
 * 	2.x上无相机
 * 上面两种情况，不足以覆盖所有情况，上面的情况都是在x为头的整棵树被覆盖的情况下讨论，
 * 事实上，还有一种可能性是x可以不用被相机覆盖，x可有由它的父节点来覆盖
 */
// 本题测试链接 : https://leetcode.com/problems/binary-tree-cameras/
public class Code02_MinCameraCover {

	public static class TreeNode {
		public int value;
		public TreeNode left;
		public TreeNode right;
	}

	/**
	 * 递归套路，已经能过了
	 */
	public static int minCameraCover1(TreeNode root) {
		Info data = process1(root);
		return (int) Math.min(data.uncovered + 1, Math.min(data.coveredNoCamera, data.coveredHasCamera));
	}

	/*
	* 潜台词：x是头节点，x下方的点都被覆盖的情况下
	* 为什么只讨论x下方的点都被覆盖？
	* x还可以被父的相机覆盖，x的子如果有没被覆盖的，x又不放相机的话，x的子将无法被补救
	* */
	public static class Info {
		public long uncovered; // x没有被覆盖，x为头的树至少需要几个相机
		public long coveredNoCamera; // x被相机覆盖，但是x没相机，x为头的树至少需要几个相机
		public long coveredHasCamera; // x被相机覆盖了，并且x上放了相机，x为头的树至少需要几个相机

		public Info(long un, long no, long has) {
			uncovered = un;
			coveredNoCamera = no;
			coveredHasCamera = has;
		}
	}

	// 所有可能性都穷尽了
	public static Info process1(TreeNode X) {
		if (X == null) { // base case
			/*空节点，默认是被覆盖的，且不能放相机*/
			return new Info(Integer.MAX_VALUE, 0, Integer.MAX_VALUE);
		}

		Info left = process1(X.left);
		Info right = process1(X.right);
		// x uncovered x自己不被覆盖，x下方所有节点，都被覆盖
		// 左孩子： 左孩子没被覆盖，左孩子以下的点都被覆盖
		// 左孩子被覆盖但没相机，左孩子以下的点都被覆盖
		// 左孩子被覆盖也有相机，左孩子以下的点都被覆盖
		long uncovered = left.coveredNoCamera + right.coveredNoCamera;

		/*x下方的点都被covered，x也被cover，但x上没相机：x左右节点至少有一个有相机*/
		long coveredNoCamera = Math.min(
				// 1)
				left.coveredHasCamera + right.coveredHasCamera,

				Math.min(
						// 2)
						left.coveredHasCamera + right.coveredNoCamera,

						// 3)
						left.coveredNoCamera + right.coveredHasCamera));

		
		
		
		// x下方的点都被covered，x也被cover，且x上有相机
		long coveredHasCamera =
				/*左右树：x上都有相机了，左右孩子有没有被覆盖，有没有相机都无所谓*/
				Math.min(left.uncovered, Math.min(left.coveredNoCamera, left.coveredHasCamera))
				+ Math.min(right.uncovered, Math.min(right.coveredNoCamera, right.coveredHasCamera))
				/*x节点上的相机*/
				+ 1;

		return new Info(uncovered, coveredNoCamera, coveredHasCamera);
	}

	/**
	 * 优化
	 * 上面的解给了3个信息，存在3个信息的min计算，是否能够返回一个确定的信息呢？
	 * 比如叶节点举例：叶节点x左右孩子都没有相机，且都被覆盖了，
	 * 那x上就没有必要放相机，x只要告诉它的父，我没有被覆盖，它的父会放一个相机，而且父的相机明显比自己覆盖的节点更多
	 * 所以用一个状态表明自己有没有相机，有没有被覆盖，再返回一个确定的信息，大大优化了常数项
	 */
	public static int minCameraCover2(TreeNode root) {
		Data data = process2(root);
		return data.cameras + (data.status == Status.UNCOVERED ? 1 : 0);
	}

	// 以x为头，x下方的节点都是被covered，x自己的状况，分三种
	public static enum Status {
		UNCOVERED, COVERED_NO_CAMERA, COVERED_HAS_CAMERA
	}

	// 以x为头，x下方的节点都是被covered，得到的最优解中：
	// x是什么状态，在这种状态下，需要至少几个相机
	public static class Data {
		public Status status;
		public int cameras;

		public Data(Status status, int cameras) {
			this.status = status;
			this.cameras = cameras;
		}
	}

	public static Data process2(TreeNode X) {
		if (X == null) {
			/*被覆盖了，没有相机*/
			return new Data(Status.COVERED_NO_CAMERA, 0);
		}
		Data left = process2(X.left);
		Data right = process2(X.right);
		int cameras = left.cameras + right.cameras;
		
		/*左右孩子，有一个没覆盖，x必须上相机*/
		if (left.status == Status.UNCOVERED || right.status == Status.UNCOVERED) {
			return new Data(Status.COVERED_HAS_CAMERA, cameras + 1);
		}

		/*到这里左右孩子肯定都被覆盖了，但如果有一个有相机，那x肯定被覆盖了，x也不需要上相机*/
		if (left.status == Status.COVERED_HAS_CAMERA || right.status == Status.COVERED_HAS_CAMERA) {
			return new Data(Status.COVERED_NO_CAMERA, cameras);
		}
		/*到这里左右孩子肯定都被覆盖了，而且都没有相机，那x也不用上相机，直接上报自己没有被覆盖，让父上相机好了*/
		return new Data(Status.UNCOVERED, cameras);
	}

}
