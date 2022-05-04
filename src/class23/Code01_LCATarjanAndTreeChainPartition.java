package class23;

import java.util.HashSet;

/**
 * 给定数组father大小为N，表示一共有N个节点，每个节点值都不一样
 * father[i] = j 表示点i的父亲是点j， father表示的树一定是一棵树而不是森林
 * queries是二维数组，大小为M*2，每一个长度为2的数组都表示一条查询
 * [4,9], 表示想查询4和9之间的最低公共祖先…
 * [3,7], 表示想查询3和7之间的最低公共祖先…
 * tree和queries里面的所有值，都一定在0~N-1之间
 * 返回一个数组ans，大小为M，ans[i]表示第i条查询的答案
 *
 * 解题：
 * 暴力解：
 * 	对query[0],把它所有的father节点放到一个set中去，再去遍历query[1]所有的father，看看在不在set中，第一个在的，就是最低公共祖先
 * 	复杂度O(N*M)
 * 在线解：
 * 	将father数组建立好树链剖分，query[0]，query[1]两个节点往上跳，最终会跳到同一条嫡系链中去，此时深度的节点就是最低公共祖先
 * 	复杂度：建立好树链剖分O(N)，查询O(logN)，所以O(N)+O(M*logN)
 * 	此时query数组增加查询，不影响复杂度
 * 离线解：
 * 	将query中所有涉及到的节点建立信息库lib：自己最为索引，信息包括：（可能有好几条信息）
 * 		1. 和哪个节点查最低公共祖先
 * 		2. 我是query[0],它是query[1],这个问题是第几条记录
 * 	深度优先遍历father数组组成的tree，来到cur节点
 * 		1. 使用并查集，将cur和它所有的子树节点合并到一起，并给这个集合打上标签，为cur
 * 		2. 到lib中找到自己，如果找到了，就遍历每一条信息，看信息1中的节点有没有在某个集合中
 * 				不在，搁置这个问题
 * 				在，那就找到是哪个集合，这个集合的tag是谁？这个tag就是cur和信息1节点的最低公共祖先，答案填到信息2里去
 * 	复杂度：O(N+M)
 * 	此时query数组增加查询，需要重新建立所以，重新遍历tree，将会影响复杂度
 */
public class Code01_LCATarjanAndTreeChainPartition {

	/**
	 * 暴力方法
	 * O(N*M)
	 */
	public static int[] query1(int[] father, int[][] queries) {
		int M = queries.length;
		int[] ans = new int[M];
		HashSet<Integer> path = new HashSet<>();
		for (int i = 0; i < M; i++) {
			int jump = queries[i][0];
			while (father[jump] != jump) {
				path.add(jump);
				jump = father[jump];
			}
			path.add(jump);
			jump = queries[i][1];
			while (!path.contains(jump)) {
				jump = father[jump];
			}
			ans[i] = jump;
			path.clear();
		}
		return ans;
	}

	/**
	 * 离线解
	 * O(N + M)
	 */
	// 离线批量查询最优解 -> Tarjan + 并查集
	// 如果有M条查询，时间复杂度O(N + M)
	// 但是必须把M条查询一次给全，不支持在线查询
	public static int[] query2(int[] father, int[][] queries) {
		int N = father.length;
		int M = queries.length;
		int[] help = new int[N];
		int h = 0;
		for (int i = 0; i < N; i++) {
			if (father[i] == i) {
				h = i;
			} else {
				help[father[i]]++;
			}
		}
		int[][] mt = new int[N][];
		for (int i = 0; i < N; i++) {
			mt[i] = new int[help[i]];
		}
		for (int i = 0; i < N; i++) {
			if (i != h) {
				mt[father[i]][--help[father[i]]] = i;
			}
		}
		for (int i = 0; i < M; i++) {
			if (queries[i][0] != queries[i][1]) {
				help[queries[i][0]]++;
				help[queries[i][1]]++;
			}
		}
		int[][] mq = new int[N][];
		int[][] mi = new int[N][];
		for (int i = 0; i < N; i++) {
			mq[i] = new int[help[i]];
			mi[i] = new int[help[i]];
		}
		for (int i = 0; i < M; i++) {
			if (queries[i][0] != queries[i][1]) {
				mq[queries[i][0]][--help[queries[i][0]]] = queries[i][1];
				mi[queries[i][0]][help[queries[i][0]]] = i;
				mq[queries[i][1]][--help[queries[i][1]]] = queries[i][0];
				mi[queries[i][1]][help[queries[i][1]]] = i;
			}
		}
		/*上面的代码，建立好树、索引库*/
		int[] ans = new int[M];
		/*树上n个节点，建立并查集*/
		UnionFind uf = new UnionFind(N);
		process(h, mt, mq, mi, uf, ans);
		for (int i = 0; i < M; i++) {
			if (queries[i][0] == queries[i][1]) {
				/*query[0]和query[0]相等，那么最低公共祖先就是两个随便哪个*/
				ans[i] = queries[i][0];
			}
		}
		return ans;
	}

