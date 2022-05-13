package class38;

/**
 * 360笔试题
 * 长城守卫军
 * 题目描述：
 * 长城上有连成一排的n个烽火台，每个烽火台都有士兵驻守。
 * 第i个烽火台驻守着ai个士兵，相邻峰火台的距离为1。另外，有m位将军，
 * 每位将军可以驻守一个峰火台，每个烽火台可以有多个将军驻守，
 * 将军可以影响所有距离他驻守的峰火台小于等于x的烽火台。
 * 每个烽火台的基础战斗力为士兵数，另外，每个能影响此烽火台的将军都能使这个烽火台的战斗力提升k。
 * 长城的战斗力为所有烽火台的战斗力的最小值。
 * 请问长城的最大战斗力可以是多少？
 * 输入描述
 * 第一行四个正整数n,m,x,k(1<=x<=n<=10^5,0<=m<=10^5,1<=k<=10^5)
 * 第二行n个整数ai(0<=ai<=10^5)
 * 输出描述 仅一行，一个整数，表示长城的最大战斗力
 * 样例输入
 * 5 2 1 2
 * 4 4 2 4 4
 * 样例输出
 * 6
 *
 * 解题：
 * 	本题首先想到的是贪心，要求最大战斗力，先看看每次加成最薄弱的位置，最后看看最薄弱的位置加到了多少
 * 	这个贪心实施起来有难度，因为加成是个范围，不是单点
 * 	换个思路
 * 	现在的加成，能够让战斗力加到的范围，这个范围是可以知道的
 * 	最大值：把加成都给最大的，得到的值max
 * 	最小值：不用加成，目前的最小值min
 * 	最后的答案ans一定在min~max中间
 * 	怎么确定是多少呢？范围内求值，首先想想能不能二分
 * 	二分需要具备的条件:单调性
 * 	因为加成和ans有单调性，加成越多ans越大，因为给到的总加成是一定的，所以能够求出ans的位置
 * 	所以搞一个bool的函数，参数是总共的加成和min~max上的一个值，返回目前的加成是多了还是少了，
 */
public class Code02_GreatWall {

	/**
	 * m个将军，每个将军的加成是k，加成范围是x
	 */
	public static int maxForce(int[] wall, int m, int x, int k) {
		long L = 0;
		long R = 0;
		for (int num : wall) {
			/*找出战斗力最高值*/
			R = Math.max(R, num);
		}
		/*最高值+所有的加成，就是max*/
		R += m * k;
		long ans = 0;
		/*
		* max：R
		* min:理论上应该是wall的最小值，但是这个最小值一定不会小于0，所以这里就写0了
		* */
		while (L <= R) {
			long M = (L + R) / 2;
			if (can(wall, m, x, k, M)) {
				/*can返回true，表示加成还有富余，ans可以更高*/
				ans = M;
				L = M + 1;
			} else {
				/*否则，加成不足，ans降低一点*/
				R = M - 1;
			}
		}
		return (int) ans;
	}

	/*
	* wall中，初始战斗力
	* m个将军，每个将军的加成是k，加成范围是x
	* 返回能不能将wall的战斗力加成到limit？
	*
	* 关于加成：区间的累加：线段树
	* */
	public static boolean can(int[] wall, int m, int x, int k, long limit) {
		int N = wall.length;
		// 注意：下标从1开始
		SegmentTree st = new SegmentTree(wall);
		st.build(1, N, 1);
		int need = 0;
		for (int i = 0; i < N; i++) {
			// 注意：下标从1开始
			long cur = st.query(i + 1, i + 1, 1, N, 1);
//			long cur = wall[i];
			/*
			* 自己的尝试：失败！
			* 这里cur为什么不能直接等于wall[i]?
			* 已经为wall上的战斗力经过加成，值已经变了，
			* cur要拿加成后的值
			* */
			if (cur < limit) {
				int add = (int) ((limit - cur + k - 1) / k);
				need += add;
				if (need > m) {
					return false;
				}
				st.add(i + 1, Math.min(i + x, N), add * k, 1, N, 1);
			}
		}
		return true;
	}

	public static class SegmentTree {
		private int MAXN;
		private int[] arr;
		private int[] sum;
		private int[] lazy;
		private int[] change;
		private boolean[] update;

		public SegmentTree(int[] origin) {
			MAXN = origin.length + 1;
			arr = new int[MAXN];
			for (int i = 1; i < MAXN; i++) {
				arr[i] = origin[i - 1];
			}
			sum = new int[MAXN << 2];
			lazy = new int[MAXN << 2];
			change = new int[MAXN << 2];
			update = new boolean[MAXN << 2];
		}

		private void pushUp(int rt) {
			sum[rt] = sum[rt << 1] + sum[rt << 1 | 1];
		}

		private void pushDown(int rt, int ln, int rn) {
			if (update[rt]) {
				update[rt << 1] = true;
				update[rt << 1 | 1] = true;
				change[rt << 1] = change[rt];
				change[rt << 1 | 1] = change[rt];
				lazy[rt << 1] = 0;
				lazy[rt << 1 | 1] = 0;
				sum[rt << 1] = change[rt] * ln;
				sum[rt << 1 | 1] = change[rt] * rn;
				update[rt] = false;
			}
			if (lazy[rt] != 0) {
				lazy[rt << 1] += lazy[rt];
				sum[rt << 1] += lazy[rt] * ln;
				lazy[rt << 1 | 1] += lazy[rt];
				sum[rt << 1 | 1] += lazy[rt] * rn;
				lazy[rt] = 0;
			}
		}

		public void build(int l, int r, int rt) {
			if (l == r) {
				sum[rt] = arr[l];
				return;
			}
			int mid = (l + r) >> 1;
			build(l, mid, rt << 1);
			build(mid + 1, r, rt << 1 | 1);
			pushUp(rt);
		}

		public void update(int L, int R, int C, int l, int r, int rt) {
			if (L <= l && r <= R) {
				update[rt] = true;
				change[rt] = C;
				sum[rt] = C * (r - l + 1);
				lazy[rt] = 0;
				return;
			}
			int mid = (l + r) >> 1;
			pushDown(rt, mid - l + 1, r - mid);
			if (L <= mid) {
				update(L, R, C, l, mid, rt << 1);
			}
			if (R > mid) {
				update(L, R, C, mid + 1, r, rt << 1 | 1);
			}
			pushUp(rt);
		}

		public void add(int L, int R, int C, int l, int r, int rt) {
			if (L <= l && r <= R) {
				sum[rt] += C * (r - l + 1);
				lazy[rt] += C;
				return;
			}
			int mid = (l + r) >> 1;
			pushDown(rt, mid - l + 1, r - mid);
			if (L <= mid) {
				add(L, R, C, l, mid, rt << 1);
			}
			if (R > mid) {
				add(L, R, C, mid + 1, r, rt << 1 | 1);
			}
			pushUp(rt);
		}

		public long query(int L, int R, int l, int r, int rt) {
			if (L <= l && r <= R) {
				return sum[rt];
			}
			int mid = (l + r) >> 1;
			pushDown(rt, mid - l + 1, r - mid);
			long ans = 0;
			if (L <= mid) {
				ans += query(L, R, l, mid, rt << 1);
			}
			if (R > mid) {
				ans += query(L, R, mid + 1, r, rt << 1 | 1);
			}
			return ans;
		}

	}

	public static void main(String[] args) {
		int[] wall = { 3, 1, 1, 1, 3 };
		int m = 2;
		int x = 3;
		int k = 1;
		System.out.println(maxForce(wall, m, x, k));
	}

}
