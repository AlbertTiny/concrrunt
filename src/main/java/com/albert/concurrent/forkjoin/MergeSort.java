package com.albert.concurrent.forkjoin;

import com.albert.concurrent.util.MakeArray;

import java.util.Arrays;
import java.util.concurrent.*;

import static java.lang.System.currentTimeMillis;

/**
 * fork/join实现排序例子
 */
public class MergeSort {

    static class Mytask extends RecursiveAction {
        private int data[];
        private int fromIndex;
        private int toIndex;
        public Mytask(int data[],int fromIndex,int toIndex){
            this.data=data;
            this.fromIndex=fromIndex;
            this.toIndex=toIndex;
        }

        @Override
        protected void compute() {
            int mid= (toIndex+fromIndex)/2;
            if(toIndex-fromIndex>=2){
                Mytask left= new Mytask(data,fromIndex,mid);
                Mytask right= new Mytask(data,mid+1,toIndex);
                invokeAll(left,right);
                left.join();
                right.join();
            }
            merge(data,fromIndex,mid,toIndex);
        }

        public static void merge(int a[], int left, int mid, int right) {
            int len = right - left + 1;
            int temp[] = new int[len];
            int i = left;
            int j = mid + 1;
            int index = 0;
            while(i<=mid && j <= right) {
                temp[index++] = a[i] <= a[j] ? a[i++] : a[j++];
            }
            while (i <= mid) {
                temp[index++] = a[i++];
            }
            while (j<=right) {
                temp[index++] = a[j++];
            }
            for (int k = 0; k<temp.length; k++) {
                a[left++] = temp[k];
            }
        }
    }
    public static long forkJoinCost(){
        int[] data = MakeArray.makeArray();
        Long startTime =currentTimeMillis();
        ForkJoinPool forkJoinPool =new ForkJoinPool();
        forkJoinPool.invoke(new Mytask(data,0,data.length-1));
        return currentTimeMillis()-startTime;
    }
    public static long sortCost(){
        int[] data = MakeArray.makeArray();
        Long startTime =currentTimeMillis();
        Arrays.sort(data);
        return currentTimeMillis()-startTime;
    }
    public static void main(String[] args) {
        System.out.println("forkjoin耗时"+forkJoinCost());
        System.out.println("sort耗时"+sortCost());
    }
}
