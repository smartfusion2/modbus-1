package com.xht.util;


public class NumberUtil {
	public static final int Short_MAX = Short.MAX_VALUE - Short.MIN_VALUE + 1;
	
	/**
	 * 将short类型转换为int类型。
	 * 如果short值为正数则返回当前值。
	 * 如果short值为负数则返回当前值-short最小值+short最大值。
	 * 
	 * 注：
	 * 	formatInt(10)	return 10 
	 * 	formatInt(-10)	return (-10) - (-32768) + 32767 + 1
	 * 
	 * @param num
	 * @return 
	 */
	public static int formatInt(short num) {
		int result = num;
		if(num < 0){
			result = num - Short.MIN_VALUE + Short.MAX_VALUE + 1;
		}
		return result;
	}
	/**
	 * 将short数组转换为int类型
	 * short[0]为高16位，short[1]为低16位。
	 * short[0]左移16位，short[1]根据formatInt(short num)计算
	 * 
	 * @param arr
	 * @return 计算后的short[0] + short[1]
	 */
	public static int formatInt(short[] arr) {
		int data_0 = arr[0] << 16;
		int data_1 = formatInt(arr[1]);
		return data_0 + data_1;
	}
	/**
	 * 将int值转换为short数组。
	 * 当前值/Short_MAX的值为数组[0]的值，
	 * 如果当前值%Short_MAX的值大于short最大值
	 * 则数组[1]的值为当前值取余SHort_MAX的值-short最大值+short最小值-1
	 * 如果前值%Short_MAX的值不大于short最大值
	 * 则数组[1]的值为当前值取余SHort_MAX的值
	 * @param num
	 * @return
	 */
	public static short[] formatShort(int num) {
		short[] arr = new short[2];
		arr[0] = (short) (num / Short_MAX);
		int remainder = num % Short_MAX;
		if(remainder > Short.MAX_VALUE) {
			arr[1] = (short) (remainder - Short.MAX_VALUE + Short.MIN_VALUE - 1);
		}else {
			arr[1] = (short) remainder;
		}
		return arr;
	}
}













