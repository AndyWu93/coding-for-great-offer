package class20;

import java.util.HashMap;

/**
 * 给定一个由不同正整数的组成的非空数组 A，考虑下面的图：有 A.length 个节点，按从 A[0] 到 A[A.length - 1] 标记；
 * 只有当 A[i] 和 A[j] 共用一个大于 1 的公因数时，A[i] 和 A[j] 之间才有一条边。返回图中最大连通集合的大小
 *
 * 题意：给定数组arr，arr中两个数的gcd不是1，这两个数属于一个集合，求arr中最大集合里几个元素
 * 解题：
 * 	题目中已经给出了，一定是用并查集
 * 思路1
 * 	枚举arr中所有的两个元素，检查，合并，复杂度O(N^2)，本题过不了
 * 思路2
 * 	遍历arr，建立一张因子对应表map，每个数分解出来的质数因子，都放入这表中
 * 	当来到i位置，分解出了3个因子，分别看着3个因子在map中有没有对应，有的话拿出来与i合并，没有的话，把i的因子放入表中
 * 优化与思考：
 * 	map中的因子一定要是质数吗？
 * 		不一定要是质数，不影响结果，而且降低了难度
 *  如何分解arr[i]，复杂度怎么算？
 *  	借鉴求一个数是否是质数的方法（见拓展），从1~根号下arr[i],依次判断是否是arr[i]的因子，比如判断a是arr[i]的因子，那另一个因子就是arr[i]/a
 *  	复杂度O(根号下v)
 *  所以思路2的复杂度：O(N*根号下V) （V:arr中最大的数）
 *
 *  拓展：判断一个数v是否是质数
 *  从1~根号下v（用a表示），依次 v%a==0？是： 表示a是v的因子，而且v/a也是v的因子，最后看因子是不是只有2个
 *  为什么到根号下v就可以停了，因为根号下v后面的数，前面都算过了
 */
// 本题为leetcode原题
// 测试链接：https://leetcode.com/problems/largest-component-size-by-common-factor/
// 方法1会超时，但是方法2直接通过
public class Code02_LargestComponentSizebyCommonFactor {

	public static int largestComponentSize1(int[] arr) {
		int N = arr.length;
		UnionFind set = new UnionFind(N);
		for (int i = 0; i < N; i++) {
			for (int j = i + 1; j < N; j++) {
				if (gcd(arr[i], arr[j]) != 1) {
					set.union(i, j);
				}
			}
		}
		return set.maxSize();
	}

	/**
	 * 复杂度O(N*根号下V)
	 */
	public static int largestComponentSize2(int[] arr) {
		int N = arr.length;
		// arr中，N个位置，在并查集初始时，每个位置自己是一个集合
		UnionFind unionFind = new UnionFind(N);
		/*
		* key 某个因子   value 哪个位置拥有这个因子
		* */
		HashMap<Integer, Integer> fatorsMap = new HashMap<>();
		for (int i = 0; i < N; i++) {
			int num = arr[i];
			// 求出根号N， -> limit
			int limit = (int) Math.sqrt(num);
			for (int j = 1; j <= limit; j++) {
				if (num % j == 0) {
					/*j是num的因子*/
					if (j != 1) {
						if (!fatorsMap.containsKey(j)) {
							/*表中没有，就加入*/
							fatorsMap.put(j, i);
						} else {
							/*表中有，不用加入了，拿出来与num合并*/
							unionFind.union(fatorsMap.get(j), i);
						}
					}
					/*另一个因子也能拿到*/
					int other = num / j;
					if (other != 1) {
						if (!fatorsMap.containsKey(other)) {
							fatorsMap.put(other, i);
						} else {
							unionFind.union(fatorsMap.get(other), i);
						}
					}
				}
			}
		}
		return unionFind.maxSize();
	}

	// O(1)
	// m,n 要是正数，不能有任何一个等于0
	public static int gcd(int a, int b) {
		return b == 0 ? a : gcd(b, a % b);
	}

	public static class UnionFind {
		/*parents[2]=4 : 节点2的代表节点是4*/
		private int[] parents;
		/*每一个代表节点的size*/
		private int[] sizes;
		/*优化到代表节点的距离*/
		private int[] help;

		public UnionFind(int N) {
			parents = new int[N];
			sizes = new int[N];
			help = new int[N];
			for (int i = 0; i < N; i++) {
				parents[i] = i;
				sizes[i] = 1;
			}
		}

		public int maxSize() {
			int ans = 0;
			for (int size : sizes) {
				ans = Math.max(ans, size);
			}
			return ans;
		}

		private int find(int i) {
			int hi = 0;
			while (i != parents[i]) {
				help[hi++] = i;
				i = parents[i];
			}
			for (hi--; hi >= 0; hi--) {
				parents[help[hi]] = i;
			}
			return i;
		}

		public void union(int i, int j) {
			int f1 = find(i);
			int f2 = find(j);
			if (f1 != f2) {
				int big = sizes[f1] >= sizes[f2] ? f1 : f1;
				int small = big == f1 ? f2 : f1;
				parents[small] = big;
				sizes[big] = sizes[f1] + sizes[f2];
				/*标准并查集：不是代表节点了，要从size中抹掉，但是这里不加不影响本题结果*/
				sizes[small] = 0;
			}
		}
	}

}
