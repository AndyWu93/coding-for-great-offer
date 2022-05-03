package class21;

import java.util.HashMap;

/**
 * 树链剖分专题
 * 给定数组father，大小为N，表示一共有N个节点
 * father[i] = j 表示点i的父亲是点j， father表示的树一定是一棵树而不是森林
 * 给定数组values，大小为N，values[i]=v表示节点i的权值是v
 * 实现如下4个方法，保证4个方法都很快！
 * 1)让某个子树所有节点值加上v，入参：int head, int v
 * 2)查询某个子树所有节点值的累加和，入参：int head
 * 3)在树上从a到b的整条链上所有加上v，入参：int a, int b, int v
 * 4)查询在树上从a到b的整条链上所有节点值的累加和，入参：int a, int b
 *
 * 树链剖分结构解析：
 * 	对于一颗多叉树，任意节点x，将节点较多的那颗子树作为嫡出，其他都是庶出，如果子树的节点都一样多，就选排在前面的作为太子
 * 	对这棵多叉树玩深度优先遍历，选嫡子优先深入，这样形成的遍历顺序为dfs序
 * 	该dfs序特点：
 * 		1. 嫡系的节点会组成一条高速公路，快速到达x节点
 * 		2. 庶系的节点，通过自己的嫡系父亲，搭上高速公路后，也能快速到达x节点
 * 		3. x节点的所有子树节点，都在x的dfs后面的一组连续的节点
 * 	以上时间复杂度为O(logN)
 * 	该dfs序可以做什么？
 * 		dfs序作为一组连续的编号，借助线段树，可以轻易的实现某个子树的所有节点，或者两个节点之间的所有节点，查询和修改
 * 	dfs中线段树的操作复杂度为O(logN)
 * 	所以整体复杂度为O((logN)^2)
 */
public class TreeChainPartition {

	public static class TreeChain {
		// 时间戳 0 1 2 3 4
		/*为了生成dfs序的编号*/
		private int tim;
		// 节点个数是n，节点编号是1~n
		/*简单信息，总节点数*/
		private int n;
		// 谁是头
		/*简单信息，整颗树头节点index*/
		private int h;
		// 朴素树结构
		/*
		* 朴素树：描述树的结构
		* index从1开始
		* 举例：
		* 	原树：0->2,3,4
		* 	朴素树：1->3,4,5
		* 	tree[1]=[3,4,5]
		* */
		private int[][] tree;
		/*权重数组 原始的0节点权重是6 -> val[1] = 6*/
		private int[] val;
		// father数组一个平移，因为标号要+1
		private int[] fa;
		// 深度数组！
		/*记录朴素树中每个节点的深度*/
		private int[] dep;
		// son[i] = 0 i这个节点，没有儿子
		// son[i] != 0 j i这个节点，重儿子是j
		/*嫡子数组，每个节点只可能有一个嫡子*/
		private int[] son;
		// siz[i] i这个节点为头的子树，有多少个节点
		private int[] siz;
		// top[i] = j i这个节点，所在的重链，头是j
		/*嫡系数组，记录一条嫡系链的头节点*/
		private int[] top;
		// dfn[i] = j i这个节点，在dfs序中是第j个
		private int[] dfn;
		// 如果原来的节点a，权重是10
		// 如果a节点在dfs序中是第5个节点, tnw[5] = 10
		private int[] tnw;
		// 线段树，在tnw上，玩连续的区间查询或者更新
		private SegmentTree seg;

		public TreeChain(int[] father, int[] values) {
			/*
			* 初始化朴素树tree，新index构成的树
			* 以及新index的值数组val
			* 以及头节点index
			* */
			initTree(father, values);
			/*
			* 第一次dfs
			* 完成
			* fa：新index构成father数组
			* dep：tree每个节点的深度
			* son：嫡子数组，每个节点只有1个嫡子
			* siz：每个节点的子树节点个数
			* */
			dfs1(h, 0);
			/*
			* 第二次dfs
			* 完成
			* top：嫡系一条链的头节点
			* dfn：tree中，嫡系深度优先的dfs序
			* tnw：dfs序直接对应的val
			* */
			dfs2(h, h);
			seg = new SegmentTree(tnw);
			seg.build(1, n, 1);
		}

