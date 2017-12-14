package com.xht.service;

import com.xht.modbus.ModbusRTU;
import com.xht.util.NumberUtil;

public class MainService {
	private static int startOffset = 0;
	
	public MainService() {
	}
	/**
	 * 清零
	 * @param code
	 */
	public void clearZero(int code) {
		short[] sdata = NumberUtil.formatShort(0);
		ModbusRTU.writeRegisters(code, startOffset, sdata);
	}
}
