package com.mooo.mycoz.action.operation;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mooo.mycoz.action.BaseSupport;
import com.mooo.mycoz.db.pool.DbConnectionManager;
import com.mooo.mycoz.dbobj.mycozBranch.FileInfo;
import com.mooo.mycoz.dbobj.mycozShared.LinearCode;
import com.mooo.mycoz.util.FileUtil;
import com.mooo.mycoz.util.IDGenerator;
import com.mooo.mycoz.util.UploadFile;

public class FileAction extends BaseSupport {
	private static Log log = LogFactory.getLog(FileAction.class);
	private static final String INSERT_FILE="INSERT INTO FileInfo(id,typeid,name,datetime,filePath) VALUES(?,?,?,?,?)";
	 
	public String list(HttpServletRequest request, HttpServletResponse response) {
		Connection connection=null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			if (log.isDebugEnabled()) log.debug("list");

			long fileBit = 0;
			double fileK = 0.0;
			double fileM = 0.0;
			String uploadPath = request.getRealPath("/")+"upload/";
			
			String value = null;
			String sql = null;
			File file = null;

			DecimalFormat df = new DecimalFormat("###0.00");

			//ResultSet rs = null;

			sql = "SELECT  fi.*,lc.name FROM FileInfo fi,mycozShared.LinearCode lc,mycozShared.CodeType ct WHERE fi.typeId=lc.id AND lc.typeId=ct.id AND ct.id=1";

			value = request.getParameter("Key");
			
			if (value != null && !value.equals("")) {
				sql += " AND dt.ID=" + value;
			}


			List files = new ArrayList();
			
			//List downs = down.searchAndRetrieveList();
			connection = DbConnectionManager.getConnection();
			stmt = connection.createStatement();

			rs = stmt.executeQuery(sql);
			FileInfo fi;
			File checkFile;
			while (rs.next()) {
				fi = new FileInfo();
				fi.setId(rs.getInt("fi.id"));
				fi.setTypename(rs.getString("lc.name"));
				fi.setName(rs.getString("fi.name"));
				fi.setDatetime(rs.getString("fi.datetime"));
				value = rs.getString("fi.filePath");
				fi.setFilepath(value);
				
				checkFile = new File(uploadPath + value);
				
				if (log.isDebugEnabled()) log.debug("checkFile = "+(checkFile.exists()));

				if (!checkFile.exists()){
					/*
					Blob fileBlob = rs.getBlob("fi.file");
					InputStream in = fileBlob.getBinaryStream();
					FileOutputStream out = new FileOutputStream(checkFile);
					int len=(int)fileBlob.length();
				     byte[] buffer=new byte[len];  // 建立缓冲区
				     while((len=in.read(buffer)) != -1){
				    	 out.write(buffer,0,len);
				     }
				     out.close();      
				     in.close();
				     */
				}
				
				fileBit = checkFile.length();
				fileK = fileBit / 1024;
				fileM = fileK / 1024;
				fi.setSize(df.format(fileM) + "M");
				
				files.add(fi);
			}
			
			request.setAttribute("files", files);
		} catch (SQLException e) {
			if (log.isDebugEnabled())
				log.debug("Exception Load error of: " + e.getMessage());
		} catch (Exception e) {
			if (log.isDebugEnabled())
				log.debug("Exception Load error of: " + e.getMessage());
		}finally {
			try {
				if(rs != null)
					rs.close();
				if(stmt != null)
					stmt.close();
				if(connection != null)
					connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		return "success";
	}

	public String promptUpload(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			if (log.isDebugEnabled()) log.debug("promptUpload");
			
			Map fileTypes = new HashMap();
			
			LinearCode lc = new LinearCode();
			lc.setCatalog("mycozShared");
			lc.setTypeid(1);
			LinearCode bean;
			List cts = lc.searchAndRetrieveList();

			for(Iterator it = cts.iterator(); it.hasNext();){
				bean = (LinearCode)it.next();
				fileTypes.put(bean.getId(), bean.getName());
			}
			
			request.setAttribute("fileTypes", fileTypes );

		} catch (Exception e) {
			if (log.isDebugEnabled())
				log.debug("Exception Load error of: " + e.getMessage());
		}
		return "success";
	}