		private void initTree(int[] father, int[] values) {
			tim = 0;
			/*0位置弃而不用，下标从1~n，所以空间时n+1*/
			n = father.length + 1;
			tree = new int[n][];
			val = new int[n];
			fa = new int[n];
			dep = new int[n];
			son = new int[n];
			siz = new int[n];
			top = new int[n];
			dfn = new int[n];
			/*空间开完以后，回到n*/
			tnw = new int[n--];
			/*统计每个节点的直接儿子数量，来给tree的第二维数组开空间*/
			int[] cnum = new int[n];
			for (int i = 0; i < n; i++) {
				/*平移值数组*/
				val[i + 1] = values[i];
			}
			for (int i = 0; i < n; i++) {
				if (father[i] == i) {
					/*i的父是自己，表示这个是头节点index*/
					h = i + 1;
				} else {
					/*否则i作为父的直接儿子，父的儿子数量++*/
					cnum[father[i]]++;
				}
			}
			/*tree[0]： 弃而不用的位置，也开一个0空间*/
			tree[0] = new int[0];
			for (int i = 0; i < n; i++) {
				/*tree其他位置，按儿子数量把空间都开好*/
				tree[i + 1] = new int[cnum[i]];
			}
			for (int i = 0; i < n; i++) {
				if (i + 1 != h) {
					/*下标1~n的节点，除了h头节点，其他节点都放到tree中该放的位置*/
					tree[father[i] + 1][--cnum[father[i]]] = i + 1;
				}
			}
		}

		/*
		* u 当前节点新index
		* f u的父节点新index
		* 完成
		* fa：新index构成father数组
		* dep：tree每个节点的深度
		* son：嫡子数组，每个节点只有1个嫡子
		* siz：每个节点的子树节点个数
		* */
		private void dfs1(int u, int f) {
			fa[u] = f;
			dep[u] = dep[f] + 1;
			siz[u] = 1;
			/*用于筛选出哪个是嫡子*/
			int maxSize = -1;
			/*遍历u所有的儿子*/
			for (int v : tree[u]) { // 遍历u节点，所有的直接孩子
				/*深度优先，把v的属性填好*/
				dfs1(v, u);
				/*更新u的子树大小*/
				siz[u] += siz[v];
				/*判断v有没有可能成为嫡子*/
				if (siz[v] > maxSize) {
					maxSize = siz[v];
					son[u] = v;
				}
			}
		}

		/*
		* u 当前节点新index
		* t u所在嫡系链的头index
		* 完成
		* top：嫡系一条链的头节点
		* dfn：tree中，嫡系深度优先的dfs序
		* tnw：dfs序直接对应的val
		* */
		private void dfs2(int u, int t) {
			/*dfs序*/
			dfn[u] = ++tim;
			top[u] = t;
			/*dfs序对应val*/
			tnw[tim] = val[u];
			/*u如果有嫡子（u如果只有1个儿子的话，那这个儿子一定是嫡子）*/
			if (son[u] != 0) { // 如果u有儿子 siz[u] > 1
				/*u的嫡子，嫡系链头也同样是t*/
				dfs2(son[u], t);
				/*u其他庶出的儿子，不能加入t的嫡系链，他们自己开一个嫡系链*/
				for (int v : tree[u]) {
					if (v != son[u]) {
						dfs2(v, v);
					}
				}
			}
		}

		/*
		* head为头的子树上，所有节点值+value
		* */
		public void addSubtree(int head, int value) {
			/*用户传入的head是旧的index，先转化成新index*/
			head++;
			/*找出head子树大小，在dfs序定位出区间，在区间上更新*/
			seg.add(dfn[head], dfn[head] + siz[head] - 1, value, 1, n, 1);
		}

