package class44;

/**
 * 给定两个字符串low和high，它们只由数字字符组成，代表两个数字并且low<=high，返回在[low, high]范围上旋转有效串的数量
 * 旋转有效串：一个字符串以中心点为支点，旋转180度之后依然是原来字符串，叫做旋转有效串，比如：
 * 181旋转之后还是181，是旋转有效串
 * 8008旋转之后还是8008，是旋转有效串
 * 689旋转之后还是689，是旋转有效串
 * 而6816就不是旋转有效串，因为旋转之后是9189
 *
 * 解题：
 * 	数位dp
 * 整体思路：
 * 	先确定能够旋转的数字
 * 	对于一个low和high
 * 	计算出low到9*范围内符合条件的个数
 * 	在计算出10*到high范围内的个数
 * 	中间的范围，按位数直接计算
 * 	3部分的结果相加
 */
//leetcode题目 : https://leetcode.com/problems/strobogrammatic-number-iii/
public class Problem_0248_StrobogrammaticNumberIII {

	public static int strobogrammaticInRange(String l, String h) {
		char[] low = l.toCharArray();
		char[] high = h.toCharArray();
		if (!equalMore(low, high)) {
			/*确保high大于等于low，否则返回0个*/
			return 0;
		}
		int lowLen = low.length;
		int highLen = high.length;
		if (lowLen == highLen) {
			/*
			* high和low的长度相同，假设都是3位
			* 计算low到999符合条件的个数 x
			* 计算high到999符合条件的个数 y
			* x-y; 如果high本身也符合条件，x-y+1
			* */
			int up1 = up(low, 0, false, 1);
			int up2 = up(high, 0, false, 1);
			return up1 - up2 + (valid(high) ? 1 : 0);
		}
		/*
		* high和low的长度不相同，分3部分，分别计算
		* */
		int ans = 0;
		/*
		* 第一部分：假设lowLen = 3 highLen = 7
		* 那就是计算长度是 4 5 6 的所有数字中，符合条件的个数
		* */
		for (int i = lowLen + 1; i < highLen; i++) {
			ans += all(i);
		}
		/*
		* 第二部分：假设lowLen = 3，low一直到999范围内符合条件的个数
		* */
		ans += up(low, 0, false, 1);
		/*
		* 第三部分：假设highLen = 7，1000000一直到high范围内符合条件的个数
		* */
		ans += down(high, 0, false, 1);
		return ans;
	}

	/*
	* cur是否>=low
	* */
	public static boolean equalMore(char[] low, char[] cur) {
		if (low.length != cur.length) {
			return low.length < cur.length;
		}
		for (int i = 0; i < low.length; i++) {
			if (low[i] != cur[i]) {
				return low[i] < cur[i];
			}
		}
		return true;
	}

	/*
	* str是否是可旋转数
	* */
	public static boolean valid(char[] str) {
		int L = 0;
		int R = str.length - 1;
		while (L <= R) {
			boolean t = L != R;
			if (convert(str[L++], t) != str[R--]) {
				return false;
			}
		}
		return true;
	}

	/*
	* 对于left任意cha字符，right配合应该做什么决定，能够得到可旋转数
	* 如果left怎么也得不到cha字符，返回-1；如果能得到，返回right配合应做什么决定
	* */
	// 比如，left!=right，即不是同一个位置
	// left想得到0，那么就right就需要是0
	// left想得到1，那么就right就需要是1
	// left想得到6，那么就right就需要是9
	// left想得到8，那么就right就需要是8
	// left想得到9，那么就right就需要是6
	// 除此了这些之外，left不能得到别的了。
	// 比如，left==right，即是同一个位置
	// left想得到0，那么就right就需要是0
	// left想得到1，那么就right就需要是1
	// left想得到8，那么就right就需要是8
	// 除此了这些之外，left不能得到别的了，比如：
	// left想得到6，那么就right就需要是9，而left和right是一个位置啊，怎么可能即6又9，返回-1
	// left想得到9，那么就right就需要是6，而left和right是一个位置啊，怎么可能即9又6，返回-1
	public static int convert(char cha, boolean diff) {
		switch (cha) {
		case '0':
			return '0';
		case '1':
			return '1';
		case '6':
			return diff ? '9' : -1;
		case '8':
			return '8';
		case '9':
			return diff ? '6' : -1;
		default:
			return -1;
		}
	}