	public String processUpload(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			if (log.isDebugEnabled()) log.debug("processUpload");
			    
			    	String value ="";
			    	String uploadDirectory = "upload/";
			    	String tmpDirectory = "tmp/";
			    	String uploadPath = request.getRealPath("/")+uploadDirectory;
			    	String tmpPath = request.getRealPath("/")+tmpDirectory;
			    	
					if (log.isDebugEnabled()) log.debug("uploadPath="+uploadPath);

				    File tmpFile = new File(tmpPath);
				    File uploadFile = new File(uploadPath);

				       if (!tmpFile.exists()) {
				    	   tmpFile.mkdirs();
				       }
				       
				       if (!uploadFile.exists()) {
				    	   uploadFile.mkdirs();
				       }
				       
			    	UploadFile uf = new UploadFile();
			    	uf.setRequest(request);
			    	uf.setUploadPath(tmpPath);
			    	uf.process();

			    	value = uf.getParameter("name").trim();
			    	
					if (log.isDebugEnabled()) log.debug("name:"+value);

			    	if (value==null || value.equals("")) 
			    		throw new Exception("Input Name NULL");
			    	
					//DBSession session = DBSession.getInstance();
			        //Finally, delete the forum itself and all permissions and properties
			        //associated with it.
			        Connection con = null;
			        PreparedStatement pstmt = null;
			        try {
			            con = DbConnectionManager.getConnection();
			            pstmt = con.prepareStatement(INSERT_FILE);
			            pstmt.setInt(1,new Integer(IDGenerator.getNextID("FileInfo")));
			            pstmt.setInt(2, new Integer(uf.getParameter("typeid").trim()));
			            pstmt.setString(3, uf.getParameter("name").trim());
						if (log.isDebugEnabled()) log.debug("date="+uf.getParameter("date").trim());

						Date inDate = new Date(uf.getParameter("date").trim());
						java.sql.Timestamp oDate= new java.sql.Timestamp(inDate.getTime());
						
						if (log.isDebugEnabled()) log.debug("oDate="+oDate);

			            pstmt.setTimestamp(4, oDate);
			            
				    	Iterator<?> fileList = uf.getFileIterator();
				    	int i=0;
				    	while(fileList.hasNext()){
				    		value = (String)fileList.next();
				            pstmt.setString(5, value);
				    		i++;
				    		}
				    	
						/*if (log.isDebugEnabled()) log.debug("file="+uploadPath+value);

				    	File inputFile = new File(uploadPath+value);
				    	FileInputStream fis = new FileInputStream(inputFile);
				    	
						if (log.isDebugEnabled()) log.debug("file="+uploadPath+value);

			            pstmt.setBinaryStream(6, fis, fis.available());
						 */
			            pstmt.execute();
			            pstmt.close();
			            
			            FileUtil.copy(new File(tmpPath+value), new File(uploadPath+value), true);
			            
			            //User perms
			            //pstmt = con.prepareStatement(DELETE_FORUM_USER_PERMS);
			            //pstmt.setInt(1,forum.getID());
			            //pstmt.execute();
			            //pstmt.close();
			        }
			        catch( SQLException sqle ) {
			            System.err.println("Error in sqle:" + sqle);
			        }
			        finally {
			            try {  pstmt.close(); }
			            catch (Exception e) { e.printStackTrace(); }
			            try {  con.close();   }
			            catch (Exception e) { e.printStackTrace(); }
			        }
			        
	           System.out.print("upload succeed");
		} catch (Exception e) {
			if (log.isDebugEnabled())
				log.debug("Exception Load error of: " + e.getMessage());
			return "promptUpload";
		}
		return "success";
	}

	public String processDelete(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			if (log.isDebugEnabled()) log.debug("processDeleteCode");
	    	String uploadDirectory = "upload/";
	    	String uploadPath = request.getRealPath("/")+uploadDirectory;
	    	
			String[] ids =  request.getParameterValues("id");
			
			for(int i=0;i<ids.length;i++){
				if (log.isDebugEnabled()) log.debug("ids="+ids[i]);
				FileInfo bean = new FileInfo();
				bean.setId( new Integer(ids[i]));
				bean.retrieve();
				
		    	File file = new File(uploadPath+bean.getFilepath());
				if (log.isDebugEnabled()) log.debug("filePath="+uploadPath+bean.getFilepath());

		    	if(file.exists())
		    		file.delete();
		    	
				bean.delete();
			}
		} catch (Exception e) {
			if (log.isDebugEnabled())
				log.debug("Exception Load error of: " + e.getMessage());
		}
		return "list";
	}

	public String processDownload(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			if (log.isDebugEnabled()) log.debug("processDeleteCode");
	    	String uploadDirectory = "upload/";
	    	String uploadPath = request.getRealPath("/")+uploadDirectory;
	    	
			String[] ids =  request.getParameterValues("id");
			
			for(int i=0;i<ids.length;i++){
				if (log.isDebugEnabled()) log.debug("ids="+ids[i]);
				FileInfo bean = new FileInfo();
				bean.setId( new Integer(ids[i]));
				bean.retrieve();
				
		    	File file = new File(uploadPath+bean.getFilepath());
		    	//response.setContentType("*.*");
		    	
				if (log.isDebugEnabled()) log.debug("filePath="+uploadPath+bean.getFilepath());
			}

		} catch (Exception e) {
			if (log.isDebugEnabled())
				log.debug("Exception Load error of: " + e.getMessage());
		}
		return "list";
	}
}