package class18;

/**
 * 给定两个有序数组arr1和arr2，再给定一个整数k，返回来自arr1和arr2的两个数相加和最大的前k个，两个数必须分别来自两个数组，按照降序输出
 * 时间复杂度为O(klogk)
 * 输入描述：
 * 第一行三个整数N, K分别表示数组arr1, arr2的大小，以及需要询问的数
 * 接下来一行N个整数，表示arr1内的元素
 * 再接下来一行N个整数，表示arr2内的元素
 * 输出描述：
 * 输出K个整数表示答案
 *
 * 题意：从两个有序数组中各取一个任意数作为一个组合（一共M*N个组合），求组合里sum最大的前k个
 *
 * 思路：一个样本作行，一个样本作列，但不是dp
 * (arr1[n-1],arr2[n-1])这个组合一定是sum最大的组合
 * 准备一个组合对象的大根堆，和一个标记样本组合的set
 * 先把右下角定义成对象(i,j,sum)放入大根堆，标记set[i][j]=true
 * 弹出堆顶，收集结果sum，再将弹出对象左边位置代表的组合和上边位置代表的组合放入大根堆，两个组合在set中标记为true
 * 弹出堆顶，收集结果sum，再将...
 * 直到收集满K个
 */
// 牛客的测试链接：
// https://www.nowcoder.com/practice/7201cacf73e7495aa5f88b223bbbf6d1
// 不要提交包信息，把import底下的类名改成Main，提交下面的代码可以直接通过
// 因为测试平台会卡空间，所以把set换成了动态加和减的结构
import java.util.Scanner;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;

public class Code04_TopKSumCrossTwoArrays {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int N = sc.nextInt();
		int K = sc.nextInt();
		int[] arr1 = new int[N];
		int[] arr2 = new int[N];
		for (int i = 0; i < N; i++) {
			arr1[i] = sc.nextInt();
		}
		for (int i = 0; i < N; i++) {
			arr2[i] = sc.nextInt();
		}
		int[] topK = topKSum(arr1, arr2, K);
		for (int i = 0; i < K; i++) {
			System.out.print(topK[i] + " ");
		}
		System.out.println();
		sc.close();
	}

	// 放入大根堆中的结构
	public static class Node {
		public int index1;// arr1中的位置
		public int index2;// arr2中的位置
		public int sum;// arr1[index1] + arr2[index2]的值

		public Node(int i1, int i2, int s) {
			index1 = i1;
			index2 = i2;
			sum = s;
		}
	}

	// 生成大根堆的比较器
	public static class MaxHeapComp implements Comparator<Node> {
		@Override
		public int compare(Node o1, Node o2) {
			return o2.sum - o1.sum;
		}
	}

	/**
	 * 复杂度：
	 * while中循环k次找到k个数 O(K)
	 * 每次通过堆弹出最大值，堆的最大容量不会超过k O(logK)
	 * 所以：O(K*logK)
	 */
	public static int[] topKSum(int[] arr1, int[] arr2, int topK) {
		if (arr1 == null || arr2 == null || topK < 1) {
			return null;
		}
		int N = arr1.length;
		int M = arr2.length;
		/*组合个数：最大N * M个*/
		topK = Math.min(topK, N * M);
		/*res和resIndex,来模拟ArrayList收集结果*/
		int[] res = new int[topK];
		int resIndex = 0;
		/*大根堆*/
		PriorityQueue<Node> maxHeap = new PriorityQueue<>(new MaxHeapComp());
		/*
		* 组合标记set
		* 可以用boolean[i][j]来标记
		* 但是这里用一个set<Long>,节省了很大的空间
		* 进入set的就表示boolean[i][j]=true，
		* (i,j)如何转化为long进入set？二维变一维
		* */
		HashSet<Long> set = new HashSet<>();
		int i1 = N - 1;
		int i2 = M - 1;
		/*右下角加进去*/
		maxHeap.add(new Node(i1, i2, arr1[i1] + arr2[i2]));
		/*右下角标记为true*/
		set.add(x(i1, i2, M));
		while (resIndex != topK) {
			Node curNode = maxHeap.poll();
			res[resIndex++] = curNode.sum;
			i1 = curNode.index1;
			i2 = curNode.index2;
			/*
			* 弹出的点可以不用再进行标记，为什么？
			* 因为在这个代码流程中，这个组合后面是不可能再出现了，弹出可以省空间
			* */
			set.remove(x(i1, i2, M));
			/*弹出位置的上边*/
			if (i1 - 1 >= 0 && !set.contains(x(i1 - 1, i2, M))) {
				set.add(x(i1 - 1, i2, M));
				maxHeap.add(new Node(i1 - 1, i2, arr1[i1 - 1] + arr2[i2]));
			}
			/*弹出位置的左边*/
			if (i2 - 1 >= 0 && !set.contains(x(i1, i2 - 1, M))) {
				set.add(x(i1, i2 - 1, M));
				maxHeap.add(new Node(i1, i2 - 1, arr1[i1] + arr2[i2 - 1]));
			}
		}
		return res;
	}

	public static long x(int i1, int i2, int M) {
		return (long) i1 * (long) M + (long) i2;
	}

}
