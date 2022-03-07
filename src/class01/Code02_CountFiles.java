package class01;

import java.io.File;
import java.util.Stack;

/**
 * 给定一个文件目录的路径，
 * 写一个函数统计这个目录下所有的文件数量并返回
 * 隐藏文件也算，但是文件夹不算
 *
 * 解题：
 * 	一般情况下使用宽度优先遍历，遇到文件count++，遇到文件夹放入队列中等待遍历
 */
public class Code02_CountFiles {

	// 注意这个函数也会统计隐藏文件
	public static int getFileNumber(String folderPath) {
		File root = new File(folderPath);
		if (!root.isDirectory() && !root.isFile()) {
			return 0;
		}
		if (root.isFile()) {
			return 1;
		}
		/*这里使用的是栈，属于深度优先，如果不习惯可以改成Queue变成宽度优先*/
		Stack<File> stack = new Stack<>();
		stack.add(root);
		int files = 0;
		while (!stack.isEmpty()) {
			File folder = stack.pop();
			for (File next : folder.listFiles()) {
				if (next.isFile()) {
					/*是文件就统计，这里包含隐藏文件*/
					files++;
				}
				if (next.isDirectory()) {
					stack.push(next);
				}
			}
		}
		return files;
	}

	public static void main(String[] args) {
		// 你可以自己更改目录
		String path = "/Users/zuochengyun/Desktop/";
		System.out.println(getFileNumber(path));
	}

}
