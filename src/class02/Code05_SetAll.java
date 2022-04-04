package class02;

import java.util.HashMap;

/**
 * 如何在map中添加一个setAll方法，依旧保持时间复杂度O(1)
 * 给每一个值添加时间戳，相当于版本
 */
public class Code05_SetAll {

	public static class MyValue<V> {
		public V value;
		public long time;

		public MyValue(V v, long t) {
			value = v;
			time = t;
		}
	}

	public static class MyHashMap<K, V> {
		private HashMap<K, MyValue<V>> map;
		/*全局变量*/
		private long time;
		/*维护setAll，一个单独的值*/
		private MyValue<V> setAll;

		public MyHashMap() {
			map = new HashMap<>();
			time = 0;
			/*初始值null，时间-1*/
			setAll = new MyValue<V>(null, -1);
		}

		public void put(K key, V value) {
			/*put的时候，把时间带上*/
			map.put(key, new MyValue<V>(value, time++));
		}

		public void setAll(V value) {
			setAll = new MyValue<V>(value, time++);
		}

		public V get(K key) {
			if (!map.containsKey(key)) {
				return null;
			}
			/*get的时候看下时间，如果大于all的时间，表示是在all之后的，就是安全的，否则就要返回all的值*/
			if (map.get(key).time > setAll.time) {
				return map.get(key).value;
			} else {
				return setAll.value;
			}
		}
	}

}