	// low [左边已经做完决定了 left.....right 右边已经做完决定了]
	// leftMore：
	// 左边已经做完决定的部分，如果大于low的原始，leftMore = true;
	// 左边已经做完决定的部分，如果不大于low的原始，那一定是相等，不可能小于，leftMore = false;
	// rightLessEqualMore：
	// 右边已经做完决定的部分，如果小于low的原始，rightLessEqualMore = 0;
	// 右边已经做完决定的部分，如果等于low的原始，rightLessEqualMore = 1;
	// 右边已经做完决定的部分，如果大于low的原始，rightLessEqualMore = 2;
	// rightLessEqualMore < = >
	//                    0 1 2
	// 返回 ：
	// 没做决定的部分，随意变，几个有效的情况？返回！
	public static int up(char[] low, int left, boolean leftMore, int rightLessEqualMore) {
		int N = low.length;
		int right = N - 1 - left;
		if (left > right) { // 都做完决定了！
			// 如果左边做完决定的部分大于原始 或者 如果左边做完决定的部分等于原始&左边做完决定的部分不小于原始
			// 有效！
			// 否则，无效！
			return leftMore || (!leftMore && rightLessEqualMore != 0) ? 1 : 0;
		}
		// 如果上面没有return，说明决定没做完，就继续做
		if (leftMore) { // 如果左边做完决定的部分大于原始
			/*那中间的数字可以随意做选择，相当于调用函数all(len,false)*/
			return num(N - (left << 1));
		} else { // 如果左边做完决定的部分等于原始
			int ways = 0;
			/*当前left做的决定，大于原始的left, 并且是有效的决定，中间的数可以调用公式计算*/
			for (char cha = (char) (low[left] + 1); cha <= '9'; cha++) {
				if (convert(cha, left != right) != -1) {
					ways += up(low, left + 1, true, rightLessEqualMore);
				}
			}
			/*当前left做的决定，等于原始的left，要判断下是否有效*/
			int convert = convert(low[left], left != right);
			if (convert != -1) {
				/*
				* left坐好了决定，就是原始left，而且是有效的，那么right也就是定好了
				* 此时需要根据right的情况来讨论中间的数是否能随意变
				* 用rightLessEqualMore参数来表示，这个参数最终在basecase中用到
				* 即都做完决定了，left部分都与原始left相等，最后一个做决定的right必须比原始大或者相等，即为一个有效的决定
				* */
				if (convert < low[right]) {
					ways += up(low, left + 1, false, 0);
				} else if (convert == low[right]) {
					ways += up(low, left + 1, false, rightLessEqualMore);
				} else {
					ways += up(low, left + 1, false, 2);
				}
			}
			return ways;
		}
	}

	// ll < =
	// rs < = >
	public static int down(char[] high, int left, boolean ll, int rs) {
		int N = high.length;
		int right = N - 1 - left;
		if (left > right) {
			return ll || (!ll && rs != 2) ? 1 : 0;
		}
		if (ll) {
			return num(N - (left << 1));
		} else {
			int ways = 0;
			for (char cha = (N != 1 && left == 0) ? '1' : '0'; cha < high[left]; cha++) {
				if (convert(cha, left != right) != -1) {
					ways += down(high, left + 1, true, rs);
				}
			}
			int convert = convert(high[left], left != right);
			if (convert != -1) {
				if (convert < high[right]) {
					ways += down(high, left + 1, false, 0);
				} else if (convert == high[right]) {
					ways += down(high, left + 1, false, rs);
				} else {
					ways += down(high, left + 1, false, 2);
				}
			}
			return ways;
		}
	}

	public static int num(int bits) {
		if (bits == 1) {
			return 3;
		}
		if (bits == 2) {
			return 5;
		}
		int p2 = 3;
		int p1 = 5;
		int ans = 0;
		for (int i = 3; i <= bits; i++) {
			ans = 5 * p2;
			p2 = p1;
			p1 = ans;
		}
		return ans;
	}

	/*
	* 所有的len位数，有几个有效的？
	* len是实际数字长度，两头没东西
	* 可以参考下面的递归方法
	* */
	// 如果是最开始 :
	// Y X X X Y
	// -> 1 X X X 1
	// -> 8 X X X 8
	// -> 9 X X X 6
	// -> 6 X X X 9
	// 如果不是最开始 :
	// Y X X X Y
	// -> 0 X X X 0
	// -> 1 X X X 1
	// -> 8 X X X 8
	// -> 9 X X X 6
	// -> 6 X X X 9
	// 所有的len位数，有几个有效的？
	public static int all(int len) {
		int ans = (len & 1) == 0 ? 1 : 3;
		for (int i = (len & 1) == 0 ? 2 : 3; i < len; i += 2) {
			ans *= 5;
		}
		return ans << 2;
	}

	/*
	* 上面all(int)函数不是很好理解，
	* 其实就是下面这个递归改成的迭代
	* len：等待计算数字的长度
	* init：
	* 	true：len是数字实际长度，两头没数
	* 	false：len是数字中间长度，两头都有数
	* */
	public static int all(int len, boolean init) {
		if (len == 0) { // init == true，不可能调用all(0)
			/*长度是0，两头一定是有数的，为什么返回1？返回0就把所有的记得置零了*/
			return 1;
		}
		if (len == 1) {
			/*
			* 不管两头是否有数，1个长度符合条件的都是
			* 0
			* 1
			* 8
			* */
			return 3;
		}
		if (init) {
			/*
			* 两头没数
			* 两头只能是下面4个组合
			* 1 1
			* 6 9
			* 8 8
			* 9 6
			* 所以中间的长度调递归，结果乘以4
			* */
			return all(len - 2, false) << 2;
		} else {
			/*
			 * 两头有数
			 * 两头是下面5个组合
			 * 0 0
			 * 1 1
			 * 6 9
			 * 8 8
			 * 9 6
			 * 所以中间的长度调递归，结果乘以5
			 * */
			return all(len - 2, false) * 5;
		}
	}

}
