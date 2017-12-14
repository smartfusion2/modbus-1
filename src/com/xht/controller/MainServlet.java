package com.xht.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xht.service.MainService;


public class MainServlet extends HttpServlet {
	private MainService service = new MainService();
	/**
	 * 
	 */
	private static final long serialVersionUID = 2146146687029350142L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = req.getServletPath();
		path = path.substring(1, path.lastIndexOf("."));
		if("clearZero".equals(path)){
			PrintWriter pw = resp.getWriter();
			try{
				String machineCode = req.getParameter("machineCode");
				int code = Integer.parseInt(machineCode);
				service.clearZero(code);
				pw.write("1");
			}catch(Exception e){
				pw.write("0");
			}finally {
				pw.close();
			}
		}
	}

}
