package com.albert.concurrent.forkjoin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * 1到100求和
 * 同步 有返回值使用例子
 */
public class ForkJoinTest {
    static class MyRecursiveTask extends RecursiveTask<Integer> {
        private int fromIndex;
        private int toIndex;
        private int[] data;
        private static int THRESHOLD = 2;

        public MyRecursiveTask(int a[], int s, int end) {
            this.data = a;
            this.fromIndex = s;
            this.toIndex = end;
        }

        @Override
        protected Integer compute() {
            if (toIndex - fromIndex <= THRESHOLD) {
                int sum = 0;
                for (int i = fromIndex; i <= toIndex; i++) {
                    sum += data[i];
                }
                return sum;
            } else {
                int mid= (fromIndex+toIndex)/2;
                MyRecursiveTask left =new MyRecursiveTask(data,fromIndex,mid);
                MyRecursiveTask right =new MyRecursiveTask(data,mid+1,toIndex);
                invokeAll(left,right);
                return left.join()+right.join();
            }
        }
    }

    public static void main(String[] args) {
        final ForkJoinPool forkJoinPool = new ForkJoinPool();
        int[] d = data();
        MyRecursiveTask task =new MyRecursiveTask(d,0,d.length-1);
        Integer invoke = forkJoinPool.invoke(task);
        System.out.println(invoke);
        System.out.println(task.join());

    }

    public static int[] data() {
        int[] d = new int[100];
        for (int i = 0; i < 100; i++) {
            d[i] = i + 1;
        }
        return d;
    }
}
