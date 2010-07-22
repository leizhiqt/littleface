package com.mooo.mycoz.dbobj.mycozShared;

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

import com.mooo.mycoz.dbobj.DBObject;




import com.mooo.mycoz.util.ActionServlet;

import com.mooo.mycoz.util.SAXParserConf;
import com.mooo.mycoz.util.ActionMap;


/**

 */
public class BlogType extends DBObject{

    private static Log log = LogFactory.getLog(BlogType.class);
    /**
     */
    public BlogType()
	throws SQLException{
	getConection("mycozShared");
	getStatement();
    } /* BlogType() */

    public int getNextID()
        throws SQLException {

		int id = 0;
		ResultSet rs = null;
		String sql = "SELECT MAX(ID) AS MaxId FROM BlogType";
		rs = getResultSet(sql);
		if(rs.first()) id=rs.getInt("MaxId")+1;

	return id;
    } /* getNextID() */

    public int getID(String name) {
	int id = 0;
	try{
		ResultSet rs = null;
		String sql = "SELECT ID FROM BlogType";
		sql += " WHERE Name='" + name+"'";

		rs = getResultSet(sql);
		if(rs.first()) id=rs.getInt("ID");

        } catch (SQLException sqlE) {
            	if (log.isDebugEnabled()) log.error("SQLException:" + sqlE.getMessage());
        } catch (Exception e) {
		if (log.isDebugEnabled()) log.error("Exception: " + e.getMessage());
	}

	return id;
    } /* getID(String) */

    public String getName(int id) {
        String var = null;
	try{
		ResultSet rs = null;
		String sql = "SELECT Name FROM BlogType";
		sql += " WHERE ID=" + id ;

		rs = getResultSet(sql);
		if(rs.first()) var = rs.getString("Name");

        } catch (SQLException sqlE) {
            	if (log.isDebugEnabled()) log.error("SQLException:" + sqlE.getMessage());
        } catch (Exception e) {
		if (log.isDebugEnabled()) log.error("Exception: " + e.getMessage());
	}

	return var;
    } /* getName(int) */

    public Hashtable getValues()
        throws SQLException {

	Hashtable< String, String> lTable = new Hashtable< String, String>();

	lTable.put("0","--select--");

	ResultSet rs = null;
	String sql = "SELECT ID,Name FROM BlogType WHERE ID > 0";
	rs = getResultSet(sql);
	while(rs.next()) {
		lTable.put(rs.getString("ID"),rs.getString("Name"));
               }

	return lTable;
    } /* getValues() */
/*
    public String getFiled(String str) {
       String var = null;
	ResultSet rs = null;
	String sql = null;
	try{

		//sql += " WHERE ID=" + id ;

		//rs = getResultSet(sql);
		//if(rs.first()) var = rs.getString("Name");

        } catch (SQLException sqlE) {
            	if (log.isDebugEnabled()) log.error("SQLException:" + sqlE.getMessage());
        } catch (Exception e) {
		if (log.isDebugEnabled()) log.error("Exception: " + e.getMessage());
	}

	return var;
    }*/ /* getFiled(String) */
  //  public CallableStatement procCall(String name)
    //    throws SQLException {

 	//CallableStatement cstmt = DBConection.prepareCall("{call BlogType_test(?,?)}");
	//cstmt.setString(0,"abcd");
	//cstmt.execute();
 	//cstmt.registerOutParameter(3, OracleTypes.CHAR);
   // } /* getValues() */
}
