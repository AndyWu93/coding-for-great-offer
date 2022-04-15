package class09;

/**
 * 给定一个数组arr，长度为N，arr中的值不是0就是1
 * arr[i]表示第i栈灯的状态，0代表灭灯，1代表亮灯
 * 每一栈灯都有开关，但是按下i号灯的开关，会同时改变i-1、i、i+1栈灯的状态
 * 问题一：
 * 如果N栈灯排成一条直线,请问最少按下多少次开关,能让灯都亮起来
 * 排成一条直线说明：
 * i为中间位置时，i号灯的开关能影响i-1、i和i+1
 * 0号灯的开关只能影响0和1位置的灯
 * N-1号灯的开关只能影响N-2和N-1位置的灯
 * 解题：
 * 	动态规划，从左往右的尝试
 * 	难点：
 * 		本题设计动态规划中的一个重要领域，将外部信息用参数描述。
 * 		当下的决策涉及自己和左右的状态，要注意process方法中参数的定义
 * 
 * 问题二：
 * 如果N栈灯排成一个圈,请问最少按下多少次开关,能让灯都亮起来
 * 排成一个圈说明：
 * i为中间位置时，i号灯的开关能影响i-1、i和i+1
 * 0号灯的开关能影响N-1、0和1位置的灯
 * N-1号灯的开关能影响N-2、N-1和0位置的灯
 * 
 * */
public class Code01_LightProblem {

	/**
	 *无环改灯问题的递归版本
	 */
	public static int noLoopMinStep1(int[] arr) {
		if (arr == null || arr.length == 0) {
			return 0;
		}
		if (arr.length == 1) {
			/*
			* 一盏灯：
			* 1：返回0
			* 0：返回1
			* */
			return arr[0] ^ 1;
		}
		if (arr.length == 2) {
			/*
			* 两盏灯：
			* 都亮：返回0
			* 都不亮：返回1
			* 一个亮一个不亮：返回无解
			* */
			return arr[0] != arr[1] ? Integer.MAX_VALUE : (arr[0] ^ 1);
		}
		// 不变0位置的状态
		int p1 = process1(arr, 2, arr[0], arr[1]);
		// 改变0位置的状态
		int p2 = process1(arr, 2, arr[0] ^ 1, arr[1] ^ 1);
		if (p2 != Integer.MAX_VALUE) {
			/*arr[0]状态改变*/
			p2++;
		}
		return Math.min(p1, p2);
	}

	/*
	* 当前在哪个位置上，做选择，nextIndex - 1 = cur ，当前！
	* cur - 1 灯的状态：preStatus
	* cur 灯的状态： curStatus
	* 0....cur-2  全亮的,
	* 从cur开始做选择直到末尾，所有的灯全亮，至少需要的灯数量返回
	* */
	public static int process1(int[] arr, int nextIndex, int preStatus, int curStatus) {
		if (nextIndex == arr.length) { // 当前来到最后一个开关的位置
			/*
			* baseCase:
			* 在最后一个开关，
			* 如果当前status和之前状态一样：都亮就不要按了，都不亮按下去
			* 如果当前status和之前状态不一样：怎么按都不行
			* */
			return preStatus != curStatus ? (Integer.MAX_VALUE) : (curStatus ^ 1);
		}
		// 没到最后一个按钮呢！
		// i < arr.length
		if (preStatus == 0) { // 一定要改变
			/*
			* 之前的灯不亮
			* 当前的按钮一定要按下去，来改变之前灯的状态，否则到后面无法补救
			* curStatus ^= 1：当前状态被按钮改变
			* arr[nextIndex] ^ 1：下个状态也被按钮改变
			* */
			curStatus ^= 1;
			int cur = arr[nextIndex] ^ 1;
			int next = process1(arr, nextIndex + 1, curStatus, cur);
			return next == Integer.MAX_VALUE ? next : (next + 1);
		} else { // 一定不能改变
			/*
			* 之前的灯亮
			* 当前的按钮一定不能按，按了就改变了之前灯的状态，后面也无法补救
			* */
			return process1(arr, nextIndex + 1, curStatus, arr[nextIndex]);
		}
	}

