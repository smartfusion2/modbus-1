package com.xht.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xht.dao.DataDAO;
import com.xht.util.Cache;

public class StoreDataTask {
	private DataDAO dao;
	private Log log;
	
	public StoreDataTask(){
		dao = new DataDAO();
		log = LogFactory.getLog(StoreDataTask.class);
	}
	/**
	 * 定时保存数据
	 */
	@SuppressWarnings("unchecked")
	public void start(){
		log.info("保存历史记录开始");
		List<Integer> codes = dao.findMachineCodes();
		List<Map<String, Object>> datas = new ArrayList<Map<String,Object>>();
		for(int slaveId : codes){
			Map<String, Object> data = (Map<String, Object>) Cache.get(Cache.HISTORY_DATA, slaveId);
			if(data != null){
				datas.add(data);
			}
		}
		int res = dao.addHistoryDatas(datas);	
		log.info("共保存历史记录数据[" + res + "]条");
	}
}
