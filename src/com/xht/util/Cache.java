package com.xht.util;

import java.util.HashMap;
import java.util.Map;

public class Cache {
	/**
	 * 历史记录类型数据
	 */
	public static final String HISTORY_DATA = "HISTORY_DATA_";
	/**
	 * 计划产量数据类型
	 */
	public static final String PLAN_NUM = "PLAN_NUM_";
	private static Map<String, Object> cache;
	
	static{
		cache = new HashMap<String, Object>();
	}
	/**
	 * 添加数据
	 * @param type
	 * @param slaveId
	 * @param map
	 */
	public static void put(String type, int slaveId, Object data){
		cache.put(type + slaveId, data);
	}
	/**
	 * 获取数据
	 * @param type
	 * @param slaveId
	 * @return
	 */
	public static Object get(String type, int slaveId){
		return cache.get(type + slaveId);
	}
	/**
	 * 获取数据并从集合中删除
	 * @param type
	 * @param slaveId
	 * @return
	 */
	public static Object poll(String type, int slaveId){
		Object result = cache.get(type + slaveId);
		cache.remove(type + slaveId);
		return result;
	}
}
