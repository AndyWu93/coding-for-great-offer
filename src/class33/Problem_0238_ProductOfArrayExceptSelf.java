package class33;

/**
 * Given an integer array nums, return an array answer such that answer[i] is equal to the product of all the elements of nums except nums[i].
 *
 * The product of any prefix or suffix of nums is guaranteed to fit in a 32-bit integer.
 *
 * You must write an algorithm that runs in O(n) time and without using the division operation.
 * 题意：给出arr[]，求出ans[],要求ans[i] = 乘积(arr中除了arr[i]的其他所有数)，要求时间复杂度O(N),额外空间复杂O(1)
 * 解题：
 * 	借用ans[],生成后缀积，再用一个变量从前往后收集乘积，
 * 	这样来到i位置就能够同时获得i-1位置的前缀积和i+1位置的后缀积
 *
 * 进阶：arr[]自我更新成ans
 * 	处理掉arr中有1个0和有多个0的情况
 * 	没有0：arr[i] = 数组乘积/arr[i],不能用/？那就用位运算实现/
 */
public class Problem_0238_ProductOfArrayExceptSelf {

	public int[] productExceptSelf(int[] nums) {
		int n = nums.length;
		int[] ans = new int[n];
		ans[0] = nums[0];
		for (int i = 1; i < n; i++) {
			ans[i] = ans[i - 1] * nums[i];
		}
		int right = 1;
		for (int i = n - 1; i > 0; i--) {
			ans[i] = ans[i - 1] * right;
			right *= nums[i];
		}
		ans[0] = right;
		return ans;
	}

	// 扩展 : 如果仅仅是不能用除号，把结果直接填在nums里呢？
	// 解法：数一共几个0；每一个位得到结果就是，a / b，位运算替代 /，之前的课讲过（新手班）

}
