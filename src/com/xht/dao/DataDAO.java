package com.xht.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataDAO {
	/**
	 * 查询machine所有的code
	 * @return
	 */
	public List<Integer> findMachineCodes() {
		List<Integer> codes = new ArrayList<Integer>();
		String sql = "SELECT code FROM machine WHERE delFlag = ?";
		ResultSet result = DBUtil.query(sql, 1);
		try {
			while(result.next()){
				codes.add(result.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return codes;
	}
	/**
	 * 修改监控数据
	 * @param slaveId
	 * @return
	 */
	public int updateMonitor(int slaveId, int planNum, int sdata) {
		String sql = "UPDATE machine_monitor SET state = ?, planNum = ?, productNum = ? WHERE machineCode = ?";
		int result = DBUtil.update(sql, 1, planNum, sdata, slaveId);
		return result;
	}
	/**
	 * 添加一条监控数据
	 * @param slaveId
	 * @param data
	 */
	public int addMonitor(int slaveId, int planNum, int sdata) {
		String sql = "INSERT INTO machine_monitor(machineCode, planNum, productNum, state) VALUES(?, ?, ?, ?)";
		int result = DBUtil.update(sql, slaveId, planNum, sdata, 1);
		return result;
	}
	/**
	 * 修改监控状态
	 * @param slaveId
	 */
	public int updateMonitorState(int slaveId) {
		String sql = "UPDATE machine_monitor SET state = ? WHERE machineCode = ?";
		int result = DBUtil.update(sql, 0, slaveId);
		return result;
	}
	/**
	 * 添加历史记录
	 * @param obj
	 * @return
	 */
	public int addHistoryData(Map<String, Object> obj) {
		String sql = "INSERT INTO history_data(machineCode, productNum, addTime, clearFlag) VALUES(?, ?, ?, ?)";
		int machineCode = (int) obj.get("machineCode");
//		int planNum = (int) obj.get("planNum");
		int productNum = (int) obj.get("productNum");
//		String perfection = (String) obj.get("perfection");
		Timestamp addTime = (Timestamp) obj.get("addTime");
		int clearFlag = (int) obj.get("clearFlag");
		int result = DBUtil.update(sql, machineCode, productNum, addTime, clearFlag);
		return result;
	}
	/**
	 * 批量添加历史记录
	 * @param datas
	 * @return
	 */
	public int addHistoryDatas(List<Map<String, Object>> datas) {
		String sql = "INSERT INTO history_data(machineCode, productNum, addTime, clearFlag) VALUES(?, ?, ?, ?)";
		int result = 0;
		for(Map<String, Object> data : datas){
			int machineCode = (int) data.get("machineCode");
//			int planNum = (int) data.get("planNum");
			int productNum = (int) data.get("productNum");
//			String perfection = (String) data.get("perfection");
			Timestamp addTime = (Timestamp) data.get("addTime");
			int clearFlag = (int) data.get("clearFlag");
			result += DBUtil.update(sql, machineCode, productNum, addTime, clearFlag);
		}
		return result;
	}
	/**
	 * 查询所有可执行生产计划
	 * @return
	 */
	public List<Map<String, Object>> findPlanTasks() {
		String sql = "SELECT machineCode, planNum FROM plan_task WHERE delFlag = ?";
		ResultSet result = DBUtil.query(sql, 1);
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		try {
			while(result.next()){
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("slaveId", result.getInt(1));
				data.put("planNum", result.getInt(2));
				datas.add(data);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return datas;
	}
	public Map<String, Object> findPlanTaskById(int id) {
		String sql = "SELECT * FROM plan_task WHERE id = ?";
		ResultSet result = DBUtil.query(sql, id);
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			while(result.next()){
				data.put("code", result.getInt(2));
				data.put("planNum", result.getInt(3));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return data;
	}
	public int updateMonitor(int slaveId, int sdata) {
		String sql = "UPDATE machine_monitor SET state = ?, productNum = ? WHERE machineCode = ?";
		int result = DBUtil.update(sql, 1, sdata, slaveId);
		return result;
	}
	public int addMonitor(int slaveId, int sdata) {
		String sql = "INSERT INTO machine_monitor(machineCode, productNum, state) VALUES(?, ?, ?)";
		int result = DBUtil.update(sql, slaveId, sdata, 1);
		return result;
	}
}
