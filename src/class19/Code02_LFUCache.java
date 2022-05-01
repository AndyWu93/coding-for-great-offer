package class19;

import java.util.HashMap;

/**
 * Design and implement a data structure for a Least Frequently Used (LFU) cache.
 *
 * Implement the LFUCache class:
 *
 * LFUCache(int capacity) Initializes the object with the capacity of the data structure.
 * int get(int key) Gets the value of the key if the key exists in the cache. Otherwise, returns -1.
 * void put(int key, int value) Update the value of the key if present, or inserts the key if not already present. When the cache reaches its capacity, it should invalidate and remove the least frequently used key before inserting a new item. For this problem, when there is a tie (i.e., two or more keys with the same frequency), the least recently used key would be invalidated.
 * To determine the least frequently used key, a use counter is maintained for each key in the cache. The key with the smallest use counter is the least frequently used key.
 *
 * When a key is first inserted into the cache, its use counter is set to 1 (due to the put operation). The use counter for a key in the cache is incremented either a get or put operation is called on it.
 *
 * The functions get and put must each run in O(1) average time complexity.
 * 题意：实现一个缓存：
 *  固定大小，可以查询，插入，更新操作，要求时间复杂度O(1)
 *  缓存满了，要再加入一个缓存，就移除最早被操作过且操作次数最少的缓存
 * 解题：
 * 	本题考查结构设计，hard级别
 * 	整体是二维deque的结构
 * 	deque1：按时间先后连接起来的节点
 * 	deque2：按操作次数大小连接起来的桶，桶里维护着deque1
 * 	map1:key: 缓存的key，value：节点
 * 	map2：key：节点，value：桶
 * 	删除：删掉最左边的桶里的最上边的节点
 */
// 本题测试链接 : https://leetcode.com/problems/lfu-cache/
// 提交时把类名和构造方法名改为 : LFUCache
public class Code02_LFUCache {

	private int capacity; // 缓存的大小限制，即K
	private int size; // 缓存目前有多少个节点
	private HashMap<Integer, Node> records;// 表示key(Integer)由哪个节点(Node)代表
	private HashMap<Node, Bucket> heads; // 表示节点(Node)在哪个桶(Bucket)里
	/*维护一个桶*/
	private Bucket headBucket; // 整个结构中位于最左的桶

	public Code02_LFUCache(int K) {
		capacity = K;
		size = 0;
		records = new HashMap<>();
		heads = new HashMap<>();
		headBucket = null;
	}


	// 节点的数据结构
	public static class Node {
		public Integer key;
		public Integer value;
		public Integer times; // 这个节点发生get或者set的次数总和
		public Node up; // 节点之间是双向链表所以有上一个节点
		public Node down;// 节点之间是双向链表所以有下一个节点

		public Node(int k, int v, int t) {
			key = k;
			value = v;
			times = t;
		}
	}

	// 桶结构
	public static class Bucket {
		/*head,tail,维护一个双向链表*/
		public Node head; // 桶的头节点
		public Node tail; // 桶的尾节点
		/*桶自己是链表结构*/
		public Bucket previous; // 桶之间是双向链表所以有前一个桶
		public Bucket next; // 桶之间是双向链表所以有后一个桶

		public Bucket(Node node) {
			head = node;
			tail = node;
		}

		// 把一个新的节点加入这个桶，新的节点都放在顶端变成新的头部
		public void addNodeFromHead(Node newHead) {
			newHead.down = head;
			head.up = newHead;
			head = newHead;
		}

		// 判断这个桶是不是空的
		public boolean isEmpty() {
			return head == null;
		}

		// 删除node节点并保证node的上下环境重新连接
		public void deleteNode(Node node) {
			if (head == tail) {
				head = null;
				tail = null;
			} else {
				if (node == head) {
					head = node.down;
					head.up = null;
				} else if (node == tail) {
					tail = node.up;
					tail.down = null;
				} else {
					node.up.down = node.down;
					node.down.up = node.up;
				}
			}
			node.up = null;
			node.down = null;
		}
	}