	/**
	 * 思考：上面的递归，有3个可变参数，是一个3维的dp吗？
	 * if (preStatus == 0) {
	 *		curStatus ^=1;
			int cur = arr[nextIndex] ^ 1;
			int next = process1(arr, nextIndex + 1, curStatus, cur);
	 *		return next ==Integer.MAX_VALUE ?next :(next +1);
		} else{
	 *  	return process1(arr,nextIndex+1,curStatus,arr[nextIndex]);
	 * 	}
	 * 这是上面递归的代码，可以发现，两个if中都有return，说明递归只会走一侧
	 * 那么一旦arr[0]的按钮决定了， preStatus、curStatus 这两个参数，只有0/1两个值，可以从前往后推出来
	 * 所以这是一个一维的dp，下面是递归改成一维迭代，O(N)
	 */
	// 无环改灯问题的迭代版本
	public static int noLoopMinStep2(int[] arr) {
		if (arr == null || arr.length == 0) {
			return 0;
		}
		if (arr.length == 1) {
			return arr[0] == 1 ? 0 : 1;
		}
		if (arr.length == 2) {
			return arr[0] != arr[1] ? Integer.MAX_VALUE : (arr[0] ^ 1);
		}
		int p1 = traceNoLoop(arr, arr[0], arr[1]);
		int p2 = traceNoLoop(arr, arr[0] ^ 1, arr[1] ^ 1);
		p2 = (p2 == Integer.MAX_VALUE) ? p2 : (p2 + 1);
		return Math.min(p1, p2);
	}

	/*
	* 当前来到了1位置做决定，1位置的状态是curStatus
	* 0位置已经决定好了，状态是preStatus
	* 从1位置往后，至少要按几次按钮，会全亮
	* */
	public static int traceNoLoop(int[] arr, int preStatus, int curStatus) {
		/*下个位置*/
		int i = 2;
		/*操作次数*/
		int op = 0;
		while (i != arr.length) {
			if (preStatus == 0) {
				op++;
				/*
				* 按按钮了，
				* 当前状态变一下，赋值给preStatus
				* 下个状态变一下，赋值给curStatus
				* i++;
				* */
				preStatus = curStatus ^ 1;
				curStatus = arr[i++] ^ 1;
			} else {
				/*
				* 不按
				* 当前状态不变，赋值给preStatus
				* 下个状态不变，赋值给curStatus
				* i++;
				* */
				preStatus = curStatus;
				curStatus = arr[i++];
			}
		}
		/*跳出循环，来到了最后一个按钮*/
		return (preStatus != curStatus) ? Integer.MAX_VALUE : (op + (curStatus ^ 1));
	}


	/**
	 * 有环改灯问题的递归版本
	 * 思路：
	 * 	0位置的状态可以不用管，后面可以补救，那从1位置开始保证全亮。那就是从2位置开始决策按钮
	 * 	那么0位置和1位置的按钮，到底按还是不按，就有4种组合，相应改变了0,N-1等多个位置的状态，把这些加入参数中
	 */
	public static int loopMinStep1(int[] arr) {
		if (arr == null || arr.length == 0) {
			return 0;
		}
		if (arr.length == 1) {
			return arr[0] == 1 ? 0 : 1;
		}
		if (arr.length == 2) {
			return arr[0] != arr[1] ? Integer.MAX_VALUE : (arr[0] ^ 1);
		}
		if (arr.length == 3) {
			return (arr[0] != arr[1] || arr[0] != arr[2]) ? Integer.MAX_VALUE : (arr[0] ^ 1);
		}
		// 0不变，1不变
		int p1 = process2(arr, 3, arr[1], arr[2], arr[arr.length - 1], arr[0]);
		// 0改变，1不变
		int p2 = process2(arr, 3, arr[1] ^ 1, arr[2], arr[arr.length - 1] ^ 1, arr[0] ^ 1);
		// 0不变，1改变
		int p3 = process2(arr, 3, arr[1] ^ 1, arr[2] ^ 1, arr[arr.length - 1], arr[0] ^ 1);
		// 0改变，1改变
		int p4 = process2(arr, 3, arr[1], arr[2] ^ 1, arr[arr.length - 1] ^ 1, arr[0]);
		p2 = p2 != Integer.MAX_VALUE ? (p2 + 1) : p2;
		p3 = p3 != Integer.MAX_VALUE ? (p3 + 1) : p3;
		p4 = p4 != Integer.MAX_VALUE ? (p4 + 2) : p4;
		return Math.min(Math.min(p1, p2), Math.min(p3, p4));
	}

	
	// 下一个位置是，nextIndex
	// 当前位置是，nextIndex - 1 -> curIndex
	// 上一个位置是, nextIndex - 2 -> preIndex   preStatus
	// 当前位置是，nextIndex - 1, curStatus
	// endStatus, N-1位置的状态
	// firstStatus, 0位置的状态
	// 返回，让所有灯都亮，至少按下几次按钮
	
