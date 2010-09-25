package com.mooo.mycoz.db;

import com.mooo.mycoz.util.PropertyManager;

public class DbFactory {
    private static String className = "com.mooo.mycoz.db.DbMysql";
    private static DbProcess factory = null;

	public static DbProcess getInstance() {
		String classNameProp = PropertyManager.getProperty("DbFactory.className");
		if (classNameProp != null) {
			className = classNameProp;
		}
		try {
			// Load the class and create an instance.
			Class<?> c = Class.forName(className);
			factory = (DbProcess) c.newInstance();
		} catch (Exception e) {
			System.err.println("Failed to load ForumFactory class " + className
					+ ". Yazd cannot function normally.");
			e.printStackTrace();
			return null;
		}
		return factory;
	}
	/*
    private static Object initLock = new Object();
    private static String className = "com.mooo.mycoz.db.DbMysql";
    private static DbAction factory = null;

	public static DbAction getInstance() {
		if (factory == null) {
			synchronized (initLock) {
				if (factory == null) {
					String classNameProp = PropertyManager.getProperty("DbFactory.className");
					if (classNameProp != null) {
						className = classNameProp;
					}
					try {
						// Load the class and create an instance.
						Class<?> c = Class.forName(className);
						factory = (DbAction) c.newInstance();
					} catch (Exception e) {
						System.err.println("Failed to load ForumFactory class "
								+ className
								+ ". Yazd cannot function normally.");
						e.printStackTrace();
						return null;
					}
				}
			}
		}
		return factory;
	}
	*/
}
