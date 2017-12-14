package com.xht.task;

import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

import com.xht.dao.DataDAO;
import com.xht.modbus.ModbusRTU;
import com.xht.util.Cache;

/**
 * 写出完成路
 * @author Administrator
 *
 */
public class CountTask {
	private DataDAO dao;
	private static int startOffset = 2;
	
	public CountTask() {
		dao = new DataDAO();
	}
	@SuppressWarnings("unchecked")
	public void start(){
		List<Integer> codes = dao.findMachineCodes();
		NumberFormat format = NumberFormat.getInstance();
		format.setMaximumFractionDigits(1);
		for(int slaveId : codes){
			int planNum = (int) Cache.get(Cache.PLAN_NUM, slaveId);
			Map<String, Object> map = (Map<String, Object>) Cache.get(Cache.HISTORY_DATA, slaveId);
			int productNum = (int) map.get("productNum");
			short[] sdata = new short[1];
			sdata[0] = Short.parseShort(format.format((double)productNum/(double)planNum*100));
			ModbusRTU.writeRegisters(slaveId, startOffset, sdata);
		}
	}
	
}
                                                  