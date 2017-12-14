package com.xht.listener;

import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xht.dao.DBUtil;
import com.xht.modbus.ModbusRTU;
import com.xht.task.LoadMachineTask;
import com.xht.task.StoreDataTask;
//import com.xht.task.WriteTask;

public class InitListener implements ServletContextListener {
	private Log log = LogFactory.getLog(InitListener.class);
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		ModbusRTU.destroy();
		DBUtil.closeConnection();
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		if(DBUtil.init()!=null){
			log.info("数据库初始化成功");
		}else{
			log.info("数据库初始化失败");
		}
		if(ModbusRTU.init()!=null){
			log.info("modbus初始化成功");
		}else{
			log.info("modbus初始化失败");
		}
		//启动定时器
		start();
	}
	/**
	 * 启动任务
	 */
	public void start(){
//		WriteTask writeTask = new WriteTask();
//		writeTask.start();
		//设置定时器读取数据
		new Timer().schedule(new TimerTask() {
			LoadMachineTask loadTask = new LoadMachineTask();
			StoreDataTask storeTask = new StoreDataTask();
			int i = 0;
			@Override
			public void run() {
				//加载机台
				loadTask.start();
				if(i%60==0){
					//定时保存数据
					storeTask.start();
				}
				i++;
			}
		}, 10, 2000);
	}
}
