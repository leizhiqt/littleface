package com.mooo.mycoz.dbobj;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mooo.mycoz.db.pool.DbConnectionManager;
import com.mooo.mycoz.db.sql.AbstractSQL;
import com.mooo.mycoz.util.BeanUtil;
import com.mooo.mycoz.util.StringUtils;

public class AbstractDb extends AbstractSQL implements DbAction{
	private static Log log = LogFactory.getLog(AbstractDb.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -4716776899444767709L;
	
	//private static final String CACHE_TYPE = "dbobj";
	//private static CacheManager cacheManager = CacheManager.getInstance();
/*
	public void addCache(String key, Object object){
		cacheManager.add(CACHE_TYPE, key, object);
	}

	public Object getCache(String key){
		return cacheManager.get(CACHE_TYPE, key);
	}
	*/
	public Class cls;

	public Class getCls() {
		return cls;
	}

	public void setCls(Class cls) {
		this.cls = cls;
	}

	public List<Object> searchAndRetrieveList(String sql, Class<?> obj) {
		List<Object> retrieveList = null;
		Statement stmt = null;
		ResultSetMetaData rsmd = null;
		ResultSet result = null;
		boolean closeCon = false;

		try {
			retrieveList = new ArrayList<Object>();

			if(connection == null || connection.isClosed()){
				connection = DbConnectionManager.getConnection();
				closeCon=true;
			}
			
			stmt = connection.createStatement();
			result = stmt.executeQuery(sql);
			
			rsmd = result.getMetaData();
			Object bean;

			while (result.next()) {
				bean = obj.newInstance();
				
				for (int i = 0; i < rsmd.getColumnCount(); i++) {
					BeanUtil.bindProperty(bean, StringUtils.prefixToUpper(rsmd.getColumnName(i + 1)),result.getString(i + 1), null);
				}
				retrieveList.add(bean);
			}

			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {
				if (result != null)
					result.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			try {
				if (connection != null && closeCon)
					connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		
		return retrieveList;
	}

	public List<Object> searchAndRetrieveList(String sql) {
		List<Object> retrieveList = null;
		Statement stmt = null;
		ResultSetMetaData rsmd = null;
		boolean closeCon = false;

		try {
			retrieveList = new ArrayList<Object>();
			
			if(connection == null || connection.isClosed()){
				connection = DbConnectionManager.getConnection();
				closeCon=true;
			}
			
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			
			rsmd = rs.getMetaData();
			Object bean;

			while (rs.next()) {
				bean = this.getClass().newInstance();
				for (int i = 0; i < rsmd.getColumnCount(); i++) {
					BeanUtil.bindProperty(bean, StringUtils.prefixToUpper(rsmd.getColumnName(i + 1)),rs.getString(i + 1), null);
				}
				retrieveList.add(bean);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			try {
				if (connection != null && closeCon)
					connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		
		return retrieveList;
	}
	
	public List<Object> searchAndRetrieveList() throws SQLException{

/*
		Object obj = getCache(doSql);

		if(obj != null) {
			retrieveList =  (List<Object>) obj;
		}
		
		if(retrieveList != null)
			return retrieveList;
	*/	
		List<Object> retrieveList = null;
		
		//setTable(StringUtils.upperToPrefix(this.getClass().getSimpleName(),null));

		String doSql = searchSQL();
		doSql += " LIMIT 10";
		System.out.println("doSql:"+doSql);

		Statement stmt = null;
		ResultSetMetaData rsmd = null;
		ResultSet result = null;
		boolean closeCon = false;

		try {
			retrieveList = new ArrayList<Object>();
			
			if (connection == null || connection.isClosed()) {
				connection = DbConnectionManager.getConnection();
				closeCon = true;
			}

			stmt = connection.createStatement();
			result = stmt.executeQuery(doSql);

			rsmd = result.getMetaData();
			Object bean;

			while (result.next()) {

				bean = cls.newInstance();

				for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {
					BeanUtil.bindProperty(bean,
							StringUtils.prefixToUpper(rsmd.getColumnName(i)),
							result.getString(i), null);
				}
				retrieveList.add(bean);
			}
			//addCache(doSql, retrieveList);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {
				if (result != null)
					result.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			try {
				if (connection != null && closeCon)
					connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return retrieveList;
	}

	public Integer count() throws SQLException{
		
		String doSql = countSQL();
		
		if(log.isDebugEnabled())log.debug("doSql="+doSql);
		
		Statement stmt = null;
		ResultSet result = null;
		boolean closeCon = false;
		int total=0;
		
		try {
			
			if(connection == null || connection.isClosed()){
				connection = DbConnectionManager.getConnection();
				closeCon=true;
			}
			
			stmt = connection.createStatement();
			result = stmt.executeQuery(doSql);
			
			if(result.next())
				total = result.getInt(1);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {
				if (result != null)
					result.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			try {
				if (connection != null && closeCon)
					connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return total;
	}
	
	public void add() throws SQLException {
		
		boolean closeCon = false;
		Statement stmt = null;
		try{
			if(connection == null || connection.isClosed()){
				connection = DbConnectionManager.getConnection();
				closeCon=true;
			}
			
			stmt = connection.createStatement();
			stmt.execute(addSQL());
		}finally {

			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			try {
				if (connection != null && closeCon)
					connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}
	
	public void delete() throws SQLException {
		Statement stmt = null;
		boolean closeCon = false;

		try{
			if(connection == null || connection.isClosed()){
				connection = DbConnectionManager.getConnection();
				closeCon=true;
			}
			
			stmt = connection.createStatement();
			stmt.execute(deleteSQL());

		}finally {

			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			try {
				if (connection != null && closeCon)
					connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public void update() throws SQLException{
		boolean closeCon = false;

		Statement stmt = null;
		try{
			if(connection == null || connection.isClosed()){
				connection = DbConnectionManager.getConnection();
				closeCon=true;
			}
			
			stmt = connection.createStatement();
			stmt.execute(updateSQL());
		}finally {

			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			try {
				if (connection != null && closeCon)
					connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void retrieve() throws SQLException{
		Statement stmt = null;
		ResultSetMetaData rsmd = null;
		ResultSet result = null;
		boolean closeCon = false;

		try{
			if(connection == null || connection.isClosed()){
				connection = DbConnectionManager.getConnection();
				closeCon=true;
			}
			
			stmt = connection.createStatement();
			result = stmt.executeQuery(searchSQL());
			
			rsmd = result.getMetaData();
			String value;
			while (result.next()) {
				for (int i = 0; i < rsmd.getColumnCount(); i++) {
					value = rsmd.getColumnName(i + 1);
					BeanUtil.bindProperty(this, StringUtils.prefixToUpper(value), result.getString(i + 1), null);					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();		
		}finally {

			try {
				if (result != null)
					result.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			try {
				if (connection != null && closeCon)
					connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
}