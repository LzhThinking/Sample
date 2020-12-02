package com.lzh.sample;

import java.io.File;

public class AlgorithmTest {
    public static void main(String[] args) {
        showAllFileList(new File("C:\\workspace_android\\Sample\\app\\src\\main\\java"));

        factorial(5);
    }

    public static void recursionSort(int[] source, int startPos, int endPos) {
        if (startPos < endPos) {
            int mid = (startPos + endPos) / 2;
            recursionSort(source, startPos, mid);
            recursionSort(source, mid, endPos);

        }
    }

    /**
     * 递归结束条件：0或1的阶乘= 1
     * 递归调用：n的阶乘= n * (n-1)的阶乘
     * @param n
     * @return
     */
    public static int factorial(int n) {
        if (n < 0) {
            print("n must lager than 0");
            return -1;
        } else if (n == 0 || n == 1) {
            print("factorial " + n + " is " + 1);
            return 1;
        } else {
            int result = n * factorial(n - 1);
            print("factorial " + n + " is " + result);
            return result;
        }
    }


    /**
     * 递归结束条件：如果当前File对象不是目录，则不用继续递归
     * 递归调用：根目录和子目录的处理方式都是一样
     * @param rootFile
     */
    public static void showAllFileList(File rootFile) {
        File[] files = rootFile.listFiles();

        for (File file: files) {
            if (file.isDirectory()) {
                print("directoryName : " + file.getName());
                showAllFileList(file);
            } else {
                print("fileName : " + file.getName());
            }
        }

    }

    public static void print(String msg) {
        System.out.println(msg);
    }
}
