package class32;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

/**
 * Given an array nums of size n, return the majority element.
 *
 * The majority element is the element that appears more than ⌊n / 2⌋ times. You may assume that the majority element always exists in the array.
 *
 * 找到arr中出现次数超过一半的数
 * 思路：出现次数超过一半的数一定只有一个
 * 如果有一种机制，一次删除两个不同的数，最后没数可以删了剩下来的一定包含出现次数超过一半的数
 * 如何一次删除两个不同的数？
 * 准备一个候选candidate，一个变量计次
 * 遍历到i位置，
 * 如果没有候选，i位置数作为候选
 * 如果有候选，i位置数与候选比较，相同计次+1，不同计次-1（-1就代表删除两个不同的数）
 * 最后剩下来的candidate再验证一下出现了多少次
 * 时间复杂度O(N)
 * 空间复杂度O(1)
 *
 *
 * 进阶：找到arr中出现次数超过n/k次的数
 * 思路：
 * 出现次数超过n/2的数一定只有1个
 * 出现次数超过n/3的数一定只有2个
 * ...
 * 出现次数超过n/k的数一定只有k-1个
 * 准备一个候选池，里面包含k-1个位置
 * 候选不满时，立候选，给初始血量1
 * 遇到候选里面的数，hp+1
 * 遇到不是候选里面的数，候选又满了，这时所有的候选hp都-1，陪葬当前遇到的数，相当于一次删除了k-1个数
 * 最后候选中的数一定有出现n/K次的的，遍历一下arr确认
 * 时间复杂度O(N*K)(因为要遍历候选区给所有优选-1)
 * 空间复杂度O(K)
 */
public class Problem_0169_FindKMajority {

	public static void printHalfMajor(int[] arr) {
		int cand = 0;
		int HP = 0;
		for (int i = 0; i < arr.length; i++) {
			if (HP == 0) {
				/*hp=0表示没有候选了，立当前数为候选，初始血量1*/
				cand = arr[i];
				HP = 1;
			} else if (arr[i] == cand) {
				HP++;
			} else {
				/*这里相当于删除了当前数和候选一次*/
				HP--;
			}
		}
		/*最终没有候选剩下来*/
		if(HP == 0) {
			System.out.println("no such number.");
			return;
		}
		HP = 0;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] == cand) {
				HP++;
			}
		}
		if (HP > arr.length / 2) {
			System.out.println(cand);
		} else {
			System.out.println("no such number.");
		}
	}

	/**
     * 进阶
	 */
	public static void printKMajor(int[] arr, int K) {
		if (K < 2) {
			System.out.println("the value of K is invalid.");
			return;
		}
		// 攒候选，cands，候选表，最多K-1条记录！ > N / K次的数字，最多有K-1个
		HashMap<Integer, Integer> cands = new HashMap<Integer, Integer>();
		for (int i = 0; i != arr.length; i++) {
			if (cands.containsKey(arr[i])) {
				cands.put(arr[i], cands.get(arr[i]) + 1);
			} else { // arr[i] 不是候选
				if (cands.size() == K - 1) { // 当前数肯定不要！，每一个候选付出1点血量，血量变成0的候选，要删掉！
					allCandsMinusOne(cands);
				} else {
					cands.put(arr[i], 1);
				}
			}
		}
		// 所有可能的候选，都在cands表中！遍历一遍arr，每个候选收集真实次数
		HashMap<Integer, Integer> reals = getReals(arr, cands);
		boolean hasPrint = false;
		for (Entry<Integer, Integer> set : cands.entrySet()) {
			Integer key = set.getKey();
			if (reals.get(key) > arr.length / K) {
				hasPrint = true;
				System.out.print(key + " ");
			}
		}
		System.out.println(hasPrint ? "" : "no such number.");
	}

	public static void allCandsMinusOne(HashMap<Integer, Integer> map) {
		List<Integer> removeList = new LinkedList<Integer>();
		for (Entry<Integer, Integer> set : map.entrySet()) {
			Integer key = set.getKey();
			Integer value = set.getValue();
			if (value == 1) {
				removeList.add(key);
			}
			map.put(key, value - 1);
		}
		for (Integer removeKey : removeList) {
			map.remove(removeKey);
		}
	}

	public static HashMap<Integer, Integer> getReals(int[] arr,
			HashMap<Integer, Integer> cands) {
		HashMap<Integer, Integer> reals = new HashMap<Integer, Integer>();
		for (int i = 0; i != arr.length; i++) {
			int curNum = arr[i];
			if (cands.containsKey(curNum)) {
				if (reals.containsKey(curNum)) {
					reals.put(curNum, reals.get(curNum) + 1);
				} else {
					reals.put(curNum, 1);
				}
			}
		}
		return reals;
	}

	public static void main(String[] args) {
		int[] arr = { 1, 2, 3, 1, 1, 2, 1 };
		printHalfMajor(arr);
		int K = 4;
		printKMajor(arr, K);
	}

}