	/*
	* 当前来到head点
	* mt是整棵树 head下方有哪些点 mt[head] = {a,b,c,d} head的孩子是abcd
	* mq问题列表 head有哪些问题 mq[head] = {x,y,z} (head，x) (head，y) (head z)
	* mi得到问题的答案，填在ans的什么地方 mi[head] = {6,12,34}
	* 把答案mq中的问题求出答案，通过mi找到在ans中的位置，填好返回
	* */
	public static void process(int head, int[][] mt, int[][] mq, int[][] mi, UnionFind uf, int[] ans) {
		for (int next : mt[head]) { // head有哪些孩子，都遍历去吧！
			process(next, mt, mq, mi, uf, ans);
			/*head和孩子合并*/
			uf.union(head, next);
			/*head所在集合的tag：head*/
			uf.setTag(head, head);
		}
		// 解决head的问题！
		int[] q = mq[head];
		int[] i = mi[head];
		for (int k = 0; k < q.length; k++) {
			// head和谁有问题 q[k] 答案填哪 i[k]
			int tag = uf.getTag(q[k]);
			if (tag != -1) {
				/*head的对象没有tag，就先搁置，有tag，tag就是他们的最低公共祖先*/
				ans[i[k]] = tag;
			}
		}
	}

	public static class UnionFind {
		private int[] f; // father -> 并查集里面father信息，i -> i的father
		private int[] s; // size[] -> 集合 --> i size[i]
		/*非并查集标准信息，这里记录代表节点的tag，就是整个集合的tag*/
		private int[] t; // tag[] -> 集合 ---> tag[i] = ?
		private int[] h; // 栈？并查集搞扁平化

		public UnionFind(int N) {
			f = new int[N];
			s = new int[N];
			t = new int[N];
			h = new int[N];
			for (int i = 0; i < N; i++) {
				f[i] = i;
				s[i] = 1;
				t[i] = -1;
			}
		}

		private int find(int i) {
			int j = 0;
			// i -> j -> k -> s -> a -> a
			while (i != f[i]) {
				h[j++] = i;
				i = f[i];
			}
			// i -> a
			// j -> a
			// k -> a
			// s -> a
			while (j > 0) {
				h[--j] = i;
			}
			// a
			return i;
		}

		public void union(int i, int j) {
			int fi = find(i);
			int fj = find(j);
			if (fi != fj) {
				int si = s[fi];
				int sj = s[fj];
				int m = si >= sj ? fi : fj;
				int l = m == fi ? fj : fi;
				f[l] = m;
				s[m] += s[l];
			}
		}

		// 集合的某个元素是i，请把整个集合打上统一的标签，tag
		public void setTag(int i, int tag) {
			t[find(i)] = tag;
		}

		// 集合的某个元素是i，请把整个集合的tag信息返回
		public int getTag(int i) {
			return t[find(i)];
		}

	}

	// 在线查询最优解 -> 树链剖分
	// 空间复杂度O(N), 支持在线查询，单次查询时间复杂度O(logN)
	// 如果有M次查询，时间复杂度O(N + M * logN)
	public static int[] query3(int[] father, int[][] queries) {
		TreeChain tc = new TreeChain(father);
		int M = queries.length;
		int[] ans = new int[M];
		for (int i = 0; i < M; i++) {
			// x y ?
			// x x x
			if (queries[i][0] == queries[i][1]) {
				ans[i] = queries[i][0];
			} else {
				/*query[0]query[1],各自跳，一直跳到同一条嫡系链*/
				ans[i] = tc.lca(queries[i][0], queries[i][1]);
			}
		}
		return ans;
	}