		public int querySubtree(int head) {
			head++;
			return seg.query(dfn[head], dfn[head] + siz[head] - 1, 1, n, 1);
		}

		/*
		* a到b节点，加值v
		* */
		public void addChain(int a, int b, int v) {
			/*index转换*/
			a++;
			b++;
			/*a和b不在一条嫡系链*/
			while (top[a] != top[b]) {
				/*
				* 看下a和b所在各自嫡系链头所在深度，深度大的链条先计算（一条嫡系链一定是连续的区间）
				* 计算结束后，来到嫡系链头的父亲节点（嫡系链头和它的父亲一定是庶出的关系）
				* */
				if (dep[top[a]] > dep[top[b]]) {
					seg.add(dfn[top[a]], dfn[a], v, 1, n, 1);
					a = fa[top[a]];
				} else {
					seg.add(dfn[top[b]], dfn[b], v, 1, n, 1);
					b = fa[top[b]];
				}
			}
			/*
			* 因为一次或者多次嫡系链头到父亲的庶出跳跃，跨越了一次或者多次嫡系链之后，
			* a和b一定来到同一条嫡系链上
			* 因为同一条嫡系链是一个连续的区间，所以直接操作线段树
			* （同一条嫡系链：深度越大，dfs序越大）
			* */
			if (dep[a] > dep[b]) {
				seg.add(dfn[b], dfn[a], v, 1, n, 1);
			} else {
				seg.add(dfn[a], dfn[b], v, 1, n, 1);
			}
		}

		public int queryChain(int a, int b) {
			a++;
			b++;
			int ans = 0;
			while (top[a] != top[b]) {
				if (dep[top[a]] > dep[top[b]]) {
					ans += seg.query(dfn[top[a]], dfn[a], 1, n, 1);
					a = fa[top[a]];
				} else {
					ans += seg.query(dfn[top[b]], dfn[b], 1, n, 1);
					b = fa[top[b]];
				}
			}
			if (dep[a] > dep[b]) {
				ans += seg.query(dfn[b], dfn[a], 1, n, 1);
			} else {
				ans += seg.query(dfn[a], dfn[b], 1, n, 1);
			}
			return ans;
		}
	}

	public static class SegmentTree {
		private int MAXN;
		private int[] arr;
		private int[] sum;
		private int[] lazy;

		public SegmentTree(int[] origin) {
			MAXN = origin.length;
			arr = origin;
			sum = new int[MAXN << 2];
			lazy = new int[MAXN << 2];
		}

		private void pushUp(int rt) {
			sum[rt] = sum[rt << 1] + sum[rt << 1 | 1];
		}

		private void pushDown(int rt, int ln, int rn) {
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

		public int query(int L, int R, int l, int r, int rt) {
			if (L <= l && r <= R) {
				return sum[rt];
			}
			int mid = (l + r) >> 1;
			pushDown(rt, mid - l + 1, r - mid);
			int ans = 0;
			if (L <= mid) {
				ans += query(L, R, l, mid, rt << 1);
			}
			if (R > mid) {
				ans += query(L, R, mid + 1, r, rt << 1 | 1);
			}
			return ans;
		}

	}

	// 为了测试，这个结构是暴力但正确的方法
	public static class Right {
		private int n;
		private int[][] tree;
		private int[] fa;
		private int[] val;
		private HashMap<Integer, Integer> path;

		public Right(int[] father, int[] value) {
			n = father.length;
			tree = new int[n][];
			fa = new int[n];
			val = new int[n];
			for (int i = 0; i < n; i++) {
				fa[i] = father[i];
				val[i] = value[i];
			}
			int[] help = new int[n];
			int h = 0;
			for (int i = 0; i < n; i++) {
				if (father[i] == i) {
					h = i;
				} else {
					help[father[i]]++;
				}
			}
			for (int i = 0; i < n; i++) {
				tree[i] = new int[help[i]];
			}
			for (int i = 0; i < n; i++) {
				if (i != h) {
					tree[father[i]][--help[father[i]]] = i;
				}
			}
			path = new HashMap<>();
		}