	// 当前来到的位置(nextIndex - 1)，一定不能是1！至少从2开始
	// nextIndex >= 3
	/*
	* 为什么要加endStatus和firstStatus的状态？
	* 	firstStatus：0位置的状态不一定要亮着，因为来到n-1位置时可以补救0位置状态
	* 	endStatus：n-1位置的状态需要提前告知，因为0位置的决策可能已经改变了其状态
	* 为什么curIndex从2开始，而不是从1开始？
	* 	0位置可以是0，所以从2开始做决策，因为只要保证从1开始是亮的就行了
	* */
	public static int process2(int[] arr, 
			int nextIndex, int preStatus, int curStatus, 
			int endStatus, int firstStatus) {
		
		if (nextIndex == arr.length) { // 最后一按钮！
			/*开始、N-2、N-1位置3个状态需要一致，否则无解*/
			return (endStatus != firstStatus || endStatus != preStatus) ? Integer.MAX_VALUE : (endStatus ^ 1);
		}
		// 当前位置，nextIndex - 1
		// 当前的状态，叫curStatus
		// 如果不按下按钮，下一步的preStatus: curStatus
		// 如果按下按钮，下一步的preStatus: curStatus ^ 1
		// 如果不按下按钮，下一步的curStatus, arr[nextIndex]
		// 如果按下按钮，下一步的curStatus, arr[nextIndex] ^ 1
		int noNextPreStatus = 0;
		int yesNextPreStatus = 0;
		int noNextCurStatus =0;
		int yesNextCurStatus = 0;
		int noEndStatus = 0;
		int yesEndStatus = 0;
		/*
		* n-2位置的按钮，影响到了下次调用时的多个参数，需要单独讨论
		* */
		if(nextIndex < arr.length - 1) {// 当前没来到N-2位置
			 noNextPreStatus = curStatus;
			 yesNextPreStatus = curStatus ^ 1;
			 noNextCurStatus = arr[nextIndex];
			 yesNextCurStatus = arr[nextIndex] ^ 1;
			 /*endStatus，firstStatus一直不受影响*/
		} else if(nextIndex == arr.length - 1) {// 当前来到的就是N-2位置
			/*cur赋值给pre，和上面一样*/
			noNextPreStatus = curStatus;
			yesNextPreStatus = curStatus ^ 1;
			/*下一个赋值cur，下一个状态不能从arr中取，因为可能已经发生了改变，从参数中取*/
			noNextCurStatus = endStatus;
			yesNextCurStatus = endStatus ^ 1;
			/*firstStatus不受影响，endStatus会被当前按钮影响*/
			noEndStatus = endStatus;
			yesEndStatus = endStatus ^ 1;
		}
		if(preStatus == 0) {
			/*必须按*/
			int next = process2(arr, nextIndex + 1, yesNextPreStatus, yesNextCurStatus,
					/*注意endStatus的传参判断*/
					nextIndex == arr.length - 1 ? yesEndStatus : endStatus, firstStatus);
			return next == Integer.MAX_VALUE ? next : (next + 1);
		}else {
			return process2(arr, nextIndex + 1, noNextPreStatus, noNextCurStatus, 
					nextIndex == arr.length - 1 ? noEndStatus : endStatus, firstStatus);
					
		}
//		int curStay = (nextIndex == arr.length - 1) ? endStatus : arr[nextIndex];
//		int curChange = (nextIndex == arr.length - 1) ? (endStatus ^ 1) : (arr[nextIndex] ^ 1);
//		int endChange = (nextIndex == arr.length - 1) ? curChange : endStatus;
//		if (preStatus == 0) {
//			int next = process2(arr, nextIndex + 1, curStatus ^ 1, curChange, endChange, firstStatus);
//			return next == Integer.MAX_VALUE ? next : (next + 1);
//		} else {
//			return process2(arr, nextIndex + 1, curStatus, curStay, endStatus, firstStatus);
//		}
	}

