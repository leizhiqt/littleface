package com.mooo.mycoz.action.profile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mooo.mycoz.action.BaseSupport;


public class ProfileAction extends BaseSupport {

private static Log log = LogFactory.getLog(ProfileAction.class);

	public String index(HttpServletRequest request, HttpServletResponse response) {

		try {
			if (log.isDebugEnabled())
				log.debug("index ");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "success";
	}
}
