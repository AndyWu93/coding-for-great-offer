package class01;

/**
 * 一个数组中只有两种字符'G'和’B’，
 * 可以让所有的G都放在左侧，所有的B都放在右侧
 * 或者可以让所有的G都放在右侧，所有的B都放在左侧
 * 但是只能在相邻字符之间进行交换操作，
 * 返回至少需要交换几次
 * 思路：贪心
 * 	将第一个出现的G移动到0位置，第二个出现的G移动到1位置...
 * 流程：
 * 	准备两个变量l,i
 * 	一开始l,i在0位置，
 * 	i右移，
 * 	如果i来到了G位置，统计一下此时i->l的距离（表示此时的G移动到左边第一个不是G的位置的代价），l右移
 * 	如果i来到了B位置，i继续右移
 */
public class Code04_MinSwapStep {

	// 一个数组中只有两种字符'G'和'B'，
	// 可以让所有的G都放在左侧，所有的B都放在右侧
    // 或者可以让所有的G都放在右侧，所有的B都放在左侧
	// 但是只能在相邻字符之间进行交换操作，请问请问至少需要交换几次，
	public static int minSteps1(String s) {
		if (s == null || s.equals("")) {
			return 0;
		}
		char[] str = s.toCharArray();
		/*统计的总步数*/
		int step1 = 0;
		/*一开始两个指针gi、i都在0位置*/
		int gi = 0;
		for (int i = 0; i < str.length; i++) {
			if (str[i] == 'G') {
				/*遇到了G，统计i到gi的距离，就是G移动的代价，然后gi位置已经是G了，所以右移*/
				step1 += i - (gi++);
			}
		}
		/*下面统计的是B移动的代价*/
		int step2 = 0;
		int bi = 0;
		for (int i = 0; i < str.length; i++) {
			if (str[i] == 'B') {
				step2 += i - (bi++);
			}
		}
		/*取小*/
		return Math.min(step1, step2);
	}

	/*
	* 这个方法和上面的其实是一样的，只是这里将两个统计写到一个for循环里了
	* */
	// 可以让G在左，或者在右
	public static int minSteps2(String s) {
		if (s == null || s.equals("")) {
			return 0;
		}
		char[] str = s.toCharArray();
		int step1 = 0;
		int step2 = 0;
		int gi = 0;
		int bi = 0;
		for (int i = 0; i < str.length; i++) {
			if (str[i] == 'G') { // 当前的G，去左边   方案1
				step1 += i - (gi++);
			} else {// 当前的B，去左边   方案2
				step2 += i - (bi++);
			}
		}
		return Math.min(step1, step2);
	}

	// 为了测试
	public static String randomString(int maxLen) {
		char[] str = new char[(int) (Math.random() * maxLen)];
		for (int i = 0; i < str.length; i++) {
			str[i] = Math.random() < 0.5 ? 'G' : 'B';
		}
		return String.valueOf(str);
	}

	public static void main(String[] args) {
		int maxLen = 100;
		int testTime = 1000000;
		System.out.println("测试开始");
		for (int i = 0; i < testTime; i++) {
			String str = randomString(maxLen);
			int ans1 = minSteps1(str);
			int ans2 = minSteps2(str);
			if (ans1 != ans2) {
				System.out.println("Oops!");
			}
		}
		System.out.println("测试结束");
	}
}