	/**
	 * 同样的思考
	 * 上面5个变化的参数，都是在0、1位置决定好后依次推出，从而形成一个O(N)的复杂度
	 */
	// 有环改灯问题的迭代版本
	public static int loopMinStep2(int[] arr) {
		if (arr == null || arr.length == 0) {
			return 0;
		}
		if (arr.length == 1) {
			return arr[0] == 1 ? 0 : 1;
		}
		if (arr.length == 2) {
			return arr[0] != arr[1] ? Integer.MAX_VALUE : (arr[0] ^ 1);
		}
		if (arr.length == 3) {
			return (arr[0] != arr[1] || arr[0] != arr[2]) ? Integer.MAX_VALUE : (arr[0] ^ 1);
		}
		// 0不变，1不变
		int p1 = traceLoop(arr, arr[1], arr[2], arr[arr.length - 1], arr[0]);
		// 0改变，1不变
		int p2 = traceLoop(arr, arr[1] ^ 1, arr[2], arr[arr.length - 1] ^ 1, arr[0] ^ 1);
		// 0不变，1改变
		int p3 = traceLoop(arr, arr[1] ^ 1, arr[2] ^ 1, arr[arr.length - 1], arr[0] ^ 1);
		// 0改变，1改变
		int p4 = traceLoop(arr, arr[1], arr[2] ^ 1, arr[arr.length - 1] ^ 1, arr[0]);
		p2 = p2 != Integer.MAX_VALUE ? (p2 + 1) : p2;
		p3 = p3 != Integer.MAX_VALUE ? (p3 + 1) : p3;
		p4 = p4 != Integer.MAX_VALUE ? (p4 + 2) : p4;
		return Math.min(Math.min(p1, p2), Math.min(p3, p4));
	}

	public static int traceLoop(int[] arr, int preStatus, int curStatus, int endStatus, int firstStatus) {
		int i = 3;
		int op = 0;
		while (i < arr.length - 1) {
			if (preStatus == 0) {
				op++;
				preStatus = curStatus ^ 1;
				curStatus = (arr[i++] ^ 1);
			} else {
				preStatus = curStatus;
				curStatus = arr[i++];
			}
		}
		if (preStatus == 0) {
			op++;
			preStatus = curStatus ^ 1;
			endStatus ^= 1;
			curStatus = endStatus;
		} else {
			preStatus = curStatus;
			curStatus = endStatus;
		}
		return (endStatus != firstStatus || endStatus != preStatus) ? Integer.MAX_VALUE : (op + (endStatus ^ 1));
	}



