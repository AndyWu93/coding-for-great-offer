package class01;

/**
 * 位运算技巧
 */
public class Code03_Near2Power {

	// 已知n是正数
	// 返回大于等于，且最接近n的，2的某次方的值
	public static final int tableSizeFor(int n) {
		/*先--，是为了防止n就是2的某次方，所以--后就打散了*/
		n--;
		/*下面是将二进制中从第一个1开始往后的位上都填上1*/
		n |= n >>> 1;
		n |= n >>> 2;
		n |= n >>> 4;
		n |= n >>> 8;
		n |= n >>> 16;
		/*
		* 最后再加1，就成了2的某次方了
		* n<0?:这里n不可能小于0，加上这个就表示如果n<0,那返回1就好了
		* */
		return (n < 0) ? 1 : n + 1;
	}

	public static void main(String[] args) {
		int cap = 120;
		System.out.println(tableSizeFor(cap));
	}

}
