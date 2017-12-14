package com.xht.modbus;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.serotonin.io.serial.SerialParameters;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersRequest;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersResponse;
import com.serotonin.modbus4j.msg.WriteRegistersRequest;
import com.serotonin.modbus4j.msg.WriteRegistersResponse;

public class ModbusRTU {
	//modbus通讯的串行口
	private static String portId;
	//奇偶校验  （ 0：无校验，  1：奇校验，  2：偶校验）
	private static int parity;
	//数据位的位数
	private static int dataBits;
	//停止位的位数  （无奇偶校验为2，  有奇偶校验为1）
	private static int stopBits;
	//波特率
	private static int baudRate;
	//端口名称
	private static String portOwnerName;
	//通信重试次数
	private static int retries;
	//通讯超时时间
	private static int timeout;
	//ModbusMaster实例
	private static ModbusMaster master;
	//记录日志
	private static Log log;
	
	static{
		log = LogFactory.getLog(ModbusRTU.class);
		Properties prop = new Properties();
		try {
			prop.load(ModbusRTU.class.getResourceAsStream("/config/modbus.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		portId = prop.getProperty("portId");
		parity = Integer.parseInt(prop.getProperty("parity"));
		dataBits = Integer.parseInt(prop.getProperty("dataBits"));
		stopBits = Integer.parseInt(prop.getProperty("stopBits"));
		baudRate = Integer.parseInt(prop.getProperty("baudRate"));
		portOwnerName = prop.getProperty("portOwnerName");
		retries = Integer.parseInt(prop.getProperty("retries"));
		timeout = Integer.parseInt(prop.getProperty("timeout"));
	}
	/**
	 * 初始化
	 * @return
	 */
	public static ModbusMaster init() {
		SerialParameters paras = new SerialParameters();
		paras.setCommPortId(portId);	// 设定MODBUS通讯的串行口
		paras.setParity(parity);		// 设定奇偶校验
		paras.setDataBits(dataBits);	// 设定数据位
		paras.setStopBits(stopBits);	// 设定为停止位
		paras.setBaudRate(baudRate);	// 设置波特率
		paras.setPortOwnerName(portOwnerName);//设置端口名称
		master = new ModbusFactory().createRtuMaster(paras);
		master.setRetries(retries);		// 设置重连次数
		master.setTimeout(timeout);		// 设置通讯超出时间
		try {
			master.init();
		} catch (ModbusInitException e) {
			log.error("初始化modbus时出错：" + e);
		}
		return master;
	}
	/**
	 * 获取master
	 * @return
	 */
	public static ModbusMaster getMaster() {
		return master;
	}

	public static void destroy(){
		master.destroy();
		master = null;
	}
	/**
	 * 读取下位机数据
	 * @param slaveId
	 * @param startOffset
	 * @param numberOfRegisters
	 * @return
	 */
	public static short[] readHoldingRegister(int slaveId, int startOffset, int numberOfRegisters) {
		short[] result = null;
		try {
			ReadHoldingRegistersRequest request = new ReadHoldingRegistersRequest(slaveId, startOffset, numberOfRegisters);
			ReadHoldingRegistersResponse response = (ReadHoldingRegistersResponse) master.send(request);
			if(response == null){
				log.info("连接下位机[" + slaveId + "]失败");
			}else if(response.isException()){
				log.error("response出错：" + response.getExceptionMessage());
			}else{
				result = response.getShortData();
			}
		} catch (ModbusTransportException e) {
			log.error("读取寄存器数据出错：" + e);
		}
		return result;
	}

	/**
	 * 写出数据到下位机
	 * @param slaveId
	 * @param startOffset
	 * @param sdata
	 */
	public static void writeRegisters(int slaveId, int startOffset, short[] sdata) {
		try {
			WriteRegistersRequest request = new WriteRegistersRequest(slaveId, startOffset, sdata);
			WriteRegistersResponse response = (WriteRegistersResponse) master.send(request);
			if(response == null){
				log.info("连接下位机[" + slaveId + "]失败");
			}else if(response.isException()){
				log.error("response出错：" + response.getExceptionMessage());
			}
		} catch (ModbusTransportException e) {
			log.error("写出寄存器数据时出错！", e);
		}
	}

//	
//	public void readDiscreteInputTest(int slaveId, int startOffset, int numberOfBits) {
//		try {
//			ReadDiscreteInputsRequest request = new ReadDiscreteInputsRequest(slaveId, startOffset, numberOfBits);
//			ReadDiscreteInputsResponse response = (ReadDiscreteInputsResponse) master.send(request);
//			if (response.isException()) {
//				log.info("response出错："+response.getExceptionMessage());
//			} else {
//				log.info("返回值:" + Arrays.toString(response.getBooleanData()));
//			}
//		} catch (ModbusTransportException e) {
//			log.error("读开关量型的输入信号时出错:", e);
//		}
//	}

}