	// for test
	// 生成长度为len的随机数组，值只有0和1两种值
	public static int[] randomArray(int len) {
		int[] arr = new int[len];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (int) (Math.random() * 2);
		}
		return arr;
	}



	public static void main(String[] args) {
		System.out.println("如果没有任何Oops打印，说明所有方法都正确");
		System.out.println("test begin");
		int testTime = 20000;
		int lenMax = 12;
		for (int i = 0; i < testTime; i++) {
			int len = (int) (Math.random() * lenMax);
			int[] arr = randomArray(len);
			int ans1 = noLoopRight(arr);
			int ans2 = noLoopMinStep1(arr);
			int ans3 = noLoopMinStep2(arr);
			if (ans1 != ans2 || ans1 != ans3) {
				System.out.println("1 Oops!");
			}
		}
		for (int i = 0; i < testTime; i++) {
			int len = (int) (Math.random() * lenMax);
			int[] arr = randomArray(len);
			int ans1 = loopRight(arr);
			int ans2 = loopMinStep1(arr);
			int ans3 = loopMinStep2(arr);
			if (ans1 != ans2 || ans1 != ans3) {
				System.out.println("2 Oops!");
			}
		}
		System.out.println("test end");

		int len = 100000000;
		System.out.println("性能测试");
		System.out.println("数组大小：" + len);
		int[] arr = randomArray(len);
		long start = 0;
		long end = 0;
		start = System.currentTimeMillis();
		noLoopMinStep2(arr);
		end = System.currentTimeMillis();
		System.out.println("noLoopMinStep2 run time: " + (end - start) + "(ms)");

		start = System.currentTimeMillis();
		loopMinStep2(arr);
		end = System.currentTimeMillis();
		System.out.println("loopMinStep2 run time: " + (end - start) + "(ms)");
	}



	// 无环改灯问题的暴力版本
	public static int noLoopRight(int[] arr) {
		if (arr == null || arr.length == 0) {
			return 0;
		}
		if (arr.length == 1) {
			return arr[0] == 1 ? 0 : 1;
		}
		if (arr.length == 2) {
			return arr[0] != arr[1] ? Integer.MAX_VALUE : (arr[0] ^ 1);
		}
		return f1(arr, 0);
	}

	public static int f1(int[] arr, int i) {
		if (i == arr.length) {
			return valid(arr) ? 0 : Integer.MAX_VALUE;
		}
		int p1 = f1(arr, i + 1);
		change1(arr, i);
		int p2 = f1(arr, i + 1);
		change1(arr, i);
		p2 = (p2 == Integer.MAX_VALUE) ? p2 : (p2 + 1);
		return Math.min(p1, p2);
	}

	public static void change1(int[] arr, int i) {
		if (i == 0) {
			arr[0] ^= 1;
			arr[1] ^= 1;
		} else if (i == arr.length - 1) {
			arr[i - 1] ^= 1;
			arr[i] ^= 1;
		} else {
			arr[i - 1] ^= 1;
			arr[i] ^= 1;
			arr[i + 1] ^= 1;
		}
	}

	public static boolean valid(int[] arr) {
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] == 0) {
				return false;
			}
		}
		return true;
	}

	// 有环改灯问题的暴力版本
	public static int loopRight(int[] arr) {
		if (arr == null || arr.length == 0) {
			return 0;
		}
		if (arr.length == 1) {
			return arr[0] == 1 ? 0 : 1;
		}
		if (arr.length == 2) {
			return arr[0] != arr[1] ? Integer.MAX_VALUE : (arr[0] ^ 1);
		}
		return f2(arr, 0);
	}

	public static int f2(int[] arr, int i) {
		if (i == arr.length) {
			return valid(arr) ? 0 : Integer.MAX_VALUE;
		}
		int p1 = f2(arr, i + 1);
		change2(arr, i);
		int p2 = f2(arr, i + 1);
		change2(arr, i);
		p2 = (p2 == Integer.MAX_VALUE) ? p2 : (p2 + 1);
		return Math.min(p1, p2);
	}

	public static void change2(int[] arr, int i) {
		arr[lastIndex(i, arr.length)] ^= 1;
		arr[i] ^= 1;
		arr[nextIndex(i, arr.length)] ^= 1;
	}

	public static int lastIndex(int i, int N) {
		return i == 0 ? (N - 1) : (i - 1);
	}

	public static int nextIndex(int i, int N) {
		return i == N - 1 ? 0 : (i + 1);
	}

}
