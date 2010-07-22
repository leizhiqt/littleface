package com.mooo.mycoz.controller.setup;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.List;
import java.util.MissingResourceException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import javax.servlet.ServletException;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;




import java.lang.reflect.Method;
import java.lang.NoSuchMethodException;
import java.lang.IllegalAccessException;
import java.lang.reflect.InvocationTargetException;
import java.lang.ClassNotFoundException;


import com.mooo.mycoz.dbobj.mycozBranch.User;
import com.mooo.mycoz.dbobj.mycozShared.Country;
import com.mooo.mycoz.dbobj.mycozShared.Language;
import com.mooo.mycoz.dbobj.mycozShared.OperatorUser;
import com.mooo.mycoz.jdbc.DBLoad;
import com.mooo.mycoz.jdbc.DBMap;
import com.mooo.mycoz.jdbc.DBNode;
import com.mooo.mycoz.jdbc.MysqlConnection;
import com.mooo.mycoz.util.ActionServlet;


import com.mooo.mycoz.util.SAXParserConf;
import com.mooo.mycoz.util.ActionMap;


public class OperatorUserController  {

private static Log log = LogFactory.getLog(OperatorUserController.class);

public void listStateRun(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	try {
		String var = "";
		
		// list for this
		request.setAttribute("ID",request.getParameter("ID"));
		request.setAttribute("UserName",request.getParameter("UserName"));
		request.setAttribute("Email",request.getParameter("Email"));

		OperatorUser bt = new OperatorUser();
		ResultSet rs = null;

		String sql = "SELECT ID,UserName,Email FROM OperatorUser WHERE ID > 0";
		var = request.getParameter("ID");
		if(var != null && !var.equals(""))
		sql += " AND ID LIKE '%" + var + "%'";

		var = request.getParameter("UserName");
		if(var != null && !var.equals(""))
		sql += " AND UserName LIKE '%" + var + "%'";

		var = request.getParameter("Email");
		if(var != null && !var.equals(""))
		sql += " AND Email LIKE '%" + var + "%'";

		rs = bt.getResultSet(sql);
		int i = 0;
		while(rs.next()) {

			request.setAttribute("ID"+i,rs.getString("ID"));
			request.setAttribute("UserName"+i,rs.getString("UserName"));
			request.setAttribute("Email"+i,rs.getString("Email"));

			i++;
               	}

		in.addIterateValue(request,"List",i+"");

     		} catch (Exception e) {
      			if (log.isDebugEnabled()) log.debug("Exception Load error of: " + e.getMessage());
     		}
	}


public void promptAddStateRun(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	try {
		
		request.setAttribute("UserName",request.getParameter("UserName"));
		request.setAttribute("Password",request.getParameter("Password"));
		request.setAttribute("Passwordd",request.getParameter("Passwordd"));

		Country ct = new Country();
		in.addHashMapValues(request,"Country",ct.getValues());

		request.setAttribute("City",request.getParameter("City"));
		request.setAttribute("Address",request.getParameter("Address"));
		request.setAttribute("Email",request.getParameter("Email"));
		request.setAttribute("Tel",request.getParameter("Tel"));
		request.setAttribute("Zip",request.getParameter("Zip"));

		Language lg = new Language();
		in.addHashMapValues(request,"Language",lg.getValues());

     		} catch (Exception e) {
      			if (log.isDebugEnabled()) log.debug("Exception Load error of: " + e.getMessage());
     		}
	}

public void processAddStateRun(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
try {
	int id=0;
	String var = request.getParameter("Password");
	if(!var.equals(request.getParameter("Passwordd"))) throw new Exception("check two input password");
	else if ((request.getParameter("Password")).equals("") || (request.getParameter("Passwordd")).equals("")) 
		throw new Exception("Input password NULL");
	OperatorUser ou = new OperatorUser();
	id = ou.getNextID();

	String sql = "INSERT INTO OperatorUser(ID,UserName,Password,CountryID,City,Address,Email,Tel,Zip,LanguageID,States) VALUES(";
	sql += id+",";
	sql += "'"+request.getParameter("UserName")+"',";
	sql += "Password('"+request.getParameter("Password")+"'),";
	sql += "'"+request.getParameter("Country")+"',";
	sql += "'"+request.getParameter("City")+"',";
	sql += "'"+request.getParameter("Address")+"',";
	sql += "'"+request.getParameter("Email")+"',";
	sql += "'"+request.getParameter("Tel")+"',";
	sql += "'"+request.getParameter("Zip")+"',";
	sql += "'"+request.getParameter("Language")+"',";
	sql += "7)";

if (log.isDebugEnabled()) log.debug("SQL: " + sql);
	ou.execute(sql);
	response.sendRedirect(request.getContextPath()+"/OperatorUser.do?state=list");
     	} catch (Exception e) {

      		if (log.isDebugEnabled()) log.debug("Exception Load error of: " + e.getMessage());
		response.sendRedirect(request.getContextPath()+"/OperatorUser.do?state=promptAdd");
     	}
}

public void promptUpdateStateRun(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	try {
		String key = request.getParameter("Key");
		OperatorUser bt = new OperatorUser();
		ResultSet rs = null;
		String sql = "";
		
		if(key != null) {
			sql += "SELECT ID,UserName,CountryID,City,Address,Email,Tel,Zip,LanguageID FROM OperatorUser";
			sql += " WHERE ID = " + key + " LIMIT 1";
			rs = bt.getResultSet(sql);

			if(rs.first()) {
				request.setAttribute("ID",rs.getString("ID"));
				request.setAttribute("UserName",rs.getString("UserName"));
				request.setAttribute("Password",request.getParameter("Password"));
				request.setAttribute("Passwordd",request.getParameter("Passwordd"));

				Country ct = new Country();
				in.addHashMapValues(request,"Country",ct.getValues(),rs.getString("CountryID"));

				request.setAttribute("City",rs.getString("City"));
				request.setAttribute("Address",rs.getString("Address"));
				request.setAttribute("Email",rs.getString("Email"));
				request.setAttribute("Tel",rs.getString("Tel"));
				request.setAttribute("Zip",rs.getString("Zip"));

				Language lg = new Language();
				in.addHashMapValues(request,"Language",lg.getValues(),rs.getString("LanguageID"));
               		}

		}
		if (log.isDebugEnabled()) log.debug("SQL:"+sql);
     		} catch (Exception e) {
      			if (log.isDebugEnabled()) log.debug("Exception Load error of: " + e.getMessage());
     		}
	}

public void processUpdateStateRun(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	try {
		OperatorUser bt = new OperatorUser();
		ResultSet rs = null;

		String sql = "UPDATE OperatorUser SET ";
		sql += " UserName = '" + request.getParameter("UserName") + "',";
		sql += " Password = Password('" + request.getParameter("Password") + "'),";
		sql += " CountryID = " + request.getParameter("Country") + ",";
		sql += " City = '" + request.getParameter("City") + "',";
		sql += " Address = '" + request.getParameter("Address") + "',";
		sql += " Email = '" + request.getParameter("Email") + "',";
		sql += " Tel = '" + request.getParameter("Tel") + "',";
		sql += " Zip = '" + request.getParameter("Zip") + "',";
		sql += " LanguageID = " + request.getParameter("Language");
		sql += " WHERE ID = " + request.getParameter("ID");

		if (log.isDebugEnabled()) log.debug("SQL:"+sql);
		bt.execute(sql);
		response.sendRedirect(request.getContextPath()+"/OperatorUser.do?state=list");
     		} catch (Exception e) {
      			if (log.isDebugEnabled()) log.debug("Exception Load error of: " + e.getMessage());
			response.sendRedirect(request.getContextPath()+"/OperatorUser.do?state=promptUpdate");
     		}
	}


public void processDeleteStateRun(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	try {
		String key = request.getParameter("Key");
		OperatorUser bt = new OperatorUser();
		ResultSet rs = null;
		String sql = "";

		if(key != null) {
			sql += "DELETE FROM OperatorUser";
			sql += " WHERE ID = " + key;
			bt.execute(sql);
		    }

		if (log.isDebugEnabled()) log.debug("SQL:"+sql);
		response.sendRedirect(request.getContextPath()+"/OperatorUser.do?state=list");
     		} catch (Exception e) {
      			if (log.isDebugEnabled()) log.debug("Exception Load error of: " + e.getMessage());
			response.sendRedirect(request.getContextPath()+"/OperatorUser.do?state=list");
     		}
	}

}
