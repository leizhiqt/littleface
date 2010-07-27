package com.mooo.mycoz.dbobj;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;

import com.mooo.mycoz.db.pool.DbConnectionManager;
import com.mooo.mycoz.db.sql.DbBulildSQL;
import com.mooo.mycoz.util.ReflectUtil;

public class DBSession extends DbBulildSQL {

	private static Object initLock = new Object();
	private static DBSession factory;

	public Connection connection;
	public Connection conn;
	
	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public static DBSession getInstance() {
		if (factory == null) {
			synchronized (initLock) {
				try {
					factory = new DBSession();
				} catch (Exception e) {
					System.err.println("Exception DbSession." + e.getMessage());
					e.printStackTrace();
					return null;
				}
			}
		}
		return factory;
	}

	public void save(Object bean) throws SQLException{
		try {

			List<String> methods = ReflectUtil.getMethodNames(bean.getClass());
			
			setTable(bean.getClass().getSimpleName());
			
			String method;
			String field;
			for (Iterator<String> it = methods.iterator(); it.hasNext();) {

				method = it.next();
				
				if(method.indexOf("get")==0){
					
					Method getMethod;
					getMethod = bean.getClass().getMethod(method);
					
					//Class<?> cl = getMethod.getReturnType();
					//System.out.println("getReturnType="+cl.getName());
					Object obj = getMethod.invoke(bean);
					
					if(obj !=null) {
						field = method.substring(method.indexOf("get")+3).toLowerCase();
						setField(field, obj.toString());
					}
				}
			}
			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Statement stmt = null;
		try{
			connection = DbConnectionManager.getConnection();
			if(connection!=null){
				stmt = connection.createStatement();
			}else{
				if(conn==null)
				conn=DbConnectionManager.getConnection();
				stmt = conn.createStatement();
			}
			stmt.executeUpdate(AddSQL());
		}finally {

			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}
}
