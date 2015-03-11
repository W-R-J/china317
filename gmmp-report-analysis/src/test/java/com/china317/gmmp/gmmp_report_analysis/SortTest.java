package com.china317.gmmp.gmmp_report_analysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

/**
 * TODO(这里用一句话描述这个类的作用)
 * <p>
 * TODO
 * 
 * @author
 * @Date 2012-9-9
 */
public class SortTest {
	public static int binanrySearch(List<Integer> list, int key) {
		int low = 0;
		int height = list.size() - 1;
		while (low <= height) {
			// 位运算提高效率
			int mid = (low + height) >>> 1;
			// System.out.println(mid);
			Integer midal = list.get(mid);
			// System.out.println(midal);
			// 与key比较 比key大 返回1 否则返回-1 相等返回0
			int copInt = midal.compareTo(key);
			// System.out.println(copInt);
			if (copInt < 0) {
				low = mid + 1;
			} else if (copInt > 0) {
				height = mid - 1;
			} else {
				// System.out.println(mid);
				return mid; // found key
			}

		}
		// System.out.println(-(low + 1));
		return -(low + 1);// not found key
	}

	
	public static void TwoInsertSort( List<Integer> data ) {
	       int left,right,num;
	       int middle,j;
	       for( int i = 1; i < data.size(); i++ ) {
	           // 准备
	           left = 0;
	           right = i-1;
	           num = data.get(i);
	           // 二分法查找插入位置
	           while( right >= left ) {
	               // 指向已排序好的中间位置
	               middle = ( left + right ) / 2;
	               if( num < data.get(middle) )
	                   right = middle-1; // 插入的元素在右区间
	               else
	                   left = middle+1;  // 插入的元素在左区间 
	           }
	           // 后移排序码大于R[i]的记录
	           for( j = i-1; j >= left; j-- ) {
	               //data[j+1] = data[j];
	              data.set(j+1, data.get(j));
	           }
	           // 插入
	           //data[left] = num;
	           data.set(left, num);
	       }
	   }
	public static List<Integer> insertSort(List<Integer> list) {
		// 装换成数组排序
		Integer[] arry = list.toArray(new Integer[0]);
		// System.out.println(arry.length);
		for (int i = 0; i < arry.length; i++) {
			for (int j = i; j > 0 && arry[j - 1] > arry[j]; j--) {
				swap(arry, j, j - 1);
			}
		}

		// System.out.println(Arrays.toString(arry));
		// 利用ListIterator 还是按照排序的顺序放入list中
		ListIterator<Integer> i = list.listIterator();
		for (int j = 0; j < arry.length; j++) {
			i.next();
			i.set(arry[j]);
		}
		return list;

	}

	public static void swap(Integer[] arry, int j, int i) {
		int t = arry[j];
		arry[j] = arry[i];
		arry[i] = t;

	}

	public static void main(String[] args) {
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(4);
		list.add(3);
		list.add(9);
		list.add(8);
		list.add(6);
		//insertSort(list);
		TwoInsertSort(list);
		System.out.println(Arrays.toString(list.toArray()));
		// Collections 中的排序
		// Collections.sort(list);
		// System.out.println(Arrays.toString(list.toArray()));
		// System.out.println(Collections.binarySearch(list, 6));
		System.out.println(binanrySearch(list, 4));

	}
}