	public static class TreeChain {
		private int n;
		private int h;
		private int[][] tree;
		private int[] fa;
		private int[] dep;
		private int[] son;
		private int[] siz;
		private int[] top;

		public TreeChain(int[] father) {
			initTree(father);
			dfs1(h, 0);
			dfs2(h, h);
		}

		private void initTree(int[] father) {
			n = father.length + 1;
			tree = new int[n][];
			fa = new int[n];
			dep = new int[n];
			son = new int[n];
			siz = new int[n];
			top = new int[n--];
			int[] cnum = new int[n];
			for (int i = 0; i < n; i++) {
				if (father[i] == i) {
					h = i + 1;
				} else {
					cnum[father[i]]++;
				}
			}
			tree[0] = new int[0];
			for (int i = 0; i < n; i++) {
				tree[i + 1] = new int[cnum[i]];
			}
			for (int i = 0; i < n; i++) {
				if (i + 1 != h) {
					tree[father[i] + 1][--cnum[father[i]]] = i + 1;
				}
			}
		}

		private void dfs1(int u, int f) {
			fa[u] = f;
			dep[u] = dep[f] + 1;
			siz[u] = 1;
			int maxSize = -1;
			for (int v : tree[u]) {
				dfs1(v, u);
				siz[u] += siz[v];
				if (siz[v] > maxSize) {
					maxSize = siz[v];
					son[u] = v;
				}
			}
		}

		private void dfs2(int u, int t) {
			top[u] = t;
			if (son[u] != 0) {
				dfs2(son[u], t);
				for (int v : tree[u]) {
					if (v != son[u]) {
						dfs2(v, v);
					}
				}
			}
		}

		public int lca(int a, int b) {
			a++;
			b++;
			/*
			* 每次都是深度大节点先跳，直到两个节点再同一条嫡系链中
			* */
			while (top[a] != top[b]) {
				if (dep[top[a]] > dep[top[b]]) {
					a = fa[top[a]];
				} else {
					b = fa[top[b]];
				}
			}
			/*此时a和b已经在同一条嫡系链上了，返回深度较浅的节点，最后要-1，因为树链剖分里的index都是从1开始*/
			return (dep[a] < dep[b] ? a : b) - 1;
		}
	}

	// 为了测试
	// 随机生成N个节点树，可能是多叉树，并且一定不是森林
	// 输入参数N要大于0
	public static int[] generateFatherArray(int N) {
		int[] order = new int[N];
		for (int i = 0; i < N; i++) {
			order[i] = i;
		}
		for (int i = N - 1; i >= 0; i--) {
			swap(order, i, (int) (Math.random() * (i + 1)));
		}
		int[] ans = new int[N];
		ans[order[0]] = order[0];
		for (int i = 1; i < N; i++) {
			ans[order[i]] = order[(int) (Math.random() * i)];
		}
		return ans;
	}

	// 为了测试
	// 随机生成M条查询，点有N个，点的编号在0~N-1之间
	// 输入参数M和N都要大于0
	public static int[][] generateQueries(int M, int N) {
		int[][] ans = new int[M][2];
		for (int i = 0; i < M; i++) {
			ans[i][0] = (int) (Math.random() * N);
			ans[i][1] = (int) (Math.random() * N);
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
	public static boolean equal(int[] a, int[] b) {
		if (a.length != b.length) {
			return false;
		}
		for (int i = 0; i < a.length; i++) {
			if (a[i] != b[i]) {
				return false;
			}
		}
		return true;
	}

	// 为了测试
	public static void main(String[] args) {
		int N = 1000;
		int M = 200;
		int testTime = 50000;
		System.out.println("测试开始");
		for (int i = 0; i < testTime; i++) {
			int size = (int) (Math.random() * N) + 1;
			int ques = (int) (Math.random() * M) + 1;
			int[] father = generateFatherArray(size);
			int[][] queries = generateQueries(ques, size);
			int[] ans1 = query1(father, queries);
			int[] ans2 = query2(father, queries);
			int[] ans3 = query3(father, queries);
			if (!equal(ans1, ans2) || !equal(ans1, ans3)) {
				System.out.println("出错了！");
				break;
			}
		}
		System.out.println("测试结束");
	}

}
