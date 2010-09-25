package com.mooo.mycoz.tools.test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mooo.mycoz.db.DbProcess;
import com.mooo.mycoz.db.DbFactory;
import com.mooo.mycoz.db.pool.DbConnectionManager;
import com.mooo.mycoz.dbobj.mycozBranch.Example;
import com.mooo.mycoz.util.Transaction;

public class MultiThread {
	private static Log log = LogFactory.getLog(MultiThread.class);

	private static final int maxInt = 2147483647;
	//private static final long maxLong = 9223372036854775807L;
	//private static final double maxDouble = 1.79769313486231570e+308;
	private static final int maxThread=800;

	private Thread[] threadPool;
	private int maxConnMSec;
	//private int maxConnPool=25;

	public MultiThread(int maxConns,double maxConnTime) {

		threadPool = new Thread[maxConns];
		
		Transaction tx = new Transaction();
		tx.start();
		tx.end();
		
		//maxConnMSec = (int) (maxConnTime * 86400000.0); // 86400 sec/day
		maxConnMSec = (int) (maxConnTime * 1000.0);		// loop 1 seconds.
		//maxConnMSec = (int) (maxConnTime * 3000.0);		// loop 1 seconds.

		if (maxConnMSec < 30000) { // Recycle no less than 30 seconds.
			maxConnMSec = 30000;
		}
		
		System.out.println("^_^ 服务器启动  ^_^");
		long startTime = System.currentTimeMillis();
		System.out.println("startTime:"+ startTime);

		try {
			//check init
			if(maxConns > maxThread)
				maxConns=maxThread;
			
			// create threadPool
			//DoThread doThread = new DoThread();
			for (int i = 0; i < maxConns;i++) {
				
				//threadPool[i] = new Thread(doThread);
				threadPool[i] = new Thread(new DoThread());

				if(log.isDebugEnabled()) log.debug("create thread["+i+"]");
				threadPool[i].start();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("create threadPool error:"+e.getMessage());
		}
		
		try {
			boolean forever = true;
			
			while (forever) {
				try {
					
//					System.out.println("name \tPriority \tisAlive \tState \tisInterrupted \tisDaemon");
//					System.out.println(threadPool[i].getName() + "\t" + threadPool[i].getPriority() +
//							"\t" + threadPool[i].isAlive() + "\t" + threadPool[i].getState() + 
//							"\t" + threadPool[i].isInterrupted() +
//							"\t" + threadPool[i].isDaemon());
					
					int runCount=0;
					
					for (int i= 0; i < maxConns; i++) {
						if(threadPool[i].isAlive())
							runCount++;
					}
					System.out.println("=======================运行线程数===================:"+runCount);
					
					if(runCount == 0){
						break;
					}
					
					//wait(5000);
					Thread.sleep(5000); // sleep 5 seconds.
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//System.out.println("=======================LOOP Watch===================");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception:"+e.getMessage());
		}
		long finishTime = System.currentTimeMillis();
		long hours = (finishTime - startTime) / 1000 / 60 / 60;
		long minutes = (finishTime - startTime) / 1000 / 60 - hours * 60;
		long seconds = (finishTime - startTime) / 1000 - hours * 60 * 60 - minutes * 60;
		
		System.out.println(finishTime - startTime);
		System.out.println("expends:   " + hours + ":" + minutes + ":" + seconds);
		
		System.out.println("^_^  服务器停止 ^_^");
	}

	public static void main(String[] args) throws IOException {
		new MultiThread(8,10);
	}
	
	class DoThread implements Runnable  {
		private Object initLock = new Object();
		
		private DbProcess dbAction;
		
		private long createTime;

		public DoThread(){
			createTime = System.currentTimeMillis();
			dbAction = DbFactory.getInstance();
		}
		
		public void run() {
			boolean forever = true;
			while (forever) {
					synchronized (initLock) {
						//writeJDBC();
						writeTransaction();
						long age = System.currentTimeMillis() - createTime;
						if (age > maxConnMSec) { // Force a reset at the max
							System.out.println("===超时 退出=====");
							break;
						}
					} //synchronized end
			} // loop run
		} // end run

		public void writeJDBC(){
			Connection connection = null;
			Statement stmt = null;
			String sql = null;
			try {
				//mypool
				connection = DbConnectionManager.getConnection();
				connection.setAutoCommit(false);
				
				System.out.println("打开连接-------------");
				System.out.println(connection);
				
				stmt = connection.createStatement();
				sql = "INSERT INTO Example(id,name) VALUES(";
				sql += (new Random().nextDouble() * maxInt)+",";
				sql += "'"+(new Random().nextDouble() * maxInt) + "名称"+"'";
				sql += ")";
			

				stmt.execute(sql);
				System.out.println("commit");
				connection.commit();
				
			}catch (SQLException e) {
				e.printStackTrace();
				try {
					connection.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				System.out.println("SQLException: " + e.getMessage());

			}catch (Exception e) {
				e.printStackTrace();
				try {
					connection.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				System.out.println("Exception: " + e.getMessage());

			} finally {
				try {
					stmt.close();
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
		}
		
		public void writeTransaction (){
			Transaction tx = new Transaction();
			try {
				tx.start();
				
				Example ex = new Example();
				ex.setId(new Random().nextDouble() * maxInt);
				ex.setName(new Random().nextDouble() * maxInt + "名称");
				
				dbAction.add(tx.getConnection(),ex);
				
				tx.commit();
			}catch (SQLException e) {
				e.printStackTrace();
				if(log.isDebugEnabled()) log.debug("SQLException:"+e.getMessage());
				System.out.println("SQLException:"+e.getMessage());
				tx.rollback();
			}catch (Exception e) {
				e.printStackTrace();
				if(log.isDebugEnabled()) log.debug("Exception:"+e.getMessage());
				System.out.println("Exception:"+e.getMessage());
				tx.rollback();
			}finally {
				tx.end();
			}
			
		}
	}
}