	// removeBucket：刚刚减少了一个节点的桶
	// 这个函数的功能是，判断刚刚减少了一个节点的桶是不是已经空了。
	// 1）如果不空，什么也不做
	//
	// 2)如果空了，removeBucket还是整个缓存结构最左的桶(headList)。
	// 删掉这个桶的同时也要让最左的桶变成removeBucket的下一个。
	//
	// 3)如果空了，removeBucket不是整个缓存结构最左的桶(headList)。
	// 把这个桶删除，并保证上一个的桶和下一个桶之间还是双向链表的连接方式
	//
	// 函数的返回值表示刚刚减少了一个节点的桶是不是已经空了，空了返回true；不空返回false
	private boolean modifyHeadList(Bucket removeBucket) {
		if (removeBucket.isEmpty()) {
			if (headBucket == removeBucket) {
				headBucket = removeBucket.next;
				if (headBucket != null) {
					headBucket.previous = null;
				}
			} else {
				removeBucket.previous.next = removeBucket.next;
				if (removeBucket.next != null) {
					removeBucket.next.previous = removeBucket.previous;
				}
			}
			return true;
		}
		return false;
	}

	// 函数的功能
	// node这个节点的次数+1了，这个节点原来在oldBucket里。
	// 把node从oldBucket删掉，然后放到次数+1的桶中
	// 整个过程既要保证桶之间仍然是双向链表，也要保证节点之间仍然是双向链表
	private void move(Node node, Bucket oldBucket) {
		oldBucket.deleteNode(node);
		// preList表示次数+1的桶的前一个桶是谁
		// 如果oldBucket删掉node之后还有节点，oldBucket就是次数+1的桶的前一个桶
		// 如果oldBucket删掉node之后空了，oldBucket是需要删除的，所以次数+1的桶的前一个桶，是oldBucket的前一个
		Bucket preList = modifyHeadList(oldBucket) ? oldBucket.previous : oldBucket;
		// nextList表示次数+1的桶的后一个桶是谁
		Bucket nextList = oldBucket.next;
		if (nextList == null) {
			Bucket newList = new Bucket(node);
			if (preList != null) {
				preList.next = newList;
			}
			newList.previous = preList;
			if (headBucket == null) {
				headBucket = newList;
			}
			heads.put(node, newList);
		} else {
			if (nextList.head.times.equals(node.times)) {
				nextList.addNodeFromHead(node);
				heads.put(node, nextList);
			} else {
				Bucket newList = new Bucket(node);
				if (preList != null) {
					preList.next = newList;
				}
				newList.previous = preList;
				newList.next = nextList;
				nextList.previous = newList;
				if (headBucket == nextList) {
					headBucket = newList;
				}
				heads.put(node, newList);
			}
		}
	}

	public void put(int key, int value) {
		if (capacity == 0) {
			return;
		}
		if (records.containsKey(key)) {
			Node node = records.get(key);
			node.value = value;
			node.times++;
			Bucket curBucket = heads.get(node);
			move(node, curBucket);
		} else {
			if (size == capacity) {
				Node node = headBucket.tail;
				headBucket.deleteNode(node);
				modifyHeadList(headBucket);
				records.remove(node.key);
				heads.remove(node);
				size--;
			}
			Node node = new Node(key, value, 1);
			if (headBucket == null) {
				headBucket = new Bucket(node);
			} else {
				if (headBucket.head.times.equals(node.times)) {
					headBucket.addNodeFromHead(node);
				} else {
					Bucket newList = new Bucket(node);
					newList.next = headBucket;
					headBucket.previous = newList;
					headBucket = newList;
				}
			}
			records.put(key, node);
			heads.put(node, headBucket);
			size++;
		}
	}

	public int get(int key) {
		if (!records.containsKey(key)) {
			return -1;
		}
		Node node = records.get(key);
		node.times++;
		Bucket curBucket = heads.get(node);
		move(node, curBucket);
		return node.value;
	}

}