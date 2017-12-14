package com.xht.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;


public class DBUtil {
	private static String driver;
	private static String url;
	private static String username;
	private static String password;
	private static Connection conn;
	
	/**
	 * 加载配置文件读取参数
	 * 加载连接驱动
	 */
	static{
		Properties prop = new Properties();
		try {
			prop.load(DBUtil.class.getResourceAsStream("/config/jdbc.properties"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		driver = prop.getProperty("driver");
		url = prop.getProperty("url");
		username = prop.getProperty("username");
		password = prop.getProperty("password");
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 初始化Connection
	 * @return
	 */
	public static Connection init(){
		if(conn!=null){
			return conn;
		}
		try {
			conn = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	/**
	 * 增删改
	 * @param sql
	 * @param paras
	 * @return
	 */
	public static int update(String sql, Object... paras){
		PreparedStatement ps = null;
		int result = 0;
		try {
			ps = conn.prepareStatement(sql);
			for(int i=0; i<paras.length; i++){
				ps.setObject(i+1, paras[i]);
			}
			result = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return result;
	}
	/**
	 * 查询
	 * @param sql
	 * @param paras
	 * @return
	 */
	public static ResultSet query(String sql, Object... paras){
		PreparedStatement ps = null;
		ResultSet result = null;
		try {
			ps = conn.prepareStatement(sql);
			for(int i=0; i<paras.length; i++){
				ps.setObject(i+1, paras[i]);
			}
			result = ps.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 关闭连接
	 */
	public static void closeConnection(){
		try {
			//关闭当前Connection
			conn.close();
			conn = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
