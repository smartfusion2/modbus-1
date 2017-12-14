package com.xht.task;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xht.dao.DataDAO;
import com.xht.modbus.ModbusRTU;
import com.xht.util.Cache;

public class WriteTask {
	private DataDAO dao;
	@SuppressWarnings("unused")
	private Log log;
	private static int startOffset = 0; 
	
	public WriteTask() {
		dao = new DataDAO();
		log = LogFactory.getLog(WriteTask.class);
	}
	
	public void start(){
		List<Map<String, Object>> plans = dao.findPlanTasks();
		for(Map<String, Object> plan : plans){
			int slaveId = (int)plan.get("slaveId");
			short[] sdata = new short[1];
			int data = (int)plan.get("planNum");
			sdata[0] = (short)data;
			Cache.put(Cache.PLAN_NUM, slaveId, data);
			ModbusRTU.writeRegisters(slaveId, startOffset, sdata);
		}
	}
}
