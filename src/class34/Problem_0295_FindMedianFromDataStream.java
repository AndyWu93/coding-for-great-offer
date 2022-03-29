package class34;

import java.util.PriorityQueue;

/**
 * The median is the middle value in an ordered integer list. If the size of the list is even, there is no middle value and the median is the mean of the two middle values.
 *
 * For example, for arr = [2,3,4], the median is 3.
 * For example, for arr = [2,3], the median is (2 + 3) / 2 = 2.5.
 * Implement the MedianFinder class:
 *
 * MedianFinder() initializes the MedianFinder object.
 * void addNum(int num) adds the integer num from the data stream to the data structure.
 * double findMedian() returns the median of all elements so far. Answers within 10-5 of the actual answer will be accepted.
 * 题意：设计一种结构，包含一个构造和两个api
 * 	api1：添加一个数
 * 	api2：此时的中位数
 * 要求时间复杂度O(logN)
 * 解题：
 * 	如果加入的数字都是不重复的，有序表可以做，但是有序表中需要拿index位置的数，所以设计有序表改造
 * 	如果加入的数可以重复，本题用了一种更好的设计，通过两个堆来维护大小各半的数：
 * 		较小的数进大根堆
 * 		较大的数进小根堆
 * 		大根堆的堆顶作为判断标准
 */
public class Problem_0295_FindMedianFromDataStream {

	class MedianFinder {

		private PriorityQueue<Integer> maxh;
		private PriorityQueue<Integer> minh;

		public MedianFinder() {
			maxh = new PriorityQueue<>((a, b) -> b - a);
			minh = new PriorityQueue<>((a, b) -> a - b);
		}

		public void addNum(int num) {
			/*
			* 大根堆是空，表示目前一个数都没有
			* 否则就可以作为判断标准s
			* 	小于等于s的数进大根堆
			* 	大于s的数进小根堆
			* */
			if (maxh.isEmpty() || maxh.peek() >= num) {
				maxh.add(num);
			} else {
				minh.add(num);
			}
			/*调用平衡*/
			balance();
		}

		public double findMedian() {
			if (maxh.size() == minh.size()) {
				/*size相等：两个堆顶求平均数*/
				return (double) (maxh.peek() + minh.peek()) / 2;
			} else {
				/*size不等：较大的堆顶就是中位数*/
				return maxh.size() > minh.size() ? maxh.peek() : minh.peek();
			}
		}

		/*
		* 两个堆的size相差2就需要平衡，多的堆给一个给少的
		* */
		private void balance() {
			if (Math.abs(maxh.size() - minh.size()) == 2) {
				if (maxh.size() > minh.size()) {
					minh.add(maxh.poll());
				} else {
					maxh.add(minh.poll());
				}
			}
		}

	}

}