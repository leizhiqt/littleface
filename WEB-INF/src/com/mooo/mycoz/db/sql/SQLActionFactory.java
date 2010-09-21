package com.mooo.mycoz.db.sql;

import com.mooo.mycoz.util.PropertyManager;

public abstract class SQLActionFactory implements SQLAction{
	
    private static Object initLock = new Object();
    private static String className = "com.mooo.mycoz.db.sql.MysqlAction";
    private static SQLAction factory = null;

	public static SQLAction getInstance() {
		if (factory == null) {
			synchronized (initLock) {
				if (factory == null) {
					String classNameProp = PropertyManager.getProperty("SQLActionFactory.className");
					if (classNameProp != null) {
						className = classNameProp;
					}
					try {
						// Load the class and create an instance.
						Class<?> c = Class.forName(className);
						factory = (SQLAction) c.newInstance();
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

}