package class32;

import java.util.ArrayList;
import java.util.List;

/**
 * 给定一个有序数组nums，和一个范围[lower,uppder]
 * 将nums中缺失的范围返回
 * 比如[0,1,3,50,75],范围[-5,100]
 * 返回{"-5->-1","2","4->49","51->74","76->100"}
 *
 * 解题：
 * 	遍历arr，来到i位置，
 * 	如果arr[i]>lower,那么缺失lower到arr[i]-1的范围
 */
public class Problem_0163_MissingRanges {

	public static List<String> findMissingRanges(int[] nums, int lower, int upper) {
		List<String> ans = new ArrayList<>();
		for (int num : nums) {
			if (num > lower) {
				/*缺失掉了lower到num-1的范围*/
				ans.add(miss(lower, num - 1));
			}
			if (num == upper) {
				/*已经到了范围的R*/
				return ans;
			}
			/*重新定义范围的L，后继续遍历arr*/
			lower = num + 1;
		}
		/*如果arr遍历结束了，范围还缺，那就将还缺少的范围补上*/
		if (lower <= upper) {
			ans.add(miss(lower, upper));
		}
		return ans;
	}

	// 生成"lower->upper"的字符串，如果lower==upper，只用生成"lower"
	public static String miss(int lower, int upper) {
		String left = String.valueOf(lower);
		String right = "";
		if (upper > lower) {
			right = "->" + String.valueOf(upper);
		}
		return left + right;
	}

}
