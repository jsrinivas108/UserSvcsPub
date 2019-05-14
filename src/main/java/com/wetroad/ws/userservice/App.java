package com.wetroad.ws.userservice;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wetroad.ws.userservice.japi.UserService;
import com.wetroad.ws.userservice.japi.vo.AppResponse;
import com.wetroad.ws.userservice.japi.vo.UserVO;

import spark.ResponseTransformer;
import spark.Spark;

/**
 *
 */
public class App 
{
	static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main( String[] args )
    {

		// http://localhost:8080/hello
    	// Need to write all the routes and transformation code here from HTTP to POJO
    	// route path pattern need to come from Spring config.
    	
    	/*
		 * /usersvcs/v1/rest/ is the base path /session - POST with body will create
		 * session /session/:sessionPK - GET without body will validate and extend
		 * session /session/:sessionPK - DELETE without body will delete the session
		 * 
		 * /response - POST with body will add a response
		 * 
		 */
    	ApplicationContext ac = new ClassPathXmlApplicationContext("japi/main-appCntxt.xml");
    	UserService us = (UserService) ac.getBean("userService");
		ResponseTransformer rt = (ResponseTransformer) ac.getBean("responseTransformer");

		ObjectMapper om = new ObjectMapper();

		Spark.port(9090);
		Spark.threadPool(200, 5, 600000);
		Spark.get("/user/:userPK", (req, res) ->
			{
				String requestId = req.headers("swsc-reqid");
				if (requestId == null)
			{
					requestId = UUID.randomUUID().toString();
				}
				String userPK = req.params(":userPK");
				AppResponse<UserVO> questionResponse = us.getUser(userPK);
				questionResponse.apiCallName = "getUser";
				questionResponse.requestId = requestId;
				Map<String, Object> ret = new HashMap<String, Object>();
				ret.put("RESPONSE", res);
				ret.put("APPRESPONSE", questionResponse);
				return ret;
			}, rt);

		Spark.put("/user/", (req, res) ->
		{
			logger.info("inside user put");
			if (req.contentLength() > 2048)
				throw new RuntimeException("content too large");
			String requestId = req.headers("swsc-reqid");
			if (requestId == null)
			{
				requestId = UUID.randomUUID().toString();
			}
			String bodyStr = req.body();
			UserVO userVO = om.readValue(bodyStr, UserVO.class);
			AppResponse<UserVO> createSession = us
					.createUser(userVO);
			createSession.apiCallName = "createUser";
			createSession.requestId = requestId;
			Map<String, Object> ret = new HashMap<String, Object>();
			ret.put("RESPONSE", res);
			ret.put("APPRESPONSE", createSession);
			return ret;
		}, rt);

		Spark.post("/user/", (req, res) ->
		{
			logger.info("inside user post");
			if (req.contentLength() > 2048)
				throw new RuntimeException("content too large");
			String requestId = req.headers("swsc-reqid");
			if (requestId == null)
			{
				requestId = UUID.randomUUID().toString();
			}
			String bodyStr = req.body();
			UserVO userVO = om.readValue(bodyStr, UserVO.class);
			AppResponse<UserVO> createSession = us
					.updateUser(userVO);
			createSession.apiCallName = "updateUser";
			createSession.requestId = requestId;
			Map<String, Object> ret = new HashMap<String, Object>();
			ret.put("RESPONSE", res);
			ret.put("APPRESPONSE", createSession);
			return ret;
		}, rt);

		Spark.get("/hello", (req, res) -> "Hello World");

		//TODO:  this does not work.. need to check
		new StopThread();

    }
}

class StopThread extends Thread
{
	private String stopFile = null;

	StopThread()
	{
		stopFile = System.getenv("stopFile");
		if (stopFile != null)
		{
			this.start();
		}
	}

	@Override
	public void run() {
		do
		{
			try
			{
				sleep(5000);
			}
			catch (InterruptedException e)
			{
				App.logger.warn("", e);
			}
		} while (!(new File(this.stopFile).exists()));
		Spark.stop();
	}
}