package class34;

import java.util.HashMap;

/**
 * Implement the RandomizedSet class:
 *
 * RandomizedSet() Initializes the RandomizedSet object.
 * bool insert(int val) Inserts an item val into the set if not present. Returns true if the item was not present, false otherwise.
 * bool remove(int val) Removes an item val from the set if present. Returns true if the item was present, false otherwise.
 * int getRandom() Returns a random element from the current set of elements (it's guaranteed that at least one element exists when this method is called). Each element must have the same probability of being returned.
 * You must implement the functions of the class such that each function works in average O(1) time complexity.
 * 题意：实现一个结构，有3个方法：
 * 	bool insert(int val) ：添加一个值，重复的返回false
 * 	bool remove(int val) ：删除一个值
 * 	int getRandom() ：等概率返回现在结构里有的数
 * 解题：
 * 	用两个map，一个size
 * 		map1：val->index
 * 		map2：index->val
 * 	getRandom时，对size-1取随机数，得到的index到map2中找
 * 	remove时，remove之后，将最后一个值放到被remove的空位上去
 */
// 测试链接 : https://leetcode.com/problems/insert-delete-getrandom-o1/
public class Problem_0380_InsertDeleteGetRandom {

	public class RandomizedSet {

		private HashMap<Integer, Integer> keyIndexMap;
		private HashMap<Integer, Integer> indexKeyMap;
		private int size;

		public RandomizedSet() {
			keyIndexMap = new HashMap<Integer, Integer>();
			indexKeyMap = new HashMap<Integer, Integer>();
			size = 0;
		}

		public boolean insert(int val) {
			if (!keyIndexMap.containsKey(val)) {
				keyIndexMap.put(val, size);
				indexKeyMap.put(size++, val);
				return true;
			}
			return false;
		}

		public boolean remove(int val) {
			if (keyIndexMap.containsKey(val)) {
				int deleteIndex = keyIndexMap.get(val);
				int lastIndex = --size;
				int lastKey = indexKeyMap.get(lastIndex);
				keyIndexMap.put(lastKey, deleteIndex);
				indexKeyMap.put(deleteIndex, lastKey);
				keyIndexMap.remove(val);
				indexKeyMap.remove(lastIndex);
				return true;
			}
			return false;
		}

		public int getRandom() {
			if (size == 0) {
				return -1;
			}
			int randomIndex = (int) (Math.random() * size);
			return indexKeyMap.get(randomIndex);
		}
	}

}
