package class34;

import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * You are given a nested list of integers nestedList. Each element is either an integer or a list whose elements may also be integers or other lists. Implement an iterator to flatten it.
 *
 * Implement the NestedIterator class:
 *
 * NestedIterator(List<NestedInteger> nestedList) Initializes the iterator with the nested list nestedList.
 * int next() Returns the next integer in the nested list.
 * boolean hasNext() Returns true if there are still some integers in the nested list and false otherwise.
 * Your code will be tested with the following pseudocode:
 *
 * initialize iterator with nestedList
 * res = []
 * while iterator.hasNext()
 *     append iterator.next() to the end of res
 * return res
 * If res matches the expected flattened list, then your code will be judged as correct.
 *
 * 题意：给一个可以无限嵌套的结构写迭代器
 * 解题：
 * 	本题考察深度优先遍历
 * 	思路：
 * 	用栈记住目前遍历到了哪个位置，栈中记录了此时节点的深度。
 * 	难点：
 * 	寻找下一个：根据当前栈路径，把下一个元素的路径压进去，注意下一个元素的路径可能和当前元素路径不一样长
 */
public class Problem_0341_FlattenNestedListIterator {

	// 不要提交这个接口类
	public interface NestedInteger {

		// @return true if this NestedInteger holds a single integer, rather than a
		// nested list.
		public boolean isInteger();

		// @return the single integer that this NestedInteger holds, if it holds a
		// single integer
		// Return null if this NestedInteger holds a nested list
		public Integer getInteger();

		// @return the nested list that this NestedInteger holds, if it holds a nested
		// list
		// Return null if this NestedInteger holds a single integer
		public List<NestedInteger> getList();
	}

	public class NestedIterator implements Iterator<Integer> {

		private List<NestedInteger> list;
		private Stack<Integer> stack;
		private boolean used;

		public NestedIterator(List<NestedInteger> nestedList) {
			list = nestedList;
			stack = new Stack<>();
			stack.push(-1);
			used = true;
			hasNext();
		}

		@Override
		public Integer next() {
			Integer ans = null;
			if (!used) {
				ans = get(list, stack);
				used = true;
				hasNext();
			}
			return ans;
		}

		@Override
		public boolean hasNext() {
			if (stack.isEmpty()) {
				return false;
			}
			if (!used) {
				return true;
			}
			if (findNext(list, stack)) {
				used = false;
			}
			return !used;
		}

		private Integer get(List<NestedInteger> nestedList, Stack<Integer> stack) {
			int index = stack.pop();
			Integer ans = null;
			if (!stack.isEmpty()) {
				/*递归去拿*/
				ans = get(nestedList.get(index).getList(), stack);
			} else {
				/*直接拿index位置的数*/
				ans = nestedList.get(index).getInteger();
			}
			/*记得还原现场*/
			stack.push(index);
			return ans;
		}

		private boolean findNext(List<NestedInteger> nestedList, Stack<Integer> stack) {
			int index = stack.pop();
			/*如果stack不空，表明还可以再深入，所以调用后面nestedList.get(index).getList()*/
			if (!stack.isEmpty() && findNext(nestedList.get(index).getList(), stack)) {
				stack.push(index);
				return true;
			}
			/*
			* stack空了，那当前index位置就是数字，看看它下一个是什么
			* 没有下一个的话，这个for循环就直接退出了
			* */
			for (int i = index + 1; i < nestedList.size(); i++) {
				if (pickFirst(nestedList.get(i), i, stack)) {
					return true;
				}
			}
			return false;
		}

		private boolean pickFirst(NestedInteger nested, int position, Stack<Integer> stack) {
			if (nested.isInteger()) {
				/*这个位置是数字，就在栈中记录*/
				stack.add(position);
				return true;
			} else {
				List<NestedInteger> actualList = nested.getList();
				for (int i = 0; i < actualList.size(); i++) {
					if (pickFirst(actualList.get(i), i, stack)) {
						/*这里压入的list的index，数字的index在if判断时递归压入了*/
						stack.add(position);
						return true;
					}
				}
			}
			return false;
		}

	}

}
