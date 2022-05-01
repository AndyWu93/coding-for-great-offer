package class19;

import java.util.HashMap;

/**
 * Design a data structure that follows the constraints of a Least Recently Used (LRU) cache.
 *
 * Implement the LRUCache class:
 *
 * LRUCache(int capacity) Initialize the LRU cache with positive size capacity.
 * int get(int key) Return the value of the key if the key exists, otherwise return -1.
 * void put(int key, int value) Update the value of the key if the key exists. Otherwise, add the key-value pair to the cache. If the number of keys exceeds the capacity from this operation, evict the least recently used key.
 * The functions get and put must each run in O(1) average time complexity.
 * 题意：实现一个缓存：
 * 	固定大小，可以查询，插入，更新操作，要求时间复杂度O(1)
 * 	缓存满了，要再加入一个缓存，就移除最早被操作过的缓存
 * 解题：
 * 	本题考查结构设计
 * 	使用双向链表+map就可以实现，map实现高效增删改查，deque按操作时间串好
 * 	双向链表：从头到尾表示时间的由远及近
 * 	map：key：缓存的key，value：缓存的整体构成的节点
 */
// 本题测试链接 : https://leetcode.com/problems/lru-cache/
// 提交时把类名和构造方法名改成 : LRUCache
public class Code01_LRUCache {

	public Code01_LRUCache(int capacity) {
		cache = new MyCache<>(capacity);
	}

	private MyCache<Integer, Integer> cache;

	public int get(int key) {
		Integer ans = cache.get(key);
		return ans == null ? -1 : ans;
	}

	public void put(int key, int value) {
		cache.set(key, value);
	}

	public static class Node<K, V> {
		public K key;
		public V value;
		public Node<K, V> previous;
		public Node<K, V> next;

		public Node(K key, V value) {
			this.key = key;
			this.value = value;
		}
	}

	/*
	* 自实现一个双向链表
	* */
	public static class NodeDoubleLinkedList<K, V> {
		private Node<K, V> head;
		private Node<K, V> tail;

		public NodeDoubleLinkedList() {
			head = null;
			tail = null;
		}

		// 现在来了一个新的node，请挂到尾巴上去
		public void addNode(Node<K, V> newNode) {
			if (newNode == null) {
				return;
			}
			if (head == null) {
				head = newNode;
				tail = newNode;
			} else {
				tail.next = newNode;
				newNode.previous = tail;
				tail = newNode;
			}
		}

		// node 入参，一定保证！node在双向链表里！
		// node原始的位置，左右重新连好，然后把node分离出来
		// 挂到整个链表的尾巴上
		public void moveNodeToTail(Node<K, V> node) {
			if (tail == node) {
				return;
			}
			/*node断开前后连接*/
			if (head == node) {
				head = node.next;
				head.previous = null;
			} else {
				node.previous.next = node.next;
				node.next.previous = node.previous;
			}
			/*node 挂到尾巴上去*/
			node.previous = tail;
			node.next = null;
			tail.next = node;
			tail = node;
		}

		public Node<K, V> removeHead() {
			if (head == null) {
				return null;
			}
			Node<K, V> res = head;
			if (head == tail) {
				head = null;
				tail = null;
			} else {
				head = res.next;
				res.next = null;
				head.previous = null;
			}
			return res;
		}

	}


	 /*
	 * 缓存的结构
	 * */
	public static class MyCache<K, V> {
		/*map*/
		private HashMap<K, Node<K, V>> keyNodeMap;
		/*deque*/
		private NodeDoubleLinkedList<K, V> nodeList;
		/*容量*/
		private final int capacity;

		public MyCache(int cap) {
			keyNodeMap = new HashMap<K, Node<K, V>>();
			nodeList = new NodeDoubleLinkedList<K, V>();
			capacity = cap;
		}

		public V get(K key) {
			if (keyNodeMap.containsKey(key)) {
				Node<K, V> res = keyNodeMap.get(key);
				/*被操作了，移到尾部*/
				nodeList.moveNodeToTail(res);
				return res.value;
			}
			return null;
		}

		// set(Key, Value)
		// 新增  更新value的操作
		public void set(K key, V value) {
			if (keyNodeMap.containsKey(key)) {
				Node<K, V> node = keyNodeMap.get(key);
				node.value = value;
				/*有的话，更新，再移到尾部*/
				nodeList.moveNodeToTail(node);
			} else { // 新增！
				Node<K, V> newNode = new Node<K, V>(key, value);
				keyNodeMap.put(key, newNode);
				/*没有的话新增，节点挂在尾部，每次新增完都要检查容量*/
				nodeList.addNode(newNode);
				if (keyNodeMap.size() == capacity + 1) {
					/*remove least recent used cache*/
					removeMostUnusedCache();
				}
			}
		}

		private void removeMostUnusedCache() {
			/*deque和map中都要删除*/
			Node<K, V> removeNode = nodeList.removeHead();
			keyNodeMap.remove(removeNode.key);
		}

	}

}
