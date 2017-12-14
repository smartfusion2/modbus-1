package com.xht.task;

import java.sql.Timestamp;
//import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xht.dao.DataDAO;
import com.xht.modbus.ModbusRTU;
import com.xht.util.Cache;
import com.xht.util.NumberUtil;

public class LoadMachineTask {
	private DataDAO dao;
	private static int startOffset = 0;
	private static int numberOfRegisters = 2;
	private Log log;
	
	public LoadMachineTask() {
		log = LogFactory.getLog(LoadMachineTask.class);
		dao = new DataDAO();
	}
	/**
	 * 机台加载执行
	 */
	public void start(){
		List<Integer> codes = loadMachineCode();
		loadOnlineMachine(codes);
	}
	/**
	 * 加载机台code
	 * @return
	 */
	private List<Integer> loadMachineCode(){
		List<Integer> codes = dao.findMachineCodes();
		return codes;
	}
	/**
	 * 加载在线机台
	 */
	private void loadOnlineMachine(List<Integer> codes){
		for(Integer slaveId : codes){
			short[] data = ModbusRTU.readHoldingRegister(slaveId, startOffset, numberOfRegisters);
			dataStore(slaveId,data);
		}
	}
	/**
	 * 读取数据并存储到数据库
	 * @param slaveId
	 * @param data
	 */
	@SuppressWarnings("unchecked")
	private void dataStore(int slaveId, short[] data){
		if(data == null){
			dao.updateMonitorState(slaveId);
			log.info("读取栈号[" + slaveId + "]生产数据失败！");
		}else{
//			int planNum = -1;
//			try{
//				planNum = (int) Cache.get(Cache.PLAN_NUM, slaveId);
//			}catch(Exception e){
//				planNum = 0;
//			}
//			int sdata_0 = data[0]<<16;
//			int sdata_1 = data[1];
//			System.out.println(sdata_0);
//			System.out.println(sdata_1);
			int sdata = NumberUtil.formatInt(data);
			log.info("读取[" + slaveId + "]下位机数据为：[" + sdata + "]");
			int res = dao.updateMonitor(slaveId, sdata);
			if(res==0){
				dao.addMonitor(slaveId, sdata);
			}
			Map<String,Object> obj = (Map<String, Object>) Cache.poll(Cache.HISTORY_DATA, slaveId);
			log.info("获取到的缓存数据：" + obj);
			if(obj != null && sdata < (int)obj.get("productNum")){
				log.info("读取到的缓存数据大于读取到的数据，添加一条历史记录");
				obj.put("clearFlag", 1);
				dao.addHistoryData(obj);
				return;
			}
//			NumberFormat f = NumberFormat.getInstance();
//			f.setMaximumFractionDigits(2);
			Map<String, Object> historyData = new HashMap<String, Object>();
			historyData.put("productNum", sdata);
			historyData.put("machineCode", slaveId);
//			historyData.put("planNum", planNum);
//			historyData.put("perfection", f.format((double)sdata/(double)planNum*100));
			historyData.put("clearFlag", 0);
			historyData.put("addTime", new Timestamp(System.currentTimeMillis()));
			log.info("添加当前读取的数据到缓存数据中：" + historyData);
			Cache.put(Cache.HISTORY_DATA, slaveId, historyData);
		}
	}
}