		public void addSubtree(int head, int value) {
			val[head] += value;
			for (int next : tree[head]) {
				addSubtree(next, value);
			}
		}

		public int querySubtree(int head) {
			int ans = val[head];
			for (int next : tree[head]) {
				ans += querySubtree(next);
			}
			return ans;
		}

		public void addChain(int a, int b, int v) {
			path.clear();
			path.put(a, null);
			while (a != fa[a]) {
				path.put(fa[a], a);
				a = fa[a];
			}
			while (!path.containsKey(b)) {
				val[b] += v;
				b = fa[b];
			}
			val[b] += v;
			while (path.get(b) != null) {
				b = path.get(b);
				val[b] += v;
			}
		}

		public int queryChain(int a, int b) {
			path.clear();
			path.put(a, null);
			while (a != fa[a]) {
				path.put(fa[a], a);
				a = fa[a];
			}
			int ans = 0;
			while (!path.containsKey(b)) {
				ans += val[b];
				b = fa[b];
			}
			ans += val[b];
			while (path.get(b) != null) {
				b = path.get(b);
				ans += val[b];
			}
			return ans;
		}

	}

	// 为了测试
	// 随机生成N个节点树，可能是多叉树，并且一定不是森林
	// 输入参数N要大于0
	public static int[] generateFatherArray(int N) {
		int[] order = new int[N];
		for (int i = 0; i < N; i++) {
			/*先给order按序赋值*/
			order[i] = i;
		}
		for (int i = N - 1; i >= 0; i--) {
			/*把order数组打乱，怎么打乱的，从后往前遍历，把当前位置与前面的随机位置交换*/
			swap(order, i, (int) (Math.random() * (i + 1)));
		}
		/*根据order生成ans*/
		int[] ans = new int[N];
		/*order[0]的父是自己*/
		ans[order[0]] = order[0];
		for (int i = 1; i < N; i++) {
			/*order后面的元素，在自己位置前面随机找一个作自己的父*/
			ans[order[i]] = order[(int) (Math.random() * i)];
		}
		return ans;
	}

	// 为了测试
	public static void swap(int[] arr, int i, int j) {
		int tmp = arr[i];
		arr[i] = arr[j];
		arr[j] = tmp;
	}

	// 为了测试
	public static int[] generateValueArray(int N, int V) {
		int[] ans = new int[N];
		for (int i = 0; i < N; i++) {
			ans[i] = (int) (Math.random() * V) + 1;
		}
		return ans;
	}

	// 对数器
	public static void main(String[] args) {
		int N = 50000;
		int V = 100000;
		int[] father = generateFatherArray(N);
		int[] values = generateValueArray(N, V);
		TreeChain tc = new TreeChain(father, values);
		Right right = new Right(father, values);
		int testTime = 1000000;
		System.out.println("测试开始");
		for (int i = 0; i < testTime; i++) {
			double decision = Math.random();
			if (decision < 0.25) {
				int head = (int) (Math.random() * N);
				int value = (int) (Math.random() * V);
				tc.addSubtree(head, value);
				right.addSubtree(head, value);
			} else if (decision < 0.5) {
				int head = (int) (Math.random() * N);
				if (tc.querySubtree(head) != right.querySubtree(head)) {
					System.out.println("出错了!");
				}
			} else if (decision < 0.75) {
				int a = (int) (Math.random() * N);
				int b = (int) (Math.random() * N);
				int value = (int) (Math.random() * V);
				tc.addChain(a, b, value);
				right.addChain(a, b, value);
			} else {
				int a = (int) (Math.random() * N);
				int b = (int) (Math.random() * N);
				if (tc.queryChain(a, b) != right.queryChain(a, b)) {
					System.out.println("出错了!");
				}
			}
		}
		System.out.println("测试结束");
	}

}
